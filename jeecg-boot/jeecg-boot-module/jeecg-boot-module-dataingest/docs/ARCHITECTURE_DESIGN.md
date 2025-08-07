# DataIngest 架构设计文档

## 🏗️ 整体架构图

```mermaid
graph TB
    subgraph "用户配置层"
        A[JeecgBoot在线表单] --> B[任务配置管理]
        B --> C[数据源配置]
        B --> D[CDC表配置]
        B --> E[字段映射配置]
    end
    
    subgraph "任务执行层"
        F[DebeziumClusterTaskManager] --> G[MySQL CDC]
        F --> H[PostgreSQL CDC]
        F --> I[SQL Server CDC]
        
        G --> J[数据变更事件]
        H --> J
        I --> J
    end
    
    subgraph "数据处理层"
        J --> K[字段映射转换]
        K --> L[数据质量检查]
        L --> M[血缘信息添加]
        M --> N[PostgreSQL目标表]
    end
    
    subgraph "后续处理层"
        N --> O[NiFi通知服务]
        O --> P[NiFi DWD处理器]
        O --> Q[NiFi DWS处理器]
    end
    
    subgraph "监控日志层"
        R[执行日志记录] --> S[任务状态监控]
        S --> T[告警通知]
    end
    
    subgraph "集群协调层"
        U[Redis分布式锁] --> V[任务分配]
        V --> W[节点健康检查]
        W --> X[故障转移]
    end
    
    F --> R
    N --> R
    O --> R
    
    F --> U
    
    style A fill:#e1f5fe
    style F fill:#f3e5f5
    style N fill:#e8f5e8
    style O fill:#fff3e0
```

## 📊 数据模型设计

### ER关系图

```mermaid
erDiagram
    DataIngestMoudleIngestTask {
        string id PK
        string taskName
        string taskType
        string taskConfig
        string targetTableNamePre
        string scheduleConfig
        integer status
        datetime lastExecuteTime
        datetime nextExecuteTime
        integer executeCount
        integer successCount
        integer failCount
        string remark
    }
    
    DataIngestMoudleDataSourceConfig {
        string id PK
        string taskId FK
        string sourceName
        string connectionConfig
        string authConfig
        string tableNamePreDefault
        string remark
    }
    
    DataIngestMoudleDataCdcTable {
        string id PK
        string taskId FK
        string sourceTableName
        string targetTableName
        string businessDomain
        string nifiDwdProcessorId
        string nifiDwsProcessorId
        integer enableNifiNotify
        integer nifiNotifyMode
        integer notifyDelaySeconds
    }
    
    DataIngestMoudleFieldMapping {
        string id PK
        string taskId FK
        string sourceFieldName
        string targetFieldName
        string fieldType
        string transformRule
        integer isRequired
    }
    
    DataIngestMoudleIngestLog {
        string id PK
        string taskId FK
        string batchId
        integer status
        datetime startTime
        datetime endTime
        integer recordCount
        integer successCount
        integer failCount
        string errorMessage
        string executeLog
    }
    
    DataIngestMoudleIngestTask ||--o{ DataIngestMoudleDataSourceConfig : "一对多"
    DataIngestMoudleIngestTask ||--o{ DataIngestMoudleDataCdcTable : "一对多"
    DataIngestMoudleIngestTask ||--o{ DataIngestMoudleFieldMapping : "一对多"
    DataIngestMoudleIngestTask ||--o{ DataIngestMoudleIngestLog : "一对多"
```

## 🔧 核心组件架构

### 1. Debezium集群任务管理器

```mermaid
graph LR
    subgraph "集群节点管理"
        A[节点注册] --> B[心跳维护]
        B --> C[节点发现]
        C --> D[健康检查]
    end
    
    subgraph "任务调度"
        E[任务分配] --> F[负载均衡]
        F --> G[故障转移]
        G --> H[任务监控]
    end
    
    subgraph "分布式锁"
        I[Redis锁管理] --> J[任务唯一性]
        J --> K[并发控制]
        K --> L[死锁检测]
    end
    
    subgraph "CDC引擎"
        M[Debezium引擎] --> N[变更事件]
        N --> O[数据转换]
        O --> P[目标写入]
    end
    
    D --> E
    H --> I
    L --> M
```

**关键特性**：
- **分布式协调**：基于Redis实现节点间协调
- **任务唯一性**：分布式锁确保同一任务不重复执行
- **故障转移**：自动检测节点故障并重新分配任务
- **负载均衡**：根据节点负载动态分配任务

### 2. NiFi集成架构

```mermaid
graph TD
    subgraph "CDC数据变更"
        A[INSERT事件] --> D[通知决策]
        B[UPDATE事件] --> D
        C[DELETE事件] --> D
    end
    
    subgraph "通知策略"
        D --> E{通知模式判断}
        E -->|立即通知| F[立即触发NiFi]
        E -->|批量通知| G[批量收集]
        E -->|定时通知| H[定时任务]
        
        G --> I[达到阈值触发]
        H --> J[定时触发]
    end
    
    subgraph "NiFi处理"
        F --> K[DWD处理器]
        I --> K
        J --> K
        
        K --> L[数据清洗]
        L --> M[数据标准化]
        M --> N[DWS处理器]
        N --> O[数据聚合]
        O --> P[指标计算]
    end
    
    subgraph "监控反馈"
        Q[处理器状态检查] --> R[执行结果记录]
        R --> S[告警通知]
    end
    
    K --> Q
    N --> Q
```

**通知模式说明**：
- **立即通知**（Mode=1）：每个CDC事件立即触发NiFi处理器
- **批量通知**（Mode=2）：收集一定数量的变更后批量触发
- **定时通知**（Mode=3）：按固定时间间隔触发，适合大批量处理

### 3. 数据血缘追踪架构

```mermaid
graph LR
    subgraph "源数据识别"
        A[源数据库] --> B[源表名]
        B --> C[业务域标识]
        C --> D[数据源配置]
    end
    
    subgraph "血缘信息生成"
        D --> E[血缘路径构建]
        E --> F[转换规则记录]
        F --> G[质量评分计算]
        G --> H[时间戳标记]
    end
    
    subgraph "目标表增强"
        H --> I[血缘字段添加]
        I --> J[索引自动创建]
        J --> K[软删除支持]
        K --> L[历史版本管理]
    end
    
    subgraph "血缘查询"
        L --> M[血缘关系查询]
        M --> N[影响分析]
        N --> O[数据质量报告]
    end
```

**血缘字段设计**：
```sql
-- 自动添加到每个ODS表的血缘字段
ALTER TABLE ods_target_table ADD COLUMN IF NOT EXISTS source_database VARCHAR(100);
ALTER TABLE ods_target_table ADD COLUMN IF NOT EXISTS source_table VARCHAR(100);
ALTER TABLE ods_target_table ADD COLUMN IF NOT EXISTS business_domain VARCHAR(50);
ALTER TABLE ods_target_table ADD COLUMN IF NOT EXISTS data_lineage TEXT;
ALTER TABLE ods_target_table ADD COLUMN IF NOT EXISTS transformation_rules TEXT;
ALTER TABLE ods_target_table ADD COLUMN IF NOT EXISTS data_quality_score DECIMAL(3,2);
ALTER TABLE ods_target_table ADD COLUMN IF NOT EXISTS sync_timestamp BIGINT;
ALTER TABLE ods_target_table ADD COLUMN IF NOT EXISTS cdc_operation VARCHAR(10);
ALTER TABLE ods_target_table ADD COLUMN IF NOT EXISTS record_status VARCHAR(10) DEFAULT 'ACTIVE';
```

## 🔄 数据流转设计

### 完整数据流转图

```mermaid
sequenceDiagram
    participant U as 用户
    participant J as JeecgBoot表单
    participant T as 任务管理器
    participant D as Debezium引擎
    participant S as 源数据库
    participant P as PostgreSQL
    participant N as NiFi
    participant L as 日志系统
    
    U->>J: 1. 配置数据接入任务
    J->>T: 2. 保存任务配置
    T->>D: 3. 启动CDC监听
    D->>S: 4. 连接源数据库
    S->>D: 5. 数据变更事件
    D->>D: 6. 事件解析和转换
    D->>P: 7. 写入目标表
    P->>N: 8. 触发NiFi通知
    N->>N: 9. DWD/DWS处理
    D->>L: 10. 记录执行日志
    L->>J: 11. 状态反馈
```

### 错误处理流程

```mermaid
graph TD
    A[CDC事件处理] --> B{处理是否成功?}
    B -->|成功| C[记录成功日志]
    B -->|失败| D[错误分类]
    
    D --> E{错误类型}
    E -->|网络错误| F[重试机制]
    E -->|数据格式错误| G[跳过并记录]
    E -->|目标表不存在| H[自动建表]
    E -->|权限错误| I[告警通知]
    
    F --> J{重试次数检查}
    J -->|未达上限| K[延迟重试]
    J -->|达到上限| L[标记失败]
    
    K --> A
    G --> M[错误日志记录]
    H --> N[建表成功?]
    N -->|是| A
    N -->|否| L
    I --> L
    L --> M
    M --> O[告警通知]
```

## 🚀 性能优化设计

### 1. 批量处理优化

```mermaid
graph LR
    subgraph "事件收集"
        A[CDC事件] --> B[事件缓冲区]
        B --> C[批量阈值检查]
    end
    
    subgraph "批量处理"
        C --> D[批量SQL生成]
        D --> E[事务批量提交]
        E --> F[批量NiFi通知]
    end
    
    subgraph "性能监控"
        F --> G[处理时间统计]
        G --> H[吞吐量监控]
        H --> I[动态调优]
    end
    
    I --> B
```

**优化策略**：
- **批量大小**：根据数据量和延迟要求动态调整（默认1000条）
- **提交频率**：平衡数据一致性和性能（默认5秒或1000条）
- **并发控制**：多线程处理不同表的数据变更
- **内存管理**：限制缓冲区大小防止内存溢出

### 2. 集群负载均衡

```mermaid
graph TD
    subgraph "负载评估"
        A[节点CPU使用率] --> D[综合负载评分]
        B[节点内存使用率] --> D
        C[当前任务数量] --> D
    end
    
    subgraph "任务分配策略"
        D --> E{负载均衡算法}
        E -->|轮询| F[Round Robin]
        E -->|最少连接| G[Least Connection]
        E -->|加权轮询| H[Weighted Round Robin]
    end
    
    subgraph "动态调整"
        I[性能监控] --> J[负载重新评估]
        J --> K[任务重新分配]
        K --> L[平滑迁移]
    end
    
    F --> I
    G --> I
    H --> I
```

### 3. 缓存策略

```mermaid
graph LR
    subgraph "多级缓存"
        A[本地缓存] --> B[Redis缓存]
        B --> C[数据库]
    end
    
    subgraph "缓存内容"
        D[任务配置] --> A
        E[数据源配置] --> A
        F[字段映射] --> B
        G[表结构信息] --> B
    end
    
    subgraph "缓存更新"
        H[配置变更] --> I[缓存失效]
        I --> J[异步刷新]
        J --> K[版本控制]
    end
```

## 🔒 安全架构设计

### 1. 数据安全

```mermaid
graph TD
    subgraph "连接安全"
        A[SSL/TLS加密] --> B[证书验证]
        B --> C[连接池管理]
    end
    
    subgraph "认证授权"
        D[数据源认证] --> E[权限验证]
        E --> F[访问控制]
    end
    
    subgraph "数据加密"
        G[敏感字段加密] --> H[传输加密]
        H --> I[存储加密]
    end
    
    subgraph "审计日志"
        J[操作记录] --> K[访问日志]
        K --> L[安全审计]
    end
    
    C --> D
    F --> G
    I --> J
```

### 2. 系统安全

- **访问控制**：基于JeecgBoot的RBAC权限模型
- **API安全**：JWT令牌验证和API限流
- **数据脱敏**：敏感字段自动脱敏处理
- **审计追踪**：完整的操作日志和变更记录

## 📊 监控告警架构

### 监控指标体系

```mermaid
graph TB
    subgraph "业务指标"
        A[任务执行成功率] --> D[综合监控面板]
        B[数据同步延迟] --> D
        C[数据质量评分] --> D
    end
    
    subgraph "系统指标"
        E[CPU使用率] --> F[系统监控]
        G[内存使用率] --> F
        H[磁盘IO] --> F
        I[网络IO] --> F
    end
    
    subgraph "应用指标"
        J[连接池状态] --> K[应用监控]
        L[缓存命中率] --> K
        M[异常统计] --> K
    end
    
    subgraph "告警策略"
        N[阈值告警] --> O[告警通知]
        P[趋势告警] --> O
        Q[异常告警] --> O
    end
    
    D --> N
    F --> P
    K --> Q
```

**告警级别**：
- **P0 紧急**：系统不可用、数据丢失
- **P1 重要**：功能异常、性能严重下降
- **P2 一般**：性能轻微下降、配置问题
- **P3 提示**：资源使用提醒、优化建议

## 🔄 扩展性设计

### 水平扩展能力

```mermaid
graph LR
    subgraph "计算扩展"
        A[新增节点] --> B[自动发现]
        B --> C[任务重分配]
        C --> D[负载重新平衡]
    end
    
    subgraph "存储扩展"
        E[分库分表] --> F[数据分片]
        F --> G[跨库查询]
        G --> H[分布式事务]
    end
    
    subgraph "功能扩展"
        I[插件机制] --> J[自定义处理器]
        J --> K[扩展接口]
        K --> L[第三方集成]
    end
```

### 插件化架构

- **数据源插件**：支持新的数据库类型
- **转换插件**：自定义数据转换逻辑
- **通知插件**：支持更多的下游系统
- **监控插件**：集成第三方监控系统

## 📈 未来演进规划

### 短期目标（3个月）
- [ ] 支持更多数据源类型（Oracle、MongoDB等）
- [ ] 增强数据质量检查功能
- [ ] 优化大表同步性能
- [ ] 完善监控告警体系

### 中期目标（6个月）
- [ ] 支持流式计算集成（Flink/Spark）
- [ ] 增加数据血缘可视化界面
- [ ] 支持Schema演化自动处理
- [ ] 增强集群管理功能

### 长期目标（1年）
- [ ] AI驱动的数据质量优化
- [ ] 自动化运维和故障自愈
- [ ] 多云部署支持
- [ ] 实时数据湖架构完善

这个架构设计充分考虑了企业级应用的需求，在保证功能完整性的同时，注重性能、安全性和可扩展性，为数据接入提供了一个稳定可靠的技术基础。