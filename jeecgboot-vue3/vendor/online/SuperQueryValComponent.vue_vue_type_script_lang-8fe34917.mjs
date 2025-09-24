var A = Object.defineProperty, E = Object.defineProperties;
var N = Object.getOwnPropertyDescriptors;
var C = Object.getOwnPropertySymbols;
var O = Object.prototype.hasOwnProperty, S = Object.prototype.propertyIsEnumerable;
var b = (e, o, n) => o in e ? A(e, o, { enumerable: !0, configurable: !0, writable: !0, value: n }) : e[o] = n, u = (e, o) => {
  for (var n in o || (o = {}))
    O.call(o, n) && b(e, n, o[n]);
  if (C)
    for (var n of C(o))
      S.call(o, n) && b(e, n, o[n]);
  return e;
}, v = (e, o) => E(e, N(o));
import { defineComponent as g, computed as M, createVNode as P, unref as f } from "vue";
import { upperFirst as D } from "lodash-es";
import { componentMap as I } from "/@/components/Form/src/componentMap";
import { isFunction as K } from "/@/utils/is";
const G = /* @__PURE__ */ g({
  name: "SuperQueryValComponent",
  inheritAttrs: !1,
  props: {
    schema: {
      type: Object,
      default: () => ({})
    },
    formModel: {
      type: Object,
      default: () => ({})
    },
    setFormModel: {
      type: Function,
      default: null
    }
  },
  emits: ["submit"],
  setup(e, {
    emit: o
  }) {
    const n = M(() => {
      var r;
      const {
        schema: t,
        formModel: s
      } = e;
      let {
        componentProps: l = {}
      } = t;
      return K(l) && (l = (r = l({
        schema: t,
        formModel: s
      })) != null ? r : {}), l;
    }), F = M(() => {
      const {
        formModel: t,
        schema: s
      } = e;
      return {
        field: s.field,
        model: t,
        values: u({}, t),
        schema: s
      };
    });
    function y() {
      var h;
      const {
        component: t,
        changeEvent: s = "change",
        valueField: l
      } = e.schema, r = "val", p = t && ["Switch", "Checkbox"].includes(t), m = `on${D(s)}`, V = {
        [m]: (...a) => {
          const [d] = a;
          c[m] && c[m](...a);
          const i = d ? d.target : null, x = i ? p ? i.checked : i.value : d;
          e.setFormModel(r, x);
        }
      }, j = I.get(t), c = u({
        allowClear: !0,
        getPopupContainer: (a) => a == null ? void 0 : a.parentNode
      }, f(n));
      !c.disabled && t !== "RangePicker" && t && (c.placeholder = ((h = f(n)) == null ? void 0 : h.placeholder) || k(t) + e.schema.label), c.codeField = r, c.formValues = f(F);
      const w = {
        [l || (p ? "checked" : "value")]: e.formModel[r]
      }, _ = v(u(u(u({}, c), V), w), {
        allowClear: !0,
        onPressEnter() {
          o("submit");
        }
      });
      return P(j, _, null);
    }
    return () => P("div", {
      style: "width:100%"
    }, [y()]);
    function k(t) {
      return t.includes("Input") || t.includes("Complete") ? "请输入" : "请选择";
    }
  }
});
export {
  G as _
};
