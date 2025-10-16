var m = (r, c, o) => new Promise((p, s) => {
  var f = (n) => {
    try {
      a(o.next(n));
    } catch (l) {
      s(l);
    }
  }, e = (n) => {
    try {
      a(o.throw(n));
    } catch (l) {
      s(l);
    }
  }, a = (n) => n.done ? p(n.value) : Promise.resolve(n.value).then(f, e);
  a((o = o.apply(r, c)).next());
});
import { defineComponent as B, nextTick as b, resolveComponent as h, openBlock as j, createBlock as x, withCtx as L, createVNode as P } from "vue";
import { BasicModal as R, useModalInner as U } from "/@/components/Modal";
import { BasicForm as V, useForm as v } from "/@/components/Form";
import { useMessage as O } from "/@/hooks/web/useMessage";
import { a as Q } from "./useSchemas-b074f3a1.mjs";
import { _ as E } from "./index-9e1e1e53.mjs";
import "ant-design-vue";
import "@ant-design/icons-vue";
import "/@/utils/common/compUtils";
import "/@/hooks/web/usePermission";
import "/@/utils/helper/validator";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const I = B({
  name: "CgformExtConfigModel",
  components: { BasicModal: R, BasicForm: V },
  props: {
    parentForm: {
      type: Object,
      required: !0
    }
  },
  emits: ["register", "ok"],
  setup(r, { emit: c }) {
    const { createMessage: o } = O(), { formSchemas: p } = Q(r, {
      onIsDesformChange: C,
      onJoinQueryChange: T,
      onReportPrintShowChange: y,
      onFormLabelLengthShow: M
    }), [s, { resetFields: f, setFieldsValue: e, getFieldsValue: a, clearValidate: n, validate: l }] = v({
      schemas: p,
      showActionButtonGroup: !1,
      labelAlign: "right"
    }), [w, { closeModal: d }] = U((t) => m(this, null, function* () {
      yield f(), yield e(t.extConfigJson);
    }));
    function _() {
      return m(this, null, function* () {
        yield n(), yield b();
        try {
          const t = yield l();
          c("ok", t), d();
        } catch (t) {
        }
      });
    }
    function F() {
      d();
    }
    function C(t) {
      if (t === "Y") {
        let { themeTemplate: i } = r.parentForm.getFieldsValue(["themeTemplate"]);
        i === "erp" && (r.parentForm.setFieldsValue({ themeTemplate: "normal" }), o.warning("请注意：erp风格不支持对接表单设计，已自动改为默认风格！"));
      } else
        n("desFormCode");
    }
    const u = "{{ window._CONFIG['domianURL'] }}/jmreport/view/{积木报表ID}";
    function y(t) {
      return m(this, null, function* () {
        let i = a().reportPrintUrl;
        t === 0 ? i === u && (yield e({ reportPrintUrl: "" })) : t === 1 && i === "" && (yield e({ reportPrintUrl: u })), n("reportPrintUrl");
      });
    }
    function M(t) {
      return m(this, null, function* () {
        t == 0 && (yield e({ formLabelLength: null })), yield n("formLabelLength");
      });
    }
    function T(t) {
      // 放开前端对联合查询的限制，交由后端根据配置自动处理
      // 不再强制回写 joinQuery=0，仅保留提示（可按需补充）
    }
    return {
      handleOk: _,
      handleCancel: F,
      registerModal: w,
      registerForm: s
    };
  }
});
function $(r, c, o, p, s, f) {
  const e = h("BasicForm"), a = h("BasicModal");
  return j(), x(a, {
    onRegister: r.registerModal,
    title: "表单扩展配置项",
    width: 1e3,
    onOk: r.handleOk,
    onCancel: r.handleCancel
  }, {
    default: L(() => [
      P(e, { onRegister: r.registerForm }, null, 8, ["onRegister"])
    ]),
    _: 1
  }, 8, ["onRegister", "onOk", "onCancel"]);
}
const ee = /* @__PURE__ */ E(I, [["render", $], ["__scopeId", "data-v-f3d939e5"]]);
export {
  ee as default
};
