var u = (e, o, t) => new Promise((i, r) => {
  var m = (n) => {
    try {
      s(t.next(n));
    } catch (c) {
      r(c);
    }
  }, g = (n) => {
    try {
      s(t.throw(n));
    } catch (c) {
      r(c);
    }
  }, s = (n) => n.done ? i(n.value) : Promise.resolve(n.value).then(m, g);
  s((t = t.apply(e, o)).next());
});
import { ref as a, computed as d, unref as p, defineComponent as f, resolveComponent as l, resolveDirective as _, withDirectives as h, openBlock as w, createElementBlock as C, normalizeClass as H, createVNode as v } from "vue";
import { useDesign as y } from "/@/hooks/web/useDesign";
import { useRootSetting as L } from "/@/hooks/setting/useRootSetting";
import { useTransitionSetting as P } from "/@/hooks/setting/useTransitionSetting";
import { createPageContext as R } from "/@/hooks/component/usePageContext";
import { useWindowSizeFn as V } from "/@/hooks/event/useWindowSizeFn";
import { _ as x } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const $ = a(0), D = a(0);
function S() {
  const e = a(window.innerHeight), o = a(window.innerHeight), t = d(() => p(e) - p($) - p(D) || 0);
  V(
    () => {
      e.value = window.innerHeight;
    },
    100,
    { immediate: !0 }
  );
  function i(r) {
    return u(this, null, function* () {
      o.value = r;
    });
  }
  R({
    contentHeight: t,
    setPageHeight: i,
    pageHeight: o
  });
}
const k = f({
  name: "LayoutContent",
  components: {},
  setup() {
    const { prefixCls: e } = y("layout-content"), { getOpenPageLoading: o } = P(), { getLayoutContentMode: t, getPageLoading: i } = L();
    return S(), {
      prefixCls: e,
      getOpenPageLoading: o,
      getLayoutContentMode: t,
      getPageLoading: i
    };
  }
});
function z(e, o, t, i, r, m) {
  const g = l("RouterView"), s = _("loading");
  return h((w(), C("div", {
    class: H([e.prefixCls, e.getLayoutContentMode])
  }, [
    v(g)
  ], 2)), [
    [s, e.getOpenPageLoading && e.getPageLoading]
  ]);
}
const A = /* @__PURE__ */ x(k, [["render", z]]);
export {
  A as default
};
