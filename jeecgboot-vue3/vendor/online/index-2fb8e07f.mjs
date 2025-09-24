var L = (U, g, c) => new Promise((b, u) => {
  var k = (r) => {
    try {
      n(c.next(r));
    } catch (m) {
      u(m);
    }
  }, h = (r) => {
    try {
      n(c.throw(r));
    } catch (m) {
      u(m);
    }
  }, n = (r) => r.done ? b(r.value) : Promise.resolve(r.value).then(k, h);
  n((c = c.apply(U, g)).next());
});
import { defineComponent as O, resolveComponent as d, openBlock as S, createElementBlock as E, createVNode as a, unref as p, mergeProps as V, withCtx as l, createTextVNode as C, createBlock as q, createCommentVNode as F } from "vue";
import { BasicTable as K, TableAction as Q } from "/@/components/Table";
import { useListPage as H } from "/@/hooks/system/useListPage";
import { l as j, c as z, s as G, C as J, b as W, g as X, d as Y } from "./CgreportModal-8a84aae5.mjs";
import { useModal as Z } from "/@/components/Modal";
import R from "clipboard";
import { useRouter as ee } from "vue-router";
import { buildUUID as te } from "/@/utils/uuid";
import "/@/components/Form/index";
import "/@/hooks/system/useJvxeMethods.ts";
import "/@/components/jeecg/JVxeTable/types";
import "/@/views/system/user/user.api";
import "/@/utils/http/axios";
import "/@/hooks/web/useMessage";
import "/@/store/modules/permission";
import "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
const ye = /* @__PURE__ */ O({
  __name: "index",
  setup(U, { expose: g }) {
    let c = ee();
    const [b, { openModal: u }] = Z(), {
      prefixCls: k,
      tableContext: h,
      createMessage: n,
      createConfirm: r
    } = H({
      designScope: "online-cgreport-list",
      pagination: !0,
      tableProps: {
        title: "Online报表",
        api: j,
        rowKey: "id",
        columns: z,
        formConfig: {
          autoSubmitOnEnter: !0,
          showAdvancedButton: !0,
          schemas: G
        }
      }
    }), [m, { reload: w }, { rowSelection: T, selectedRowKeys: y }] = h;
    function x() {
      u(!0, {
        isUpdate: !1,
        showFooter: !0
      });
    }
    function P(t) {
      u(!0, {
        record: t,
        isUpdate: !0,
        showFooter: !0
      });
    }
    function $(t) {
      return L(this, null, function* () {
        yield Y({ id: t.id }, w);
      });
    }
    function A() {
      return L(this, null, function* () {
        yield W({ ids: y.value }, () => {
          w(), y.value = [];
        });
      });
    }
    function D() {
      w();
    }
    function B(t) {
      return [
        {
          label: "编辑",
          onClick: P.bind(null, t)
        }
      ];
    }
    function v(t) {
      return [
        {
          label: "功能测试",
          class: ["low-app-hide"],
          onClick: () => I(t.id)
        },
        {
          label: "配置地址",
          class: ["low-app-hide"],
          onClick: () => M(t)
        },
        {
          label: "删除",
          popConfirm: {
            title: "是否确认删除",
            confirm: $.bind(null, t)
          }
        }
      ];
    }
    function I(t) {
      c.push({ path: "/online/cgreport/" + t });
    }
    function M(t) {
      let i = t.id, s = `/online/cgreport/${i}`, f = `INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) 
                         VALUES ('${te()}', NULL, '${t.name}', '${s}', '1', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 0, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', null, NULL, NULL, 0)`;
      X(i).then((o) => {
        let e = "";
        if (o && o.length > 0)
          for (let _ of o)
            e += _.paramName + "=${" + _.paramName + "}&";
        e.length > 0 && (e = e.substring(0, e.length - 1), s += "?" + e);
      }).catch(() => {
        n.warning("获取参数失败!");
      }).finally(() => {
        r({
          iconType: "info",
          width: 500,
          title: `菜单链接【${t.name}】`,
          content: s,
          closable: !0,
          maskClosable: !0,
          cancelText: "复制SQL",
          okText: "复制URL",
          cancelButtonProps: {
            class: "copy-this-sql",
            "data-clipboard-text": f
          },
          okButtonProps: {
            class: "copy-this-text",
            "data-clipboard-text": s
          },
          onOk() {
            return new Promise((o) => {
              const e = new R(".copy-this-text");
              e.on("success", () => {
                e.destroy(), n.success("复制URL成功"), o();
              }), e.on("error", () => {
                n.error("该浏览器不支持自动复制"), e.destroy(), o();
              });
            });
          },
          onCancel() {
            return new Promise((o) => {
              const e = new R(".copy-this-sql");
              e.on("success", () => {
                e.destroy(), n.success("复制插入菜单SQL成功"), o();
              }), e.on("error", () => {
                n.error("该浏览器不支持自动复制"), e.destroy(), o();
              });
            });
          }
        });
      });
    }
    return g({
      handleAdd: x
    }), (t, i) => {
      const s = d("a-button"), f = d("Icon"), o = d("a-menu-item"), e = d("a-menu"), _ = d("a-dropdown");
      return S(), E("div", null, [
        a(p(K), V({
          onRegister: p(m),
          rowSelection: p(T)
        }, t.$attrs), {
          tableTitle: l(() => [
            a(s, {
              preIcon: "ant-design:plus-outlined",
              type: "primary",
              onClick: x,
              style: { "margin-right": "5px" }
            }, {
              default: l(() => i[0] || (i[0] = [
                C("录入")
              ])),
              _: 1
            }),
            p(y).length > 0 ? (S(), q(_, { key: 0 }, {
              overlay: l(() => [
                a(e, null, {
                  default: l(() => [
                    a(o, {
                      key: "1",
                      onClick: A
                    }, {
                      default: l(() => [
                        a(f, { icon: "ant-design:delete-outlined" }),
                        i[1] || (i[1] = C(" 删除 "))
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                })
              ]),
              default: l(() => [
                a(s, null, {
                  default: l(() => [
                    i[2] || (i[2] = C("批量操作 ")),
                    a(f, { icon: "mdi:chevron-down" })
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })) : F("", !0)
          ]),
          action: l(({ record: N }) => [
            a(p(Q), {
              actions: B(N),
              dropDownActions: v(N)
            }, null, 8, ["actions", "dropDownActions"])
          ]),
          _: 1
        }, 16, ["onRegister", "rowSelection"]),
        a(J, {
          onRegister: p(b),
          onSuccess: D
        }, null, 8, ["onRegister"])
      ]);
    };
  }
});
export {
  ye as default
};
