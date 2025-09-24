var c = (e, i, o) => new Promise((l, n) => {
  var p = (t) => {
    try {
      m(o.next(t));
    } catch (r) {
      n(r);
    }
  }, a = (t) => {
    try {
      m(o.throw(t));
    } catch (r) {
      n(r);
    }
  }, m = (t) => t.done ? l(t.value) : Promise.resolve(t.value).then(p, a);
  m((o = o.apply(e, i)).next());
});
import { defineComponent as P, ref as v, watch as O, resolveComponent as d, openBlock as u, createBlock as b, mergeProps as E, withCtx as s, createElementVNode as L, toDisplayString as V, createCommentVNode as j, renderSlot as A, createVNode as C } from "vue";
import { BasicModal as W } from "/@/components/Modal";
import q from "./OnlineFormDetail-fc087725.mjs";
import { b as H } from "./useExtendComponent-bb98e568.mjs";
import U from "/@/components/jeecg/comment/CommentPanel.vue";
import { a as z } from "./constant-fa63bd66.mjs";
import { _ as G } from "./index-9e1e1e53.mjs";
import "/@/hooks/web/useMessage";
import "/@/components/Loading";
import "/@/utils/auth";
import "/@/utils";
import "@ant-design/icons-vue";
import "./DetailForm-c592b8d8.mjs";
import "/@/utils/propTypes";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/dict/index";
import "/@/api/common/api";
import "/@/utils/http/axios";
import "/@/components/Form/src/utils/Area";
import "/@/utils/common/compUtils";
import "/@/components/Preview/index";
import "/@/components/Markdown";
import "/@/components/Form/src/componentMap";
import "/@/components/Form/index";
import "lodash-es";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/utils/is";
import "/@/views/system/user/user.api";
import "/@/store/modules/user";
import "/@/utils/desform/customExpression";
import "/@/store/modules/permission";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "vue-router";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useAppInject";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
import "./OnlineSubFormDetail-8be879b9.mjs";
const J = P({
  name: "OnlineDetailModal",
  props: {
    id: {
      type: String,
      required: !1,
      default: ""
    },
    // 为了区分来源，编码时主要是erp子表有特殊处理
    source: {
      type: String,
      default: ""
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
    BasicModal: W,
    OnlineFormDetail: q,
    CommentPanel: U
  },
  emits: ["success", "register", "formConfig"],
  setup(e, { emit: i }) {
    const o = v();
    function l() {
      o.value && o.value.reload();
    }
    const {
      title: n,
      modalWidth: p,
      registerModal: a,
      cgButtonList: m,
      handleCgButtonClick: t,
      disableSubmit: r,
      handleSubmit: h,
      submitLoading: g,
      handleCancel: B,
      handleFormConfig: N,
      onlineFormCompRef: S,
      formTemplate: w,
      isTreeForm: R,
      pidFieldName: F,
      renderSuccess: M,
      formRendered: T,
      showSub: k,
      tableName: y,
      formDataId: _,
      enableComment: D,
      themeTemplate: I
    } = H(!1, { emit: i }, l);
    O(() => e.id, $, { immediate: !0 });
    function $() {
      return c(this, null, function* () {
        if (T.value = !1, !e.id)
          return;
        let f = {};
        e.source === z && (f.tabletype = 3), yield N(e.id, f);
      });
    }
    return {
      title: n,
      onlineFormCompRef: S,
      renderSuccess: M,
      registerModal: a,
      handleSubmit: h,
      handleCancel: B,
      modalWidth: p,
      formTemplate: w,
      disableSubmit: r,
      cgButtonList: m,
      handleCgButtonClick: t,
      isTreeForm: R,
      pidFieldName: F,
      submitLoading: g,
      showSub: k,
      tableName: y,
      formDataId: _,
      enableComment: D,
      commentPanelRef: o,
      themeTemplate: I
    };
  }
});
function K(e, i, o, l, n, p) {
  const a = d("a-button"), m = d("online-form-detail"), t = d("comment-panel"), r = d("BasicModal");
  return u(), b(r, E({
    title: e.title,
    width: e.modalWidth,
    maxHeight: 600,
    enableComment: e.enableComment,
    defaultFullscreen: !1
  }, e.$attrs, {
    onRegister: e.registerModal,
    wrapClassName: "jeecg-online-detail-modal"
  }), {
    footer: s(() => [
      e.cancelBtnCfg.enabled ? (u(), b(a, {
        key: "back",
        onClick: e.handleCancel
      }, {
        default: s(() => [
          L("span", null, V(e.cancelBtnCfg.buttonName), 1)
        ]),
        _: 1
      }, 8, ["onClick"])) : j("", !0),
      A(e.$slots, "footerBtn")
    ]),
    comment: s(() => [
      C(t, {
        ref: "commentPanelRef",
        tableName: e.tableName,
        dataId: e.formDataId
      }, null, 8, ["tableName", "dataId"])
    ]),
    default: s(() => [
      C(m, {
        ref: "onlineFormCompRef",
        id: e.id,
        "form-template": e.formTemplate,
        "show-sub": e.showSub,
        themeTemplate: e.themeTemplate,
        onRendered: e.renderSuccess
      }, null, 8, ["id", "form-template", "show-sub", "themeTemplate", "onRendered"])
    ]),
    _: 3
  }, 16, ["title", "width", "enableComment", "onRegister"]);
}
const We = /* @__PURE__ */ G(J, [["render", K]]);
export {
  We as default
};
