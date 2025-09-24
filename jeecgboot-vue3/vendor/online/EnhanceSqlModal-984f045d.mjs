var X = Object.defineProperty;
var E = Object.getOwnPropertySymbols;
var Y = Object.prototype.hasOwnProperty, Z = Object.prototype.propertyIsEnumerable;
var R = (e, o, n) => o in e ? X(e, o, { enumerable: !0, configurable: !0, writable: !0, value: n }) : e[o] = n, A = (e, o) => {
  for (var n in o || (o = {}))
    Y.call(o, n) && R(e, n, o[n]);
  if (E)
    for (var n of E(o))
      Z.call(o, n) && R(e, n, o[n]);
  return e;
};
var d = (e, o, n) => new Promise((_, p) => {
  var b = (i) => {
    try {
      c(n.next(i));
    } catch (u) {
      p(u);
    }
  }, s = (i) => {
    try {
      c(n.throw(i));
    } catch (u) {
      p(u);
    }
  }, c = (i) => i.done ? _(i.value) : Promise.resolve(i.value).then(b, s);
  c((n = n.apply(e, o)).next());
});
import { defineComponent as x, ref as v, reactive as ee, computed as oe, nextTick as ne, resolveComponent as r, openBlock as D, createBlock as q, withCtx as a, createVNode as l, createTextVNode as g, createElementBlock as te, createCommentVNode as L, normalizeProps as ae, guardReactiveProps as le } from "vue";
import { BasicModal as ie, useModalInner as re, useModal as se } from "/@/components/Modal";
import { BasicForm as ce, useForm as ue } from "/@/components/Form";
import { BasicTable as me, TableAction as de } from "/@/components/Table";
import { useListPage as pe } from "/@/hooks/system/useListPage";
import { u as fe } from "./useOnlineTest-e4bd8be3.mjs";
import { b as ge, c as _e } from "./enhance.data-6601ff44.mjs";
import { c as be, e as Ce, f as P } from "./enhance.api-138e6826.mjs";
import { _ as we } from "./index-9e1e1e53.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "/@/utils/http/axios";
import "/@/utils/is";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const ye = x({
  name: "EnhanceSqlModal",
  components: { BasicModal: ie, BasicTable: me, BasicForm: ce, TableAction: de },
  emits: ["register"],
  setup() {
    const e = v(""), o = v(""), n = v([]), { columns: _ } = _e(n), { doRequest: p, doDeleteRecord: b, tableContext: s } = pe({
      tableProps: {
        api: (t) => d(this, null, function* () {
          let { dataSource: y, btnList: J } = yield be(e.value, t);
          return n.value = J, y;
        }),
        columns: _,
        canResize: !1,
        useSearchForm: !1,
        beforeFetch(t) {
          return Object.assign(t, { column: "orderNum", order: "asc" });
        }
      }
    }), [c, { reload: i }, { rowSelection: u, selectedRowKeys: C }] = s, [M, { closeModal: S }] = re((t) => d(this, null, function* () {
      e.value = t.row.id, o.value = t.row.tableName, i();
    })), { aiTestMode: h, genEnhanceSqlData: B } = fe(), [w, m] = se(), f = v(!1), F = ee({
      onRegister: w,
      title: oe(() => f != null && f.value ? "修改" : "新增"),
      width: 800,
      confirmLoading: !1,
      onOk: H,
      onCancel: m.closeModal
    });
    let k = {};
    const { formSchemas: N } = ge(n), [$, { resetFields: O, setFieldsValue: V, validate: G }] = ue({
      schemas: N,
      showActionButtonGroup: !1,
      // update-begin--author:liaozhiyang---date:20231017---for：【issues/790】弹窗内文本框不居中问题
      labelWidth: 80,
      labelCol: null,
      wrapperCol: null
      // update-end--author:liaozhiyang---date:20231017---for：【issues/790】弹窗内文本框不居中问题
    });
    function I() {
      S();
    }
    function T(t) {
      return d(this, null, function* () {
        var y;
        f.value = t.isUpdate, k = A({}, (y = t.record) != null ? y : {}), m.openModal(), yield ne(), yield O(), V(k);
      });
    }
    function j() {
      T({ isUpdate: !1 });
    }
    function z(t) {
      T({ isUpdate: !0, record: t });
    }
    function K() {
      B(e.value, o.value);
    }
    function U() {
      return d(this, null, function* () {
        p(() => P(C.value));
      });
    }
    function H() {
      return d(this, null, function* () {
        try {
          F.confirmLoading = !0;
          let t = yield G();
          t = Object.assign({}, k, t), yield Ce(e.value, t, f.value), i(), m.closeModal();
        } finally {
          F.confirmLoading = !1;
        }
      });
    }
    function Q(t) {
      return [
        {
          label: "编辑",
          onClick: () => z(t)
        }
      ];
    }
    function W(t) {
      return [
        {
          label: "删除",
          popConfirm: {
            title: "确定删除吗？",
            placement: "left",
            confirm: () => b(() => P([t.id]))
          }
        }
      ];
    }
    return {
      rowSelection: u,
      selectedRowKeys: C,
      aiTestMode: h,
      onCancel: I,
      onAdd: j,
      onGenEnhanceSqlData: K,
      onBatchDelete: U,
      getTableAction: Q,
      getDropDownAction: W,
      formModalProps: F,
      registerModal: M,
      registerTable: c,
      registerForm: $
    };
  },
  computed: {
    tableScroll() {
      return {
        y: window.innerHeight - 320
      };
    }
  }
});
const ve = {
  key: 0,
  style: { float: "left" }
};
function Me(e, o, n, _, p, b) {
  const s = r("a-button"), c = r("a-icon"), i = r("a-menu-item"), u = r("a-menu"), C = r("a-dropdown"), M = r("TableAction"), S = r("BasicTable"), h = r("BasicForm"), B = r("a-spin"), w = r("BasicModal");
  return D(), q(w, {
    onRegister: e.registerModal,
    title: "SQL增强",
    width: 1200,
    defaultFullscreen: "",
    onCancel: e.onCancel
  }, {
    footer: a(() => [
      l(s, { onClick: e.onCancel }, {
        default: a(() => o[3] || (o[3] = [
          g("关闭")
        ])),
        _: 1
      }, 8, ["onClick"]),
      e.aiTestMode ? (D(), te("div", ve, [
        l(s, { onClick: e.onGenEnhanceSqlData }, {
          default: a(() => o[4] || (o[4] = [
            g("生成测试数据")
          ])),
          _: 1
        }, 8, ["onClick"])
      ])) : L("", !0)
    ]),
    default: a(() => [
      l(S, {
        onRegister: e.registerTable,
        rowSelection: e.rowSelection
      }, {
        tableTitle: a(() => [
          l(s, {
            onClick: e.onAdd,
            type: "primary",
            preIcon: "ant-design:plus"
          }, {
            default: a(() => o[0] || (o[0] = [
              g("新增")
            ])),
            _: 1
          }, 8, ["onClick"]),
          e.selectedRowKeys.length > 0 ? (D(), q(C, { key: 0 }, {
            overlay: a(() => [
              l(u, null, {
                default: a(() => [
                  l(i, {
                    key: "1",
                    onClick: e.onBatchDelete
                  }, {
                    default: a(() => [
                      l(c, { type: "delete" }),
                      o[1] || (o[1] = g(" 删除 "))
                    ]),
                    _: 1
                  }, 8, ["onClick"])
                ]),
                _: 1
              })
            ]),
            default: a(() => [
              l(s, { style: { "margin-left": "8px" } }, {
                default: a(() => [
                  o[2] || (o[2] = g(" 批量操作 ")),
                  l(c, { type: "down" })
                ]),
                _: 1
              })
            ]),
            _: 1
          })) : L("", !0)
        ]),
        action: a(({ record: m }) => [
          l(M, {
            actions: e.getTableAction(m),
            dropDownActions: e.getDropDownAction(m)
          }, null, 8, ["actions", "dropDownActions"])
        ]),
        _: 1
      }, 8, ["onRegister", "rowSelection"]),
      l(w, ae(le(e.formModalProps)), {
        default: a(() => [
          l(B, {
            spinning: e.formModalProps.confirmLoading
          }, {
            default: a(() => [
              l(h, {
                class: "popupForm",
                onRegister: e.registerForm
              }, null, 8, ["onRegister"])
            ]),
            _: 1
          }, 8, ["spinning"])
        ]),
        _: 1
      }, 16)
    ]),
    _: 1
  }, 8, ["onRegister", "onCancel"]);
}
const je = /* @__PURE__ */ we(ye, [["render", Me], ["__scopeId", "data-v-0a46f791"]]);
export {
  je as default
};
