var D = Object.defineProperty, I = Object.defineProperties;
var M = Object.getOwnPropertyDescriptors;
var F = Object.getOwnPropertySymbols;
var O = Object.prototype.hasOwnProperty, P = Object.prototype.propertyIsEnumerable;
var V = (e, t, a) => t in e ? D(e, t, { enumerable: !0, configurable: !0, writable: !0, value: a }) : e[t] = a, x = (e, t) => {
  for (var a in t || (t = {}))
    O.call(t, a) && V(e, a, t[a]);
  if (F)
    for (var a of F(t))
      P.call(t, a) && V(e, a, t[a]);
  return e;
}, R = (e, t) => I(e, M(t));
import { defineComponent as S, ref as E, computed as $, resolveComponent as s, openBlock as H, createBlock as J, mergeProps as q, withCtx as c, createVNode as i, normalizeClass as N, createElementVNode as T, withDirectives as z, vShow as j } from "vue";
import { JVxeTypes as n } from "/@/components/jeecg/JVxeTable/types";
import { u as K } from "./useTableSync-075826a1.mjs";
import { useMessage as G } from "/@/hooks/web/useMessage";
import { useJPrompt as L } from "/@/components/jeecg/JPrompt";
import { useDesign as Q } from "/@/hooks/web/useDesign";
import { _ as U } from "./index-9e1e1e53.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "lodash-es";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const W = S({
  name: "CheckDictTable",
  components: {
    VNodes: (e, { attrs: t }) => t.vnodes
  },
  setup() {
    const { prefixCls: e } = Q("cgform-check-dict-table"), { createMessage: t } = G(), a = E([
      { title: "字段名称", key: "dbFieldName", width: 100 },
      { title: "字段备注", key: "dbFieldTxt", width: 100 },
      {
        title: "字段Href",
        key: "fieldHref",
        width: 130,
        type: n.textarea,
        defaultValue: ""
      },
      {
        title: "验证规则",
        key: "fieldValidType",
        width: 170,
        type: n.slot,
        slotName: "fieldValidType",
        allowInput: !0,
        defaultValue: "",
        placeholder: "空"
      },
      {
        title: "校验必填",
        key: "fieldMustInput",
        width: 80,
        type: n.checkbox,
        align: "center",
        customValue: ["1", "0"],
        defaultChecked: !1
      },
      {
        title: "字典Table",
        key: "dictTable",
        width: 280,
        type: n.textarea,
        defaultValue: ""
      },
      {
        title: "字典Code",
        key: "dictField",
        width: 280,
        type: n.input,
        defaultValue: "",
        validateRules: [{ handler: r }]
      },
      {
        title: "字典Text",
        key: "dictText",
        width: 280,
        type: n.input,
        defaultValue: "",
        validateRules: [{ handler: r }]
      }
    ]), v = K(a), { tableRef: k, tables: w } = v, p = E([
      { label: "空", value: "" },
      { label: "唯一校验", value: "only" },
      { label: "6到16位数字", value: "n6-16" },
      { label: "6到18位字母", value: "s6-18" },
      { label: "6到16位任意字符", value: "*6-16" },
      { label: "网址", value: "url" },
      { label: "电子邮件", value: "e" },
      { label: "手机号码", value: "m" },
      { label: "邮政编码", value: "p" },
      { label: "字母", value: "s" },
      { label: "数字", value: "n" },
      { label: "整数", value: "z" },
      { label: "非空", value: "*" },
      { label: "金额", value: "money" }
    ]), m = $(() => p.value.map((l) => l.value)), { createJPrompt: d } = L();
    function f(l) {
      return l != null && !m.value.includes(l);
    }
    function b(l) {
      d({
        title: "自定义正则表达式",
        placeholder: "请输入正则表达式",
        rules: [{ required: !0, message: "正则表达式不能为空！" }, { validator: o }],
        onOk: (u) => {
          l.triggerChange(u), t.success("添加成功");
        }
      });
    }
    function g(l) {
      d({
        title: "修改自定义正则表达式",
        defaultValue: l.value,
        placeholder: "请输入正则表达式",
        rules: [{ required: !0, message: "正则表达式不能为空！" }, { validator: o }],
        onOk: (u) => {
          l.triggerChange(u), u !== l.value && t.success("修改成功");
        }
      });
    }
    function o(l, u) {
      return f(u) ? Promise.resolve() : Promise.reject("当前校验已存在");
    }
    function h(l) {
      l.dbIsNull === "0" && k.value.setValues([
        {
          rowKey: l.id,
          values: { fieldMustInput: "1" }
        }
      ]);
    }
    function r({ cellValue: l, row: u }, _) {
      const { dbFieldName: A } = u, y = w.pageTable.value.tableRef.getTableData(), C = y == null ? void 0 : y.find((B) => B.dbFieldName === A);
      (C == null ? void 0 : C.fieldShowType) === "popup_dict" ? l.indexOf(",") == -1 ? _(!0) : _(!1, "popup字典组件只允许填写一个字段") : _(!0);
    }
    return R(x({}, v), {
      prefixCls: e,
      columns: a,
      isCustomRegexp: f,
      validTypeOptions: p,
      validTypeValues: m,
      onAddCustomRegexp: b,
      onChangeCustomRegexp: g,
      syncFieldMustInput: h
    });
  }
});
const X = { class: "menu" }, Y = {
  class: "custom-option-list rc-virtual-list-holder-inner",
  style: { "border-top": "1px solid #dfdfdf" }
}, Z = ["onClick"];
function ee(e, t, a, v, k, w) {
  const p = s("VNodes"), m = s("a-select"), d = s("a-col"), f = s("a-button"), b = s("a-row"), g = s("JVxeTable");
  return H(), J(g, q({
    ref: "tableRef",
    rowNumber: "",
    keyboardEdit: "",
    class: [e.prefixCls],
    maxHeight: e.tableHeight.noToolbar,
    loading: e.loading,
    columns: e.columns,
    dataSource: e.dataSource,
    disabledRows: { dbFieldName: ["id", "has_child"] }
  }, e.tableProps), {
    fieldValidType: c((o) => [
      i(b, {
        type: "flex",
        class: N(["row-valid-type", { full: !e.isCustomRegexp(o.value) }])
      }, {
        default: c(() => [
          i(d, {
            class: N(["left"])
          }, {
            default: c(() => [
              i(m, {
                value: o.value,
                options: e.validTypeOptions,
                placeholder: "空",
                style: { width: "100%" },
                onChange: o.triggerChange,
                virtual: !1
              }, {
                dropdownRender: c(({ menuNode: h }) => [
                  T("div", X, [
                    i(p, { vnodes: h }, null, 8, ["vnodes"])
                  ]),
                  z(T("div", Y, [
                    T("div", {
                      class: "ant-select-item ant-select-item-option",
                      title: "使用自定义正则表达式作为校验规则",
                      onClick: (r) => e.onAddCustomRegexp(o),
                      onMousedown: t[0] || (t[0] = (r) => r.preventDefault())
                    }, " 正则表达式 ", 40, Z)
                  ], 512), [
                    [j, !e.isCustomRegexp(o.value)]
                  ])
                ]),
                _: 2
              }, 1032, ["value", "options", "onChange"])
            ]),
            _: 2
          }, 1024),
          i(d, {
            class: "right",
            title: "修改自定义正则表达式"
          }, {
            default: c(() => [
              i(f, {
                preIcon: "ant-design:edit",
                onClick: () => e.onChangeCustomRegexp(o)
              }, null, 8, ["onClick"])
            ]),
            _: 2
          }, 1024)
        ]),
        _: 2
      }, 1032, ["class"])
    ]),
    _: 1
  }, 16, ["class", "maxHeight", "loading", "columns", "dataSource"]);
}
const be = /* @__PURE__ */ U(W, [["render", ee], ["__scopeId", "data-v-272f2c52"]]);
export {
  be as default
};
