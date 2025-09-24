var k = (e, p, o) => new Promise((d, s) => {
  var f = (t) => {
    try {
      r(o.next(t));
    } catch (i) {
      s(i);
    }
  }, a = (t) => {
    try {
      r(o.throw(t));
    } catch (i) {
      s(i);
    }
  }, r = (t) => t.done ? d(t.value) : Promise.resolve(t.value).then(f, a);
  r((o = o.apply(e, p)).next());
});
import { defineComponent as q, ref as N, watch as z, resolveComponent as l, openBlock as u, createBlock as b, mergeProps as y, withCtx as m, createVNode as C, createElementBlock as G, Fragment as J, renderList as K, createTextVNode as Q, toDisplayString as B, createElementVNode as O, createCommentVNode as M } from "vue";
import { BasicModal as X } from "/@/components/Modal";
import Y from "./OnlineForm-58282699.mjs";
import { b as Z } from "./useExtendComponent-bb98e568.mjs";
import _ from "/@/components/jeecg/comment/CommentPanel.vue";
import { a as x } from "./constant-fa63bd66.mjs";
import { _ as ee } from "./index-9e1e1e53.mjs";
import "/@/hooks/web/useMessage";
import "/@/components/Form/index";
import "/@/utils/http/axios";
import "lodash-es";
import "/@/utils";
import "/@/components/Loading";
import "/@/components/jeecg/JVxeTable/types";
import "/@/utils/auth";
import "@ant-design/icons-vue";
import "/@/hooks/core/useContext";
import "/@/utils/mitt";
import "./useCustomHook-acb00837.mjs";
import "/@/utils/cache";
import "/@/utils/common/compUtils";
import "/@/store/modules/user";
import "/@/hooks/web/useAppInject";
import "/@/utils/is";
import "/@/store/modules/permission";
import "./OnlineForm.vue_vue_type_style_index_0_scoped_3f26e7bd_lang-4ed993c7.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/views/system/user/user.api";
import "/@/utils/desform/customExpression";
import "/@/utils/dict/JDictSelectUtil";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "vue-router";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/api/common/api";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
const te = q({
  name: "OnlineAutoModal",
  props: {
    id: {
      type: String,
      default: ""
    },
    // 为了区分来源，编码时主要是erp子表有特殊处理
    source: {
      type: String,
      default: ""
    },
    buttonSwitch: Object,
    cgBIBtnMap: Object,
    confirmBtnCfg: {
      type: Object,
      default: () => ({
        enabled: !0,
        buttonName: "确定",
        buttonIcon: ""
      })
    },
    cancelBtnCfg: {
      type: Object,
      default: () => ({
        enabled: !0,
        buttonName: "关闭",
        buttonIcon: ""
      })
    }
  },
  components: {
    BasicModal: X,
    OnlineForm: Y,
    CommentPanel: _
  },
  emits: ["success", "register", "formConfig"],
  setup(e, { emit: p }) {
    const o = N(), d = N(0);
    function s() {
      o.value && o.value.reload();
    }
    const {
      title: f,
      modalWidth: a,
      registerModal: r,
      closeModal: t,
      cgButtonList: i,
      handleCgButtonClick: g,
      disableSubmit: h,
      handleSubmit: n,
      submitLoading: I,
      handleCancel: T,
      handleFormConfig: w,
      onlineFormCompRef: F,
      formTemplate: R,
      isTreeForm: v,
      pidFieldName: $,
      renderSuccess: E,
      formRendered: L,
      tableName: j,
      tableId: P,
      formDataId: A,
      enableComment: V,
      onCloseEvent: S,
      themeTemplate: D
    } = Z(!1, { emit: p }, s);
    function W(c) {
      p("success", c), t(), S();
    }
    z(() => e.id, H, { immediate: !0 });
    function H() {
      return k(this, null, function* () {
        if (L.value = !1, !e.id)
          return;
        const c = {};
        e.source === x && (c.tabletype = 3), yield w(e.id, c);
      });
    }
    return {
      title: f,
      onlineFormCompRef: F,
      renderSuccess: E,
      registerModal: r,
      handleSubmit: n,
      handleSuccess: W,
      handleCancel: T,
      modalWidth: a,
      formTemplate: R,
      disableSubmit: h,
      cgButtonList: i,
      handleCgButtonClick: g,
      isTreeForm: v,
      pidFieldName: $,
      submitLoading: I,
      tableName: j,
      tableId: P,
      formDataId: A,
      enableComment: V,
      commentPanelRef: o,
      onCloseEvent: S,
      themeTemplate: D,
      handleCommentOpen: (c, U) => {
        d.value = U;
      },
      commentSpan: d
    };
  }
});
function oe(e, p, o, d, s, f) {
  const a = l("a-button"), r = l("a-col"), t = l("a-row"), i = l("online-form"), g = l("comment-panel"), h = l("BasicModal");
  return u(), b(h, y({
    title: e.title,
    onCancel: e.onCloseEvent,
    enableComment: e.enableComment,
    width: e.modalWidth
  }, e.$attrs, {
    maxHeight: 600,
    onRegister: e.registerModal,
    wrapClassName: "jeecg-online-modal",
    onOk: e.handleSubmit,
    onCommentOpen: e.handleCommentOpen
  }), {
    footer: m(() => [
      C(t, null, {
        default: m(() => [
          C(r, {
            span: 24 - e.commentSpan
          }, {
            default: m(() => [
              (u(!0), G(J, null, K(e.cgButtonList, (n) => (u(), b(a, {
                key: n.id,
                type: "primary",
                onClick: (I) => e.handleCgButtonClick(n.optType, n.buttonCode),
                preIcon: n.buttonIcon ? "ant-design:" + n.buttonIcon : ""
              }, {
                default: m(() => [
                  Q(B(n.buttonName), 1)
                ]),
                _: 2
              }, 1032, ["onClick", "preIcon"]))), 128)),
              !e.disableSubmit && e.confirmBtnCfg.enabled ? (u(), b(a, {
                key: "submit",
                type: "primary",
                preIcon: e.confirmBtnCfg.buttonIcon,
                loading: e.submitLoading,
                onClick: e.handleSubmit
              }, {
                default: m(() => [
                  O("span", null, B(e.confirmBtnCfg.buttonName), 1)
                ]),
                _: 1
              }, 8, ["preIcon", "loading", "onClick"])) : M("", !0),
              e.cancelBtnCfg.enabled ? (u(), b(a, {
                key: "back",
                onClick: e.handleCancel
              }, {
                default: m(() => [
                  O("span", null, B(e.cancelBtnCfg.buttonName), 1)
                ]),
                _: 1
              }, 8, ["onClick"])) : M("", !0)
            ]),
            _: 1
          }, 8, ["span"])
        ]),
        _: 1
      })
    ]),
    comment: m(() => [
      C(g, {
        ref: "commentPanelRef",
        tableId: e.tableId,
        tableName: e.tableName,
        dataId: e.formDataId
      }, null, 8, ["tableId", "tableName", "dataId"])
    ]),
    default: m(() => [
      C(i, y(e.$attrs, {
        ref: "onlineFormCompRef",
        id: e.id,
        disabled: e.disableSubmit,
        "form-template": e.formTemplate,
        isTree: e.isTreeForm,
        pidField: e.pidFieldName,
        themeTemplate: e.themeTemplate,
        cgBIBtnMap: e.cgBIBtnMap,
        buttonSwitch: e.buttonSwitch,
        onRendered: e.renderSuccess,
        onSuccess: e.handleSuccess
      }), null, 16, ["id", "disabled", "form-template", "isTree", "pidField", "themeTemplate", "cgBIBtnMap", "buttonSwitch", "onRendered", "onSuccess"])
    ]),
    _: 1
  }, 16, ["title", "onCancel", "enableComment", "width", "onRegister", "onOk", "onCommentOpen"]);
}
const Ze = /* @__PURE__ */ ee(te, [["render", oe]]);
export {
  Ze as default
};
