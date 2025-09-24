var W = Object.defineProperty;
var j = Object.getOwnPropertySymbols;
var z = Object.prototype.hasOwnProperty, G = Object.prototype.propertyIsEnumerable;
var N = (e, l, i) => l in e ? W(e, l, { enumerable: !0, configurable: !0, writable: !0, value: i }) : e[l] = i, A = (e, l) => {
  for (var i in l || (l = {}))
    z.call(l, i) && N(e, i, l[i]);
  if (j)
    for (var i of j(l))
      G.call(l, i) && N(e, i, l[i]);
  return e;
};
import { defineComponent as H, ref as M, watch as b, useCssVars as Q, resolveComponent as o, openBlock as u, createBlock as n, normalizeClass as R, withCtx as c, createElementVNode as w, toDisplayString as I, createElementBlock as p, Fragment as m, createVNode as V, renderList as X, createTextVNode as Z, mergeProps as K } from "vue";
import { JDictSelectTag as _, JTreeSelect as x, JCategorySelect as ee, JSelectUser as le, JSelectUserByDept as te, JSelectDept as ae, JPopup as ue, JAreaLinkage as ne, JAreaSelect as ie, JSelectMultiple as oe } from "/@/components/Form";
import re from "./JOnlineSearchSelect-04ec87d0.mjs";
import { _ as de } from "./index-9e1e1e53.mjs";
import "@vueuse/core";
import "/@/utils/http/axios";
import "/@/hooks/web/useMessage";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const O = H({
  name: "OnlineSearchFormItem",
  components: {
    JOnlineSearchSelect: re,
    JDictSelectTag: _,
    JTreeSelect: x,
    JCategorySelect: ee,
    JSelectUser: le,
    JSelectUserByDept: te,
    JSelectDept: ae,
    JPopup: ue,
    JAreaLinkage: ne,
    JAreaSelect: ie,
    JSelectMultiple: oe
  },
  props: {
    value: {
      type: String,
      default: ""
    },
    item: {
      type: Object,
      default: () => {
      },
      required: !0
    },
    dictOptions: {
      type: Object,
      default: () => {
      },
      required: !1
    },
    onlineForm: {
      type: Object,
      default: () => {
      },
      required: !1
    }
  },
  emits: ["update:value", "change"],
  setup(e, { emit: l }) {
    const i = "120px", q = {
      style: {
        "max-width": i
      }
    }, P = "single";
    let h = M(""), s = M(""), f = M("");
    b(
      () => e.value,
      () => {
        C() ? h.value = e.value ? e.value : void 0 : h.value = e.value, e.value || (s.value = "", f.value = "");
      },
      { deep: !0, immediate: !0 }
    ), b(
      h,
      (a) => {
        l("update:value", a);
      },
      { immediate: !0 }
    ), b(s, (a) => {
      l("change", e.item.field + "_begin", a), l("update:value", "1");
    }), b(f, (a) => {
      l("change", e.item.field + "_end", a), l("update:value", "1");
    });
    function J(a) {
      return a.dbField ? a.dbField : a.field;
    }
    function C() {
      let a = e.item;
      return a ? a.view == "list" || a.view == "radio" || a.view == "switch" : !1;
    }
    function D() {
      let a = e.item;
      return a.dictTable && a.dictTable.length > 0 ? a.dictTable + "," + a.dictText + "," + a.dictCode : a.dictCode;
    }
    function k() {
      let a = e.item, { dictTable: t, dictCode: v, dictText: d } = a, g = t.toLowerCase().split("where"), r = "";
      return g.length > 1 && (r = " where" + g[1]), "select " + v + " as 'value', " + d + " as 'text' from " + g[0] + r;
    }
    function T(a) {
      let { dictText: t, dictCode: v } = a;
      if (!t || t.length == 0)
        return [];
      let d = t.split(","), y = v.split(","), g = [];
      for (let r = 0; r < d.length; r++)
        g.push({
          target: d[r],
          source: y[r]
        });
      return g;
    }
    function E(a) {
      let { dictText: t } = e.item, d = t.split(",")[0];
      l("change", d, a[d]);
    }
    function B(a) {
      l("update:value", a);
    }
    function S(a, t, v) {
      let d = {
        labelKey: t,
        rowKey: v
      }, y = a.fieldExtendJson;
      if (y && typeof y == "string") {
        let g = JSON.parse(y), r = A({}, g);
        r.text && (d.labelKey = r.text), r.store && (d.rowKey = r.store);
      }
      return d;
    }
    let U = S(e.item, "realname", "username"), Y = S(e.item, "departName", "id");
    function F(a) {
      a && a.length > 0 ? l("update:value", a.join(",")) : l("update:value", "");
    }
    return {
      getPopupFieldConfig: T,
      userSelectProp: U,
      depSelectProp: Y,
      handleSelectChange: F,
      setFieldsValue: E,
      innerValue: h,
      beginValue: s,
      endValue: f,
      isEasySelect: C,
      getDictOptionKey: J,
      getDictCode: D,
      labelTextMaxWidth: i,
      labelCol: q,
      single_mode: P,
      getSqlByDictCode: k,
      handleCategoryTreeChange: B
    };
  }
}), $ = () => {
  Q((e) => ({
    "6524b8a9": e.labelTextMaxWidth
  }));
}, L = O.setup;
O.setup = L ? (e, l) => ($(), L(e, l)) : $;
const se = O;
const pe = ["title"];
function me(e, l, i, q, P, h) {
  const s = o("a-date-picker"), f = o("JDictSelectTag"), J = o("a-select-option"), C = o("a-select"), D = o("JTreeSelect"), k = o("JCategorySelect"), T = o("JOnlineSearchSelect"), E = o("JSelectUser"), B = o("JSelectDept"), S = o("JPopup"), U = o("JAreaSelect"), Y = o("JSelectMultiple"), F = o("a-input"), a = o("a-form-item");
  return u(), n(a, {
    labelCol: e.labelCol,
    class: R("jeecg-online-search")
  }, {
    label: c(() => [
      w("span", {
        title: e.item.label,
        class: "label-text"
      }, I(e.item.label), 9, pe)
    ]),
    default: c(() => [
      e.item.view == "date" ? (u(), p(m, { key: 0 }, [
        e.single_mode === e.item.mode ? (u(), n(s, {
          key: 0,
          style: { width: "100%" },
          showTime: !1,
          valueFormat: "YYYY-MM-DD",
          placeholder: "请选择" + e.item.label,
          value: e.innerValue,
          "onUpdate:value": l[0] || (l[0] = (t) => e.innerValue = t)
        }, null, 8, ["placeholder", "value"])) : (u(), p(m, { key: 1 }, [
          V(s, {
            showTime: !1,
            valueFormat: "YYYY-MM-DD",
            placeholder: "开始日期",
            value: e.beginValue,
            "onUpdate:value": l[1] || (l[1] = (t) => e.beginValue = t),
            style: { width: "calc(50% - 15px)" }
          }, null, 8, ["value"]),
          l[20] || (l[20] = w("span", { class: "group-query-strig" }, "~", -1)),
          V(s, {
            showTime: !1,
            valueFormat: "YYYY-MM-DD",
            placeholder: "结束日期",
            value: e.endValue,
            "onUpdate:value": l[2] || (l[2] = (t) => e.endValue = t),
            style: { width: "calc(50% - 15px)" }
          }, null, 8, ["value"])
        ], 64))
      ], 64)) : e.item.view == "datetime" ? (u(), p(m, { key: 1 }, [
        e.single_mode === e.item.mode ? (u(), n(s, {
          key: 0,
          style: { width: "100%" },
          showTime: !0,
          valueFormat: "YYYY-MM-DD hh:mm:ss",
          placeholder: "请选择" + e.item.label,
          value: e.innerValue,
          "onUpdate:value": l[3] || (l[3] = (t) => e.innerValue = t)
        }, null, 8, ["placeholder", "value"])) : (u(), p(m, { key: 1 }, [
          V(s, {
            showTime: !0,
            valueFormat: "YYYY-MM-DD hh:mm:ss",
            placeholder: "开始时间",
            value: e.beginValue,
            "onUpdate:value": l[4] || (l[4] = (t) => e.beginValue = t),
            style: { width: "calc(50% - 15px)" }
          }, null, 8, ["value"]),
          l[21] || (l[21] = w("span", { class: "group-query-strig" }, "~", -1)),
          V(s, {
            showTime: !0,
            valueFormat: "YYYY-MM-DD hh:mm:ss",
            placeholder: "结束时间",
            value: e.endValue,
            "onUpdate:value": l[5] || (l[5] = (t) => e.endValue = t),
            style: { width: "calc(50% - 15px)" }
          }, null, 8, ["value"])
        ], 64))
      ], 64)) : e.isEasySelect() ? (u(), p(m, { key: 2 }, [
        e.item.config === "1" ? (u(), n(f, {
          key: 0,
          placeholder: "请选择" + e.item.label,
          value: e.innerValue,
          "onUpdate:value": l[6] || (l[6] = (t) => e.innerValue = t),
          dictCode: e.getDictCode()
        }, null, 8, ["placeholder", "value", "dictCode"])) : (u(), n(C, {
          key: 1,
          placeholder: "请选择" + e.item.label,
          value: e.innerValue,
          "onUpdate:value": l[7] || (l[7] = (t) => e.innerValue = t)
        }, {
          default: c(() => [
            (u(!0), p(m, null, X(e.dictOptions[e.getDictOptionKey(e.item)], (t, v) => (u(), n(J, {
              key: v,
              value: t.value
            }, {
              default: c(() => [
                Z(I(t.text), 1)
              ]),
              _: 2
            }, 1032, ["value"]))), 128))
          ]),
          _: 1
        }, 8, ["placeholder", "value"]))
      ], 64)) : e.item.view === "sel_tree" ? (u(), n(D, {
        key: 3,
        placeholder: "请选择" + e.item.label,
        value: e.innerValue,
        "onUpdate:value": l[8] || (l[8] = (t) => e.innerValue = t),
        dict: e.item.dict,
        pidField: e.item.pidField,
        pidValue: e.item.pidValue,
        hasChildField: e.item.hasChildField,
        "load-triggle-change": ""
      }, null, 8, ["placeholder", "value", "dict", "pidField", "pidValue", "hasChildField"])) : e.item.view === "cat_tree" ? (u(), n(k, {
        key: 4,
        onChange: e.handleCategoryTreeChange,
        loadTriggleChange: !0,
        pcode: e.item.pcode,
        value: e.innerValue,
        "onUpdate:value": l[9] || (l[9] = (t) => e.innerValue = t),
        placeholder: "请选择" + e.item.label
      }, null, 8, ["onChange", "pcode", "value", "placeholder"])) : e.item.view === "sel_search" ? (u(), p(m, { key: 5 }, [
        e.item.config === "1" ? (u(), n(f, {
          key: 0,
          value: e.innerValue,
          "onUpdate:value": l[10] || (l[10] = (t) => e.innerValue = t),
          placeholder: "请选择" + e.item.label,
          dict: e.getDictCode()
        }, null, 8, ["value", "placeholder", "dict"])) : (u(), n(T, {
          key: 1,
          value: e.innerValue,
          "onUpdate:value": l[11] || (l[11] = (t) => e.innerValue = t),
          placeholder: "请选择" + e.item.label,
          sql: e.getSqlByDictCode()
        }, null, 8, ["value", "placeholder", "sql"]))
      ], 64)) : e.item.view == "sel_user" ? (u(), n(E, K({ key: 6 }, e.userSelectProp, {
        value: e.innerValue,
        "onUpdate:value": l[12] || (l[12] = (t) => e.innerValue = t),
        placeholder: "请选择" + e.item.label
      }), null, 16, ["value", "placeholder"])) : e.item.view == "sel_depart" ? (u(), n(B, K({
        key: 7,
        showButton: !1
      }, e.depSelectProp, {
        value: e.innerValue,
        "onUpdate:value": l[13] || (l[13] = (t) => e.innerValue = t),
        placeholder: "请选择" + e.item.label
      }), null, 16, ["value", "placeholder"])) : e.item.view == "popup" ? (u(), n(S, {
        key: 8,
        placeholder: "请选择" + e.item.label,
        value: e.innerValue,
        "onUpdate:value": l[14] || (l[14] = (t) => e.innerValue = t),
        code: e.item.dictTable,
        setFieldsValue: e.setFieldsValue,
        "field-config": e.getPopupFieldConfig(e.item),
        multi: !0
      }, null, 8, ["placeholder", "value", "code", "setFieldsValue", "field-config"])) : e.item.view == "pca" ? (u(), n(U, {
        key: 9,
        placeholder: "请选择" + e.item.label,
        value: e.innerValue,
        "onUpdate:value": l[15] || (l[15] = (t) => e.innerValue = t)
      }, null, 8, ["placeholder", "value"])) : e.item.view == "checkbox" || e.item.view == "list_multi" ? (u(), n(Y, {
        key: 10,
        dictCode: e.getDictCode(),
        placeholder: "请选择" + e.item.label,
        value: e.innerValue,
        "onUpdate:value": l[16] || (l[16] = (t) => e.innerValue = t)
      }, null, 8, ["dictCode", "placeholder", "value"])) : (u(), p(m, { key: 11 }, [
        e.single_mode === e.item.mode ? (u(), n(F, {
          key: 0,
          placeholder: "请选择" + e.item.label,
          value: e.innerValue,
          "onUpdate:value": l[17] || (l[17] = (t) => e.innerValue = t)
        }, null, 8, ["placeholder", "value"])) : (u(), p(m, { key: 1 }, [
          V(F, {
            placeholder: "开始值",
            value: e.beginValue,
            "onUpdate:value": l[18] || (l[18] = (t) => e.beginValue = t),
            style: { width: "calc(50% - 15px)" }
          }, null, 8, ["value"]),
          l[22] || (l[22] = w("span", { class: "group-query-strig" }, "~", -1)),
          V(F, {
            placeholder: "结束值",
            value: e.endValue,
            "onUpdate:value": l[19] || (l[19] = (t) => e.endValue = t),
            style: { width: "calc(50% - 15px)" }
          }, null, 8, ["value"])
        ], 64))
      ], 64))
    ]),
    _: 1
  }, 8, ["labelCol"]);
}
const we = /* @__PURE__ */ de(se, [["render", me], ["__scopeId", "data-v-e62a9629"]]);
export {
  we as default
};
