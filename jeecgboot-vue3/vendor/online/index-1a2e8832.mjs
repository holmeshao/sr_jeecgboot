var C = (e, s, r) => new Promise((a, i) => {
  var c = (t) => {
    try {
      n(r.next(t));
    } catch (o) {
      i(o);
    }
  }, l = (t) => {
    try {
      n(r.throw(t));
    } catch (o) {
      i(o);
    }
  }, n = (t) => t.done ? a(t.value) : Promise.resolve(t.value).then(c, l);
  n((r = r.apply(e, s)).next());
});
import { Dropdown as D, Menu as g } from "ant-design-vue";
import { defineComponent as I, computed as _, resolveComponent as f, openBlock as k, createBlock as M, withCtx as d, createVNode as h, createElementVNode as p, normalizeClass as u, toDisplayString as A } from "vue";
import { useUserStore as $ } from "/@/store/modules/user";
import { useHeaderSetting as U } from "/@/hooks/setting/useHeaderSetting";
import { useI18n as x } from "/@/hooks/web/useI18n";
import { useDesign as L } from "/@/hooks/web/useDesign";
import { useMessage as N } from "/@/hooks/web/useMessage";
import y from "/@/assets/images/header.jpg";
import { propTypes as b } from "/@/utils/propTypes";
import { createAsyncComponent as O } from "/@/utils/factory/createAsyncComponent";
import { refreshCache as S, queryAllDictItems as B } from "/@/views/system/dict/dict.api";
import { DB_DICT_DATA_KEY as v } from "/@/enums/cacheEnum";
import { removeAuthCache as K, setAuthCache as T } from "/@/utils/auth";
import { getFileAccessHttpUrl as E } from "/@/utils/common/compUtils";
import { _ as F } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const { createMessage: w } = N(), H = I({
  name: "UserDropdown",
  components: {
    Dropdown: D,
    Menu: g,
    MenuItem: O(() => import("./DropMenuItem-1e49abea.mjs")),
    MenuDivider: g.Divider
  },
  props: {
    theme: b.oneOf(["dark", "light"])
  },
  setup() {
    const { prefixCls: e } = L("header-user-dropdown"), { t: s } = x(), { getUseLockPage: r } = U(), a = $(), i = _(() => {
      const { realname: o = "NoLogin", avatar: m } = a.getUserInfo || {};
      return {
        realname: o,
        avatar: m || y
      };
    }), c = _(() => {
      let { avatar: o } = i.value;
      return o == y ? o : E(o);
    });
    function l() {
      a.confirmLoginOut();
    }
    function n() {
      return C(this, null, function* () {
        if ((yield S()).success) {
          const m = yield B();
          K(v), T(v, m.result), w.success(s("layout.header.refreshCacheComplete")), a.setAllDictItems(m.result);
        } else
          w.error(s("layout.header.refreshCacheFailure"));
      });
    }
    function t(o) {
      switch (o.key) {
        case "logout":
          l();
          break;
        case "cache":
          n();
          break;
      }
    }
    return {
      prefixCls: e,
      t: s,
      getUserInfo: i,
      getAvatarUrl: c,
      handleMenuClick: t,
      getUseLockPage: r
    };
  }
});
const V = ["src"];
function q(e, s, r, a, i, c) {
  const l = f("MenuItem"), n = f("Menu"), t = f("Dropdown");
  return k(), M(t, {
    placement: "bottomLeft",
    overlayClassName: `${e.prefixCls}-dropdown-overlay`
  }, {
    overlay: d(() => [
      h(n, { onClick: e.handleMenuClick }, {
        default: d(() => [
          h(l, {
            itemKey: "cache",
            text: e.t("layout.header.dropdownItemRefreshCache"),
            icon: "ion:sync-outline"
          }, null, 8, ["text"]),
          h(l, {
            itemKey: "logout",
            text: e.t("layout.header.dropdownItemLoginOut"),
            icon: "ion:power-outline"
          }, null, 8, ["text"])
        ]),
        _: 1
      }, 8, ["onClick"])
    ]),
    default: d(() => [
      p("span", {
        class: u([[e.prefixCls, `${e.prefixCls}--${e.theme}`], "flex"])
      }, [
        p("img", {
          class: u(`${e.prefixCls}__header`),
          src: e.getAvatarUrl
        }, null, 10, V),
        p("span", {
          class: u(`${e.prefixCls}__info hidden md:block`)
        }, [
          p("span", {
            class: u([`${e.prefixCls}__name  `, "truncate"])
          }, A(e.getUserInfo.realname), 3)
        ], 2)
      ], 2)
    ]),
    _: 1
  }, 8, ["overlayClassName"]);
}
const ie = /* @__PURE__ */ F(H, [["render", q]]);
export {
  ie as default
};
