# 🎯 JeecgBoot字段权限集成指南

## 概述

这是一个**业界首创**的Flowable设计器字段权限集成方案，让用户可以直接在流程设计器中可视化配置表单字段权限，实现**流程设计+权限配置一体化**。

## 🚀 功能特性

### ✅ **核心功能**
- 🔥 **一体化配置**：流程设计+权限配置一次完成
- 🔥 **可视化界面**：直观的字段权限配置表格
- 🔥 **智能默认策略**：零配置即可应用合理权限
- 🔥 **实时预览**：配置效果实时反馈
- 🔥 **JeecgBoot集成**：深度集成现有表单系统

### ✅ **用户体验**
- 📋 **字段权限表格**：支持可编辑、只读、隐藏、必填四种权限
- 🎨 **权限指示器**：在流程图上显示权限配置状态
- ⚡ **批量操作**：一键设置全部可编辑/只读/隐藏
- 🧠 **智能分类**：自动识别业务字段、流程字段、系统字段
- 💾 **配置持久化**：自动保存到BPMN扩展属性

## 📁 文件结构

```
jeecg-module-flowable/src/main/resources/static/modeler/
├── editor-app/
│   ├── configuration/
│   │   ├── properties-jeecg-field-permission-controller.js     # 主控制器
│   │   ├── properties-jeecg-field-permission-popup.html        # 弹窗模板
│   │   └── properties-jeecg-field-permission-property.html     # 属性面板模板
│   └── jeecg-field-permission-integration.js                   # 集成脚本
├── stencilsets/bpmn2.0/
│   └── properties-usertask-jeecg-extension.json               # 属性定义
└── JEECG_FIELD_PERMISSION_INTEGRATION.md                      # 本文档
```

## 🔧 集成步骤

### 步骤1：添加脚本引用

在Flowable设计器的主HTML页面中添加以下脚本引用：

```html
<!-- 在现有Flowable脚本之后添加 -->
<script src="editor-app/configuration/properties-jeecg-field-permission-controller.js"></script>
<script src="editor-app/jeecg-field-permission-integration.js"></script>
```

### 步骤2：修改用户任务属性配置

在 `app/editor/editor-app/configuration/properties-usertask-controller.js` 中添加字段权限属性：

```javascript
// 在用户任务属性数组中添加
{
    id: 'fieldpermission',
    type: 'field-permission-config',
    title: 'PROPERTY.FIELDPERMISSION.TITLE',
    templateUrl: 'editor-app/configuration/properties-jeecg-field-permission-property.html',
    condition: function(readModeOn, shape) {
        return !readModeOn && shape.stencil.idWithoutNs() === 'UserTask';
    }
}
```

### 步骤3：添加CSS样式

在设计器的主CSS文件中添加以下样式：

```css
/* JeecgBoot字段权限样式 */
.jeecg-permission-indicator {
    position: absolute !important;
    top: -8px !important;
    right: -8px !important;
    width: 16px !important;
    height: 16px !important;
    border-radius: 50% !important;
    background: #52c41a !important;
    color: white !important;
    font-size: 10px !important;
    display: flex !important;
    align-items: center !important;
    justify-content: center !important;
    z-index: 1000 !important;
    cursor: pointer !important;
}

.field-permission-tab {
    padding: 15px;
}

.field-permission-summary {
    margin: 10px 0;
    padding: 8px;
    background-color: #f0f9ff;
    border: 1px solid #b3e5fc;
    border-radius: 4px;
}
```

### 步骤4：配置后端API

确保以下API端点可用：

```
GET /workflow/node-permission/getFormFields?formId={formId}
```

返回格式：
```json
{
  "success": true,
  "result": [
    {
      "fieldName": "apply_title",
      "fieldLabel": "申请标题", 
      "fieldType": "input",
      "category": "business"
    }
  ]
}
```

## 🎯 使用方法

### 方法1：在设计器中配置

1. **打开Flowable设计器**
2. **选择用户任务节点**
3. **配置表单Key**（在"表单"选项卡中）
4. **点击"配置字段权限"按钮**
5. **可视化配置字段权限**
6. **保存配置**

### 方法2：快速操作

在属性面板中提供以下快速操作：
- 🪄 **智能默认**：一键应用智能权限策略
- 🧹 **清除配置**：清除所有权限配置

### 方法3：批量操作

在权限配置弹窗中支持：
- ✅ **全部可编辑**：设置所有字段为可编辑
- 👁️ **全部只读**：设置所有字段为只读  
- 🚫 **全部隐藏**：设置所有字段为隐藏

## 📊 智能权限策略

### 自动字段分类

- 🏢 **业务字段**：申请内容、业务数据等
- 🔄 **流程字段**：审批意见、处理说明等
- ⚙️ **系统字段**：ID、创建时间等
- 📎 **文件字段**：附件、图片等

### 智能权限规则

#### 发起节点（Start）
- ✅ 业务字段：**可编辑**
- 🚫 流程字段：**隐藏**  
- 🚫 系统字段：**隐藏**

#### 审批节点（UserTask）
- 👁️ 业务字段：**只读**
- ✅ 流程字段：**可编辑**
- 🚫 系统字段：**隐藏**

#### 查看节点（View）
- 👁️ 所有字段：**只读**

## 🔍 生成的BPMN配置

配置完成后，会在BPMN中生成以下扩展属性：

```xml
<bpmn:userTask id="userTask_review" name="审核节点">
  <bpmn:extensionElements>
    <jeecg:fieldPermissions>
      <jeecg:editableFields>["audit_opinion", "audit_result"]</jeecg:editableFields>
      <jeecg:readonlyFields>["apply_title", "apply_content"]</jeecg:readonlyFields>
      <jeecg:hiddenFields>["internal_notes"]</jeecg:hiddenFields>
      <jeecg:requiredFields>["audit_opinion"]</jeecg:requiredFields>
    </jeecg:fieldPermissions>
  </bpmn:extensionElements>
</bpmn:userTask>
```

## 🎨 界面预览

### 权限配置弹窗
```
┌─────────────────────────────────────────────────────────────┐
│ 🔒 字段权限配置 - maintenance_form                          │
├─────────────────────────────────────────────────────────────┤
│ [🪄智能默认] [✅全部可编辑] [👁️全部只读] [🚫全部隐藏]      │
├─────────────────────────────────────────────────────────────┤
│ 权限摘要：可编辑:3 | 只读:5 | 隐藏:2 | 必填:1                │
├─────────────────────────────────────────────────────────────┤
│ 字段名称    │ 类型   │ 分类     │ 权限设置 │ 必填 │ 操作     │
│ 申请标题    │ input  │ 业务字段 │ 可编辑   │ ☑   │ [✅👁️🚫] │
│ 申请内容    │ textarea│ 业务字段 │ 可编辑   │ ☑   │ [✅👁️🚫] │
│ 审批意见    │ textarea│ 流程字段 │ 只读     │ ☐   │ [✅👁️🚫] │
│ 创建时间    │ datetime│ 系统字段 │ 隐藏     │ ☐   │ [✅👁️🚫] │
└─────────────────────────────────────────────────────────────┘
                         [取消] [保存配置]
```

### 流程图权限指示器
```
   ┌─────────────────┐
   │                 │🔒  ← 权限配置指示器
   │   审核节点      │
   │                 │
   └─────────────────┘
```

## 🚀 高级特性

### 1. 条件权限（规划中）
支持基于条件的动态权限配置：
```json
{
  "conditionalPermissions": {
    "conditions": [
      {
        "expression": "${urgency_level == 'HIGH'}",
        "editableFields": ["special_approval_field"]
      }
    ]
  }
}
```

### 2. 权限模板（规划中）
支持权限配置模板，快速应用常用权限组合：
```javascript
templates: {
  "standard_approval": {
    "editableFields": ["audit_opinion", "audit_result"],
    "readonlyFields": ["apply_*"]
  }
}
```

### 3. 权限继承（规划中）
支持从上游节点继承权限配置，减少重复配置。

## 🛠️ 故障排查

### 常见问题

#### Q1：权限配置按钮不显示
**解决方案：**
1. 检查是否已配置表单Key
2. 确认脚本文件已正确加载
3. 检查控制台是否有JavaScript错误

#### Q2：字段列表为空
**解决方案：**
1. 确认后端API `/workflow/node-permission/getFormFields` 可用
2. 检查表单Key是否正确
3. 验证表单是否存在字段配置

#### Q3：权限配置不生效
**解决方案：**
1. 检查BPMN扩展属性是否正确生成
2. 确认后端权限解析器是否正常工作
3. 验证前端表单组件是否支持权限控制

### 调试模式

在浏览器控制台中执行以下代码启用调试：
```javascript
// 启用详细日志
window.JEECG_FIELD_PERMISSION_DEBUG = true;

// 查看当前权限配置
console.log(angular.element('[ng-controller="JeecgFieldPermissionCtrl"]').scope().fieldPermissionConfig);
```

## 📝 开发扩展

### 添加自定义字段分类

```javascript
// 在控制器中扩展字段分类逻辑
$scope.customCategorizeField = function(field) {
    if (field.fieldName.startsWith('custom_')) {
        return 'custom';
    }
    return field.category;
};
```

### 自定义权限策略

```javascript
// 添加自定义智能默认策略
$scope.customSmartDefaults = function() {
    // 自定义权限逻辑
    $scope.formFields.forEach(function(field) {
        if (field.customCondition) {
            field.permission = 'custom_permission';
        }
    });
};
```

## 🎉 集成效果

成功集成后，您将获得：

1. **🎯 一体化设计体验**：设计流程的同时配置权限
2. **🎨 直观的权限配置**：可视化表格，支持拖拽操作
3. **🧠 智能权限策略**：零配置即可应用合理默认值
4. **📊 实时权限预览**：配置效果立即可见
5. **🔗 深度JeecgBoot集成**：无缝对接现有表单系统

这是一个**革命性的工作流权限配置方案**，将大幅提升您的流程设计效率！🚀

---

**技术支持：** JeecgBoot开源社区  
**文档版本：** v1.0  
**最后更新：** 2024-12-25