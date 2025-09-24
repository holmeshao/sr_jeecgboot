import { defineComponent as e, openBlock as o, createElementBlock as r } from "vue";
import { useUserStore as t } from "/@/store/modules/user";
const _ = /* @__PURE__ */ e({
  __name: "index",
  setup(n) {
    return t().getToken, (c, m) => (o(), r("div", null, "这里是你的登录页面"));
  }
});
export {
  _ as default
};
