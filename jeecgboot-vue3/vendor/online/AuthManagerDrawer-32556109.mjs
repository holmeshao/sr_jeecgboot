import { defineComponent as b, ref as n, computed as C, resolveComponent as a, openBlock as g, createBlock as A, withCtx as u, createVNode as o, createElementBlock as D, Fragment as B, createCommentVNode as F } from "vue";
import { BasicDrawer as I, useDrawerInner as _ } from "/@/components/Drawer";
import v from "./AuthFieldConfig-f1e224cc.mjs";
import w from "./AuthButtonConfig-d5bffca0.mjs";
import T from "./AuthDataConfig-d3b7afa4.mjs";
import { _ as k } from "./index-9e1e1e53.mjs";
import "/@/components/Table";
import "./auth.api-53df4c33.mjs";
import "/@/utils/http/axios";
import "./auth.data-626c5083.mjs";
import "/@/utils/index";
import "lodash-es";
import "/@/components/Modal";
import "/@/components/Form";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const K = b({
  name: "AuthManagerDrawer",
  components: {
    BasicDrawer: I,
    AuthFieldConfig: v,
    AuthButtonConfig: w,
    AuthDataConfig: T
  },
  props: {
    // 1单表 2主表 3附表
    tableType: {
      type: Number,
      default: 1
    }
  },
  emits: ["register"],
  setup(e) {
    const t = n(""), d = n(""), h = n([]), s = n("field"), l = n(1), p = C(() => e.tableType == 1 || e.tableType == 2), [r, { closeDrawer: m }] = _((i) => {
      t.value = i.cgformId, d.value = t.value + "?" + (/* @__PURE__ */ new Date()).getTime(), s.value = "field", l.value = i.tableType;
    });
    function f() {
      m();
    }
    return {
      activeKey: s,
      cgformId: t,
      headId: d,
      authFields: h,
      hasDataAuth: p,
      onClose: f,
      registerDrawer: r,
      curTableType: l
    };
  }
});
function R(e, t, d, h, s, l) {
  const p = a("AuthFieldConfig"), r = a("a-tab-pane"), m = a("AuthButtonConfig"), f = a("AuthDataConfig"), i = a("a-tabs"), y = a("BasicDrawer");
  return g(), A(y, {
    onRegister: e.registerDrawer,
    title: "权限管理",
    width: 800,
    onClose: e.onClose
  }, {
    default: u(() => [
      o(i, {
        activeKey: e.activeKey,
        "onUpdate:activeKey": t[1] || (t[1] = (c) => e.activeKey = c)
      }, {
        default: u(() => [
          o(r, {
            tab: "字段权限",
            key: "field",
            forceRender: ""
          }, {
            default: u(() => [
              o(p, {
                headId: e.headId,
                authFields: e.authFields,
                "onUpdate:authFields": t[0] || (t[0] = (c) => e.authFields = c)
              }, null, 8, ["headId", "authFields"])
            ]),
            _: 1
          }),
          e.hasDataAuth ? (g(), D(B, { key: 0 }, [
            o(r, {
              tab: "按钮权限",
              key: "button",
              forceRender: ""
            }, {
              default: u(() => [
                o(m, {
                  headId: e.headId,
                  tableType: e.curTableType
                }, null, 8, ["headId", "tableType"])
              ]),
              _: 1
            }),
            o(r, {
              tab: "数据权限",
              key: "data",
              forceRender: ""
            }, {
              default: u(() => [
                o(f, {
                  cgformId: e.cgformId,
                  authFields: e.authFields
                }, null, 8, ["cgformId", "authFields"])
              ]),
              _: 1
            })
          ], 64)) : F("", !0)
        ]),
        _: 1
      }, 8, ["activeKey"])
    ]),
    _: 1
  }, 8, ["onRegister", "onClose"]);
}
const W = /* @__PURE__ */ k(K, [["render", R]]);
export {
  W as default
};
