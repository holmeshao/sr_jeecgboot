var C = (e, s, i) => new Promise((r, u) => {
  var t = (n) => {
    try {
      o(i.next(n));
    } catch (a) {
      u(a);
    }
  }, m = (n) => {
    try {
      o(i.throw(n));
    } catch (a) {
      u(a);
    }
  }, o = (n) => n.done ? r(n.value) : Promise.resolve(n.value).then(t, m);
  o((i = i.apply(e, s)).next());
});
import { ref as v, defineComponent as k, computed as I, unref as R, watch as A, resolveComponent as c, openBlock as d, createElementBlock as H, createVNode as g, createBlock as _, mergeProps as b, withCtx as y, createCommentVNode as x, normalizeClass as S } from "vue";
import { Layout as D } from "ant-design-vue";
import M from "./index-cce225be.mjs";
import w from "./index-f2c2d1cf.mjs";
import { _ as U } from "./ErrorTip.vue_vue_type_script_setup_true_lang-49d35987.mjs";
import { useHeaderSetting as N } from "/@/hooks/setting/useHeaderSetting";
import { useMenuSetting as B } from "/@/hooks/setting/useMenuSetting";
import { useDesign as $ } from "/@/hooks/web/useDesign";
import { useLockPage as O } from "/@/hooks/web/useLockPage";
import { useAppInject as P } from "/@/hooks/web/useAppInject";
import { router as E } from "/@/router";
import { a as q, b as F, c as T, S as h } from "./index-5e89258d.mjs";
import { u as V } from "./shareStore-7de6c7a6.mjs";
import { useUserStore as j } from "/@/store/modules/user";
import { defHttp as L } from "/@/utils/http/axios";
import { p as z } from "./utils-9fce7606.mjs";
import { _ as G } from "./index-9e1e1e53.mjs";
import "/@/hooks/setting";
import "/@/utils/propTypes";
import "/@/components/Application";
import "/@/layouts/default/header/components";
import "/@/utils/factory/createAsyncComponent";
import "/@/views/sys/login/LoginSelect.vue";
import "/@/hooks/web/useI18n";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
import "/@/hooks/setting/useRootSetting";
import "/@/hooks/setting/useTransitionSetting";
import "/@/hooks/component/usePageContext";
import "/@/hooks/event/useWindowSizeFn";
import "/@/enums/pageEnum";
import "pinia";
import "/@/utils/auth";
import "/@/enums/cacheEnum";
import "/@/api/sys/user";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
const J = (e) => L.get({
  url: "/online/cgform/head/queryById",
  params: { id: e }
}, { isTransformResponse: !1 }), K = (e, s) => L.get({
  url: "/online/cgform/api/form/{formId}/{recordId}".replace("{formId}", e).replace("{recordId}", s),
  params: {}
}, { isTransformResponse: !1 });
function Q() {
  const e = j(), s = V(), i = v(!0), r = v("");
  function u() {
    return C(this, null, function* () {
      try {
        yield s.checkUrlToken();
        const t = E.currentRoute.value;
        if (!e.getToken) {
          E.push({
            name: q,
            query: {
              redirect: encodeURIComponent(t.path)
            }
          });
          return;
        }
        const { id: m } = t.params;
        if (!m) {
          r.value = "参数错误";
          return;
        }
        let o = yield J(m);
        if (!o.success) {
          r.value = o.message;
          return;
        }
        const n = o.result, a = z(n);
        if (!(a != null && a.enableExternalLink)) {
          r.value = "当前表单未开启外部链接";
          return;
        }
        let p = a.externalLinkActions.split(",");
        if (t.name === F) {
          if (!p.includes("add")) {
            r.value = "当前表单不支持外部新增";
            return;
          }
        } else if (t.name === T) {
          if (!p.includes("edit")) {
            r.value = "当前表单不支持外部编辑";
            return;
          }
        } else if (t.name === h) {
          if (!p.includes("detail")) {
            r.value = "当前表单不支持外部详情";
            return;
          }
        } else {
          r.value = "未知的页面";
          return;
        }
        if (n.tableType == 3) {
          r.value = "不支持附表外部链接";
          return;
        }
        if (s.setCgformRecord(n), t.name === T || t.name === h) {
          const { dataId: l } = t.params;
          if (!l) {
            r.value = "参数错误";
            return;
          }
          if (o = yield K(m, l), !o.success) {
            r.value = o.message;
            return;
          }
          const f = o.result;
          if ((f == null ? void 0 : f.id) !== l) {
            r.value = "数据不存在或已删除";
            return;
          }
          s.setDataRecord(f);
        }
      } catch (t) {
        r.value = (t == null ? void 0 : t.message) || t;
      } finally {
        i.value = !1;
      }
    });
  }
  return {
    pageLoading: i,
    pageErrorTip: r,
    initCgformShare: u
  };
}
const W = k({
  name: "DefaultLayout",
  components: {
    LayoutHeader: M,
    LayoutContent: w,
    ErrorTip: U,
    Layout: D
  },
  setup() {
    const { prefixCls: e } = $("default-layout"), { initCgformShare: s, pageLoading: i, pageErrorTip: r } = Q(), { getIsMobile: u } = P(), { getShowFullHeaderRef: t } = N(), { getShowSidebar: m, getIsMixSidebar: o, getShowMenu: n } = B(), a = O(), p = I(() => {
      let l = ["ant-layout"];
      return (R(o) || R(n)) && l.push("ant-layout-has-sider"), l;
    });
    return A(E.currentRoute, () => {
      s();
    }, { immediate: !0 }), {
      pageLoading: i,
      pageErrorTip: r,
      getShowFullHeaderRef: t,
      getShowSidebar: m,
      prefixCls: e,
      getIsMobile: u,
      getIsMixSidebar: o,
      layoutClass: p,
      lockEvents: a
    };
  }
});
const X = {
  key: 0,
  style: { "text-align": "center", "padding-top": "120px" }
};
function Y(e, s, i, r, u, t) {
  const m = c("a-spin"), o = c("ErrorTip"), n = c("LayoutHeader"), a = c("LayoutContent"), p = c("Layout");
  return e.pageLoading ? (d(), H("div", X, [
    g(m, { tip: "加载中…" })
  ])) : e.pageErrorTip ? (d(), _(o, {
    key: 1,
    subTitle: e.pageErrorTip
  }, null, 8, ["subTitle"])) : (d(), _(p, b({
    key: 2,
    class: e.prefixCls
  }, e.lockEvents), {
    default: y(() => [
      e.getIsMobile ? x("", !0) : (d(), _(n, {
        key: 0,
        fixed: ""
      })),
      g(p, {
        class: S([e.layoutClass])
      }, {
        default: y(() => [
          g(p, {
            class: S(`${e.prefixCls}-main`)
          }, {
            default: y(() => [
              g(a)
            ]),
            _: 1
          }, 8, ["class"])
        ]),
        _: 1
      }, 8, ["class"])
    ]),
    _: 1
  }, 16, ["class"]));
}
const Pe = /* @__PURE__ */ G(W, [["render", Y]]);
export {
  Pe as default
};
