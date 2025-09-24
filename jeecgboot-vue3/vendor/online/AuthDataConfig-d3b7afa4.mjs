var K = Object.defineProperty, Q = Object.defineProperties;
var H = Object.getOwnPropertyDescriptors;
var F = Object.getOwnPropertySymbols;
var W = Object.prototype.hasOwnProperty, X = Object.prototype.propertyIsEnumerable;
var M = (t, n, o) => n in t ? K(t, n, { enumerable: !0, configurable: !0, writable: !0, value: o }) : t[n] = o, S = (t, n) => {
  for (var o in n || (n = {}))
    W.call(n, o) && M(t, o, n[o]);
  if (F)
    for (var o of F(n))
      X.call(n, o) && M(t, o, n[o]);
  return t;
}, k = (t, n) => Q(t, H(n));
var A = (t, n, o) => new Promise((c, f) => {
  var p = (a) => {
    try {
      i(o.next(a));
    } catch (l) {
      f(l);
    }
  }, g = (a) => {
    try {
      i(o.throw(a));
    } catch (l) {
      f(l);
    }
  }, i = (a) => a.done ? c(a.value) : Promise.resolve(a.value).then(p, g);
  i((o = o.apply(t, n)).next());
});
import { defineComponent as Y, ref as Z, watch as y, reactive as ee, nextTick as te, resolveComponent as r, openBlock as ne, createElementBlock as oe, createVNode as u, withCtx as d, createTextVNode as ae, mergeProps as ie } from "vue";
import { BasicTable as le, TableAction as se, useTable as re } from "/@/components/Table";
import { BasicModal as ue, useModal as ce } from "/@/components/Modal";
import { BasicForm as me, useForm as de } from "/@/components/Form";
import { i as fe, j as pe, k as ge, l as he } from "./auth.api-53df4c33.mjs";
import { d as Ce, u as be, U as v } from "./auth.data-626c5083.mjs";
import { _ as _e } from "./index-9e1e1e53.mjs";
import "/@/utils/http/axios";
import "/@/utils/index";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const we = Y({
  name: "AuthDataConfig",
  components: { BasicTable: le, TableAction: se, BasicModal: ue, BasicForm: me },
  props: {
    cgformId: { type: String, required: !0 },
    authFields: { type: Array, required: !0 }
  },
  setup(t) {
    const n = Z(!1), [o, { reload: c, setLoading: f }] = re({
      api: (e) => fe(t.cgformId, e),
      rowKey: "id",
      bordered: !0,
      columns: Ce,
      showIndexColumn: !1,
      // 操作列
      actionColumn: {
        width: 120,
        title: "操作",
        fixed: !1,
        dataIndex: "action",
        slots: { customRender: "action" }
      }
    });
    y(n, (e) => f(e));
    const [p, { openModal: g, closeModal: i }] = ce(), a = ee({
      title: "",
      width: 800,
      confirmLoading: !1,
      onOk: E,
      onCancel: i,
      onRegister: p
    });
    let l = !1, h = {}, m = !1;
    const { formSchemas: _ } = be(t, {
      onRuleOperatorChange: P,
      onRuleColumnChange: $,
      onRuleNameChange: j
    }), [C, { validate: b, resetFields: I, setFieldsValue: w, getFieldsValue: R, clearValidate: O, updateSchema: D }] = de({
      schemas: _,
      showActionButtonGroup: !1,
      labelAlign: "right"
    });
    y(
      () => t.cgformId,
      () => {
        c().catch(() => null);
      },
      { immediate: !0 }
    );
    function T(e) {
      return A(this, null, function* () {
        var s;
        l = (s = e.isUpdate) != null ? s : !1, a.title = e.title, g(), yield te(), yield I(), h = Object.assign({}, e.record), yield w(h);
      });
    }
    function U() {
      T({ title: "新增" });
    }
    function V(e) {
      T({ title: "编辑", record: e, isUpdate: !0 });
    }
    function L(e) {
      n.value = !0, he(e).then(c).finally(() => n.value = !1);
    }
    function E() {
      return A(this, null, function* () {
        try {
          a.confirmLoading = !0;
          let e = yield b();
          e = Object.assign({}, h, e), e.ruleOperator == v && (e.ruleColumn = ""), e.cgformId = t.cgformId, yield pe(e, l), c(), i();
        } finally {
          a.confirmLoading = !1;
        }
      });
    }
    function N(e) {
      n.value = !0;
      let s = Math.abs(e.status - 1);
      ge(k(S({}, e), { status: s })).then(() => {
        e.status = s;
      }).finally(() => {
        n.value = !1;
      });
    }
    function P(e) {
      e == v ? (w({
        ruleColumn: "",
        ruleValue: ""
      }), D({
        field: "ruleValue",
        component: "InputTextArea"
      }), O(["ruleValue"])) : D({
        field: "ruleValue",
        component: "JInputSelect"
      });
    }
    function $(e) {
      const s = R();
      if (!s.ruleName || s.ruleName && !m) {
        const B = t.authFields.find((J) => J.value === e), G = B ? B.text : e;
        w({
          ruleName: G
        });
      }
    }
    function j(e) {
      e.target.value.length ? m = !0 : m = !1;
    }
    function x(e) {
      e && (m = !1);
    }
    function q(e) {
      return [
        {
          label: "编辑",
          onClick: () => V(e)
        }
      ];
    }
    function z(e) {
      return [
        {
          label: "删除",
          popConfirm: {
            title: "确定删除吗？",
            placement: "left",
            confirm: () => L(e.id)
          }
        }
      ];
    }
    return {
      loading: n,
      formModalProps: a,
      onAdd: U,
      onUpdateStatus: N,
      getTableAction: q,
      getDropDownAction: z,
      registerTable: o,
      registerModal: p,
      registerForm: C,
      handleOpenChange: x
    };
  }
});
function Ae(t, n, o, c, f, p) {
  const g = r("a-button"), i = r("a-switch"), a = r("TableAction"), l = r("BasicTable"), h = r("BasicForm"), m = r("a-spin"), _ = r("BasicModal");
  return ne(), oe("div", null, [
    u(l, {
      onRegister: t.registerTable,
      loading: t.loading
    }, {
      tableTitle: d(() => [
        u(g, {
          onClick: t.onAdd,
          type: "primary",
          preIcon: "ant-design:plus"
        }, {
          default: d(() => n[0] || (n[0] = [
            ae("新增")
          ])),
          _: 1
        }, 8, ["onClick"])
      ]),
      switch: d(({ text: C, record: b }) => [
        u(i, {
          size: "small",
          checked: b.status === 1,
          onClick: () => t.onUpdateStatus(b)
        }, null, 8, ["checked", "onClick"])
      ]),
      action: d(({ record: C }) => [
        u(a, {
          actions: t.getTableAction(C),
          dropDownActions: t.getDropDownAction(C)
        }, null, 8, ["actions", "dropDownActions"])
      ]),
      _: 1
    }, 8, ["onRegister", "loading"]),
    u(_, ie(t.formModalProps, { onOpenChange: t.handleOpenChange }), {
      default: d(() => [
        u(m, {
          spinning: t.formModalProps.confirmLoading
        }, {
          default: d(() => [
            u(h, { onRegister: t.registerForm }, null, 8, ["onRegister"])
          ]),
          _: 1
        }, 8, ["spinning"])
      ]),
      _: 1
    }, 16, ["onOpenChange"])
  ]);
}
const Ve = /* @__PURE__ */ _e(we, [["render", Ae]]);
export {
  Ve as default
};
