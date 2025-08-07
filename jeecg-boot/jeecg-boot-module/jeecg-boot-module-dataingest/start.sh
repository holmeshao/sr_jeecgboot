#!/bin/bash

# 数据接入微服务启动脚本

echo "=========================================="
echo "    数据接入微服务启动脚本"
echo "=========================================="

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境，请先安装Java 17"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt "17" ]; then
    echo "错误: Java版本过低，需要Java 17或更高版本"
    exit 1
fi

echo "Java版本检查通过: $(java -version 2>&1 | head -n 1)"

# 检查Maven环境
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven环境，请先安装Maven"
    exit 1
fi

echo "Maven版本: $(mvn -version | head -n 1)"

# 设置环境变量
export JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -Dfile.encoding=UTF-8"
export SPRING_PROFILES_ACTIVE=dev

echo "环境变量设置完成"
echo "JAVA_OPTS: $JAVA_OPTS"
echo "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"

# 创建必要的目录
mkdir -p logs
mkdir -p data

echo "目录创建完成"

# 编译项目
echo "开始编译项目..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "错误: 项目编译失败"
    exit 1
fi

echo "项目编译成功"

# 检查jar包是否存在
JAR_FILE="target/jeecg-boot-module-dataingest.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "错误: 未找到jar包: $JAR_FILE"
    exit 1
fi

echo "找到jar包: $JAR_FILE"

# 启动服务
echo "开始启动数据接入微服务..."
echo "服务端口: 7009"
echo "访问地址: http://localhost:7009/dataingest"
echo "健康检查: http://localhost:7009/dataingest/health"
echo ""

nohup java $JAVA_OPTS -jar $JAR_FILE > logs/startup.log 2>&1 &

# 获取进程ID
PID=$!
echo "服务启动中，进程ID: $PID"

# 等待服务启动
echo "等待服务启动..."
sleep 10

# 检查服务状态
if curl -f http://localhost:7009/dataingest/health > /dev/null 2>&1; then
    echo "=========================================="
    echo "    数据接入微服务启动成功！"
    echo "=========================================="
    echo "服务端口: 7009"
    echo "访问地址: http://localhost:7009/dataingest"
    echo "健康检查: http://localhost:7009/dataingest/health"
    echo "进程ID: $PID"
    echo "日志文件: logs/startup.log"
    echo ""
    echo "常用命令:"
    echo "  查看日志: tail -f logs/startup.log"
    echo "  停止服务: kill $PID"
    echo "  重启服务: ./restart.sh"
else
    echo "错误: 服务启动失败，请检查日志文件: logs/startup.log"
    exit 1
fi 