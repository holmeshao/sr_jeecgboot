import { computed as n } from "vue";
import { o as u } from "./cgform.data-0ca62d09.mjs";
function d(l) {
  return { columns: [
    {
      title: "页面按钮",
      align: "center",
      dataIndex: "buttonCode",
      customRender: ({ text: e }) => o(e, l.value)
    },
    {
      title: "事件状态",
      align: "center",
      dataIndex: "event",
      customRender: ({ text: e }) => e == "start" ? "开始" : "结束"
    },
    {
      title: "类型",
      align: "center",
      dataIndex: "cgJavaType",
      customRender: ({ text: e }) => e == "spring" ? "spring-key" : e === "class" ? "java-class" : e === "http" ? "http-api" : e
    },
    {
      title: "内容",
      align: "center",
      dataIndex: "cgJavaValue"
    },
    {
      title: "是否生效",
      align: "center",
      dataIndex: "activeStatus",
      customRender: ({ text: e }) => e == "1" ? "有效" : "无效"
    }
  ] };
}
function i(l) {
  return { formSchemas: n(() => [
    {
      label: "页面按钮",
      field: "buttonCode",
      component: "Select",
      componentProps: {
        options: [
          { label: "新增", value: "add" },
          { label: "编辑", value: "edit" },
          { label: "删除", value: "delete" },
          { label: "导入", value: "import" },
          { label: "导出", value: "export" },
          { label: "查询", value: "query" },
          ...l.value.map((e) => ({ label: e.buttonName, value: e.buttonCode }))
        ]
      },
      defaultValue: "add"
    },
    {
      label: "事件状态",
      field: "event",
      component: "RadioButtonGroup",
      componentProps: {
        options: [
          { label: "开始", value: "start" },
          { label: "结束", value: "end" }
        ]
      },
      defaultValue: "end"
    },
    {
      label: "类型",
      field: "cgJavaType",
      component: "RadioButtonGroup",
      componentProps: {
        options: [
          { label: "spring-key", value: "spring" },
          { label: "java-class", value: "class" },
          { label: "http-api", value: "http" }
        ]
      },
      defaultValue: "spring"
    },
    {
      label: "内容",
      field: "cgJavaValue",
      component: "Input",
      required: !0
    },
    {
      label: "是否生效",
      field: "activeStatus",
      component: "RadioButtonGroup",
      componentProps: {
        options: [
          { label: "有效", value: "1" },
          { label: "无效", value: "0" }
        ]
      },
      defaultValue: "1"
    }
  ]) };
}
function c(l) {
  return { columns: [
    {
      title: "页面按钮",
      align: "center",
      dataIndex: "buttonCode",
      customRender: ({ text: e }) => o(e, l.value)
    },
    {
      title: "增强SQL",
      align: "center",
      dataIndex: "cgbSql",
      ellipsis: !0
    }
  ] };
}
function p(l) {
  return { formSchemas: n(() => [
    {
      label: "页面按钮",
      field: "buttonCode",
      component: "Select",
      componentProps: {
        allowClear: !1,
        options: [
          { label: "新增", value: "add" },
          { label: "编辑", value: "edit" },
          { label: "删除", value: "delete" },
          ...l.value.map((e) => ({ label: e.buttonName, value: e.buttonCode }))
        ]
      },
      defaultValue: "add"
    },
    {
      label: "增强SQL",
      field: "cgbSql",
      component: "JCodeEditor",
      componentProps: {
        language: "sql",
        placeholder: "请输入SQL语句",
        languageChange: !1,
        lineNumbers: !1,
        fullScreen: !0,
        height: "320px"
      },
      defaultValue: ""
    },
    {
      label: "描述",
      field: "content",
      component: "InputTextArea",
      defaultValue: ""
    }
  ]) };
}
function o(l, a) {
  let e = "";
  for (let t of u)
    if (t.code === l) {
      e = t.title;
      break;
    }
  if (!e) {
    for (let t of a)
      if (t.buttonCode === l) {
        e = t.buttonName;
        break;
      }
  }
  return e || l;
}
export {
  d as a,
  p as b,
  c,
  i as u
};
