# BPMN流程设计器集成指南

## 概述

已成功在JeecgBoot项目中集成了 **bpmn-js** 流程设计器，提供完整的可视化BPMN流程设计功能。

## 功能特性

✅ **可视化设计** - 拖拽式流程设计界面  
✅ **BPMN 2.0标准** - 完全符合BPMN规范  
✅ **Flowable兼容** - 与后端Flowable引擎完美兼容  
✅ **属性面板** - 完整的流程元素属性配置  
✅ **导入导出** - 支持.bpmn文件的导入和导出  
✅ **一键部署** - 设计完成后直接部署到Flowable引擎  

## 安装依赖

在前端项目目录下执行以下命令：

```bash
# 进入前端项目目录
cd jeecgboot-vue3

# 安装核心依赖
npm install bpmn-js bpmn-js-properties-panel

# 可选：安装额外插件（用于增强功能）
npm install bpmn-js-token-simulation  # 流程仿真
npm install diagram-js-minimap        # 小地图导航
```

### 如果npm安装遇到问题，可以使用以下替代方案：

```bash
# 使用yarn
yarn add bpmn-js bpmn-js-properties-panel

# 使用pnpm
pnpm add bpmn-js bpmn-js-properties-panel

# 使用cnpm
cnpm install bpmn-js bpmn-js-properties-panel
```

## 文件结构

```
jeecgboot-vue3/
├── src/
│   ├── views/workflow/designer/
│   │   └── index.vue                    # BPMN设计器主页面
│   ├── router/routes/modules/
│   │   └── workflow.ts                  # 路由配置（已更新）
│   ├── locales/lang/zh-CN/routes/
│   │   └── workflow.ts                  # 国际化配置（已更新）
│   └── api/workflow/
│       └── index.ts                     # API接口（已有deploy方法）
```

## 使用方法

### 1. 访问设计器

启动项目后，通过以下方式访问流程设计器：

- **直接访问**: `http://localhost:3100/workflow/designer`
- **菜单导航**: 工作流管理 → 流程设计器
- **按钮跳转**: 在"流程定义"页面点击"流程设计器"按钮

### 2. 设计器界面说明

#### 顶部工具栏
- **新建流程**: 创建新的空白流程
- **导入BPMN**: 从本地导入.bpmn文件
- **导出BPMN**: 将当前流程导出为.bpmn文件
- **保存草稿**: 保存流程到本地存储（可扩展为后端API）
- **部署流程**: 将流程部署到Flowable引擎
- **显示/隐藏属性面板**: 切换右侧属性配置面板

#### 主设计区域
- **左侧工具面板**: 包含各种BPMN元素（开始事件、任务、网关等）
- **中央画布**: 拖拽设计流程图
- **右侧属性面板**: 配置选中元素的详细属性

### 3. 基本操作流程

1. **创建新流程**
   ```
   点击"新建流程" → 画布显示默认模板 → 开始设计
   ```

2. **添加流程元素**
   ```
   从左侧面板拖拽元素 → 放置到画布 → 配置属性
   ```

3. **配置任务属性**
   ```
   选择任务元素 → 在右侧属性面板配置：
   - 任务名称
   - 分配用户 (Assignee)
   - 表单属性 (Form Properties)
   - 执行监听器
   ```

4. **保存和部署**
   ```
   设计完成 → 点击"部署流程" → 填写部署信息 → 确认部署
   ```

## 与现有工作流的集成

### 流程启动
```javascript
// 前端启动流程
const startParams = {
  processDefinitionKey: "your_process_key",  // 设计器中定义的流程ID
  businessKey: "BUSINESS_123",
  variables: {
    // 流程变量
  }
}
workflowInstanceApi.start(startParams)
```

### 后端启动流程
```java
// 通过设计器定义的流程Key启动
ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
    "your_process_key", businessKey, variables);
```

## 高级配置

### 1. 自定义调色板
如需自定义工具面板，可以在设计器组件中添加：

```javascript
import CustomPaletteProvider from './custom-palette';

const modeler = new BpmnModeler({
  container: bpmnContainer.value,
  additionalModules: [
    BpmnPropertiesPanel,
    {
      paletteProvider: ['type', CustomPaletteProvider]
    }
  ]
});
```

### 2. 扩展属性面板
添加自定义属性配置：

```javascript
import customDescriptor from './custom-descriptor.json';

const modeler = new BpmnModeler({
  moddleExtensions: {
    flowable: flowableModdleDescriptor,
    custom: customDescriptor
  }
});
```

### 3. 集成表单设计器
可以与JeecgBoot的Online表单功能结合：

```javascript
// 在用户任务的表单属性中指定Online表单ID
<flowable:formProperty id="formKey" name="表单标识" value="online_form_123" />
```

## 故障排除

### 1. 依赖安装失败
```bash
# 清除缓存
npm cache clean --force

# 重新安装
npm install

# 如果仍然失败，删除node_modules重新安装
rm -rf node_modules package-lock.json
npm install
```

### 2. 设计器加载失败
检查浏览器控制台是否有以下错误：
- 模块加载错误：确认依赖包已正确安装
- 样式缺失：确认CSS文件已正确引入

### 3. 流程部署失败
- 检查BPMN XML格式是否正确
- 确认后端Flowable服务正常运行
- 检查流程ID是否唯一

## 扩展建议

1. **集成流程仿真**: 安装`bpmn-js-token-simulation`插件，支持流程执行预览
2. **添加小地图**: 安装`diagram-js-minimap`插件，方便大型流程图导航
3. **自定义元素**: 创建业务特定的BPMN元素和属性
4. **版本管理**: 结合Git等版本控制工具管理流程定义文件
5. **权限控制**: 基于用户角色控制设计器功能访问权限

## 相关资源

- [bpmn-js官方文档](https://bpmn.io/toolkit/bpmn-js/)
- [BPMN 2.0规范](https://www.omg.org/spec/BPMN/2.0/)
- [Flowable用户指南](https://www.flowable.org/docs/userguide/index.html)
- [JeecgBoot文档](http://doc.jeecg.com/)

## 贡献

如果您在使用过程中发现问题或有改进建议，欢迎提交Issue或Pull Request。