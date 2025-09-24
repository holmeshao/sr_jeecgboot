var u = (e, i, o) => new Promise((n, a) => {
  var c = (t) => {
    try {
      l(o.next(t));
    } catch (r) {
      a(r);
    }
  }, s = (t) => {
    try {
      l(o.throw(t));
    } catch (r) {
      a(r);
    }
  }, l = (t) => t.done ? n(t.value) : Promise.resolve(t.value).then(c, s);
  l((o = o.apply(e, i)).next());
});
import { defineComponent as R, ref as f, resolveComponent as p, openBlock as v, createBlock as h, withCtx as m, createVNode as g, createElementVNode as k, createTextVNode as D, createCommentVNode as w } from "vue";
import { defHttp as y } from "/@/utils/http/axios";
import { BasicModal as B, useModalInner as b } from "/@/components/Modal";
import { _ as F } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const E = R({
  name: "FileSelectModal",
  components: { BasicModal: B },
  emits: ["select", "register"],
  setup(e, { emit: i }) {
    const o = f(!0), n = f([]), a = f(""), c = f(!1), [s, { closeModal: l }] = b(() => u(this, null, function* () {
      a.value = "", n.value.length === 0 && _();
    }));
    function t() {
      i("select", a.value), l();
    }
    function r() {
      l();
    }
    function _() {
      return u(this, null, function* () {
        o.value = !0, n.value = yield y.get({
          url: "/online/cgform/head/rootFile"
          /* rootFileUrl */
        }).finally(() => {
          o.value = !1, c.value = !0;
        });
      });
    }
    function S(d) {
      return u(this, null, function* () {
        if (d.dataRef.children)
          return;
        let M = {
          parentPath: d.dataRef.key
        };
        d.dataRef.children = yield y.get({ url: "/online/cgform/head/fileTree", params: M }), n.value = [...n.value];
      });
    }
    function C(d) {
      a.value = d[0];
    }
    return { loading: o, treeData: n, onLoadData: S, onSelect: C, onSubmit: t, onCancel: r, registerModal: s, hanldeRefresh: () => {
      a.value = "", c.value = !1, _();
    }, directoryTreeShow: c };
  }
});
const T = { class: "btnArea" };
function V(e, i, o, n, a, c) {
  const s = p("a-button"), l = p("a-directory-tree"), t = p("a-spin"), r = p("BasicModal");
  return v(), h(r, {
    onRegister: e.registerModal,
    title: "选择目录",
    width: 500,
    onOk: e.onSubmit,
    onCancel: e.onCancel
  }, {
    default: m(() => [
      g(t, { spinning: e.loading }, {
        default: m(() => [
          k("div", T, [
            g(s, { onClick: e.hanldeRefresh }, {
              default: m(() => i[0] || (i[0] = [
                D("刷新")
              ])),
              _: 1
            }, 8, ["onClick"])
          ]),
          e.directoryTreeShow ? (v(), h(l, {
            key: 0,
            treeData: e.treeData,
            loadData: e.onLoadData,
            onSelect: e.onSelect
          }, null, 8, ["treeData", "loadData", "onSelect"])) : w("", !0)
        ]),
        _: 1
      }, 8, ["spinning"])
    ]),
    _: 1
  }, 8, ["onRegister", "onOk", "onCancel"]);
}
const j = /* @__PURE__ */ F(E, [["render", V], ["__scopeId", "data-v-102e9e9f"]]);
export {
  j as default
};
