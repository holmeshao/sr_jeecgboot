import { Menu as a } from "ant-design-vue";
import { defineComponent as u, getCurrentInstance as l, computed as f, resolveComponent as m, openBlock as _, createBlock as d, withCtx as I, createElementVNode as p, createVNode as y, toDisplayString as K } from "vue";
import M from "/@/components/Icon/index";
import { propTypes as r } from "/@/utils/propTypes";
import { _ as g } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const x = u({
  name: "DropdownMenuItem",
  components: { MenuItem: a.Item, Icon: M },
  props: {
    // 【issues/6855】
    itemKey: r.string,
    text: r.string,
    icon: r.string
  },
  setup(e) {
    const t = l();
    return { itemKey: f(() => {
      var o, n;
      return e.itemKey || ((n = (o = t == null ? void 0 : t.vnode) == null ? void 0 : o.props) == null ? void 0 : n.itemKey);
    }) };
  }
}), C = { class: "flex items-center" };
function $(e, t, s, o, n, h) {
  const c = m("Icon"), i = m("MenuItem");
  return _(), d(i, { key: e.itemKey }, {
    default: I(() => [
      p("span", C, [
        y(c, {
          icon: e.icon,
          class: "mr-1"
        }, null, 8, ["icon"]),
        p("span", null, K(e.text), 1)
      ])
    ]),
    _: 1
  });
}
const S = /* @__PURE__ */ g(x, [["render", $]]);
export {
  S as default
};
