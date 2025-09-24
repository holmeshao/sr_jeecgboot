var c = (o, s, r) => new Promise((m, p) => {
  var d = (t) => {
    try {
      a(r.next(t));
    } catch (l) {
      p(l);
    }
  }, u = (t) => {
    try {
      a(r.throw(t));
    } catch (l) {
      p(l);
    }
  }, a = (t) => t.done ? m(t.value) : Promise.resolve(t.value).then(d, u);
  a((r = r.apply(o, s)).next());
});
import { defineComponent as z, ref as i, nextTick as T, watch as J, reactive as Q, markRaw as W, defineAsyncComponent as X, resolveComponent as F, openBlock as S, createBlock as k, mergeProps as Y, withCtx as f, createVNode as R, createTextVNode as P, resolveDynamicComponent as Z } from "vue";
import { BasicModal as x, useModalInner as oo } from "/@/components/Modal";
import { g as I } from "./useExtendComponent-bb98e568.mjs";
import { defHttp as eo } from "/@/utils/http/axios";
import to from "./OnlineForm-58282699.mjs";
import { importViewsFile as no } from "/@/utils";
import { _ as ro } from "./index-9e1e1e53.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "@ant-design/icons-vue";
import "/@/hooks/web/useMessage";
import "/@/components/Form/index";
import "lodash-es";
import "./constant-fa63bd66.mjs";
import "/@/utils/common/compUtils";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/utils/is";
import "/@/views/system/user/user.api";
import "/@/store/modules/user";
import "/@/utils/desform/customExpression";
import "/@/store/modules/permission";
import "/@/utils/dict/JDictSelectUtil";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "vue-router";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/utils/auth";
import "/@/api/common/api";
import "/@/hooks/web/useAppInject";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "/@/components/Loading";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
import "/@/components/jeecg/JVxeTable/types";
import "/@/hooks/core/useContext";
import "/@/utils/mitt";
import "./useCustomHook-acb00837.mjs";
import "/@/utils/cache";
import "./OnlineForm.vue_vue_type_style_index_0_scoped_3f26e7bd_lang-4ed993c7.mjs";
const io = {
  code: "",
  title: "自定义弹框",
  width: 600,
  row: {},
  hide: [],
  show: [],
  requestUrl: "",
  tableType: "",
  foreignKeys: "",
  formComponent: ""
}, mo = z({
  name: "OnlineCustomModal",
  components: {
    OnlineForm: to,
    BasicModal: x
  },
  setup(o, { emit: s }) {
    const r = i(), m = i(""), p = i("自定义弹框"), d = i(600);
    let u = [], a = [], t = "", l = {};
    const B = {
      loadFormItems: "/online/cgform/api/getFormItem/",
      optPre: "/online/cgform/api/form/"
    }, E = { position: "relative" }, $ = i(!1), h = i(!1);
    function N() {
      h.value = !0;
    }
    const w = i(!0), [D, { setModalProps: L, closeModal: b }] = oo((e) => c(this, null, function* () {
      L({ confirmLoading: !1 }), _(e), yield T(() => c(this, null, function* () {
        e.formComponent ? K(e.formComponent) : yield H();
      }));
    }));
    function _(e) {
      let n = Object.assign({}, io, e);
      m.value = n.code, p.value = n.title, d.value = n.width, u = n.hide || [], a = n.show || [], t = U(n.requestUrl), l = n.row;
    }
    function U(e) {
      return e || B.optPre + m.value;
    }
    const g = i(!1);
    function V() {
      g.value = !0, w.value === !0 ? r.value.handleSubmit() : M.value.handleSubmit(), setTimeout(() => {
        g.value = !0;
      }, 3500);
    }
    function j() {
      b();
    }
    function A(e) {
      s("success", e), b();
    }
    function H() {
      return c(this, null, function* () {
        w.value = !0, yield I(h), r.value.handleCustomFormSh(a, u), r.value.handleCustomFormEdit(l, t);
      });
    }
    const O = i(1);
    J(m, q, { immediate: !0 });
    function q() {
      return c(this, null, function* () {
        if (h.value = !1, !m.value)
          return;
        let e = yield G(), n = e.head.formTemplate;
        O.value = n ? Number(n) : 1, T(() => c(this, null, function* () {
          (yield I(r)).createRootProperties(e);
        }));
      });
    }
    function G() {
      let e = `/online/cgform/api/getFormItem/${m.value}`;
      return new Promise((n, v) => {
        eo.get({ url: e }, { isTransformResponse: !1 }).then((y) => {
          y.success ? n(y.result) : v(y.message);
        }).catch(() => {
          v();
        });
      });
    }
    const M = i(), C = Q({
      url: "",
      is: "",
      row: {}
    });
    function K(e) {
      w.value = !1, C.url = t, C.row = l, C.is = W(X(() => no(e)));
    }
    return {
      //modal
      registerModal: D,
      title: p,
      width: d,
      modalStyle: E,
      handleSubmit: V,
      handleCancel: j,
      // online表单
      id: m,
      onlineFormCompRef: r,
      formTemplate: O,
      renderSuccess: N,
      //自定义表单
      customFormRef: M,
      customFormComponent: C,
      //通用
      open,
      isOnlineForm: w,
      confirmLoading: $,
      submitLoading: g,
      handleSuccess: A
    };
  }
});
function lo(o, s, r, m, p, d) {
  const u = F("a-button"), a = F("online-form"), t = F("a-spin"), l = F("BasicModal");
  return S(), k(l, Y({
    title: o.title,
    width: o.width
  }, o.$attrs, {
    style: o.modalStyle,
    onRegister: o.registerModal,
    wrapClassName: "jeecg-online-modal2",
    onOk: o.handleSubmit
  }), {
    footer: f(() => [
      R(u, {
        key: "submit",
        type: "primary",
        onClick: o.handleSubmit
      }, {
        default: f(() => s[0] || (s[0] = [
          P("确定")
        ])),
        _: 1
      }, 8, ["onClick"]),
      R(u, {
        key: "back",
        onClick: o.handleCancel
      }, {
        default: f(() => s[1] || (s[1] = [
          P("关闭")
        ])),
        _: 1
      }, 8, ["onClick"])
    ]),
    default: f(() => [
      R(t, { spinning: o.confirmLoading }, {
        default: f(() => [
          o.isOnlineForm ? (S(), k(a, {
            key: 0,
            ref: "onlineFormCompRef",
            id: o.id,
            "form-template": o.formTemplate,
            onRendered: o.renderSuccess,
            onSuccess: o.handleSuccess,
            modalClass: "jeecg-online-modal2"
          }, null, 8, ["id", "form-template", "onRendered", "onSuccess"])) : (S(), k(Z(o.customFormComponent.is), {
            key: 1,
            ref: "customFormRef",
            url: o.customFormComponent.url,
            row: o.customFormComponent.row,
            onClose: o.handleSuccess
          }, null, 40, ["url", "row", "onClose"]))
        ]),
        _: 1
      }, 8, ["spinning"])
    ]),
    _: 1
  }, 16, ["title", "width", "style", "onRegister", "onOk"]);
}
const oe = /* @__PURE__ */ ro(mo, [["render", lo]]);
export {
  oe as default
};
