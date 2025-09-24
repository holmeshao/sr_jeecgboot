var R = Object.defineProperty;
var F = Object.getOwnPropertySymbols;
var q = Object.prototype.hasOwnProperty, I = Object.prototype.propertyIsEnumerable;
var w = (o, a, t) => a in o ? R(o, a, { enumerable: !0, configurable: !0, writable: !0, value: t }) : o[a] = t, k = (o, a) => {
  for (var t in a || (a = {}))
    q.call(a, t) && w(o, t, a[t]);
  if (F)
    for (var t of F(a))
      I.call(a, t) && w(o, t, a[t]);
  return o;
};
var p = (o, a, t) => new Promise((i, r) => {
  var c = (n) => {
    try {
      d(t.next(n));
    } catch (f) {
      r(f);
    }
  }, l = (n) => {
    try {
      d(t.throw(n));
    } catch (f) {
      r(f);
    }
  }, d = (n) => n.done ? i(n.value) : Promise.resolve(n.value).then(c, l);
  d((t = t.apply(o, a)).next());
});
import { BasicModal as P, useModalInner as H } from "/@/components/Modal";
import { ref as b, resolveComponent as h, openBlock as J, createBlock as K, mergeProps as N, withCtx as y, createVNode as C } from "vue";
import { BasicForm as $, useForm as j } from "/@/components/Form/index";
import { defHttp as G } from "/@/utils/http/axios";
import { omit as z } from "lodash-es";
import { useMessage as D } from "/@/hooks/web/useMessage";
import { _ as E } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const Q = {
  name: "LinkTableFieldConfigModal",
  emits: ["success", "register"],
  components: {
    BasicModal: P,
    BasicForm: $
  },
  setup(o, { emit: a }) {
    const t = b(!1), i = b([]), r = b([]);
    let c = {}, l = {};
    const { createMessage: d } = D(), [n, { closeModal: f }] = H((e) => p(this, null, function* () {
      l = k({}, e.record), c = e.tableAndFieldsMap, yield M(), yield T({ dictTable: e.record.dictTable }), l.dictTable && _(l.dictTable), setTimeout(() => p(this, null, function* () {
        let s = z(e.record, "dictTable");
        yield T(s), yield O();
      }), 200);
    }));
    function M() {
      return p(this, null, function* () {
        let e = Object.keys(c);
        if (!e || e.length == 0)
          i.value = [];
        else {
          let s = [];
          for (let m of e)
            s.push({
              text: c[m].title,
              value: m
            });
          i.value = s;
        }
      });
    }
    function _(e) {
      return p(this, null, function* () {
        if (e) {
          const { table: s, fields: m } = c[e];
          if (!s) {
            d.warning("请先完善字段[" + e + "]关联记录的配置");
            return;
          }
          const V = "/online/cgform/field/listByHeadCode", g = yield G.get({ url: V, params: { headCode: s } });
          if (g && g.length > 0) {
            let L = g.map((u) => ({
              text: u.dbFieldTxt,
              value: u.dbFieldName
            })), A = m.split(",");
            r.value = L.filter((u) => A.includes(u.value));
          } else
            r.value = [];
        }
      });
    }
    const x = [
      {
        label: "rowKey",
        field: "rowKey",
        component: "Input",
        show: !1
      },
      {
        label: "字段描述",
        field: "dbFieldTxt",
        component: "Input",
        required: !0
      },
      {
        label: "关联记录",
        field: "dictTable",
        component: "JSearchSelect",
        required: !0,
        componentProps: ({ formActionType: e }) => ({
          async: !1,
          popContainer: ".link-table-field-config-modal",
          dictOptions: i.value,
          immediateChange: !0,
          onChange: (m) => p(this, null, function* () {
            l.dictText && (yield e.setFieldsValue({
              dictText: ""
            }), yield e.clearValidate()), _(m);
          })
        })
      },
      {
        label: "显示字段",
        field: "dictText",
        component: "JSearchSelect",
        required: !0,
        componentProps: {
          async: !1,
          popContainer: ".link-table-field-config-modal",
          dictOptions: r,
          onChange: (e) => {
            l.dictText = e;
          }
        }
      }
    ], [v, { validate: B, setFieldsValue: T, clearValidate: O }] = j({
      schemas: x,
      showActionButtonGroup: !1,
      labelAlign: "right"
    });
    function S() {
      return p(this, null, function* () {
        const e = yield B();
        a("success", e), f();
      });
    }
    return {
      registerModal: n,
      spinningLoading: t,
      registerForm: v,
      handleSubmit: S
    };
  }
};
function U(o, a, t, i, r, c) {
  const l = h("BasicForm"), d = h("a-spin"), n = h("BasicModal");
  return J(), K(n, N({ wrapClassName: "link-table-field-config-modal" }, o.$attrs, {
    title: "他表字段配置",
    onRegister: i.registerModal,
    keyboard: "",
    canFullscreen: !1,
    cancelText: "关闭",
    onOk: i.handleSubmit
  }), {
    default: y(() => [
      C(d, { spinning: i.spinningLoading }, {
        default: y(() => [
          C(l, { onRegister: i.registerForm }, null, 8, ["onRegister"])
        ]),
        _: 1
      }, 8, ["spinning"])
    ]),
    _: 1
  }, 16, ["onRegister", "onOk"]);
}
const le = /* @__PURE__ */ E(Q, [["render", U]]);
export {
  le as default
};
