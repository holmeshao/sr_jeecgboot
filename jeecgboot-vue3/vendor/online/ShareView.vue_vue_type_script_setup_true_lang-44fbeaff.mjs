import { defineComponent as L, computed as s, ref as C, resolveComponent as l, openBlock as u, createElementBlock as k, normalizeClass as q, unref as S, createBlock as w, withCtx as o, createVNode as n, createElementVNode as f, toDisplayString as H, createCommentVNode as d, createTextVNode as B, Fragment as J, withDirectives as K, vShow as P } from "vue";
import { u as Q } from "./shareStore-7de6c7a6.mjs";
import W from "./OnlineAutoList-84475f1b.mjs";
import { useDesign as X } from "/@/hooks/web/useDesign";
import { _ as Y } from "./SingleView.vue_vue_type_style_index_0_lang-e0f2f6c0.mjs";
import { useGo as Z } from "/@/hooks/web/usePage";
import { useAppInject as ee } from "/@/hooks/web/useAppInject";
const te = { key: 0 }, ce = /* @__PURE__ */ L({
  __name: "ShareView",
  props: {
    isUpdate: Boolean,
    isDetail: Boolean
  },
  setup(a) {
    const { prefixCls: U } = X("online-share-view-box"), x = a, { getIsMobile: $ } = ee(), p = Q(), v = s(() => p.getCgformRecord), g = s(() => {
      var e;
      return (e = v.value) == null ? void 0 : e.id;
    }), M = s(() => {
      var e;
      return (e = v.value) == null ? void 0 : e.tableTxt;
    }), r = C(), N = s(() => {
      var e;
      return (e = r.value) == null ? void 0 : e.buttonSwitch;
    }), R = s(() => {
      var e;
      return (e = r.value) == null ? void 0 : e.cgBIBtnMap;
    }), h = s(() => {
      var e;
      return (e = r.value) == null ? void 0 : e.getFormConfirmButtonCfg;
    }), c = C("none"), m = C();
    function A(e) {
      c.value = "ok", m.value = e, x.isUpdate || p.setDataRecord(null);
    }
    function V() {
      c.value = "none", m.value = "";
    }
    const D = Z();
    function b({ key: e }) {
      const t = m.value;
      e === "add" ? _("add", "") : e === "view" ? E(t) : e === "edit" && y(t), m.value = "", c.value = "none";
    }
    function y(e) {
      _("u", e);
    }
    function E(e) {
      _("d", e);
    }
    function _(e, t) {
      var i;
      if (e === "add") {
        D(`/online/cgform/share/${g.value}/${e}`);
        return;
      }
      t || (t = (i = p.getDataRecord) == null ? void 0 : i.id), D(`/online/cgform/share/${g.value}/${e}/${t}`);
    }
    function T() {
      window.close(), setTimeout(() => alert("当前页面无法通过按钮关闭，请手动关闭"), 1e3);
    }
    return (e, t) => {
      const i = l("a-button"), I = l("a-menu-item"), O = l("a-menu"), F = l("a-dropdown"), G = l("a-space-compact"), j = l("a-space"), z = l("a-result");
      return v.value != null ? (u(), k("div", {
        key: 0,
        class: q([S(U)])
      }, [
        c.value !== "none" ? (u(), w(z, {
          key: 0,
          status: "success",
          title: `${a.isUpdate ? "修改" : "新增"}成功`,
          subTitle: "请选择下一步操作"
        }, {
          extra: o(() => [
            n(j, null, {
              default: o(() => [
                n(i, {
                  type: "primary",
                  onClick: V
                }, {
                  default: o(() => [
                    f("span", null, "继续" + H(a.isUpdate ? "修改" : "新增"), 1)
                  ]),
                  _: 1
                }),
                n(G, { block: "" }, {
                  default: o(() => [
                    n(i, {
                      onClick: t[0] || (t[0] = (oe) => b({ key: "view" }))
                    }, {
                      default: o(() => [
                        t[1] || (t[1] = f("span", null, "查看", -1)),
                        a.isUpdate ? d("", !0) : (u(), k("span", te, "新")),
                        t[2] || (t[2] = f("span", null, "数据", -1))
                      ]),
                      _: 1
                    }),
                    n(F, { trigger: ["click", "hover"] }, {
                      overlay: o(() => [
                        n(O, { onClick: b }, {
                          default: o(() => [
                            a.isUpdate ? (u(), w(I, { key: "add" }, {
                              default: o(() => t[3] || (t[3] = [
                                B("添加数据")
                              ])),
                              _: 1
                            })) : d("", !0),
                            a.isUpdate ? d("", !0) : (u(), w(I, { key: "edit" }, {
                              default: o(() => t[4] || (t[4] = [
                                B("编辑新数据")
                              ])),
                              _: 1
                            }))
                          ]),
                          _: 1
                        })
                      ]),
                      default: o(() => [
                        n(i, { preIcon: "ant-design:down" })
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }),
                n(i, { onClick: T }, {
                  default: o(() => t[5] || (t[5] = [
                    B("关闭")
                  ])),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          _: 1
        }, 8, ["title"])) : (u(), k(J, { key: 1 }, [
          n(Y, {
            ID: g.value,
            formName: M.value,
            isUpdate: a.isUpdate,
            isDetail: a.isDetail,
            isMobile: S($),
            buttonSwitch: N.value,
            cgBIBtnMap: R.value,
            confirmBtnCfg: h.value,
            onOk: A,
            onGoEdit: y
          }, null, 8, ["ID", "formName", "isUpdate", "isDetail", "isMobile", "buttonSwitch", "cgBIBtnMap", "confirmBtnCfg"]),
          K(f("div", null, [
            n(W, {
              ref_key: "listRef",
              ref: r
            }, null, 512)
          ], 512), [
            [P, !1]
          ])
        ], 64))
      ], 2)) : d("", !0);
    };
  }
});
export {
  ce as _
};
