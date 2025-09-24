var h = (o, i, t) => new Promise((m, d) => {
  var w = (r) => {
    try {
      l(t.next(r));
    } catch (u) {
      d(u);
    }
  }, M = (r) => {
    try {
      l(t.throw(r));
    } catch (u) {
      d(u);
    }
  }, l = (r) => r.done ? m(r.value) : Promise.resolve(r.value).then(w, M);
  l((t = t.apply(o, i)).next());
});
import { createVNode as a, createTextVNode as p, ref as v, h as b, nextTick as Me } from "vue";
import { useRouter as Se } from "vue-router";
import { Space as De, Button as D, RadioGroup as Fe, Radio as A, Input as ke } from "ant-design-vue";
import { useModal as c } from "/@/components/Modal";
import { useDrawer as Ee } from "/@/components/Drawer";
import { useListPage as xe } from "/@/hooks/system/useListPage";
import { l as Le, d as Ae, a as Be, b as $e, c as Re, e as Oe, f as Ie, g as Je } from "./CgformModal-c4a4e0c2.mjs";
import { isArray as Ne } from "/@/utils/is";
import { u as Ue } from "./cgformState-d9f8ec42.mjs";
import { useMessage as Ye } from "/@/hooks/web/useMessage";
var $ = /* @__PURE__ */ ((o) => (o[o.normal = 0] = "normal", o[o.copy = 1] = "copy", o))($ || {});
const {
  createConfirm: Ge
} = Ye();
function B(o, i) {
  const {
    destroy: t
  } = Ge({
    title: "请确认删除表单",
    content: () => a("p", null, [a("br", null, null), a("span", {
      style: "cursor: text;"
    }, [a("a", {
      style: "pointer-events: none;font-weight: bold; "
    }, [p("仅移除：")])]), a("span", null, [p("只删除表单配置，数据库表保留！")]), a("br", null, null), a("span", {
      style: "color: #ee0000;font-weight: bold; "
    }, [p("删除：")]), a("span", null, [p("同时删除数据库表，不可恢复！")]), a("br", null, null), a("br", null, null), a("span", null, [p("此操作很敏感，请谨慎操作..")])]),
    iconType: "warning",
    closable: !0,
    maskClosable: !0,
    footer: () => a("div", {
      style: "text-align: right;"
    }, [a(De, {
      align: "center"
    }, {
      default: () => [a(D, {
        onClick: () => t()
      }, {
        default: () => [p("取消")]
      }), a(D, {
        type: "primary",
        onClick: m(i)
      }, {
        default: () => [p("仅移除")]
      }), a(D, {
        type: "primary",
        danger: !0,
        onClick: m(o)
      }, {
        default: () => [p("删除")]
      })]
    })])
  });
  function m(d) {
    return () => h(this, null, function* () {
      yield d(), t();
    });
  }
}
function en(o) {
  const i = o.pageType === $.normal, t = Se(), m = Ue(), d = v(), w = xe({
    designScope: o.designScope,
    tableProps: {
      api: Le,
      columns: o.columns,
      formConfig: {
        //labelWidth: 200,
        schemas: o.formSchemas
      },
      beforeFetch: (e) => {
        let n = i ? 0 : 1, s = i ? void 0 : t.currentRoute.value.params.code;
        return Ne(e.tableType_MultiString) && (e.tableType_MultiString = e.tableType_MultiString.join(",")), Object.assign(e, { copyType: n, physicId: s });
      }
    }
  }), { tableContext: M, createMessage: l, createConfirm: r } = w, [, { reload: u, setLoading: T }, { selectedRowKeys: F, selectedRows: f }] = M, [R, k] = c(), [O, I] = c(), [J, N] = c(), [U, Y] = c(), [G, j] = c(), [_, q] = c(), [P, V] = c(), [K, z] = c(), [H, Q] = Ee(), [W, X] = c();
  function Z() {
    k.openModal(!0, { isUpdate: !1 });
  }
  function ee() {
    N.openModal(!0);
  }
  function ne() {
    u();
  }
  let g = null;
  function te(e) {
    g = e, k.openModal(!0, { isUpdate: !0, record: e });
  }
  function ae() {
    g != null && g.id && (m.addChangedTable(g.id), g = null), u();
  }
  function le(e) {
    return h(this, null, function* () {
      yield Re(e), u();
    });
  }
  function oe(e) {
    return h(this, null, function* () {
      yield Oe(e), u();
    });
  }
  function ie(e) {
    return B(() => le(e.id), () => oe(e.id));
  }
  function ue() {
    let e = F.value;
    if (e.length <= 0) {
      l.warning("请先选择一条记录！");
      return;
    }
    B(
      () => E(Je, e, !0),
      () => E(Ie, e, !0)
    );
  }
  function E(e, n, s = !1) {
    return h(this, null, function* () {
      try {
        T(!0);
        const S = yield e(n);
        return u(), s && (F.value = []), S;
      } finally {
        T(!1);
      }
      return Promise.reject();
    });
  }
  function se() {
    C(([e]) => j.openModal(!0, { row: e }));
  }
  function re() {
    C(([e]) => q.openModal(!0, { row: e }));
  }
  function ce() {
    C(([e]) => V.openModal(!0, { row: e }));
  }
  function fe() {
    C(([e]) => z.openModal(!0, { row: e }));
  }
  function pe() {
    I.openModal(!0, {});
  }
  function C(e, n = 1, s = 1) {
    f.value.length < n ? l.warning(`请先至少选中 ${n} 条记录`) : f.value.length > s ? l.warning(`最多只能选中 ${n} 条记录`) : e(f.value);
  }
  function de() {
    if (f.value.length === 0)
      l.warning("请先选中一条记录");
    else if (f.value.length > 1)
      l.warning("代码生成只能选中一条记录");
    else {
      let e = f.value[0];
      e ? e.isDbSynch != "Y" ? l.warning("请先同步数据库！") : e.tableType == 3 ? l.warning("请选中该表对应的主表") : Y.openModal(!0, { code: e.id }) : l.warning("请选中当前页的数据！");
    }
  }
  function me(e) {
    if (e.isTree == "Y")
      t.push({ path: "/online/cgformTreeList/" + e.id });
    else
      switch (e.themeTemplate) {
        case "erp":
          t.push({ path: "/online/cgformErpList/" + e.id });
          break;
        case "tab":
          t.push({ path: "/online/cgformTabList/" + e.id });
          break;
        case "innerTable":
          t.push({ path: "/online/cgformInnerTableList/" + e.id });
          break;
        default:
          t.push({ path: "/online/cgformList/" + e.id });
          break;
      }
  }
  function ge(e) {
    const n = v("normal"), s = v(!1), S = r({
      iconType: "info",
      title: "同步数据库",
      content: () => b(
        "div",
        {
          style: "margin: 20px 0;"
        },
        b(
          Fe,
          {
            value: n.value,
            disabled: s.value,
            "onUpdate:value": (y) => n.value = y
          },
          () => [b(A, { value: "normal" }, () => "普通同步（保留表数据）"), b(A, { value: "force" }, () => "强制同步（删除表，重新生成）")]
        )
      ),
      maskClosable: !0,
      okText: "开始同步",
      onOk() {
        return h(this, null, function* () {
          s.value = !0, S.update({
            maskClosable: !1,
            keyboard: !1,
            okText: "同步中…",
            okButtonProps: { loading: s.value },
            cancelButtonProps: { disabled: s.value }
          });
          try {
            yield Be(e.id, n.value);
          } catch (y) {
          } finally {
            yield u(), Me(() => {
              f.value.length && f.value.forEach((y) => {
                var L;
                const x = ((L = d.value.getDataSource()) != null ? L : []).find((ve) => ve.id === y.id);
                x && Object.assign(y, x);
              });
            });
          }
        });
      }
    });
  }
  const [he, be] = c();
  function ye(e) {
    let n;
    e.themeTemplate === "erp" ? n = `/online/cgformErpList/${e.id}` : e.themeTemplate === "innerTable" ? n = `/online/cgformInnerTableList/${e.id}` : e.themeTemplate === "tab" ? n = `/online/cgformTabList/${e.id}` : e.isTree == "Y" ? n = `/online/cgformTreeList/${e.id}` : n = `/online/cgformList/${e.id}`, be.openModal(!0, {
      title: `菜单链接【${e.tableTxt}】`,
      content: n,
      copyText: n,
      copyTitle: `${e.tableTxt}`,
      record: e
    });
  }
  function we(e) {
    const n = v(e.tableName + "_copy");
    r({
      title: "复制表",
      content: () => b(
        "div",
        {
          style: "margin: 20px 0;"
        },
        [
          "请输入新表名：",
          b(ke, {
            value: n.value,
            "onUpdate:value": (s) => n.value = s
          })
        ]
      ),
      iconType: "info",
      closable: !0,
      okText: "复制",
      onOk() {
        n.value ? n.value === e.tableName ? l.warning("新表名和旧表名不能一致") : $e(e.id, n.value).then(u) : l.warning("请输入新表名");
      }
    });
  }
  function Te(e) {
    return [
      {
        label: "编辑",
        onClick: () => te(e)
      }
    ];
  }
  function Ce(e) {
    return [
      {
        label: "同步数据库",
        onClick: () => ge(e),
        ifShow: () => i && e.isDbSynch != "Y"
      },
      {
        // TODO 功能测试
        label: "功能测试",
        class: ["low-app-hide"],
        onClick: () => me(e),
        ifShow: () => i ? e.isDbSynch == "Y" && e.tableType !== 3 : !0
      },
      {
        label: "配置地址",
        class: ["low-app-hide"],
        onClick: () => ye(e),
        ifShow: () => i ? e.isDbSynch == "Y" && e.tableType !== 3 : !0
      },
      {
        label: "权限控制",
        onClick: () => Q.openDrawer(!0, { cgformId: e.id, tableType: e.tableType })
      },
      {
        label: "角色授权",
        onClick: () => X.openModal(!0, { cgformId: e.id })
      },
      {
        label: "视图管理",
        class: ["low-app-hide"],
        onClick: () => t.push(`/online/copyform/${e.id}`),
        ifShow: () => i && e.hascopy == 1
      },
      {
        label: "生成视图",
        class: ["low-app-hide"],
        // @ts-ignore
        popConfirm: {
          title: "确定生成视图吗？",
          placement: "left",
          confirm: () => {
            T(!0), Ae(e.id).then(() => {
              l.success("已成功生成视图");
            }).finally(() => {
              T(!1), u();
            });
          }
        },
        ifShow: () => i
      },
      {
        label: "复制表",
        onClick: () => we(e),
        ifShow: () => i
      },
      // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8485】online删除提示优化
      {
        label: "删除",
        onClick: () => ie(e),
        ifShow: () => i
      }
      // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8485】online删除提示优化
    ];
  }
  return {
    router: t,
    pageContext: w,
    onAdd: Z,
    onAiCreateTable: ee,
    onSuccess: ae,
    onDeleteBatch: ue,
    onImportDbTable: pe,
    onGenerateCode: de,
    onShowCustomButton: se,
    onShowEnhanceJs: re,
    onShowEnhanceSql: ce,
    onShowEnhanceJava: fe,
    onCreateAiTable: ne,
    getTableAction: Te,
    getDropDownAction: Ce,
    registerCustomButtonModal: G,
    registerEnhanceJsModal: _,
    registerEnhanceSqlModal: P,
    registerEnhanceJavaModal: K,
    registerAuthManagerDrawer: H,
    registerAuthSetterModal: W,
    registerCgformModal: R,
    registerDbToOnlineModal: O,
    registerCodeGeneratorModal: U,
    registerAiToOnlineModal: J,
    registerAddressModal: he,
    tableRef: d
  };
}
export {
  $ as C,
  en as u
};
