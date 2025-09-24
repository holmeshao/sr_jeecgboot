var p = Object.defineProperty, m = Object.defineProperties;
var s = Object.getOwnPropertyDescriptors;
var a = Object.getOwnPropertySymbols;
var u = Object.prototype.hasOwnProperty, c = Object.prototype.propertyIsEnumerable;
var i = (e, t, o) => t in e ? p(e, t, { enumerable: !0, configurable: !0, writable: !0, value: o }) : e[t] = o, r = (e, t) => {
  for (var o in t || (t = {}))
    u.call(t, o) && i(e, o, t[o]);
  if (a)
    for (var o of a(t))
      c.call(t, o) && i(e, o, t[o]);
  return e;
}, n = (e, t) => m(e, s(t));
import { defineComponent as b, ref as f, resolveComponent as y, openBlock as h, createBlock as g, mergeProps as T } from "vue";
import { JVxeTypes as l } from "/@/components/jeecg/JVxeTable/types";
import { u as k } from "./useTableSync-075826a1.mjs";
import { _ as w } from "./index-9e1e1e53.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "lodash-es";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const F = b({
  name: "ForeignKeyTable",
  props: {
    actionButton: { type: Boolean, default: !0 }
  },
  setup() {
    const e = f([
      { title: "字段名称", key: "dbFieldName", width: 160 },
      { title: "字段备注", key: "dbFieldTxt", width: 160 },
      {
        title: "主表名",
        key: "mainTable",
        width: 280,
        type: l.input,
        defaultValue: ""
      },
      {
        title: "主表字段",
        key: "mainField",
        width: 280,
        type: l.input,
        defaultValue: ""
      }
    ]), t = k(e);
    return n(r({}, t), { columns: e });
  }
});
function _(e, t, o, B, V, S) {
  const d = y("JVxeTable");
  return h(), g(d, T({
    ref: "tableRef",
    rowNumber: "",
    keyboardEdit: "",
    maxHeight: e.tableHeight.noToolbar,
    loading: e.loading,
    columns: e.columns,
    dataSource: e.dataSource,
    disabled: !e.actionButton,
    disabledRows: { dbFieldName: ["id", "has_child"] }
  }, e.tableProps), null, 16, ["maxHeight", "loading", "columns", "dataSource", "disabled"]);
}
const z = /* @__PURE__ */ w(F, [["render", _]]);
export {
  z as default
};
