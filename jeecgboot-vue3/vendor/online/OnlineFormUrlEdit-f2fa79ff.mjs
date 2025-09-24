import { defineComponent as x, ref as n, unref as o, watch as F, openBlock as u, createElementBlock as I, normalizeClass as c, createVNode as d, withCtx as f, createElementVNode as b, createCommentVNode as _, createBlock as R } from "vue";
import { Card as y, Spin as O } from "ant-design-vue";
import { useMessage as U } from "/@/hooks/web/useMessage";
import "/@/components/Form/index";
import "/@/utils/http/axios";
import "lodash-es";
import "/@/utils";
import { u as B } from "./useExtendComponent-bb98e568.mjs";
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
import { useRoute as M, useRouter as S } from "vue-router";
import T from "./OnlineAutoModal-95f46901.mjs";
import { u as D, a as N } from "./useListButton-98908683.mjs";
import { u as V } from "./useFormUrl-2ee1a82d.mjs";
import { _ as $ } from "./index-9e1e1e53.mjs";
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
import "./OnlineForm-58282699.mjs";
import "./useCustomHook-acb00837.mjs";
import "/@/components/jeecg/comment/CommentPanel.vue";
import "/@/router";
import "/@/hooks/core/onMountedOrActivated";
import "/@/store/modules/multipleTab";
import "./cgformState-d9f8ec42.mjs";
import "pinia";
import "/@/store";
import "/@/hooks/system/useMethods";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
const H = /* @__PURE__ */ x({
  __name: "OnlineFormUrlEdit",
  setup(q) {
    const { ID: t, onlineTableContext: m, onlineExtConfigJson: g, handleFormConfig: h } = D(), { createMessage: C } = U(), e = M(), p = n(null), a = n(400), s = n(!0), v = S(), { token: r } = V();
    if (!o(r))
      throw new Error("token不存在~");
    if (!t.value || !e.params.dataId)
      throw C.warning("地址错误, 配置ID不存在!"), new Error("地址错误, 配置ID不存在!");
    B(m), F(
      () => p.value,
      (i) => {
        a.value = i.offsetHeight - 60;
      }
    );
    const { registerModal: w, handleEdit: l } = N(m, g), E = (i) => document.querySelector(`.online-edit-${t.value}`), k = () => {
      setTimeout(() => {
        l({ id: e.params.dataId }), v.push({ path: "/online/formUrlSuccess" });
      }, 0);
    };
    return setTimeout(() => {
      r && l({ id: e.params.dataId }), s.value = !1;
    }, 1e3), (i, z) => (u(), I("div", {
      class: c([["p-2"], "online-wrap"])
    }, [
      d(o(y), { bordered: !1 }, {
        default: f(() => [
          d(o(O), { spinning: s.value }, {
            default: f(() => [
              b("div", {
                class: c(["wrap", `online-edit-${o(t)}`]),
                ref_key: "wrapRef",
                ref: p
              }, [
                _("", !0),
                o(r) ? (u(), R(T, {
                  key: 1,
                  id: o(t),
                  maskClosable: !1,
                  onRegister: o(w),
                  getContainer: E,
                  onFormConfig: o(h),
                  onSuccess: k,
                  height: a.value
                }, null, 8, ["id", "onRegister", "onFormConfig", "height"])) : _("", !0)
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
const Lo = /* @__PURE__ */ $(H, [["__scopeId", "data-v-0b4f6a76"]]);
export {
  Lo as default
};
