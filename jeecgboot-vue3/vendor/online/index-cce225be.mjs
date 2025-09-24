import { defineComponent as $, computed as _, unref as n, ref as H, onMounted as w, toRaw as D, resolveComponent as i, openBlock as y, createElementBlock as C, Fragment as I, createVNode as l, normalizeClass as r, withCtx as v, createElementVNode as u, normalizeStyle as x, toDisplayString as S, createCommentVNode as M } from "vue";
import { useGlobSetting as b } from "/@/hooks/setting";
import { propTypes as A } from "/@/utils/propTypes";
import { Layout as T } from "ant-design-vue";
import { AppLogo as F } from "/@/components/Application";
import { useHeaderSetting as U } from "/@/hooks/setting/useHeaderSetting";
import { useMenuSetting as k } from "/@/hooks/setting/useMenuSetting";
import { LayoutBreadcrumb as B, FullScreen as N } from "/@/layouts/default/header/components";
import { createAsyncComponent as R } from "/@/utils/factory/createAsyncComponent";
import { useAppInject as V } from "/@/hooks/web/useAppInject";
import { useDesign as W } from "/@/hooks/web/useDesign";
import z from "/@/views/sys/login/LoginSelect.vue";
import { useUserStore as E } from "/@/store/modules/user";
import { useI18n as j } from "/@/hooks/web/useI18n";
import { _ as G } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const q = R(() => import("./index-1a2e8832.mjs"), {
  loading: !0
}), { t: J } = j(), K = $({
  name: "LayoutHeader",
  components: {
    Header: T.Header,
    AppLogo: F,
    LayoutBreadcrumb: B,
    UserDropDown: q,
    FullScreen: N,
    LoginSelect: z
  },
  props: {
    fixed: A.bool
  },
  setup(e) {
    const { prefixCls: o } = W("layout-header"), f = E(), {
      getIsMixMode: g,
      getMenuWidth: a
    } = k(), { title: h } = b(), {
      getHeaderTheme: m
    } = U(), { getIsMobile: s } = V(), c = _(() => {
      const t = n(m);
      return [
        o,
        {
          [`${o}--fixed`]: e.fixed,
          [`${o}--mobile`]: n(s),
          [`${o}--${t}`]: t
        }
      ];
    }), d = _(() => !n(g) || n(s) ? {} : { width: `${n(a) < 180 ? 180 : n(a)}px` }), p = H();
    function L() {
      const t = D(f.getLoginInfo) || {};
      t.isLogin && p.value.show(t);
    }
    return w(() => {
      L();
    }), {
      t: J,
      title: h,
      loginSelectRef: p,
      prefixCls: o,
      getHeaderClass: c,
      getHeaderTheme: m,
      getIsMobile: s,
      getLogoWidth: d
    };
  }
});
const O = {
  key: 0,
  style: { height: "60px" }
};
function P(e, o, f, g, a, h) {
  const m = i("AppLogo"), s = i("FullScreen"), c = i("UserDropDown"), d = i("Header"), p = i("LoginSelect");
  return y(), C(I, null, [
    l(d, {
      class: r(e.getHeaderClass)
    }, {
      default: v(() => [
        u("div", {
          class: r(`${e.prefixCls}-left`)
        }, [
          l(m, {
            class: r(`${e.prefixCls}-logo`),
            theme: e.getHeaderTheme,
            style: x(e.getLogoWidth)
          }, null, 8, ["class", "theme", "style"]),
          u("span", {
            class: r([e.prefixCls, `${e.prefixCls}--${e.getHeaderTheme}`, "headerIntroductionClass"])
          }, S(e.t("layout.header.welcomeIn")) + " " + S(e.title), 3)
        ], 2),
        u("div", {
          class: r(`${e.prefixCls}-action`)
        }, [
          l(s, {
            class: r(`${e.prefixCls}-action__item fullscreen-item`)
          }, null, 8, ["class"]),
          l(c, { theme: e.getHeaderTheme }, null, 8, ["theme"])
        ], 2)
      ]),
      _: 1
    }, 8, ["class"]),
    l(p, { ref: "loginSelectRef" }, null, 512),
    e.fixed ? (y(), C("div", O)) : M("", !0)
  ], 64);
}
const fe = /* @__PURE__ */ G(K, [["render", P]]);
export {
  fe as default
};
