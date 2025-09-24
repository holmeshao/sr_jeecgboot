import { ref as r, watch as _, onMounted as f, resolveComponent as N, openBlock as g, createElementBlock as S, createElementVNode as o, createVNode as v } from "vue";
import { isArray as b } from "/@/utils/is";
import { _ as y } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const h = { class: "setSwitchOptions" }, w = "Y", Y = "N", x = {
  __name: "SetSwitchOptions",
  props: {
    value: {
      type: [Array, String],
      default: ["Y", "N"]
    }
  },
  emits: "change",
  setup(m, { emit: c }) {
    const d = m, u = c, n = r(w), a = r(Y);
    _(
      () => d.value,
      (t) => {
        if (typeof t == "string") {
          const e = t.split(",");
          n.value = e[0], a.value = e[1];
        } else
          b(t) && (n.value = t[0], a.value = t[1]);
      },
      { immediate: !0 }
    );
    const s = () => {
      n.value != "" && a.value != "" && i();
    }, i = () => {
      let t = Number(n.value), e = Number(a.value);
      (Number.isNaN(t) || Number.isNaN(e)) && (t = n.value, e = a.value), u("change", [t, e]), u("update:value", [t, e]);
    };
    return f(() => {
      i();
    }), (t, e) => {
      const p = N("a-input");
      return g(), S("div", h, [
        o("p", null, [
          e[2] || (e[2] = o("span", null, "是", -1)),
          v(p, {
            value: n.value,
            "onUpdate:value": e[0] || (e[0] = (l) => n.value = l),
            onChange: s
          }, null, 8, ["value"])
        ]),
        o("p", null, [
          e[3] || (e[3] = o("span", null, "否", -1)),
          v(p, {
            value: a.value,
            "onUpdate:value": e[1] || (e[1] = (l) => a.value = l),
            onChange: s
          }, null, 8, ["value"])
        ])
      ]);
    };
  }
}, U = /* @__PURE__ */ y(x, [["__scopeId", "data-v-b718aa7e"]]);
export {
  U as default
};
