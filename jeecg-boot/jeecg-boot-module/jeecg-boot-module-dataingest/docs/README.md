# DataIngest 模块文档目录

## 📚 文档概览

本目录包含 JeecgBoot DataIngest 数据接入模块的完整技术文档。

## 📋 文档结构

### 🎯 核心文档

| 文档名称 | 文件路径 | 描述 | 适用人群 |
|---------|---------|------|---------|
| **模块概览** | [MODULE_OVERVIEW.md](MODULE_OVERVIEW.md) | 完整的功能介绍、架构设计和技术特性 | 架构师、技术经理 |
| **架构设计** | [ARCHITECTURE_DESIGN.md](ARCHITECTURE_DESIGN.md) | 详细的技术架构、组件设计和扩展方案 | 开发工程师、架构师 |
| **使用指南** | [USAGE_GUIDE.md](USAGE_GUIDE.md) | 完整的配置手册、操作指南和故障排查 | 运维工程师、开发工程师 |

### 🚀 功能增强文档

| 文档名称 | 文件路径 | 描述 | 适用场景 |
|---------|---------|------|---------|
| **增强CDC指南** | [ENHANCED_CDC_GUIDE.md](ENHANCED_CDC_GUIDE.md) | 高级CDC配置、数据血缘追踪和性能优化 | 高级用户、数据工程师 |
| **自动建表功能** | [AUTO_TABLE_CREATION.md](AUTO_TABLE_CREATION.md) | 自动表结构创建、Schema演化管理 | 数据库管理员 |
| **ODS实施案例** | [CDC_ODS_EXAMPLE.md](CDC_ODS_EXAMPLE.md) | 完整的ODS层实施案例和最佳实践 | 数据仓库工程师 |

### 📊 数据库脚本

| 脚本名称 | 文件路径 | 描述 | 用途 |
|---------|---------|------|-----|
| **主任务表** | [data_ingest_moudle_ingest_task.sql](data_ingest_moudle_ingest_task.sql) | 数据接入任务主表建表脚本 | 系统初始化 |
| **数据源配置表** | [data_ingest_moudle_data_source_config.sql](data_ingest_moudle_data_source_config.sql) | 数据源配置子表建表脚本 | 系统初始化 |
| **CDC表配置** | [data_ingest_moudle_data_cdc_table.sql](data_ingest_moudle_data_cdc_table.sql) | CDC表映射配置建表脚本 | 系统初始化 |
| **字段映射表** | [data_ingest_moudle_field_mapping.sql](data_ingest_moudle_field_mapping.sql) | 字段映射规则建表脚本 | 系统初始化 |
| **执行日志表** | [data_ingest_moudle_ingest_log.sql](data_ingest_moudle_ingest_log.sql) | 任务执行日志建表脚本 | 系统初始化 |
| **数据源增强配置** | [data_source_config_enhanced.sql](data_source_config_enhanced.sql) | 数据源配置表增强字段脚本 | 功能升级 |
| **NiFi字段配置** | [cdc_table_nifi_fields.sql](cdc_table_nifi_fields.sql) | CDC表NiFi集成字段脚本 | 功能升级 |

## 🎯 快速导航

### 👨‍💻 开发人员
1. 首先阅读 [MODULE_OVERVIEW.md](MODULE_OVERVIEW.md) 了解整体功能
2. 深入学习 [ARCHITECTURE_DESIGN.md](ARCHITECTURE_DESIGN.md) 掌握技术架构
3. 参考 [USAGE_GUIDE.md](USAGE_GUIDE.md) 进行开发和调试

### 👨‍🔧 运维人员  
1. 重点阅读 [USAGE_GUIDE.md](USAGE_GUIDE.md) 的部署和运维章节
2. 了解 [MODULE_OVERVIEW.md](MODULE_OVERVIEW.md) 的监控指标
3. 参考故障排查手册解决问题

### 👨‍💼 项目经理
1. 查看 [MODULE_OVERVIEW.md](MODULE_OVERVIEW.md) 了解功能特性和优势
2. 参考 [ARCHITECTURE_DESIGN.md](ARCHITECTURE_DESIGN.md) 的扩展规划
3. 评估技术可行性和实施成本

### 🎓 数据工程师
1. 深入研究 [ENHANCED_CDC_GUIDE.md](ENHANCED_CDC_GUIDE.md) 的血缘追踪
2. 学习 [CDC_ODS_EXAMPLE.md](CDC_ODS_EXAMPLE.md) 的最佳实践
3. 掌握数据质量管控和监控方法

## 📈 文档版本说明

| 版本 | 日期 | 更新内容 | 作者 |
|------|------|---------|------|
| v1.0 | 2025-01-XX | 初始版本，完整文档体系 | DataIngest Team |
| v1.1 | 待定 | 增加更多实践案例 | 待定 |
| v2.0 | 待定 | 支持新数据源类型 | 待定 |

## 🔄 文档更新流程

1. **需求收集**：收集用户反馈和功能需求
2. **内容规划**：制定文档更新计划
3. **编写审核**：技术团队编写并交叉审核
4. **测试验证**：实际环境验证文档准确性
5. **发布更新**：正式发布并通知用户

## 📞 文档反馈

如果您在使用文档过程中遇到问题或有改进建议，请通过以下方式联系我们：

- 📧 **邮箱反馈**：docs@your-company.com
- 🐛 **问题报告**：[GitHub Issues](https://github.com/your-org/jeecgboot-dataingest/issues)
- 💡 **改进建议**：[GitHub Discussions](https://github.com/your-org/jeecgboot-dataingest/discussions)
- 📝 **文档贡献**：欢迎提交 Pull Request 改进文档

## 🎯 文档质量标准

我们致力于维护高质量的技术文档：

- ✅ **准确性**：所有代码示例和配置都经过实际测试
- ✅ **完整性**：覆盖从入门到高级的完整使用场景  
- ✅ **及时性**：与代码版本保持同步更新
- ✅ **易读性**：清晰的结构和丰富的示例
- ✅ **实用性**：提供可直接使用的配置和脚本

## 🏆 文档贡献者

感谢以下贡献者对文档建设的支持：

- **核心团队**：DataIngest 开发团队
- **技术审核**：架构师团队  
- **用户反馈**：社区用户和企业用户
- **文档优化**：技术写作团队

---

<div align="center">

**📚 持续完善的文档是项目成功的重要保障 📚**

*最后更新：2025-01-XX*

</div>