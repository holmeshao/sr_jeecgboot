<route lang="json5" type="page">
{
  layout: 'default',
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: 'Online表单编辑',
    disableScroll: true, // 微信禁止页面滚动
    'app-plus': {
      bounce: 'none', // 禁用 iOS 弹性效果
    },
  },
}
</route>

<template>
  <PageLayout :navTitle="navTitle" :backRouteName="backRouteName">
    <scroll-view scroll-y>
      <!-- 🎯 使用增强的工作流表单组件 -->
      <workflow-mobile-form
        v-if="reload && useWorkflowForm"
        ref="workflowFormRef"
        :table="dynamicTableName"
        :data-id="dataId"
        :task-id="taskId"
        :edit="true"
        :flow-edit="flowEdit"
        :workflow-buttons="workflowButtons"
        :need-comment="needComment"
        :show-workflow-actions="hasWorkflowTask"
        @success="handleWorkflowSuccess"
        @save="handleSave"
        @submit="handleSubmit"
        @workflow-action="handleWorkflowAction"
      />
      
      <!-- 🎯 兼容原有的在线表单组件 -->
      <online-loader
        v-else-if="reload"
        ref="onlineEdit"
        :table="dynamicTableName"
        :dataId="dataId"
        :title="navTitle"
        :edit="true"
        show-footer
        @success="handleSuccess"
        @back="backRoute"
      />
    </scroll-view>
  </PageLayout>
</template>

<script lang="ts" setup>
import OnlineLoader from '@/components/online/online-loader.vue'
import WorkflowMobileForm from '@/components/workflow/workflow-mobile-form.vue'
import router from '@/router'
import { onLoad } from '@dcloudio/uni-app'
import { http } from '@/utils/http'
import { useToast } from 'wot-design-uni'
import { isMp, isH5 } from '@/utils/platform'
import { getRefPromise } from "@/utils"
import { generateMobileWorkflowButtons, handleMobileButtonClick } from '@/utils/workflow/mobile-button-manager'
import type { MobileWorkflowButton } from '@/components/workflow/workflow-mobile-form.vue';
defineOptions({
  name: 'onlineEdit',
  options: {
    styleIsolation: 'shared',
  },
})
const toast = useToast()
// 定义响应式数据
const tableName = ref('')
const navTitle = ref('')
const dataId = ref('')
const backRouteName = ref('')
const process_url = ref('/act/process/extActProcess/startMutilProcess')
const flow_code_pre = ref('onl_')
const flowEdit = ref(false)
const edit = ref(true)
const reload = ref(true)

// 🎯 新增工作流相关数据
const taskId = ref('')
const useWorkflowForm = ref(false)
const workflowButtons = ref<MobileWorkflowButton[]>([])
const needComment = ref(false)
const hasWorkflowTask = ref(false)

// 引用组件
const onlineEdit = ref(null)
const workflowFormRef = ref(null)
// 🎯 增强的initForm方法，支持工作流检测
const initForm = async (item) => {
  console.log('initForm item', item)
  // 表描述
  navTitle.value = `表单【${item.desformName}】`
  flowEdit.value = item.backRouteName == 'draft' ? true : false
  // 返回上一页面
  item.backRouteName && (backRouteName.value = item.backRouteName)
  
  // 🎯 检测是否使用工作流表单
  taskId.value = item.taskId || ''
  await checkWorkflowMode(item)
  
  reload.value = false
  nextTick(() => {
    reload.value = true
    // 表名
    tableName.value = item.desformCode
    // 数据ID
    dataId.value = item.dataId
    
    let delay = 0
    if (isH5 === false) {
      // 小程序端需要延时下，否则不显示
      delay = 300
    }
    
    setTimeout(() => {
      if (useWorkflowForm.value) {
        // 使用工作流表单
        loadWorkflowButtons()
      } else {
        // 使用原有表单
        getRefPromise(onlineEdit).then(() => {
          onlineEdit.value?.loadByTableName(item.dataId, item.desformCode)
        })
      }
    }, delay)
  })
}

// 🎯 检测工作流模式
const checkWorkflowMode = async (item) => {
  try {
    // 检查是否有任务ID或者是工作流编辑模式
    if (item.taskId || item.backRouteName === 'draft') {
      useWorkflowForm.value = true
      hasWorkflowTask.value = !!item.taskId
      needComment.value = hasWorkflowTask.value
      
      console.log('启用工作流表单模式', {
        taskId: item.taskId,
        hasWorkflowTask: hasWorkflowTask.value
      })
    } else {
      useWorkflowForm.value = false
      hasWorkflowTask.value = false
      needComment.value = false
    }
  } catch (error) {
    console.error('检测工作流模式失败:', error)
    useWorkflowForm.value = false
  }
}

// 🎯 加载工作流按钮
const loadWorkflowButtons = async () => {
  try {
    if (hasWorkflowTask.value && taskId.value) {
      const buttons = await generateMobileWorkflowButtons(
        taskId.value,
        undefined,
        tableName.value
      )
      workflowButtons.value = buttons
      console.log('加载移动端工作流按钮:', buttons)
    } else {
      workflowButtons.value = []
    }
  } catch (error) {
    console.error('加载工作流按钮失败:', error)
    workflowButtons.value = []
  }
}
const dynamicTableName = computed(() => {
  return tableName.value
})
// 开启流程
const startProcess = (id) => {
  const param = {
    flowCode: flow_code_pre.value + tableName.value,
    id: id,
    formUrl: 'modules/bpm/task/form/OnlineFormDetail',
    formUrlMobile: 'check/onlineForm/detail',
  }
  console.log('提交流程参数', param)
  http.post(process_url.value, param).then((res: any) => {
    toast.info(res.message)
    if (res.success) {
      uni.$emit('draft:reload')
      router.back()
    }
  })
}

const backRoute = () => {
  router.back()
}

// 定义 handleSuccess 方法
const handleSuccess = (id) => {
  if (backRouteName.value === 'draft') {
    uni.showModal({
      title: '提示',
      content: '确认提交流程吗?',
      cancelText: '取消',
      confirmText: '确认',
      success: (res) => {
        if (res.confirm) {
          startProcess(id)
          uni.showToast({
            title: '发起流程成功~',
            icon: 'none',
          })
        } else {
          router.back()
        }
      },
    })
  } else {
    uni.$emit('refreshList')
    backRoute()
  }
}

// 🎯 新增工作流事件处理方法
const handleWorkflowSuccess = (data) => {
  console.log('工作流表单成功:', data)
  // 可以在这里处理成功后的逻辑
  toast.success('表单处理成功')
  router.back()
}

const handleSave = (data) => {
  console.log('保存草稿:', data)
  toast.success('保存成功')
}

const handleSubmit = (data) => {
  console.log('提交表单:', data)
  toast.success('提交成功')
  router.back()
}

const handleWorkflowAction = async (button, comment, data) => {
  try {
    console.log('执行工作流动作:', { button, comment, data })
    
    // 调用移动端工作流动作处理
    const result = await handleMobileButtonClick(
      button.code,
      taskId.value,
      undefined,
      comment,
      data
    )
    
    toast.success(`${button.label}成功`)
    
    // 重新加载按钮状态
    await loadWorkflowButtons()
    
    // 根据动作类型决定是否返回上一页
    if (['approve', 'reject', 'return'].includes(button.code)) {
      router.back()
    }
    
  } catch (error) {
    console.error('工作流动作执行失败:', error)
    toast.error(`${button.label}失败: ${error.message}`)
  }
}

// onLoad 生命周期钩子
onLoad((option) => {
  initForm(option)
})
</script>

<style lang="scss" scoped>
//
</style>
