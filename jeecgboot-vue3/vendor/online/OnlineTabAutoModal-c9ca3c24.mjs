var L = (e, n, i) => new Promise((c, d) => {
  var C = (a) => {
    try {
      s(i.next(a));
    } catch (p) {
      d(p);
    }
  }, h = (a) => {
    try {
      s(i.throw(a));
    } catch (p) {
      d(p);
    }
  }, s = (a) => a.done ? c(a.value) : Promise.resolve(a.value).then(C, h);
  s((i = i.apply(e, n)).next());
});
import { defineComponent as oe, ref as B, watch as ne, computed as ae, resolveComponent as r, openBlock as m, createBlock as u, mergeProps as re, createSlots as le, withCtx as o, createVNode as f, createElementBlock as A, Fragment as V, renderList as j, createTextVNode as I, toDisplayString as g, createElementVNode as N, createCommentVNode as me } from "vue";
import { BasicModal as ie } from "/@/components/Modal";
import de from "./OnlineTabForm-1940e88b.mjs";
import { b as se } from "./useExtendComponent-bb98e568.mjs";
import pe from "/@/components/jeecg/comment/CommentPanel.vue";
import { T as ue } from "./constant-fa63bd66.mjs";
import { useAppInject as ce } from "/@/hooks/web/useAppInject";
import { _ as be } from "./index-9e1e1e53.mjs";
import "/@/hooks/web/useMessage";
import "/@/components/Form/index";
import "/@/utils/http/axios";
import "lodash-es";
import "/@/utils";
import "/@/components/Loading";
import "/@/utils/auth";
import "@ant-design/icons-vue";
import "./useCustomHook-acb00837.mjs";
import "/@/utils/cache";
import "/@/utils/common/compUtils";
import "/@/store/modules/user";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/utils/is";
import "/@/views/system/user/user.api";
import "/@/utils/desform/customExpression";
import "/@/store/modules/permission";
import "/@/utils/dict/JDictSelectUtil";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "vue-router";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/api/common/api";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
const fe = oe({
  name: "OnlineTabAutoModal",
  props: {
    id: {
      type: String,
      default: ""
    },
    cgBIBtnMap: Object,
    buttonSwitch: Object,
    confirmBtnCfg: {
      type: Object,
      default: () => ({
        enabled: !0,
        buttonName: "确定",
        buttonIcon: ""
      })
    }
  },
  components: {
    BasicModal: ie,
    OnlineForm: de,
    CommentPanel: pe
  },
  emits: ["success", "register", "formConfig"],
  setup(e, { emit: n }) {
    const i = B(), c = B([]), d = B("-1"), C = B(0), { getIsMobile: h } = ce();
    function s() {
      i.value && i.value.reload();
    }
    const {
      title: a,
      modalWidth: p,
      registerModal: S,
      closeModal: T,
      cgButtonList: y,
      handleCgButtonClick: w,
      disableSubmit: M,
      handleSubmit: _,
      submitLoading: O,
      handleCancel: t,
      handleFormConfig: b,
      onlineFormCompRef: P,
      formTemplate: W,
      isTreeForm: H,
      pidFieldName: U,
      renderSuccess: q,
      formRendered: z,
      tableName: G,
      formDataId: J,
      enableComment: K,
      onCloseEvent: D,
      themeTemplate: Q
    } = se(!1, { emit: n }, s);
    function X(l) {
      n("success", l), T(), D(), E();
    }
    ne(() => e.id, Y, { immediate: !0 });
    function Y() {
      return L(this, null, function* () {
        z.value = !1, e.id && (yield b(e.id, {}, (l) => {
          const k = [], F = [], { head: $, schema: ee } = l, { properties: te } = ee;
          k.push({ tableName: $.tableName, tableTxt: $.tableTxt }), Object.entries(te).forEach(([R, v]) => {
            v.view == "tab" && F.push({ tableName: R, tableTxt: v.describe, order: v.order });
          }), F.sort((R, v) => R.order - v.order), c.value = [...k, ...F];
        }));
      });
    }
    const Z = (l) => String(l - 1), E = () => {
      setTimeout(() => {
        d.value = "-1";
      }, 500);
    }, x = ae(() => h.value && c.value.length > 2);
    return {
      title: a,
      onlineFormCompRef: P,
      renderSuccess: q,
      registerModal: S,
      handleSubmit: _,
      handleSuccess: X,
      handleCancel: t,
      modalWidth: p,
      formTemplate: W,
      disableSubmit: M,
      cgButtonList: y,
      handleCgButtonClick: w,
      isTreeForm: H,
      pidFieldName: U,
      submitLoading: O,
      tableName: G,
      formDataId: J,
      enableComment: K,
      commentPanelRef: i,
      onCloseEvent: D,
      themeTemplate: Q,
      tabNav: c,
      tabValue: Z,
      tabIndex: d,
      TAB: ue,
      restTabIndex: E,
      handleMenuClick: ({ key: l }) => {
        d.value = l;
      },
      showDropdownBtn: x,
      handleToggleTab: (l) => {
        d.value = l;
      },
      handleCommentOpen: (l, k) => {
        C.value = k;
      },
      commentSpan: C
    };
  }
});
const ge = { class: "titleArea" }, Ce = { class: "title" }, he = { class: "right" };
function Te(e, n, i, c, d, C) {
  const h = r("a-menu-item"), s = r("a-menu"), a = r("a-dropdown-button"), p = r("a-radio-button"), S = r("a-radio-group"), T = r("a-button"), y = r("a-col"), w = r("a-row"), M = r("online-form"), _ = r("comment-panel"), O = r("BasicModal");
  return m(), u(O, re({
    title: e.title,
    onCancel: n[2] || (n[2] = () => {
      e.onCloseEvent(), e.restTabIndex();
    }),
    enableComment: e.enableComment,
    width: e.modalWidth
  }, e.$attrs, {
    maxHeight: 600,
    onRegister: e.registerModal,
    wrapClassName: "jeecg-online-modal",
    onOk: e.handleSubmit,
    onCommentOpen: e.handleCommentOpen
  }), le({
    footer: o(() => [
      f(w, null, {
        default: o(() => [
          f(y, {
            span: 24 - e.commentSpan
          }, {
            default: o(() => [
              (m(!0), A(V, null, j(e.cgButtonList, (t) => (m(), u(T, {
                key: t.id,
                type: "primary",
                onClick: (b) => e.handleCgButtonClick(t.optType, t.buttonCode),
                preIcon: t.buttonIcon ? "ant-design:" + t.buttonIcon : ""
              }, {
                default: o(() => [
                  I(g(t.buttonName), 1)
                ]),
                _: 2
              }, 1032, ["onClick", "preIcon"]))), 128)),
              !e.disableSubmit && e.confirmBtnCfg.enabled ? (m(), u(T, {
                key: "submit",
                type: "primary",
                preIcon: e.confirmBtnCfg.buttonIcon,
                loading: e.submitLoading,
                onClick: e.handleSubmit
              }, {
                default: o(() => [
                  N("span", null, g(e.confirmBtnCfg.buttonName), 1)
                ]),
                _: 1
              }, 8, ["preIcon", "loading", "onClick"])) : me("", !0),
              f(T, {
                key: "back",
                onClick: n[1] || (n[1] = () => {
                  e.handleCancel(), e.restTabIndex();
                })
              }, {
                default: o(() => n[3] || (n[3] = [
                  I("关闭")
                ])),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["span"])
        ]),
        _: 1
      })
    ]),
    comment: o(() => [
      f(_, {
        ref: "commentPanelRef",
        tableName: e.tableName,
        dataId: e.formDataId
      }, null, 8, ["tableName", "dataId"])
    ]),
    default: o(() => [
      f(M, {
        ref: "onlineFormCompRef",
        id: e.id,
        disabled: e.disableSubmit,
        "form-template": e.formTemplate,
        isTree: e.isTreeForm,
        pidField: e.pidFieldName,
        themeTemplate: e.themeTemplate,
        tabIndex: e.tabIndex,
        cgBIBtnMap: e.cgBIBtnMap,
        buttonSwitch: e.buttonSwitch,
        onRendered: e.renderSuccess,
        onSuccess: e.handleSuccess,
        onToggleTab: e.handleToggleTab
      }, null, 8, ["id", "disabled", "form-template", "isTree", "pidField", "themeTemplate", "tabIndex", "cgBIBtnMap", "buttonSwitch", "onRendered", "onSuccess", "onToggleTab"])
    ]),
    _: 2
  }, [
    e.themeTemplate === e.TAB ? {
      name: "title",
      fn: o(() => [
        N("div", ge, [
          N("div", Ce, g(e.title), 1),
          N("div", he, [
            e.showDropdownBtn ? (m(), u(a, {
              key: 0,
              trigger: "click"
            }, {
              overlay: o(() => [
                f(s, { onClick: e.handleMenuClick }, {
                  default: o(() => [
                    (m(!0), A(V, null, j(e.tabNav, (t, b) => (m(), u(h, {
                      key: e.tabValue(b)
                    }, {
                      default: o(() => [
                        I(g(t.tableTxt), 1)
                      ]),
                      _: 2
                    }, 1024))), 128))
                  ]),
                  _: 1
                }, 8, ["onClick"])
              ]),
              default: o(() => [
                I(g(e.tabNav[+e.tabIndex + 1].tableTxt) + " ", 1)
              ]),
              _: 1
            })) : (m(), u(S, {
              key: 1,
              value: e.tabIndex,
              "onUpdate:value": n[0] || (n[0] = (t) => e.tabIndex = t)
            }, {
              default: o(() => [
                (m(!0), A(V, null, j(e.tabNav, (t, b) => (m(), u(p, {
                  value: e.tabValue(b),
                  key: t.tableName
                }, {
                  default: o(() => [
                    I(g(t.tableTxt), 1)
                  ]),
                  _: 2
                }, 1032, ["value"]))), 128))
              ]),
              _: 1
            }, 8, ["value"]))
          ])
        ])
      ]),
      key: "0"
    } : void 0
  ]), 1040, ["title", "enableComment", "width", "onRegister", "onOk", "onCommentOpen"]);
}
const ut = /* @__PURE__ */ be(fe, [["render", Te], ["__scopeId", "data-v-e1588480"]]);
export {
  ut as default
};
