var S = (t, a, o) => new Promise((d, l) => {
  var g = (i) => {
    try {
      r(o.next(i));
    } catch (s) {
      l(s);
    }
  }, f = (i) => {
    try {
      r(o.throw(i));
    } catch (s) {
      l(s);
    }
  }, r = (i) => i.done ? d(i.value) : Promise.resolve(i.value).then(g, f);
  r((o = o.apply(t, a)).next());
});
import { defineComponent as w, ref as p, watch as y, resolveComponent as v, openBlock as _, createBlock as C, withCtx as x, createElementBlock as T, Fragment as k, renderList as B, createElementVNode as E, toDisplayString as N } from "vue";
import { defHttp as F } from "/@/utils/http/axios";
import { useMessage as V } from "/@/hooks/web/useMessage";
import { _ as L } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const O = "/online/cgform/api/querySelectOptions", P = w({
  name: "OnlineSelectCascade",
  props: {
    table: { type: String, default: "" },
    txt: { type: String, default: "" },
    store: { type: String, default: "" },
    idField: { type: String, default: "" },
    pidField: { type: String, default: "" },
    pidValue: { type: String, default: "-1" },
    origin: { type: Boolean, default: !1 },
    condition: { type: String, default: "" },
    value: { type: String, default: "" },
    isNumber: { type: Boolean, default: !1 },
    placeholder: { type: String, default: "请选择" }
  },
  emits: ["change", "next"],
  setup(t, { emit: a }) {
    const { createMessage: o } = V(), d = p(""), l = p([]), g = p(!0);
    function f(e) {
      let n = e || "";
      a("change", n), m(n);
    }
    y(
      () => t.condition,
      (e) => {
        g.value = !0, e && i();
      },
      { immediate: !0 }
    ), y(
      () => t.pidValue,
      (e) => {
        e === "-1" ? l.value = [] : i();
      }
    ), y(
      () => t.value,
      (e, n) => {
        e ? d.value = e : (d.value = [], n && (a("change", ""), a("next", "-1"))), e && !n && r(e);
      },
      { immediate: !0 }
    );
    function r(e) {
      return S(this, null, function* () {
        if (t.idField === t.store)
          a("next", e);
        else if (t.origin === !0)
          yield b(), m(e);
        else {
          let n = yield $();
          m(e, n);
        }
      });
    }
    function i() {
      let e = s();
      t.origin === !0 ? e.condition = t.condition : e.pidValue = t.pidValue, l.value = [], F.get({ url: O, params: e }, { isTransformResponse: !1 }).then((n) => {
        n.success ? l.value = [...n.result] : o.warning("联动组件数据加载失败,请检查配置!");
      });
    }
    function s() {
      return {
        table: t.table,
        txt: t.txt,
        key: t.store,
        idField: t.idField,
        pidField: t.pidField
      };
    }
    function $() {
      return new Promise((e) => {
        if (!t.value)
          d.value = [], e([]);
        else {
          let n = s();
          t.isNumber === !0 ? n.condition = `${t.store} = ${t.value}` : n.condition = `${t.store} = '${t.value}'`, F.get({ url: O, params: n }, { isTransformResponse: !1 }).then((u) => {
            u.success ? e(u.result) : (o.warning("联动组件数据加载失败,请检查配置!"), e([]));
          });
        }
      });
    }
    function b() {
      return new Promise((e) => {
        (function u(c) {
          c > 10 && e([]);
          let h = l.value;
          h && h.length > 0 ? e(h) : setTimeout(() => {
            u(c++);
          }, 300);
        })(0);
      });
    }
    function m(e, n = []) {
      if (e && e.length > 0) {
        (!n || n.length == 0) && (n = l.value);
        let u = n.filter((c) => c.store === e);
        if (u && u.length > 0) {
          let c = u[0].id;
          a("next", c);
        }
      }
    }
    return {
      selectedValue: d,
      dictOptions: l,
      handleChange: f
    };
  }
}), R = ["title"];
function M(t, a, o, d, l, g) {
  const f = v("a-select-option"), r = v("a-select");
  return _(), C(r, {
    placeholder: t.placeholder,
    value: t.selectedValue,
    onChange: t.handleChange,
    allowClear: "",
    style: { width: "100%" }
  }, {
    default: x(() => [
      (_(!0), T(k, null, B(t.dictOptions, (i, s) => (_(), C(f, {
        key: s,
        value: i.store
      }, {
        default: x(() => [
          E("span", {
            style: { display: "inline-block", width: "100%" },
            title: i.label
          }, N(i.label), 9, R)
        ]),
        _: 2
      }, 1032, ["value"]))), 128))
    ]),
    _: 1
  }, 8, ["placeholder", "value", "onChange"]);
}
const z = /* @__PURE__ */ L(P, [["render", M]]);
export {
  z as default
};
