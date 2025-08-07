import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
import { t } from '@/hooks/web/useI18n';

const workflow: AppRouteModule = {
  path: '/workflow',
  name: 'Workflow',
  component: LAYOUT,
  redirect: '/workflow/task',
  meta: {
    orderNo: 30,
    icon: 'ion:git-branch-outline',
    title: t('routes.workflow.moduleName'),
  },
  children: [
    {
      path: 'task',
      name: 'WorkflowTask',
      component: () => import('@/views/workflow/task/index.vue'),
      meta: {
        title: t('routes.workflow.task'),
        icon: 'ion:list-outline',
      },
    },
    {
      path: 'definition',
      name: 'WorkflowDefinition',
      component: () => import('@/views/workflow/definition/index.vue'),
      meta: {
        title: t('routes.workflow.definition'),
        icon: 'ion:git-network-outline',
      },
    },
    {
      path: 'designer',
      name: 'WorkflowDesigner',
      component: () => import('@/views/workflow/designer/index.vue'),
      meta: {
        title: t('routes.workflow.designer'),
        icon: 'ion:code-working-outline',
      },
    },
    {
      path: 'history',
      name: 'WorkflowHistory',
      component: () => import('@/views/workflow/history/index.vue'),
      meta: {
        title: t('routes.workflow.history'),
        icon: 'ion:time-outline',
      },
    },
    {
      path: 'monitor',
      name: 'WorkflowMonitor',
      component: () => import('@/views/workflow/monitor/index.vue'),
      meta: {
        title: t('routes.workflow.monitor'),
        icon: 'ion:bar-chart-outline',
      },
    },
    {
      path: 'onlineForm',
      name: 'WorkflowOnlineForm',
      redirect: '/workflow/onlineForm/config',
      meta: {
        title: t('routes.workflow.onlineForm'),
        icon: 'ion:document-text-outline',
      },
      children: [
        {
          path: 'config',
          name: 'WorkflowConfigList',
          component: () => import('@/views/workflow/onlineForm/WorkflowConfigList.vue'),
          meta: {
            title: t('routes.workflow.workflowConfig'),
          },
        },
      ],
    },
    // 统一表单页面路由 - 核心功能
    {
      path: 'form/:formType/:dataId?',
      name: 'UniversalFormPage',
      component: () => import('@/views/workflow/universal/UniversalFormPage.vue'),
      meta: {
        title: '表单详情',
        hideMenu: true,
        hideTab: false,
        dynamicLevel: 2,
        realPath: '/workflow/form/:formType/:dataId?',
      },
    },
  ],
};

export default workflow;

export default workflow; 