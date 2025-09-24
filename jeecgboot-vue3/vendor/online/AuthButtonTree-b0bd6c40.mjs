var h = (e, o, c) => new Promise((u, p) => {
  var m = (r) => {
    try {
      n(c.next(r));
    } catch (d) {
      p(d);
    }
  }, l = (r) => {
    try {
      n(c.throw(r));
    } catch (d) {
      p(d);
    }
  }, n = (r) => r.done ? u(r.value) : Promise.resolve(r.value).then(m, l);
  n((c = c.apply(e, o)).next());
});
import { defineComponent as N, ref as i, computed as w, watch as M, resolveComponent as g, openBlock as v, createElementBlock as b, createBlock as E, Fragment as S, createElementVNode as P, createVNode as k, withCtx as I, createTextVNode as B } from "vue";
import { useMessage as $ } from "/@/hooks/web/useMessage";
import { c as R } from "./auth.data-626c5083.mjs";
import { o as V, n as z, p as L } from "./auth.api-53df4c33.mjs";
import { _ as j } from "./index-9e1e1e53.mjs";
import "/@/utils/index";
import "/@/utils/http/axios";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const q = N({
  name: "AuthButtonTree",
  props: {
    cgformId: { type: String, required: !0 }
  },
  setup(e) {
    const { createMessage: o, createSuccessModal: c } = $(), u = i(""), p = i(2), m = i(!0), l = i([]), n = i([]), r = i([]), d = i(""), C = w(() => !u.value);
    M(() => e.cgformId, y, { immediate: !0 });
    function y() {
      return h(this, null, function* () {
        if (!e.cgformId)
          return;
        let t = yield V(e.cgformId, p.value);
        t.forEach((a) => {
          for (const f of R)
            if (a.code == f.code) {
              a.title || (a.title = f.title);
              break;
            }
        });
        let s = [];
        for (let a of t) {
          let f = T(a);
          s.push({ key: a.id, title: f });
        }
        r.value = s;
      });
    }
    function D() {
      y(), _(u.value, d.value);
    }
    function _(t, s) {
      return h(this, null, function* () {
        u.value = t, d.value = s, n.value = [], yield y();
        let a = yield z({
          roleId: t,
          cgformId: e.cgformId,
          type: p.value,
          authMode: s
        });
        n.value = a.map((f) => f.authId);
      });
    }
    function K() {
      u.value = "", y();
    }
    function x() {
      return h(this, null, function* () {
        try {
          const { success: t, message: s, result: a } = yield L(u.value, e.cgformId, {
            authId: JSON.stringify(n.value),
            authMode: d.value
          });
          t ? Array.isArray(a == null ? void 0 : a.disabledNames) ? c({
            title: "保存成功",
            content: `由于以下按钮未激活，所以权限未生效。<br>${a.disabledNames.join("<br>")}`
          }) : o.success("保存成功") : o.error(s);
        } catch (t) {
          o.error("保存出现异常");
        }
      });
    }
    function T(t) {
      let s = t.title + "-";
      return t.code && t.code.includes("form_sub") ? s += "表单可见（附表）" : t.page == 3 ? s += "列表可见" : t.page == 5 && (s += "表单可见"), s;
    }
    function A(t) {
      l.value = t, m.value = !1;
    }
    function F() {
      u.value = "", n.value = [];
    }
    return {
      loadChecked: _,
      clear: F,
      expandedKeys: l,
      autoExpandParent: m,
      checkedKeys: n,
      treeData: r,
      disabled: C,
      onSave: x,
      onExpand: A,
      onRefresh: D,
      clearChecked: K
    };
  }
});
const J = { class: "onl-auth-tree-btns" };
function O(e, o, c, u, p, m) {
  const l = g("a-empty"), n = g("a-button"), r = g("a-tree");
  return v(), b("div", null, [
    e.disabled ? (v(), E(l, {
      key: 0,
      description: "请先选中左侧角色/部门/用户"
    })) : e.treeData.length === 0 ? (v(), E(l, {
      key: 1,
      description: "无权限信息"
    })) : (v(), b(S, { key: 2 }, [
      P("div", J, [
        k(n, {
          onClick: e.onRefresh,
          size: "small",
          type: "primary",
          preIcon: "ant-design:redo",
          ghost: ""
        }, {
          default: I(() => o[1] || (o[1] = [
            B("刷新")
          ])),
          _: 1
        }, 8, ["onClick"]),
        k(n, {
          onClick: e.onSave,
          size: "small",
          type: "primary",
          preIcon: "ant-design:save",
          ghost: ""
        }, {
          default: I(() => o[2] || (o[2] = [
            B("保存")
          ])),
          _: 1
        }, 8, ["onClick"])
      ]),
      k(r, {
        checkable: "",
        checkedKeys: e.checkedKeys,
        "onUpdate:checkedKeys": o[0] || (o[0] = (d) => e.checkedKeys = d),
        expandedKeys: e.expandedKeys,
        autoExpandParent: e.autoExpandParent,
        treeData: e.treeData,
        onExpand: e.onExpand
      }, null, 8, ["checkedKeys", "expandedKeys", "autoExpandParent", "treeData", "onExpand"])
    ], 64))
  ]);
}
const ae = /* @__PURE__ */ j(q, [["render", O], ["__scopeId", "data-v-b62449eb"]]);
export {
  ae as default
};
