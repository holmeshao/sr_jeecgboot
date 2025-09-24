var T = Object.defineProperty, _ = Object.defineProperties;
var C = Object.getOwnPropertyDescriptors;
var c = Object.getOwnPropertySymbols;
var k = Object.prototype.hasOwnProperty, q = Object.prototype.propertyIsEnumerable;
var f = (e, t, l) => t in e ? T(e, t, { enumerable: !0, configurable: !0, writable: !0, value: l }) : e[t] = l, b = (e, t) => {
  for (var l in t || (t = {}))
    k.call(t, l) && f(e, l, t[l]);
  if (c)
    for (var l of c(t))
      q.call(t, l) && f(e, l, t[l]);
  return e;
}, h = (e, t) => _(e, C(t));
import { defineComponent as V, ref as F, resolveComponent as x, openBlock as w, createBlock as H, mergeProps as S } from "vue";
import { JVxeTypes as r } from "/@/components/jeecg/JVxeTable/types";
import { u as P } from "./useTableSync-075826a1.mjs";
import { _ as $ } from "./index-9e1e1e53.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "lodash-es";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const I = V({
  name: "QueryTable",
  emits: ["query"],
  setup(e, { emit: t }) {
    const l = F([
      { title: "字段名称", key: "dbFieldName", width: 130 },
      { title: "字段备注", key: "dbFieldTxt", width: 130 },
      {
        title: "控件类型",
        key: "queryShowType",
        width: 170,
        type: r.select,
        options: [
          { title: "文本框", value: "text" },
          { title: "日期(yyyy-MM-dd)", value: "date" },
          { title: "日期（yyyy-MM-dd HH:mm:ss）", value: "datetime" },
          { title: "时间（HH:mm:ss）", value: "time" },
          // update-begin--author:liaozhiyang---date:20240531---for：【TV360X-415】个性化查询支持年、月、周、季度
          // 虚拟的date_year、date_month、date_week、date_quarter其实走的还是date组件，为了查询
          { title: "日期-年", value: "date_year" },
          { title: "日期-月", value: "date_month" },
          { title: "日期-周", value: "date_week" },
          { title: "日期-季度", value: "date_quarter" },
          // update-end--author:liaozhiyang---date:20240531---for：【TV360X-415】个性化查询支持年、月、周、季度
          { title: "下拉框", value: "list" },
          { title: "下拉多选框", value: "list_multi" },
          { title: "下拉搜索框", value: "sel_search" },
          { title: "分类字典树", value: "cat_tree" },
          { title: "Popup弹框", value: "popup" },
          { title: "部门选择", value: "sel_depart" },
          { title: "用户选择", value: "sel_user" },
          { title: "省市区组件", value: "pca" },
          { title: "自定义树控件", value: "sel_tree" },
          // update-begin--author:liaozhiyang---date:20240521---for：【TV360X-8】个性化配置控件类型增加开关组件
          { title: "开关", value: "switch" },
          // update-end--author:liaozhiyang---date:20240521---for：【TV360X-8】个性化配置控件类型增加开关组件
          // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-415】个性化配置控件类型增加popup字典组件
          { title: "Popup字典", value: "popup_dict" }
          // update-end--author:liaozhiyang---date:20240529---for：【TV360X-415】个性化配置控件类型增加popup字典组件
        ],
        defaultValue: "text",
        placeholder: "请选择${title}",
        validateRules: [{ handler: n }]
      },
      {
        title: "字典Table",
        key: "queryDictTable",
        width: 130,
        type: r.textarea,
        defaultValue: ""
      },
      {
        title: "字典Code",
        key: "queryDictField",
        width: 130,
        type: r.input,
        defaultValue: ""
      },
      {
        title: "字典Text",
        key: "queryDictText",
        width: 130,
        type: r.input,
        defaultValue: ""
      },
      {
        title: "默认值",
        key: "queryDefVal",
        width: 130,
        type: r.input,
        defaultValue: ""
      },
      {
        title: "是否启用",
        key: "queryConfigFlag",
        minWidth: 80,
        type: r.checkbox,
        customValue: ["1", "0"],
        defaultChecked: !1,
        // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-816】如果是关联记录或者没勾选数据库字段个性化查询checkbox禁用
        props: {
          isDisabledCell({ row: a, column: o }) {
            let { pageTable: i, dbTable: v, fkTable: g } = m;
            const s = i.value.tableRef.getTableData({ rowIds: [a.id] })[0];
            if (["link_table"].includes(s == null ? void 0 : s.fieldShowType))
              return a.queryConfigFlag = "0", !0;
            const p = v.value.tableRef.getTableData({ rowIds: [a.id] })[0];
            if ((p == null ? void 0 : p.dbIsPersist) == "0")
              return a.queryConfigFlag = "0", !0;
            const u = g.value.tableRef.getTableData({ rowIds: [a.id] })[0];
            return !!(u != null && u.mainTable && (u != null && u.mainField));
          }
        }
        // update-end--author:liaozhiyang---date:20240603---for：【TV360X-816】如果是关联记录或者没勾选数据库字段个性化查询checkbox禁用
      }
    ]), d = P(l), { tables: m } = d;
    function y({ row: a, column: o, value: i }) {
      o.key === "queryConfigFlag" && i === "1" && t("query", a.id);
    }
    function n({ cellValue: a, row: o }, i) {
      a == null && o.queryConfigFlag == "1" && i(!1, "查询启用状态下，控件类型必选~"), i(!0);
    }
    return h(b({}, d), { columns: l, handleChange: y });
  }
});
function M(e, t, l, d, m, y) {
  const n = x("JVxeTable");
  return w(), H(n, S({
    ref: "tableRef",
    rowNumber: "",
    keyboardEdit: "",
    maxHeight: e.tableHeight.noToolbar,
    loading: e.loading,
    columns: e.columns,
    dataSource: e.dataSource,
    disabledRows: { dbFieldName: ["id", "has_child"] },
    onValueChange: e.handleChange
  }, e.tableProps), null, 16, ["maxHeight", "loading", "columns", "dataSource", "onValueChange"]);
}
const L = /* @__PURE__ */ $(I, [["render", M]]);
export {
  L as default
};
