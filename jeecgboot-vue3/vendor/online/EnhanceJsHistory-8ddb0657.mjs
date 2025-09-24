var x = Object.defineProperty;
var b = Object.getOwnPropertySymbols;
var N = Object.prototype.hasOwnProperty, $ = Object.prototype.propertyIsEnumerable;
var S = (e, n, t) => n in e ? x(e, n, { enumerable: !0, configurable: !0, writable: !0, value: t }) : e[n] = t, v = (e, n) => {
  for (var t in n || (n = {}))
    N.call(n, t) && S(e, t, n[t]);
  if (b)
    for (var t of b(n))
      $.call(n, t) && S(e, t, n[t]);
  return e;
};
var M = (e, n, t) => new Promise((u, f) => {
  var m = (s) => {
    try {
      r(t.next(s));
    } catch (d) {
      f(d);
    }
  }, p = (s) => {
    try {
      r(t.throw(s));
    } catch (d) {
      f(d);
    }
  }, r = (s) => s.done ? u(s.value) : Promise.resolve(s.value).then(m, p);
  r((t = t.apply(e, n)).next());
});
import { formatToDate as j } from "/@/utils/dateUtil";
import { defineComponent as I, ref as h, nextTick as R, resolveComponent as i, openBlock as w, createBlock as T, withCtx as a, createVNode as l, createTextVNode as k, normalizeClass as D, createElementVNode as F, toDisplayString as V } from "vue";
import { BasicModal as A, useModalInner as O } from "/@/components/Modal";
import { JCodeEditor as z } from "/@/components/Form";
import "/@/store";
import { defineStore as P } from "pinia";
import { createLocalStorage as q } from "/@/utils/cache";
import { _ as G } from "./index-9e1e1e53.mjs";
const B = q(), H = "enhance_", K = P({
  id: "online-cgform-enhance",
  state: () => ({
    enhanceJs: {}
  }),
  getters: {},
  actions: {
    getEnhanceJs(e) {
      return this.enhanceJs[e] = B.get(H + e), this.enhanceJs[e];
    },
    addEnhanceJs(e) {
      this.enhanceJs[e.code] ? this.enhanceJs[e.code].push(v({}, e)) : this.enhanceJs[e.code] = [v({}, e)];
      let n = this.enhanceJs[e.code];
      for (; n.length > 16; )
        n.shift();
      B.set(H + e.code, n);
    }
  }
}), Q = I({
  name: "EnhanceJsHistory",
  components: { BasicModal: A, JCodeEditor: z },
  setup() {
    const e = K(), n = h(), t = h(!1), u = h([]), f = h(""), m = h(0), [p, { closeModal: r }] = O((o) => M(this, null, function* () {
      s(o.code, o.type);
    }));
    function s(o, E) {
      f.value = "", u.value = [];
      let J = e.getEnhanceJs(o), c = [], _ = 0;
      for (let g of J)
        g.type === E && (_++, c.push(Object.assign({}, g, { index: _ })));
      c && c.length > 0 && c.sort((g, L) => L.date - g.date), u.value = [...c], R(() => y(c[0]));
    }
    function d() {
      r();
    }
    function C(o) {
      return j(o, "yyyy-MM-DD HH:mm:ss");
    }
    function y(o) {
      m.value = o.index, n.value.setValue(o.str);
    }
    return {
      codeEditorRef: n,
      fullCode: y,
      registerModal: p,
      getFormatDate: C,
      onCancel: d,
      confirmLoading: t,
      dataList: u,
      jsStr: f,
      activeIndex: m
    };
  }
});
const U = ["onClick"];
function W(e, n, t, u, f, m) {
  const p = i("a-divider"), r = i("a-list-item"), s = i("a-list"), d = i("a-layout-sider"), C = i("JCodeEditor"), y = i("a-layout-content"), o = i("a-layout"), E = i("a-spin"), J = i("a-button"), c = i("BasicModal");
  return w(), T(c, {
    onRegister: e.registerModal,
    title: "JS增强历史记录",
    width: 1200,
    maskClosable: !1,
    confirmLoading: e.confirmLoading,
    defaultFullscreen: "",
    onCancel: e.onCancel
  }, {
    footer: a(() => [
      l(J, { onClick: e.onCancel }, {
        default: a(() => n[1] || (n[1] = [
          k("关闭")
        ])),
        _: 1
      }, 8, ["onClick"])
    ]),
    default: a(() => [
      l(E, { spinning: e.confirmLoading }, {
        default: a(() => [
          l(o, null, {
            default: a(() => [
              l(d, { theme: "light" }, {
                default: a(() => [
                  l(s, {
                    bordered: "",
                    dataSource: e.dataList,
                    class: D("enhance-list")
                  }, {
                    header: a(() => [
                      F("div", null, [
                        l(p, { style: { margin: "0" } }, {
                          default: a(() => n[0] || (n[0] = [
                            k("保存时间")
                          ])),
                          _: 1
                        })
                      ])
                    ]),
                    renderItem: a(({ item: _ }) => [
                      l(r, {
                        class: D(e.activeIndex === _.index ? "bg-blue" : "")
                      }, {
                        default: a(() => [
                          F("a", {
                            onClick: (g) => e.fullCode(_)
                          }, V(e.getFormatDate(_.date)), 9, U)
                        ]),
                        _: 2
                      }, 1032, ["class"])
                    ]),
                    _: 1
                  }, 8, ["dataSource"])
                ]),
                _: 1
              }),
              l(o, null, {
                default: a(() => [
                  l(y, { style: { margin: "8px 8px", padding: "8px", background: "#fff", minHeight: "280px" } }, {
                    default: a(() => [
                      l(C, {
                        ref: "codeEditorRef",
                        language: "javascript",
                        fullScreen: !0,
                        lineNumbers: !1,
                        "language-change": !1
                      }, null, 512)
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              })
            ]),
            _: 1
          })
        ]),
        _: 1
      }, 8, ["spinning"])
    ]),
    _: 1
  }, 8, ["onRegister", "confirmLoading", "onCancel"]);
}
const X = /* @__PURE__ */ G(Q, [["render", W], ["__scopeId", "data-v-a2408ade"]]), le = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: X
}, Symbol.toStringTag, { value: "Module" }));
export {
  X as E,
  le as a,
  K as u
};
