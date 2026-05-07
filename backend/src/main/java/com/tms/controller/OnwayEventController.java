package com.tms.controller;

import com.tms.common.PageResult;
import com.tms.common.Result;
import com.tms.dto.OnwayEventDTO;
import com.tms.entity.OnwayEvent;
import com.tms.service.OnwayEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在途事件Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/onway-event")
public class OnwayEventController {

    private final OnwayEventService onwayEventService;

    @Autowired
    public OnwayEventController(OnwayEventService onwayEventService) {
        this.onwayEventService = onwayEventService;
    }

    /**
     * 创建在途事件
     *
     * @param dto 在途事件DTO
     * @return 创建结果
     */
    @PostMapping
    public Result Result<OnwayEventDTO> createEvent(@Valid @RequestBody OnwayEventDTO dto) {
        OnwayEvent event = onwayEventService.createEvent(dto);
        return Result.success(convertToDTO(event));
    }

    /**
     * 更新在途事件
     *
     * @param id  在途事件ID
     * @param dto 在途事件DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result Result<OnwayEventDTO> updateEvent(@PathVariable Long id,
                                             @Valid @RequestBody OnwayEventDTO dto) {
        OnwayEvent event = onwayEventService.updateEvent(id, dto);
        return Result.success(convertToDTO(event));
    }

    /**
     * 删除在途事件
     *
     * @param id 在途事件ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteEvent(@PathVariable Long id) {
        onwayEventService.deleteEvent(id);
        return Result.success();
    }

    /**
     * 根据ID查询在途事件
     *
     * @param id 在途事件ID
     * @return 在途事件信息
     */
    @GetMapping("/{id}")
    public Result Result<OnwayEventDTO> getEventById(@PathVariable Long id) {
        return onwayEventService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "事件不存在"));
    }

    /**
     * 根据调度单ID查询事件列表
     *
     * @param dispatchId 调度单ID
     * @return 事件列表
     */
    @GetMapping("/by-dispatch/{dispatchId}")
    public Result<List<List<OnwayEventDTO>> getEventsByDispatch(@PathVariable Long dispatchId) {
        List List<OnwayEventDTO> list = onwayEventService.findByDispatchId(dispatchId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 分页查询事件列表
     *
     * @param dispatchId 调度单ID
     * @param eventType  事件类型
     * @param eventLevel 事件级别
     * @param isHandled  处理状态
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result Result<PageResultResult<OnwayEventDTO>> getEventList(
            @RequestParam(required = false) Long dispatchId,
            @RequestParam(required = false) Integer eventType,
            @RequestParam(required = false) Integer eventLevel,
            @RequestParam(required = false) Integer isHandled,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page Page<OnwayEvent> page = onwayEventService.findEvents(dispatchId, eventType, eventLevel, isHandled, pageable);

        List List<OnwayEventDTO> list = page.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Result.success(new PageResult<>(pageNum, pageSize, page.getTotalElements(), list));
    }

    /**
     * 处理事件
     *
     * @param id             事件ID
     * @param params         包含handleResult的请求参数
     * @param authentication 当前用户认证信息
     * @return 操作结果
     */
    @PostMapping("/{id}/handle")
    public Result Result<OnwayEventDTO> handleEvent(@PathVariable Long id,
                                             @RequestBody Map<String, String> params,
                                             Authentication authentication) {
        String handleResult = params.get("handleResult");
        String handleBy = authentication.getName();
        OnwayEvent event = onwayEventService.handleEvent(id, handleResult, handleBy);
        return Result.success(convertToDTO(event));
    }

    /**
     * 获取未处理事件数量
     *
     * @return 未处理事件数量
     */
    @GetMapping("/unhandled-count")
    public Result<Map<String, Long>> getUnhandledCount() {
        long count = onwayEventService.countUnhandledEvents();
        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return Result.success(result);
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 在途事件实体
     * @return 在途事件DTO
     */
    private OnwayEventDTO convertToDTO(OnwayEvent entity) {
        OnwayEventDTO dto = new OnwayEventDTO();
        dto.setId(entity.getId());
        dto.setDispatchId(entity.getDispatchId());
        dto.setWaybillId(entity.getWaybillId());
        dto.setEventType(entity.getEventType());
        dto.setEventLevel(entity.getEventLevel());
        dto.setEventContent(entity.getEventContent());
        dto.setLocation(entity.getLocation());
        dto.setEventTime(entity.getEventTime());
        dto.setIsHandled(entity.getIsHandled());
        dto.setHandleResult(entity.getHandleResult());
        dto.setHandleTime(entity.getHandleTime());
        dto.setHandleBy(entity.getHandleBy());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}
