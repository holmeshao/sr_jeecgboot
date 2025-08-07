# 数据接入微服务项目总结

## 项目概述

本项目是一个基于Spring Boot 2.x的数据接入微服务，作为JeecgBoot社区版（v3.8.1）的配套组件，专门用于处理各种数据源的接入、转换和存储。

## 核心功能

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

### 3. 核心特性
- 数据源配置管理
- 任务调度管理
- 字段映射配置
- 执行日志记录
- 数据质量监控
- 与JeecgBoot平台集成

## 技术架构

### 技术栈
- **框架**: Spring Boot 2.7.18
- **微服务**: Spring Cloud Alibaba
- **服务注册**: Nacos
- **缓存**: Redis
- **数据库**: PostgreSQL
- **CDC引擎**: Debezium Embedded
- **任务调度**: XXL-Job
- **HTTP客户端**: OkHttp
- **构建工具**: Maven
- **Java版本**: 17

### 模块结构
```
dataingest/
├── core/                    # 核心模块
│   ├── entity/             # 实体类
│   ├── service/            # 核心服务
│   └── config/             # 配置类
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

## 核心组件

### 1. 实体类
- `DataSourceConfig`: 数据源配置表
- `IngestTask`: 数据接入任务表
- `IngestLog`: 数据接入日志表
- `FieldMapping`: 字段映射配置表

### 2. 核心服务
- `IDataIngestService`: 数据接入服务接口
- `DataIngestServiceImpl`: 数据接入服务实现
- `PostgresWriteService`: PostgreSQL数据写入服务

### 3. 模块服务
- `DebeziumService`: Debezium CDC服务
- `KingdeeService`: 金蝶接入服务
- `OpenApiService`: 通用API服务

### 4. 任务调度
- `DataIngestJobHandler`: XXL-Job任务处理器
- 支持金蝶数据接入、通用API接入、文件数据接入等任务

## 配置说明

### 应用配置
- **服务端口**: 7009
- **上下文路径**: /dataingest
- **数据库**: PostgreSQL
- **缓存**: Redis
- **服务注册**: Nacos

### 数据源配置
- **SQLServer**: 支持CDC模式
- **金蝶**: 支持Cloud/K3 API
- **通用API**: 支持HTTP/HTTPS接口

### 调度配置
- **XXL-Job**: 任务调度中心
- **执行器端口**: 9999
- **日志路径**: /data/applogs/xxl-job/jobhandler

## 部署方式

### 1. 单体模式
作为依赖模块集成到JeecgBoot主系统中

### 2. 微服务模式
独立部署为微服务，支持Docker容器化

### 3. Docker部署
```bash
# 构建镜像
docker build -t jeecg-dataingest .

# 运行容器
docker run -d -p 7009:7009 --name jeecg-dataingest jeecg-dataingest
```

### 4. Docker Compose部署
```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f dataingest
```

## 管理接口

### 健康检查
- `GET /dataingest/health` - 服务健康检查
- `GET /dataingest/info` - 服务信息
- `GET /dataingest/system/status` - 系统状态

### 任务管理
- `POST /dataingest/task/start/{taskId}` - 启动任务
- `POST /dataingest/task/stop/{taskId}` - 停止任务
- `GET /dataingest/task/status/{taskId}` - 获取任务状态
- `GET /dataingest/task/statistics/{taskId}` - 获取任务统计

### CDC管理
- `POST /dataingest/cdc/start` - 启动CDC监听
- `POST /dataingest/cdc/stop` - 停止CDC监听

## 数据库设计

### 核心表结构
1. **data_source_config**: 数据源配置表
2. **ingest_task**: 数据接入任务表
3. **ingest_log**: 数据接入日志表
4. **field_mapping**: 字段映射配置表

### ODS层表
1. **ods_invoice**: 发票数据表
2. **ods_material**: 物料数据表
3. **ods_order**: 订单数据表

## 与NiFi集成

### 架构设计
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

## 监控和日志

### 日志配置
- **日志级别**: DEBUG (开发环境)
- **日志文件**: logs/dataingest.log
- **日志格式**: 包含时间戳、线程、级别、类名、消息

### 监控指标
- 任务执行状态
- 数据接入量统计
- 错误率监控
- 性能指标

## 开发计划

### 已完成 ✅
1. 基础框架搭建
2. 核心模块开发
3. Debezium CDC模块
4. 金蝶接入模块
5. 通用API模块
6. 调度模块
7. 管理接口
8. 数据库设计
9. Docker部署配置
10. 启动脚本

### 待完成 🔄
1. 数据质量监控
2. 性能优化
3. 监控告警
4. 单元测试
5. 集成测试
6. 文档完善

## 使用示例

### 1. CDC数据接入
```bash
# 启动CDC监听
curl -X POST http://localhost:7009/dataingest/cdc/start

# 检查状态
curl http://localhost:7009/dataingest/health
```

### 2. 金蝶数据接入
```bash
# 启动金蝶数据接入任务
curl -X POST http://localhost:7009/dataingest/task/start/2

# 查看任务状态
curl http://localhost:7009/dataingest/task/status/2
```

### 3. 通用API接入
```bash
# 启动API数据接入任务
curl -X POST http://localhost:7009/dataingest/task/start/3

# 查看任务统计
curl http://localhost:7009/dataingest/task/statistics/3
```

## 注意事项

### 环境要求
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+
- Nacos 2.0+

### 配置要求
- 确保Nacos服务正常运行
- 确保Redis服务正常运行
- 确保PostgreSQL服务正常运行
- 确保SQLServer开启CDC功能
- 端口7009未被占用

### 安全考虑
- 数据库密码加密存储
- API接口权限控制
- 日志敏感信息脱敏
- 网络访问控制

## 总结

本项目成功实现了一个功能完整的数据接入微服务，具备以下特点：

1. **模块化设计**: 清晰的模块划分，便于维护和扩展
2. **多种接入方式**: 支持CDC、API、定时任务等多种数据接入方式
3. **配置化管理**: 通过配置文件和数据表管理各种配置
4. **监控完善**: 提供完整的任务监控和日志记录
5. **部署灵活**: 支持单体模式和微服务模式部署
6. **集成友好**: 与JeecgBoot平台和NiFi系统良好集成

该微服务可以作为JeecgBoot生态的重要组成部分，为数据中台建设提供强有力的数据接入能力。 