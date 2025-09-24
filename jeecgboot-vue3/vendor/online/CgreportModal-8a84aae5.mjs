var ce = Object.defineProperty, de = Object.defineProperties;
var ue = Object.getOwnPropertyDescriptors;
var z = Object.getOwnPropertySymbols;
var pe = Object.prototype.hasOwnProperty, me = Object.prototype.propertyIsEnumerable;
var A = (t, l, r) => l in t ? ce(t, l, { enumerable: !0, configurable: !0, writable: !0, value: r }) : t[l] = r, D = (t, l) => {
  for (var r in l || (l = {}))
    pe.call(l, r) && A(t, r, l[r]);
  if (z)
    for (var r of z(l))
      me.call(l, r) && A(t, r, l[r]);
  return t;
}, J = (t, l) => de(t, ue(l));
var q = (t, l, r) => new Promise((_, m) => {
  var g = (d) => {
    try {
      c(r.next(d));
    } catch (C) {
      m(C);
    }
  }, S = (d) => {
    try {
      c(r.throw(d));
    } catch (C) {
      m(C);
    }
  }, c = (d) => d.done ? _(d.value) : Promise.resolve(d.value).then(g, S);
  c((r = r.apply(t, l)).next());
});
import { defineComponent as fe, ref as P, reactive as U, unref as h, computed as ge, resolveComponent as v, openBlock as M, createBlock as W, mergeProps as he, withCtx as b, createVNode as y, createElementVNode as w, createTextVNode as T } from "vue";
import { useModalInner as be, BasicModal as ye } from "/@/components/Modal";
import { useForm as xe, BasicForm as _e } from "/@/components/Form/index";
import { useJvxeMethod as Se } from "/@/hooks/system/useJvxeMethods.ts";
import { JVxeTypes as n } from "/@/components/jeecg/JVxeTable/types";
import { duplicateCheckDelay as Ce } from "/@/views/system/user/user.api";
import { defHttp as x } from "/@/utils/http/axios";
import { useMessage as K } from "/@/hooks/web/useMessage";
import { usePermissionStore as ve } from "/@/store/modules/permission";
import { _ as we } from "./index-9e1e1e53.mjs";
const { createConfirm: ke } = K(), Ie = "/online/cgreport/param/listByHeadId", Pe = "/online/cgreport/item/listByHeadId", Ge = (t) => x.get({ url: "/online/cgreport/head/list", params: t }), Xe = (t, l) => x.delete({ url: "/online/cgreport/head/delete", params: t }, { joinParamsToUrl: !0 }).then(() => {
  l();
}), Ye = (t, l) => {
  ke({
    title: "确认删除",
    content: "是否删除选中数据",
    okText: "确认",
    cancelText: "取消",
    iconType: "warning",
    onOk: () => x.delete({ url: "/online/cgreport/head/deleteBatch", data: t }, { joinParamsToUrl: !0 }).then(() => {
      l();
    })
  });
}, Te = (t, l) => l ? x.put({ url: "/online/cgreport/head/editAll", params: t }) : x.post({ url: "/online/cgreport/head/add", params: t }), et = (t) => x.get({ url: "/online/cgreport/api/getParamsInfo/" + t }), Ve = () => x.get({
  url: "/sys/dataSource/options"
  /* getDataSourceList */
}), Le = (t) => x.get({
  url: "/online/cgreport/head/parseSql?" + t
}), $e = ve(), tt = [
  {
    title: "报表名字",
    align: "center",
    dataIndex: "name",
    width: 120
  },
  {
    title: "报表编码",
    align: "center",
    dataIndex: "code",
    width: 120
  },
  {
    title: "报表SQL",
    align: "center",
    dataIndex: "cgrSql",
    width: 360
  },
  {
    title: "数据源",
    align: "center",
    dataIndex: "dbSource",
    customRender: ({ text: t, record: l }) => l.dbSource_dictText ? l.dbSource_dictText : t,
    width: 120
  },
  {
    title: "创建时间",
    align: "center",
    dataIndex: "createTime",
    width: 120
  }
], lt = [
  {
    label: "报表名称",
    field: "name",
    component: "JInput"
  },
  {
    label: "报表编码",
    field: "code",
    component: "JInput"
  }
], qe = /^[a-z|A-Z][a-z|A-Z|\d|_|-]{0,}$/, Ne = [
  {
    label: "",
    field: "id",
    component: "Input",
    show: !1
  },
  {
    label: "报表编码",
    field: "code",
    component: "Input",
    colProps: {
      sm: 24,
      xs: 24,
      md: 12,
      lg: 8,
      xl: 8,
      xxl: 8
    },
    dynamicRules: ({ values: t, model: l }) => [
      {
        required: !0,
        validator: (r, _) => new Promise((m, g) => {
          if (!_)
            return g("请输入报表编码！");
          if (!qe.test(_))
            return g("编码必须以字母开头，可包含数字、下划线、横杠！");
          let S = {
            tableName: "onl_cgreport_head",
            fieldName: "code",
            fieldVal: _,
            dataId: l.id
          };
          Ce(S).then((c) => {
            c.success ? m() : g("报表编码已存在!");
          }).catch((c) => {
            g(c.message || "校验失败");
          });
        })
      }
    ]
  },
  {
    label: "报表名字",
    field: "name",
    component: "Input",
    colProps: {
      sm: 24,
      xs: 24,
      md: 12,
      lg: 8,
      xl: 8,
      xxl: 8
    },
    dynamicRules: () => [{ required: !0, message: "请输入报表名字!" }]
  },
  {
    label: "动态数据源",
    field: "dbSource",
    colProps: {
      sm: 24,
      xs: 24,
      md: 12,
      lg: 8,
      xl: 8,
      xxl: 8
    },
    component: "ApiSelect",
    rules: [{ required: $e.sysSafeMode, message: "请选择数据源！" }],
    componentProps: {
      api: Ve
    }
  },
  /*    {
        label: ' ',
        field: 'line1',
        component: 'Input',
        slot: 'line1',
        colProps: {
            span: 24
        },
        itemProps:{
            labelCol: { xs: 1, sm: 1 },
            wrapperCol: { xs: 23, sm: 23 },
            colon: false
        },
    },*/
  {
    label: "报表SQL",
    field: "cgrSql",
    component: "JCodeEditor",
    rules: [{ required: !0, message: "请填写报表SQL" }],
    // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
    // itemProps: {
    //   labelCol: { xs: 24, sm: 4, md: 2, lg: 2, xl: 3, xxl: 2 },
    //   wrapperCol: { xs: { span: 24 }, sm: { span: 18 }, md: { span: 24 } },
    // },
    // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
    componentProps: {
      height: "200px",
      fullScreen: !0
    },
    colProps: {
      sm: 24,
      xs: 24,
      md: 18,
      lg: 16,
      xl: 16,
      xxl: 16
    }
  },
  {
    label: " ",
    field: "analyseButton",
    component: "Input",
    slot: "analyseButton",
    colProps: {
      xs: 24,
      sm: 24,
      md: 6,
      lg: 8,
      xl: 8,
      xxl: 8
    },
    itemProps: {
      labelCol: { xs: 1, sm: 1 },
      wrapperCol: { xs: 23, sm: 23 },
      colon: !1
    }
  }
], Fe = [
  {
    title: "参数字段",
    key: "paramName",
    type: n.input,
    width: "200px",
    placeholder: "请输入${title}",
    defaultValue: "",
    validateRules: [{ required: !0, message: "${title}不能为空" }]
  },
  {
    title: "参数文本",
    key: "paramTxt",
    type: n.input,
    width: "200px",
    placeholder: "请输入${title}",
    defaultValue: "",
    validateRules: [{ required: !0, message: "${title}不能为空" }]
  },
  {
    title: "参数默认值",
    key: "paramValue",
    type: n.input,
    width: "200px",
    placeholder: "请输入${title}",
    defaultValue: ""
  }
], Re = [
  {
    title: "字段名字",
    key: "fieldName",
    type: n.input,
    minWidth: "150px",
    placeholder: "请输入${title}",
    defaultValue: "",
    validateRules: [{ required: !0, message: "${title}不能为空" }]
  },
  {
    title: "字段文本",
    key: "fieldTxt",
    type: n.input,
    minWidth: "150px",
    placeholder: "请输入${title}",
    defaultValue: "",
    validateRules: [{ required: !0, message: "${title}不能为空" }]
  },
  {
    title: "字段宽度",
    key: "fieldWidth",
    type: n.input,
    minWidth: "100px",
    defaultValue: ""
  },
  {
    title: "字段类型",
    key: "fieldType",
    minWidth: "150px",
    placeholder: "请输入${title}",
    defaultValue: "",
    validateRules: [{ required: !0, message: "${title}不能为空" }],
    type: n.select,
    options: [
      { title: "数值类型", value: "Integer" },
      { title: "字符类型", value: "String" },
      { title: "日期类型", value: "Date" },
      { title: "时间类型", value: "Datetime" },
      { title: "长整型", value: "Long" },
      { title: "图片类型", value: "Image" }
    ]
  },
  {
    title: "是否显示",
    key: "isShow",
    minWidth: "80px",
    align: "center",
    type: n.checkbox,
    customValue: [1, 0],
    defaultChecked: !0
  },
  {
    title: "字段href",
    key: "fieldHref",
    type: n.input,
    minWidth: "150px",
    placeholder: "请输入${title}",
    defaultValue: ""
  },
  {
    title: "查询模式",
    key: "searchMode",
    type: n.select,
    minWidth: "150px",
    placeholder: "请选择${title}",
    options: [
      { title: "单条件查询", value: "single" },
      { title: "范围查询", value: "group" }
    ]
  },
  {
    title: "取值表达式",
    key: "replaceVal",
    type: n.input,
    minWidth: "150px",
    placeholder: "请输入${title}",
    defaultValue: ""
  },
  {
    title: "字典code",
    key: "dictCode",
    type: n.input,
    minWidth: "150px",
    placeholder: "请输入${title}",
    defaultValue: ""
  },
  {
    title: "分组标题",
    key: "groupTitle",
    type: n.input,
    minWidth: "150px",
    placeholder: "请输入${title}",
    defaultValue: ""
  },
  {
    title: "是否查询",
    key: "isSearch",
    type: n.checkbox,
    customValue: ["1", "0"],
    minWidth: "80px",
    align: "center",
    defaultChecked: !1
  },
  {
    title: "是否合计",
    align: "center",
    key: "isTotal",
    type: n.checkbox,
    customValue: ["1", "0"],
    minWidth: "80px",
    defaultChecked: !1
  }
], De = { style: { flex: "1", "text-align": "left" } }, Me = /* @__PURE__ */ fe({
  __name: "CgreportModal",
  emits: ["register", "success"],
  setup(t, { emit: l }) {
    const { createMessage: r } = K(), _ = l, m = P(!0), g = P(!0), S = P(["onlCgreportItem", "onlCgreportParam"]), c = P("onlCgreportItem"), d = P(), C = P(), j = { onlCgreportItem: C, onlCgreportParam: d }, k = U({
      loading: !1,
      dataSource: [],
      columns: Fe
    }), I = U({
      loading: !1,
      dataSource: [],
      columns: Re
    }), [Q, { setProps: H, resetFields: E, setFieldsValue: Z, validate: Be, validateFields: G }] = xe({
      // labelWidth: 150,
      schemas: Ne,
      showActionButtonGroup: !1,
      // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
      labelWidth: 100,
      wrapperCol: null
      // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
    }), [X, { setModalProps: V, closeModal: Y }] = be((e) => q(this, null, function* () {
      var o, a;
      yield re(), V({ confirmLoading: !1, showCancelBtn: e == null ? void 0 : e.showFooter, showOkBtn: e == null ? void 0 : e.showFooter }), m.value = !!(e != null && e.isUpdate), h(m) && (yield Z(D({}, e.record)), B(Ie, { headId: (o = e == null ? void 0 : e.record) == null ? void 0 : o.id }, k), B(Pe, { headId: (a = e == null ? void 0 : e.record) == null ? void 0 : a.id }, I)), H({ disabled: !(e != null && e.showFooter) });
    })), [ee, te, B, le] = Se(
      ne,
      ae,
      j,
      c,
      S
    ), oe = ge(() => h(m) ? "编辑" : "新增");
    function re() {
      return q(this, null, function* () {
        yield E(), c.value = "onlCgreportItem", k.dataSource = [], I.dataSource = [];
      });
    }
    function ae(e) {
      let o = Object.assign({}, e.formValue);
      return J(D({}, o), {
        // 展开
        onlCgreportParamList: e.tablesValue[1].tableData,
        onlCgreportItemList: e.tablesValue[0].tableData
      });
    }
    function ne(e) {
      return q(this, null, function* () {
        try {
          V({ confirmLoading: !0 });
          let o = [], a = [], s = {};
          Object.keys(e).map((i) => {
            i == "onlCgreportItemList" ? a = e[i] : i == "onlCgreportParamList" ? o = e[i] : s[i] = e[i];
          }), yield Te({ head: s, params: o, items: a }, m.value), Y(), _("success");
        } finally {
          V({ confirmLoading: !1 });
        }
      });
    }
    function ie() {
      V({ confirmLoading: !0 }), G(["cgrSql", "dbSource"]).then((e) => {
        let { cgrSql: o, dbSource: a } = e, s = "sql=" + encodeURIComponent(o);
        a && (s += "&dbKey=" + a), Le(s).then((u) => {
          if (u) {
            r.success("解析成功");
            let { fields: i, params: p } = u, f = i.filter(($) => $.fieldName != "__row_number__"), N = C.value.getTableData(), L = O(N, f || [], "fieldName");
            L = L.sort(($, R) => $.orderNum - R.orderNum), I.dataSource = L;
            let se = d.value.getTableData(), F = O(se, p || [], "paramName");
            F = F.sort(($, R) => $.orderNum - R.orderNum), k.dataSource = F;
          }
        });
      }).catch(() => {
      }).finally(() => {
        V({ confirmLoading: !1 });
      });
    }
    function O(e, o, a) {
      if (e.length > 0) {
        let s = [], u = [], i = 1;
        for (let p of o)
          for (let f of e)
            if (f[a] == p[a]) {
              s.push(f), u.push(p[a]), f.orderNum > i && (i = f.orderNum);
              break;
            }
        for (let p of o)
          u.indexOf(p[a]) < 0 && (p.orderNum = ++i, s.push(p));
        return s;
      } else {
        let s = 0;
        for (let u of o)
          u.orderNum || (u.orderNum = ++s);
        return o;
      }
    }
    return (e, o) => {
      const a = v("a-icon"), s = v("a-popover"), u = v("a-button"), i = v("a-divider"), p = v("JVxeTable"), f = v("a-tab-pane"), N = v("a-tabs");
      return M(), W(h(ye), he(e.$attrs, {
        onRegister: h(X),
        title: oe.value,
        width: 1200,
        maskClosable: !1,
        defaultFullscreen: !0,
        confirmLoading: g.value,
        onOk: h(te)
      }), {
        default: b(() => [
          y(h(_e), {
            onRegister: h(Q),
            ref_key: "formRef",
            ref: le
          }, {
            analyseButton: b(() => [
              w("div", De, [
                y(s, {
                  title: "使用指南",
                  trigger: "hover",
                  style: { margin: "0 10px 0 6px" }
                }, {
                  content: b(() => o[1] || (o[1] = [
                    T(" 您可以键入“”作为一个参数，这里abc是参数的名称。例如："),
                    w("br", null, null, -1),
                    T(" select * from table where id = ${abc}。"),
                    w("br", null, null, -1),
                    T(" select * from table where id like concat('%',${abc},'%')。(mysql模糊查询)"),
                    w("br", null, null, -1),
                    T(" select * from table where id like '%'||${abc}||'%'。(oracle模糊查询)"),
                    w("br", null, null, -1),
                    T(" select * from table where id like '%'+${abc}+'%'。(sqlserver模糊查询)"),
                    w("br", null, null, -1),
                    w("span", { style: { color: "red" } }, "注：参数只支持动态报表，popup暂不支持", -1)
                  ])),
                  default: b(() => [
                    y(a, { type: "question-circle" })
                  ]),
                  _: 1
                }),
                y(u, {
                  style: { "margin-left": "10px" },
                  type: "primary",
                  onClick: ie
                }, {
                  default: b(() => o[2] || (o[2] = [
                    T("SQL解析")
                  ])),
                  _: 1
                })
              ])
            ]),
            _: 1
          }, 8, ["onRegister"]),
          y(i, {
            style: { margin: "1px 0" },
            class: "cust-divider"
          }),
          y(N, {
            activeKey: c.value,
            "onUpdate:activeKey": o[0] || (o[0] = (L) => c.value = L),
            animated: "",
            onChange: h(ee)
          }, {
            default: b(() => [
              (M(), W(f, {
                tab: "动态报表配置明细",
                key: S.value[0],
                forceRender: !0
              }, {
                default: b(() => [
                  y(p, {
                    "keep-source": "",
                    dragSort: "",
                    resizable: "",
                    ref_key: "onlCgreportItem",
                    ref: C,
                    loading: I.loading,
                    columns: I.columns,
                    dataSource: I.dataSource,
                    height: 390,
                    rowNumber: !0,
                    rowSelection: !0,
                    dragSortFixed: "none",
                    rowNumberFixed: "none",
                    rowSelectionFixed: "none",
                    toolbar: !0
                  }, null, 8, ["loading", "columns", "dataSource"])
                ]),
                _: 1
              })),
              (M(), W(f, {
                tab: "报表参数",
                key: S.value[1],
                forceRender: !0
              }, {
                default: b(() => [
                  y(p, {
                    "keep-source": "",
                    resizable: "",
                    dragSort: "",
                    ref_key: "onlCgreportParam",
                    ref: d,
                    loading: k.loading,
                    columns: k.columns,
                    dataSource: k.dataSource,
                    height: 390,
                    rowNumber: !0,
                    rowSelection: !0,
                    dragSortFixed: "none",
                    rowNumberFixed: "none",
                    rowSelectionFixed: "none",
                    toolbar: !0
                  }, null, 8, ["loading", "columns", "dataSource"])
                ]),
                _: 1
              }))
            ]),
            _: 1
          }, 8, ["activeKey", "onChange"])
        ]),
        _: 1
      }, 16, ["onRegister", "title", "confirmLoading", "onOk"]);
    };
  }
});
const We = /* @__PURE__ */ we(Me, [["__scopeId", "data-v-e89d83a0"]]), ot = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: We
}, Symbol.toStringTag, { value: "Module" }));
export {
  We as C,
  ot as a,
  Ye as b,
  tt as c,
  Xe as d,
  et as g,
  Ge as l,
  lt as s
};
