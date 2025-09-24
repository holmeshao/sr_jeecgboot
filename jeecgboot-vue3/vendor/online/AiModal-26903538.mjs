import { ref as l, resolveComponent as i, openBlock as x, createBlock as A, unref as r, mergeProps as k, withCtx as c, createElementVNode as t, createVNode as u, createTextVNode as y } from "vue";
import { useModalInner as B, BasicModal as V } from "/@/components/Modal";
import { useMessage as b } from "/@/hooks/web/useMessage";
import { defHttp as z } from "/@/utils/http/axios";
import { _ as I } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const N = { class: "aiWrap" }, $ = { class: "content" }, R = {
  __name: "AiModal",
  emits: ["register", "success"],
  setup(U, { emit: p }) {
    const d = {
      aigc: "/online/cgform/api/aigc"
    }, m = p, [v, { closeModal: _ }] = B(), { createMessage: f } = b(), o = l(""), s = l(!1), g = l("ai创建表"), n = () => {
      _();
    }, h = () => {
      o.value.trim() === "" ? f.warning("请输入修饰词~") : (s.value = !0, z.post({ url: `${d.aigc}?prompt=${o.value}`, timeout: 1e3 * 60 * 5 }).then((a) => {
        s.value = !1, n(), m("success"), o.value = "";
      }).catch((a) => {
        s.value = !1;
      }));
    };
    return (a, e) => {
      const C = i("a-input"), M = i("a-button");
      return x(), A(r(V), k({
        height: 180,
        title: g.value,
        width: 600,
        maskClosable: !1
      }, a.$attrs, {
        onRegister: r(v),
        footer: null,
        onCancel: n
      }), {
        default: c(() => [
          t("div", N, [
            e[2] || (e[2] = t("div", { class: "titleArea" }, [
              t("svg", {
                t: "1707100353985",
                class: "icon",
                viewBox: "0 0 1024 1024",
                version: "1.1",
                xmlns: "http://www.w3.org/2000/svg",
                "p-id": "4235",
                width: "26",
                height: "26"
              }, [
                t("path", {
                  d: "M512 64C264.8 64 64 264.8 64 512s200.8 448 448 448 448-200.8 448-448S759.2 64 512 64z m32 704h-64v-64h64v64z m11.2-203.2l-5.6 4.8c-3.2 2.4-5.6 8-5.6 12.8v58.4h-64v-58.4c0-24.8 11.2-48 29.6-63.2l5.6-4.8c56-44.8 83.2-68 83.2-108C598.4 358.4 560 320 512 320c-49.6 0-86.4 36.8-86.4 86.4h-64C361.6 322.4 428 256 512 256c83.2 0 150.4 67.2 150.4 150.4 0 72.8-49.6 112.8-107.2 158.4z",
                  "p-id": "4236",
                  fill: "currentColor"
                })
              ]),
              t("h3", null, "创建工作表字段需要专业建议?试试AI智能推荐吧")
            ], -1)),
            e[3] || (e[3] = t("p", { class: "tip" }, "可尝试添加修饰词，如:智能家居行业的生产计划表", -1)),
            t("div", $, [
              u(C, {
                value: o.value,
                "onUpdate:value": e[0] || (e[0] = (w) => o.value = w),
                valueModifiers: { trim: !0 },
                placeholder: "请输入修饰词"
              }, null, 8, ["value"]),
              u(M, {
                loading: s.value,
                type: "primary",
                onClick: h
              }, {
                default: c(() => e[1] || (e[1] = [
                  y("生成表")
                ])),
                _: 1
              }, 8, ["loading"])
            ])
          ])
        ]),
        _: 1
      }, 16, ["title", "onRegister"]);
    };
  }
}, q = /* @__PURE__ */ I(R, [["__scopeId", "data-v-a85052cc"]]);
export {
  q as default
};
