var E = (e, n, d) => new Promise((C, y) => {
  var p = (u) => {
    try {
      l(d.next(u));
    } catch (s) {
      y(s);
    }
  }, a = (u) => {
    try {
      l(d.throw(u));
    } catch (s) {
      y(s);
    }
  }, l = (u) => u.done ? C(u.value) : Promise.resolve(u.value).then(p, a);
  l((d = d.apply(e, n)).next());
});
import { defineComponent as P, ref as f, computed as R, watch as V, unref as $, resolveComponent as m, openBlock as _, createElementBlock as A, createBlock as b, Fragment as q, createElementVNode as H, createVNode as r, withCtx as h, createTextVNode as k } from "vue";
import { useMessage as J } from "/@/hooks/web/useMessage";
import { m as L, n as j, s as G } from "./auth.api-53df4c33.mjs";
import { DownCircleOutlined as Q, HomeOutlined as W, UpCircleOutlined as X, UndoOutlined as Y, CheckOutlined as Z } from "@ant-design/icons-vue";
import { _ as ee } from "./index-9e1e1e53.mjs";
import "/@/utils/http/axios";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const ne = P({
  name: "AuthFieldTree",
  components: {
    DownCircleOutlined: Q,
    HomeOutlined: W,
    UpCircleOutlined: X,
    UndoOutlined: Y,
    CheckOutlined: Z
  },
  props: {
    cgformId: { type: String, required: !0 }
  },
  setup(e) {
    const { createMessage: n } = J(), d = f(""), C = f(1), y = f(!0), p = f([]), a = f([]), l = f([]), u = f([]), s = f(""), O = R(() => !d.value);
    V(() => e.cgformId, v, { immediate: !0 });
    function v() {
      return E(this, null, function* () {
        if (!e.cgformId)
          return;
        let t = yield L(e.cgformId, C.value), o = [], i = [];
        t.forEach((c) => {
          i.includes(c.code) || (i.push(c.code), o.push({ key: c.code, title: c.title }));
        });
        for (let c of o) {
          let I = [];
          for (let F of t)
            if (c.key === F.code) {
              let M = D(F);
              I.push({ key: F.id, title: M });
            }
          c.children = I;
        }
        u.value = o, p.value = [...i], l.value = i;
      });
    }
    function D(t) {
      let o = "";
      return t.page == 3 ? o += "列表" : t.page == 5 && (o += "表单"), t.control == 3 ? o += "可编辑" : t.control == 5 && (o += "可见"), o;
    }
    function g(t, o) {
      return E(this, null, function* () {
        d.value = t, s.value = o, a.value = [], yield v();
        let i = yield j({
          roleId: t,
          cgformId: e.cgformId,
          type: C.value,
          authMode: o
        });
        a.value = i.map((c) => c.authId);
      });
    }
    function K() {
      d.value = "", v();
    }
    function w() {
      v(), g(d.value, s.value);
    }
    function T() {
      return E(this, null, function* () {
        let t = a.value.filter((o) => l.value.indexOf(o) < 0);
        yield G(d.value, e.cgformId, {
          authId: JSON.stringify(t),
          authMode: s.value
        }), n.success("保存成功");
      });
    }
    function S() {
      p.value = [...l.value];
    }
    function x() {
      p.value = [];
    }
    function U(t) {
      p.value = t, y.value = !1;
    }
    function z() {
      d.value = "", a.value = [];
    }
    function B() {
      a.value = [];
    }
    function N() {
      const t = function(o) {
        for (let i of o)
          a.value.push(i.key), i.children && i.children.length > 0 && t.call(null, i.children);
      };
      a.value = [], t.call(null, $(u));
    }
    return {
      loadChecked: g,
      clear: z,
      expandedKeys: p,
      autoExpandParent: y,
      checkedKeys: a,
      treeData: u,
      disabled: O,
      onSave: T,
      onExpand: U,
      clearChecked: K,
      onCloseAll: x,
      onExpandAll: S,
      onRefresh: w,
      onClearSelected: B,
      onSelectAll: N
    };
  }
});
const te = { class: "onl-auth-tree-btns" };
function oe(e, n, d, C, y, p) {
  const a = m("a-empty"), l = m("a-button"), u = m("DownCircleOutlined"), s = m("UpCircleOutlined"), O = m("CheckOutlined"), v = m("UndoOutlined"), D = m("a-tree");
  return _(), A("div", null, [
    e.disabled ? (_(), b(a, {
      key: 0,
      description: "请先选中左侧角色/部门/用户"
    })) : e.treeData.length === 0 ? (_(), b(a, {
      key: 1,
      description: "无权限信息"
    })) : (_(), A(q, { key: 2 }, [
      H("div", te, [
        r(l, {
          onClick: e.onRefresh,
          size: "small",
          type: "primary",
          preIcon: "ant-design:redo",
          ghost: ""
        }, {
          default: h(() => n[1] || (n[1] = [
            k("刷新")
          ])),
          _: 1
        }, 8, ["onClick"]),
        r(l, {
          onClick: e.onExpandAll,
          size: "small",
          type: "primary",
          ghost: ""
        }, {
          default: h(() => [
            r(u),
            n[2] || (n[2] = k("展开"))
          ]),
          _: 1
        }, 8, ["onClick"]),
        r(l, {
          onClick: e.onCloseAll,
          size: "small",
          type: "primary",
          ghost: ""
        }, {
          default: h(() => [
            r(s),
            n[3] || (n[3] = k("折叠"))
          ]),
          _: 1
        }, 8, ["onClick"]),
        r(l, {
          onClick: e.onSave,
          size: "small",
          type: "primary",
          preIcon: "ant-design:save",
          ghost: ""
        }, {
          default: h(() => n[4] || (n[4] = [
            k("保存")
          ])),
          _: 1
        }, 8, ["onClick"]),
        r(l, {
          onClick: e.onSelectAll,
          size: "small",
          type: "primary",
          ghost: ""
        }, {
          default: h(() => [
            r(O),
            n[5] || (n[5] = k("全选"))
          ]),
          _: 1
        }, 8, ["onClick"]),
        r(l, {
          onClick: e.onClearSelected,
          size: "small",
          type: "primary",
          ghost: ""
        }, {
          default: h(() => [
            r(v),
            n[6] || (n[6] = k("重置"))
          ]),
          _: 1
        }, 8, ["onClick"])
      ]),
      r(D, {
        checkable: "",
        checkedKeys: e.checkedKeys,
        "onUpdate:checkedKeys": n[0] || (n[0] = (g) => e.checkedKeys = g),
        expandedKeys: e.expandedKeys,
        autoExpandParent: e.autoExpandParent,
        treeData: e.treeData,
        onExpand: e.onExpand
      }, null, 8, ["checkedKeys", "expandedKeys", "autoExpandParent", "treeData", "onExpand"])
    ], 64))
  ]);
}
const fe = /* @__PURE__ */ ee(ne, [["render", oe], ["__scopeId", "data-v-e6dabb1d"]]);
export {
  fe as default
};
