var f = (e, n, t) => new Promise((l, d) => {
  var i = (a) => {
    try {
      u(t.next(a));
    } catch (r) {
      d(r);
    }
  }, o = (a) => {
    try {
      u(t.throw(a));
    } catch (r) {
      d(r);
    }
  }, u = (a) => a.done ? l(a.value) : Promise.resolve(a.value).then(i, o);
  u((t = t.apply(e, n)).next());
});
import { defineComponent as A, ref as s, computed as B, watch as F, resolveComponent as v, openBlock as y, createElementBlock as g, createBlock as _, Fragment as N, createElementVNode as M, createVNode as h, withCtx as E, createTextVNode as D } from "vue";
import { useMessage as P } from "/@/hooks/web/useMessage";
import { q as S, n as R, r as V } from "./auth.api-53df4c33.mjs";
import { _ as $ } from "./index-9e1e1e53.mjs";
import "/@/utils/http/axios";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const q = A({
  name: "AuthDataTree",
  props: {
    cgformId: { type: String, required: !0 }
  },
  setup(e) {
    const { createMessage: n } = P(), t = s(""), l = s(3), d = s(!0), i = s([]), o = s([]), u = s([]), a = s(""), r = B(() => !t.value);
    F(() => e.cgformId, m, { immediate: !0 });
    function m() {
      return f(this, null, function* () {
        if (!e.cgformId)
          return;
        let c = yield S(e.cgformId, l.value);
        u.value = c.map((p) => ({ key: p.id, title: p.ruleName }));
      });
    }
    function k(c, p) {
      return f(this, null, function* () {
        t.value = c, a.value = p, o.value = [], yield m();
        let T = yield R({
          roleId: c,
          cgformId: e.cgformId,
          type: l.value,
          authMode: p
        });
        o.value = T.map((w) => w.authId);
      });
    }
    function I() {
      t.value = "", m();
    }
    function C() {
      m(), k(t.value, a.value);
    }
    function K() {
      return f(this, null, function* () {
        yield V(t.value, e.cgformId, {
          authId: JSON.stringify(o.value),
          authMode: a.value
        }), n.success("保存成功");
      });
    }
    function b(c) {
      i.value = c, d.value = !1;
    }
    function x() {
      t.value = "", o.value = [];
    }
    return {
      loadChecked: k,
      clear: x,
      expandedKeys: i,
      autoExpandParent: d,
      checkedKeys: o,
      treeData: u,
      disabled: r,
      onSave: K,
      onExpand: b,
      onRefresh: C,
      clearChecked: I
    };
  }
});
const z = { class: "onl-auth-tree-btns" };
function J(e, n, t, l, d, i) {
  const o = v("a-empty"), u = v("a-button"), a = v("a-tree");
  return y(), g("div", null, [
    e.disabled ? (y(), _(o, {
      key: 0,
      description: "请先选中左侧角色/部门/用户"
    })) : e.treeData.length === 0 ? (y(), _(o, {
      key: 1,
      description: "无权限信息"
    })) : (y(), g(N, { key: 2 }, [
      M("div", z, [
        h(u, {
          onClick: e.onRefresh,
          size: "small",
          type: "primary",
          preIcon: "ant-design:redo",
          ghost: ""
        }, {
          default: E(() => n[1] || (n[1] = [
            D("刷新")
          ])),
          _: 1
        }, 8, ["onClick"]),
        h(u, {
          onClick: e.onSave,
          size: "small",
          type: "primary",
          preIcon: "ant-design:save",
          ghost: ""
        }, {
          default: E(() => n[2] || (n[2] = [
            D("保存")
          ])),
          _: 1
        }, 8, ["onClick"])
      ]),
      h(a, {
        checkable: "",
        checkedKeys: e.checkedKeys,
        "onUpdate:checkedKeys": n[0] || (n[0] = (r) => e.checkedKeys = r),
        expandedKeys: e.expandedKeys,
        autoExpandParent: e.autoExpandParent,
        treeData: e.treeData,
        onExpand: e.onExpand
      }, null, 8, ["checkedKeys", "expandedKeys", "autoExpandParent", "treeData", "onExpand"])
    ], 64))
  ]);
}
const X = /* @__PURE__ */ $(q, [["render", J], ["__scopeId", "data-v-c6be2157"]]);
export {
  X as default
};
