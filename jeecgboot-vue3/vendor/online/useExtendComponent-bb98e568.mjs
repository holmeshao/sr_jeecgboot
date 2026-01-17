var Tn = Object.defineProperty, xn = Object.defineProperties;
var On = Object.getOwnPropertyDescriptors;
var vt = Object.getOwnPropertySymbols;
var Pn = Object.prototype.hasOwnProperty, Dn = Object.prototype.propertyIsEnumerable;
var yt = (n, e, t) => e in n ? Tn(n, e, { enumerable: !0, configurable: !0, writable: !0, value: t }) : n[e] = t, ce = (n, e) => {
  for (var t in e || (e = {}))
    Pn.call(e, t) && yt(n, t, e[t]);
  if (vt)
    for (var t of vt(e))
      Dn.call(e, t) && yt(n, t, e[t]);
  return n;
}, Re = (n, e) => xn(n, On(e));
var Z = (n, e, t) => new Promise((i, c) => {
  var r = (h) => {
    try {
      v(t.next(h));
    } catch (y) {
      c(y);
    }
  }, f = (h) => {
    try {
      v(t.throw(h));
    } catch (y) {
      c(y);
    }
  }, v = (h) => h.done ? i(h.value) : Promise.resolve(h.value).then(r, f);
  v((t = t.apply(n, e)).next());
});
import { add as In } from "/@/components/Form/src/componentMap";
import { propTypes as re } from "/@/utils/propTypes";
import { computed as pe, watch as he, unref as Ce, ref as D, reactive as Se, toRaw as be, nextTick as Be, resolveComponent as se, openBlock as ne, createBlock as Pe, createElementBlock as de, normalizeClass as qe, createVNode as fe, defineComponent as Ot, h as Ae, mergeProps as Pt, withCtx as oe, createTextVNode as Te, toDisplayString as Oe, createCommentVNode as xe, watchEffect as Mn, inject as Rn, onMounted as Dt, onBeforeUnmount as kn, createElementVNode as me, Fragment as He, renderList as lt, markRaw as En, defineAsyncComponent as $n } from "vue";
import { PrinterOutlined as Ln, DiffOutlined as jn, FormOutlined as An, PlusOutlined as ot, EditOutlined as Nn, MinusCircleFilled as Bn } from "@ant-design/icons-vue";
import { useModal as Ie, useModalInner as It, BasicModal as Mt } from "/@/components/Modal";
import { useMessage as ke } from "/@/hooks/web/useMessage";
import { BasicForm as Rt, useForm as kt } from "/@/components/Form/index";
import { defHttp as ge } from "/@/utils/http/axios";
import { pick as Qe } from "lodash-es";
import { L as st } from "./constant-fa63bd66.mjs";
import { replaceUserInfoByExpression as Vn, getFileAccessHttpUrl as Ue } from "/@/utils/common/compUtils";
import { UploadTypeEnum as Wn } from "/@/components/Form/src/jeecg/components/JUpload";
import { isArray as wt, isObject as Et } from "/@/utils/is";
import { duplicateCheck as Jn } from "/@/views/system/user/user.api";
import { useUserStore as Yn } from "/@/store/modules/user";
import { replaceAll as Un, _eval as at, goJmReportViewPage as qn, importViewsFile as Hn, split as Ct, getWeekMonthQuarterYear as Qn } from "/@/utils";
import * as $t from "/@/utils/desform/customExpression";
import { usePermissionStore as zn } from "/@/store/modules/permission";
import { filterMultiDictText as Lt } from "/@/utils/dict/JDictSelectUtil";
import { BasicTable as Kn, TableAction as Zn } from "/@/components/Table";
import { useListPage as Gn } from "/@/hooks/system/useListPage";
import { useRouter as Xn } from "vue-router";
import { getAreaTextByCode as ei } from "/@/components/Form/src/utils/Area";
import { createImgPreview as ti } from "/@/components/Preview/index";
import ni from "./LinkTableListPiece-e016b8e6.mjs";
import { getToken as jt } from "/@/utils/auth";
import { downloadFile as ii } from "/@/api/common/api";
import { useAppInject as At } from "/@/hooks/web/useAppInject";
import { _ as $e } from "./index-9e1e1e53.mjs";
import ze from "/@/assets/images/placeholderImage.png";
import li from "./OnlineSelectCascade-d631ed72.mjs";
import { Loading as Nt } from "/@/components/Loading";
import si from "./JModalTip-a927f85d.mjs";
import { Button as Xe } from "ant-design-vue";
import { useDebounceFn as ri } from "@vueuse/core";
const oi = "jeecg_submit_form_and_flow", Es = "flow_submit_id", ai = "online_form_table_name", je = "validate-failed", $s = "setup", Ls = "EnhanceJS", js = {
  password: "text",
  file: "text",
  image: "text",
  textarea: "text",
  umeditor: "text",
  markdown: "text",
  checkbox: "list_multi",
  radio: "list"
}, ui = ".jeecg-online-modal .ant-modal-content", ci = "online_";
class ee {
  constructor(e, t) {
    this._data = t, this.field = e, this.label = t.title, this.hidden = !1, this.order = t.order || 999, this.required = !1, this.onlyValidator = "", this.setFieldsValue = "", this.hasChange = !0, e.indexOf("@") > 0 ? this.pre = e.substring(0, e.indexOf("@") + 1) : this.pre = "", this.schemaProp = {}, this.searchForm = !1, this.disabled = !1, this.popContainer = "", this.handleWidgetAttr(t), this.inPopover = !1, this.labelLength = st, this.initLabelLength();
  }
  /**
   * 获取最终的表单配置项，外面获取调用此方法
   */
  getFormItemSchema() {
    let e = this.getItem();
    return this.addDefaultChangeEvent(e), e;
  }
  /**
   * 获取表单配置，子类重写此方法
   */
  getItem() {
    let e = {
      field: this.field,
      label: this.label,
      labelLength: this.labelLength,
      component: "Input",
      itemProps: {
        labelCol: {
          class: "online-form-label"
        }
      }
    }, t = this.getRule();
    return t.length > 0 && this.onlyValidator && (e.rules = t), this.hidden === !0 && (e.show = !1), e;
  }
  /**
   * 设置表单ref
   * popup、分类树需要关联设置其他表单值的时候用到
   * @param ref
   */
  setFormRef(e) {
    this.formRef = e;
  }
  /**
   * 设置表单元素隐藏
   */
  isHidden() {
    return this.hidden = !0, this;
  }
  /**
   * 设置是否必填项
   * @param array
   */
  isRequired(e) {
    return e && e.length > 0 && e.indexOf(this.field) >= 0 && (this.required = !0), this;
  }
  /**
   * 初始化 label长度
   */
  initLabelLength() {
    let e = this.getExtendData();
    e && e.labelLength && (this.labelLength = e.labelLength);
  }
  /**
   * 获取扩展参数
   */
  getExtendData() {
    let e = {}, { fieldExtendJson: t } = this._data;
    if (t && typeof t == "string")
      try {
        let i = JSON.parse(t);
        e = ce({}, i);
      } catch (i) {
      }
    return e;
  }
  /***
   * 获取和此字段相关的其他字段 需要设置其为隐藏
   */
  getRelatedHideFields() {
    return [];
  }
  /**
   * placeholder
   */
  getPlaceholder(e) {
    let t = "请输入";
    return [
      "list",
      "radio",
      "checkbox",
      "date",
      "datetime",
      "time",
      "list_multi",
      "sel_search",
      "popup",
      "cat_tree",
      "sel_depart",
      "sel_user",
      "pca",
      "link_down",
      "sel_tree",
      "switch",
      "link_table",
      "link_table_field",
      "popup_dict",
      "LinkTableForQuery",
      "CascaderPcaForQuery",
      "select_user2",
      "rangeDate",
      "rangeTime",
      "rangeNumber"
    ].includes(e) ? t = "请选择" : ["file", "image"].includes(e) && (t = "请上传"), t + this.label;
  }
  /**
   * 唯一校验
   */
  setOnlyValidateFun(e) {
    e && (this.onlyValidator = (t, i) => Z(this, null, function* () {
      let c = yield e(t, i);
      return c ? Promise.reject(c) : Promise.resolve();
    }));
  }
  /**
   * 获取校验规则
   */
  getRule() {
    let e = [];
    const { view: t, errorInfo: i, pattern: c, type: r, fieldExtendJson: f } = this._data;
    if (this.required === !0) {
      let v = this.getPlaceholder(t);
      if (f) {
        const h = JSON.parse(f);
        h.validateError && (v = h.validateError);
      }
      i && (v = i), t == "sel_depart" || t == "sel_user" ? (this.schemaProp.required = !0, e.push({ required: !0, message: v })) : e.push({ required: !0, message: v });
    }
    if (t == "sel_user" && c === "only" && this.onlyValidator && e.push({ validator: this.onlyValidator }), t === "list" || t === "radio" || t === "markdown" || t === "pca" || t.indexOf("sel") >= 0 || t === "time" || t.indexOf("upload") >= 0 || t.indexOf("file") >= 0 || t.indexOf("image") >= 0)
      return e;
    if (c)
      if (c === "only")
        this.onlyValidator && e.push({ validator: this.onlyValidator });
      else if (c === "z")
        r == "number" || r == "integer" || e.push({ pattern: /^-?\d+$/, message: "请输入整数" });
      else {
        let v = i || "正则校验失败", h;
        try {
          h = new RegExp(c), h || (h = c);
        } catch (y) {
          h = c;
        }
        e.push({ pattern: h, message: v });
      }
    return e;
  }
  /**
   * 添加默认的change事件
   * @param schema
   */
  addDefaultChangeEvent(e) {
    this.hasChange && (e.componentProps || (e.componentProps = {}), this.disabled == !0 && (e.componentProps.disabled = !0), e.componentProps.hasOwnProperty("onChange") || (e.componentProps.onChange = (t, i) => {
      t instanceof Event && (t = t.target.value), t instanceof Array && (t = t.join(",")), !this.formRef || !this.formRef.value || !this.formRef.value.$formValueChange || this.formRef.value.$formValueChange(this.field, t, i);
    })), Object.keys(this.schemaProp).map((t) => {
      e[t] = this.schemaProp[t];
    });
  }
  noChange() {
    this.hasChange = !1;
  }
  updateField(e) {
    this.field = e;
  }
  /**
   * 高级查询 没有表单ref对象 手动设置setFieldValue方法用于 popup设置表单值
   */
  setFunctionForFieldValue(e) {
    e && (this.setFieldsValue = e);
  }
  asSearchForm() {
    this.searchForm = !0;
  }
  /**获取modal作为类下拉组件pop的父容器*/
  getModalAsContainer() {
    let e = this.getPopContainer();
    if (e != "body") {
      const t = document.querySelectorAll(e);
      if (t && t.length > 1) {
        const i = [];
        if (t.forEach((c) => {
          c.offsetWidth == 0 && c.offsetHeight == 0 || i.push(c);
        }), i.length === 1)
          return i[0];
      }
    }
    return document.querySelector(e);
  }
  /**区分modal表单和查询表单*/
  getPopContainer() {
    return this.searchForm === !0 ? "body" : this.inPopover === !0 ? `.${this.popContainer}` : this.popContainer ? `.${this.popContainer} .ant-modal-content` : ui;
  }
  handleWidgetAttr(e) {
    e.ui && e.ui.widgetattrs && e.ui.widgetattrs.disabled == !0 && (this.disabled = !0);
  }
  /**
   * 设置 popContainer
   */
  setCustomPopContainer(e) {
    this.popContainer = e;
  }
  //update-begin-author:taoyan date:2022-8-5 for: 他表字段/关联记录用
  // 获取他表字段的 配置信息
  getLinkFieldInfo() {
    return "";
  }
  // 1.将他表字段的配置信息设置到关联记录字段上
  setOtherInfo(e) {
  }
  //update-end-author:taoyan date:2022-8-5 for: 他表字段/关联记录用
  // 表单设计器高级查询用
  isInPopover() {
    this.inPopover = !0;
  }
  handleDictTableParams() {
    if (!this.formRef.value)
      return;
    const e = this._data.dictTable;
    if (!e)
      return;
    const t = e.match(/\${([^}]+)}/g);
    if (!t || t.length == 0)
      return;
    const i = t.map((f) => f.replace("${", "").replace("}", "")), c = pe(() => {
      const f = this.formRef.value.formModel;
      return i.map((v) => f[v]).join("");
    });
    let r = null;
    he(c, () => {
      r && clearTimeout(r), r = setTimeout(() => {
        const f = this.formRef.value.formModel;
        let v = e.replace(/\${([^}]+)}/g, (h, y) => f[y] == null ? "" : f[y]);
        this.updateDictTable(v);
      }, 150);
    }, { immediate: !0 });
  }
  updateDictTable(e) {
  }
  /**
   * 获取表字典的编码，可替换系统变量
   * @param dictTable
   * @param dictText
   * @param dictCode
   */
  genDictTableCode(e, t, i) {
    return e = Vn(e), encodeURI(`${e},${t},${i}`);
  }
}
class St extends ee {
  getItem() {
    let e = super.getItem();
    return this.hidden === !0 && (e.show = !1), e;
  }
}
var Bt = /* @__PURE__ */ ((n) => (n.datetime = "YYYY-MM-DD HH:mm:ss", n.date = "YYYY-MM-DD", n))(Bt || {});
class di extends ee {
  constructor(e, t, i) {
    super(e, t), this.format = Bt[t.view], this.showTime = t.view != "date";
    let c = t.fieldExtendJson;
    t.view == "date" && c && (c = JSON.parse(c), c.picker && c.picker != "default" ? this.picker = c.picker : this.picker = void 0), this.allowSelectRange = ["eq", "ne"].includes(i == null ? void 0 : i.rule);
  }
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "DatePickerInFilter",
      componentProps: {
        placeholder: `请选择${this.label}`,
        showTime: this.showTime,
        valueFormat: this.format,
        allowSelectRange: this.allowSelectRange,
        // update-begin--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
        picker: this.picker,
        // update-end--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
        style: {
          width: "100%"
        },
        getPopupContainer: (t) => this.getModalAsContainer()
      }
    });
  }
}
class fi extends ee {
  constructor(e, t) {
    super(e, t), this.schema = t, this.options = this.getOptions(t.enum, t.type), this.dictTable = t.dictTable, this.dictText = t.dictText, this.dictCode = t.dictCode, this.multi = t.multi || !1;
  }
  getItem() {
    let e = super.getItem(), t = this.getFormComponent(), i = this.getComponentProps();
    return Object.assign({}, e, {
      component: t,
      componentProps: i
    });
  }
  getFormComponent() {
    return this.options.length > 0 ? "Select" : "JDictSelectTag";
  }
  setFormRef(e) {
    super.setFormRef(e), this.handleDictTableParams();
  }
  updateDictTable(e) {
    this.formRef.value.updateSchema({
      field: this.field,
      componentProps: {
        dictCode: this.genDictTableCode(e, this.dictText, this.dictCode)
      }
    });
  }
  getComponentProps() {
    let t = {
      allowClear: !0,
      mode: this.multi === !0 ? "multiple" : "combobox",
      style: {
        width: "100%"
      },
      getPopupContainer: (i) => this.getModalAsContainer(),
      // 下拉框展开/关闭的回调
      onDropdownVisibleChange: (i) => {
        i && typeof this.schema.updateOptions == "function" && this.schema.updateOptions();
      }
    };
    return this.options.length > 0 ? t.options = this.options : this.dictTable ? t.dictCode = this.genDictTableCode(this.dictTable, this.dictText, this.dictCode) : (t.dictCode = this.dictCode, t.useDicColor = !0), t;
  }
  getOptions(e, t) {
    if (!e || e.length == 0)
      return [];
    let i = t == "number", c = [];
    for (let r of e) {
      if (r == null)
        break;
      let f = r.value;
      i && (f = parseInt(f)), c.push(Re(ce({}, r), {
        value: f,
        label: r.title
      }));
    }
    return c;
  }
}
class pi extends ee {
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "InputPassword"
    });
  }
}
class mi extends ee {
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "JUpload",
      componentProps: t
    });
  }
  getComponentProps() {
    let e = this.getExtendData();
    return e && e.uploadnum ? {
      maxCount: Number(e.uploadnum)
    } : {};
  }
}
class hi extends ee {
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "JUpload",
      componentProps: t
    });
  }
  getComponentProps() {
    let e = {
      fileType: Wn.image
    }, t = this.getExtendData();
    return t && t.uploadnum && (e.maxCount = Number(t.uploadnum)), e;
  }
}
class gi extends ee {
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "InputTextArea",
      componentProps: {
        autoSize: {
          minRows: 4,
          maxRows: 10
        }
      }
    });
  }
}
class bi extends ee {
  constructor(e, t) {
    super(e, t), this.dictTable = t.dictTable, this.dictText = t.dictText, this.dictCode = t.dictCode;
  }
  setFormRef(e) {
    super.setFormRef(e), this.handleDictTableParams();
  }
  updateDictTable(e) {
    this.formRef.value.updateSchema({
      field: this.field,
      componentProps: {
        dictCode: this.genDictTableCode(e, this.dictText, this.dictCode)
      }
    });
  }
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "JSelectMultiple",
      componentProps: t
    });
  }
  getComponentProps() {
    if (!this.dictTable && !this.dictCode)
      return {};
    {
      let e = {};
      return this.dictTable ? e.dictCode = this.genDictTableCode(this.dictTable, this.dictText, this.dictCode) : (e.dictCode = this.dictCode, e.useDicColor = !0), e.triggerChange = !0, e.popContainer = this.getPopContainer(), e;
    }
  }
}
class vi extends ee {
  constructor(e, t) {
    super(e, t), t.dictTable && t.dictText && t.dictCode ? (this.dict = this.genDictTableCode(t.dictTable, t.dictText, t.dictCode), this.type = 1) : (this.dict = encodeURI(`${t.dictCode}`), this.type = 0);
  }
  setFormRef(e) {
    super.setFormRef(e), this.handleDictTableParams();
  }
  updateDictTable(e) {
    this.formRef.value.updateSchema({
      field: this.field,
      componentProps: {
        dict: this.genDictTableCode(e, this._data.dictText, this._data.dictCode)
      }
    });
  }
  getItem() {
    let e = super.getItem(), t = this.getPopContainer();
    return Object.assign({}, e, {
      component: "JSearchSelect",
      componentProps: {
        dict: this.dict,
        pageSize: 10,
        // update-begin--author:liaozhiyang---date:20240628---for：【issues/6336】online下拉搜索框设置数据字典编辑弹窗报错
        async: !!this.type,
        // update-end--author:liaozhiyang---date:20240628---for：【issues/6336】online下拉搜索框设置数据字典编辑弹窗报错
        popContainer: t
      }
    });
  }
}
class yi extends ee {
  constructor(e, t) {
    super(e, t), this.code = t.code, this.multi = t.popupMulti, this.fieldConfig = this.getFieldConfig(t);
  }
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "JPopup",
      componentProps: t
    });
  }
  getComponentProps() {
    let e = {
      code: this.code,
      multi: this.multi,
      fieldConfig: this.fieldConfig
    };
    return this.formRef ? e.formElRef = this.formRef : e.setFieldsValue = this.setFieldsValue, this.inPopover === !0 && (e.getContainer = () => this.getModalAsContainer()), e.getFormValues = () => Ce(this.formRef).getFieldsValue(), e;
  }
  getFieldConfig(e) {
    let { destFields: t, orgFields: i, dictText: c } = e;
    if (!t || t.length == 0)
      return [];
    let r = t.split(","), f = i.split(","), v = c ? c.split(",") : null, h = [];
    const y = this.pre;
    for (let O = 0; O < r.length; O++)
      h.push({
        target: y + r[O],
        source: f[O],
        label: v ? v[O] : void 0
      });
    return h;
  }
}
class wi extends ee {
  constructor(e, t) {
    super(e, t), this.dictCode = `${t.code},${t.destFields},${t.orgFields}`, this.multi = t.popupMulti;
  }
  getItem() {
    const e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "JPopupDict",
      componentProps: t
    });
  }
  getComponentProps() {
    const e = {
      dictCode: this.dictCode,
      multi: this.multi
    };
    return this.inPopover && (e.getContainer = () => this.getModalAsContainer()), e.getFormValues = () => Ce(this.formRef).getFieldsValue(), e;
  }
}
class Ci extends ee {
  constructor(e, t) {
    super(e, t), this.multi = !1, this.pid = t.pidValue, this.pcode = t.pcode, this.textField = t.textField;
  }
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      componentProps: t,
      component: "JCategorySelect"
    });
  }
  /**
   * 1. 不带返回值的
   * 2. 带文本返回的
   */
  getComponentProps() {
    let e = {
      placeholder: "请选择" + this.label
    };
    if (this.pcode)
      e.pcode = this.pcode;
    else {
      let t = this.pid || "EMPTY_PID";
      e.pid = t;
    }
    return this.textField ? Re(ce({
      loadTriggleChange: !0,
      multiple: this.multi
    }, e), {
      back: this.textField,
      onChange: (t, i) => {
        this.formRef && (this.formRef.value.setFieldsValue(i), this.formRef.value.$formValueChange(this.field, t));
      }
    }) : ce({
      multiple: this.multi
    }, e);
  }
  getRelatedHideFields() {
    let e = [];
    return this.textField && e.push(this.textField), e;
  }
}
class Si extends ee {
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "JSelectDept",
      componentProps: t
    });
  }
  getComponentProps() {
    let e = this.getExtendData(), t = {
      checkStrictly: !0,
      showButton: !1
    };
    return e.text && (t.labelKey = e.text), e.store && (t.rowKey = e.store), e.multiSelect === !1 && (t.multiple = !1), e.multiSelect === !0 && (t.multiple = !0), t.maxTagCount = 3, this.inPopover === !0 && (t.getContainer = () => this.getModalAsContainer()), t;
  }
}
class Fi extends ee {
  constructor(e, t) {
    super(e, t), this.showButton = t.showButton !== !1;
  }
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "JSelectUser",
      componentProps: t
    });
  }
  getComponentProps() {
    let e = this.getExtendData(), t = {
      showSelected: !1,
      allowClear: !0,
      isRadioSelection: !1,
      showButton: this.showButton
    };
    return e.text && (t.labelKey = e.text), e.store && (t.rowKey = e.store), e.multiSelect === !1 && (t.isRadioSelection = !0), t.maxTagCount = 3, this.inPopover === !0 && (t.getContainer = () => this.getModalAsContainer()), t;
  }
}
class _i extends ee {
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "JEditor",
      componentProps: {
        //update-begin-author:taoyan date:2022-6-1 for: VUEN-1159 第一次加载时，点击第一个输入框，光标会跑到富文本输入框
        options: {
          auto_focus: !1
        }
        //update-end-author:taoyan date:2022-6-1 for: VUEN-1159 第一次加载时，点击第一个输入框，光标会跑到富文本输入框
        // fileMax:1,
        // showImageUpload:false,
        // width:"966px",
        // height:"200px"
      }
    });
  }
}
class Ti extends ee {
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "JMarkdownEditor",
      componentProps: {
        // height: 300,
      }
    });
  }
}
class xi extends ee {
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "JAreaLinkage",
      componentProps: {
        saveCode: "region",
        getPopupContainer: () => document.querySelector("body")
      }
    });
  }
}
class Oi extends ee {
  constructor(e, t) {
    super(e, t), this.dict = t.dict, this.pidField = t.pidField, this.pidValue = t.pidValue, this.hasChildField = t.hasChildField;
  }
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "JTreeSelect",
      componentProps: {
        dict: this.dict,
        pidField: this.pidField,
        pidValue: this.pidValue,
        // update-begin--author:liaozhiyang---date:20240509---for：【issues/6197】解决自定义树组件是否含有子节点功能不生效
        hasChildField: this.hasChildField
        // update-end--author:liaozhiyang---date:20240509---for：【issues/6197】解决自定义树组件是否含有子节点功能不生效
      }
    });
  }
}
class Pi extends ee {
  constructor(e, t) {
    super(e, t), this.dictTable = t.dictTable, this.dictText = t.dictText, this.dictCode = t.dictCode;
  }
  setFormRef(e) {
    super.setFormRef(e), this.handleDictTableParams();
  }
  updateDictTable(e) {
    this.formRef.value.updateSchema({
      field: this.field,
      componentProps: {
        dictCode: this.genDictTableCode(e, this.dictText, this.dictCode)
      }
    });
  }
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "JDictSelectTag",
      componentProps: t
    });
  }
  getComponentProps() {
    return !this.dictTable && !this.dictCode ? {} : this.dictTable ? {
      dictCode: this.genDictTableCode(this.dictTable, this.dictText, this.dictCode),
      type: "radio"
    } : {
      // update-begin--author:liaozhiyang---date:20230110---for：【QQYUN-7799】字典组件（原生组件除外）加上颜色配置
      useDicColor: !0,
      // update-end--author:liaozhiyang---date:20230110---for：【QQYUN-7799】字典组件（原生组件除外）加上颜色配置
      dictCode: this.dictCode,
      type: "radio"
    };
  }
}
class Di extends ee {
  constructor(e, t) {
    super(e, t), this.options = this.getOptions(t.enum);
  }
  setFormRef(e) {
    super.setFormRef(e), this.handleDictTableParams();
  }
  updateDictTable(e) {
    this.formRef.value.updateSchema({
      field: this.field,
      componentProps: {
        options: [],
        dictCode: this.genDictTableCode(e, this._data.dictText, this._data.dictCode)
      }
    });
  }
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "JCheckbox",
      componentProps: {
        options: this.options,
        triggerChange: !0,
        // update-begin--author:liaozhiyang---date:20230110---for：【QQYUN-7799】字典组件（原生组件除外）加上颜色配置
        useDicColor: !0
        // update-end--author:liaozhiyang---date:20230110---for：【QQYUN-7799】字典组件（原生组件除外）加上颜色配置
      }
    });
  }
  getOptions(e) {
    if (!e || e.length == 0)
      return [];
    let t = [];
    for (let i of e)
      t.push({
        value: i.value,
        label: i.title,
        // update-begin--author:liaozhiyang---date:20230110---for：【QQYUN-7799】字典组件（原生组件除外）加上颜色配置
        color: i.color
        // update-end--author:liaozhiyang---date:20230110---for：【QQYUN-7799】字典组件（原生组件除外）加上颜色配置
      });
    return t;
  }
}
class Ii extends ee {
  constructor(e, t) {
    super(e, t);
  }
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "JSwitch",
      componentProps: t
    });
  }
  getComponentProps() {
    let { fieldExtendJson: e } = this._data, t = ["Y", "N"];
    if (e && typeof e == "string") {
      const i = JSON.parse(e);
      wt(i) && i.length == 2 ? t = i : Et(i) && wt(i.switchOptions) && (t = i.switchOptions);
    }
    return {
      options: t
    };
  }
}
class Mi extends ee {
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "TimePicker",
      componentProps: {
        placeholder: `请选择${this.label}`,
        valueFormat: "HH:mm:ss",
        getPopupContainer: (t) => this.getModalAsContainer(),
        style: {
          width: "100%"
        }
      }
    });
  }
}
class Ri extends ee {
  constructor(e, t) {
    super(e, t);
    const { dictTable: i, dictText: c, dictCode: r, pidField: f, idField: v, origin: h, condition: y } = t;
    this.table = i, this.txt = c, this.store = r, this.idField = v, this.pidField = f, this.origin = h, this.condition = y, this.options = [], this.next = t.next || "", this.type = t.type;
  }
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "OnlineSelectCascade",
      componentProps: t
    });
  }
  getComponentProps() {
    let e = {
      table: this.table,
      txt: this.txt,
      store: this.store,
      pidField: this.pidField,
      idField: this.idField,
      origin: this.origin,
      pidValue: "-1",
      style: {
        width: "100%"
      },
      onChange: (t) => {
        this.valueChange(t);
      },
      onNext: (t) => {
        this.nextOptionsChange(t);
      }
    };
    return this._data.origin === !0 && (e.condition = this.condition), e;
  }
  nextOptionsChange(e) {
    return Z(this, null, function* () {
      if (!this.formRef || !this.next)
        return;
      yield this.formRef.value.updateSchema({
        field: this.next,
        componentProps: {
          pidValue: e
        }
      });
    });
  }
  valueChange(e) {
    return Z(this, null, function* () {
      if (!this.formRef)
        return;
      let t = this.formRef.value;
      t.$formValueChange(this.field, e), this.next && (yield t.setFieldsValue({ [this.next]: "" }));
    });
  }
}
class Ft extends ee {
  constructor(e, t) {
    super(e, t), this.slot = "";
    let i = t.fieldExtendJson;
    t.view == "date" && i && (i = JSON.parse(i), i.picker && i.picker != "default" ? this.picker = i.picker : this.picker = void 0), this.precision = t.dbPointLength;
  }
  getItem() {
    let e = super.getItem(), t = this.slot;
    const i = {};
    return this.picker && (i.picker = this.picker), this.precision && (i.precision = this.precision), Object.assign({}, e, {
      slot: t,
      componentProps: i
    });
  }
  groupDate() {
    return this.slot = "groupDate", this;
  }
  groupDatetime() {
    return this.slot = "groupDatetime", this;
  }
  groupTime() {
    return this.slot = "groupTime", this;
  }
  groupNumber() {
    return this.slot = "groupNumber", this;
  }
}
class ki extends ee {
  constructor(e, t) {
    super(e, t), this.dbPointLength = t.dbPointLength;
  }
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "InputNumber",
      componentProps: t
    });
  }
  getComponentProps() {
    const e = {
      style: {
        width: "100%"
      }
    };
    return this.dbPointLength >= 0 && (e.precision = this.dbPointLength), e;
  }
}
class Ei extends ee {
  constructor(e, t) {
    super(e, t), this.dictTable = t.dictTable, this.dictText = t.dictText, this.dictCode = t.dictCode, this.view = t.view, this.componentString = "", this.linkFields = [];
  }
  getItem() {
    let e = super.getItem();
    const t = this.getComponentProps();
    return Object.assign({}, e, {
      component: this.componentString,
      componentProps: t
    });
  }
  getComponentProps() {
    let e = {
      textField: this.dictText,
      tableName: this.dictTable,
      valueField: this.dictCode
    }, t = this.getExtendData();
    if (t.multiSelect ? e.multi = !0 : e.multi = !1, t.imageField ? e.imageField = t.imageField : e.imageField = "", t.showType == "select") {
      this.componentString = "LinkTableSelect";
      let i = this.getPopContainer();
      e.popContainer = i;
    } else
      this.componentString = "LinkTableCard";
    return this.linkFields.length > 0 && (e.linkFields = this.linkFields), e;
  }
  // 他表字段用于翻译
  setOtherInfo(e) {
    this.linkFields = e;
  }
}
class $i extends ee {
  constructor(e, t) {
    super(e, t), this.dictTable = t.dictTable, this.dictText = t.dictText;
  }
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      componentProps: {
        readOnly: !0,
        allowClear: !1,
        disabled: !0,
        style: {
          background: "none",
          color: "rgba(0, 0, 0, 0.85)",
          border: "none"
        }
      }
    });
  }
  /**
   * 获取他表字段的关联信息
   */
  getLinkFieldInfo() {
    return [this.dictTable, `${this.field},${this.dictText}`];
  }
}
class Li extends ee {
  constructor(e, t) {
    super(e, t), this.code = t.code, this.titleField = t.titleField, this.multi = t.multi || !1;
  }
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "LinkTableForQuery",
      componentProps: {
        code: this.code,
        multi: this.multi,
        field: this.titleField,
        style: {
          width: "100%"
        }
      }
    });
  }
}
class ji extends ee {
  constructor(e, t, i) {
    var c;
    super(e, t), this.schema = t, this.areaLevel = (c = t.areaLevel) != null ? c : 3, this.allowChangeLevel = ["eq", "ne"].includes(i == null ? void 0 : i.rule);
  }
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: "CascaderPcaInFilter",
      componentProps: {
        areaLevel: this.areaLevel,
        allowChangeLevel: this.allowChangeLevel,
        placeholder: "请选择…",
        style: {
          width: "100%"
        }
      }
    });
  }
}
class Ai extends ee {
  constructor(e, t) {
    super(e, t), this.multi = t.multi === !0, this.store = t.store || "", this.query = t.query || !1;
  }
  getItem() {
    let e = super.getItem(), t = this.getComponentProps();
    return Object.assign({}, e, {
      component: "UserSelect",
      componentProps: t
    });
  }
  getComponentProps() {
    let e = {
      multi: this.multi,
      store: this.store,
      query: this.query
    };
    return this.inPopover === !0 && (e.getContainer = () => this.getModalAsContainer()), e;
  }
}
class Ni extends ee {
  constructor(e, t) {
    super(e, t);
    let i = t.view;
    this.format = t.format, this.datetime = !1, i === "rangeNumber" ? this.componentType = "JRangeNumber" : i === "rangeTime" ? this.componentType = "RangeTime" : (this.componentType = "RangeDate", t.datetime === !0 && (this.datetime = !0));
  }
  getItem() {
    let e = super.getItem();
    return Object.assign({}, e, {
      component: this.componentType,
      componentProps: {
        datetime: this.datetime,
        format: this.format,
        getPopupContainer: (t) => this.getModalAsContainer()
      }
    });
  }
}
class et {
  static createFormSchema(e, t, i) {
    switch (t.view) {
      case "password":
        return new pi(e, t);
      case "list":
        return new fi(e, t);
      case "radio":
        return new Pi(e, t);
      case "checkbox":
        return new Di(e, t);
      case "date":
      case "datetime":
        return new di(e, t, i);
      case "time":
        return new Mi(e, t);
      case "file":
        return new mi(e, t);
      case "image":
        return new hi(e, t);
      case "textarea":
        return new gi(e, t);
      case "list_multi":
        return new bi(e, t);
      case "sel_search":
        return new vi(e, t);
      case "popup":
        return new yi(e, t);
      case "cat_tree":
        return new Ci(e, t);
      case "sel_depart":
        return new Si(e, t);
      case "sel_user":
        return new Fi(e, t);
      case "umeditor":
        return new _i(e, t);
      case "markdown":
        return new Ti(e, t);
      case "pca":
        return new xi(e, t);
      case "link_down":
        return new Ri(e, t);
      case "sel_tree":
        return new Oi(e, t);
      case "switch":
        return new Ii(e, t);
      case "link_table":
        return new Ei(e, t);
      case "link_table_field":
        return new $i(e, t);
      case "popup_dict":
        return new wi(e, t);
      case "slot":
        return new Ft(e, t);
      case "LinkTableForQuery":
        return new Li(e, t);
      case "CascaderPcaForQuery":
        return new ji(e, t, i);
      case "select_user2":
        return new Ai(e, t);
      case "rangeDate":
      case "rangeTime":
      case "rangeNumber":
        return new Ni(e, t);
      case "hidden":
        return new St(e, t).isHidden();
      default:
        return t.type == "number" ? new ki(e, t) : new St(e, t);
    }
  }
  static createSlotFormSchema(e, t) {
    let i = new Ft(e, t), c = t.view;
    if (c == "date")
      i.groupDate();
    else if (c == "datetime")
      i.groupDatetime();
    else if (c == "time")
      i.groupTime();
    else {
      let r = t.type;
      (r == "number" || r == "integer") && i.groupNumber();
    }
    return i;
  }
  /**
   * 表单ID 默认是隐藏的
   */
  static createIdField() {
    return {
      label: "",
      field: "id",
      component: "Input",
      show: !1
    };
  }
}
var ut = typeof globalThis != "undefined" ? globalThis : typeof window != "undefined" ? window : typeof global != "undefined" ? global : typeof self != "undefined" ? self : {};
function ct(n) {
  return n && n.__esModule && Object.prototype.hasOwnProperty.call(n, "default") ? n.default : n;
}
var Vt = { exports: {} };
(function(n, e) {
  (function(t, i) {
    n.exports = i();
  })(ut, function() {
    var t = 1e3, i = 6e4, c = 36e5, r = "millisecond", f = "second", v = "minute", h = "hour", y = "day", O = "week", b = "month", P = "quarter", C = "year", N = "date", E = "Invalid Date", U = /^(\d{4})[-/]?(\d{1,2})?[-/]?(\d{0,2})[Tt\s]*(\d{1,2})?:?(\d{1,2})?:?(\d{1,2})?[.:]?(\d+)?$/, B = /\[([^\]]+)]|Y{1,4}|M{1,4}|D{1,2}|d{1,4}|H{1,2}|h{1,2}|a|A|m{1,2}|s{1,2}|Z{1,2}|SSS/g, L = { name: "en", weekdays: "Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"), months: "January_February_March_April_May_June_July_August_September_October_November_December".split("_"), ordinal: function(d) {
      var p = ["th", "st", "nd", "rd"], l = d % 100;
      return "[" + d + (p[(l - 20) % 10] || p[l] || p[0]) + "]";
    } }, J = function(d, p, l) {
      var o = String(d);
      return !o || o.length >= p ? d : "" + Array(p + 1 - o.length).join(l) + d;
    }, A = { s: J, z: function(d) {
      var p = -d.utcOffset(), l = Math.abs(p), o = Math.floor(l / 60), a = l % 60;
      return (p <= 0 ? "+" : "-") + J(o, 2, "0") + ":" + J(a, 2, "0");
    }, m: function d(p, l) {
      if (p.date() < l.date())
        return -d(l, p);
      var o = 12 * (l.year() - p.year()) + (l.month() - p.month()), a = p.clone().add(o, b), s = l - a < 0, F = p.clone().add(o + (s ? -1 : 1), b);
      return +(-(o + (l - a) / (s ? a - F : F - a)) || 0);
    }, a: function(d) {
      return d < 0 ? Math.ceil(d) || 0 : Math.floor(d);
    }, p: function(d) {
      return { M: b, y: C, w: O, d: y, D: N, h, m: v, s: f, ms: r, Q: P }[d] || String(d || "").toLowerCase().replace(/s$/, "");
    }, u: function(d) {
      return d === void 0;
    } }, Q = "en", z = {};
    z[Q] = L;
    var X = "$isDayjsObject", T = function(d) {
      return d instanceof M || !(!d || !d[X]);
    }, I = function d(p, l, o) {
      var a;
      if (!p)
        return Q;
      if (typeof p == "string") {
        var s = p.toLowerCase();
        z[s] && (a = s), l && (z[s] = l, a = s);
        var F = p.split("-");
        if (!a && F.length > 1)
          return d(F[0]);
      } else {
        var x = p.name;
        z[x] = p, a = x;
      }
      return !o && a && (Q = a), a || !o && Q;
    }, _ = function(d, p) {
      if (T(d))
        return d.clone();
      var l = typeof p == "object" ? p : {};
      return l.date = d, l.args = arguments, new M(l);
    }, S = A;
    S.l = I, S.i = T, S.w = function(d, p) {
      return _(d, { locale: p.$L, utc: p.$u, x: p.$x, $offset: p.$offset });
    };
    var M = function() {
      function d(l) {
        this.$L = I(l.locale, null, !0), this.parse(l), this.$x = this.$x || l.x || {}, this[X] = !0;
      }
      var p = d.prototype;
      return p.parse = function(l) {
        this.$d = function(o) {
          var a = o.date, s = o.utc;
          if (a === null)
            return /* @__PURE__ */ new Date(NaN);
          if (S.u(a))
            return /* @__PURE__ */ new Date();
          if (a instanceof Date)
            return new Date(a);
          if (typeof a == "string" && !/Z$/i.test(a)) {
            var F = a.match(U);
            if (F) {
              var x = F[2] - 1 || 0, u = (F[7] || "0").substring(0, 3);
              return s ? new Date(Date.UTC(F[1], x, F[3] || 1, F[4] || 0, F[5] || 0, F[6] || 0, u)) : new Date(F[1], x, F[3] || 1, F[4] || 0, F[5] || 0, F[6] || 0, u);
            }
          }
          return new Date(a);
        }(l), this.init();
      }, p.init = function() {
        var l = this.$d;
        this.$y = l.getFullYear(), this.$M = l.getMonth(), this.$D = l.getDate(), this.$W = l.getDay(), this.$H = l.getHours(), this.$m = l.getMinutes(), this.$s = l.getSeconds(), this.$ms = l.getMilliseconds();
      }, p.$utils = function() {
        return S;
      }, p.isValid = function() {
        return this.$d.toString() !== E;
      }, p.isSame = function(l, o) {
        var a = _(l);
        return this.startOf(o) <= a && a <= this.endOf(o);
      }, p.isAfter = function(l, o) {
        return _(l) < this.startOf(o);
      }, p.isBefore = function(l, o) {
        return this.endOf(o) < _(l);
      }, p.$g = function(l, o, a) {
        return S.u(l) ? this[o] : this.set(a, l);
      }, p.unix = function() {
        return Math.floor(this.valueOf() / 1e3);
      }, p.valueOf = function() {
        return this.$d.getTime();
      }, p.startOf = function(l, o) {
        var a = this, s = !!S.u(o) || o, F = S.p(l), x = function(K, G) {
          var te = S.w(a.$u ? Date.UTC(a.$y, G, K) : new Date(a.$y, G, K), a);
          return s ? te : te.endOf(y);
        }, u = function(K, G) {
          return S.w(a.toDate()[K].apply(a.toDate("s"), (s ? [0, 0, 0, 0] : [23, 59, 59, 999]).slice(G)), a);
        }, w = this.$W, R = this.$M, q = this.$D, Y = "set" + (this.$u ? "UTC" : "");
        switch (F) {
          case C:
            return s ? x(1, 0) : x(31, 11);
          case b:
            return s ? x(1, R) : x(0, R + 1);
          case O:
            var H = this.$locale().weekStart || 0, j = (w < H ? w + 7 : w) - H;
            return x(s ? q - j : q + (6 - j), R);
          case y:
          case N:
            return u(Y + "Hours", 0);
          case h:
            return u(Y + "Minutes", 1);
          case v:
            return u(Y + "Seconds", 2);
          case f:
            return u(Y + "Milliseconds", 3);
          default:
            return this.clone();
        }
      }, p.endOf = function(l) {
        return this.startOf(l, !1);
      }, p.$set = function(l, o) {
        var a, s = S.p(l), F = "set" + (this.$u ? "UTC" : ""), x = (a = {}, a[y] = F + "Date", a[N] = F + "Date", a[b] = F + "Month", a[C] = F + "FullYear", a[h] = F + "Hours", a[v] = F + "Minutes", a[f] = F + "Seconds", a[r] = F + "Milliseconds", a)[s], u = s === y ? this.$D + (o - this.$W) : o;
        if (s === b || s === C) {
          var w = this.clone().set(N, 1);
          w.$d[x](u), w.init(), this.$d = w.set(N, Math.min(this.$D, w.daysInMonth())).$d;
        } else
          x && this.$d[x](u);
        return this.init(), this;
      }, p.set = function(l, o) {
        return this.clone().$set(l, o);
      }, p.get = function(l) {
        return this[S.p(l)]();
      }, p.add = function(l, o) {
        var a, s = this;
        l = Number(l);
        var F = S.p(o), x = function(R) {
          var q = _(s);
          return S.w(q.date(q.date() + Math.round(R * l)), s);
        };
        if (F === b)
          return this.set(b, this.$M + l);
        if (F === C)
          return this.set(C, this.$y + l);
        if (F === y)
          return x(1);
        if (F === O)
          return x(7);
        var u = (a = {}, a[v] = i, a[h] = c, a[f] = t, a)[F] || 1, w = this.$d.getTime() + l * u;
        return S.w(w, this);
      }, p.subtract = function(l, o) {
        return this.add(-1 * l, o);
      }, p.format = function(l) {
        var o = this, a = this.$locale();
        if (!this.isValid())
          return a.invalidDate || E;
        var s = l || "YYYY-MM-DDTHH:mm:ssZ", F = S.z(this), x = this.$H, u = this.$m, w = this.$M, R = a.weekdays, q = a.months, Y = a.meridiem, H = function(G, te, ie, Fe) {
          return G && (G[te] || G(o, s)) || ie[te].slice(0, Fe);
        }, j = function(G) {
          return S.s(x % 12 || 12, G, "0");
        }, K = Y || function(G, te, ie) {
          var Fe = G < 12 ? "AM" : "PM";
          return ie ? Fe.toLowerCase() : Fe;
        };
        return s.replace(B, function(G, te) {
          return te || function(ie) {
            switch (ie) {
              case "YY":
                return String(o.$y).slice(-2);
              case "YYYY":
                return S.s(o.$y, 4, "0");
              case "M":
                return w + 1;
              case "MM":
                return S.s(w + 1, 2, "0");
              case "MMM":
                return H(a.monthsShort, w, q, 3);
              case "MMMM":
                return H(q, w);
              case "D":
                return o.$D;
              case "DD":
                return S.s(o.$D, 2, "0");
              case "d":
                return String(o.$W);
              case "dd":
                return H(a.weekdaysMin, o.$W, R, 2);
              case "ddd":
                return H(a.weekdaysShort, o.$W, R, 3);
              case "dddd":
                return R[o.$W];
              case "H":
                return String(x);
              case "HH":
                return S.s(x, 2, "0");
              case "h":
                return j(1);
              case "hh":
                return j(2);
              case "a":
                return K(x, u, !0);
              case "A":
                return K(x, u, !1);
              case "m":
                return String(u);
              case "mm":
                return S.s(u, 2, "0");
              case "s":
                return String(o.$s);
              case "ss":
                return S.s(o.$s, 2, "0");
              case "SSS":
                return S.s(o.$ms, 3, "0");
              case "Z":
                return F;
            }
            return null;
          }(G) || F.replace(":", "");
        });
      }, p.utcOffset = function() {
        return 15 * -Math.round(this.$d.getTimezoneOffset() / 15);
      }, p.diff = function(l, o, a) {
        var s, F = this, x = S.p(o), u = _(l), w = (u.utcOffset() - this.utcOffset()) * i, R = this - u, q = function() {
          return S.m(F, u);
        };
        switch (x) {
          case C:
            s = q() / 12;
            break;
          case b:
            s = q();
            break;
          case P:
            s = q() / 3;
            break;
          case O:
            s = (R - w) / 6048e5;
            break;
          case y:
            s = (R - w) / 864e5;
            break;
          case h:
            s = R / c;
            break;
          case v:
            s = R / i;
            break;
          case f:
            s = R / t;
            break;
          default:
            s = R;
        }
        return a ? s : S.a(s);
      }, p.daysInMonth = function() {
        return this.endOf(b).$D;
      }, p.$locale = function() {
        return z[this.$L];
      }, p.locale = function(l, o) {
        if (!l)
          return this.$L;
        var a = this.clone(), s = I(l, o, !0);
        return s && (a.$L = s), a;
      }, p.clone = function() {
        return S.w(this.$d, this);
      }, p.toDate = function() {
        return new Date(this.valueOf());
      }, p.toJSON = function() {
        return this.isValid() ? this.toISOString() : null;
      }, p.toISOString = function() {
        return this.$d.toISOString();
      }, p.toString = function() {
        return this.$d.toUTCString();
      }, d;
    }(), $ = M.prototype;
    return _.prototype = $, [["$ms", r], ["$s", f], ["$m", v], ["$H", h], ["$W", y], ["$M", b], ["$y", C], ["$D", N]].forEach(function(d) {
      $[d[1]] = function(p) {
        return this.$g(p, d[0], d[1]);
      };
    }), _.extend = function(d, p) {
      return d.$i || (d(p, M, _), d.$i = !0), _;
    }, _.locale = I, _.isDayjs = T, _.unix = function(d) {
      return _(1e3 * d);
    }, _.en = z[Q], _.Ls = z, _.p = {}, _;
  });
})(Vt);
var Bi = Vt.exports;
const we = /* @__PURE__ */ ct(Bi);
var Wt = { exports: {} };
(function(n, e) {
  (function(t, i) {
    n.exports = i();
  })(ut, function() {
    var t = "week", i = "year";
    return function(c, r, f) {
      var v = r.prototype;
      v.week = function(h) {
        if (h === void 0 && (h = null), h !== null)
          return this.add(7 * (h - this.week()), "day");
        var y = this.$locale().yearStart || 1;
        if (this.month() === 11 && this.date() > 25) {
          var O = f(this).startOf(i).add(1, i).date(y), b = f(this).endOf(t);
          if (O.isBefore(b))
            return 1;
        }
        var P = f(this).startOf(i).date(y).startOf(t).subtract(1, "millisecond"), C = this.diff(P, t, !0);
        return C < 0 ? f(this).startOf("week").week() : Math.ceil(C);
      }, v.weeks = function(h) {
        return h === void 0 && (h = null), this.week(h);
      };
    };
  });
})(Wt);
var Vi = Wt.exports;
const Wi = /* @__PURE__ */ ct(Vi);
var Jt = { exports: {} };
(function(n, e) {
  (function(t, i) {
    n.exports = i();
  })(ut, function() {
    var t = "month", i = "quarter";
    return function(c, r) {
      var f = r.prototype;
      f.quarter = function(y) {
        return this.$utils().u(y) ? Math.ceil((this.month() + 1) / 3) : this.month(this.month() % 3 + 3 * (y - 1));
      };
      var v = f.add;
      f.add = function(y, O) {
        return y = Number(y), this.$utils().p(O) === i ? this.add(3 * y, t) : v.bind(this)(y, O);
      };
      var h = f.startOf;
      f.startOf = function(y, O) {
        var b = this.$utils(), P = !!b.u(O) || O;
        if (b.p(y) === i) {
          var C = this.quarter() - 1;
          return P ? this.month(3 * C).startOf(t).startOf("day") : this.month(3 * C + 2).endOf(t).endOf("day");
        }
        return h.bind(this)(y, O);
      };
    };
  });
})(Jt);
var Ji = Jt.exports;
const Yi = /* @__PURE__ */ ct(Ji);
we.extend(Wi);
we.extend(Yi);
const Yt = Object.keys($t), Ui = Yt.join(","), qi = Yt.map((n) => $t[n]), Ut = /#{([^}]+)?}/g, qt = /{{([^}]+)?}}/g, Ve = /\${([^}]+)?}/g, Ne = { ADD: "add", EDIT: "edit", DETAIL: "detail", RELOAD: "reload" };
function tt(n, e, t) {
  if (dt(e.defVal)) {
    const i = { field: n, type: e.type, value: e.defVal, view: e.view, fieldExtendJson: e.fieldExtendJson }, c = t.findIndex((r) => r.field === n);
    c === -1 ? t.push(i) : t[c] = i;
  }
}
function Hi(n, e) {
  dt(n.fieldDefaultValue) && e.push({ field: n.key, type: n.type, value: n.fieldDefaultValue });
}
function rt(n, e, t) {
  return Z(this, null, function* () {
    if (Array.isArray(n) && n.length > 0) {
      let i = {};
      for (let c of n) {
        let { value: r, type: f, field: v } = c;
        r = yield Ht(r, Ne.ADD, t || {}), f === "number" && r && (r = Number.parseFloat(r)), r = Qi(c, r), i[v] = r;
      }
      e(i);
    }
  });
}
function Qi(n, e) {
  const { type: t, field: i, view: c, fieldExtendJson: r } = n;
  if (c == "date" && r) {
    const f = JSON.parse(r), { picker: v } = f;
    if (v && v != "default" && e) {
      let h;
      try {
        if (v === "year") {
          const O = e.split("-")[0];
          h = we().year(O).format("YYYY-MM-DD");
        }
        if (v === "month") {
          const y = e.split("-"), O = y[0], b = +y[1] + 1;
          h = we().year(O).month(b).format("YYYY-MM-DD");
        }
        if (v === "week") {
          const y = e.split("-"), O = y[0], b = y[1].match(/^(\d+)周$/)[1];
          h = we().year(O).week(b).format("YYYY-MM-DD");
        }
        if (v === "quarter") {
          const y = e.split("-"), O = y[0], b = y[1].match(/^[Qq](\d)$/)[1];
          h = we().year(O).quarter(b).format("YYYY-MM-DD");
        }
      } catch (y) {
        h = e;
      }
      return h;
    }
    return e;
  }
  return e;
}
function As(n, e, t) {
  return Z(this, null, function* () {
    let { defVal: i, type: c } = e;
    if (dt(i)) {
      let r = yield Ht(i, Ne.ADD, {});
      if (c === "number" && r)
        if (e.mode == "group" && typeof r == "string" && r.indexOf(",") != -1) {
          const f = r.split(",");
          r = [], f[0] && r.push(Number.parseFloat(f[0])), f[1] && r.push(Number.parseFloat(f[1]));
        } else
          r = Number.parseFloat(r);
      t[n] = r;
    }
  });
}
function dt(n) {
  return !!(n || n === 0);
}
function Ht(n, e, t) {
  return Z(this, null, function* () {
    if (n != null && Qt(n)) {
      let i = yield zi(n, e, t);
      if (i != null)
        return i;
    }
    return n;
  });
}
function Qt(n) {
  let e = 0, t = 0, i = 0;
  if (n.replace(Ve, () => i++), i > 1)
    return !1;
  n.replace(Ut, () => e++), n.replace(qt, () => t++);
  let c = e + t;
  return !(i > 0 && c > 0);
}
function zt(n, e) {
  let t = /* @__PURE__ */ new Map();
  return n.replace(e, function(i, c) {
    return t.set(i, c.trim()), i;
  }), t;
}
function zi(n, e, t) {
  return Z(this, null, function* () {
    return (e === Ne.ADD || e === Ne.RELOAD) && Ve.test(n) ? yield nt(n, Ve, Xi, [t]) : e === Ne.ADD ? (n = yield nt(n, Ut, Ki), n = yield nt(n, qt, Gi), n) : null;
  });
}
function nt(c, r, f) {
  return Z(this, arguments, function* (n, e, t, i = []) {
    let v = zt(n, e);
    for (let h of v.keys()) {
      let y = v.get(h), O = yield t.apply(null, [y, h, ...i]);
      if (h === n)
        return O;
      n = Un(n, h, O);
    }
    return n;
  });
}
function Ki(n, e) {
  return Z(this, null, function* () {
    switch (n) {
      case "date":
        return we().format("YYYY-MM-DD");
      case "time":
        return we().format("HH:mm:ss");
      case "datetime":
        return we().format("YYYY-MM-DD HH:mm:ss");
      default:
        let t = Zi(n);
        return t != null ? t : e;
    }
  });
}
function Zi(n) {
  let t = Yn().getUserInfo;
  if (t)
    switch (n) {
      case "sysUserId":
        return t.id;
      case "sysUserCode":
      case "sys_user_code":
        return t.username;
      case "sysUserName":
        return t.realname;
      case "sysOrgCode":
      case "sys_org_code":
        return t.orgCode;
    }
  return null;
}
function Gi(n, e) {
  return Z(this, null, function* () {
    let t = at(`(function (${Ui}){ return ${n} })`);
    try {
      return t.apply(null, qi);
    } catch (i) {
      return e;
    }
  });
}
function Xi(n, e, t) {
  return Z(this, null, function* () {
    let i = {};
    typeof t == "function" ? i = t() : t && (i = ce({}, t)), n = Kt(n).exp;
    let c = `/sys/fillRule/executeRuleByCode/${n}`, { success: r, message: f, result: v } = yield ge.put({ url: c, params: i }, { isTransformResponse: !1 });
    return r ? v : e;
  });
}
function Kt(n) {
  let e = n.split("?");
  if (e.length > 1) {
    let t = "", i = [], r = e[1].split("&");
    return r.forEach((f, v) => {
      let [h, y] = f.split("=");
      y = y.trim(), h === "onl_watch" ? i = y.split(",") : (t += `${h}=${y}`, v < r.length - 1 && (t += "&"));
    }), {
      exp: e[0] + (t === "" ? "" : "?" + t),
      watchFields: i
    };
  }
  return { exp: n, watchFields: [] };
}
function Ns(n) {
  const e = /* @__PURE__ */ new Map();
  if (Array.isArray(n) && n.length > 0)
    for (let t of n) {
      let { value: i, field: c } = t;
      if (!(i == null || i == "") && Qt(i) && Ve.test(i)) {
        let r = zt(i, Ve);
        for (let f of r.keys()) {
          let v = r.get(f);
          const { watchFields: h } = Kt(v);
          for (const y of h) {
            let O = e.get(y);
            Array.isArray(O) || (O = [], e.set(y, O)), !O.includes(c) && O.push(c);
          }
        }
      }
    }
  return e;
}
const ft = "link_down", el = "link_table_field", tl = "link_table";
function Zt(n, e) {
  Gl();
  const t = n.modalClass, i = D([]), c = D(""), r = D({}), f = Se({}), v = D(!1), h = D([]), y = D({}), O = {}, b = D([]), P = Se({}), C = D("");
  C.value = { sm: 24, xs: 24, md: 12, lg: 12, xl: 12, xxl: 12 };
  const N = D({ xs: { span: 24 }, sm: { span: 4 }, md: { span: 4 }, lg: { span: 4 }, xl: { span: 4 }, xxl: { span: 4 } }), E = D(null), U = D(6 * 14 + 10);
  function B(T, I, _, S = {}) {
    var x;
    ll(P), P[c.value] = [];
    let M = [], $ = [], d = [], p = {}, l = {};
    Object.keys(T).map((u) => {
      var R;
      const w = T[u];
      if (w.view == "tab") {
        v.value = !0, P[u] = [];
        let q = {
          key: u,
          // 这个foreignKey是主表的字段
          foreignKey: w.foreignKey,
          describe: w.describe,
          relationType: w.relationType,
          requiredFields: w.required || [],
          order: w.order,
          id: w.id
        };
        w.relationType == 1 ? (O[u] = D(null), q.properties = w.properties) : (L(w), O[u] = D(), q.columns = w.columns, p[u] = []), M.push(q), rl(u, w);
      } else if (tt(u, w, P[c.value]), w.view === ft) {
        let q = il(w, u);
        for (let Y of q) {
          const H = Y.key == u ? w : (R = w.others) == null ? void 0 : R.find((G) => G.field === Y.key);
          H && tt(Y.key, H, P[c.value]), f[Y.key] = !0, f[Y.key + "_load"] = !0, f[Y.key + "_disabled"] = !1, J(S, Y);
          let j = et.createFormSchema(Y.key, Y);
          _ && j.setOnlyValidateFun(_), j.isRequired(I), j.setFormRef(e), j.handleWidgetAttr(w);
          let K = _t($, Y.key);
          K == -1 ? $.push(j) : $[K] = j;
        }
      } else if (tt(u, w, P[c.value]), f[u] = !0, f[u + "_load"] = !0, f[u + "_disabled"] = !1, _t($, u) == -1) {
        J(S, w);
        let Y = et.createFormSchema(u, w);
        if (_ && Y.setOnlyValidateFun(_), Y.isRequired(I), Y.setFormRef(e), $.push(Y), d.push(...Y.getRelatedHideFields()), w.view === el) {
          let H = Y.getLinkFieldInfo();
          H && (l[H[0]] ? l[H[0]].push(H[1]) : l[H[0]] = [H[1]]);
        }
      }
    }), $.sort(function(u, w) {
      return u.order - w.order;
    });
    const o = [];
    (() => {
      for (let u = 0, w = $.length; u < w; u++) {
        const R = $[u];
        A(R == null ? void 0 : R._data, "isOneRow") && (o.push($.splice(u, 1)[0]), u--, w--);
      }
    })(), $ = [...$, ...o];
    let a = [];
    a.push(et.createIdField());
    let s = null, F = !1;
    for (let u of $) {
      const w = u.label.length;
      s ? (s.label.length < w || s.label.length === w && !s.required && u.required) && (s = u) : s = u, u.required && (F = !0), u.view && u.view == tl && l[u.field] && u.setOtherInfo(l[u.field]), d.indexOf(u.field) >= 0 && u.isHidden(), t && u.setCustomPopContainer(t);
      const R = u.getFormItemSchema();
      if (R.component === "JDictSelectTag" && ((x = u == null ? void 0 : u._data) == null ? void 0 : x.type) === "number" && (R.componentProps.stringToNumber = !0), n.formTemplate > 1 && A(u == null ? void 0 : u._data, "isOneRow")) {
        R.colProps = { span: 24 };
        const q = Q(), { labelCol: Y = {} } = q, H = {}, j = {};
        Object.keys(Y).forEach((K) => {
          if (["xs", "sm", "md", "lg", "xl", "xxl"].includes(K)) {
            const G = Y[K].span, te = Math.round(G / n.formTemplate);
            H[K] = { span: te }, j[K] = { span: 24 - te - 1 };
          }
        }), R.itemProps = { labelCol: H, wrapperCol: j };
      }
      a.push(R);
    }
    if (i.value = a, M.sort(function(u, w) {
      return u.order - w.order;
    }), M.forEach((u) => {
      const w = u.columns;
      u.columns && w.forEach((R) => {
        var q;
        if (u.relationType == 0 && ["popup", "popup_dict"].includes(R.type)) {
          let Y = !0;
          R.fieldExtendJson && (Y = JSON.parse(R.fieldExtendJson).popupMulti);
          const H = (q = R.props) != null ? q : {};
          R.props = Re(ce({}, H), { multi: Y });
        }
        if (R.type === "date" && R.fieldExtendJson) {
          const Y = JSON.parse(R.fieldExtendJson);
          Y.picker && Y.picker != "default" && Object.assign(R, { picker: Y.picker });
        }
      });
    }), h.value = M, y.value = p, S.formLabelLengthShow && S.formLabelLength)
      U.value = S.formLabelLength * 14 + 10 + +`${F ? 13 : 0}`, E.value = null;
    else if (s) {
      let u = s.label.length;
      u = u > st ? st : u;
      const w = s.required, R = u * 14 + 10 + +`${w ? 13 : 0}`;
      U.value = R;
    }
  }
  he(
    f,
    (T) => {
      let I = e.value, _ = [], S = be(T);
      Object.keys(S).map((M) => {
        if (!M.endsWith("_load")) {
          let $ = {
            field: M,
            show: S[M]
          }, d = M + "_load";
          S.hasOwnProperty(d) && ($.ifShow = S[d]);
          let p = M + "_disabled";
          S.hasOwnProperty(p) && ($.dynamicDisabled = () => S[p]), _.push($);
        }
      }), I && I.updateSchema(_);
    },
    { immediate: !1 }
  );
  function L(T) {
    Gt(T, (I) => {
      Hi(I, P[T.key]);
    });
  }
  function J(T, I, _ = "labelLength") {
    const { formLabelLengthShow: S, formLabelLength: M } = T;
    if (S && M) {
      let $ = I == null ? void 0 : I.fieldExtendJson;
      $ ? ($ = JSON.parse($), $[_] = M) : $ = { [_]: M }, I.fieldExtendJson = JSON.stringify($);
    }
  }
  function A(T = {}, I) {
    let _ = T == null ? void 0 : T.fieldExtendJson;
    if (_)
      return _ = JSON.parse(_), _[I];
  }
  he(
    () => n.formTemplate,
    () => {
      const T = Q();
      C.value = T.baseColProps, N.value = T.labelCol, E.value = T.wrapperCol;
    },
    { immediate: !0 }
  );
  function Q() {
    let T = n.formTemplate;
    return T == 2 ? {
      baseColProps: { sm: 24, xs: 24, md: 12, lg: 12, xl: 12, xxl: 12 }
      // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      // labelCol: { xs: { span: 24 }, sm: { span: 4 }, md: { span: 4 }, lg: { span: 4 }, xl: { span: 4 }, xxl: { span: 4 } },
      // wrapperCol: { xs: { span: 24 }, sm: { span: 19 }, md: { span: 19 }, lg: { span: 19 }, xl: { span: 19 }, xxl: { span: 19 } },
      // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
    } : T == 3 ? {
      baseColProps: { sm: 24, xs: 24, md: 8, lg: 8, xl: 8, xxl: 8 }
      // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      // labelCol: { xs: { span: 24 }, sm: { span: 6 }, md: { span: 6 }, lg: { span: 6 }, xl: { span: 6 }, xxl: { span: 6 } },
      // wrapperCol: { xs: { span: 24 }, sm: { span: 17 }, md: { span: 17 }, lg: { span: 17 }, xxl: { span: 17 } },
      // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
    } : T == 4 ? {
      baseColProps: { sm: 24, xs: 24, md: 6, lg: 6, xl: 6, xxl: 6 }
      // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      // labelCol: { xs: { span: 24 }, sm: { span: 4 }, md: { span: 4 }, lg: { span: 4 }, xl: { span: 4 }, xxl: { span: 4 } },
      // wrapperCol: { xs: { span: 24 }, sm: { span: 18 }, md: { span: 18 }, lg: { span: 18 }, xl: { span: 18 }, xxl: { span: 18 } },
      // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
    } : {
      baseColProps: { sm: 24, xs: 24, md: 24, lg: 24, xl: 24, xxl: 24 }
      // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      // labelCol: { xs: { span: 24 }, sm: { span: 4 }, md: { span: 4 }, lg: { span: 4 }, xl: { span: 4 }, xxl: { span: 4 } },
      // wrapperCol:{ xs: { span: 24 }, sm: { span: 18 }, md: { span: 18 }, lg: { span: 18 }, xl: { span: 18 }, xxl: { span: 18 } },
      // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
    };
  }
  function z(T, I) {
    return new Promise((_) => {
      I || _("");
      let M = {
        tableName: c.value.replace(/\$\d+/, ""),
        fieldName: T.field,
        fieldVal: I
      }, $ = r.value;
      $.id && (M.dataId = $.id), Jn(M).then((d) => {
        d.success ? _("") : _(d.message);
      }).catch((d) => {
        _(d);
      });
    });
  }
  function X(T) {
    return Object.keys(T).map((I) => {
      T[I] && T[I] instanceof Array && (T[I] = T[I].join(","));
    }), T;
  }
  return {
    formSchemas: i,
    defaultValueFields: P,
    tableName: c,
    dbData: r,
    checkOnlyFieldValue: z,
    createFormSchemas: B,
    fieldDisplayStatus: f,
    subTabInfo: h,
    hasSubTable: v,
    subDataSource: y,
    baseColProps: C,
    changeDataIfArray2String: X,
    linkDownList: b,
    refMap: O,
    labelCol: N,
    wrapperCol: E,
    labelWidth: U
  };
}
function Gt(n, e) {
  const t = {
    inputNumber: "input-number",
    sel_depart: "depart-select",
    sel_user: "user-select",
    list_multi: "select-multiple",
    input_pop: "textarea",
    sel_search: "select-search",
    "select-dict-search": "selectDictSearch"
  };
  n.columns.forEach((r) => {
    r.type === "radio" ? r.type = "select" : t[r.type] ? r.type = t[r.type] : r.type === "popup" && i(r), r.type === "depart-select" && (r.checkStrictly = !0), r.type === "user-select" && c(r), r.type === "pca" && (r.width = "230px"), (r.width == 120 || r.width == "120px") && (r.type == "image" || r.type == "file") && (r.width = "130px"), r.width || (r.width = "200px"), e && e(r);
  });
  function i(r) {
    let { destFields: f, orgFields: v } = r, h = [];
    if (!(!f || f.length == 0)) {
      let y = f.split(","), O = v.split(",");
      for (let b = 0; b < y.length; b++)
        h.push({
          target: y[b],
          source: O[b]
        });
    }
    r.fieldConfig = h;
  }
  function c(r) {
    let f = r.fieldExtendJson, v = !1;
    if (f)
      try {
        JSON.parse(f).multiSelect === !1 && (v = !0);
      } catch (h) {
      }
    r.isRadioSelection = v;
  }
}
function nl(n) {
  let e = {};
  const t = {
    addSubRows: "<m> 一对多子表，新增自定义行",
    changeOptions: "<m> 改变下拉框选项",
    clearSubRows: "<m> 清空一对多子表行",
    clearThenAddRows: "<m> 清空一对多子表行，然后新增自定义行",
    executeMainFillRule: "<m> 刷新主表的增值规制值",
    executeSubFillRule: "<m> 刷新子表的增值规制值",
    getFieldsValue: "<m> 获取表单控件的值",
    getSubTableInstance: "<m> 获取子表实例",
    isUpdate: "<p> 判断是否为编辑模式",
    loading: "<p> 页面加载状态",
    onlineFormRef: "<p> 当前表单ref对象",
    refMap: "<p> 子表ref对象map",
    setFieldsValue: "<m> 设置表单控件的值",
    sh: "<p> 表单控件的显示隐藏状态",
    subActiveKey: "<p> 子表激活tab，对应子表表名",
    subFormHeight: "<p> 一对一子表表单高度",
    submitFlowFlag: "<p> 是否提交流程状态",
    subTableHeight: "<p> 一对多子表表格高度",
    tableName: "<p> 当前表名",
    triggleChangeValues: "<m> 修改多个表单值",
    triggleChangeValue: "<m> 修改表单值",
    updateSchema: "<m> 修改表单控件配置",
    // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
    changeSubTableOptions: "<m> 改变一对多子表下拉框选项",
    changeSubFormbleOptions: "<m> 改变一对一子表下拉框选项",
    // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
    // update-begin--author:liaozhiyang---date:20240321---for：【QQYUN-5806】js增强改变下拉搜索options
    changeRemoteOptions: "<m> 改变远程下拉框选项",
    // update-end--author:liaozhiyang---date:20240321---for：【QQYUN-5806】js增强改变下拉搜索options
    // update-begin--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
    submitFormAndFlow: "<m> 提交表单且发起流程"
    // update-end--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
  }, i = new Proxy(t, {
    get(b, P) {
      return Reflect.get(e, P);
    }
  });
  function c(b, P) {
    e[b] = P;
  }
  function r(b) {
    Object.keys(b).map((P) => {
      e[P] = b[P];
    });
  }
  c("$nextTick", Be), c("addObject2Context", c);
  const f = (b, P) => pe(() => {
    const { buttonSwitch: C } = n, N = {
      enabled: !0,
      buttonIcon: P[0],
      buttonName: P[1]
    };
    if ((C == null ? void 0 : C[b]) === !1)
      return N.enabled = !1, N;
    const { cgBIBtnMap: E } = n;
    return E != null && E[b] ? E[b] : N;
  }), v = f("form_sub_add", ["ant-design:plus-outlined", "新增"]), h = f("form_sub_batch_delete", ["ant-design:minus-outlined", "删除"]), y = f("form_sub_open_add", ["ant-design:expand-alt-outlined", "新增"]), O = f("form_sub_open_edit", ["ant-design:form-outlined", ""]);
  return {
    onlineFormContext: i,
    addObject2Context: c,
    resetContext: r,
    getSubAddBtnCfg: v,
    getSubRemoveBtnCfg: h,
    getSubOpenAddBtnCfg: y,
    getSubOpenEditBtnCfg: O
  };
}
function il(n, e) {
  const {
    config: { table: t, key: i, txt: c, linkField: r, idField: f, pidField: v, condition: h },
    others: y,
    order: O,
    title: b
  } = n;
  let P = {
    dictTable: t,
    dictText: c,
    dictCode: i,
    pidField: v,
    idField: f,
    view: ft,
    type: n.type
  }, C = [], N = ce({
    key: e,
    title: b,
    order: O,
    condition: h,
    origin: !0
  }, P);
  if (r && r.length > 0) {
    let E = r.split(",");
    N.next = E[0];
    for (let U = 0; U < E.length; U++)
      for (let B of y)
        if (B.field == E[U]) {
          let L = ce({
            key: B.field,
            title: B.title,
            order: B.order,
            origin: !1
          }, P);
          U + 1 < E.length && (L.next = E[U + 1]), C.push(L);
        }
  }
  return C.push(N), C;
}
function _t(n, e) {
  let t = -1;
  for (let i = 0; i < n.length; i++)
    if (n[i].field === e) {
      t = i;
      break;
    }
  return t;
}
function Ee(n) {
  return new Promise((e) => {
    (function t() {
      let i = n.value;
      i ? e(i) : setTimeout(() => {
        t();
      }, 100);
    })();
  });
}
function ll(n) {
  Object.keys(n).map((e) => {
    delete n[e];
  });
}
const sl = zn();
function rl(n, e) {
  let t = e.hideButtons, i = ci + n + ":";
  t || (t = []), sl.setOnlineSubTableAuth(i, t);
}
function Bs(n) {
  const e = D([]), t = {}, i = Se({}), c = D(!1), r = D([]), f = D({}), { getIsMobile: v } = At(), h = pe(() => {
    let C = n.formTemplate;
    return v.value ? 24 : C == "2" ? 12 : C == "3" ? 8 : C == "4" ? 6 : 24;
  });
  function y(C) {
    let N = [], E = [], U = {};
    Object.keys(C).map((B) => {
      const L = C[B];
      if (L.view == "tab") {
        c.value = !0;
        let J = {
          key: B,
          // 这个foreignKey是主表的字段
          foreignKey: L.foreignKey,
          describe: L.describe,
          relationType: L.relationType,
          requiredFields: L.required || [],
          order: L.order
        };
        L.relationType == 1 ? (t[B] = D(null), J.properties = L.properties) : (O(L), t[B] = D(), J.columns = L.columns, U[B] = [], i[B] = !1), N.push(J);
      } else if (L.view === ft) {
        let J = P(L, B);
        for (let A of J) {
          let Q = b(E, A.key), z = {
            field: A.key,
            label: A.title,
            view: A.view,
            order: A.order,
            dictTable: A.dictTable,
            linkField: A.linkField || ""
          };
          Q == -1 ? E.push(z) : E[Q] = z;
        }
      } else if (L.view != "hidden") {
        if (b(E, B) == -1) {
          let A = Object.assign(
            {
              field: B,
              label: L.title
            },
            Qe(L, ["view", "order", "fieldExtendJson", "dictTable", "dictText", "dictCode", "dict"])
          );
          if (L.view == "file" && (A.span = 24, A.isFile = !0), L.view == "image" && (A.span = 24, A.isImage = !0), L.view == "link_table" && L.fieldExtendJson)
            try {
              let Q = JSON.parse(L.fieldExtendJson);
              Q.showType != "select" && (A.isCard = !0), Q.multiSelect == !0 && (A.multi = !0);
            } catch (Q) {
            }
          (L.view == "umeditor" || L.view == "markdown") && (A.isHtml = !0, A.span = 24), E.push(A);
        }
      }
    }), E.sort(function(B, L) {
      return B.order - L.order;
    }), N.sort(function(B, L) {
      return B.order - L.order;
    }), r.value = N;
    for (let B = 0; B < E.length; B++) {
      let L = E[B];
      if ((L.isFile === !0 || L.isImage === !0 || L.isHtml === !0) && B > 0) {
        let J = E[B - 1], A = J.span || h.value;
        J.span = A;
      }
    }
    e.value = E, f.value = U;
  }
  function O(C) {
    Gt(C);
  }
  function b(C, N) {
    let E = -1;
    for (let U = 0; U < C.length; U++)
      if (C[U].field === N) {
        E = U;
        break;
      }
    return E;
  }
  function P(C, N) {
    let E = [];
    const {
      config: { table: U, key: B, txt: L, linkField: J },
      order: A,
      title: Q,
      others: z
    } = C;
    let T = {
      view: "link_down",
      order: A,
      title: Q,
      dictTable: JSON.stringify({
        table: U,
        key: B,
        txt: L
      })
    };
    if (E.push(Object.assign({}, { linkField: J, key: N }, T)), J) {
      let I = J.split(",");
      for (let _ of I) {
        let S = "";
        for (let M of z)
          M.field == _ && (S = M.title);
        E.push(Object.assign({}, { key: _ }, T, { title: S }));
      }
    }
    return E;
  }
  return {
    detailFormSchemas: e,
    hasSubTable: c,
    subTabInfo: r,
    refMap: t,
    showStatus: i,
    createFormSchemas: y,
    formSpan: h,
    subDataSource: f
  };
}
function ol(n, e = !0) {
  let t = Se({});
  const i = (b, P) => ge.get({ url: b, params: P }, { isTransformResponse: !1 }), c = (b, P) => ge.post({ url: b, params: P }, { isTransformResponse: !1 }), r = (b, P) => ge.put({ url: b, params: P }, { isTransformResponse: !1 }), f = (b, P) => ge.delete({ url: b, params: P }, { isTransformResponse: !1 });
  e === !0 ? (n._getAction = i, n._postAction = c, n._putAction = r, n._deleteAction = f, n._useMessage = ke) : (n.addObject2Context("_getAction", i), n.addObject2Context("_postAction", c), n.addObject2Context("_putAction", r), n.addObject2Context("_deleteAction", f), n.addObject2Context("_useMessage", ke));
  function v(b) {
    if (b) {
      let P, C;
      try {
        P = at(b), C = new P(i, c, f);
      } catch (N) {
        C = {};
        const { createMessage: E } = ke();
        E.warning(`js增强代码有语法错误，请检查代码~ ${N}`);
      }
      return C;
    } else
      return {};
  }
  function h(b, P) {
    t && t[P] && t[P](b);
  }
  function y(b, P) {
    return t && t.beforeSubmit ? t.beforeSubmit(b, P) : Promise.resolve();
  }
  function O(b, P) {
    return t && t.beforeDelete ? t.beforeDelete(b, P) : Promise.resolve();
  }
  return e === !0 && n && (n.beforeDelete = (b) => {
    const P = n.EnhanceJS;
    return P && P.beforeDelete ? P.beforeDelete(n, b) : Promise.resolve();
  }, n.beforeEdit = (b) => {
    const P = n.EnhanceJS;
    return P && P.beforeEdit ? P.beforeEdit(n, b) : Promise.resolve();
  }), {
    EnhanceJS: t,
    initCgEnhanceJs: v,
    customBeforeSubmit: y,
    beforeDelete: O,
    triggerJsFun: h
  };
}
const al = "/online/cgform/api/subform", ul = {
  name: "OnlineSubForm",
  components: {
    BasicForm: Rt,
    Loading: Nt
  },
  props: {
    properties: {
      type: Object,
      required: !0
    },
    mainId: {
      type: String,
      default: ""
    },
    table: {
      type: String,
      default: ""
    },
    formTemplate: {
      type: Number,
      default: 1
    },
    requiredFields: {
      type: Array,
      default: []
    },
    isUpdate: {
      type: Boolean,
      default: !1
    },
    disabled: {
      type: Boolean,
      default: !1
    }
  },
  emits: ["formChange"],
  setup(n, { emit: e }) {
    const t = D(null), i = D(!1);
    ke();
    const {
      formSchemas: c,
      defaultValueFields: r,
      changeDataIfArray2String: f,
      tableName: v,
      dbData: h,
      checkOnlyFieldValue: y,
      fieldDisplayStatus: O,
      createFormSchemas: b,
      baseColProps: P,
      labelCol: C,
      wrapperCol: N,
      labelWidth: E
    } = Zt(n, t), [U, { setProps: B, validate: L, resetFields: J, setFieldsValue: A, getFieldsValue: Q, updateSchema: z, scrollToField: X }] = kt({
      schemas: c,
      showActionButtonGroup: !1,
      baseColProps: P,
      // update-begin--author:liaozhiyang---date:20240429---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      labelWidth: E,
      // update-end--author:liaozhiyang---date:20240429---for：【QQYUN-7632】 label栅格改成labelwidth固宽
      // update-begin--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
      labelCol: C,
      wrapperCol: N
      // update-end--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
    });
    he(
      () => n.table,
      () => {
        v.value = n.table;
      },
      { immediate: !0 }
    ), he(
      () => n.properties,
      (o) => {
        i.value = !1, T(), b(n.properties, n.requiredFields, y), i.value = !0;
      },
      { deep: !0, immediate: !0 }
    ), he(
      () => n.mainId,
      (o) => {
        setTimeout(() => {
          _();
        }, 100);
      },
      { immediate: !0 }
    ), he(
      () => n.disabled,
      (o) => {
        B({ disabled: o });
      }
    );
    function T() {
      return Z(this, null, function* () {
        let o = yield Ee(t);
        o.$formValueChange = (a, s) => {
          let F = { [a]: s };
          e("formChange", F);
        };
      });
    }
    function I() {
      if (Ce(n.isUpdate) === !1) {
        let o = be(r[v.value]);
        rt(o, (a) => {
          A(a);
        });
      }
    }
    function _() {
      return Z(this, null, function* () {
        yield Ee(i), yield J(), I();
        const { table: o, mainId: a } = n;
        if (!o || !a)
          return;
        let s = yield S(o, a);
        h.value = s, yield A(s);
      });
    }
    function S(o, a) {
      let s = `${al}/${o}/${a}`;
      return new Promise((F, x) => {
        ge.get({ url: s }, { isTransformResponse: !1 }).then((u) => {
          u.success ? F(u.result) : x();
        });
      }).finally(() => {
        h.value = "";
      });
    }
    function M() {
      return new Promise((o, a) => {
        L().then(() => {
          let s = Q();
          s = f(s), o(s);
        }).catch((s) => {
          s.errorFields && (s.scrollToField = () => s.errorFields[0] && X(s.errorFields[0].name, { behavior: "smooth", block: "center" })), a(s);
        });
      });
    }
    function $() {
      let o = Q();
      return o.id || (o.id = "sub-change-temp-id"), {
        row: o,
        target: l
      };
    }
    function d(o) {
      A(o);
    }
    function p() {
      let o = Q(), a = be(r[v.value]);
      rt(a, (s) => {
        A(s);
      }, o);
    }
    const l = {
      onlineFormRef: t,
      baseColProps: P,
      formSchemas: c,
      registerForm: U,
      setFieldsValue: A,
      getFieldsValue: Q,
      getFormEvent: $,
      setValues: d,
      getAll: M,
      executeFillRule: p,
      sh: O,
      resetFields: J,
      updateSchema: z
    };
    return l;
  }
};
function cl(n, e, t, i, c, r) {
  const f = se("BasicForm");
  return ne(), Pe(f, {
    ref: "onlineFormRef",
    onRegister: n.registerForm
  }, null, 8, ["onRegister"]);
}
const Xt = /* @__PURE__ */ $e(ul, [["render", cl], ["__scopeId", "data-v-196bf574"]]), Vs = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: Xt
}, Symbol.toStringTag, { value: "Module" })), it = {
  optPre: "/online/cgform/api/form/",
  urlButtonAction: "/online/cgform/api/doButton"
}, dl = {
  name: "OnlinePopForm",
  components: {
    BasicForm: Rt,
    Loading: Nt,
    OnlineSubForm: Xt,
    PrinterOutlined: Ln,
    DiffOutlined: jn,
    FormOutlined: An
  },
  props: {
    id: {
      type: String,
      default: ""
    },
    formTemplate: {
      type: Number,
      default: 1
    },
    disabled: {
      type: Boolean,
      default: !1
    },
    isTree: {
      type: Boolean,
      default: !1
    },
    pidField: {
      type: String,
      default: ""
    },
    submitTip: {
      type: Boolean,
      default: !0
    },
    modalClass: {
      type: String,
      default: ""
    },
    //是否发送请求-即表单的保存/编辑请求，false则只将表单数据抛出去
    request: {
      type: Boolean,
      default: !0
    },
    // 是否是vxeTable上方按钮点击打开的表单数据
    isVxeTableData: {
      type: Boolean,
      default: !1
    }
  },
  emits: ["success", "rendered", "dataChange"],
  setup(n, { emit: e }) {
    const { createMessage: t } = ke(), [i, { openModal: c }] = Ie(), r = D(""), f = D(null), v = D(!0), h = D(!1), y = D(1), O = D(!1), b = D(!1), P = Se({
      reportPrintShow: 0,
      reportPrintUrl: "",
      joinQuery: 0,
      modelFullscreen: 0,
      modalMinWidth: ""
    }), { onlineFormContext: C, resetContext: N } = nl(), {
      formSchemas: E,
      defaultValueFields: U,
      changeDataIfArray2String: B,
      tableName: L,
      dbData: J,
      checkOnlyFieldValue: A,
      hasSubTable: Q,
      subTabInfo: z,
      refMap: X,
      subDataSource: T,
      baseColProps: I,
      createFormSchemas: _,
      fieldDisplayStatus: S,
      labelCol: M,
      wrapperCol: $,
      labelWidth: d
    } = Zt(n, f);
    let { EnhanceJS: p, initCgEnhanceJs: l } = ol(C, !1);
    const [o, { setProps: a, validate: s, resetFields: F, setFieldsValue: x, updateSchema: u, getFieldsValue: w, scrollToField: R }] = kt({
      schemas: E,
      showActionButtonGroup: !1,
      baseColProps: I,
      // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
      labelWidth: d,
      // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
      // update-begin--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
      labelCol: M,
      wrapperCol: $
      // update-end--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
    }), q = D(!1);
    function Y() {
      let m = n.disabled;
      q.value = m, a({ disabled: m });
    }
    function H(m, g, k) {
      return Z(this, null, function* () {
        yield F(), J.value = "";
        let W = Ce(m);
        b.value = W, W && (yield K(g)), yield Be(() => {
          !W && k && x(k), j(), Ge("js", "loaded"), Y();
        });
      });
    }
    function j() {
      if (Ce(b) === !1) {
        let m = be(U[L.value]);
        rt(m, (g) => {
          x(g);
        });
      }
    }
    function K(m) {
      return Z(this, null, function* () {
        let g = yield te(m.id);
        (!g || Object.keys(g).length == 0) && (g = ce({}, be(m))), J.value = Object.assign({}, g);
        let k = G.value, W = Qe(g, ...k);
        n.isVxeTableData === !0 && (W = Object.assign({}, W, m)), yield x(W);
      });
    }
    let G = pe(() => {
      let m = E.value, g = [];
      for (let k of m)
        g.push(k.field);
      return g;
    });
    function te(m) {
      let g = `${it.optPre}${n.id}/${m}`;
      return new Promise((k, W) => {
        ge.get({ url: g }, { isTransformResponse: !1 }).then((le) => {
          le.success ? k(le.result) : (W(), t.warning(le.message));
        }).catch(() => {
          W();
        });
      });
    }
    function ie(m) {
      return Z(this, null, function* () {
        y.value = m.head.tableType, L.value = m.head.tableName, v.value = m.head.tableType == 1, Fe(m.head.extConfigJson), _(m.schema.properties, m.schema.required, A, P), p = l(m.enhanceJs), e("rendered", P);
        let g = yield Ee(f);
        g.$formValueChange = (k, W, le) => {
          pn(k, W), le && x(le);
        };
      });
    }
    function Fe(m) {
      let g = { reportPrintShow: 0, reportPrintUrl: "", joinQuery: 0, modelFullscreen: 1, modalMinWidth: "", formLabelLength: null };
      m && (g = JSON.parse(m)), Object.keys(g).map((k) => {
        P[k] = g[k];
      });
    }
    function Ze() {
      v.value === !0 ? De() : V();
    }
    function V() {
      ae().then((m) => {
        ue(m);
      });
    }
    function ae() {
      let m = {};
      return new Promise((g, k) => {
        s().then(
          (W) => g(W),
          ({ errorFields: W }) => {
            k({
              code: je,
              key: L.value,
              // 滚动到未通过校验的字段上
              scrollToField: () => W[0] && R(W[0].name, { behavior: "smooth", block: "center" })
            });
          }
        );
      }).then((g) => (Object.assign(m, B(g)), Me())).then((g) => (Object.assign(m, g), Promise.resolve(m))).catch((g) => ((g === je || (g == null ? void 0 : g.code) === je) && (t.warning("校验未通过"), g.key && (ve(g.key), g.scrollToField && setTimeout(() => g.scrollToField(), 150))), Promise.reject(null)));
    }
    function ve(m) {
      let g = z.value;
      for (let k = 0; k < g.length; k++)
        if (m == g[k].key) {
          Le.value = k + "";
          break;
        }
    }
    function Me() {
      return new Promise((m, g) => Z(this, null, function* () {
        let k = {};
        try {
          let W = z.value;
          for (let le = 0; le < W.length; le++) {
            let ye = W[le].key, _e = X[ye].value;
            if (_e instanceof Array && (_e = _e[0]), W[le].relationType == 1)
              try {
                let Ye = yield _e.getAll();
                k[ye] = [], k[ye].push(Ye);
              } catch (Ye) {
                return g(ce({ code: je, key: ye }, Ye));
              }
            else {
              if (yield _e.fullValidateTable())
                return g({ code: je, key: ye });
              k[ye] = _e.getTableData();
            }
          }
        } catch (W) {
          g(W);
        }
        m(k);
      }));
    }
    function De() {
      return Z(this, null, function* () {
        try {
          let m = yield s();
          m = Object.assign({}, J.value, m), m = B(m), h.value = !0, ue(m);
        } catch (m) {
          if (Et(m)) {
            const g = m.errorFields;
            g != null && g.length && g[0].errors && (t.warning(g[0].errors[0]), R(g[0].name, { behavior: "smooth", block: "center" }));
          }
        } finally {
          h.value = !1;
        }
      });
    }
    function ue(m) {
      gn(bt, m).then(() => {
        Fn(m);
      }).catch((g) => {
        t.warning(g);
      });
    }
    function We(m, g, k) {
      g && k ? k.setValues ? k.setValues(m) : k.setValues([
        {
          rowKey: g,
          values: m
        }
      ]) : x(m);
    }
    function Je(m, g) {
      let k = {};
      k[m] = g, x(k);
    }
    const Le = D("0"), pt = D(300), mt = D(340);
    function on(m) {
      if (b.value === !0) {
        let g = J.value;
        return an(g, m);
      }
      return "";
    }
    function an(m, g) {
      if (m) {
        let k = m[g];
        return !k && k !== 0 && (k = m[g.toLowerCase()], !k && k !== 0 && (k = m[g.toUpperCase()])), k;
      }
      return "";
    }
    function un(m, g) {
      if (p && p[g + "_onlChange"]) {
        let k = p[g + "_onlChange"](), W = Object.keys(m)[0];
        if (k[W]) {
          let le = X[g].value;
          le instanceof Array && (le = le[0]);
          let ye = le.getFormEvent(), _e = ce({
            column: { key: W },
            value: m[W]
          }, ye);
          k[W].call(C, C, _e);
        }
      }
    }
    function cn(m, g) {
      if (p && p[g + "_onlChange"]) {
        let k = p[g + "_onlChange"](C);
        k[m.column.key] && k[m.column.key].call(C, C, m);
      }
    }
    function dn(m, g) {
    }
    function fn(m) {
      return "online_" + m + ":";
    }
    function pn(m, g) {
      return Z(this, null, function* () {
        if (J.value[m] != g && e("dataChange", m), !p || !p.onlChange || !m)
          return !1;
        let W = p.onlChange();
        if (W[m]) {
          let ye = {
            row: yield w(),
            column: { key: m },
            value: g
          };
          W[m].call(C, C, ye);
        }
      });
    }
    function Ge(m, g) {
      if (m == "js")
        p && p[g] && p[g].call(C, C);
      else if (m == "action") {
        let k = J.value, W = {
          formId: n.id,
          buttonCode: g,
          dataId: k.id,
          uiFormData: Object.assign({}, k)
        };
        ge.post(
          {
            url: `${it.urlButtonAction}`,
            params: W
          },
          { isTransformResponse: !1 }
        ).then((le) => {
          le.success ? t.success("处理完成!") : t.warning("处理失败!");
        });
      }
    }
    function ht(m) {
      let g = X[m].value, k = [...g.getNewDataWithId(), ...T.value[m]];
      if (!k || k.length == 0)
        return !1;
      let W = [];
      for (let le of k)
        W.push(le.id);
      g.removeRowsById(W);
    }
    function gt(m, g) {
      if (!g)
        return !1;
      let k = X[m].value;
      typeof g == "object" ? k.addRows(g, !0) : t.error("添加子表数据,参数不识别!");
    }
    function mn(m, g) {
      ht(m), gt(m, g);
    }
    function hn(m, g) {
      !g && g.length <= 0 && (g = []), g.map((k) => {
        k.hasOwnProperty("label") || (k.label = k.text);
      }), u({
        field: m,
        componentProps: {
          options: g
        }
      });
    }
    function gn(m, g) {
      return p && p.beforeSubmit ? p.beforeSubmit(m, g) : Promise.resolve();
    }
    function bn(m, g) {
      let k = be(S);
      m && m.length > 0 ? Object.keys(k).map((W) => {
        !W.endsWith("_load") && m.indexOf(W) < 0 && (S[W] = !1);
      }) : g && g.length > 0 && Object.keys(k).map((W) => {
        g.indexOf(W) >= 0 && (S[W] = !1);
      });
    }
    function vn(m) {
      return Z(this, null, function* () {
        yield F(), J.value = "", b.value = !0, yield K(m), yield Be(() => {
          Ge("js", "loaded");
        });
      });
    }
    function yn(m) {
      let g = X[m].value;
      return g instanceof Array && (g = g[0]), g;
    }
    function wn() {
      let m = P.reportPrintUrl, g = J.value.id, k = jt();
      qn(m, g, k);
    }
    function Cn(m) {
      r.value = m.id, c(!0);
    }
    function Sn(m) {
    }
    function Fn(m) {
      if (Object.keys(m).map((g) => {
        Array.isArray(m[g]) && m[g].length == 0 && (m[g] = "");
      }), n.request == !1)
        e("success", m);
      else {
        let g = `${it.optPre}${n.id}?tabletype=${y.value}`;
        O.value === !0 && (m[oi] = 1);
        let k = b.value === !0 ? "put" : "post";
        ge.request({ url: g, method: k, params: m }, { isTransformResponse: !1 }).then((W) => {
          W.success ? (W.result && (m.id || (m.id = W.result)), e("success", m), J.value = m, b.value = !0, t.success("操作成功!")) : t.warning(W.message);
        }).finally(() => {
          h.value = !1;
        });
      }
    }
    function _n() {
      return Z(this, null, function* () {
        let m = J.value, g = G.value, k = Qe(m, ...g);
        if (m)
          yield x(k);
        else {
          let W = {};
          for (let le of g)
            W[le] = "";
          yield x(W);
        }
      });
    }
    let bt = {
      tableName: L,
      loading: h,
      subActiveKey: Le,
      onlineFormRef: f,
      getFieldsValue: w,
      setFieldsValue: x,
      submitFlowFlag: O,
      subFormHeight: pt,
      subTableHeight: mt,
      refMap: X,
      triggleChangeValues: We,
      triggleChangeValue: Je,
      sh: S,
      clearSubRows: ht,
      addSubRows: gt,
      clearThenAddRows: mn,
      changeOptions: hn,
      isUpdate: b,
      getSubTableInstance: yn
    };
    return N(bt), {
      //主表
      tableName: L,
      onlineFormRef: f,
      registerForm: o,
      loading: h,
      //子表
      subActiveKey: Le,
      hasSubTable: Q,
      subTabInfo: z,
      refMap: X,
      //一对一子表
      subFormHeight: pt,
      getSubTableForeignKeyValue: on,
      isUpdate: b,
      handleSubFormChange: un,
      //一对多子表
      subTableHeight: mt,
      onlineFormDisabled: q,
      subDataSource: T,
      getSubTableAuthPre: fn,
      handleAdded: dn,
      handleValueChange: cn,
      openSubFormModalForAdd: Cn,
      openSubFormModalForEdit: Sn,
      registerVxeFormModal: i,
      vxeTableId: r,
      //父组件调用
      show: H,
      createRootProperties: ie,
      handleSubmit: Ze,
      sh: S,
      handleCgButtonClick: Ge,
      handleCustomFormSh: bn,
      handleCustomFormEdit: vn,
      //跳转
      dbData: J,
      onOpenReportPrint: wn,
      onlineExtConfigJson: P,
      recoverFormData: _n
    };
  }
};
const fl = ["id"];
function pl(n, e, t, i, c, r) {
  const f = se("BasicForm");
  return ne(), de("div", {
    id: i.tableName + "_form",
    class: qe(["onlinePopFormWrap", [`formTemplate_${t.formTemplate}`]])
  }, [
    fe(f, {
      ref: "onlineFormRef",
      onRegister: i.registerForm
    }, null, 8, ["onRegister"])
  ], 10, fl);
}
const en = /* @__PURE__ */ $e(dl, [["render", pl], ["__scopeId", "data-v-2c2d5706"]]), Ws = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: en
}, Symbol.toStringTag, { value: "Module" }));
function ml(n, { emit: e } = {}, t) {
  const i = D(null), c = D(!1), r = D(1), f = D([]), v = D(!1), h = D(0), y = D(!1), O = D(""), b = D(!1), P = D(!1), C = D(!0), N = Se({}), E = D(!0), U = D(""), B = D(!0), L = D(!1), { popModalFixedWidth: J, resetBodyStyle: A, popBodyStyle: Q } = tn(), z = D(!1), X = D(""), { getIsMobile: T } = At(), I = {
    handleOpenModal: (V) => {
    }
  }, _ = D(""), S = D(""), M = D(""), $ = D(!1);
  let d = {};
  const p = pe(() => {
    let V = U.value;
    return V || (Ce(c) === !0 ? "详情" : Ce(P) === !0 ? "编辑" : "新增");
  }), [l, { setModalProps: o, closeModal: a }] = It((V) => Z(this, null, function* () {
    U.value = "", L.value = !1, n === !0 ? yield I.handleOpenModal(V) : yield x(V), A(), t && t();
  })), s = D(!1);
  function F() {
    return Z(this, null, function* () {
      return yield Ee(s), z.value;
    });
  }
  function x(V) {
    return Z(this, null, function* () {
      o({ confirmLoading: !1 }), P.value = V.isUpdate, c.value = V.disableSubmit || !1, (V == null ? void 0 : V.hideSub) === !0 && (E.value = !1), V != null && V.title && (U.value = V.title), V != null && V.record ? M.value = V.record.id : M.value = "", yield Be(() => Z(this, null, function* () {
        yield Ee(v), w(), yield i.value.show(V == null ? void 0 : V.isUpdate, V == null ? void 0 : V.record, V == null ? void 0 : V.param);
      }));
    });
  }
  function u(V) {
    v.value = !0, h.value = V.modalMinWidth, V.modelFullscreen == 1 ? o({ defaultFullscreen: !0 }) : o({ defaultFullscreen: !1 }), d = V, T.value && (d.commentStatus = 0);
  }
  function w() {
    let V = M.value;
    d.commentStatus == 1 && V ? ($.value = !0, o({ defaultFullscreen: !0 })) : $.value = !1;
  }
  const R = 800, q = 1100, Y = pe(() => {
    let V = 200 * (r.value - 1), ae = (Ce(C) ? R : q) + V;
    ae = H(ae);
    let ve = h.value;
    return ve && ae < ve && (ae = ve), ae;
  });
  function H(V) {
    let ae = N.modalMinWidth;
    if (ae != null && ae !== "")
      try {
        if (ae = Number.parseInt(ae), V < ae)
          return ae;
      } catch (ve) {
      }
    return V;
  }
  function j(V, ae) {
    i.value.handleCgButtonClick(V, ae);
  }
  function K() {
    b.value = !0, setTimeout(() => {
      b.value = !1;
    }, 1500), i.value.handleSubmit();
  }
  function G() {
    a();
  }
  function te(V, ae = {}) {
    let ve = `/online/cgform/api/getFormItem/${V}`;
    return new Promise((Me, De) => {
      ge.get({ url: ve, params: ae }, { isTransformResponse: !1 }).then((ue) => {
        ue.success ? Me(ue.result) : De(ue.message);
      }).catch(() => {
        De();
      });
    });
  }
  function ie(V, ae, ve, Me, De) {
    return Z(this, null, function* () {
      let ue = null;
      if (Me && De) {
        const Je = `/online/cgform/api/getFormItemBytbname/${De}`, Le = { taskId: Me };
        ue = yield ge.get({ url: Je, params: Le });
      } else
        ue = yield te(V, ae);
      let We = ue.head.formTemplate;
      r.value = We ? Number(We) : 1, f.value = ue.cgButtonList, y.value = ue.head.isTree === "Y", O.value = ue.head.treeParentIdField || "", _.value = ue.head.id, S.value = ue.head.tableName, X.value = ue.head.themeTemplate, ue.form_disable_update === !0 ? z.value = !0 : z.value = !1, s.value = !0, e && e("formConfig", ue), ve && ve(ue), yield Be(() => Z(this, null, function* () {
        yield (yield Ee(i)).createRootProperties(ue);
      }));
    });
  }
  function Fe(V) {
    V[ai] = S.value, e("success", V), B.value == !0 && a(), L.value = !1, B.value = !0;
  }
  function Ze() {
    i.value && i.value.onCloseModal(), P.value && (d != null ? d : {}).commentStatus == 1 && o({ defaultFullscreen: !1 });
  }
  return {
    // modal
    title: p,
    modalWidth: Y,
    registerModal: l,
    closeModal: a,
    modalObject: I,
    onCloseEvent: Ze,
    // 自定义按钮
    cgButtonList: f,
    handleCgButtonClick: j,
    // 提交/关闭按钮
    disableSubmit: c,
    handleSubmit: K,
    submitLoading: b,
    handleCancel: G,
    successThenClose: B,
    handleSuccess: Fe,
    topTipVisible: L,
    //表单
    handleFormConfig: ie,
    onlineFormCompRef: i,
    formTemplate: r,
    isTreeForm: y,
    pidFieldName: O,
    renderSuccess: u,
    formRendered: v,
    isUpdate: P,
    showSub: E,
    themeTemplate: X,
    // 评论区域参数
    tableId: _,
    tableName: S,
    formDataId: M,
    enableComment: $,
    popBodyStyle: Q,
    popModalFixedWidth: J,
    getFormStatus: F
  };
}
function tn() {
  const e = D(800);
  let t = window.innerWidth - 300;
  t < 800 && (t = 800), e.value = t;
  const i = D({});
  function c() {
    let r = window.innerHeight - 210;
    i.value = {
      height: r + "px",
      overflowY: "auto"
    };
  }
  return {
    popModalFixedWidth: e,
    popBodyStyle: i,
    resetBodyStyle: c
  };
}
const hl = Ot({
  name: "OnlinePopModal",
  props: {
    /**可以是表名 可以是ID*/
    id: {
      type: String,
      default: ""
    },
    /*展示字段名*/
    showFields: {
      type: Array,
      default: () => []
    },
    /*隐藏字段名*/
    hideFields: {
      type: Array,
      default: () => []
    },
    topTip: {
      type: Boolean,
      default: !1
    },
    request: {
      type: Boolean,
      default: !0
    },
    saveClose: {
      type: Boolean,
      default: !1
    },
    // 是否是vxeTable上方按钮点击打开的表单数据
    isVxeTableData: {
      type: Boolean,
      default: !1
    },
    formTableType: {
      type: String,
      default: ""
    },
    // -update-begin--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
    // 有taskId即是流程
    taskId: {
      type: String
    },
    tableName: {
      type: String
    }
    // -update-end--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
  },
  components: {
    BasicModal: Mt,
    OnlinePopForm: en,
    JModalTip: si,
    Button: Xe
  },
  emits: ["success", "register", "formConfig"],
  setup(n, { emit: e }) {
    const {
      title: t,
      registerModal: i,
      cgButtonList: c,
      handleCgButtonClick: r,
      disableSubmit: f,
      handleSubmit: v,
      submitLoading: h,
      handleCancel: y,
      handleFormConfig: O,
      onlineFormCompRef: b,
      formTemplate: P,
      isTreeForm: C,
      pidFieldName: N,
      renderSuccess: E,
      formRendered: U,
      handleSuccess: B,
      topTipVisible: L,
      successThenClose: J,
      isUpdate: A,
      popBodyStyle: Q,
      popModalFixedWidth: z,
      getFormStatus: X
    } = ml(!1, { emit: e });
    he(() => n.id, T, { immediate: !0 });
    function T() {
      return Z(this, null, function* () {
        if (U.value = !1, !n.id)
          return;
        let p = {};
        n.formTableType && (p.tabletype = n.formTableType), n.taskId ? yield O(n.id, p, null, n.taskId, n.tableName) : yield O(n.id, p);
      });
    }
    function I() {
      n.saveClose === !1 && (J.value = !1), v();
    }
    function _() {
      L.value = !1, b.value.recoverFormData();
    }
    function S() {
      L.value = !0;
    }
    const M = pe(() => f.value || !A.value ? !1 : n.topTip), $ = pe(() => {
      if (A.value == !0)
        return null;
      {
        let p = h.value;
        return [
          Ae(Xe, { type: "primary", loading: p, onClick: v }, () => "确定"),
          Ae(Xe, { onClick: y }, () => "关闭")
        ];
      }
    });
    return {
      title: t,
      topTipVisible: L,
      handleSaveData: I,
      handleRecover: _,
      onlineFormCompRef: b,
      renderSuccess: E,
      registerModal: i,
      handleSubmit: v,
      handleSuccess: B,
      handleCancel: y,
      formTemplate: P,
      disableSubmit: f,
      cgButtonList: c,
      handleCgButtonClick: r,
      isTreeForm: C,
      pidFieldName: N,
      submitLoading: h,
      handleDataChange: S,
      isUpdate: A,
      showTopTip: M,
      modalFooter: $,
      popBodyStyle: Q,
      popModalFixedWidth: z,
      getFormStatus: X
    };
  }
});
function gl(n, e, t, i, c, r) {
  const f = se("j-modal-tip"), v = se("online-pop-form"), h = se("BasicModal");
  return ne(), Pe(h, Pt({
    width: n.popModalFixedWidth,
    dialogStyle: { top: "70px" },
    bodyStyle: n.popBodyStyle
  }, n.$attrs, {
    footer: n.modalFooter,
    cancelText: "关闭",
    onRegister: n.registerModal,
    wrapClassName: "jeecg-online-pop-modal",
    onOk: n.handleSubmit
  }), {
    title: oe(() => [
      Te(Oe(n.title) + " ", 1),
      n.showTopTip ? (ne(), Pe(f, {
        key: 0,
        visible: n.topTipVisible,
        onSave: n.handleSaveData,
        onCancel: n.handleRecover
      }, null, 8, ["visible", "onSave", "onCancel"])) : xe("", !0)
    ]),
    default: oe(() => [
      fe(v, {
        ref: "onlineFormCompRef",
        id: n.id,
        disabled: n.disableSubmit,
        "form-template": n.formTemplate,
        isTree: n.isTreeForm,
        pidField: n.pidFieldName,
        request: n.request,
        isVxeTableData: n.isVxeTableData,
        onRendered: n.renderSuccess,
        onSuccess: n.handleSuccess,
        onDataChange: n.handleDataChange,
        "modal-class": "jeecg-online-pop-modal"
      }, null, 8, ["id", "disabled", "form-template", "isTree", "pidField", "request", "isVxeTableData", "onRendered", "onSuccess", "onDataChange"])
    ]),
    _: 1
  }, 16, ["width", "bodyStyle", "footer", "onRegister", "onOk"]);
}
const Ke = /* @__PURE__ */ $e(hl, [["render", gl]]), Js = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: Ke
}, Symbol.toStringTag, { value: "Module" }));
function Tt(n, e) {
  const t = "/online/cgform/api/getData/" + n;
  return ge.get({ url: t, params: e });
}
function bl(n, e) {
  const t = "/online/cgform/api/getColumns/" + n;
  return ge.get({ url: t, params: e });
}
function nn(n) {
  const e = D("1"), t = D({}), i = D({}), c = D(""), r = Se({
    add: !0,
    update: !0
  }), f = pe(() => n.textField ? n.textField.split(",") : []), v = D([]), h = pe(() => {
    let T = v.value;
    return n.multi == !0 ? T.slice(0, 3) : T.slice(0, 6);
  });
  Mn(() => Z(this, null, function* () {
    if (n.tableName) {
      let I = n.valueField || "", _ = n.textField || "", S = [];
      if (I && S.push(I), _) {
        let $ = _.split(",");
        c.value = $[0];
        for (let d of $)
          S.push(d);
      }
      let M = n.imageField || "";
      M && S.push(M), t.value = {
        linkTableSelectFields: S.join(",")
      }, yield C(), yield N();
    }
  }));
  const y = pe(() => {
    let T = n.textField || "", I = [], _ = "";
    if (T) {
      let S = T.split(",");
      _ = S[0];
      for (let M = 0; M < S.length; M++)
        M > 0 && I.push(S[M]);
    }
    return {
      others: I,
      labelField: _
    };
  }), O = D([]), b = D([]), P = D({});
  function C() {
    return Z(this, null, function* () {
      let T = t.value;
      const I = yield bl(n.tableName, T);
      if (b.value = I.columns, I.columns) {
        let _ = n.imageField, S = I.columns.filter((M) => M.dataIndex != c.value && M.dataIndex != _);
        v.value = S;
      }
      if (P.value = I.dictOptions, I.hideColumns) {
        let _ = I.hideColumns;
        _.indexOf("add") >= 0 ? r.add = !1 : r.add = !0, _.indexOf("update") >= 0 ? r.update = !1 : r.update = !0;
      }
    });
  }
  function N() {
    return Z(this, null, function* () {
      let T = U(), _ = (yield Tt(n.tableName, T)).records, S = [], { others: M, labelField: $ } = y.value, d = n.imageField;
      if (_ && _.length > 0)
        for (let p of _) {
          let l = ce({}, p);
          E(l);
          let o = Object.assign({}, Qe(l, M), { id: l.id, label: l[$], value: l[n.valueField] });
          d && (o[d] = l[d]), S.push(o);
        }
      n.editBtnShow && S.push({}), O.value = S;
    });
  }
  function E(T) {
    let I = b.value, _ = P.value;
    for (let S of I) {
      const { dataIndex: M, customRender: $ } = S;
      if ((T[M] || T[M] === 0) && $ && $ == M && _[$]) {
        T[M] = Lt(_[$], T[M]);
        continue;
      }
      let d = T[M + "_dictText"];
      d && (T[M] = d);
    }
  }
  function U() {
    return Object.assign({ pageSize: 100, pageNo: e.value }, t.value, i.value);
  }
  function B(T) {
    if (!T)
      i.value = {};
    else {
      let I = f.value, _ = [], S = [];
      for (let M = 0; M < I.length; M++)
        M <= 1 && (S.push(I[M]), _.push({ field: I[M], rule: "like", val: T }));
      _.superQueryMatchType = "or", _.superQueryParams = encodeURI(JSON.stringify(_)), i.value = _;
    }
  }
  function L(T) {
    return Z(this, null, function* () {
      if (!T)
        return [];
      let I = n.valueField, _ = Re(ce({}, t.value), {
        pageSize: 100,
        pageNo: e.value
      });
      _.superQueryMatchType = "and";
      let S = [{ field: I, rule: "in", val: T }];
      _.superQueryParams = encodeURI(JSON.stringify(S));
      let $ = (yield Tt(n.tableName, _)).records, d = [];
      if ($ && $.length > 0)
        for (let p of $) {
          let l = ce({}, p);
          E(l), d.push(l);
        }
      return d;
    });
  }
  function J(T, I) {
    if (!T || T.length == 0)
      return !1;
    let _ = I.split(",");
    if (_.length != T.length)
      return !1;
    let S = !0;
    for (let M of T) {
      let $ = M[n.valueField];
      _.indexOf($) < 0 && (S = !1);
    }
    return S;
  }
  function A(T) {
    Object.keys(T).map((I) => {
      T[I] instanceof Array && (T[I] = T[I].join(","));
    });
  }
  function Q(T, I, _) {
    if (_ || (_ = {}), I && I.length > 0)
      for (let S of I) {
        let M = S.split(","), $ = M[0], d = M[1];
        if (T[$])
          T[$].push(_[d]);
        else {
          let p = _[d] || "";
          T[$] = [p];
        }
      }
  }
  function z(T) {
    if (n.imageField) {
      let I = T[n.imageField];
      return typeof I == "string" && (I = I.split(",")[0]), Ue(I);
    }
    return "";
  }
  const X = pe(() => !!n.imageField);
  return {
    pageNo: e,
    otherColumns: v,
    realShowColumns: h,
    selectOptions: O,
    reloadTableLinkOptions: N,
    textFieldArray: f,
    addQueryParams: B,
    tableColumns: b,
    transData: E,
    mainContentField: c,
    loadOne: L,
    compareData: J,
    formatData: A,
    initFormData: Q,
    getImageSrc: z,
    showImage: X,
    auths: r
  };
}
const vl = {
  name: "LinkTableSelect",
  components: {
    PlusOutlined: ot,
    EditOutlined: Nn,
    OnlinePopModal: Ke
  },
  props: {
    valueField: re.string.def(""),
    textField: re.string.def(""),
    tableName: re.string.def(""),
    multi: re.bool.def(!1),
    value: re.oneOfType([re.string, re.number, re.array]),
    linkFields: re.array.def([]),
    imageField: re.string.def(""),
    // update-begin--author:liaozhiyang---date:20240530---for：【TV360X-389】普通查询关联记录去掉编辑按钮
    editBtnShow: re.bool.def(!0)
    // update-end--author:liaozhiyang---date:20240530---for：【TV360X-389】普通查询关联记录去掉编辑按钮
  },
  emits: ["change", "update:value"],
  setup(n, { emit: e, attrs: t }) {
    const i = Rn("tableId", D(null)), c = D(), r = D([]), { auths: f, mainContentField: v, textFieldArray: h, selectOptions: y, reloadTableLinkOptions: O, addQueryParams: b, formatData: P, initFormData: C, getImageSrc: N, showImage: E } = nn(n), [U, { openModal: B }] = Ie(), L = pe(() => n.tableName), J = pe(() => n.multi === !0 ? Re(ce({}, t), {
      // update-end--author:liaozhiyang---date:20240617---for：【TV360X-988】关联记录组件下拉风格禁用未生效
      mode: "multiple"
    }) : ce({}, t));
    function A(d) {
      d == null || d.stopPropagation(), d == null || d.preventDefault(), B(!0, {});
    }
    function Q(d, p) {
      d == null || d.stopPropagation(), d == null || d.preventDefault(), f.update != !1 && B(!0, {
        isUpdate: !0,
        record: p
      });
    }
    const z = "custom:online:reload";
    Dt(() => {
      c.value && c.value.addEventListener(z, X);
    }), kn(() => {
      c.value && c.value.removeEventListener(z, X);
    });
    function X() {
      O();
    }
    function T(d) {
      return Z(this, null, function* () {
        try {
          const l = document.querySelectorAll(`.online-list-${i.value} .jeecg-basic-table-form-container.online-query-form .link-table-select-box`);
          l && l.length > 0 && l.forEach((o) => o.dispatchEvent(new Event(z)));
        } catch (l) {
        }
        yield O();
        let p = d[n.valueField];
        n.multi === !0 ? r.value = [p] : r.value = p, _(r.value);
      });
    }
    function I(d) {
      b(d), O();
    }
    function _(d) {
      S(d), d || (b(), O());
    }
    function S(d) {
      let p = {}, l = n.linkFields, o = [];
      if (!d)
        C(p, l);
      else {
        let a = be(y.value), s = be(d);
        s instanceof Array ? o = [...s] : n.multi == !0 ? o = s.split(",") : o = [s];
        let F = a.filter((x) => o.indexOf(x[n.valueField]) >= 0);
        if (F && F.length > 0) {
          let x = ce({}, F[0]);
          if (F.length > 1)
            for (let w = 1; w < F.length; w++)
              x = M(x, F[w]);
          let u = v.value;
          x[u] = x.label, C(p, l, x);
        }
      }
      P(p), e("change", o.join(",") || "", p), e("update:value", o.join(",") || "");
    }
    function M(d, p) {
      let l = {};
      return Object.keys(d).map((o) => {
        l[o] = (d[o] || "") + "," + (p[o] || "");
      }), l;
    }
    he(() => n.value, (d) => Z(this, null, function* () {
      d ? (n.multi == !0 ? r.value = d.split(",") : r.value = d, n.linkFields && n.linkFields.length > 0 && S(d)) : r.value = [];
    }), { immediate: !0 }), he(() => y.value, (d) => {
      d && d.length > 0 && n.linkFields && n.linkFields.length > 0 && r.value && r.value.length > 0 && S(r.value);
    });
    const $ = (d) => {
      d.target.src = ze;
    };
    return {
      boxRef: c,
      selectValue: r,
      selectOptions: y,
      registerPopModal: U,
      popTableName: L,
      textFieldArray: h,
      handleClickAdd: A,
      handleClickEdit: Q,
      getFormData: T,
      handleSearch: ri(I, 800),
      handleChange: _,
      bindValue: J,
      showImage: E,
      getImageSrc: N,
      auths: f,
      placeholderImage: ze,
      handleImageError: $
    };
  }
};
const yl = {
  class: "link-table-select-box",
  ref: "boxRef"
}, wl = {
  key: 1,
  class: "online-select-item"
}, Cl = {
  key: 0,
  class: "left-avatar"
}, Sl = ["src"], Fl = ["src"], _l = { class: "right-content" }, Tl = { class: "others" }, xl = { class: "other-item ellipsis" };
function Ol(n, e, t, i, c, r) {
  const f = se("PlusOutlined"), v = se("EditOutlined"), h = se("a-select"), y = se("online-pop-modal");
  return ne(), de("div", yl, [
    fe(h, Pt({
      value: i.selectValue,
      "onUpdate:value": e[2] || (e[2] = (O) => i.selectValue = O),
      style: { width: "100%" },
      placeholder: "请选择",
      "option-label-prop": "label",
      popupClassName: "table-link-select",
      allowClear: "",
      "show-search": ""
    }, i.bindValue, {
      options: i.selectOptions,
      "filter-option": !1,
      "not-found-content": null,
      onSearch: i.handleSearch,
      onChange: i.handleChange
    }), {
      option: oe((O) => [
        !O.value && i.auths.add ? (ne(), de("div", {
          key: 0,
          class: "opt-add",
          onClick: e[0] || (e[0] = (...b) => i.handleClickAdd && i.handleClickAdd(...b))
        }, [
          fe(f),
          e[3] || (e[3] = Te(" 记录 "))
        ])) : (ne(), de("div", wl, [
          i.showImage ? (ne(), de("div", Cl, [
            i.getImageSrc(O) ? (ne(), de("img", {
              key: 0,
              src: i.getImageSrc(O),
              alt: "",
              onError: e[1] || (e[1] = (...b) => i.handleImageError && i.handleImageError(...b))
            }, null, 40, Sl)) : (ne(), de("img", {
              key: 1,
              src: i.placeholderImage,
              alt: ""
            }, null, 8, Fl))
          ])) : xe("", !0),
          me("div", _l, [
            me("div", {
              class: qe(["label", { noEditBtn: !(t.editBtnShow && i.auths.update) }])
            }, [
              t.editBtnShow && i.auths.update ? (ne(), Pe(v, {
                key: 0,
                onClick: (b) => i.handleClickEdit(b, O)
              }, null, 8, ["onClick"])) : xe("", !0),
              Te(" " + Oe(O.label), 1)
            ], 2),
            me("div", Tl, [
              (ne(!0), de(He, null, lt(i.textFieldArray, (b) => (ne(), de("div", xl, Oe(O[b]), 1))), 256))
            ])
          ])
        ]))
      ]),
      _: 1
    }, 16, ["value", "options", "onSearch", "onChange"]),
    fe(y, {
      id: i.popTableName,
      onRegister: i.registerPopModal,
      onSuccess: i.getFormData,
      topTip: ""
    }, null, 8, ["id", "onRegister", "onSuccess"])
  ], 512);
}
const ln = /* @__PURE__ */ $e(vl, [["render", Ol], ["__scopeId", "data-v-76bee333"]]), Ys = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: ln
}, Symbol.toStringTag, { value: "Module" }));
function Pl(n, e) {
  let t = Xn();
  const i = D([]), c = D({}), r = D([]), f = D(null);
  let v = D(!0), h = pe(() => {
    if (v.value != !0)
      return { x: !1 };
  });
  const [y, { openModal: O }] = Ie(), b = D(""), [P, { openModal: C }] = Ie(), N = D("");
  function E(l, o = "checkbox") {
    c.value = l.dictOptions, l.checkboxFlag == "Y" ? f.value = {
      selectedRowKeys: r,
      onChange: U,
      type: o
    } : f.value = null, v.value = l.scrollFlag == 1;
    let a = l.columns;
    a.forEach((u) => {
      var w;
      if (u.fieldExtendJson && JSON.parse(u.fieldExtendJson).isFixed && (u.fixed = "left"), u.hrefSlotName && u.scopedSlots) {
        const R = (w = l.fieldHrefSlots) == null ? void 0 : w.find((q) => q.slotName === u.hrefSlotName);
        R && (u.fieldHref = R);
      }
      Object.keys(u).map((R) => {
        u[R] == null && delete u[R];
      });
    });
    let s = l.fieldHrefSlots;
    const F = {};
    s.forEach((u) => F[u.slotName] = u);
    let x = [];
    if (x = B(a, F), T(x), n.isTree() === !0) {
      let u = l.textField, w = -1;
      for (let R = 0; R < x.length; R++)
        if (x[R].dataIndex == u) {
          w = R;
          break;
        }
      if (w > 0) {
        let R = x.splice(w, 1);
        x.unshift(R[0]);
      }
      x.length > 0 && (x[0].align = "left");
    }
    i.value = x, n.reloadTable();
  }
  function U(l, o) {
    r.value = l, n.selectedRows = be(o), n.selectedRowKeys = be(l);
  }
  function B(l, o) {
    var a;
    for (let s of l) {
      let { customRender: F, hrefSlotName: x, fieldType: u } = s;
      if (u == "date" || u == "Date")
        s.customRender = ({ text: w }) => w ? w.length > 10 ? w.substring(0, 10) : w : "";
      else if (u == "link_table") {
        const w = (a = s.fieldExtendJson) != null ? a : "{}", R = JSON.parse(w);
        s.customRender = ({ text: q, record: Y }) => {
          if (!q)
            return "";
          if (n.isPopList === !0)
            return Y[s.dataIndex + "_dictText"];
          {
            let H = (q + "").split(","), j = [];
            Y[s.dataIndex + "_dictText"] && (j = Y[s.dataIndex + "_dictText"].split(","));
            let K = [];
            for (let G = 0; G < H.length; G++) {
              let te = Ae(
                ni,
                {
                  id: H[G],
                  text: j[G],
                  onTab: (ie) => p(ie, x, R.isListReadOnly)
                }
              );
              K.push(te);
            }
            return K.length == 0 ? "" : Ae("div", { style: { overflow: "hidden" } }, K);
          }
        };
      } else if (u === "popup_dict")
        s.customRender = ({ text: w, record: R }) => R[s.dataIndex + "_dictText"] != null ? R[s.dataIndex + "_dictText"] : w;
      else {
        if (!x && s.scopedSlots && s.scopedSlots.customRender && o.hasOwnProperty(s.scopedSlots.customRender) && (x = s.scopedSlots.customRender), F || x) {
          let w = F, R = "_replace_text_";
          s.ellipsis = !0, s.customRender = ({ text: q, record: Y }) => {
            let H = q;
            if (w)
              if (w.startsWith(R)) {
                let j = w.replace(R, "");
                H = Y[j];
              } else
                H = Lt(Ce(c)[w], q + "");
            if (s.showLength && H && H.length > s.showLength && (H = H.substr(0, s.showLength) + "..."), x) {
              let j = o[x];
              if (j)
                return Ae(
                  "a",
                  {
                    onClick: () => L(j, Y)
                  },
                  H
                );
            }
            return H;
          };
        }
        if (s.scopedSlots) {
          s.ellipsis = !0;
          let w = s.scopedSlots;
          s.slots = w, delete s.scopedSlots;
        }
      }
    }
    return l;
  }
  function L(l, o) {
    let a = l.href, s = /(ht|f)tp(s?)\:\/\/[0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*(:(0-9)*)*(\/?)([a-zA-Z0-9\-\.\?\,\'\/\\\+&amp;%\$#_]*)?/, F = /\.vue(\?.*)?$/, x = /{{([^}]+)}}/g;
    if (typeof a == "string")
      if (a.startsWith("ONLINE:")) {
        let u = a.split(":");
        b.value = u[1];
        let w = u[2];
        O(!0, {
          isUpdate: !0,
          disableSubmit: !0,
          hideSub: !0,
          record: { id: o[w] }
        });
      } else
        a = a.trim().replace(/\${([^}]+)?}/g, (u, w) => o[w]), x.test(a) && (a = a.replace(x, function(u, w) {
          try {
            return w.trim() === "ACCESS_TOKEN" ? jt() : at(w);
          } catch (R) {
            return u;
          }
        })), s.test(a) ? window.open(a, "_blank") : F.test(a) ? Q(a) : t.push(a);
  }
  const A = Se({
    model: {
      title: "",
      okText: "关闭",
      width: "100%",
      open: !1,
      destroyOnClose: !0,
      style: {
        top: 0,
        left: 0,
        height: "100%",
        margin: 0,
        padding: 0
      },
      // dialogStyle: dialogStyle,
      bodyStyle: { padding: "8px", height: "calc(100vh - 108px)", overflow: "auto", overflowX: "hidden" },
      // 隐藏掉取消按钮
      cancelButtonProps: { style: { display: "none" } }
    },
    on: {
      ok: () => A.model.open = !1,
      cancel: () => A.model.open = !1
    },
    is: null,
    params: {}
  });
  function Q(l) {
    let o = l.indexOf("?"), a = l;
    if (o !== -1) {
      a = l.substring(0, o);
      let F = l.substring(o + 1, l.length).split("&"), x = {};
      F.forEach((u) => {
        let w = u.split("=");
        x[w[0]] = w[1];
      }), A.params = x;
    } else
      A.params = {};
    A.model.open = !0, A.model.title = "操作", A.is = En($n(() => Hn(a)));
  }
  let z = "left";
  n.isTree() && (z = "right");
  const X = Se({
    title: "操作",
    dataIndex: "action",
    slots: { customRender: "action" },
    fixed: z,
    align: "center",
    width: 150
  });
  he(() => e == null ? void 0 : e.value, () => {
    var l, o;
    ((l = e == null ? void 0 : e.value) == null ? void 0 : l.tableFixedAction) === 1 && (X.fixed = ((o = e == null ? void 0 : e.value) == null ? void 0 : o.tableFixedActionType) || "right", n.isTree() && (X.fixed = "right"));
  });
  function T(l) {
    let o = !1;
    for (let a = 0; a < l.length; a++)
      if (l[a].dataIndex.toLowerCase() == "bpm_status") {
        o = !0;
        break;
      }
    return n.hasBpmStatus = o, o;
  }
  function I(l, o, a, s) {
    if (l)
      if (l.indexOf(",") > 0)
        ii(`/online/cgform/field/download/${s}/${o.id}/${a.dataIndex}`, `文件_${o.id}.zip`);
      else {
        const F = Ue(l);
        window.open(F);
      }
  }
  function _(l) {
    return l && l.indexOf(",") > 0 && (l = Ct(l)[0]), Ue(l);
  }
  function S(l) {
    return l ? ei(l) : "";
  }
  function M(l, o) {
    if (!l)
      return "";
    let a = l;
    a.length > 10 && (a = a.substring(0, 10));
    let s = o == null ? void 0 : o.fieldExtendJson;
    return s && (s = JSON.parse(s), s.picker && s.picker != "default") ? Qn(a)[s.picker] : a;
  }
  he(r, () => {
    n.selectedRowKeys = be(r.value);
  }), n.clearSelectedRow = () => {
    r.value = [], n.selectedRows = [], n.selectedRowKeys = [];
  };
  function $(l) {
    if (l) {
      let o = [];
      const a = Ct(l);
      for (let s of a)
        s && o.push(Ue(s));
      ti({ imageList: o });
    }
  }
  const d = D();
  function p(l, o, a) {
    return Z(this, null, function* () {
      N.value = o, (yield d.value.getFormStatus()) == !0 ? (b.value = o, O(!0, {
        isUpdate: !0,
        disableSubmit: !0,
        hideSub: !0,
        record: { id: l }
      })) : C(!0, {
        isUpdate: !0,
        // update-begin--author:liaozhiyang---date:20250318---for：【issues/7930】表格列表中支持关联记录配置是否只读
        disableSubmit: !!a,
        // update-end--author:liaozhiyang---date:20250318---for：【issues/7930】表格列表中支持关联记录配置是否只读
        record: {
          id: l
        }
      });
    });
  }
  return {
    columns: i,
    actionColumn: X,
    selectedKeys: r,
    rowSelection: f,
    enableScrollBar: v,
    tableScroll: h,
    downloadRowFile: I,
    getImgView: _,
    getPcaText: S,
    getFormatDate: M,
    handleColumnResult: E,
    onSelectChange: U,
    hrefComponent: A,
    viewOnlineCellImage: $,
    hrefMainTableId: b,
    registerOnlineHrefModal: y,
    registerPopModal: P,
    openPopModal: C,
    openOnlineHrefModal: O,
    onlinePopModalRef: d,
    popTableId: N,
    handleClickFieldHref: L
  };
}
const Dl = Ot({
  name: "OnlinePopListModal",
  props: {
    /**可以是表名 可以是ID*/
    id: {
      type: String,
      default: ""
    },
    multi: {
      type: Boolean,
      default: !1
    },
    addAuth: {
      type: Boolean,
      default: !0
    }
  },
  components: {
    BasicModal: Mt,
    BasicTable: Kn,
    TableAction: Zn,
    PlusOutlined: ot,
    OnlinePopModal: Ke
  },
  emits: ["success", "register"],
  setup(n, { emit: e }) {
    const { createMessage: t } = ke(), { popModalFixedWidth: i, resetBodyStyle: c, popBodyStyle: r } = tn(), f = D(""), v = D(800), [h, { closeModal: y }] = It((j) => {
      f.value = "", s.value = j.selectedRowKeys, F.value = j.selectedRows, o({ current: 1 }), l(), c();
    }), [O, { openModal: b }] = Ie();
    function P() {
      y();
    }
    const C = pe(() => {
      const j = s.value;
      return !(j && j.length > 0);
    }), N = D(!1);
    function E() {
      N.value = !0;
      let j = be(F.value);
      j && j.length > 0 && (e("success", j), y()), setTimeout(() => {
        N.value = !1;
      }, 200);
    }
    function U(j) {
      const K = "/online/cgform/api/getData/" + n.id;
      return ge.get({ url: K, params: j });
    }
    function B(j) {
      return j.column = "id", new Promise((K, G) => Z(this, null, function* () {
        const te = yield U(j);
        K(te);
      }));
    }
    const L = {
      isPopList: !0,
      reloadTable() {
      },
      isTree() {
        return !1;
      }
    }, J = D({}), {
      columns: A,
      downloadRowFile: Q,
      getImgView: z,
      getPcaText: X,
      getFormatDate: T,
      handleColumnResult: I,
      hrefComponent: _,
      viewOnlineCellImage: S
    } = Pl(L, J);
    function M() {
      const j = "/online/cgform/api/getColumns/" + n.id;
      return new Promise((K, G) => {
        ge.get({ url: j }, { isTransformResponse: !1 }).then((te) => {
          te.success ? K(te.result) : (t.warning(te.message), G());
        });
      });
    }
    const $ = D("");
    he(() => n.id, () => Z(this, null, function* () {
      let j = yield M();
      I(j), $.value = j.description;
    }), { immediate: !0 });
    const { tableContext: d } = Gn({
      designScope: "process-design",
      pagination: !0,
      tableProps: {
        title: "",
        api: B,
        clickToRowSelect: !0,
        columns: A,
        showTableSetting: !1,
        immediate: !1,
        //showIndexColumn: true,
        canResize: !1,
        showActionColumn: !1,
        actionColumn: {
          dataIndex: "action",
          slots: { customRender: "action" }
        },
        useSearchForm: !1,
        beforeFetch: (j) => q(j)
      }
    }), [p, { reload: l, setPagination: o }, { rowSelection: a, selectedRowKeys: s, selectedRows: F }] = d;
    he(() => n.multi, (j) => {
      j == !0 ? a.type = "checkbox" : a.type = "radio";
    }, { immediate: !0 });
    function x(j) {
      return [
        {
          label: "编辑",
          onClick: u.bind(null, j)
        }
      ];
    }
    function u(j) {
    }
    function w() {
      l();
    }
    const R = ["int", "double", "Date", "Datetime", "BigDecimal"];
    function q(j) {
      let K = f.value;
      if (!K)
        return j.superQueryMatchType = "or", j.superQueryParams = "", j;
      let G = A.value, te = [];
      if (G && G.length > 0)
        for (let ie of G)
          ie.dbType && (ie.dbType == "string" ? te.push({ field: ie.dataIndex, type: ie.dbType.toLowerCase(), rule: "like", val: K }) : ie.dbType == "Date" ? K.length == 10 && te.push({ field: ie.dataIndex, type: ie.dbType.toLowerCase(), rule: "eq", val: K }) : ie.dbType == "Datetime" ? K.length == 19 && te.push({ field: ie.dataIndex, type: ie.dbType.toLowerCase(), rule: "eq", val: K }) : R.indexOf(ie.dbType) && te.push({ field: ie.dataIndex, type: ie.dbType.toLowerCase(), rule: "eq", val: K }));
      return j.superQueryMatchType = "or", j.superQueryParams = encodeURI(JSON.stringify(te)), j;
    }
    function Y() {
      b(!0, {});
    }
    function H(j) {
      e("success", [j]), y();
    }
    return {
      registerModal: h,
      modalWidth: v,
      handleCancel: P,
      submitDisabled: C,
      submitLoading: N,
      handleSubmit: E,
      registerTable: p,
      getTableAction: x,
      searchText: f,
      onSearch: w,
      downloadRowFile: Q,
      getImgView: z,
      getPcaText: X,
      getFormatDate: T,
      hrefComponent: _,
      viewOnlineCellImage: S,
      rowSelection: a,
      modalTitle: $,
      registerPopModal: O,
      handleAdd: Y,
      reload: l,
      popModalFixedWidth: i,
      popBodyStyle: r,
      handleDataSave: H
    };
  }
}), Il = { style: { display: "inline-block", width: "calc(100% - 140px)", "text-align": "left" } }, Ml = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, Rl = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, kl = ["src", "onClick"], El = ["innerHTML"], $l = ["title"];
function Ll(n, e, t, i, c, r) {
  const f = se("PlusOutlined"), v = se("a-button"), h = se("a-input-search"), y = se("TableAction"), O = se("BasicTable"), b = se("BasicModal"), P = se("online-pop-modal");
  return ne(), de(He, null, [
    fe(b, {
      onRegister: n.registerModal,
      width: n.popModalFixedWidth,
      dialogStyle: { top: "70px" },
      bodyStyle: n.popBodyStyle,
      title: n.modalTitle,
      wrapClassName: "jeecg-online-pop-list-modal"
    }, {
      footer: oe(() => [
        me("div", Il, [
          n.addAuth ? (ne(), Pe(v, {
            key: 0,
            style: { "border-radius": "50px" },
            type: "primary",
            onClick: n.handleAdd
          }, {
            default: oe(() => [
              fe(f),
              e[1] || (e[1] = Te("新增记录"))
            ]),
            _: 1
          }, 8, ["onClick"])) : xe("", !0)
        ]),
        fe(v, {
          key: "back",
          onClick: n.handleCancel
        }, {
          default: oe(() => e[2] || (e[2] = [
            Te("关闭")
          ])),
          _: 1
        }, 8, ["onClick"]),
        fe(v, {
          disabled: n.submitDisabled,
          key: "submit",
          type: "primary",
          onClick: n.handleSubmit,
          loading: n.submitLoading
        }, {
          default: oe(() => e[3] || (e[3] = [
            Te("确定")
          ])),
          _: 1
        }, 8, ["disabled", "onClick", "loading"])
      ]),
      default: oe(() => [
        fe(O, {
          onRegister: n.registerTable,
          rowSelection: n.rowSelection
        }, {
          tableTitle: oe(() => [
            fe(h, {
              value: n.searchText,
              "onUpdate:value": e[0] || (e[0] = (C) => n.searchText = C),
              onSearch: n.onSearch,
              placeholder: "请输入关键词，按回车搜索",
              style: { width: "240px" }
            }, null, 8, ["value", "onSearch"])
          ]),
          action: oe(({ record: C }) => [
            fe(y, {
              actions: n.getTableAction(C)
            }, null, 8, ["actions"])
          ]),
          fileSlot: oe(({ text: C }) => [
            C ? (ne(), Pe(v, {
              key: 1,
              ghost: !0,
              type: "primary",
              preIcon: "ant-design:download",
              size: "small",
              onClick: (N) => n.downloadRowFile(C)
            }, {
              default: oe(() => e[4] || (e[4] = [
                Te(" 下载 ")
              ])),
              _: 2
            }, 1032, ["onClick"])) : (ne(), de("span", Ml, "无文件"))
          ]),
          imgSlot: oe(({ text: C }) => [
            C ? (ne(), de("img", {
              key: 1,
              src: n.getImgView(C),
              alt: "图片不存在",
              class: "online-cell-image",
              onClick: (N) => n.viewOnlineCellImage(C)
            }, null, 8, kl)) : (ne(), de("span", Rl, "无图片"))
          ]),
          htmlSlot: oe(({ text: C }) => [
            me("div", { innerHTML: C }, null, 8, El)
          ]),
          pcaSlot: oe(({ text: C }) => [
            me("div", {
              title: n.getPcaText(C)
            }, Oe(n.getPcaText(C)), 9, $l)
          ]),
          dateSlot: oe(({ text: C, column: N }) => [
            me("span", null, Oe(n.getFormatDate(C, N)), 1)
          ]),
          _: 1
        }, 8, ["onRegister", "rowSelection"])
      ]),
      _: 1
    }, 8, ["onRegister", "width", "bodyStyle", "title"]),
    fe(P, {
      id: n.id,
      onRegister: n.registerPopModal,
      onSuccess: n.handleDataSave,
      topTip: ""
    }, null, 8, ["id", "onRegister", "onSuccess"])
  ], 64);
}
const sn = /* @__PURE__ */ $e(Dl, [["render", Ll]]), Us = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: sn
}, Symbol.toStringTag, { value: "Module" }));
const jl = {
  name: "LinkTableCard",
  props: {
    valueField: re.string.def(""),
    textField: re.string.def(""),
    tableName: re.string.def(""),
    multi: re.bool.def(!1),
    value: re.oneOfType([re.string, re.number]),
    // ["表单字段,表字典字段","表单字段,表字典字段"]
    linkFields: re.array.def([]),
    //是否是禁用页面
    disabled: re.bool.def(!1),
    // 是否是detail页面
    detail: re.bool.def(!1),
    imageField: re.string.def("")
  },
  components: {
    PlusOutlined: ot,
    MinusCircleFilled: Bn,
    OnlinePopListModal: sn,
    OnlinePopModal: Ke
  },
  emits: ["change", "update:value"],
  setup(n, { emit: e }) {
    const t = pe(() => n.tableName), [i, { openModal: c }] = Ie(), [r, { openModal: f }] = Ie(), v = D([]), h = D([]), y = D(null), O = D(0), b = pe(() => !(n.disabled == !0 || n.multi === !1 && h.value.length > 0)), { auths: P, otherColumns: C, realShowColumns: N, tableColumns: E, textFieldArray: U, transData: B, loadOne: L, compareData: J, formatData: A, initFormData: Q, getImageSrc: z, showImage: X } = nn(n), T = pe(() => n.multi === !0 ? 12 : 24), I = pe(() => n.multi === !0 ? 24 : 12);
    function _(s) {
      if (s && U.value.length > 0) {
        let F = U.value[0];
        return s[F];
      }
    }
    function S(s) {
      s == null || s.stopPropagation(), s == null || s.preventDefault();
    }
    function M(s, F) {
      S(s), P.update != !1 && n.disabled == !1 && f(!0, {
        isUpdate: !0,
        record: F
      });
    }
    function $(s) {
      c(!0, {
        // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-43】修复关联记录可以添加重复数据
        selectedRowKeys: h.value.map((F) => F.id),
        selectedRows: [...h.value]
        // update-end--author:liaozhiyang---date:20240517---for：【TV360X-43】修复关联记录可以添加重复数据
      });
    }
    function d(s) {
      let F = [];
      for (let x of s) {
        let u = ce({}, x);
        B(u), F.push(u);
      }
      h.value = F, o();
    }
    function p(s) {
      let F = h.value;
      for (let x = 0; x < F.length; x++)
        if (F[x].id === s.id) {
          let u = ce({}, s);
          B(u), F.splice(x, 1, u);
        }
      h.value = F, o();
    }
    function l(s, F) {
      S(s);
      let x = h.value;
      x && x.length > F && (x.splice(F, 1), h.value = x), o();
    }
    function o() {
      let s = h.value, F = [], x = {}, u = n.linkFields;
      if (s.length > 0)
        for (let R = 0; R < s.length; R++)
          F.push(s[R][n.valueField]), Q(x, u, s[R]);
      else
        Q(x, u);
      let w = F.join(",");
      A(x), e("change", w, x), e("update:value", w);
    }
    return he(() => n.value, (s) => Z(this, null, function* () {
      if (s) {
        if (J(h.value, s) === !1) {
          let x = yield L(s);
          h.value = x;
        }
        n.linkFields && n.linkFields.length > 0 && o();
      } else
        h.value = [];
    }), { immediate: !0 }), Dt(() => {
      y.value.offsetWidth < 250 && (O.value = 24);
    }), {
      popTableName: t,
      selectRecords: h,
      otherColumns: C,
      realShowColumns: N,
      showButton: b,
      selectValue: v,
      handleAddRecord: $,
      handleDeleteRecord: l,
      getMainContent: _,
      itemSpan: T,
      columnSpan: I,
      tableColumns: E,
      addCard: d,
      registerListModal: i,
      registerFormModal: r,
      handleClickEdit: M,
      updateCardData: p,
      getImageSrc: z,
      showImage: X,
      auths: P,
      tableLinkCardRef: y,
      fixedSpan: O,
      placeholderImage: ze,
      handleImageError: (s) => {
        s.target.src = ze;
      }
    };
  }
}, Al = { ref: "tableLinkCardRef" }, Nl = { class: "table-link-card" }, Bl = { style: { width: "100%", height: "100%" } }, Vl = {
  key: 0,
  class: "card-button"
}, Wl = ["onClick"], Jl = {
  key: 0,
  class: "card-delete"
}, Yl = { class: "card-inner" }, Ul = { class: "card-main-content" }, ql = { class: "other-content" }, Hl = { class: "label ellipsis" }, Ql = { class: "text ellipsis" }, zl = {
  key: 0,
  class: "card-item-image"
}, Kl = ["src"];
function Zl(n, e, t, i, c, r) {
  const f = se("PlusOutlined"), v = se("a-button"), h = se("minus-circle-filled"), y = se("a-col"), O = se("a-row"), b = se("online-pop-list-modal"), P = se("online-pop-modal");
  return ne(), de("div", Al, [
    me("div", Nl, [
      me("div", Bl, [
        i.showButton ? (ne(), de("div", Vl, [
          fe(v, { onClick: i.handleAddRecord }, {
            default: oe(() => [
              fe(f),
              e[1] || (e[1] = Te("记 录"))
            ]),
            _: 1
          }, 8, ["onClick"])
        ])) : xe("", !0),
        fe(O, null, {
          default: oe(() => [
            (ne(!0), de(He, null, lt(i.selectRecords, (C, N) => (ne(), Pe(y, {
              span: i.fixedSpan ? i.fixedSpan : i.itemSpan
            }, {
              default: oe(() => [
                me("div", {
                  class: qe(["card-item", { "disabled-chunk": t.detail == !0 }]),
                  onClick: (E) => i.handleClickEdit(E, C)
                }, [
                  me("div", {
                    class: qe(["card-item-left", { "show-right-image": i.getImageSrc(C) }])
                  }, [
                    t.disabled == !1 ? (ne(), de("span", Jl, [
                      fe(h, {
                        onClick: (E) => i.handleDeleteRecord(E, N)
                      }, null, 8, ["onClick"])
                    ])) : xe("", !0),
                    me("div", Yl, [
                      me("div", Ul, Oe(i.getMainContent(C)), 1),
                      me("div", ql, [
                        fe(O, null, {
                          default: oe(() => [
                            (ne(!0), de(He, null, lt(i.realShowColumns, (E) => (ne(), Pe(y, { span: i.columnSpan }, {
                              default: oe(() => [
                                me("span", Hl, Oe(E.title), 1),
                                me("span", Ql, Oe(C[E.dataIndex]), 1)
                              ]),
                              _: 2
                            }, 1032, ["span"]))), 256))
                          ]),
                          _: 2
                        }, 1024)
                      ])
                    ])
                  ], 2),
                  i.getImageSrc(C) ? (ne(), de("div", zl, [
                    i.getImageSrc(C) ? (ne(), de("img", {
                      key: 0,
                      src: i.getImageSrc(C),
                      alt: "",
                      onError: e[0] || (e[0] = (...E) => i.handleImageError && i.handleImageError(...E))
                    }, null, 40, Kl)) : xe("", !0)
                  ])) : xe("", !0)
                ], 10, Wl)
              ]),
              _: 2
            }, 1032, ["span"]))), 256))
          ]),
          _: 1
        })
      ])
    ]),
    fe(b, {
      onRegister: i.registerListModal,
      multi: t.multi,
      id: i.popTableName,
      addAuth: i.auths.add,
      onSuccess: i.addCard
    }, null, 8, ["onRegister", "multi", "id", "addAuth", "onSuccess"]),
    fe(P, {
      id: i.popTableName,
      onRegister: i.registerFormModal,
      onSuccess: i.updateCardData,
      topTip: ""
    }, null, 8, ["id", "onRegister", "onSuccess"])
  ], 512);
}
const rn = /* @__PURE__ */ $e(jl, [["render", Zl], ["__scopeId", "data-v-6c31f866"]]), qs = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: rn
}, Symbol.toStringTag, { value: "Module" })), xt = {};
function Gl() {
  n("OnlineSelectCascade", li), n("LinkTableSelect", ln), n("LinkTableCard", rn);
  function n(t, i) {
    xt[t] || (In(t, i), xt[t] = 1);
  }
  function e(t) {
    t.component == "LinkTableCard" && (t.component = "LinkTableSelect", t.componentProps.popContainer = "body");
  }
  return {
    addComponent: n,
    linkTableCard2Select: e
  };
}
export {
  Ls as E,
  et as F,
  rn as L,
  Ke as O,
  oi as S,
  je as V,
  Pl as a,
  ml as b,
  Xt as c,
  nl as d,
  Zt as e,
  Es as f,
  Ee as g,
  ai as h,
  Gl as i,
  we as j,
  Ns as k,
  rt as l,
  Bs as m,
  js as n,
  As as o,
  ft as p,
  il as q,
  _t as r,
  $s as s,
  Vs as t,
  ol as u,
  Ws as v,
  Js as w,
  Ys as x,
  Us as y,
  qs as z
};
