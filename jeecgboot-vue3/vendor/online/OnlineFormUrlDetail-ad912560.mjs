import { defineComponent as C, ref as r, unref as t, watch as D, openBlock as l, createElementBlock as k, normalizeClass as u, createVNode as c, withCtx as d, createElementVNode as x, createCommentVNode as f, createBlock as b } from "vue";
import { Card as E, Spin as I } from "ant-design-vue";
import { useMessage as y } from "/@/hooks/web/useMessage";
import "/@/components/Form/index";
import "/@/utils/http/axios";
import "lodash-es";
import "/@/utils";
import { u as M } from "./useExtendComponent-bb98e568.mjs";
import "/@/components/Loading";
import "/@/components/jeecg/JVxeTable/types";
import "/@/utils/auth";
import "@ant-design/icons-vue";
import "/@/hooks/core/useContext";
import "/@/utils/mitt";
import "/@/components/Modal";
import "/@/utils/cache";
import "/@/utils/common/compUtils";
import "/@/store/modules/user";
import "/@/hooks/web/useAppInject";
import "/@/utils/is";
import "/@/store/modules/permission";
import "./OnlineForm.vue_vue_type_style_index_0_scoped_3f26e7bd_lang-4ed993c7.mjs";
import { useRoute as O } from "vue-router";
import R from "./OnlineDetailModal-5b412bb9.mjs";
import { u as S, a as U } from "./useListButton-98908683.mjs";
import { u as B } from "./useFormUrl-2ee1a82d.mjs";
import { _ as F } from "./index-9e1e1e53.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "./constant-fa63bd66.mjs";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/views/system/user/user.api";
import "/@/utils/desform/customExpression";
import "/@/utils/dict/JDictSelectUtil";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/api/common/api";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "@vueuse/core";
import "./OnlineFormDetail-fc087725.mjs";
import "./DetailForm-c592b8d8.mjs";
import "/@/utils/dict";
import "/@/utils/dict/index";
import "/@/components/Markdown";
import "./OnlineSubFormDetail-8be879b9.mjs";
import "/@/components/jeecg/comment/CommentPanel.vue";
import "/@/router";
import "./useCustomHook-acb00837.mjs";
import "/@/hooks/core/onMountedOrActivated";
import "/@/store/modules/multipleTab";
import "./cgformState-d9f8ec42.mjs";
import "pinia";
import "/@/store";
import "/@/hooks/system/useMethods";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
const N = /* @__PURE__ */ C({
  __name: "OnlineFormUrlDetail",
  setup(T) {
    const i = O(), { ID: e, onlineTableContext: m, onlineExtConfigJson: _ } = S(), { createMessage: g } = y(), n = r(null), p = r(400), a = r(!0), { token: s } = B();
    if (!t(s))
      throw new Error("token不存在~");
    if (!e.value || !i.params.dataId)
      throw g.warning("地址错误, 配置ID不存在!"), new Error("地址错误, 配置ID不存在!");
    D(
      () => n.value,
      (o) => {
        p.value = o.offsetHeight;
      }
    ), M(m);
    const { registerDetailModal: h, openDetailModal: v } = U(m, _), w = (o) => document.querySelector(`.online-detail-${e.value}`);
    return setTimeout(() => {
      v(!0, {
        isUpdate: !0,
        disableSubmit: !0,
        record: {
          id: i.params.dataId
        }
      }), a.value = !1;
    }, 1e3), (o, V) => (l(), k("div", {
      class: u([["p-2"], "online-wrap"])
    }, [
      c(t(E), { bordered: !1 }, {
        default: d(() => [
          c(t(I), { spinning: a.value }, {
            default: d(() => [
              x("div", {
                class: u(["wrap", `online-detail-${t(e)}`]),
                ref_key: "wrapRef",
                ref: n
              }, [
                f("", !0),
                t(s) ? (l(), b(R, {
                  key: 1,
                  id: t(e),
                  maskClosable: !1,
                  onRegister: t(h),
                  onSuccess: o.success,
                  getContainer: w,
                  height: p.value
                }, null, 8, ["id", "onRegister", "onSuccess", "height"])) : f("", !0)
              ], 2)
            ]),
            _: 1
          }, 8, ["spinning"])
        ]),
        _: 1
      })
    ]));
  }
});
const jt = /* @__PURE__ */ F(N, [["__scopeId", "data-v-cea77271"]]);
export {
  jt as default
};
