var I = Object.defineProperty;
var C = Object.getOwnPropertySymbols;
var J = Object.prototype.hasOwnProperty, q = Object.prototype.propertyIsEnumerable;
var S = (i, t, l) => t in i ? I(i, t, { enumerable: !0, configurable: !0, writable: !0, value: l }) : i[t] = l, T = (i, t) => {
  for (var l in t || (t = {}))
    J.call(t, l) && S(i, l, t[l]);
  if (C)
    for (var l of C(t))
      q.call(t, l) && S(i, l, t[l]);
  return i;
};
var u = (i, t, l) => new Promise((s, f) => {
  var n = (o) => {
    try {
      d(l.next(o));
    } catch (c) {
      f(c);
    }
  }, h = (o) => {
    try {
      d(l.throw(o));
    } catch (c) {
      f(c);
    }
  }, d = (o) => o.done ? s(o.value) : Promise.resolve(o.value).then(n, h);
  d((l = l.apply(i, t)).next());
});
import { BasicModal as A, useModalInner as G } from "/@/components/Modal";
import { ref as b, computed as H, resolveComponent as _, openBlock as K, createBlock as $, mergeProps as j, withCtx as k, createVNode as M } from "vue";
import { BasicForm as z, useForm as D } from "/@/components/Form/index";
import { defHttp as E } from "/@/utils/http/axios";
import { omit as Q } from "lodash-es";
import { useMessage as U } from "/@/hooks/web/useMessage";
import { _ as W } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const X = {
  name: "LinkTableConfigModal",
  emits: ["success", "register"],
  components: {
    BasicModal: A,
    BasicForm: z
  },
  setup(i, { emit: t }) {
    const l = b(!1), { createMessage: s } = U(), f = b("");
    let n = {};
    const [h, { closeModal: d }] = G((e) => u(this, null, function* () {
      n = T({}, e.record), yield w({ dictTable: e.record.dictTable }), setTimeout(() => u(this, null, function* () {
        let r = Q(e.record, "dictTable");
        yield w(r), yield L();
      }), 200), f.value = e.fieldName;
    })), o = b(""), c = b(""), F = b([]), v = b([]);
    function x(e) {
      return u(this, null, function* () {
        if (e) {
          const r = "/online/cgform/field/listByHeadCode", m = yield E.get({ url: r, params: { headCode: e } });
          if (m && m.length > 0) {
            let p = m.filter((a) => a.dbFieldName != "id" && a.dbIsPersist == 1 && a.isShowList == 1);
            p.length > 0 ? F.value = p.map((a) => ({
              text: a.dbFieldTxt,
              value: a.dbFieldName
            })) : F.value = [];
            let g = m.filter((a) => a.dbFieldName != "id" && a.fieldShowType == "image" && a.dbIsPersist == 1);
            g.length > 0 ? v.value = g.map((a) => ({
              text: a.dbFieldTxt,
              value: a.dbFieldName
            })) : v.value = [{ text: "无图片字段可以选择", value: "", key: "", disabled: !0 }];
          } else
            F.value = [], v.value = [{ text: "无图片字段可以选择", value: "", key: "", disabled: !0 }];
        }
      });
    }
    function V(e) {
      return u(this, null, function* () {
        o.value = "", c.value = "", yield x(e);
      });
    }
    const N = H(() => {
      let e = F.value, r = o.value, m = c.value;
      return e.filter((p) => p.value != r && p.value != m);
    }), B = [
      {
        label: "rowKey",
        field: "rowKey",
        component: "Input",
        show: !1
      },
      {
        label: "dictField",
        field: "dictField",
        component: "Input",
        defaultValue: "id",
        show: !1
      },
      {
        label: "字段描述",
        field: "dbFieldTxt",
        component: "Input",
        required: !0
      },
      {
        label: "关联表",
        field: "dictTable",
        component: "JSearchSelect",
        required: !0,
        componentProps: ({ formActionType: e }) => ({
          dict: "onl_cgform_head where copy_type = 0,table_txt,table_name",
          pageSize: 10,
          async: !0,
          immediateChange: !0,
          popContainer: ".link-table-config-modal",
          params: { order: "desc", column: "create_time" },
          onChange: (r) => u(this, null, function* () {
            (n.titleField || n.otherFields) && (yield e.setFieldsValue({
              titleField: "",
              otherFields: "",
              imageField: ""
            }), yield e.clearValidate()), yield V(r);
          })
        })
      },
      {
        label: "标题字段",
        field: "titleField",
        component: "JSearchSelect",
        required: !0,
        componentProps: {
          async: !1,
          popContainer: ".link-table-config-modal",
          dictOptions: F,
          immediateChange: !0,
          onChange: (e) => {
            o.value = e, n.titleField = e;
          }
        }
      },
      {
        label: "封面图片",
        field: "imageField",
        component: "JSearchSelect",
        componentProps: {
          async: !1,
          popContainer: ".link-table-config-modal",
          dictOptions: v,
          immediateChange: !0,
          onChange: (e) => {
            c.value = e, n.imageFieldName = e;
          }
        }
      },
      {
        label: "其他字段",
        field: "otherFields",
        component: "JSelectMultiple",
        // update-begin--author:liaozhiyang---date:20240320---for：【QQYUN-8574】关联记录最多选6个字段
        componentProps: ({ schema: e, tableAction: r, formActionType: m, formModel: p }) => ({
          popContainer: ".link-table-config-modal",
          options: N.value,
          onChange: (g) => {
            if (g.split(",").length > 6) {
              const a = g.split(",");
              a.pop();
              const y = a.join(",");
              setTimeout(() => {
                p.otherFields = y, n.otherFields = y;
              }, 0), s.warning("最多选择6个字段~");
            } else
              n.otherFields = g;
          }
        })
        // update-end--author:liaozhiyang---date:20240320---for：【QQYUN-8574】关联记录最多选6个字段
      },
      {
        label: "显示方式",
        field: "showType",
        component: "Select",
        defaultValue: "card",
        componentProps: {
          options: [
            { label: "卡片", value: "card" },
            { label: "下拉框", value: "select" }
          ]
        }
      },
      {
        label: "是否多选",
        field: "multiSelect",
        component: "RadioGroup",
        defaultValue: !1,
        componentProps: {
          options: [
            { label: "否", value: !1 },
            { label: "是", value: !0 }
          ]
        }
      },
      {
        label: "列表只读",
        field: "isListReadOnly",
        component: "RadioGroup",
        defaultValue: !1,
        componentProps: {
          options: [
            { label: "否", value: !1 },
            { label: "是", value: !0 }
          ]
        }
      }
    ], [P, { validate: O, setFieldsValue: w, clearValidate: L, resetFields: Z }] = D({
      schemas: B,
      showActionButtonGroup: !1,
      labelAlign: "right"
    });
    function R() {
      return u(this, null, function* () {
        let e = yield O();
        e.fieldName = f.value, t("success", e), d();
      });
    }
    return {
      registerModal: h,
      spinningLoading: l,
      registerForm: P,
      handleSubmit: R
    };
  }
};
function Y(i, t, l, s, f, n) {
  const h = _("BasicForm"), d = _("a-spin"), o = _("BasicModal");
  return K(), $(o, j({ wrapClassName: "link-table-config-modal" }, i.$attrs, {
    title: "关联记录配置",
    onRegister: s.registerModal,
    keyboard: "",
    canFullscreen: !1,
    cancelText: "关闭",
    onOk: s.handleSubmit
  }), {
    default: k(() => [
      M(d, { spinning: s.spinningLoading }, {
        default: k(() => [
          M(h, { onRegister: s.registerForm }, null, 8, ["onRegister"])
        ]),
        _: 1
      }, 8, ["spinning"])
    ]),
    _: 1
  }, 16, ["onRegister", "onOk"]);
}
const ce = /* @__PURE__ */ W(X, [["render", Y]]);
export {
  ce as default
};
