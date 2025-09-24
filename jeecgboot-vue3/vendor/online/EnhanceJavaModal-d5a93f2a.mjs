var W = Object.defineProperty;
var A = Object.getOwnPropertySymbols;
var X = Object.prototype.hasOwnProperty, Y = Object.prototype.propertyIsEnumerable;
var E = (e, o, n) => o in e ? W(e, o, { enumerable: !0, configurable: !0, writable: !0, value: n }) : e[o] = n, R = (e, o) => {
  for (var n in o || (o = {}))
    X.call(o, n) && E(e, n, o[n]);
  if (A)
    for (var n of A(o))
      Y.call(o, n) && E(e, n, o[n]);
  return e;
};
var p = (e, o, n) => new Promise((b, f) => {
  var C = (r) => {
    try {
      l(n.next(r));
    } catch (u) {
      f(u);
    }
  }, c = (r) => {
    try {
      l(n.throw(r));
    } catch (u) {
      f(u);
    }
  }, l = (r) => r.done ? b(r.value) : Promise.resolve(r.value).then(C, c);
  l((n = n.apply(e, o)).next());
});
import { defineComponent as Z, ref as k, reactive as x, computed as ee, nextTick as oe, resolveComponent as s, openBlock as D, createBlock as J, withCtx as a, createVNode as i, createTextVNode as g, createElementBlock as ne, createCommentVNode as S, normalizeProps as te, guardReactiveProps as ae } from "vue";
import { BasicModal as ie, useModalInner as re, useModal as se } from "/@/components/Modal";
import { BasicForm as le, useForm as ce } from "/@/components/Form";
import { BasicTable as ue, TableAction as me } from "/@/components/Table";
import { useListPage as de } from "/@/hooks/system/useListPage";
import { u as pe } from "./useOnlineTest-e4bd8be3.mjs";
import { u as fe, a as ge } from "./enhance.data-6601ff44.mjs";
import { a as be, b as Ce, d as L } from "./enhance.api-138e6826.mjs";
import { _ as ve } from "./index-9e1e1e53.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "/@/utils/http/axios";
import "/@/utils/is";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const we = Z({
  name: "EnhanceJavaModal",
  components: { BasicModal: ie, BasicTable: ue, BasicForm: le, TableAction: me },
  emits: ["register"],
  setup() {
    const e = k(""), o = k([]), { columns: n } = ge(o), { doRequest: b, doDeleteRecord: f, tableContext: C } = de({
      tableProps: {
        api: (t) => p(this, null, function* () {
          let { dataSource: v, btnList: Q } = yield be(e.value, t);
          return o.value = Q, v;
        }),
        columns: n,
        canResize: !1,
        useSearchForm: !1,
        beforeFetch(t) {
          return Object.assign(t, { column: "orderNum", order: "asc" });
        }
      }
    }), [c, { reload: l }, { rowSelection: r, selectedRowKeys: u }] = C, [w, { closeModal: _ }] = re((t) => p(this, null, function* () {
      e.value = t.row.id, l();
    })), { aiTestMode: y, genEnhanceJavaData: M } = pe(), [h, d] = se(), m = k(!1), B = x({
      onRegister: h,
      title: ee(() => m != null && m.value ? "修改" : "新增"),
      width: 800,
      confirmLoading: !1,
      bodyStyle: { height: "350px" },
      onOk: U,
      onCancel: d.closeModal
    });
    let F = {};
    const { formSchemas: P } = fe(o), [V, { resetFields: $, setFieldsValue: N, validate: O }] = ce({
      schemas: P,
      showActionButtonGroup: !1,
      // update-begin--author:liaozhiyang---date:20231017---for：【issues/790】弹窗内文本框不居中问题
      labelCol: { xs: 24, sm: 5 },
      wrapperCol: { xs: 24, sm: 16 }
      // update-end--author:liaozhiyang---date:20231017---for：【issues/790】弹窗内文本框不居中问题
    });
    function G() {
      _();
    }
    function T(t) {
      return p(this, null, function* () {
        var v;
        m.value = t.isUpdate, F = R({}, (v = t.record) != null ? v : {}), d.openModal(), yield oe(), yield $(), N(F);
      });
    }
    function j() {
      T({ isUpdate: !1 });
    }
    function z(t) {
      T({ isUpdate: !0, record: t });
    }
    function I() {
      M(e.value);
    }
    function K() {
      return p(this, null, function* () {
        b(() => L(u.value));
      });
    }
    function U() {
      return p(this, null, function* () {
        try {
          B.confirmLoading = !0;
          let t = yield O();
          t = Object.assign({}, F, t), yield Ce(e.value, t, m.value), l(), d.closeModal();
        } finally {
          B.confirmLoading = !1;
        }
      });
    }
    function q(t) {
      return [
        {
          label: "编辑",
          onClick: () => z(t)
        }
      ];
    }
    function H(t) {
      return [
        {
          label: "删除",
          popConfirm: {
            title: "确定删除吗？",
            placement: "left",
            confirm: () => f(() => L([t.id]))
          }
        }
      ];
    }
    return {
      rowSelection: r,
      selectedRowKeys: u,
      aiTestMode: y,
      onCancel: G,
      onAdd: j,
      onGenEnhanceJavaData: I,
      onBatchDelete: K,
      getTableAction: q,
      getDropDownAction: H,
      formModalProps: B,
      registerModal: w,
      registerTable: c,
      registerForm: V
    };
  },
  computed: {
    tableScroll() {
      return {
        y: window.innerHeight - 320
      };
    }
  }
}), _e = {
  key: 0,
  style: { float: "left" }
};
function ye(e, o, n, b, f, C) {
  const c = s("a-button"), l = s("a-icon"), r = s("a-menu-item"), u = s("a-menu"), w = s("a-dropdown"), _ = s("TableAction"), y = s("BasicTable"), M = s("BasicForm"), h = s("a-spin"), d = s("BasicModal");
  return D(), J(d, {
    onRegister: e.registerModal,
    title: "JAVA增强",
    width: 1200,
    defaultFullscreen: "",
    onCancel: e.onCancel
  }, {
    footer: a(() => [
      i(c, { onClick: e.onCancel }, {
        default: a(() => o[3] || (o[3] = [
          g("关闭")
        ])),
        _: 1
      }, 8, ["onClick"]),
      e.aiTestMode ? (D(), ne("div", _e, [
        i(c, { onClick: e.onGenEnhanceJavaData }, {
          default: a(() => o[4] || (o[4] = [
            g("生成测试数据")
          ])),
          _: 1
        }, 8, ["onClick"])
      ])) : S("", !0)
    ]),
    default: a(() => [
      i(y, {
        onRegister: e.registerTable,
        rowSelection: e.rowSelection
      }, {
        tableTitle: a(() => [
          i(c, {
            onClick: e.onAdd,
            type: "primary",
            preIcon: "ant-design:plus"
          }, {
            default: a(() => o[0] || (o[0] = [
              g("新增")
            ])),
            _: 1
          }, 8, ["onClick"]),
          e.selectedRowKeys.length > 0 ? (D(), J(w, { key: 0 }, {
            overlay: a(() => [
              i(u, null, {
                default: a(() => [
                  i(r, {
                    key: "1",
                    onClick: e.onBatchDelete
                  }, {
                    default: a(() => [
                      i(l, { type: "delete" }),
                      o[1] || (o[1] = g(" 删除 "))
                    ]),
                    _: 1
                  }, 8, ["onClick"])
                ]),
                _: 1
              })
            ]),
            default: a(() => [
              i(c, { style: { "margin-left": "8px" } }, {
                default: a(() => [
                  o[2] || (o[2] = g(" 批量操作 ")),
                  i(l, { type: "down" })
                ]),
                _: 1
              })
            ]),
            _: 1
          })) : S("", !0)
        ]),
        action: a(({ record: m }) => [
          i(_, {
            actions: e.getTableAction(m),
            dropDownActions: e.getDropDownAction(m)
          }, null, 8, ["actions", "dropDownActions"])
        ]),
        _: 1
      }, 8, ["onRegister", "rowSelection"]),
      i(d, te(ae(e.formModalProps)), {
        default: a(() => [
          i(h, {
            spinning: e.formModalProps.confirmLoading
          }, {
            default: a(() => [
              i(M, { onRegister: e.registerForm }, null, 8, ["onRegister"])
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
const je = /* @__PURE__ */ ve(we, [["render", ye]]);
export {
  je as default
};
