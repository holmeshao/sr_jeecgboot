/*
 * 🎯 JeecgBoot字段权限集成脚本
 * 将字段权限配置功能集成到Flowable设计器中
 * 
 * 集成方式：
 * 1. 在用户任务属性面板添加"字段权限"选项卡
 * 2. 提供可视化的字段权限配置界面
 * 3. 支持智能默认权限策略
 * 4. 自动生成BPMN扩展属性
 */

(function() {
    'use strict';
    
    console.log('🎯 开始集成JeecgBoot字段权限功能到Flowable设计器');
    
    // 等待Flowable应用初始化完成
    function waitForFlowableApp() {
        if (typeof angular !== 'undefined' && angular.module) {
            try {
                var flowableApp = angular.module('flowableModeler');
                if (flowableApp) {
                    console.log('✅ Flowable应用已加载，开始集成字段权限功能');
                    integrateFieldPermission();
                    return;
                }
            } catch (e) {
                // Flowable应用还未加载
            }
        }
        
        // 如果应用还未加载，等待100ms后重试
        setTimeout(waitForFlowableApp, 100);
    }
    
    /**
     * 🎯 集成字段权限功能
     */
    function integrateFieldPermission() {
        
        // 1. 修改用户任务的属性面板配置
        modifyUserTaskProperties();
        
        // 2. 注册字段权限相关的翻译
        registerTranslations();
        
        // 3. 扩展BPMN序列化功能
        extendBpmnSerialization();
        
        // 4. 添加节点图标指示器
        addNodeIndicators();
        
        console.log('✅ JeecgBoot字段权限功能集成完成');
    }
    
    /**
     * 🎯 修改用户任务属性面板
     */
    function modifyUserTaskProperties() {
        
        // 获取现有的用户任务配置
        var originalConfig = KISBPM.TOOLBAR.ACTIONS.saveModel.prototype;
        
        // 监听属性面板加载事件
        if (typeof ORYX !== 'undefined' && ORYX.Plugins) {
            
            // 扩展属性面板插件
            ORYX.Plugins.PropertyWindow = ORYX.Plugins.PropertyWindow.extend({
                
                // 重写属性面板构建方法
                buildPropertyWindow: function(originalMethod) {
                    return function() {
                        var result = originalMethod.apply(this, arguments);
                        
                        // 如果是用户任务，添加字段权限选项卡
                        if (this.shapeSelection && 
                            this.shapeSelection.length === 1 && 
                            this.shapeSelection[0].getStencil().idWithoutNs() === 'UserTask') {
                            
                            this.addFieldPermissionTab();
                        }
                        
                        return result;
                    };
                }(ORYX.Plugins.PropertyWindow.prototype.buildPropertyWindow),
                
                /**
                 * 添加字段权限选项卡
                 */
                addFieldPermissionTab: function() {
                    try {
                        var propertyWindow = this.propertyWindow;
                        var selectedShape = this.shapeSelection[0];
                        
                        // 创建字段权限选项卡
                        var fieldPermissionTab = $('<div class="field-permission-tab"></div>');
                        fieldPermissionTab.load('/modeler/editor-app/configuration/properties-jeecg-field-permission-property.html');
                        
                        // 添加到属性面板
                        var tabContainer = propertyWindow.find('.property-tabs');
                        if (tabContainer.length === 0) {
                            tabContainer = $('<div class="property-tabs"></div>');
                            propertyWindow.append(tabContainer);
                        }
                        
                        // 添加选项卡标题
                        var tabHeader = $('<li class="field-permission-tab-header">' +
                                         '<a href="#field-permission-content" data-toggle="tab">' +
                                         '<i class="fa fa-lock"></i> 字段权限</a></li>');
                        
                        var tabContent = $('<div id="field-permission-content" class="tab-pane">' +
                                          '</div>');
                        tabContent.append(fieldPermissionTab);
                        
                        // 查找并添加到现有选项卡
                        var existingTabs = propertyWindow.find('.nav-tabs');
                        if (existingTabs.length > 0) {
                            existingTabs.append(tabHeader);
                        }
                        
                        var existingContent = propertyWindow.find('.tab-content');
                        if (existingContent.length > 0) {
                            existingContent.append(tabContent);
                        }
                        
                        console.log('✅ 字段权限选项卡已添加到属性面板');
                        
                    } catch (error) {
                        console.error('❌ 添加字段权限选项卡失败:', error);
                    }
                }
            });
        }
    }
    
    /**
     * 🎯 注册翻译文本
     */
    function registerTranslations() {
        try {
            if (typeof FLOWABLE_EDITOR_CONFIG !== 'undefined' && FLOWABLE_EDITOR_CONFIG.contextRoot) {
                
                // 添加中文翻译
                var translations = {
                    'PROPERTY.FIELDPERMISSION.TITLE': '字段权限配置',
                    'PROPERTY.FIELDPERMISSION.DESCRIPTION': '配置表单字段的编辑、显示和必填权限',
                    'PROPERTY.FIELDPERMISSION.EDITABLE': '可编辑字段',
                    'PROPERTY.FIELDPERMISSION.READONLY': '只读字段',
                    'PROPERTY.FIELDPERMISSION.HIDDEN': '隐藏字段',
                    'PROPERTY.FIELDPERMISSION.REQUIRED': '必填字段',
                    'PROPERTY.FIELDPERMISSION.SMART_DEFAULT': '智能默认',
                    'PROPERTY.FIELDPERMISSION.CLEAR_CONFIG': '清除配置'
                };
                
                // 尝试注册到翻译系统
                if (window.$translate && window.$translate.use) {
                    window.$translate.use().then(function(currentLang) {
                        angular.extend(window.FLOWABLE_EDITOR_CONFIG.translations[currentLang], translations);
                    });
                }
                
                console.log('✅ 字段权限翻译已注册');
            }
        } catch (error) {
            console.warn('⚠️ 注册翻译失败:', error);
        }
    }
    
    /**
     * 🎯 扩展BPMN序列化功能
     */
    function extendBpmnSerialization() {
        try {
            // 确保字段权限扩展属性能正确序列化到BPMN
            if (typeof KISBPM !== 'undefined' && KISBPM.BPMN20_NAMESPACE) {
                
                // 添加JeecgBoot命名空间
                KISBPM.JEECG_NAMESPACE = 'http://jeecg.org/bpmn';
                KISBPM.JEECG_NAMESPACE_PREFIX = 'jeecg';
                
                // 扩展序列化方法
                var originalSerialize = KISBPM.REPOSITORY.getModel;
                KISBPM.REPOSITORY.getModel = function() {
                    var model = originalSerialize.apply(this, arguments);
                    
                    // 确保包含JeecgBoot扩展属性
                    if (model && model.definition && model.definition.properties) {
                        model.definition.properties.namespace = model.definition.properties.namespace || '';
                        if (model.definition.properties.namespace.indexOf(KISBPM.JEECG_NAMESPACE) === -1) {
                            model.definition.properties.namespace += 
                                ' xmlns:' + KISBPM.JEECG_NAMESPACE_PREFIX + '="' + KISBPM.JEECG_NAMESPACE + '"';
                        }
                    }
                    
                    return model;
                };
                
                console.log('✅ BPMN序列化功能已扩展');
            }
        } catch (error) {
            console.warn('⚠️ 扩展BPMN序列化失败:', error);
        }
    }
    
    /**
     * 🎯 添加节点权限配置指示器
     */
    function addNodeIndicators() {
        try {
            // 在流程图上为配置了字段权限的节点添加视觉指示器
            if (typeof ORYX !== 'undefined' && ORYX.Core && ORYX.Core.Canvas) {
                
                var originalUpdate = ORYX.Core.Canvas.prototype.update;
                ORYX.Core.Canvas.prototype.update = function() {
                    var result = originalUpdate.apply(this, arguments);
                    
                    // 为配置了字段权限的用户任务添加图标
                    this.getChildNodes().forEach(function(node) {
                        if (node.getStencil().idWithoutNs() === 'UserTask') {
                            addPermissionIndicator(node);
                        }
                    });
                    
                    return result;
                };
                
                /**
                 * 为节点添加权限配置指示器
                 */
                function addPermissionIndicator(node) {
                    try {
                        var hasPermissionConfig = false;
                        
                        // 检查是否配置了字段权限
                        if (node.properties && node.properties.extensionelements && 
                            node.properties.extensionelements.values) {
                            
                            hasPermissionConfig = node.properties.extensionelements.values.some(function(ext) {
                                return ext.elementType === 'jeecg:fieldPermissions';
                            });
                        }
                        
                        // 移除现有指示器
                        node.node.querySelectorAll('.jeecg-permission-indicator').forEach(function(indicator) {
                            indicator.remove();
                        });
                        
                        // 如果有权限配置，添加指示器
                        if (hasPermissionConfig) {
                            var indicator = document.createElement('div');
                            indicator.className = 'jeecg-permission-indicator';
                            indicator.innerHTML = '🔒';
                            indicator.style.cssText = 
                                'position: absolute; top: -8px; right: -8px; ' +
                                'width: 16px; height: 16px; border-radius: 50%; ' +
                                'background: #52c41a; color: white; font-size: 10px; ' +
                                'display: flex; align-items: center; justify-content: center; ' +
                                'z-index: 1000; cursor: pointer;';
                            
                            indicator.title = '已配置字段权限';
                            node.node.appendChild(indicator);
                        }
                        
                    } catch (error) {
                        console.warn('添加权限指示器失败:', error);
                    }
                }
                
                console.log('✅ 节点权限指示器功能已添加');
            }
        } catch (error) {
            console.warn('⚠️ 添加节点指示器失败:', error);
        }
    }
    
    // 开始集成过程
    waitForFlowableApp();
    
})();

// 🎯 在页面加载完成后自动加载集成脚本
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        console.log('🎯 页面加载完成，JeecgBoot字段权限集成脚本已启动');
    });
} else {
    console.log('🎯 JeecgBoot字段权限集成脚本已启动');
}