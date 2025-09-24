var C = (e, t, h) => new Promise((x, f) => {
  var l = (a) => {
    try {
      c(h.next(a));
    } catch (u) {
      f(u);
    }
  }, r = (a) => {
    try {
      c(h.throw(a));
    } catch (u) {
      f(u);
    }
  }, c = (a) => a.done ? x(a.value) : Promise.resolve(a.value).then(l, r);
  c((h = h.apply(e, t)).next());
});
import { defineComponent as q, ref as g, reactive as M, resolveComponent as m, openBlock as w, createBlock as E, withCtx as o, createVNode as n, createTextVNode as F, createCommentVNode as A, createElementVNode as b } from "vue";
import { BasicModal as W, useModalInner as Q, useModal as z } from "/@/components/Modal";
import { JCodeEditor as L } from "/@/components/Form";
import { u as X } from "./useOnlineTest-e4bd8be3.mjs";
import { E as Y, u as Z } from "./EnhanceJsHistory-8ddb0657.mjs";
import { g as ee, s as te } from "./enhance.api-138e6826.mjs";
import { useMessage as se } from "/@/hooks/web/useMessage";
import { QuestionCircleOutlined as oe } from "@ant-design/icons-vue";
import { Tooltip as le } from "ant-design-vue";
import { _ as ae } from "./index-9e1e1e53.mjs";
import "/@/utils/dateUtil";
import "/@/store";
import "pinia";
import "/@/utils/cache";
import "/@/utils/http/axios";
import "/@/utils/is";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const k = {
  list: [
    //------  列表api -------
    // 属性
    { text: ".acceptHrefParams", displayText: "acceptHrefParams", superiors: "this", desc: "获取地址栏上的条件" },
    { text: ".currentPage", displayText: "currentPage", superiors: "this", desc: "获取当前页数，默认1" },
    { text: ".currentTableName", displayText: "currentTableName", desc: "获取当前表名" },
    { text: ".description", displayText: "description", superiors: "this", desc: "获取当前表描述" },
    { text: ".hasChildrenField", displayText: "hasChildrenField", superiors: "this", desc: "如果是树形列表，获取是否有子节点字段名" },
    { text: ".ID", displayText: "ID", superiors: "this", desc: "获取当前表的配置ID" },
    { text: ".pageSize", displayText: "pageSize", superiors: "this", desc: "获取当前每页条数，默认10" },
    { text: ".queryParam", displayText: "queryParam", superiors: "this", desc: "获取查询表单的查询条件" },
    { text: ".selectedRowKeys", displayText: "selectedRowKeys", superiors: "this", desc: "获取选中行的id的数组" },
    { text: ".selectedRows", displayText: "selectedRows", superiors: "this", desc: "获取选中行的数据数组" },
    { text: ".sortField", displayText: "sortField", superiors: "this", desc: "获取排序字段，默认‘id’" },
    { text: ".sortType", displayText: "sortType", superiors: "this", desc: "获取排序类型，默认升序‘asc’" },
    { text: ".total", displayText: "total", superiors: "this", desc: "获取总条数" },
    // 方法
    { text: ".loadData()", displayText: "loadData()", superiors: "this", desc: "加载数据" },
    { text: ".clearSelectedRow()", displayText: "clearSelectedRow()", superiors: "this", desc: "清除选中的行" },
    {
      text: ".getLoadDataParams()",
      displayText: "getLoadDataParams()",
      superiors: "this",
      desc: "获取所有的查询条件，返回一个对象，包括：查询表单，高级查询，地址栏参数，分页信息，排序信息等"
    },
    { text: ".isTree()", displayText: "isTree()", superiors: "this", desc: "判断当前表是不是树，返回布尔值" },
    // 事件(前置)
    {
      text: `beforeEdit(row){
  return new Promise((resolve, reject) => {
    if(row.字段名 == '字段值'){
      reject('测试~');
    }else{
      resolve();
    }
  })     
}`,
      displayText: "beforeEdit(row){}",
      desc: "点击操作列下的编辑按钮触发，返回promise对象"
    },
    {
      text: `beforeDelete(row){
	return new Promise((resolve, reject) => {
  	if(row.字段名 == '字段值'){
    	reject('测试~');
    }else{
    	resolve();
    }
  })     
}`,
      displayText: "beforeDelete(row){}",
      desc: "点击操作列下的删除按钮触发，返回promise对象"
    },
    { text: "console.log()", displayText: "console.log()", desc: "打印日志" }
  ],
  form: [
    //------ 表单api -------
    // 属性
    { text: ".loading", displayText: "loading", superiors: "this", desc: "是否加载中，返回的是一个ref对象" },
    { text: ".isUpdate", displayText: "isUpdate", superiors: "this", desc: "是否是编辑页面，返回的是一个ref对象" },
    { text: ".onlineFormRef", displayText: "onlineFormRef", superiors: "this", desc: "主表/单表表单的ref对象" },
    { text: ".refMap", displayText: "refMap", superiors: "this", desc: "子表表单/子表table的ref对象map，key为子表表名" },
    { text: ".subActiveKey", displayText: "subActiveKey", superiors: "this", desc: "子表的激活的tab索引值对应的字符串，从‘0’开始，返回的是一个ref对象" },
    { text: ".sh", displayText: "sh", superiors: "this", desc: "单表/主表字段的显示隐藏状态" },
    { text: ".submitFlowFlag", displayText: "submitFlowFlag", superiors: "this", desc: "是否提交表单后自动提交流程，返回一个ref对象" },
    { text: ".subFormHeight", displayText: "subFormHeight", superiors: "this", desc: "一对一子表表单的高度，不需要设置，返回一个ref对象" },
    { text: ".subTableHeight", displayText: "subTableHeight", superiors: "this", desc: "一对多子表table的高度，不需要设置，返回一个ref对象" },
    { text: ".tableName", displayText: "tableName", superiors: "this", desc: "当前表名，返回的是一个ref对象" },
    { text: ".$nextTick", displayText: "$nextTick", superiors: "this", desc: "调用的是vue3的nextTick" },
    { text: ".字段名_load", displayText: "字段名_load", superiors: "this", desc: "控制字段的加载与否，设置为false表示当前字段不加载" },
    { text: ".字段名_disabled", displayText: "字段名_disabled", superiors: "this", desc: "控制字段的禁用与否，设置为true表示当前字段禁用" },
    // 方法
    { text: ".addSubRows(tableName, rows)", displayText: "addSubRows(tableName, rows)", superiors: "this", desc: "往一对多子表table里添加数据" },
    {
      text: ".changeOptions(field, options)",
      texdisplayTextt: "changeOptions(field, options)",
      superiors: "this",
      desc: "改变单表/主笔 下拉控件的下拉选项"
    },
    { text: ".clearSubRows(tableName)", displayText: "clearSubRows(tableName)", superiors: "this", desc: "清空一对多子表table的数据" },
    {
      text: ".clearThenAddRows(tableName, rows)",
      displayText: "clearThenAddRows(tableName, rows)",
      superiors: "this",
      desc: "先清空一对多子表table的数据，再往里添加数据"
    },
    { text: ".getFieldsValue()", displayText: "getFieldsValue()", superiors: "this", desc: "获取主表/单表 所有字段的值" },
    {
      text: ".getSubTableInstance(tableName)",
      displayText: "getSubTableInstance(tableName)",
      superiors: "this",
      desc: "获取子表的实例对象，这个对象可以调用子表table的方法"
    },
    { text: ".setFieldsValue(row)", displayText: "setFieldsValue(row)", superiors: "this", desc: "设置主表/单表 字段的值" },
    {
      text: ".triggleChangeValues(values,id,target)",
      displayText: "triggleChangeValues(values,id,target)",
      superiors: "this",
      desc: "改变单表/主表/子表 字段的值，一般用于change事件，其中id，target需要通过change事件的内置参数获取，如果不传id，target的值，则改变的是主表的字段"
    },
    { text: ".triggleChangeValue(field, value)", displayText: "triggleChangeValue(field, value)", superiors: "this", desc: "设置单表/主表 字段的值" },
    {
      text: ".onlineFormValueChange(field, value, otherValus)",
      displayText: "onlineFormValueChange(field, value, otherValus)",
      superiors: "this",
      desc: "定义后，当表单值改变的时候会触发该方法（因js增强hook方式不支持原来的onlChange，所以定义此方法）"
    },
    {
      text: ".changeSubTableOptions(tableName，field，options)",
      displayText: "changeSubTableOptions(tableName，field，options)",
      superiors: "this",
      desc: "改变一对一子表下拉框options"
    },
    {
      text: ".changeSubFormbleOptions(tableName，field，options)",
      displayText: "changeSubFormbleOptions(tableName，field，options)",
      superiors: "this",
      desc: "改变一对多子表下拉框options"
    },
    {
      text: ".changeRemoteOptions({ field, dict, label, type?, subTableName? })",
      displayText: "changeRemoteOptions({ field, dict, label, type?, subTableName? })",
      superiors: "this",
      desc: "改变动态下拉框options"
    },
    {
      text: ".submitFormAndFlow()",
      displayText: "submitFormAndFlow()",
      superiors: "this",
      desc: "表单提交且发起流程"
    },
    // 提交前置事件
    {
      text: `beforeSubmit(row){
	return new Promise((resolve, reject)=>{
    //此处模拟等待时间，可能需要发起请求
    setTimeout(()=>{
      if(row.字段名 == '字段值'){
        // 当某个字段不满足要求的时候可以reject 
        reject('测试~');
      }else{
        resolve();
      }
    },3000)
  })
}`,
      displayText: "beforeSubmit(row){}",
      desc: "提交前置事件"
    },
    // 表单加载事件
    {
      text: `loaded(){
  this.$nextTick(()=>{
    // let text = '测试js增强设置默认值';
    // if(this.isUpdate.value === true){
    //   text = '测试js增强修改表单值';
    // }
    this.setFieldsValue({
      字段名: 修改的值
    })
  })
}`,
      displayText: "loaded(){}",
      desc: "表单加载事件"
    },
    // 单表#表单值改变事件
    {
      text: `onlChange(){
  return {
    字段名(){
      let value = event.value
      console.log(value)
      this.triggleChangeValues({'字段名': '修改后的值'})
    }
  }
 }`,
      displayText: "onlChange(){}",
      desc: "单表#表单值改变事件"
    },
    // 子表#表单值改变事件
    {
      text: `子表名_onlChange(){
  return {
    字段名(){
      let value = event.value;
      console.log(value);
      let row = {'字段名': '测试一对多值改变：'+value};
      this.triggleChangeValues(row, event.row.id, event.target)
  }
  }
}`,
      displayText: "子表名_onlChange(){}",
      desc: "子表#表单值改变事件"
    },
    // 子改主#表单值改变事件
    {
      text: `子表名_onlChange(){
  return {
    子表字段01(){
      this.getSubTableInstance('子表名').getValues((err,values)=>{
        this.triggleChangeValues({'主表字段名': '修改后的值'})
      }) 
    },
  }
}
`,
      displayText: "子表名_onlChange(){}",
      desc: "子改主#表单值改变事件"
    },
    // js增强实现下拉联动
    {
      text: `onlChange(){
  return {
    字段名01(){
      let value = event.value
      this.changeOptions('字段名02', '修改后的值');
    }
    字段名02(){
      let value = event.value
      this.changeOptions('字段名03', '修改后的值');
    }
  }
}`,
      displayText: "changeOptions()",
      desc: "js增强实现下拉联动"
    },
    { text: "console.log()", displayText: "console.log()", desc: "打印日志" }
  ],
  common: [
    // JS增强 http请求
    {
      text: `getAction('请求url', { 'key': 'value'}).then(res => {
  console.log(res)
})`,
      displayText: "getAction(url, param)",
      desc: "get请求"
    },
    {
      text: `postAction('请求url', { 'key': 'value'}).then(res => {
  console.log(res)
})`,
      displayText: "postAction(url, param)",
      desc: "post请求"
    },
    {
      text: `putAction('请求url', { 'key': 'value'}).then(res => {
  console.log(res)
})`,
      displayText: "putAction(url, param)",
      desc: "put请求"
    },
    {
      text: `deleteAction('请求url', { 'key': 'value'}).then(res => {
  console.log(res)
})`,
      displayText: "deleteAction(url, param)",
      desc: "delete请求"
    },
    { text: "this", displayText: "this", desc: "上下文" },
    {
      text: ".openCustomModal({title,width,row,formComponent,requestUrl,hide,show})",
      displayText: "openCustomModal({title,width,row,formComponent,requestUrl,hide,show})",
      desc: "打开一个弹窗-参考 Js增强打开自定义弹窗"
    }
  ]
}, ie = q({
  name: "EnhanceJs",
  components: { BasicModal: W, JCodeEditor: L, EnhanceJsHistory: Y, QuestionCircleOutlined: oe, Tooltip: le },
  emits: ["register"],
  setup() {
    const { createMessage: e } = se(), t = Z(), h = g(), x = g(), f = M({ form: {}, list: {} }), l = g("list"), r = g(""), c = g(!1), a = g(!1), u = g(""), y = M({ form: "", list: "" }), p = { form: !1, list: !1 }, T = g(!1), S = [...k.list, ...k.common], J = [...k.form, ...k.common], d = g("240px"), [H, { closeModal: V }] = Q((s) => C(this, null, function* () {
      P(s.row);
    })), [j, O] = z(), { aiTestMode: _, genEnhanceJsData: N } = X();
    function P(s) {
      r.value = s.id, a.value = !1, u.value = s.tableName;
      let i = t.getEnhanceJs(r.value);
      (i == null ? void 0 : i.length) > 0 ? (l.value = i[i.length - 1].type, c.value = !0) : c.value = !1, p.form = !1, p.list = !1, l.value ? R(l.value) : R("form"), T.value = !0, setTimeout(() => T.value = !1, 150);
    }
    function $() {
      return C(this, null, function* () {
        yield Promise.all([D("form"), D("list")]), V(), e.success("保存成功");
      });
    }
    function D(s) {
      return C(this, null, function* () {
        let i = f[s], v = {
          cgJs: y[s],
          cgJsType: s
        };
        if (!p[s] || i.cgJs === v.cgJs)
          return;
        let B = !!i.id;
        B && (v = Object.assign({}, i, v)), yield te(r.value, v, B), t.addEnhanceJs({
          code: r.value,
          str: v.cgJs,
          type: v.cgJsType,
          date: (/* @__PURE__ */ new Date()).getTime()
        });
      });
    }
    function K() {
      V();
    }
    function R(s) {
      return C(this, null, function* () {
        l.value = s;
        try {
          if (!p[s]) {
            let i = yield ee(r.value, s);
            Object.assign(f[s], { id: null }, i), y[s] = f[s].cgJs, p[s] = !0;
          }
        } catch (i) {
        }
        setTimeout(() => {
          s == "list" ? x.value.refresh() : h.value.refresh();
        }, 150);
      });
    }
    function U() {
      O.openModal(!0, {
        code: r.value,
        type: l.value
      });
    }
    function I(s) {
      y[l.value] != s && (a.value = !0, y[l.value] = s);
    }
    function G() {
      l.value === "form" ? N(u.value, l.value, h.value) : N(u.value, l.value, x.value);
    }
    return {
      formEditorRef: h,
      listEditorRef: x,
      reloading: T,
      enhanceValues: y,
      enhanceType: l,
      showHistory: c,
      aiTestMode: _,
      tableName: u,
      genEnhanceJsData: N,
      onGenTestData: G,
      onChangeType: R,
      onCodeChange: I,
      onShowHistory: U,
      onSubmit: $,
      onCancel: K,
      registerModal: H,
      registerEnhanceJsHistory: j,
      listKeyWords: S,
      formKeyWords: J,
      handleGo: (s) => {
        window.open(`https://help.jeecg.com/java/online/enhanceJs/${s}`);
      },
      codeEditorHeight: d,
      handleFullScreenChange: (s) => {
        s ? d.value = `${document.documentElement.clientHeight - 250}px` : d.value = "240px";
      }
    };
  }
});
const ne = { class: "titleBox" }, re = { class: "titleBox" };
function ue(e, t, h, x, f, l) {
  const r = m("QuestionCircleOutlined"), c = m("Tooltip"), a = m("JCodeEditor"), u = m("a-tab-pane"), y = m("a-tabs"), p = m("a-button"), T = m("a-space"), S = m("EnhanceJsHistory"), J = m("BasicModal");
  return w(), E(J, {
    onRegister: e.registerModal,
    title: "JS增强",
    width: 800,
    onFullScreen: e.handleFullScreenChange
  }, {
    footer: o(() => [
      n(T, null, {
        default: o(() => [
          n(p, { onClick: e.onCancel }, {
            default: o(() => t[9] || (t[9] = [
              F("关闭")
            ])),
            _: 1
          }, 8, ["onClick"]),
          n(p, {
            type: "primary",
            onClick: e.onSubmit
          }, {
            default: o(() => t[10] || (t[10] = [
              F("确定")
            ])),
            _: 1
          }, 8, ["onClick"])
        ]),
        _: 1
      }),
      n(T, { style: { float: "left" } }, {
        default: o(() => [
          e.showHistory ? (w(), E(p, {
            key: 0,
            onClick: e.onShowHistory
          }, {
            default: o(() => t[11] || (t[11] = [
              F("查看历史版本")
            ])),
            _: 1
          }, 8, ["onClick"])) : A("", !0),
          e.aiTestMode ? (w(), E(p, {
            key: 1,
            onClick: e.onGenTestData
          }, {
            default: o(() => t[12] || (t[12] = [
              F("生成测试数据")
            ])),
            _: 1
          }, 8, ["onClick"])) : A("", !0)
        ]),
        _: 1
      })
    ]),
    default: o(() => [
      n(y, {
        activeKey: e.enhanceType,
        "onUpdate:activeKey": t[4] || (t[4] = (d) => e.enhanceType = d),
        onChange: e.onChangeType
      }, {
        default: o(() => [
          n(u, {
            key: "form",
            forceRender: ""
          }, {
            tab: o(() => [
              b("div", ne, [
                t[6] || (t[6] = b("span", { class: "title" }, "form", -1)),
                n(c, null, {
                  title: o(() => t[5] || (t[5] = [
                    b("span", null, "表单js增强文档", -1)
                  ])),
                  default: o(() => [
                    n(r, {
                      onClick: t[0] || (t[0] = (d) => e.handleGo("form"))
                    })
                  ]),
                  _: 1
                })
              ])
            ]),
            default: o(() => [
              !e.reloading && e.enhanceType === "form" ? (w(), E(a, {
                key: 0,
                ref: "formEditorRef",
                value: e.enhanceValues.form,
                "onUpdate:value": t[1] || (t[1] = (d) => e.enhanceValues.form = d),
                language: "javascript",
                fullScreen: !0,
                lineNumbers: !1,
                height: e.codeEditorHeight,
                "language-change": !1,
                onChange: e.onCodeChange,
                keywords: e.formKeyWords,
                placeholder: `代码提示技巧：
全局对象: this.调用属性或方法
事件方法：beforeSubmit、loaded、onlChange、getAction、postAction、putAction、deleteAction、deleteAction、openCustomModal等`
              }, null, 8, ["value", "height", "onChange", "keywords"])) : A("", !0)
            ]),
            _: 1
          }),
          n(u, {
            key: "list",
            forceRender: ""
          }, {
            tab: o(() => [
              b("div", re, [
                t[8] || (t[8] = b("span", { class: "title" }, "list", -1)),
                n(c, null, {
                  title: o(() => t[7] || (t[7] = [
                    b("span", null, "列表js增强文档", -1)
                  ])),
                  default: o(() => [
                    n(r, {
                      onClick: t[2] || (t[2] = (d) => e.handleGo("list"))
                    })
                  ]),
                  _: 1
                })
              ])
            ]),
            default: o(() => [
              !e.reloading && e.enhanceType === "list" ? (w(), E(a, {
                key: 0,
                ref: "listEditorRef",
                value: e.enhanceValues.list,
                "onUpdate:value": t[3] || (t[3] = (d) => e.enhanceValues.list = d),
                language: "javascript",
                fullScreen: !0,
                lineNumbers: !1,
                height: e.codeEditorHeight,
                "language-change": !1,
                onChange: e.onCodeChange,
                keywords: e.listKeyWords,
                placeholder: `代码提示技巧：
全局对象: this.调用属性或方法 
事件方法：beforeDelete、beforeEdit、getAction、postAction、putAction、deleteAction、deleteAction、openCustomModal等`
              }, null, 8, ["value", "height", "onChange", "keywords"])) : A("", !0)
            ]),
            _: 1
          })
        ]),
        _: 1
      }, 8, ["activeKey", "onChange"]),
      n(S, { onRegister: e.registerEnhanceJsHistory }, null, 8, ["onRegister"])
    ]),
    _: 1
  }, 8, ["onRegister", "onFullScreen"]);
}
const Re = /* @__PURE__ */ ae(ie, [["render", ue], ["__scopeId", "data-v-73c97e14"]]);
export {
  Re as default
};
