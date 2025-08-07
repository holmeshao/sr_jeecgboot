# 数据接入微服务 (Data Ingest Service)

## 项目说明
数据接入微服务，专门用于处理各种数据源的接入、转换和存储，作为JeecgBoot的下游微服务，负责从外部系统接入数据并入库PostgreSQL的ODS层。

## 技术栈
- Spring Boot 2.7.18
- Spring Cloud Alibaba
- Nacos (服务注册与配置中心)
- Redis
- PostgreSQL
- Debezium (CDC数据捕获)
- XXL-Job (任务调度)
- OkHttp (HTTP客户端)

## 服务信息
- **服务名**: jeecg-dataingest
- **端口**: 7009
- **上下文路径**: /dataingest
- **访问地址**: http://localhost:7009/dataingest

## 功能特性

### 1. 多种数据接入方式
- **CDC模式**: 基于Debezium的SQLServer变更数据捕获
- **API拉取**: 支持金蝶Cloud/K3、通用第三方API
- **定时任务**: 基于XXL-Job的调度任务
- **文件导入**: Excel、CSV、JSON等文件格式

### 2. 数据源支持
- SQLServer (CDC模式)
- 金蝶Cloud/K3 (API模式)
- 通用HTTP API
- 文件系统

### 3. 核心功能
- 数据源配置管理
- 任务调度管理
- 字段映射配置
- 执行日志记录
- 数据质量监控

## 模块结构

```
dataingest/
├── core/                    # 核心模块
│   ├── entity/             # 实体类
│   └── service/            # 核心服务
├── debezium/               # Debezium CDC模块
│   ├── config/             # 配置类
│   └── service/            # Debezium服务
├── kingdee/                # 金蝶接入模块
│   ├── config/             # 配置类
│   └── service/            # 金蝶服务
├── openapi/                # 通用API模块
│   └── service/            # API服务
├── scheduler/              # 调度模块
│   ├── config/             # XXL-Job配置
│   └── job/                # 任务处理器
└── controller/             # 管理接口
```

## 启动方式

### 1. 单体模式
作为依赖模块集成到主系统中，启动类被注释。

### 2. 微服务模式
独立启动微服务：
```bash
# 确保Nacos服务已启动
# 确保Redis服务已启动
# 确保PostgreSQL服务已启动

# 启动微服务
java -jar jeecg-boot-module-dataingest.jar
```

## 配置说明

### 数据库配置
在 `application.yml` 中配置PostgreSQL连接信息：
```yaml
spring:
  datasource:
    dynamic:
      datasource:
        master:
          url: jdbc:postgresql://localhost:5432/jeecg-boot-dev
          username: postgres
          password: postgres
          driver-class-name: org.postgresql.Driver
```

### Debezium配置
```yaml
debezium:
  sqlserver:
    hostname: localhost
    port: 1433
    database: testdb
    username: sa
    password: password
    server-name: sqlserver-connector
    table-include-list: dbo.invoice,dbo.material
  postgres:
    url: jdbc:postgresql://localhost:5432/jeecg-boot-dev
    username: postgres
    password: postgres
    table-prefix: ods_
```

### 金蝶配置
```yaml
kingdee:
  cloud:
    base-url: https://api.kingdee.com
    app-id: your-app-id
    app-secret: your-app-secret
    username: your-username
    password: your-password
    lcid: 2052
    db-id: your-db-id
```

### XXL-Job配置
```yaml
xxl:
  job:
    admin-addresses: http://localhost:8080/xxl-job-admin
    appname: data-ingest-executor
    port: 9999
    log-path: /data/applogs/xxl-job/jobhandler
```

## 接口测试

### 健康检查
```bash
curl http://localhost:7009/dataingest/health
```

### 服务信息
```bash
curl http://localhost:7009/dataingest/info
```

### 启动CDC监听
```bash
curl -X POST http://localhost:7009/dataingest/cdc/start
```

### 停止CDC监听
```bash
curl -X POST http://localhost:7009/dataingest/cdc/stop
```

### 启动任务
```bash
curl -X POST http://localhost:7009/dataingest/task/start/{taskId}
```

### 停止任务
```bash
curl -X POST http://localhost:7009/dataingest/task/stop/{taskId}
```

### 获取任务状态
```bash
curl http://localhost:7009/dataingest/task/status/{taskId}
```

## 数据库初始化

执行SQL脚本创建必要的表结构：
```bash
psql -h localhost -U postgres -d jeecg-boot-dev -f src/main/resources/sql/dataingest_tables.sql
```

## 使用示例

### 1. CDC数据接入
1. 配置SQLServer数据源
2. 创建CDC任务
3. 启动CDC监听
4. 数据自动同步到ODS表

### 2. 金蝶数据接入
1. 配置金蝶连接信息
2. 创建API拉取任务
3. 配置调度策略
4. 定时拉取数据到ODS表

### 3. 通用API接入
1. 配置API连接信息
2. 创建API任务
3. 配置字段映射
4. 执行数据拉取

## 与NiFi集成

数据接入微服务与NiFi的集成架构：

1. **JeecgBoot**: 配置中心，管理数据源和任务配置
2. **DataIngest**: 数据接入执行器，负责数据拉取和ODS层写入
3. **Debezium**: CDC引擎，捕获数据库变更
4. **NiFi**: 流处理引擎，执行数据分层计算

### 集成流程
1. JeecgBoot配置数据源和任务
2. DataIngest根据配置执行数据接入
3. 数据写入PostgreSQL ODS层
4. Debezium捕获变更并推送给NiFi
5. NiFi执行数据处理逻辑，生成DWD层

## 注意事项
- 确保Nacos服务正常运行
- 确保Redis服务正常运行
- 确保PostgreSQL服务正常运行
- 确保SQLServer开启CDC功能
- 端口7009未被占用
- 配置正确的数据源连接信息
- 定期检查任务执行日志

## 开发计划
1. ✅ 基础框架搭建
2. ✅ 核心模块开发
3. ✅ Debezium CDC模块
4. ✅ 金蝶接入模块
5. ✅ 通用API模块
6. ✅ 调度模块
7. ✅ 管理接口
8. 🔄 数据质量监控
9. 🔄 性能优化
10. 🔄 监控告警

## 版本历史
- v1.0.0: 基础功能实现
  - 支持CDC、API、定时任务等多种接入方式
  - 完整的任务管理和监控功能
  - 与JeecgBoot平台集成 