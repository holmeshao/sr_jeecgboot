var D = Object.defineProperty, E = Object.defineProperties;
var G = Object.getOwnPropertyDescriptors;
var R = Object.getOwnPropertySymbols;
var Z = Object.prototype.hasOwnProperty, J = Object.prototype.propertyIsEnumerable;
var h = (e, t, n) => t in e ? D(e, t, { enumerable: !0, configurable: !0, writable: !0, value: n }) : e[t] = n, y = (e, t) => {
  for (var n in t || (t = {}))
    Z.call(t, n) && h(e, n, t[n]);
  if (R)
    for (var n of R(t))
      J.call(t, n) && h(e, n, t[n]);
  return e;
}, P = (e, t) => E(e, G(t));
var S = (e, t, n) => new Promise((a, u) => {
  var d = (r) => {
    try {
      m(n.next(r));
    } catch (f) {
      u(f);
    }
  }, v = (r) => {
    try {
      m(n.throw(r));
    } catch (f) {
      u(f);
    }
  }, m = (r) => r.done ? a(r.value) : Promise.resolve(r.value).then(d, v);
  m((n = n.apply(e, t)).next());
});
import { defineComponent as K, computed as I, ref as Q, reactive as W, resolveComponent as M, openBlock as X, createBlock as Y, unref as i, withCtx as s, createVNode as c, createTextVNode as ee, normalizeProps as te, guardReactiveProps as oe, createElementVNode as ne, nextTick as le } from "vue";
import { useListPage as re } from "/@/hooks/system/useListPage";
import { BasicTable as ie, TableAction as ae } from "/@/components/Table";
import { useModalInner as ue, useModal as se, BasicModal as B } from "/@/components/Modal";
import { useForm as ce, BasicForm as de } from "/@/components/Form";
import { defHttp as p } from "/@/utils/http/axios";
const Ce = (e, t) => p.get({ url: "/online/cgform/button/list/" + e, params: t });
function Re(e) {
  return p.delete(
    {
      url: "/online/cgform/button/deleteBatch",
      params: {
        ids: e.join(",")
      }
    },
    { joinParamsToUrl: !0 }
  );
}
const me = (e, t) => t ? p.put({ url: "/online/cgform/button/edit", params: e }) : p.post({ url: "/online/cgform/button/add", params: e }), fe = (e, t) => p.get({ url: "/online/cgform/button/builtInList/" + e, params: t });
function pe({ text: e }) {
  return e ? "ant-design:" + e : "";
}
const be = [
  { title: "按钮编码", align: "center", dataIndex: "buttonCode" },
  { title: "按钮名称", align: "center", dataIndex: "buttonName" },
  {
    title: "按钮样式",
    align: "center",
    dataIndex: "buttonStyle",
    customRender({ text: e, record: t }) {
      if (e === "form") {
        let n = t.optPosition;
        return e + "(" + (n == "2" ? "底部" : "侧面") + ")";
      } else
        return e;
    }
  },
  { title: "按钮类型", align: "center", dataIndex: "optType" },
  { title: "排序", align: "center", dataIndex: "orderNum" },
  {
    title: "按钮图标",
    align: "center",
    dataIndex: "buttonIcon",
    customRender: ({ text: e }) => pe({ text: e })
  },
  { title: "表达式", align: "center", dataIndex: "exp" },
  {
    title: "按钮状态",
    align: "center",
    dataIndex: "buttonStatus",
    customRender({ text: e }) {
      return e == 1 ? "激活" : "未激活";
    }
  }
], ge = ({ redoModalHeight: e }) => [
  {
    label: "按钮编码",
    field: "buttonCode",
    component: "Input",
    required: !0,
    // update-begin--author:liaozhiyang---date:20240521---for：【TV360X-139】按钮编码加上正则校验
    dynamicRules: () => [
      {
        validator: (t, n) => new Promise((a, u) => {
          /^[a-zA-Z_$][a-zA-Z0-9_$]*$/.test(n) ? a() : u("编码只能包含字母、数字、下划线 (_) 和美元符号 ($)且不能以数字开头");
        })
      },
      // update-begin--author:liaozhiyang---date:20240701---for：【TV360X-1693】自定义按钮编码排除sql和java系统内置编码
      {
        validator: (t, n) => new Promise((a, u) => {
          ["add", "edit", "detail", "delete", "batch_delete", "import", "export", "query", "reset", "bpm", "super_query", "form_confirm"].includes(n) ? u("不可使用内置按钮编码，请在“管理内置按钮”中修改内置按钮") : a();
        })
      }
      // update-end--author:liaozhiyang---date:20240701---for：【TV360X-1693】自定义按钮编码排除sql和java系统内置编码
    ]
    // update-end--author:liaozhiyang---date:20240521---for：【TV360X-139】按钮编码加上正则校验
  },
  {
    label: "按钮名称",
    field: "buttonName",
    component: "Input",
    required: !0
  },
  {
    label: "按钮样式",
    field: "buttonStyle",
    component: "Select",
    componentProps: {
      options: [
        { label: "Link", value: "link" },
        { label: "Button", value: "button" },
        { label: "Form", value: "form" }
      ],
      // update-begin--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
      onChange: () => {
        e();
      }
      // update-end--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
    },
    defaultValue: "link"
  },
  {
    label: "按钮位置",
    field: "optPosition",
    component: "Select",
    componentProps: {
      allowClear: !1,
      options: [
        // { label: '侧面', value: '1' },
        { label: "底部", value: "2" }
      ]
    },
    defaultValue: "2",
    show: ({ model: t }) => t.buttonStyle === "form"
  },
  {
    label: "按钮类型",
    field: "optType",
    component: "Select",
    componentProps: {
      allowClear: !1,
      options: [
        { label: "Js", value: "js" },
        { label: "Action", value: "action" }
      ]
    },
    defaultValue: "js"
  },
  {
    label: "排序",
    field: "orderNum",
    component: "InputNumber",
    componentProps: {
      style: "width: 100%"
    }
  },
  {
    label: "按钮图标",
    field: "buttonIcon",
    // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-136】按钮图标改成图标组件选择
    component: "IconPicker",
    componentProps: {
      clearSelect: !0,
      iconPrefixSave: !1
    },
    // update-end--author:liaozhiyang---date:20240528---for：【TV360X-136】按钮图标改成图标组件选择
    ifShow: ({ values: t, model: n }) => t.buttonStyle == "button" || t.buttonStyle == "form"
  },
  {
    label: "表达式",
    field: "exp",
    component: "Input",
    // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-89】自定义按钮样式是link时，展示表达式配置
    ifShow: ({ values: t, model: n }) => t.buttonStyle == "link" ? !0 : (n.exp = "", !1)
    // update-end--author:liaozhiyang---date:20240603---for：【TV360X-89】自定义按钮样式是link时，展示表达式配置
  },
  {
    label: "按钮状态",
    field: "buttonStatus",
    component: "RadioButtonGroup",
    componentProps: {
      options: [
        { label: "激活", value: "1" },
        { label: "未激活", value: "0" }
      ]
    },
    defaultValue: "1"
  }
], _e = { style: { "margin-top": "20px" } }, he = /* @__PURE__ */ K({
  __name: "BuiltInButtonList",
  props: {
    record: {
      type: Object,
      required: !0
    }
  },
  emits: ["register"],
  setup(e, { emit: t }) {
    const n = e, a = I(() => {
      var o;
      return (o = n.record) == null ? void 0 : o.id;
    }), u = I(() => {
      var o;
      return (o = n.record) == null ? void 0 : o.tableType;
    }), d = I(() => u.value === 1), { tableContext: v } = re({
      tableProps: {
        api: (o) => S(this, null, function* () {
          const l = yield fe(a.value, o);
          return d.value ? l.filter((x) => !x.buttonCode.includes("sub_")) : l;
        }),
        columns: be.filter((o) => !["buttonStyle", "optType", "orderNum", "exp"].includes(o.dataIndex)),
        canResize: !1,
        useSearchForm: !1,
        pagination: !1
      }
    }), [m, { reload: r }, { rowSelection: f }] = v, [T, { closeModal: N }] = ue(() => r()), [k, b] = se(), g = Q(!1), _ = W({
      onRegister: k,
      title: I(() => g != null && g.value ? "修改" : "新增"),
      width: 600,
      centered: !0,
      confirmLoading: !1,
      onOk: H,
      onCancel: b.closeModal
    });
    let w = {};
    const F = [
      ...ge({ redoModalHeight: b.redoModalHeight })
    ].filter((o) => ["buttonCode", "buttonName", "buttonIcon", "buttonStatus"].includes(o.field)).map((o) => o.field === "buttonCode" ? P(y({}, o), {
      dynamicRules: () => [],
      dynamicDisabled: () => !0
    }) : o.field === "buttonIcon" ? P(y({}, o), {
      ifShow: () => !0,
      // 以下按钮不允许设置图标
      dynamicDisabled: ({ values: l }) => ["bpm", "edit", "detail", "delete"].includes(l.buttonCode)
    }) : o), [L, { resetFields: V, setFieldsValue: j, validate: A }] = ce({
      // update-begin--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
      schemas: F,
      // update-end--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
      showActionButtonGroup: !1
    });
    function q(o) {
      return S(this, null, function* () {
        var l;
        g.value = o.isUpdate, w = y({}, (l = o.record) != null ? l : {}), b.openModal(), yield le(), yield V(), j(w);
      });
    }
    function z(o) {
      q({ isUpdate: !0, record: o });
    }
    function C() {
      N();
    }
    function H() {
      return S(this, null, function* () {
        try {
          _.confirmLoading = !0;
          let o = yield A();
          o = Object.assign({ cgformHeadId: a.value }, w, o);
          const l = o.id != null;
          yield me(o, l), r(), b.closeModal();
        } finally {
          _.confirmLoading = !1;
        }
      });
    }
    function O(o) {
      return [
        {
          label: "编辑",
          onClick: () => z(o)
        }
      ];
    }
    return (o, l) => {
      const x = M("a-button"), U = M("a-spin");
      return X(), Y(i(B), {
        onRegister: i(T),
        title: "内置按钮",
        width: 1200,
        onCancel: C
      }, {
        footer: s(() => [
          c(x, { onClick: C }, {
            default: s(() => l[0] || (l[0] = [
              ee("关闭")
            ])),
            _: 1
          })
        ]),
        default: s(() => [
          c(i(ie), {
            onRegister: i(m),
            rowSelection: i(f)
          }, {
            action: s(({ record: $ }) => [
              c(i(ae), {
                actions: O($)
              }, null, 8, ["actions"])
            ]),
            _: 1
          }, 8, ["onRegister", "rowSelection"]),
          c(i(B), te(oe(_)), {
            default: s(() => [
              c(U, {
                spinning: _.confirmLoading
              }, {
                default: s(() => [
                  ne("div", _e, [
                    c(i(de), { onRegister: i(L) }, null, 8, ["onRegister"])
                  ])
                ]),
                _: 1
              }, 8, ["spinning"])
            ]),
            _: 1
          }, 16)
        ]),
        _: 1
      }, 8, ["onRegister"]);
    };
  }
});
export {
  he as _,
  be as c,
  Re as d,
  ge as f,
  Ce as l,
  me as s
};
