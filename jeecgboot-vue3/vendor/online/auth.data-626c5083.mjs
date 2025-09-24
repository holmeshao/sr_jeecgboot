import { computed as p } from "vue";
import { useConditionFilter as h } from "/@/utils/index";
const S = [
  {
    title: "启用",
    dataIndex: "switch",
    width: 100,
    align: "center",
    slots: { customRender: "switch" }
  },
  {
    title: "字段名称",
    width: 200,
    dataIndex: "code"
  },
  {
    title: "字段描述",
    // width: 200,
    dataIndex: "title"
  },
  {
    title: "列表控制",
    dataIndex: "list",
    width: 120,
    slots: { customRender: "list" }
  },
  {
    title: "表单控制",
    dataIndex: "form",
    width: 180,
    slots: { customRender: "form" }
  }
], y = [
  {
    title: "启用",
    dataIndex: "switch",
    width: 80,
    slots: { customRender: "switch" }
  },
  {
    title: "名称",
    dataIndex: "title"
  },
  {
    title: "编码",
    dataIndex: "code"
  },
  {
    title: "权限控制",
    dataIndex: "control",
    width: 180,
    slots: { customRender: "control" }
  }
], C = [
  { code: "add", title: "新增", status: 0 },
  { code: "edit", title: "编辑", status: 0 },
  { code: "detail", title: "详情", status: 0 },
  { code: "delete", title: "删除", status: 0 },
  { code: "batch_delete", title: "批量删除", status: 0 },
  { code: "export", title: "导出", status: 0 },
  { code: "import", title: "导入", status: 0 },
  { code: "query", title: "查询", status: 0 },
  { code: "reset", title: "重置", status: 0 },
  { code: "bpm", title: "提交流程", status: 0 },
  { code: "super_query", title: "高级查询", status: 0 },
  { code: "form_confirm", title: "确定", status: 0 }
], n = "USE_SQL_RULES", v = [
  {
    title: "启用",
    dataIndex: "switch",
    width: 80,
    slots: { customRender: "switch" }
  },
  {
    title: "规则名称",
    dataIndex: "ruleName",
    width: 130
  },
  {
    title: "规则描述",
    dataIndex: "description",
    customRender({ record: { ruleOperator: o, ruleValue: e, ruleColumn: s } }) {
      return o == n ? `自定义SQL: ${e}` : `${s} ${o} ${e}`;
    }
  }
];
function w(o, e) {
  return { formSchemas: p(() => [
    {
      label: "规则名称",
      field: "ruleName",
      required: !0,
      component: "Input",
      componentProps: {
        onChange: e.onRuleNameChange
      }
    },
    {
      label: "规则字段",
      field: "ruleColumn",
      component: "JSearchSelect",
      componentProps: {
        dictOptions: o.authFields,
        getPopupContainer: () => document.body,
        onChange: e.onRuleColumnChange
      },
      dynamicRules({ model: t }) {
        return [{ required: t.ruleOperator != n, message: "请输入规则字段" }];
      },
      show: ({ model: t }) => t.ruleOperator != n
    },
    // -update-begin--author:liaozhiyang---date:20240617---for：【TV360X-201】权限管理条件根据控件过滤
    {
      label: "条件规则",
      field: "ruleOperator",
      required: !0,
      component: "JDictSelectTag",
      componentProps: {
        options: [],
        onChange: e.onRuleOperatorChange,
        getPopupContainer: () => document.body
      },
      dynamicPropskey: "options",
      dynamicPropsVal: ({ model: t, field: b }) => {
        var u;
        const c = (a) => {
          if (["BigDecimal", "double", "int"].includes(a))
            return "number";
        }, { filterCondition: m } = h();
        if (t.ruleColumn) {
          const a = (u = o.authFields.find((l) => l.value === t.ruleColumn)) != null ? u : {}, d = m({ view: a.view, fieldType: c(a.dbType) }).map((l) => {
            var i, r;
            return {
              label: (i = l.title) != null ? i : l.label,
              value: (r = l.val) != null ? r : l.value
            };
          });
          return d.push({ value: "USE_SQL_RULES", label: "自定义SQL" }), d;
        } else
          return [{ value: "USE_SQL_RULES", label: "自定义SQL" }];
      }
    },
    // {
    //   label: '条件规则',
    //   field: 'ruleOperator',
    //   required: true,
    //   component: 'JDictSelectTag',
    //   componentProps: {
    //     dictCode: 'rule_conditions',
    //     onChange: methods.onRuleOperatorChange,
    //     getPopupContainer: () => document.body,
    //   },
    // },
    // -update-end--author:liaozhiyang---date:20240617---for：【TV360X-201】权限管理条件根据控件过滤
    {
      label: "规则值",
      field: "ruleValue",
      required: !0,
      // -update-begin--author:liaozhiyang---date:20240607---for：【TV360X-536】数据权限配置配置优化及新增JInputSelect组件
      component: "JInputSelect",
      componentProps: {
        selectPlaceholder: "可选择系统变量",
        inputPlaceholder: "请输入",
        getPopupContainer: () => document.body,
        selectWidth: "200px",
        options: [
          {
            label: "登录用户账号",
            value: "#{sys_user_code}"
          },
          {
            label: "登录用户名称",
            value: "#{sys_user_name}"
          },
          {
            label: "当前日期",
            value: "#{sys_date}"
          },
          {
            label: "当前时间",
            value: "#{sys_time}"
          },
          {
            label: "登录用户部门",
            value: "#{sys_org_code}"
          },
          {
            label: "用户拥有的部门",
            value: "#{sys_multi_org_code}"
          },
          {
            label: "登录用户租户",
            value: "#{tenant_id}"
          }
        ]
      }
      // -update-end--author:liaozhiyang---date:20240607---for：【TV360X-536】数据权限配置配置优化及新增JInputSelect组件
    },
    {
      label: "状态",
      field: "status",
      required: !0,
      component: "RadioButtonGroup",
      componentProps: {
        options: [
          { label: "有效", value: 1 },
          { label: "无效", value: 0 }
        ]
      },
      defaultValue: 1
    }
  ]) };
}
export {
  n as U,
  S as a,
  y as b,
  C as c,
  v as d,
  w as u
};
