# OpenLowCode - 自建低代码平台架构设计（v3.0 最终版）

## 一、项目概述

基于 **RuoYi-Vue-Plus + Flowable + Form.io** 构建的完全开源低代码平台，摆脱 JeecgBoot "伪开源"限制。

**核心原则：**
- ✅ 100% 开源可控（全部组件 GitHub 全球开源）
- ✅ 组件持续维护（2025年仍活跃更新）
- ✅ 统一 Vue3 生态
- ✅ PostgreSQL 原生支持
- ✅ 基于成熟开源组件，不重复造轮子

---

## 二、最终技术栈

| 层级 | 技术选型 | GitHub 仓库 & 维护状态 | 核心能力 |
|------|---------|----------------------|---------|
| **基础框架** | RuoYi-Vue-Plus 2.7.x + Flowable 6.8.x | [dromara/RuoYi-Vue-Plus](https://github.com/dromara/RuoYi-Vue-Plus)（2025.12 活跃）<br>[flowable/flowable-engine](https://github.com/flowable/flowable-engine)（2025.12 活跃） | SpringBoot 2.7.x LTS，全球开源 |
| **在线表单设计器** | Form.io | [formio/formio](https://github.com/formio/formio)（2025.12 活跃，13k+ star） | 可视化拖拽、主从表、动态 CRUD、代码导出 |
| **可视化报表** | Apache Superset | [apache/superset](https://github.com/apache/superset)（2025.12 活跃） | 拖拽式可视化报表 |
| **大屏** | DataV-Vue3 | [DataV-Team/DataV](https://github.com/DataV-Team/DataV)（2025.10 活跃） | 纯 Vue3 拖拽大屏，无后端依赖 |
| **移动端** | uni-app + uView2 | [dcloudio/uni-app](https://github.com/dcloudio/uni-app)（2025.12 活跃）<br>[umicro/uView2](https://github.com/umicro/uView2.0)（2025.12 活跃） | Vue3 兼容，跨平台适配 |
| **数据库** | PostgreSQL 16 | [postgres/postgres](https://github.com/postgres/postgres)（2025.12 活跃） | 支持 JSONB，适配表单配置存储 |
| **代码生成器** | RuoYi-Vue-Plus 内置 + Form.io 导出 | 内置 | 支持工作流/移动端代码生成 |

---

## 三、Form.io 核心能力（替代 JeecgBoot 在线表单）

### 为什么选择 Form.io

| 能力 | JeecgBoot 在线表单 | Form.io |
|-----|-------------------|---------|
| 可视化拖拽设计 | ✅ 闭源 | ✅ 开源 |
| 主表/子表设计 | ✅ 闭源 | ✅ 开源 |
| 字段级配置（控件类型/校验/显示逻辑） | ✅ 闭源 | ✅ 开源 |
| 无代码模式（动态渲染+自动 CRUD） | ✅ 闭源 | ✅ 开源 |
| 有代码模式（导出 Vue/Java 代码） | ✅ 闭源 | ✅ 开源 |
| PostgreSQL 支持 | ❌ 需适配 | ✅ 原生支持 |
| 协议 | 部分闭源 | MIT 开源 |

### Form.io 核心组件

```
formio/formio          → 后端服务（Node.js，可用 Java SDK 替代）
formio/vue3            → Vue3 前端组件
formio/formio.js       → 表单渲染引擎
formio/formio-builder  → 可视化设计器
```

---

## 四、Form.io 集成方案

### 4.1 前端集成

```bash
# 进入 RuoYi-Vue-Plus 前端目录
cd ruoyi-ui

# 安装 Form.io Vue3 依赖
npm install @formio/vue3 --save
```

**设计器页面示例：**

```vue
<template>
  <div>
    <!-- Form.io 可视化设计器 -->
    <FormBuilder 
      v-model="formSchema"
      :components="components"
      @change="saveFormSchema"
    />
    <el-button @click="previewForm">预览</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { FormBuilder } from '@formio/vue3';
import { saveForm } from '@/api/form';

const formSchema = ref({
  title: '订单表单',
  components: [
    { type: 'textfield', key: 'orderNo', label: '订单号', validate: { required: true } },
    // 子表配置（订单明细）
    { type: 'datagrid', key: 'orderItems', label: '订单明细', components: [
      { type: 'textfield', key: 'productName', label: '商品名称' },
      { type: 'number', key: 'price', label: '价格' }
    ]}
  ]
});

const saveFormSchema = async () => {
  await saveForm({
    formName: formSchema.value.title,
    formSchema: JSON.stringify(formSchema.value)
  });
};
</script>
```

### 4.2 后端集成（Java SDK）

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.formio</groupId>
    <artifactId>formio-java</artifactId>
    <version>1.4.0</version>
</dependency>
```

**动态 CRUD 接口：**

```java
@RestController
@RequestMapping("/form")
public class FormController {
    
    @Autowired
    private FormService formService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 无代码模式：动态提交表单数据
    @PostMapping("/submit/{formId}")
    public Result submitForm(@PathVariable String formId, @RequestBody Map<String, Object> formData) {
        // 1. 查询表单 Schema
        FormConfig config = formService.getById(formId);
        JSONObject schema = JSON.parseObject(config.getFormSchema());
        
        // 2. 动态生成 PG 插入 SQL
        String sql = FormPgUtils.generateInsertSql(schema, formData);
        
        // 3. 执行插入
        jdbcTemplate.execute(sql);
        return Result.ok("提交成功");
    }

    // 有代码模式：导出 Vue/Java 代码
    @GetMapping("/export/{formId}")
    public void exportCode(@PathVariable String formId, HttpServletResponse response) {
        FormConfig config = formService.getById(formId);
        // 复用 RuoYi 代码生成器模板
        CodeGeneratorUtils.export(config, response);
    }
}
```

### 4.3 PostgreSQL 配置

```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/openlowcode?currentSchema=public&stringtype=unspecified
    username: postgres
    password: postgres
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

---

## 五、表单+工作流集成方案

### 数据模型

```sql
-- 表单配置表（存储 Form.io Schema）
CREATE TABLE sys_form_config (
    id BIGSERIAL PRIMARY KEY,
    form_name VARCHAR(100) NOT NULL,
    form_schema JSONB NOT NULL,           -- Form.io JSON Schema
    table_name VARCHAR(100),              -- 关联的数据库表名
    is_db_synced BOOLEAN DEFAULT false,   -- 是否已同步建表
    create_by VARCHAR(64),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 流程-表单关联表
CREATE TABLE wf_process_form (
    id BIGSERIAL PRIMARY KEY,
    process_key VARCHAR(64) NOT NULL,
    node_id VARCHAR(64) NOT NULL,
    form_id BIGINT NOT NULL,
    field_perms JSONB,                    -- 字段权限配置
    UNIQUE(process_key, node_id)
);

-- 表单数据表（动态生成，或统一存储）
CREATE TABLE sys_form_data (
    id BIGSERIAL PRIMARY KEY,
    form_id BIGINT NOT NULL,
    data JSONB NOT NULL,                  -- 表单数据
    process_instance_id VARCHAR(64),      -- 关联流程实例
    create_by VARCHAR(64),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 集成流程

```
1. 设计表单 → Form.io 设计器 → 保存 Schema 到 sys_form_config
2. 同步建表 → 根据 Schema 动态生成 CREATE TABLE SQL → 执行 DDL
3. 绑定流程 → 流程节点关联 form_id → 配置字段权限
4. 运行时 → 查询 Schema → 动态渲染表单 → 提交数据
5. 导出代码 → 根据 Schema 生成 Entity/Controller/Vue 代码
```

---

## 六、系统架构图

```
┌─────────────────────────────────────────────────────────┐
│ 前端层                                                   │
│  ├─ 管理后台：RuoYi-Vue-Plus UI（Vue3 + Element Plus）  │
│  ├─ 表单设计：Form.io Builder（可视化拖拽）             │
│  ├─ 大屏展示：DataV-Vue3（纯前端）                      │
│  ├─ 可视化报表：Apache Superset                         │
│  └─ 移动端：uni-app + uView2                            │
└───────────────────────────┬─────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────┐
│ 后端层（Java 生态，Spring Boot 2.7.x LTS）              │
│  ├─ 基础框架：RuoYi-Vue-Plus（dromara 维护）            │
│  ├─ 工作流引擎：Flowable 6.8.x                          │
│  ├─ 表单引擎：Form.io Java SDK + 动态 CRUD              │
│  ├─ 代码生成器：RuoYi 内置 + Form.io Schema 适配        │
│  └─ 数据接口：RESTful API                               │
└───────────────────────────┬─────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────┐
│ 数据层                                                   │
│  ├─ 主数据库：PostgreSQL 16（JSONB 存储表单配置）       │
│  └─ 缓存：Redis 7.x                                     │
└─────────────────────────────────────────────────────────┘
```

---

## 七、实施计划

| 阶段 | 任务 | 时间 |
|-----|------|-----|
| **第一阶段** | RuoYi-Vue-Plus + Flowable + PostgreSQL 基础环境 | 3-5 天 |
| **第二阶段** | Form.io 前端集成（设计器 + 渲染器） | 1 周 |
| **第三阶段** | Form.io 后端集成（动态 CRUD + 建表） | 1 周 |
| **第四阶段** | 表单 + 工作流集成（节点绑定 + 字段权限） | 1 周 |
| **第五阶段** | 代码生成器适配（导出 Vue/Java 代码） | 3-5 天 |
| **第六阶段** | 报表 + 大屏集成 | 1 周 |
| **第七阶段** | 移动端开发 | 1-2 周 |
| **总计** | | **6-8 周** |

---

## 八、与 JeecgBoot 对比

| 项目 | JeecgBoot | OpenLowCode |
|------|-----------|-------------|
| **开源协议** | ❌ 部分闭源（混淆） | ✅ 100% 开源 |
| **在线表单设计** | ❌ 闭源混淆 | ✅ Form.io (MIT) |
| **工作流** | Flowable | ✅ Flowable 6.8.x |
| **报表** | ❌ 积木报表（闭源） | ✅ Apache Superset |
| **大屏** | ❌ 积木报表（闭源） | ✅ DataV-Vue3 (MIT) |
| **数据库** | MySQL | ✅ PostgreSQL 16 |
| **可控性** | ❌ 受限 | ✅ 完全可控 |

---

## 九、参考资源

| 组件 | GitHub 仓库 | 文档 |
|------|------------|------|
| RuoYi-Vue-Plus | [dromara/RuoYi-Vue-Plus](https://github.com/dromara/RuoYi-Vue-Plus) | [文档](https://plus-doc.dromara.org/) |
| Flowable | [flowable/flowable-engine](https://github.com/flowable/flowable-engine) | [文档](https://www.flowable.com/open-source/docs/) |
| Form.io | [formio/formio](https://github.com/formio/formio) | [文档](https://help.form.io/) |
| Form.io Vue3 | [formio/vue](https://github.com/formio/vue) | [文档](https://github.com/formio/vue#readme) |
| Apache Superset | [apache/superset](https://github.com/apache/superset) | [文档](https://superset.apache.org/docs/intro) |
| DataV-Vue3 | [DataV-Team/DataV](https://github.com/DataV-Team/DataV) | [文档](http://datav.jiaminghi.com/) |
| uni-app | [dcloudio/uni-app](https://github.com/dcloudio/uni-app) | [文档](https://uniapp.dcloud.net.cn/) |

---

## 十、架构决策记录

### ADR-001: 基础框架选择 RuoYi-Vue-Plus
- **决策**：使用 dromara/RuoYi-Vue-Plus
- **理由**：dromara 社区维护，活跃度高，架构现代

### ADR-002: 在线表单选择 Form.io
- **决策**：使用 Form.io 替代自研或 form-create
- **理由**：全球低代码标杆，MIT 开源，功能完整（拖拽设计、主从表、动态 CRUD、代码导出），原生支持 PostgreSQL

### ADR-003: 报表引擎选择 Apache Superset
- **决策**：使用 Apache Superset
- **理由**：Apache 顶级项目，功能强大，社区活跃

### ADR-004: 大屏选择 DataV-Vue3
- **决策**：使用 DataV-Vue3 替代 GoView
- **理由**：纯前端方案，无需维护 Go 后端服务

### ADR-005: 数据库选择 PostgreSQL
- **决策**：直接使用 PostgreSQL 16
- **理由**：JSONB 支持更好，Form.io 原生支持

---

**完全自主、100% 开源的低代码平台！** 🚀
