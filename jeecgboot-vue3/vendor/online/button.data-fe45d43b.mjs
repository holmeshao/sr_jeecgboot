import { createVNode as o } from "vue";
import { Icon as r } from "/@/components/Icon";
function i({
  text: n
}) {
  return n ? o(r, {
    icon: "ant-design:" + n
  }, null) : "";
}
export {
  i as getButtonIconRender
};
