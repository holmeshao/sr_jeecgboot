var F = Object.defineProperty, O = Object.defineProperties;
var N = Object.getOwnPropertyDescriptors;
var M = Object.getOwnPropertySymbols;
var $ = Object.prototype.hasOwnProperty, V = Object.prototype.propertyIsEnumerable;
var B = (e, t, o) => t in e ? F(e, t, { enumerable: !0, configurable: !0, writable: !0, value: o }) : e[t] = o, D = (e, t) => {
  for (var o in t || (t = {}))
    $.call(t, o) && B(e, o, t[o]);
  if (M)
    for (var o of M(t))
      V.call(t, o) && B(e, o, t[o]);
  return e;
}, I = (e, t) => O(e, N(t));
var T = (e, t, o) => new Promise((p, l) => {
  var n = (f) => {
    try {
      c(o.next(f));
    } catch (v) {
      l(v);
    }
  }, i = (f) => {
    try {
      c(o.throw(f));
    } catch (v) {
      l(v);
    }
  }, c = (f) => f.done ? p(f.value) : Promise.resolve(f.value).then(n, i);
  c((o = o.apply(e, t)).next());
});
import P from "./LeftRole-b0e0b496.mjs";
import j from "./LeftDepart-52cb6743.mjs";
import q from "./LeftUser-dd4b10e2.mjs";
import z from "./AuthFieldTree-5cc0da05.mjs";
import G from "./AuthButtonTree-b0bd6c40.mjs";
import H from "./AuthDataTree-f14a98d9.mjs";
import { defineComponent as J, ref as d, computed as Q, nextTick as L, resolveComponent as s, openBlock as b, createBlock as w, withCtx as u, createVNode as a, createElementBlock as W, Fragment as X, createCommentVNode as K } from "vue";
import { BasicModal as Y, useModalInner as Z } from "/@/components/Modal";
import { _ as x } from "./index-9e1e1e53.mjs";
import "/@/utils/http/axios";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "/@/api/common/api";
import "/@/hooks/web/useMessage";
import "./auth.api-53df4c33.mjs";
import "@ant-design/icons-vue";
import "./auth.data-626c5083.mjs";
import "/@/utils/index";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const ee = J({
  name: "AuthSetterModal",
  components: {
    BasicModal: Y,
    LeftRole: P,
    LeftDepart: j,
    LeftUser: q,
    AuthFieldTree: z,
    AuthButtonTree: G,
    AuthDataTree: H
  },
  props: {
    // 1单表 2主表 3附表
    tableType: { type: Number, default: 1 }
  },
  setup(e) {
    const t = d(""), o = d(!1), p = d("field"), l = d("role"), n = {
      fieldRef: d(),
      buttonRef: d(),
      dataRef: d(),
      roleRef: d(),
      userRef: d(),
      departRef: d()
    }, i = d(""), c = Q(() => e.tableType == 1 || e.tableType == 2), f = d(!0), [v, { closeModal: R }] = Z((r) => {
      p.value = "field", l.value = "role", t.value = r.cgformId, y();
    });
    function h(r = p.value) {
      var k;
      return (k = n[r + "Ref"]) == null ? void 0 : k.value;
    }
    function y() {
      return T(this, null, function* () {
        yield L(), g(), h().clear();
      });
    }
    function S(r) {
      i.value = r, m(p.value), _();
    }
    function C(r) {
      i.value = r, m(p.value), _();
    }
    function A(r) {
      i.value = r, m(p.value), _();
    }
    function _() {
      l.value == "role" ? (n.departRef.value.clearSelected(), n.userRef.value.clearSelected()) : l.value == "depart" ? (n.roleRef.value.clearSelected(), n.userRef.value.clearSelected()) : l.value == "user" && (n.departRef.value.clearSelected(), n.roleRef.value.clearSelected());
    }
    function g() {
      l.value == "role" ? n.roleRef.value.clearSelected() : l.value == "depart" ? n.departRef.value.clearSelected() : l.value == "user" && n.userRef.value.clearSelected(), h().clearChecked(), i.value = "";
    }
    function m(r) {
      return T(this, null, function* () {
        yield L(), i.value && h(r).loadChecked(i.value, l.value);
      });
    }
    function E() {
      g();
    }
    const U = (r) => {
      f.value = r;
    };
    return I(D({}, n), {
      cgformId: t,
      loading: o,
      activeKey: p,
      hasDataAuth: c,
      authMode: l,
      onAuthModeChange: E,
      onAuthTypeChange: m,
      closeModal: R,
      onSelectRole: S,
      onSelectDepart: C,
      onSelectUser: A,
      registerModal: v,
      hanldeOpenChange: U,
      contentShow: f
    });
  }
});
function te(e, t, o, p, l, n) {
  const i = s("LeftRole"), c = s("a-tab-pane"), f = s("LeftDepart"), v = s("LeftUser"), R = s("a-tabs"), h = s("a-col"), y = s("AuthFieldTree"), S = s("AuthButtonTree"), C = s("AuthDataTree"), A = s("a-row"), _ = s("a-spin"), g = s("BasicModal");
  return b(), w(g, {
    title: "Online权限授权",
    width: 900,
    maskClosable: !1,
    defaultFullscreen: "",
    okButtonProps: { style: { display: "none" } },
    cancelText: "关闭",
    onCancel: e.closeModal,
    onRegister: e.registerModal,
    onOpenChange: e.hanldeOpenChange
  }, {
    default: u(() => [
      a(_, {
        wrapperClassName: "authsetting-container",
        spinning: e.loading
      }, {
        default: u(() => [
          e.contentShow ? (b(), w(A, { key: 0 }, {
            default: u(() => [
              a(h, { span: 12 }, {
                default: u(() => [
                  a(R, {
                    activeKey: e.authMode,
                    "onUpdate:activeKey": t[0] || (t[0] = (m) => e.authMode = m),
                    onChange: e.onAuthModeChange
                  }, {
                    default: u(() => [
                      a(c, {
                        tab: "角色授权",
                        key: "role",
                        forceRender: ""
                      }, {
                        default: u(() => [
                          a(i, {
                            ref: "roleRef",
                            onSelect: e.onSelectRole
                          }, null, 8, ["onSelect"])
                        ]),
                        _: 1
                      }),
                      a(c, {
                        tab: "部门授权",
                        key: "depart",
                        forceRender: ""
                      }, {
                        default: u(() => [
                          a(f, {
                            ref: "departRef",
                            onSelect: e.onSelectDepart
                          }, null, 8, ["onSelect"])
                        ]),
                        _: 1
                      }),
                      a(c, {
                        tab: "人员授权",
                        key: "user",
                        forceRender: ""
                      }, {
                        default: u(() => [
                          a(v, {
                            ref: "userRef",
                            onSelect: e.onSelectUser
                          }, null, 8, ["onSelect"])
                        ]),
                        _: 1
                      })
                    ]),
                    _: 1
                  }, 8, ["activeKey", "onChange"])
                ]),
                _: 1
              }),
              a(h, { span: 1 }),
              a(h, { span: 11 }, {
                default: u(() => [
                  a(R, {
                    activeKey: e.activeKey,
                    "onUpdate:activeKey": t[1] || (t[1] = (m) => e.activeKey = m),
                    onChange: e.onAuthTypeChange
                  }, {
                    default: u(() => [
                      a(c, {
                        tab: "字段权限",
                        key: "field",
                        forceRender: ""
                      }, {
                        default: u(() => [
                          a(y, {
                            class: "authFieldTree",
                            ref: "fieldRef",
                            cgformId: e.cgformId
                          }, null, 8, ["cgformId"])
                        ]),
                        _: 1
                      }),
                      e.hasDataAuth ? (b(), W(X, { key: 0 }, [
                        a(c, {
                          tab: "按钮权限",
                          key: "button",
                          forceRender: ""
                        }, {
                          default: u(() => [
                            a(S, {
                              class: "authButtonTree",
                              ref: "buttonRef",
                              cgformId: e.cgformId
                            }, null, 8, ["cgformId"])
                          ]),
                          _: 1
                        }),
                        a(c, {
                          tab: "数据权限",
                          key: "data",
                          forceRender: ""
                        }, {
                          default: u(() => [
                            a(C, {
                              class: "authDataTree",
                              ref: "dataRef",
                              cgformId: e.cgformId
                            }, null, 8, ["cgformId"])
                          ]),
                          _: 1
                        })
                      ], 64)) : K("", !0)
                    ]),
                    _: 1
                  }, 8, ["activeKey", "onChange"])
                ]),
                _: 1
              })
            ]),
            _: 1
          })) : K("", !0)
        ]),
        _: 1
      }, 8, ["spinning"])
    ]),
    _: 1
  }, 8, ["onCancel", "onRegister", "onOpenChange"]);
}
const Ae = /* @__PURE__ */ x(ee, [["render", te], ["__scopeId", "data-v-e2407ec1"]]);
export {
  Ae as default
};
