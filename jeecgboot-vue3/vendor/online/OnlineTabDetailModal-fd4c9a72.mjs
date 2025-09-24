var F = (e, t, a) => new Promise((s, i) => {
  var u = (o) => {
    try {
      n(a.next(o));
    } catch (r) {
      i(r);
    }
  }, p = (o) => {
    try {
      n(a.throw(o));
    } catch (r) {
      i(r);
    }
  }, n = (o) => o.done ? s(o.value) : Promise.resolve(o.value).then(u, p);
  n((a = a.apply(e, t)).next());
});
import { defineComponent as z, ref as C, watch as G, resolveComponent as d, openBlock as I, createBlock as M, mergeProps as J, withCtx as m, createElementVNode as N, toDisplayString as R, createVNode as f, createElementBlock as K, Fragment as Q, renderList as X, createTextVNode as S } from "vue";
import { BasicModal as Y } from "/@/components/Modal";
import Z from "./OnlineTabFormDetail-2aa67564.mjs";
import { b as ee } from "./useExtendComponent-bb98e568.mjs";
import te from "/@/components/jeecg/comment/CommentPanel.vue";
import { T as oe } from "./constant-fa63bd66.mjs";
import { _ as ae } from "./index-9e1e1e53.mjs";
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
import "./OnlineSubFormDetail-8be879b9.mjs";
import "/@/components/Form/index";
import "/@/hooks/web/useAppInject";
import "/@/components/Form/src/componentMap";
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
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
const ne = z({
  name: "OnlineTabDetailModal",
  props: {
    id: {
      type: String,
      required: !1,
      default: ""
    }
  },
  components: {
    BasicModal: Y,
    OnlineFormDetail: Z,
    CommentPanel: te
  },
  emits: ["success", "register", "formConfig"],
  setup(e, { emit: t }) {
    const a = C(), s = C([]), i = C("-1");
    function u() {
      a.value && a.value.reload();
    }
    const {
      title: p,
      modalWidth: n,
      registerModal: o,
      cgButtonList: r,
      handleCgButtonClick: c,
      disableSubmit: h,
      handleSubmit: l,
      submitLoading: g,
      handleCancel: k,
      handleFormConfig: D,
      onlineFormCompRef: y,
      formTemplate: V,
      isTreeForm: O,
      pidFieldName: $,
      renderSuccess: E,
      formRendered: P,
      showSub: A,
      tableName: L,
      formDataId: j,
      enableComment: W,
      themeTemplate: q
    } = ee(!1, { emit: t }, u);
    G(() => e.id, H, { immediate: !0 });
    function H() {
      return F(this, null, function* () {
        P.value = !1, e.id && (yield D(e.id, {}, (T) => {
          const w = [], _ = [], { head: B, schema: U } = T, { properties: x } = U;
          w.push({ tableName: B.tableName, tableTxt: B.tableTxt }), Object.entries(x).forEach(([v, b]) => {
            b.view == "tab" && _.push({ tableName: v, tableTxt: b.describe });
          }), _.sort((v, b) => v.order - b.order), s.value = [...w, ..._];
        }));
      });
    }
    return {
      title: p,
      onlineFormCompRef: y,
      renderSuccess: E,
      registerModal: o,
      handleSubmit: l,
      handleCancel: k,
      modalWidth: n,
      formTemplate: V,
      disableSubmit: h,
      cgButtonList: r,
      handleCgButtonClick: c,
      isTreeForm: O,
      pidFieldName: $,
      submitLoading: g,
      showSub: A,
      tableName: L,
      formDataId: j,
      enableComment: W,
      commentPanelRef: a,
      themeTemplate: q,
      tabNav: s,
      tabValue: (T) => String(T - 1),
      tabIndex: i,
      TAB: oe,
      restTabIndex: () => {
        setTimeout(() => {
          i.value = "-1";
        }, 500);
      }
    };
  }
});
const re = { class: "titleArea" }, me = { class: "title" }, ie = { class: "right" };
function le(e, t, a, s, i, u) {
  const p = d("a-radio-button"), n = d("a-radio-group"), o = d("a-button"), r = d("online-form-detail"), c = d("comment-panel"), h = d("BasicModal");
  return I(), M(h, J({
    title: e.title,
    onCancel: t[2] || (t[2] = () => {
      e.restTabIndex();
    }),
    width: e.modalWidth,
    maxHeight: 600,
    enableComment: e.enableComment,
    defaultFullscreen: !1
  }, e.$attrs, {
    onRegister: e.registerModal,
    wrapClassName: "jeecg-online-detail-modal"
  }), {
    title: m(() => [
      N("div", re, [
        N("div", me, R(e.title), 1),
        N("div", ie, [
          f(n, {
            value: e.tabIndex,
            "onUpdate:value": t[0] || (t[0] = (l) => e.tabIndex = l)
          }, {
            default: m(() => [
              (I(!0), K(Q, null, X(e.tabNav, (l, g) => (I(), M(p, {
                value: e.tabValue(g),
                key: l.tableName
              }, {
                default: m(() => [
                  S(R(l.tableTxt), 1)
                ]),
                _: 2
              }, 1032, ["value"]))), 128))
            ]),
            _: 1
          }, 8, ["value"])
        ])
      ])
    ]),
    footer: m(() => [
      f(o, {
        key: "back",
        onClick: t[1] || (t[1] = () => {
          e.handleCancel(), e.restTabIndex();
        })
      }, {
        default: m(() => t[3] || (t[3] = [
          S("关闭")
        ])),
        _: 1
      })
    ]),
    comment: m(() => [
      f(c, {
        ref: "commentPanelRef",
        tableName: e.tableName,
        dataId: e.formDataId
      }, null, 8, ["tableName", "dataId"])
    ]),
    default: m(() => [
      f(r, {
        ref: "onlineFormCompRef",
        id: e.id,
        "form-template": e.formTemplate,
        "show-sub": e.showSub,
        themeTemplate: e.themeTemplate,
        tabIndex: e.tabIndex,
        onRendered: e.renderSuccess
      }, null, 8, ["id", "form-template", "show-sub", "themeTemplate", "tabIndex", "onRendered"])
    ]),
    _: 1
  }, 16, ["title", "width", "enableComment", "onRegister"]);
}
const at = /* @__PURE__ */ ae(ne, [["render", le], ["__scopeId", "data-v-220dd945"]]);
export {
  at as default
};
