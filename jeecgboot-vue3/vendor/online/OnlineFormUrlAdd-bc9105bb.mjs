import { defineComponent as x, ref as r, unref as o, watch as F, openBlock as l, createElementBlock as E, normalizeClass as u, createVNode as c, withCtx as d, createElementVNode as y, createCommentVNode as f, createBlock as A } from "vue";
import { Card as O, Spin as R } from "ant-design-vue";
import { useMessage as T } from "/@/hooks/web/useMessage";
import "/@/components/Form/index";
import "/@/utils/http/axios";
import "lodash-es";
import "/@/utils";
import { u as U } from "./useExtendComponent-bb98e568.mjs";
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
import b from "./OnlineAutoModal-95f46901.mjs";
import { u as B, a as I } from "./useListButton-98908683.mjs";
import { useRouter as M } from "vue-router";
import { u as S } from "./useFormUrl-2ee1a82d.mjs";
import { _ as D } from "./index-9e1e1e53.mjs";
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
const N = /* @__PURE__ */ x({
  __name: "OnlineFormUrlAdd",
  setup(V) {
    const { ID: e, onlineTableContext: i, onlineExtConfigJson: _, handleFormConfig: g } = B(), { createMessage: h } = T(), n = r(null), m = r(400), p = r(!0), C = M(), { token: s } = S();
    if (!o(s))
      throw new Error("token不存在~");
    if (!e.value)
      throw h.warning("地址错误, 配置ID不存在!"), new Error("地址错误, 配置ID不存在!");
    U(i), F(
      () => n.value,
      (t) => {
        m.value = t.offsetHeight - 60;
      }
    );
    const { registerModal: v, handleAdd: a } = I(i, _), w = (t) => document.querySelector(`.online-add-${e.value}`), k = () => {
      setTimeout(() => {
        a(!1), setTimeout(() => {
          C.push({ path: "/online/formUrlSuccess" });
        }, 1e3);
      }, 0);
    };
    return setTimeout(() => {
      a(!1), p.value = !1;
    }, 1e3), (t, $) => (l(), E("div", {
      class: u([["p-2"], "online-wrap"])
    }, [
      c(o(O), { bordered: !1 }, {
        default: d(() => [
          c(o(R), { spinning: p.value }, {
            default: d(() => [
              y("div", {
                class: u(["wrap", `online-add-${o(e)}`]),
                ref_key: "wrapRef",
                ref: n
              }, [
                f("", !0),
                o(s) ? (l(), A(b, {
                  key: 1,
                  id: o(e),
                  maskClosable: !1,
                  onRegister: o(v),
                  getContainer: w,
                  onFormConfig: o(g),
                  onSuccess: k,
                  height: m.value
                }, null, 8, ["id", "onRegister", "onFormConfig", "height"])) : f("", !0)
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
const zo = /* @__PURE__ */ D(N, [["__scopeId", "data-v-4a0277d5"]]);
export {
  zo as default
};
