#!/bin/bash

# TMS运输管理系统打包脚本
# 打包流程：清理 -> 构建前端 -> 复制前端到后端 -> 打包后端

set -e

echo "======================================"
echo "TMS运输管理系统打包脚本"
echo "======================================"

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# 1. 清理历史打包文件
echo ""
echo "[1/5] 清理历史打包文件..."
rm -rf backend/target
rm -rf backend/src/main/resources/static
rm -rf frontend/dist
rm -rf ui-tests/test-results
rm -rf ui-tests/playwright-report

# 2. 构建前端项目
echo ""
echo "[2/5] 构建前端项目..."
cd frontend
npm install
npm run build
cd ..

# 3. 复制前端打包文件到后端
echo ""
echo "[3/5] 复制前端资源到后端..."
mkdir -p backend/src/main/resources/static
cp -r frontend/dist/* backend/src/main/resources/static/

# 4. 打包后端项目
echo ""
echo "[4/5] 打包后端项目..."
cd backend
mvn clean package -DskipTests
cd ..

# 5. 复制最终jar包到根目录
echo ""
echo "[5/5] 复制最终jar包..."
if [ -f backend/target/tms-backend-1.0.0.jar ]; then
    cp backend/target/tms-backend-1.0.0.jar tms-backend.jar
    echo "打包成功: tms-backend.jar"
else
    echo "错误: 未找到打包后的jar文件"
    exit 1
fi

echo ""
echo "======================================"
echo "打包完成！"
echo "输出文件: tms-backend.jar"
echo "启动命令: java -jar tms-backend.jar"
echo "======================================"
