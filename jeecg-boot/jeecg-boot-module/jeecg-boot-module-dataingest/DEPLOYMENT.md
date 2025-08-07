# DataIngest 微服务部署指南

## 部署方式

DataIngest微服务支持以下三种部署方式：

### 1. 集成部署（推荐）

作为JeecgBoot微服务集群的一部分，使用主项目的公共服务。

**步骤：**
1. 确保主项目已启动（包含Redis、Nacos、PostgreSQL等公共服务）
2. 在主项目的 `docker-compose-cloud.yml` 中已添加dataingest服务
3. 启动整个集群：
   ```bash
   cd /path/to/jeecg-boot
   docker-compose -f docker-compose-cloud.yml up -d
   ```

**优势：**
- 使用统一的中间件服务
- 资源利用率高
- 管理简单

### 2. 独立部署

完全独立运行，包含自己的中间件服务。

**步骤：**
1. 进入dataingest模块目录：
   ```bash
   cd jeecg-boot/jeecg-boot-module/jeecg-boot-module-dataingest
   ```

2. 构建项目：
   ```bash
   mvn clean package -DskipTests
   ```

3. 启动独立服务：
   ```bash
   docker-compose up -d
   ```

**优势：**
- 完全独立，不依赖主项目
- 可以单独维护和升级
- 适合独立团队开发

### 3. 混合部署

使用主项目的部分服务，但独立运行dataingest。

**步骤：**
1. 确保主项目的Redis、Nacos、PostgreSQL服务已启动
2. 修改 `application.yml` 中的服务地址为实际地址
3. 单独启动dataingest：
   ```bash
   cd jeecg-boot/jeecg-boot-module/jeecg-boot-module-dataingest
   mvn clean package -DskipTests
   docker build -t jeecg-dataingest .
   docker run -d -p 7009:7009 --name jeecg-dataingest jeecg-dataingest
   ```

## 服务信息

- **服务名**: jeecg-dataingest
- **端口**: 7009
- **上下文路径**: /dataingest
- **健康检查**: http://localhost:7009/dataingest/health

## 配置说明

### 集成部署配置
```yaml
spring:
  redis:
    host: jeecg-boot-redis
    port: 6379
    password:
  datasource:
    url: jdbc:postgresql://jeecg-boot-pgsql:5432/jeecg-boot
  cloud:
    nacos:
      server-addr: jeecg-boot-nacos:8848
```

### 独立部署配置
```yaml
spring:
  redis:
    host: jeecg-boot-redis
    port: 6379
    password:
  datasource:
    url: jdbc:postgresql://jeecg-boot-pgsql:5432/jeecg-boot
  cloud:
    nacos:
      server-addr: jeecg-boot-nacos:8848
```

### 混合部署配置
```yaml
spring:
  redis:
    host: 192.168.1.100  # 主项目Redis地址
    port: 6379
    password:
  datasource:
    url: jdbc:postgresql://192.168.1.100:5432/jeecg-boot
  cloud:
    nacos:
      server-addr: 192.168.1.100:8848
```

## 验证部署

### 健康检查
```bash
curl http://localhost:7009/dataingest/health
```

### 服务信息
```bash
curl http://localhost:7009/dataingest/info
```

### 查看日志
```bash
# 集成部署
docker-compose -f docker-compose-cloud.yml logs -f jeecg-dataingest-start

# 独立部署
docker-compose logs -f jeecg-boot-dataingest
```

## 故障排查

### 常见问题

1. **服务无法启动**
   - 检查端口7009是否被占用
   - 检查依赖服务（Redis、Nacos、PostgreSQL）是否正常

2. **连接超时**
   - 检查网络配置
   - 检查服务地址和端口是否正确

3. **数据库连接失败**
   - 检查PostgreSQL服务状态
   - 检查数据库连接配置

### 日志查看
```bash
# 查看应用日志
docker logs jeecg-dataingest-start

# 查看详细日志
docker exec -it jeecg-dataingest-start tail -f /jeecg-dataingest-cloud/logs/dataingest.log
```

## 性能调优

### JVM参数
```bash
JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -Dfile.encoding=UTF-8"
```

### 数据库连接池
```yaml
spring:
  datasource:
    druid:
      initial-size: 5
      min-idle: 5
      maxActive: 1000
      maxWait: 60000
```

## 监控告警

### 健康检查端点
- `GET /dataingest/health` - 服务健康状态
- `GET /dataingest/info` - 服务基本信息

### 监控指标
- 服务响应时间
- 数据库连接状态
- Redis连接状态
- 任务执行状态 