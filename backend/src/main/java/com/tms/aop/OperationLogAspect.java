package com.tms.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.entity.OperationLog;
import com.tms.repository.OperationLogRepository;
import com.tms.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 操作日志切面
 * 使用AOP记录用户操作日志
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    private final OperationLogRepository operationLogRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public OperationLogAspect(OperationLogRepository operationLogRepository, ObjectMapper objectMapper) {
        this.operationLogRepository = operationLogRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * 定义切点：所有Controller层的公共方法
     */
    @Pointcut("execution(* com.tms.controller.*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 环绕通知：记录操作日志
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();

        // 排除登录接口的记录（避免记录密码）
        if (requestUrl.contains("/auth/login")) {
            return joinPoint.proceed();
        }

        // 获取当前用户
        Long userId = null;
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userId = userDetails.getUserId();
            username = userDetails.getUsername();
        }

        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();

        // 获取操作类型和描述
        String operationType = getOperationType(requestMethod);
        String operationDesc = className + "." + methodName;

        // 获取请求参数
        String requestParams = getRequestParams(joinPoint);

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 执行目标方法
        Object result = null;
        boolean isSuccess = true;
        String errorMsg = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            isSuccess = false;
            errorMsg = e.getMessage();
            throw e;
        } finally {
            // 计算耗时
            long costTime = System.currentTimeMillis() - startTime;

            // 获取响应结果（简化记录）
            String responseResult = isSuccess ? "成功" : "失败";

            // 获取IP地址
            String ipAddress = getIpAddress(request);

            // 保存操作日志
            try {
                OperationLog operationLog = new OperationLog();
                operationLog.setUserId(userId);
                operationLog.setUsername(username);
                operationLog.setOperationType(operationType);
                operationLog.setOperationDesc(operationDesc);
                operationLog.setRequestUrl(requestUrl);
                operationLog.setRequestParams(requestParams);
                operationLog.setResponseResult(responseResult);
                operationLog.setIpAddress(ipAddress);
                operationLog.setCostTime((int) costTime);
                operationLog.setIsSuccess(isSuccess ? 1 : 0);
                operationLog.setErrorMsg(errorMsg);
                operationLog.setCreateTime(LocalDateTime.now());

                operationLogRepository.save(operationLog);
            } catch (Exception e) {
                logger.error("保存操作日志失败", e);
            }
        }
    }

    /**
     * 根据HTTP方法获取操作类型
     *
     * @param method HTTP方法
     * @return 操作类型
     */
    private String getOperationType(String method) {
        switch (method.toUpperCase()) {
            case "POST":
                return "新增";
            case "PUT":
                return "修改";
            case "DELETE":
                return "删除";
            case "GET":
                return "查询";
            default:
                return "其他";
        }
    }

    /**
     * 获取请求参数
     *
     * @param joinPoint 连接点
     * @return 请求参数JSON字符串
     */
    private String getRequestParams(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return "{}";
            }
            // 只记录第一个参数（通常是DTO）
            Object arg = args[0];
            if (arg == null) {
                return "{}";
            }
            // 过滤敏感字段
            String json = objectMapper.writeValueAsString(arg);
            // 限制长度
            if (json.length() > 2000) {
                json = json.substring(0, 2000) + "...";
            }
            return json;
        } catch (Exception e) {
            return "{}";
        }
    }

    /**
     * 获取IP地址
     *
     * @param request HTTP请求
     * @return IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
