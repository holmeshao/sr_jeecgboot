var h = (m, e, i) => new Promise((o, c) => {
  var p = (n) => {
    try {
      t(i.next(n));
    } catch (a) {
      c(a);
    }
  }, r = (n) => {
    try {
      t(i.throw(n));
    } catch (a) {
      c(a);
    }
  }, t = (n) => n.done ? o(n.value) : Promise.resolve(n.value).then(p, r);
  t((i = i.apply(m, e)).next());
});
import { ref as u, reactive as x, resolveComponent as g, openBlock as _, createElementBlock as v, Fragment as y, createVNode as s, withCtx as d, createTextVNode as f, createElementVNode as M, normalizeStyle as F, renderList as B, toDisplayString as N } from "vue";
import { defHttp as R } from "/@/utils/http/axios";
import "/@/components/Form";
import { BasicModal as T, useModalInner as z, useModal as G } from "/@/components/Modal";
import { InfoCircleTwoTone as K } from "@ant-design/icons-vue";
import S from "./CodeFileViewModal-405e2b58.mjs";
import { useMessage as H } from "/@/hooks/web/useMessage";
import { downloadByData as I } from "/@/utils/file/download";
import { _ as E } from "./index-9e1e1e53.mjs";
import "ant-design-vue";
import "/@/hooks/web/useDesign";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const j = {
  name: "CodeFileListModal",
  components: {
    BasicModal: T,
    InfoCircleTwoTone: K,
    CodeFileViewModal: S
  },
  emits: ["register"],
  setup() {
    const { createMessage: m } = H(), e = u([]), i = window.innerHeight - 150, o = x({
      overflowY: "auto",
      maxHeight: i + "px"
    }), c = u(!1), p = u(""), r = u(""), [t, { closeModal: n }] = z((l) => h(this, null, function* () {
      e.value = l.codeList, p.value = l.tableName, r.value = l.pathKey;
    }));
    function a() {
      n();
    }
    function w() {
      let l = e.value;
      if (!l || l.length == 0) {
        m.warning("无代码！");
        return;
      }
      let V = l.join(",");
      return R.post(
        {
          url: "/online/cgform/api/downGenerateCode",
          params: {
            fileList: encodeURI(V),
            pathKey: r.value
          },
          responseType: "blob"
        },
        { isTransformResponse: !1 }
      ).then((C) => {
        if (!C || C.size == 0) {
          m.warning("导出代码失败！");
          return;
        }
        let b = "导到生成代码_" + p.value + "_" + (/* @__PURE__ */ new Date()).getTime() + ".zip";
        I(C, b, "application/zip");
      });
    }
    const [L, { openModal: k }] = G();
    function D() {
      let l = e.value;
      k(!0, {
        codeList: l,
        pathKey: r.value
      });
    }
    return {
      registerModal: t,
      registerCodeViewModal: L,
      divStyle: o,
      codeList: e,
      onDownloadGenerateCode: w,
      handleClose: a,
      handleView: D,
      loading: c
    };
  }
};
function U(m, e, i, o, c, p) {
  const r = g("info-circle-two-tone"), t = g("a-button"), n = g("BasicModal"), a = g("code-file-view-modal");
  return _(), v(y, null, [
    s(n, {
      onRegister: o.registerModal,
      width: 1200,
      defaultFullscreen: !1,
      canFullscreen: !1
    }, {
      title: d(() => [
        s(r),
        e[0] || (e[0] = f(" 代码生成结果 "))
      ]),
      footer: d(() => [
        s(t, { onClick: o.handleClose }, {
          default: d(() => e[2] || (e[2] = [
            f("关闭")
          ])),
          _: 1
        }, 8, ["onClick"]),
        s(t, {
          type: "primary",
          ghost: "",
          onClick: o.handleView
        }, {
          default: d(() => e[3] || (e[3] = [
            f("在线预览")
          ])),
          _: 1
        }, 8, ["onClick"]),
        s(t, {
          type: "primary",
          onClick: o.onDownloadGenerateCode,
          loading: o.loading
        }, {
          default: d(() => e[4] || (e[4] = [
            f("下载到本地")
          ])),
          _: 1
        }, 8, ["onClick", "loading"])
      ]),
      default: d(() => [
        M("div", {
          style: F(o.divStyle)
        }, [
          M("p", null, [
            (_(!0), v(y, null, B(o.codeList, (w) => (_(), v(y, null, [
              f(N(w), 1),
              e[1] || (e[1] = M("br", null, null, -1))
            ], 64))), 256))
          ])
        ], 4)
      ]),
      _: 1
    }, 8, ["onRegister"]),
    s(a, {
      onRegister: o.registerCodeViewModal,
      onDownload: o.onDownloadGenerateCode,
      onClose: o.handleClose
    }, null, 8, ["onRegister", "onDownload", "onClose"])
  ], 64);
}
const te = /* @__PURE__ */ E(j, [["render", U]]);
export {
  te as default
};
