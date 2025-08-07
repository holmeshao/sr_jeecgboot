/*
 * 🎯 JeecgBoot字段权限配置控制器
 * 在Flowable设计器中为用户任务添加字段权限配置功能
 * 
 * 功能特性：
 * 1. 用户任务属性面板增加"字段权限"配置项
 * 2. 可视化配置可编辑、只读、隐藏、必填字段
 * 3. 集成JeecgBoot表单字段API
 * 4. 智能默认权限策略
 * 5. 实时预览权限配置效果
 */

angular.module('flowableModeler')
  .controller('JeecgFieldPermissionCtrl', ['$rootScope', '$scope', '$http', '$translate', 
    function ($rootScope, $scope, $http, $translate) {

    // =============== 控制器初始化 ===============
    
    console.log('🎯 JeecgBoot字段权限配置控制器已加载');
    
    // 权限配置数据
    $scope.fieldPermissionConfig = {
      editableFields: [],
      readonlyFields: [],
      hiddenFields: [],
      requiredFields: []
    };
    
    // 表单字段列表
    $scope.formFields = [];
    $scope.formFieldsLoaded = false;
    $scope.loadingFields = false;
    
    // UI状态
    $scope.showPermissionPanel = false;
    $scope.selectedFormKey = '';
    
    // =============== 权限配置方法 ===============
    
    /**
     * 🎯 打开字段权限配置面板
     */
    $scope.openFieldPermissionPanel = function() {
      console.log('打开字段权限配置面板');
      
      // 获取当前选中的用户任务
      var selectedShape = $scope.selectedShape;
      if (!selectedShape || selectedShape.stencil.idWithoutNs !== 'UserTask') {
        alert('请选择用户任务节点');
        return;
      }
      
      // 获取表单Key
      var formKey = selectedShape.properties.formkey;
      if (!formKey) {
        alert('请先为该用户任务配置表单Key');
        return;
      }
      
      $scope.selectedFormKey = formKey;
      $scope.showPermissionPanel = true;
      
      // 加载表单字段
      $scope.loadFormFields(formKey);
      
      // 加载现有权限配置
      $scope.loadExistingPermissions(selectedShape);
    };
    
    /**
     * 🎯 加载表单字段列表
     */
    $scope.loadFormFields = function(formKey) {
      console.log('加载表单字段:', formKey);
      
      $scope.loadingFields = true;
      $scope.formFieldsLoaded = false;
      
      // 调用JeecgBoot API获取表单字段
      var apiUrl = '/workflow/node-permission/getFormFields?formId=' + encodeURIComponent(formKey);
      
      $http.get(apiUrl).then(function(response) {
        if (response.data && response.data.success) {
          $scope.formFields = response.data.result.map(function(field) {
            return {
              fieldName: field.fieldName,
              fieldLabel: field.fieldLabel || field.fieldName,
              fieldType: field.fieldType,
              category: field.category || 'business',
              permission: 'readonly', // 默认只读
              required: false
            };
          });
          
          $scope.formFieldsLoaded = true;
          console.log('表单字段加载成功:', $scope.formFields.length, '个字段');
          
          // 应用智能默认权限
          $scope.applySmartDefaults();
        } else {
          alert('加载表单字段失败: ' + (response.data.message || '未知错误'));
        }
      }).catch(function(error) {
        console.error('加载表单字段失败:', error);
        alert('加载表单字段失败，请检查网络连接');
      }).finally(function() {
        $scope.loadingFields = false;
      });
    };
    
    /**
     * 🎯 加载现有权限配置
     */
    $scope.loadExistingPermissions = function(selectedShape) {
      try {
        // 从BPMN扩展属性中读取现有配置
        var extensionElements = selectedShape.properties.extensionelements;
        if (extensionElements && extensionElements.values) {
          var fieldPermissions = extensionElements.values.find(function(ext) {
            return ext.elementType === 'jeecg:fieldPermissions';
          });
          
          if (fieldPermissions) {
            console.log('发现现有权限配置:', fieldPermissions);
            $scope.parseExistingPermissions(fieldPermissions);
          }
        }
      } catch (error) {
        console.warn('解析现有权限配置失败:', error);
      }
    };
    
    /**
     * 🎯 解析现有权限配置
     */
    $scope.parseExistingPermissions = function(fieldPermissions) {
      try {
        if (fieldPermissions.editableFields) {
          $scope.fieldPermissionConfig.editableFields = JSON.parse(fieldPermissions.editableFields);
        }
        if (fieldPermissions.readonlyFields) {
          $scope.fieldPermissionConfig.readonlyFields = JSON.parse(fieldPermissions.readonlyFields);
        }
        if (fieldPermissions.hiddenFields) {
          $scope.fieldPermissionConfig.hiddenFields = JSON.parse(fieldPermissions.hiddenFields);
        }
        if (fieldPermissions.requiredFields) {
          $scope.fieldPermissionConfig.requiredFields = JSON.parse(fieldPermissions.requiredFields);
        }
        
        // 更新字段权限状态
        $scope.updateFieldPermissionStates();
        
      } catch (error) {
        console.error('解析权限配置失败:', error);
      }
    };
    
    /**
     * 🎯 应用智能默认权限策略
     */
    $scope.applySmartDefaults = function() {
      console.log('应用智能默认权限策略');
      
      var selectedShape = $scope.selectedShape;
      var nodeName = selectedShape.properties.name || selectedShape.resourceId;
      var isStartNode = nodeName.toLowerCase().includes('start') || 
                       nodeName.toLowerCase().includes('发起') ||
                       nodeName.toLowerCase().includes('申请');
      
      $scope.formFields.forEach(function(field) {
        if (field.category === 'workflow') {
          // 通用流程字段：发起节点隐藏，其他节点可编辑
          field.permission = isStartNode ? 'hidden' : 'editable';
        } else if (field.category === 'system') {
          // 系统字段：总是隐藏
          field.permission = 'hidden';
        } else {
          // 业务字段：发起节点可编辑，其他节点只读
          field.permission = isStartNode ? 'editable' : 'readonly';
        }
      });
      
      console.log('智能默认权限已应用');
    };
    
    /**
     * 🎯 更新字段权限状态
     */
    $scope.updateFieldPermissionStates = function() {
      $scope.formFields.forEach(function(field) {
        if ($scope.fieldPermissionConfig.editableFields.includes(field.fieldName)) {
          field.permission = 'editable';
        } else if ($scope.fieldPermissionConfig.hiddenFields.includes(field.fieldName)) {
          field.permission = 'hidden';
        } else {
          field.permission = 'readonly';
        }
        
        field.required = $scope.fieldPermissionConfig.requiredFields.includes(field.fieldName);
      });
    };
    
    /**
     * 🎯 字段权限变更事件
     */
    $scope.onFieldPermissionChange = function(field) {
      console.log('字段权限变更:', field.fieldName, '->', field.permission);
      
      // 从所有权限列表中移除该字段
      $scope.removeFieldFromAllPermissions(field.fieldName);
      
      // 根据新权限添加到对应列表
      switch (field.permission) {
        case 'editable':
          $scope.fieldPermissionConfig.editableFields.push(field.fieldName);
          break;
        case 'hidden':
          $scope.fieldPermissionConfig.hiddenFields.push(field.fieldName);
          // 隐藏字段不能是必填的
          field.required = false;
          break;
        default:
          $scope.fieldPermissionConfig.readonlyFields.push(field.fieldName);
          break;
      }
      
      $scope.updateRequiredFields();
    };
    
    /**
     * 🎯 必填状态变更事件
     */
    $scope.onFieldRequiredChange = function(field) {
      console.log('字段必填状态变更:', field.fieldName, '->', field.required);
      
      if (field.required && field.permission !== 'hidden') {
        if (!$scope.fieldPermissionConfig.requiredFields.includes(field.fieldName)) {
          $scope.fieldPermissionConfig.requiredFields.push(field.fieldName);
        }
      } else {
        $scope.removeFieldFromRequired(field.fieldName);
      }
    };
    
    /**
     * 🎯 从所有权限列表中移除字段
     */
    $scope.removeFieldFromAllPermissions = function(fieldName) {
      $scope.fieldPermissionConfig.editableFields = 
        $scope.fieldPermissionConfig.editableFields.filter(f => f !== fieldName);
      $scope.fieldPermissionConfig.readonlyFields = 
        $scope.fieldPermissionConfig.readonlyFields.filter(f => f !== fieldName);
      $scope.fieldPermissionConfig.hiddenFields = 
        $scope.fieldPermissionConfig.hiddenFields.filter(f => f !== fieldName);
    };
    
    /**
     * 🎯 从必填列表中移除字段
     */
    $scope.removeFieldFromRequired = function(fieldName) {
      $scope.fieldPermissionConfig.requiredFields = 
        $scope.fieldPermissionConfig.requiredFields.filter(f => f !== fieldName);
    };
    
    /**
     * 🎯 更新必填字段列表
     */
    $scope.updateRequiredFields = function() {
      $scope.fieldPermissionConfig.requiredFields = $scope.formFields
        .filter(f => f.required && f.permission !== 'hidden')
        .map(f => f.fieldName);
    };
    
    // =============== 批量操作方法 ===============
    
    /**
     * 🎯 批量设置权限
     */
    $scope.batchSetPermission = function(permission) {
      $scope.formFields.forEach(function(field) {
        if (field.category !== 'system') { // 系统字段不参与批量操作
          field.permission = permission;
          if (permission === 'hidden') {
            field.required = false;
          }
        }
      });
      
      $scope.rebuildPermissionConfig();
    };
    
    /**
     * 🎯 重建权限配置
     */
    $scope.rebuildPermissionConfig = function() {
      $scope.fieldPermissionConfig = {
        editableFields: $scope.formFields.filter(f => f.permission === 'editable').map(f => f.fieldName),
        readonlyFields: $scope.formFields.filter(f => f.permission === 'readonly').map(f => f.fieldName),
        hiddenFields: $scope.formFields.filter(f => f.permission === 'hidden').map(f => f.fieldName),
        requiredFields: $scope.formFields.filter(f => f.required && f.permission !== 'hidden').map(f => f.fieldName)
      };
    };
    
    /**
     * 🎯 保存权限配置
     */
    $scope.saveFieldPermissions = function() {
      console.log('保存字段权限配置:', $scope.fieldPermissionConfig);
      
      var selectedShape = $scope.selectedShape;
      
      try {
        // 创建或更新BPMN扩展属性
        $scope.updateBpmnExtensionProperties(selectedShape);
        
        // 触发属性变更事件
        $rootScope.$broadcast('property-value-changed', {
          element: selectedShape,
          property: 'fieldPermissions',
          oldValue: null,
          newValue: $scope.fieldPermissionConfig
        });
        
        alert('字段权限配置已保存！');
        $scope.closePermissionPanel();
        
      } catch (error) {
        console.error('保存权限配置失败:', error);
        alert('保存失败: ' + error.message);
      }
    };
    
    /**
     * 🎯 更新BPMN扩展属性
     */
    $scope.updateBpmnExtensionProperties = function(selectedShape) {
      // 确保extensionElements存在
      if (!selectedShape.properties.extensionelements) {
        selectedShape.properties.extensionelements = { values: [] };
      }
      
      // 查找或创建fieldPermissions扩展元素
      var fieldPermissionsElement = selectedShape.properties.extensionelements.values.find(function(ext) {
        return ext.elementType === 'jeecg:fieldPermissions';
      });
      
      if (!fieldPermissionsElement) {
        fieldPermissionsElement = {
          elementType: 'jeecg:fieldPermissions',
          namespace: 'http://jeecg.org/bpmn',
          attributes: {}
        };
        selectedShape.properties.extensionelements.values.push(fieldPermissionsElement);
      }
      
      // 更新权限配置属性
      fieldPermissionsElement.editableFields = JSON.stringify($scope.fieldPermissionConfig.editableFields);
      fieldPermissionsElement.readonlyFields = JSON.stringify($scope.fieldPermissionConfig.readonlyFields);
      fieldPermissionsElement.hiddenFields = JSON.stringify($scope.fieldPermissionConfig.hiddenFields);
      fieldPermissionsElement.requiredFields = JSON.stringify($scope.fieldPermissionConfig.requiredFields);
      
      console.log('BPMN扩展属性已更新:', fieldPermissionsElement);
    };
    
    /**
     * 🎯 关闭权限配置面板
     */
    $scope.closePermissionPanel = function() {
      $scope.showPermissionPanel = false;
      $scope.formFields = [];
      $scope.formFieldsLoaded = false;
      $scope.selectedFormKey = '';
      
      // 重置权限配置
      $scope.fieldPermissionConfig = {
        editableFields: [],
        readonlyFields: [],
        hiddenFields: [],
        requiredFields: []
      };
    };
    
    /**
     * 🎯 获取权限显示文本
     */
    $scope.getPermissionText = function(permission) {
      var textMap = {
        'editable': '可编辑',
        'readonly': '只读',
        'hidden': '隐藏'
      };
      return textMap[permission] || permission;
    };
    
    /**
     * 🎯 获取字段分类显示文本
     */
    $scope.getCategoryText = function(category) {
      var textMap = {
        'business': '业务字段',
        'workflow': '流程字段',
        'system': '系统字段',
        'file': '文件字段'
      };
      return textMap[category] || category;
    };
    
    /**
     * 🎯 获取权限配置摘要
     */
    $scope.getPermissionSummary = function() {
      var editable = $scope.fieldPermissionConfig.editableFields.length;
      var readonly = $scope.fieldPermissionConfig.readonlyFields.length;
      var hidden = $scope.fieldPermissionConfig.hiddenFields.length;
      var required = $scope.fieldPermissionConfig.requiredFields.length;
      
      return `可编辑:${editable} | 只读:${readonly} | 隐藏:${hidden} | 必填:${required}`;
    };
    
    // =============== 监听器 ===============
    
    // 监听形状选择变化
    $scope.$on('event:property-value-changed', function(event, data) {
      if (data.property === 'formkey') {
        console.log('表单Key已变更:', data.newValue);
        if ($scope.showPermissionPanel) {
          $scope.selectedFormKey = data.newValue;
          if (data.newValue) {
            $scope.loadFormFields(data.newValue);
          }
        }
      }
    });
    
    console.log('🎯 JeecgBoot字段权限配置控制器初始化完成');
}]);