import { defineComponent as p, resolveComponent as l, openBlock as f, createBlock as c, withCtx as o, createVNode as n, createTextVNode as r } from "vue";
import { router as d } from "/@/router";
import { PageEnum as _ } from "/@/enums/pageEnum";
const x = /* @__PURE__ */ p({
  __name: "ErrorTip",
  props: {
    title: {
      type: String,
      default: "啊哦，页面出错了"
    },
    subTitle: String
  },
  setup(i) {
    function u() {
      d.push(_.BASE_HOME);
    }
    function s() {
      location.reload();
    }
    function a() {
      window.close(), setTimeout(() => alert("当前页面无法通过按钮关闭，请手动关闭"), 1e3);
    }
    return (T, t) => {
      const e = l("a-button"), m = l("a-result");
      return f(), c(m, {
        status: "error",
        title: i.title,
        subTitle: i.subTitle
      }, {
        extra: o(() => [
          n(e, {
            type: "primary",
            onClick: u
          }, {
            default: o(() => t[0] || (t[0] = [
              r("返回首页")
            ])),
            _: 1
          }),
          n(e, { onClick: s }, {
            default: o(() => t[1] || (t[1] = [
              r("刷新页面")
            ])),
            _: 1
          }),
          n(e, { onClick: a }, {
            default: o(() => t[2] || (t[2] = [
              r("关闭")
            ])),
            _: 1
          })
        ]),
        _: 1
      }, 8, ["title", "subTitle"]);
    };
  }
});
export {
  x as _
};
