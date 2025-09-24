var b = (r, u, t) => new Promise((f, s) => {
  var l = (n) => {
    try {
      i(t.next(n));
    } catch (c) {
      s(c);
    }
  }, g = (n) => {
    try {
      i(t.throw(n));
    } catch (c) {
      s(c);
    }
  }, i = (n) => n.done ? f(n.value) : Promise.resolve(n.value).then(l, g);
  i((t = t.apply(r, u)).next());
});
import { defineComponent as P, ref as E, computed as d, nextTick as I, resolveComponent as T, openBlock as m, createElementBlock as C, normalizeClass as j, normalizeStyle as F, unref as N, Fragment as V, createBlock as x, normalizeProps as w, mergeProps as h, withCtx as y, createVNode as U, createTextVNode as $, createCommentVNode as z } from "vue";
import { getRefPromise as H } from "/@/utils";
import { u as A } from "./shareStore-7de6c7a6.mjs";
import L from "./OnlineAutoModal-95f46901.mjs";
import { u as J, a as W } from "./useListButton-98908683.mjs";
import { a as q, u as G } from "./useExtendComponent-bb98e568.mjs";
import K from "./OnlineDetailModal-5b412bb9.mjs";
const ae = /* @__PURE__ */ P({
  __name: "SingleView",
  props: {
    ID: String,
    formName: String,
    buttonSwitch: Object,
    cgBIBtnMap: Object,
    confirmBtnCfg: Object,
    isUpdate: Boolean,
    isDetail: Boolean,
    isMobile: Boolean
  },
  emits: ["ok", "goEdit"],
  setup(r, { emit: u }) {
    const t = r, f = u, s = A(), l = E(), g = d(() => {
      let o = 20, e = 100;
      return window.innerWidth < 1200 && (o = 0, e = 0), {
        padding: `${o}px ${e}px`
      };
    }), i = d(() => {
      const o = t.isDetail ? "详情" : t.isUpdate ? "修改" : "新增", e = {
        id: t.ID,
        title: o + ` [${t.formName}]`,
        onRegister: M
      };
      return t.isDetail || Object.assign(e, {
        cgBIBtnMap: t.cgBIBtnMap,
        buttonSwitch: t.buttonSwitch,
        confirmBtnCfg: t.confirmBtnCfg,
        onSuccess: O,
        onFormConfig: S
      }), Object.assign(e, {
        cancelBtnCfg: { enabled: !1 },
        width: 1200,
        getContainer: () => l.value,
        mask: !1,
        maskClosable: !1,
        bodyStyle: { height: n.value },
        draggable: !1,
        canFullscreen: !1,
        closeFunc: R
      }), e;
    }), n = d(() => {
      var e, a;
      let o = 140;
      return t.isMobile && (o = 110), ((a = (e = l.value) == null ? void 0 : e.offsetHeight) != null ? a : window.innerHeight) - o + "px";
    }), {
      loading: c,
      handleFormConfig: S,
      onlineTableContext: p,
      onlineExtConfigJson: B
    } = J(), {
      registerModal: _,
      handleAdd: k,
      handleEdit: v
    } = W(p, B);
    function M(o, e) {
      return b(this, null, function* () {
        if (_(o, e), yield I(), t.isUpdate) {
          const a = yield D();
          v(a);
        } else
          k({});
      });
    }
    function D() {
      return b(this, null, function* () {
        return yield H(d(() => s.getDataRecord));
      });
    }
    q(p, B), G(p);
    function O(o) {
      let e;
      t.isUpdate ? e = s.getDataRecord.id : e = o.flow_submit_id, e && f("ok", e);
    }
    function R() {
      return alert("不允许关闭"), !1;
    }
    return (o, e) => {
      const a = T("a-button");
      return m(), C("div", {
        ref_key: "boxRef",
        ref: l,
        class: j(["view-box", { "is-mobile": r.isMobile }]),
        style: F(g.value)
      }, [
        N(c) ? z("", !0) : (m(), C(V, { key: 0 }, [
          r.isDetail ? (m(), x(K, w(h({ key: 0 }, i.value)), {
            footerBtn: y(() => [
              U(a, {
                onClick: e[0] || (e[0] = (Q) => f("goEdit"))
              }, {
                default: y(() => e[1] || (e[1] = [
                  $("编辑该数据")
                ])),
                _: 1
              })
            ]),
            _: 1
          }, 16)) : (m(), x(L, w(h({ key: 1 }, i.value)), null, 16))
        ], 64))
      ], 6);
    };
  }
});
export {
  ae as _
};
