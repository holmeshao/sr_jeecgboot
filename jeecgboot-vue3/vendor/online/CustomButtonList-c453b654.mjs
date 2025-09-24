var Y = Object.defineProperty;
var I = Object.getOwnPropertySymbols;
var Z = Object.prototype.hasOwnProperty, x = Object.prototype.propertyIsEnumerable;
var E = (o, e, t) => e in o ? Y(o, e, { enumerable: !0, configurable: !0, writable: !0, value: t }) : o[e] = t, L = (o, e) => {
  for (var t in e || (e = {}))
    Z.call(e, t) && E(o, t, e[t]);
  if (I)
    for (var t of I(e))
      x.call(e, t) && E(o, t, e[t]);
  return o;
};
var B = (o, e, t) => new Promise((C, g) => {
  var M = (r) => {
    try {
      u(t.next(r));
    } catch (c) {
      g(c);
    }
  }, l = (r) => {
    try {
      u(t.throw(r));
    } catch (c) {
      g(c);
    }
  }, u = (r) => r.done ? C(r.value) : Promise.resolve(r.value).then(M, l);
  u((t = t.apply(o, e)).next());
});
import { defineComponent as ee, ref as F, reactive as oe, computed as ne, nextTick as te, resolveComponent as i, openBlock as A, createElementBlock as O, Fragment as se, createVNode as s, withCtx as a, createTextVNode as f, createCommentVNode as P, createBlock as ae, normalizeProps as re, guardReactiveProps as ie } from "vue";
import { u as le } from "./useOnlineTest-e4bd8be3.mjs";
import { useListPage as ue } from "/@/hooks/system/useListPage";
import { BasicTable as ce, TableAction as de } from "/@/components/Table";
import { BasicModal as me, useModalInner as pe, useModal as S } from "/@/components/Modal";
import { BasicForm as fe, useForm as ge } from "/@/components/Form";
import { _ as Be, l as Ce, c as Me, f as _e, s as we, d as $ } from "./BuiltInButtonList.vue_vue_type_script_setup_true_lang-07d0b7d0.mjs";
import { useMessage as be } from "/@/hooks/web/useMessage";
import { _ as ke } from "./index-9e1e1e53.mjs";
import "/@/utils/http/axios";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const ye = ee({
  name: "CustomButtonList",
  components: { BasicModal: me, BasicTable: ce, TableAction: de, BasicForm: fe, BuiltInButtonList: Be },
  emits: ["register"],
  setup() {
    const { createMessage: o } = be(), e = F(""), t = F(), { doRequest: C, doDeleteRecord: g, tableContext: M } = ue({
      tableProps: {
        api: (n) => Ce(e.value, n),
        columns: Me,
        canResize: !1,
        useSearchForm: !1,
        beforeFetch(n) {
          return Object.assign(n, { column: "orderNum", order: "asc" });
        }
      }
    }), [l, { reload: u }, { rowSelection: r, selectedRowKeys: c }] = M, [_, { closeModal: w }] = pe((n) => B(this, null, function* () {
      e.value = n.row.id, t.value = n.row, u();
    })), { aiTestMode: b, genButtons: k } = le(), [y, d] = S(), m = F(!1), p = oe({
      onRegister: y,
      title: ne(() => m != null && m.value ? "修改" : "新增"),
      width: 800,
      confirmLoading: !1,
      onOk: K,
      onCancel: d.closeModal
    });
    let v = {};
    const [h, { resetFields: N, setFieldsValue: V, validate: G }] = ge({
      // update-begin--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
      schemas: _e({ redoModalHeight: d.redoModalHeight }),
      // update-end--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
      showActionButtonGroup: !1
    });
    function T(n) {
      return B(this, null, function* () {
        var D;
        m.value = n.isUpdate, v = L({}, (D = n.record) != null ? D : {}), d.openModal(), yield te(), yield N(), V(v);
      });
    }
    function H() {
      T({ isUpdate: !1 });
    }
    function R(n) {
      T({ isUpdate: !0, record: n });
    }
    function U() {
      w();
    }
    function j() {
      k(e.value);
    }
    function z() {
      return B(this, null, function* () {
        C(() => $(c.value));
      });
    }
    function K() {
      return B(this, null, function* () {
        try {
          p.confirmLoading = !0;
          let n = yield G();
          n = Object.assign({ cgformHeadId: e.value }, v, n), yield we(n, m.value), u(), d.closeModal();
        } finally {
          p.confirmLoading = !1;
        }
      });
    }
    const [q, J] = S();
    function Q() {
      var n;
      if (((n = t.value) == null ? void 0 : n.tableType) == 3) {
        o.warn("附表不支持管理内置按钮，请选择对应主表");
        return;
      }
      J.openModal(!0, {});
    }
    function W(n) {
      return [
        {
          label: "编辑",
          onClick: () => R(n)
        }
      ];
    }
    function X(n) {
      return [
        {
          label: "删除",
          popConfirm: {
            title: "确定删除吗？",
            placement: "left",
            confirm: () => g(() => $([n.id]))
          }
        }
      ];
    }
    return {
      code: e,
      record: t,
      onAdd: H,
      onEdit: R,
      onBatchDelete: z,
      aiTestMode: b,
      onGenButtons: j,
      registerModal: _,
      registerTable: l,
      selectedRowKeys: c,
      rowSelection: r,
      onCancel: U,
      getTableAction: W,
      getDropDownAction: X,
      registerForm: h,
      formModalProps: p,
      registerBIButtonModal: q,
      onOpenBIButton: Q
    };
  }
}), ve = {
  key: 0,
  style: { float: "left" }
};
function Fe(o, e, t, C, g, M) {
  const l = i("a-button"), u = i("a-icon"), r = i("a-menu-item"), c = i("a-menu"), _ = i("a-dropdown"), w = i("TableAction"), b = i("BasicTable"), k = i("BasicForm"), y = i("a-spin"), d = i("BasicModal"), m = i("BuiltInButtonList");
  return A(), O(se, null, [
    s(d, {
      onRegister: o.registerModal,
      title: "自定义按钮",
      width: 1200,
      defaultFullscreen: "",
      onCancel: o.onCancel
    }, {
      footer: a(() => [
        s(l, { onClick: o.onCancel }, {
          default: a(() => e[0] || (e[0] = [
            f("关闭")
          ])),
          _: 1
        }, 8, ["onClick"]),
        o.aiTestMode ? (A(), O("div", ve, [
          s(l, { onClick: o.onGenButtons }, {
            default: a(() => e[1] || (e[1] = [
              f("生成测试数据")
            ])),
            _: 1
          }, 8, ["onClick"])
        ])) : P("", !0)
      ]),
      default: a(() => [
        s(b, {
          onRegister: o.registerTable,
          rowSelection: o.rowSelection
        }, {
          tableTitle: a(() => [
            s(l, {
              onClick: o.onAdd,
              type: "primary",
              preIcon: "ant-design:plus"
            }, {
              default: a(() => e[2] || (e[2] = [
                f("新增")
              ])),
              _: 1
            }, 8, ["onClick"]),
            s(l, {
              onClick: o.onOpenBIButton,
              preIcon: "ant-design:setting"
            }, {
              default: a(() => e[3] || (e[3] = [
                f("管理内置按钮")
              ])),
              _: 1
            }, 8, ["onClick"]),
            o.selectedRowKeys.length > 0 ? (A(), ae(_, { key: 0 }, {
              overlay: a(() => [
                s(c, null, {
                  default: a(() => [
                    s(r, {
                      key: "1",
                      onClick: o.onBatchDelete
                    }, {
                      default: a(() => [
                        s(u, { type: "delete" }),
                        e[4] || (e[4] = f(" 删除 "))
                      ]),
                      _: 1
                    }, 8, ["onClick"])
                  ]),
                  _: 1
                })
              ]),
              default: a(() => [
                s(l, { style: { "margin-left": "8px" } }, {
                  default: a(() => [
                    e[5] || (e[5] = f(" 批量操作 ")),
                    s(u, { type: "down" })
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })) : P("", !0)
          ]),
          action: a(({ record: p }) => [
            s(w, {
              actions: o.getTableAction(p),
              dropDownActions: o.getDropDownAction(p)
            }, null, 8, ["actions", "dropDownActions"])
          ]),
          _: 1
        }, 8, ["onRegister", "rowSelection"]),
        s(d, re(ie(o.formModalProps)), {
          default: a(() => [
            s(y, {
              spinning: o.formModalProps.confirmLoading
            }, {
              default: a(() => [
                s(k, { onRegister: o.registerForm }, null, 8, ["onRegister"])
              ]),
              _: 1
            }, 8, ["spinning"])
          ]),
          _: 1
        }, 16)
      ]),
      _: 1
    }, 8, ["onRegister", "onCancel"]),
    s(m, {
      onRegister: o.registerBIButtonModal,
      record: o.record
    }, null, 8, ["onRegister", "record"])
  ], 64);
}
const Ve = /* @__PURE__ */ ke(ye, [["render", Fe]]);
export {
  Ve as default
};
