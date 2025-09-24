var f = (n, r, o) => new Promise((s, c) => {
  var i = (e) => {
    try {
      a(o.next(e));
    } catch (t) {
      c(t);
    }
  }, d = (e) => {
    try {
      a(o.throw(e));
    } catch (t) {
      c(t);
    }
  }, a = (e) => e.done ? s(e.value) : Promise.resolve(e.value).then(i, d);
  a((o = o.apply(n, r)).next());
});
import { defineComponent as h, ref as u, resolveComponent as m, openBlock as y, createElementBlock as K, createBlock as _, withCtx as D, createVNode as v, normalizeStyle as S, createCommentVNode as k } from "vue";
import { queryTreeList as x } from "/@/api/common/api";
import { _ as C } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const N = h({
  name: "LeftDepart",
  emits: ["select"],
  setup(n, { emit: r }) {
    const o = u([]), s = u([]), c = u([]);
    function i(t, l) {
      let p = l.node.dataRef;
      s.value = [p.key], r("select", p.id);
    }
    d();
    function d() {
      return f(this, null, function* () {
        let t = yield x();
        o.value = [], t.forEach((l) => a(l));
      });
    }
    function a(t, l = 1) {
      if (t.slots = { icon: "depIcon" }, l === 1 && (o.value.push(t), c.value.push(t.id)), t.children && t.children.length > 0)
        for (const p of t.children)
          a(p, l + 1);
    }
    function e() {
      s.value = [];
    }
    return {
      treeData: o,
      expandedKeys: c,
      selectedKeys: s,
      clearSelected: e,
      onSelect: i
    };
  }
});
function $(n, r, o, s, c, i) {
  const d = m("a-icon"), a = m("a-tree");
  return y(), K("div", null, [
    n.treeData.length > 0 ? (y(), _(a, {
      key: 0,
      showIcon: "",
      autoExpandParent: "",
      treeData: n.treeData,
      selectedKeys: n.selectedKeys,
      expandedKeys: n.expandedKeys,
      "onUpdate:expandedKeys": r[0] || (r[0] = (e) => n.expandedKeys = e),
      onSelect: n.onSelect
    }, {
      depIcon: D(({ selected: e }) => [
        v(d, {
          style: S({ color: e ? "blue" : "" }),
          type: "apartment"
        }, null, 8, ["style"])
      ]),
      _: 1
    }, 8, ["treeData", "selectedKeys", "expandedKeys", "onSelect"])) : k("", !0)
  ]);
}
const V = /* @__PURE__ */ C(N, [["render", $]]);
export {
  V as default
};
