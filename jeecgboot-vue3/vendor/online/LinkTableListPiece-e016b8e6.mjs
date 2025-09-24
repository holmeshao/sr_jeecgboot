import { propTypes as o } from "/@/utils/propTypes";
import { openBlock as a, createElementBlock as s, createElementVNode as l, toDisplayString as p } from "vue";
import { _ as c } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const d = {
  name: "LinkTableListPiece",
  props: {
    text: o.string.def(""),
    id: o.string.def("")
  },
  emits: ["tab"],
  setup(n, { emit: e }) {
    function i(t) {
      t == null || t.stopPropagation(), t == null || t.preventDefault(), e("tab", n.id);
    }
    return {
      handleClick: i
    };
  }
};
function _(n, e, i, t, m, f) {
  return a(), s("div", {
    class: "link-table-piece",
    onClick: e[0] || (e[0] = (...r) => t.handleClick && t.handleClick(...r))
  }, [
    l("span", null, p(i.text), 1)
  ]);
}
const v = /* @__PURE__ */ c(d, [["render", _], ["__scopeId", "data-v-ade036b6"]]);
export {
  v as default
};
