var F = (o, r, u) => new Promise((w, g) => {
  var v = (i) => {
    try {
      d(u.next(i));
    } catch (s) {
      g(s);
    }
  }, y = (i) => {
    try {
      d(u.throw(i));
    } catch (s) {
      g(s);
    }
  }, d = (i) => i.done ? w(i.value) : Promise.resolve(i.value).then(v, y);
  d((u = u.apply(o, r)).next());
});
import { defineComponent as P, ref as h, reactive as H, resolveComponent as p, openBlock as k, createBlock as x, withCtx as m, createVNode as C, createTextVNode as V, createElementVNode as W, normalizeStyle as j, createCommentVNode as q } from "vue";
import { defHttp as z } from "/@/utils/http/axios";
import { BasicModal as U, useModalInner as Y } from "/@/components/Modal";
import { InfoCircleTwoTone as Q } from "@ant-design/icons-vue";
import { message as $ } from "ant-design-vue";
import { JCodeEditor as X } from "/@/components/Form";
import { useDesign as Z } from "/@/hooks/web/useDesign";
import { _ as ee } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const te = P({
  name: "CodeFileViewModal",
  components: {
    BasicModal: U,
    InfoCircleTwoTone: Q,
    JCodeEditor: X
  },
  emits: ["download", "register", "close"],
  setup(o, { emit: r }) {
    const u = h([]), w = h(""), g = h([]), v = h(!1), y = window.innerHeight - 142, d = h("java"), i = h("");
    let s = H({});
    const [D, { closeModal: M }] = Y((t) => F(this, null, function* () {
      s = H({}), i.value = "", u.value = t.codeList, w.value = t.pathKey, T(), v.value = !0;
    })), { prefixCls: _ } = Z("online-codeFileViewModal");
    function T() {
      let t = G(), l = t[0];
      N(l, t);
      let n = [];
      const e = function(c) {
        if (c.children) {
          let a = c.children;
          a.length == 1 ? e(a[0]) : a.length > 1 && n.push(c);
        }
      };
      e(l), g.value = n, setTimeout(() => {
        b(l);
      }, 300);
    }
    function b(t) {
      return F(this, null, function* () {
        const l = function(e) {
          if (e.isLeaf === !0)
            return e;
          if (e.children)
            return l(e.children[0]);
        };
        let n = l(t);
        if (n && n.isLeaf === !0) {
          let e = n.path;
          s[e] || (yield K(e)), d.value = A(e), i.value = s[e];
        }
      });
    }
    function N(t, l) {
      for (let n of l)
        t.key == n.pid && (t.children || (t.children = []), t.children.push(n), N(n, l));
    }
    function R(t, l) {
      let n = 0, e = "";
      for (; n <= l; )
        e += t[n], n++;
      return e;
    }
    function G() {
      let t = [], l = [], n = u.value;
      for (let e of n) {
        let c = e.replace(new RegExp("\\\\", "g"), "/").replace("生成成功：", "").trim();
        if (c) {
          let a = c.split("/");
          for (let f = 0; f < a.length; f++) {
            let B = a[f], L = R(a, f);
            if (B) {
              let E = {
                title: B,
                key: L
              };
              if (B != 0) {
                let S = R(a, f - 1);
                S && (E.pid = S);
              }
              f == a.length - 1 && (E.isLeaf = !0, E.path = c), (l.indexOf(L) < 0 || f == a.length - 1) && (t.push(E), l.push(L));
            }
          }
        }
      }
      return t;
    }
    function I() {
      M(), r("close");
    }
    function J() {
      r("download");
    }
    function A(t) {
      return t.endsWith("xml") ? "application/xml" : t.endsWith("sql") ? "text/x-sql" : t.endsWith("vue") ? "text/x-vue" : t.endsWith("ts") ? "text/typescript" : "text/x-java";
    }
    function O(t, l) {
      return F(this, null, function* () {
        let n = l.node.dataRef;
        if (n.isLeaf) {
          let e = n.path;
          s[e] || (yield K(e)), d.value = A(e), i.value = s[e];
        }
      });
    }
    function K(t) {
      return new Promise((l) => {
        let n = {
          path: encodeURI(t),
          pathKey: w.value
        };
        z.get({ url: "/online/cgform/api/codeView", params: n }, { isTransformResponse: !1 }).then((e) => {
          if (!e || e.size === 0) {
            $.warning("文件下载失败");
            return;
          } else if (e.message) {
            $.warning(e.message);
            return;
          }
          let c = new Blob([e]), a = new FileReader();
          a.readAsText(c, "utf8"), a.onload = function() {
            let f = this.result;
            s[t] = f, l(1);
          };
        });
      });
    }
    return {
      registerModal: D,
      codeList: u,
      onDownloadGenerateCode: J,
      handleClose: I,
      treeData: g,
      showCodeContent: O,
      activeCodeContent: i,
      expandStatus: v,
      height: y,
      language: d,
      prefixCls: _,
      modalHeight: 1e3
    };
  }
});
function ne(o, r, u, w, g, v) {
  const y = p("info-circle-two-tone"), d = p("a-directory-tree"), i = p("a-col"), s = p("JCodeEditor"), D = p("a-empty"), M = p("a-row"), _ = p("a-button"), T = p("BasicModal");
  return k(), x(T, {
    height: o.modalHeight,
    onRegister: o.registerModal,
    okText: "",
    cancelText: "关闭",
    width: 1200,
    defaultFullscreen: !0,
    canFullscreen: !1,
    onOk: o.onDownloadGenerateCode,
    wrapClassName: o.prefixCls
  }, {
    title: m(() => [
      C(y),
      r[1] || (r[1] = V(" 代码在线预览 "))
    ]),
    footer: m(() => [
      C(_, { onClick: o.handleClose }, {
        default: m(() => r[2] || (r[2] = [
          V("关闭")
        ])),
        _: 1
      }, 8, ["onClick"]),
      C(_, {
        type: "primary",
        onClick: o.onDownloadGenerateCode
      }, {
        default: m(() => r[3] || (r[3] = [
          V("下载到本地")
        ])),
        _: 1
      }, 8, ["onClick"])
    ]),
    default: m(() => [
      W("div", null, [
        C(M, null, {
          default: m(() => [
            C(i, {
              span: 6,
              gutter: 3,
              style: { "border-right": "1px solid #eee" }
            }, {
              default: m(() => [
                W("div", {
                  style: j({ height: o.height + "px", overflowY: "auto" })
                }, [
                  o.treeData.length ? (k(), x(d, {
                    key: 0,
                    defaultExpandAll: !0,
                    "tree-data": o.treeData,
                    onSelect: o.showCodeContent
                  }, null, 8, ["tree-data", "onSelect"])) : q("", !0)
                ], 4)
              ]),
              _: 1
            }),
            C(i, {
              span: 18,
              gutter: 3
            }, {
              default: m(() => [
                o.activeCodeContent ? (k(), x(s, {
                  key: 0,
                  value: o.activeCodeContent,
                  "onUpdate:value": r[0] || (r[0] = (b) => o.activeCodeContent = b),
                  theme: "idea",
                  language: o.language,
                  fullScreen: !1,
                  lineNumbers: !0,
                  height: o.height + "px",
                  disabled: !0,
                  "language-change": !0
                }, null, 8, ["value", "language", "height"])) : (k(), x(D, {
                  key: 1,
                  style: { "margin-top": "50px" },
                  description: "请选择左侧文件，显示详细代码"
                }))
              ]),
              _: 1
            })
          ]),
          _: 1
        })
      ])
    ]),
    _: 1
  }, 8, ["height", "onRegister", "onOk", "wrapClassName"]);
}
const ge = /* @__PURE__ */ ee(te, [["render", ne]]);
export {
  ge as default
};
