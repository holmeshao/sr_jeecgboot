import { defineComponent as O, computed as m, ref as I, reactive as C, resolveComponent as d, openBlock as N, createElementBlock as E, Fragment as S, createVNode as a, unref as b, withCtx as t, createElementVNode as r, createTextVNode as v, createBlock as V, createCommentVNode as K, mergeProps as j } from "vue";
import { useModalInner as q, BasicModal as U } from "/@/components/Modal";
import { p as F } from "./utils-9fce7606.mjs";
import { copyTextToClipboard as P } from "/@/hooks/web/useCopyToClipboard";
import { getToken as R } from "/@/utils/auth";
import { useMessage as D } from "/@/hooks/web/useMessage";
import { buildUUID as H } from "/@/utils/uuid";
import { _ as J } from "./index-9e1e1e53.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const z = { class: "content" }, Q = ["href"], G = ["href"], W = ["href"], X = /* @__PURE__ */ O({
  __name: "CgformAddressModal",
  setup(Y) {
    const { createMessage: y } = D(), g = m(() => R()), c = I(["def"]), p = I(!1), i = C({
      title: "",
      content: "",
      copyText: "",
      copyTitle: "",
      formId: ""
    }), [T] = q((o) => {
      Object.assign(i, o, {
        formId: o.record.id
      });
      const e = F(o.record);
      p.value = e.enableExternalLink === 1, p.value ? c.value = ["def", "external"] : c.value = ["def"];
    }), l = m(() => {
      const o = {};
      if (p.value) {
        const e = `/online/cgform/share/${i.formId}`;
        o.add = `${e}/add`, o.edit = `${e}/u/{dataId}`, o.detail = `${e}/d/{dataId}`;
      }
      return {
        list: i.content,
        extLink: o
      };
    });
    function h() {
      const o = `-- 插入菜单
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
VALUES ('${H()}', NULL, '${i.copyTitle}', '${i.copyText}', '1', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 0, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', null, NULL, NULL, 0)
`;
      B(o);
    }
    function B(o) {
      const e = P(o);
      return e ? y.success("复制成功！") : y.error("复制失败！"), e;
    }
    const n = C({
      base: "",
      value: "",
      visible: !1
    }), $ = m(() => n.value ? n.base.replace(/{dataId}/, n.value) + "?token=" + g.value : void 0), w = m(() => ({
      title: "请输入dataId",
      minHeight: 120,
      centered: !0,
      canFullscreen: !1,
      onOk: () => n.visible = !1,
      onCancel: () => n.visible = !1
    }));
    function k(o) {
      n.base = o, n.value = "", n.visible = !0;
    }
    function A() {
      if (!n.value) {
        y.warn("请输入dataId");
        return;
      }
      n.visible = !1;
    }
    return (o, e) => {
      const u = d("a-input"), f = d("a-col"), _ = d("a-row"), x = d("a-button"), L = d("a-collapse-panel"), M = d("a-collapse");
      return N(), E(S, null, [
        a(b(U), {
          onRegister: b(T),
          title: "配置地址",
          width: 750,
          canFullscreen: !1,
          showOkBtn: !1,
          cancelText: "关闭"
        }, {
          default: t(() => [
            r("div", z, [
              a(M, {
                activeKey: c.value,
                "onUpdate:activeKey": e[2] || (e[2] = (s) => c.value = s),
                class: "j-collapse",
                bordered: !1,
                ghost: ""
              }, {
                default: t(() => [
                  a(L, {
                    key: "def",
                    header: "配置地址",
                    class: "j-collapse-panel no-header"
                  }, {
                    default: t(() => [
                      a(_, { style: { "margin-bottom": "8px" } }, {
                        default: t(() => [
                          a(f, { span: 24 }, {
                            default: t(() => [
                              a(u, {
                                readOnly: !0,
                                addonBefore: "数据列表地址",
                                value: l.value.list
                              }, {
                                addonAfter: t(() => [
                                  r("a", {
                                    href: l.value.list,
                                    target: "_blank"
                                  }, "打开", 8, Q)
                                ]),
                                _: 1
                              }, 8, ["value"])
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }),
                      a(x, {
                        class: "copy-sql",
                        type: "primary",
                        size: "small",
                        onClick: h
                      }, {
                        default: t(() => e[5] || (e[5] = [
                          v("复制菜单SQL")
                        ])),
                        _: 1
                      })
                    ]),
                    _: 1
                  }),
                  p.value ? (N(), V(L, {
                    key: "external",
                    header: "外部链接"
                  }, {
                    default: t(() => [
                      a(_, { style: { "margin-bottom": "8px" } }, {
                        default: t(() => [
                          a(f, { span: 24 }, {
                            default: t(() => [
                              a(u, {
                                readOnly: !0,
                                addonBefore: "外部新增地址",
                                value: l.value.extLink.add
                              }, {
                                addonAfter: t(() => [
                                  r("a", {
                                    href: l.value.extLink.add + "?token=" + g.value,
                                    target: "_blank"
                                  }, "打开", 8, G)
                                ]),
                                _: 1
                              }, 8, ["value"])
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }),
                      a(_, { style: { "margin-bottom": "8px" } }, {
                        default: t(() => [
                          a(f, { span: 24 }, {
                            default: t(() => [
                              a(u, {
                                readOnly: !0,
                                addonBefore: "外部修改地址",
                                value: l.value.extLink.edit
                              }, {
                                addonAfter: t(() => [
                                  r("a", {
                                    onClick: e[0] || (e[0] = (s) => k(l.value.extLink.edit))
                                  }, "打开")
                                ]),
                                _: 1
                              }, 8, ["value"])
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }),
                      a(_, { style: { "margin-bottom": "8px" } }, {
                        default: t(() => [
                          a(f, { span: 24 }, {
                            default: t(() => [
                              a(u, {
                                readOnly: !0,
                                addonBefore: "外部详情地址",
                                value: l.value.extLink.detail
                              }, {
                                addonAfter: t(() => [
                                  r("a", {
                                    onClick: e[1] || (e[1] = (s) => k(l.value.extLink.detail))
                                  }, "打开")
                                ]),
                                _: 1
                              }, 8, ["value"])
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }),
                      e[6] || (e[6] = r("div", { style: { "text-align": "right", color: "red" } }, [
                        v("注意："),
                        r("span", { style: { "font-weight": "bold" } }, "{dataId}"),
                        v(" 为数据id")
                      ], -1))
                    ]),
                    _: 1
                  })) : K("", !0)
                ]),
                _: 1
              }, 8, ["activeKey"])
            ])
          ]),
          _: 1
        }, 8, ["onRegister"]),
        a(b(U), j({
          visible: n.visible,
          "onUpdate:visible": e[4] || (e[4] = (s) => n.visible = s)
        }, w.value), {
          footer: t(() => [
            r("a", {
              href: $.value,
              target: "_blank"
            }, [
              a(x, {
                type: "primary",
                onClick: A
              }, {
                default: t(() => e[7] || (e[7] = [
                  v("确定")
                ])),
                _: 1
              })
            ], 8, W)
          ]),
          default: t(() => [
            a(u, {
              placeholder: "请输入dataId",
              value: n.value,
              "onUpdate:value": e[3] || (e[3] = (s) => n.value = s)
            }, null, 8, ["value"])
          ]),
          _: 1
        }, 16, ["visible"])
      ], 64);
    };
  }
});
const pe = /* @__PURE__ */ J(X, [["__scopeId", "data-v-f786f675"]]);
export {
  pe as default
};
