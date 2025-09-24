import { defineComponent as D, ref as u, resolveComponent as c, openBlock as H, createBlock as I, withCtx as s, createVNode as f, createTextVNode as h, createElementVNode as T } from "vue";
import { defHttp as M } from "/@/utils/http/axios";
import { BasicTable as $ } from "/@/components/Table";
import { BasicModal as A, useModalInner as P } from "/@/components/Modal";
import { useListTable as q } from "/@/hooks/system/useListPage";
import { useMessage as O } from "/@/hooks/web/useMessage";
import { useDesign as V } from "/@/hooks/web/useDesign";
import { _ as j } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const x = D({
  name: "TransDb2Online",
  components: { BasicModal: A, BasicTable: $ },
  emits: ["success", "register"],
  setup(o, { emit: n }) {
    const { createMessage: p } = O(), l = u("暂无数据"), m = u(!1), r = u(!1), a = u([]), t = u([]), { prefixCls: d } = V("online-db-import-form-modal"), g = d, C = document.documentElement.clientHeight, b = C - 180, _ = b > 690 ? 690 : b, [L, { setPagination: y, getForm: B }, { rowSelection: F, selectedRowKeys: i }] = q({
      bordered: !0,
      columns: [{ title: "表名", align: "left", dataIndex: "id" }],
      dataSource: t,
      maxHeight: C - 400,
      locale: { emptyText: l },
      pagination: {
        showQuickJumper: !1,
        showSizeChanger: !1
      },
      clickToRowSelect: !0,
      showIndexColumn: !0,
      showActionColumn: !1,
      formConfig: {
        schemas: [
          {
            label: "表名",
            field: "tableName",
            component: "Input",
            componentProps: {
              style: { width: "100%" },
              placeholder: "请输入表名以模糊筛选",
              onChange: (e) => N(e.target.value)
            },
            disabledLabelWidth: !0,
            itemProps: {
              labelCol: { sm: 0, md: 0 },
              wrapperCol: { sm: 24, md: 20 }
            }
          }
        ],
        baseColProps: { xs: 24, sm: 24, md: 24, lg: 24, xl: 24, xxl: 24 },
        showActionButtonGroup: !1
      }
    }), [E, { closeModal: v }] = P(() => {
      var e;
      (e = B()) == null || e.resetFields(), r.value = !1, l.value = "暂无数据", i.value = [], w();
    });
    function w() {
      return m.value = !0, M.get(
        {
          url: "/online/cgform/head/queryTables"
          /* query */
        },
        {
          errorMessageMode: "none"
        }
      ).then(
        (e) => (t.value = e, a.value = [...e], e),
        (e) => {
          e.message == "noadminauth" && (l.value = "非admin用户无权限操作！", p.warn(l.value)), t.value = [], a.value = [];
        }
      ).finally(() => {
        m.value = !1;
      });
    }
    function N(e) {
      a.value.length !== 0 && (e ? (t.value = a.value.filter((S) => S.id.toLowerCase().includes(e.toLowerCase())), l.value = t.value.length === 0 ? "无筛选结果" : "暂无数据") : t.value = [...a.value], y({ current: 1 }));
    }
    function k() {
      v();
    }
    function R() {
      if (!i.value || i.value.length == 0) {
        p.warning("请选择一张表");
        return;
      } else {
        r.value = !0;
        let e = i.value.join(",");
        M.post({ url: "/online/cgform/head/transTables/" + e }, { errorMessageMode: "modal" }).then(() => {
          v();
        }).finally(() => {
          r.value = !1, n("success");
        });
      }
    }
    return {
      emptyText: l,
      confirmLoading: m,
      btnLoading: r,
      metaSource: a,
      handleTrans: R,
      handleCancel: k,
      queryTables: w,
      registerModal: E,
      registerTable: L,
      rowSelection: F,
      selectedRowKeys: i,
      wrapClassName: g,
      modalHeight: _
    };
  }
});
function z(o, n, p, l, m, r) {
  const a = c("BasicTable"), t = c("a-spin"), d = c("a-button"), g = c("BasicModal");
  return H(), I(g, {
    onRegister: o.registerModal,
    height: o.modalHeight,
    width: 500,
    title: "从数据库导入表单",
    confirmLoading: o.confirmLoading,
    onCancel: o.handleCancel,
    wrapClassName: o.wrapClassName
  }, {
    footer: s(() => [
      f(d, { onClick: o.handleCancel }, {
        default: s(() => n[1] || (n[1] = [
          h("关闭")
        ])),
        _: 1
      }, 8, ["onClick"]),
      f(d, {
        onClick: o.handleTrans,
        type: "primary",
        preIcon: "ant-design:swap",
        loading: o.confirmLoading || o.btnLoading
      }, {
        default: s(() => n[2] || (n[2] = [
          h(" 生成表单 ")
        ])),
        _: 1
      }, 8, ["onClick", "loading"])
    ]),
    default: s(() => [
      f(t, { spinning: o.confirmLoading }, {
        default: s(() => [
          f(a, {
            onRegister: o.registerTable,
            rowSelection: o.rowSelection,
            onTableRedo: o.queryTables
          }, {
            tableTitle: s(() => n[0] || (n[0] = [
              T("div", null, [
                h(" 注：导入表会排除配置前缀表 "),
                T("a", {
                  href: "http://doc.jeecg.com/2043924",
                  target: "_blank"
                }, " 参考文档")
              ], -1)
            ])),
            _: 1
          }, 8, ["onRegister", "rowSelection", "onTableRedo"])
        ]),
        _: 1
      }, 8, ["spinning"])
    ]),
    _: 1
  }, 8, ["onRegister", "height", "confirmLoading", "onCancel", "wrapClassName"]);
}
const oe = /* @__PURE__ */ j(x, [["render", z]]);
export {
  oe as default
};
