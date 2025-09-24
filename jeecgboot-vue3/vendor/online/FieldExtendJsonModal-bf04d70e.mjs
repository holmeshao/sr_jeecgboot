var P = Object.defineProperty, G = Object.defineProperties;
var I = Object.getOwnPropertyDescriptors;
var F = Object.getOwnPropertySymbols;
var T = Object.prototype.hasOwnProperty, V = Object.prototype.propertyIsEnumerable;
var L = (t, a, n) => a in t ? P(t, a, { enumerable: !0, configurable: !0, writable: !0, value: n }) : t[a] = n, _ = (t, a) => {
  for (var n in a || (a = {}))
    T.call(a, n) && L(t, n, a[n]);
  if (F)
    for (var n of F(a))
      V.call(a, n) && L(t, n, a[n]);
  return t;
}, N = (t, a) => G(t, I(a));
var R = (t, a, n) => new Promise((f, i) => {
  var l = (r) => {
    try {
      p(n.next(r));
    } catch (c) {
      i(c);
    }
  }, d = (r) => {
    try {
      p(n.throw(r));
    } catch (c) {
      i(c);
    }
  }, p = (r) => r.done ? f(r.value) : Promise.resolve(r.value).then(l, d);
  p((n = n.apply(t, a)).next());
});
import { defineComponent as C, ref as m, reactive as j, toRaw as D, toRefs as K, resolveComponent as w, openBlock as $, createBlock as A, mergeProps as J, withCtx as y, createVNode as O } from "vue";
import { BasicModal as U, useModalInner as Y } from "/@/components/Modal";
import q from "./SetSwitchOptions-f914bc17.mjs";
import { BasicForm as H, useForm as W } from "/@/components/Form/index";
import { pick as z } from "lodash-es";
import { L as x } from "./constant-fa63bd66.mjs";
import { isArray as Q } from "/@/utils/is";
import { _ as X } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const Z = C({
  name: "FieldExtendJsonModal",
  components: {
    BasicModal: U,
    BasicForm: H,
    SetSwitchOptions: q
  },
  emits: ["success", "register"],
  setup(t, { emit: a }) {
    const n = m(!1);
    function f() {
      i.uploadnum = 0, i.showLength = "", i.popupMulti = !0, i.multiSelect = !0, i.store = "", i.text = "", i.orderRule = "", i.validateError = "", i.labelLength = x;
    }
    const i = j({
      uploadnum: 0,
      showLength: "",
      popupMulti: !0,
      store: "",
      text: "",
      multiSelect: !0,
      orderRule: "",
      validateError: "",
      labelLength: x
    }), l = m(""), d = m(""), p = m("0"), r = m(""), c = [
      {
        label: "rowKey",
        field: "rowKey",
        component: "Input",
        show: !1
      },
      {
        label: "文件上传数量",
        field: "uploadnum",
        component: "InputNumber",
        componentProps: {
          style: {
            width: "100%"
          }
        },
        ifShow: () => l.value === "file" || l.value === "image"
      },
      {
        label: "是否多选",
        field: "popupMulti",
        component: "RadioGroup",
        defaultValue: !0,
        componentProps: {
          options: [
            { label: "否", value: !1 },
            { label: "是", value: !0 }
          ]
        },
        ifShow: () => l.value === "popup" || l.value === "popup_dict"
      },
      {
        label: "是否多选",
        field: "multiSelect",
        component: "RadioGroup",
        defaultValue: !0,
        componentProps: {
          options: [
            { label: "否", value: !1 },
            { label: "是", value: !0 }
          ]
        },
        ifShow: () => l.value === "sel_user" || l.value === "sel_depart"
      },
      {
        label: "存储字段",
        field: "store",
        component: "Input",
        ifShow: () => l.value === "sel_user" || l.value === "sel_depart"
      },
      {
        label: "展示字段",
        field: "text",
        component: "Input",
        ifShow: () => l.value === "sel_user" || l.value === "sel_depart"
      },
      {
        label: "默认排序",
        field: "orderRule",
        component: "RadioGroup",
        defaultValue: "",
        componentProps: {
          options: [
            { label: "降序", value: "desc" },
            { label: "升序", value: "asc" },
            { label: "不默认排序", value: "" }
          ]
        },
        ifShow: () => p.value === "1" || ["Datetime", "string", "Date", "int", "BigDecimal", "double"].includes(r.value)
      },
      {
        label: "校验提示",
        field: "validateError",
        component: "Input",
        componentProps: {
          placeholder: "请输入校验提示文本"
        }
      },
      {
        label: "查询label长度",
        field: "labelLength",
        component: "InputNumber",
        componentProps: {
          placeholder: "请输入label长度"
        }
      },
      {
        label: "是否固定",
        field: "isFixed",
        component: "RadioGroup",
        defaultValue: 0,
        componentProps: {
          options: [
            { label: "是", value: 1 },
            { label: "否", value: 0 }
          ]
        }
      },
      // update-begin--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
      {
        label: "是否独占一行",
        field: "isOneRow",
        component: "RadioGroup",
        defaultValue: !1,
        componentProps: {
          options: [
            { label: "否", value: !1 },
            { label: "是", value: !0 }
          ]
        },
        ifShow: () => l.value === "markdown" || l.value === "umeditor"
      },
      // update-end--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
      // update-begin--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
      {
        label: "日期格式",
        field: "picker",
        component: "RadioGroup",
        defaultValue: "default",
        componentProps: {
          options: [
            { label: "年", value: "year" },
            { label: "年月", value: "month" },
            { label: "年周", value: "week" },
            { label: "年季度", value: "quarter" },
            { label: "年月日", value: "default" }
          ]
        },
        ifShow: () => l.value === "date"
      },
      // update-end--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
      // update-begin--author:liaozhiyang---date:20240308---for：【TV360X-25】扩展参数配置中增加开关是否选项配置
      {
        label: "开关值",
        field: "switchOptions",
        component: "Input",
        slot: "switchOptions",
        ifShow: () => l.value === "switch"
      }
      // update-end--author:liaozhiyang---date:20240308---for：【TV360X-25】扩展参数配置中增加开关是否选项配置
    ], [h, { validate: b, setFieldsValue: v, resetFields: B }] = W({
      schemas: c,
      showActionButtonGroup: !1,
      labelAlign: "right",
      labelWidth: 100
    }), [M, { closeModal: k }] = Y((e) => R(this, null, function* () {
      if (f(), e.jsonStr) {
        let o = JSON.parse(e.jsonStr);
        e.fieldShowType === "switch" && Q(o) && o.length == 2 && (i.switchOptions = o), Object.keys(o).map((u) => {
          i[u] = o[u];
        });
      } else
        e.fieldShowType === "switch" && (i.switchOptions = ["Y", "N"]);
      l.value = e.fieldShowType, d.value = e.id, p.value = e.sortFlag, r.value = e.dbType;
      let s = D(i);
      yield B(), yield v(N(_({}, s), {
        rowKey: e.id
      }));
    }));
    function E() {
      return R(this, null, function* () {
        let e = yield b(), s = l.value, o = {};
        if (s === "file" || s === "image" ? e.uploadnum && e.uploadnum > 0 && (o.uploadnum = e.uploadnum) : s === "textarea" || s === "text" ? e.showLength && e.showLength > 0 && (o.showLength = e.showLength) : s === "sel_user" || s === "sel_depart" ? o = z(e, "store", "text", "multiSelect") : (s === "popup" || s === "popup_dict") && (o.popupMulti = e.popupMulti), e.orderRule && (o.orderRule = e.orderRule), e.validateError && (o.validateError = e.validateError), e.labelLength && (o.labelLength = e.labelLength), e.isFixed && (o.isFixed = e.isFixed), e.isOneRow && (o.isOneRow = e.isOneRow), e.picker && (o.picker = e.picker), e.switchOptions) {
          const u = e.switchOptions.split(",");
          let g = Number(u[0]), S = Number(u[1]);
          (Number.isNaN(g) || Number.isNaN(S)) && (g = u[0], S = u[1]), o.switchOptions = [g, S];
        }
        for (let u in o)
          o[u] === "" && delete o[u];
        a("success", o, e.rowKey), k();
      });
    }
    return _({
      spinningLoading: n,
      registerModal: M,
      registerForm: h,
      fieldShowType: l,
      rowKey: d,
      handleSubmit: E
    }, K(i));
  }
});
function ee(t, a, n, f, i, l) {
  const d = w("SetSwitchOptions"), p = w("BasicForm"), r = w("a-spin"), c = w("BasicModal");
  return $(), A(c, J({ wrapClassName: "field-extend-config-modal" }, t.$attrs, {
    title: "字段扩展配置项",
    onRegister: t.registerModal,
    keyboard: "",
    canFullscreen: !1,
    cancelText: "关闭",
    onOk: t.handleSubmit
  }), {
    default: y(() => [
      O(r, { spinning: t.spinningLoading }, {
        default: y(() => [
          O(p, { onRegister: t.registerForm }, {
            switchOptions: y(({ model: h, field: b }) => [
              O(d, {
                value: h[b],
                "onUpdate:value": (v) => h[b] = v
              }, null, 8, ["value", "onUpdate:value"])
            ]),
            _: 1
          }, 8, ["onRegister"])
        ]),
        _: 1
      }, 8, ["spinning"])
    ]),
    _: 1
  }, 16, ["onRegister", "onOk"]);
}
const me = /* @__PURE__ */ X(Z, [["render", ee]]);
export {
  me as default
};
