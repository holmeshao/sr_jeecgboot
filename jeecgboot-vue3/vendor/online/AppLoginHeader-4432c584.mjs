import { defineComponent as t, openBlock as i, createElementBlock as r, createElementVNode as o, unref as s } from "vue";
import p from "/@/assets/images/logo.png";
import { _ } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const a = { class: "login-header" }, l = ["src"], c = /* @__PURE__ */ t({
  __name: "AppLoginHeader",
  setup(m) {
    function n() {
      window.open("https://www.qiaoqiaoyun.com/", "_target");
    }
    return (d, e) => (i(), r("div", a, [
      o("img", {
        src: s(p),
        alt: "敲敲云",
        class: "login-image",
        onClick: n
      }, null, 8, l),
      e[0] || (e[0] = o("span", { class: "login-header-name" }, null, -1))
    ]));
  }
});
const v = /* @__PURE__ */ _(c, [["__scopeId", "data-v-6df6e301"]]);
export {
  v as default
};
