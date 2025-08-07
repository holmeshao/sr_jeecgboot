#!/bin/bash

# 数据接入微服务停止脚本

echo "=========================================="
echo "    数据接入微服务停止脚本"
echo "=========================================="

# 查找服务进程
PID=$(ps -ef | grep "jeecg-boot-module-dataingest.jar" | grep -v grep | awk '{print $2}')

if [ -z "$PID" ]; then
    echo "服务未运行"
    exit 0
fi

echo "找到服务进程，PID: $PID"

# 优雅停止
echo "正在停止服务..."
kill $PID

# 等待进程结束
for i in {1..30}; do
    if ! ps -p $PID > /dev/null; then
        echo "服务已停止"
        exit 0
    fi
    echo "等待服务停止... ($i/30)"
    sleep 1
done

# 强制停止
echo "服务未响应，强制停止..."
kill -9 $PID

if ! ps -p $PID > /dev/null; then
    echo "服务已强制停止"
else
    echo "错误: 无法停止服务"
    exit 1
fi 