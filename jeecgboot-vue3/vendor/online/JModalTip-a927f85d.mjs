import { computed as a, openBlock as l, createElementBlock as r, normalizeClass as c, createElementVNode as t, createCommentVNode as p } from "vue";
import { useAppInject as d } from "/@/hooks/web/useAppInject";
import { _ as m } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const _ = {
  name: "JModalTip",
  props: {
    visible: {
      type: Boolean,
      default: !1
    }
  },
  emits: ["save", "cancel"],
  setup(o) {
    const { getIsMobile: e } = d();
    return {
      flag: a(() => o.visible),
      getIsMobile: e
    };
  }
}, v = { class: "container" }, f = { class: "outer" }, u = { class: "inner" };
function g(o, e, i, s, b, $) {
  return s.flag ? (l(), r("div", {
    key: 0,
    class: c(["jeecg-update-tip-bar", { mobile: s.getIsMobile }])
  }, [
    t("div", v, [
      t("div", f, [
        t("div", u, [
          e[2] || (e[2] = t("span", { class: "tip" }, "正在修改表单数据 ···", -1)),
          t("div", {
            class: "cancel",
            onClick: e[0] || (e[0] = (n) => o.$emit("cancel"))
          }, "取消"),
          t("div", {
            class: "save",
            onClick: e[1] || (e[1] = (n) => o.$emit("save"))
          }, "保存")
        ])
      ])
    ])
  ], 2)) : p("", !0);
}
const J = /* @__PURE__ */ m(_, [["render", g], ["__scopeId", "data-v-dc9246f4"]]);
export {
  J as default
};
