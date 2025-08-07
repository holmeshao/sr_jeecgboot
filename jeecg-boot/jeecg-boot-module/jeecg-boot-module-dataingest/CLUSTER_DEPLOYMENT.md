# DataIngest 集群部署指南

## 架构优化说明

### 原始架构问题
1. **数据孤岛**: 每个服务实例维护独立的 `ConcurrentHashMap`，无法感知其他节点状态
2. **任务重复**: 多个节点可能同时启动相同的CDC任务
3. **负载不均**: 无法实现任务在集群间的负载均衡  
4. **故障转移**: 节点宕机后任务无法自动迁移

### Redis集群化改造优势

#### 1. 状态共享
```yaml
# Redis中的数据结构
debezium:task:config:{taskId}     # 任务配置
debezium:task:status:{taskId}     # 任务状态
debezium:task:assignment:{taskId} # 任务分配关系
debezium:node:heartbeat:{nodeId}  # 节点心跳
debezium:cluster:tasks            # 集群任务集合
debezium:lock:task:{taskId}       # 分布式锁
```

#### 2. 负载均衡
- 使用分布式锁确保一个任务只在一个节点运行
- 支持任务在节点间自动分配
- 新节点加入时自动参与负载均衡

#### 3. 故障转移
- 心跳机制检测节点存活状态
- 自动检测并接管孤儿任务
- 60秒故障转移时间窗口

## 集群部署配置

### 1. Redis配置
```yaml
# application-cluster.yml
spring:
  redis:
    host: redis-cluster.example.com
    port: 6379
    password: your-redis-password
    timeout: 5000ms
    lettuce:
      pool:
        max-active: 20
        max-idle: 10
        min-idle: 2
```

### 2. 应用配置
```yaml
# 每个节点需要不同的端口
server:
  port: 8080  # 节点1
  # port: 8081  # 节点2  
  # port: 8082  # 节点3

# 集群配置
dataingest:
  cluster:
    enabled: true
    heartbeat-interval: 10s
    task-monitor-interval: 60s
    failover-timeout: 60s
```

### 3. Docker Compose 集群示例
```yaml
version: '3.8'
services:
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    command: redis-server --requirepass your-redis-password

  dataingest-node1:
    image: jeecg-dataingest:latest
    ports:
      - "8080:8080"
    environment:
      - SERVER_PORT=8080
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PASSWORD=your-redis-password
    depends_on:
      - redis

  dataingest-node2:
    image: jeecg-dataingest:latest
    ports:
      - "8081:8080"
    environment:
      - SERVER_PORT=8080
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PASSWORD=your-redis-password
    depends_on:
      - redis

  dataingest-node3:
    image: jeecg-dataingest:latest
    ports:
      - "8082:8080"
    environment:
      - SERVER_PORT=8080
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PASSWORD=your-redis-password
    depends_on:
      - redis
```

## API使用示例

### 1. 启动集群任务
```bash
curl -X POST http://any-node:8080/dataingest/cluster/task/task001/start \
  -H "Content-Type: application/json" \
  -d '{
    "taskName": "MySQL CDC任务",
    "taskType": "MYSQL_CDC",
    "dataSourceConfigs": [...],
    "cdcTables": [...]
  }'
```

### 2. 查看任务分配
```bash
curl http://any-node:8080/dataingest/cluster/tasks
```

### 3. 手动故障转移
```bash
curl -X POST http://any-node:8080/dataingest/cluster/task/task001/failover
```

## 监控和运维

### 1. 任务状态监控
```bash
# 查看所有任务状态
curl http://node1:8080/dataingest/cluster/tasks | jq '.[].status'

# 查看特定任务详情
curl http://node1:8080/dataingest/cluster/task/task001/status
```

### 2. 节点健康检查
```bash
# 查看集群节点
curl http://node1:8080/dataingest/cluster/nodes

# Redis中查看心跳
redis-cli KEYS "debezium:node:heartbeat:*"
redis-cli GET "debezium:node:heartbeat:192.168.1.10:8080"
```

### 3. 故障恢复场景

#### 场景1: 单节点宕机
- 其他节点60秒内自动检测并接管任务
- 无需人工干预

#### 场景2: Redis故障
- 所有节点失去协调能力
- 需要先恢复Redis，然后重启应用节点

#### 场景3: 网络分区
- 使用Redis分布式锁避免脑裂
- 只有能访问Redis的节点才能运行任务

## 性能优化建议

### 1. Redis优化
```ini
# redis.conf
maxmemory 2gb
maxmemory-policy allkeys-lru
timeout 300
tcp-keepalive 60
```

### 2. JVM优化
```bash
JAVA_OPTS="-Xms2g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=100"
```

### 3. 网络优化
- 确保集群节点间网络延迟 < 10ms
- 使用专用网络连接Redis
- 配置Redis连接池

## 注意事项

### 1. 数据一致性
- 分布式锁确保任务唯一性
- 心跳机制保证及时故障检测
- 统计数据最终一致性

### 2. 扩缩容
- 新增节点自动参与任务调度
- 删除节点前需手动停止任务
- 支持滚动更新

### 3. 安全考虑
- Redis访问密码保护
- API接口权限控制
- 敏感配置加密存储

## 升级路径

### 从单机版升级到集群版
1. 部署Redis集群
2. 更新应用配置
3. 逐个重启节点
4. 验证任务分布和故障转移

### 回滚方案
1. 停止所有任务
2. 切换到单机TaskManager
3. 重启应用
4. 手动重新启动任务 