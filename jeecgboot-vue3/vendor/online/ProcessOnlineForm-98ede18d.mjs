var u = (e, t, n) => new Promise((p, s) => {
  var d = (o) => {
    try {
      a(n.next(o));
    } catch (r) {
      s(r);
    }
  }, i = (o) => {
    try {
      a(n.throw(o));
    } catch (r) {
      s(r);
    }
  }, a = (o) => o.done ? p(o.value) : Promise.resolve(o.value).then(d, i);
  a((n = n.apply(e, t)).next());
});
import C from "./OnlineForm-58282699.mjs";
import { defineComponent as S, ref as m, watch as T, nextTick as w, resolveComponent as f, openBlock as y, createElementBlock as _, createVNode as g, withCtx as h, createTextVNode as N, createCommentVNode as P } from "vue";
import { defHttp as R } from "/@/utils/http/axios";
import { g as b } from "./useExtendComponent-bb98e568.mjs";
import { _ as L } from "./index-9e1e1e53.mjs";
import "/@/hooks/web/useMessage";
import "/@/components/Form/index";
import "lodash-es";
import "/@/utils";
import "/@/components/Loading";
import "/@/components/jeecg/JVxeTable/types";
import "/@/utils/auth";
import "@ant-design/icons-vue";
import "/@/hooks/core/useContext";
import "/@/utils/mitt";
import "/@/components/Modal";
import "./useCustomHook-acb00837.mjs";
import "/@/utils/cache";
import "/@/utils/common/compUtils";
import "/@/store/modules/user";
import "./constant-fa63bd66.mjs";
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
import "/@/api/common/api";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
const $ = S({
  name: "ProcessOnlineForm",
  inheritAttrs: !1,
  components: {
    OnlineForm: C
  },
  props: {
    dataId: {
      type: String,
      default: ""
    },
    tableName: {
      type: String,
      default: ""
    },
    taskId: {
      type: String,
      default: ""
    },
    disabled: {
      type: Boolean,
      default: !1
    }
  },
  setup(e) {
    const t = m(), n = m(""), p = m(1), s = m(!1), d = m(""), i = m(!1);
    T(
      () => e.tableName,
      (c) => {
        c && a();
      },
      { immediate: !0 }
    );
    function a() {
      return u(this, null, function* () {
        i.value = !0;
        const c = `/online/cgform/api/getFormItemBytbname/${e.tableName}`, k = { taskId: e.taskId };
        try {
          let l = yield R.get({ url: c, params: k });
          n.value = l.head.id, p.value = Number(l.head.formTemplate || 1), s.value = l.head.isTree === "Y", d.value = l.head.treeParentIdField || "", yield w(() => u(this, null, function* () {
            (yield b(t)).createRootProperties(l);
          }));
        } catch (l) {
        }
      });
    }
    function o() {
      return u(this, null, function* () {
        let c = yield b(t);
        i.value = !1, c.show(!0, {
          id: e.dataId
        });
      });
    }
    const r = m(!1);
    function F() {
      return u(this, null, function* () {
        r.value = !0, t.value.handleSubmit();
      });
    }
    function v() {
      r.value = !1;
    }
    function I() {
      r.value = !1;
    }
    return {
      onlineFormCompRef: t,
      formId: n,
      formTemplate: p,
      isTreeForm: s,
      pidFieldName: d,
      renderSuccess: o,
      handleSuccess: v,
      handleClose: I,
      handleSubmit: F,
      buttonLoading: r,
      spinLoading: i
    };
  }
}), B = { class: "cust-onl-form" }, O = {
  key: 0,
  style: { width: "100%", "text-align": "center", "margin-top": "5px" }
};
function V(e, t, n, p, s, d) {
  const i = f("a-button"), a = f("online-form"), o = f("a-spin");
  return y(), _("div", B, [
    g(o, { spinning: e.spinLoading }, {
      default: h(() => [
        g(a, {
          ref: "onlineFormCompRef",
          id: e.formId,
          disabled: e.disabled,
          "form-template": e.formTemplate,
          isTree: e.isTreeForm,
          pidField: e.pidFieldName,
          taskId: e.taskId,
          onRendered: e.renderSuccess,
          onSuccess: e.handleSuccess,
          onClose: e.handleClose
        }, {
          bottom: h(() => [
            !e.disabled && !e.spinLoading ? (y(), _("div", O, [
              g(i, {
                preIcon: "ant-design:check",
                style: { width: "126px" },
                type: "primary",
                onClick: e.handleSubmit,
                loading: e.buttonLoading
              }, {
                default: h(() => t[0] || (t[0] = [
                  N(" 提 交 ")
                ])),
                _: 1
              }, 8, ["onClick", "loading"])
            ])) : P("", !0)
          ]),
          _: 1
        }, 8, ["id", "disabled", "form-template", "isTree", "pidField", "taskId", "onRendered", "onSuccess", "onClose"])
      ]),
      _: 1
    }, 8, ["spinning"])
  ]);
}
const Ne = /* @__PURE__ */ L($, [["render", V], ["__scopeId", "data-v-a4356798"]]);
export {
  Ne as default
};
