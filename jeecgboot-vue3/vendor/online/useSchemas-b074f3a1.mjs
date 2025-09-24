import { computed as b, h as m } from "vue";
import { Input as f, Button as h } from "ant-design-vue";
import { FolderOpenOutlined as P } from "@ant-design/icons-vue";
import { bindMapFormSchema as c } from "/@/utils/common/compUtils";
import { usePermission as v } from "/@/hooks/web/usePermission";
import { rules as S } from "/@/utils/helper/validator";
v();
function F(d, n, t) {
  const e = c(
    {
      // 一列
      one: {
        colProps: { xs: 24, sm: 24 },
        itemProps: {
          labelCol: { xs: 24, sm: 2 },
          wrapperCol: { xs: 24, sm: 22 }
        }
      },
      // 两列
      tow: {
        colProps: { xs: 24, sm: 12 },
        itemProps: {
          labelCol: { xs: 24, sm: 4 },
          wrapperCol: { xs: 24, sm: 20 }
        }
      },
      // 三列
      three: {
        colProps: { xs: 24, sm: 8 },
        itemProps: {
          labelCol: { xs: 24, sm: 6 },
          wrapperCol: { xs: 24, sm: 18 }
        }
      }
    },
    "three"
  ), l = [
    { label: "", field: "id", component: "Input", show: !1 },
    { label: "", field: "tableVersion", component: "Input", show: !1 },
    e({
      label: "表名",
      field: "tableName",
      component: "Input",
      required: !0,
      // 如果版本号为1 表示未曾修改 未曾同步 可以修改表名
      dynamicDisabled: ({ model: o }) => o.tableVersion && o.tableVersion != 1,
      dynamicRules: ({ model: o, schema: a }) => [
        {
          validator: (s, p) => new Promise((i, u) => {
            /[\u4E00-\u9FA5]/g.test(p) && u("不允许输入中文"), i();
          })
        },
        // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
        {
          validator: (s, p) => new Promise((i, u) => {
            p.length > 50 && u("表名最长50个字符"), i();
          })
        },
        // update-end--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
        ...S.duplicateCheckRule("onl_cgform_head", "table_name", o, a, !0)
      ]
    }),
    e({
      label: "表描述",
      field: "tableTxt",
      component: "Input",
      required: !0,
      // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
      dynamicRules: ({ model: o, schema: a }) => [
        {
          validator: (s, p) => new Promise((i, u) => {
            p.length > 200 && u("表描述最长200个字"), i();
          })
        }
      ]
      // update-end--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
    }),
    e({
      label: "表类型",
      field: "tableType",
      component: "Select",
      defaultValue: 1,
      componentProps: {
        options: [
          { label: "单表", value: 1 },
          { label: "主表", value: 2 },
          { label: "附表", value: 3 }
        ],
        onChange: t.onTableTypeChange,
        allowClear: !1
      }
    }),
    // 此处为占位符，用于将 relationType 顶到最右边
    {
      label: "",
      field: "relationType",
      component: "InputNumber",
      render: () => "",
      colProps: { xs: 0, sm: 17 },
      ifShow: r
    },
    e({
      label: "",
      field: "relationType",
      component: "RadioGroup",
      defaultValue: 0,
      componentProps: {
        options: [
          { label: "一对多", value: 0 },
          { label: "一对一", value: 1 }
        ],
        allowClear: !1
      },
      colProps: { xs: 24, sm: 4 },
      itemProps: {
        colon: !1,
        labelCol: { xs: 0, sm: 0 },
        wrapperCol: { xs: 24, sm: 24 }
      },
      ifShow: r
    }),
    e({
      label: "序号",
      field: "tabOrderNum",
      component: "InputNumber",
      componentProps: {
        style: {
          width: "100%"
        }
      },
      colProps: { xs: 24, sm: 3 },
      itemProps: {
        labelCol: { xs: 24, sm: 7 },
        wrapperCol: { xs: 24, sm: 24 - 7 }
      },
      ifShow: r
    }),
    e({
      label: "表单分类",
      field: "formCategory",
      component: "JDictSelectTag",
      defaultValue: "temp",
      componentProps: {
        dictCode: "ol_form_biz_type",
        allowClear: !1
      }
    }),
    e({
      label: "主键策略",
      field: "idType",
      component: "Select",
      defaultValue: "UUID",
      componentProps: {
        options: [
          { label: "ID_WORKER(分布式自增)", value: "UUID" }
          // { label: 'NATIVE(自增长方式)', value: 'NATIVE' },
          // { label: 'SEQUENCE(序列方式)', value: 'SEQUENCE' },
        ],
        allowClear: !1
      }
    }),
    e({
      label: "序号名称",
      field: "idSequence",
      component: "Input",
      componentProps: {},
      ifShow: r
    }),
    e({
      label: "显示复选框",
      field: "isCheckbox",
      component: "Select",
      defaultValue: "Y",
      componentProps: {
        options: [
          { label: "是", value: "Y" },
          { label: "否", value: "N" }
        ],
        allowClear: !1
      }
    }),
    e({
      label: "主题模板",
      field: "themeTemplate",
      component: "Select",
      defaultValue: "normal",
      componentProps: {
        options: [
          { label: "默认主题", value: "normal" },
          { label: "ERP主题(一对多)", value: "erp" },
          { label: "内嵌子表主题(一对多)", value: "innerTable" },
          { label: "TAB主题(一对多)", value: "tab" }
        ],
        allowClear: !1
      },
      // 单表时禁用该字段
      dynamicDisabled: ({ model: o }) => o.tableType === 1,
      // update-begin--author:liaozhiyang---date:20231123---for：【QQYUN-7073】提示ERP、内嵌子表不支持联合查询
      dynamicRules() {
        return [
          {
            validator({}, o) {
              const a = n.value;
              if (o === "erp") {
                if (a.joinQuery)
                  return Promise.reject("ERP不支持联合查询功能");
              } else if (o === "innerTable" && a.joinQuery)
                return Promise.reject("内嵌子表不支持联合查询功能");
              return Promise.resolve();
            }
          }
        ];
      }
      // update-end--author:liaozhiyang---date:20231123---for：【QQYUN-7073】提示ERP、内嵌子表不支持联合查询
    }),
    e({
      label: "表单风格",
      field: "formTemplate",
      component: "Select",
      defaultValue: "1",
      componentProps: {
        options: [
          { label: "一列", value: "1" },
          { label: "两列", value: "2" },
          { label: "三列", value: "3" },
          { label: "四列", value: "4" }
        ],
        placeholder: "请选择PC表单风格",
        allowClear: !1
      }
    }),
    e({
      label: "移动表单风格",
      field: "formTemplateMobile",
      component: "Select",
      defaultValue: "1",
      componentProps: {
        options: [
          { label: "AntDesign模板", value: "1" },
          { label: "Bootstrap模板", value: "2" }
        ],
        placeholder: "请选择移动表单风格"
      },
      // 暂时先隐藏
      ifShow: !1
    }),
    e({
      label: "滚动条",
      field: "scroll",
      component: "Select",
      defaultValue: 1,
      componentProps: {
        options: [
          { label: "有", value: 1 },
          { label: "无", value: 0 }
        ],
        allowClear: !1
      }
    }),
    e({
      label: "是否分页",
      field: "isPage",
      component: "Select",
      defaultValue: "Y",
      componentProps: {
        options: [
          { label: "是", value: "Y" },
          { label: "否", value: "N" }
        ],
        allowClear: !1
      }
    }),
    e({
      label: "是否树",
      field: "isTree",
      component: "Select",
      defaultValue: "N",
      componentProps: {
        options: [
          { label: "是", value: "Y" },
          { label: "否", value: "N" }
        ],
        onChange: t.onIsTreeChange,
        allowClear: !1
      },
      dynamicRules({ model: o }) {
        return [
          {
            validator({}, a) {
              return a === "Y" && (o.tableType == 2 || o.tableType == 3) ? Promise.reject("主表和附表不支持树类型！") : Promise.resolve();
            }
          }
        ];
      },
      // update-begin--author:liaozhiyang---date:20240604---for：【TV360X-125】选择主表和附表时隐藏树表配置
      show({ model: o, values: a }) {
        return o.tableType == 2 || o.tableType == 3 ? (o.isTree = "N", t.onIsTreeChange("N"), !1) : !0;
      }
      // update-end--author:liaozhiyang---date:20240604---for：【TV360X-125】选择主表和附表时隐藏树表配置
    }),
    e({
      // 空格不要删，否则布局会乱
      label: " ",
      // 扩展配置
      field: "extConfigJson",
      component: "Input",
      slot: "extConfigButton",
      itemProps: { colon: !1 }
    }),
    e({
      label: "树表单父ID",
      field: "treeParentIdField",
      component: "Input",
      ifShow: r
    }),
    e({
      label: "是否有子节点字段",
      field: "treeIdField",
      component: "Input",
      show: !1
    }),
    e({
      label: "树开表单列",
      field: "treeFieldname",
      required: !0,
      component: "Input",
      ifShow: r
    }),
    e(
      {
        label: "附表",
        field: "subTableStr",
        component: "Input",
        componentProps: {
          disabled: !0,
          placeholder: " ",
          allowClear: !1
        },
        ifShow: t.ifShowOfSubTableStr
      },
      "one"
    )
  ];
  function r({ field: o, model: a }) {
    switch (o) {
      case "relationType":
      case "tabOrderNum":
        return a.tableType === 3;
      case "treeParentIdField":
      case "treeIdField":
      case "treeFieldname":
        return a.isTree === "Y";
      case "idSequence":
        return a.idType === "SEQUENCE";
    }
    return !0;
  }
  return { formSchemas: l };
}
function R(d, n) {
  const t = c(
    {
      left: {
        colProps: { xs: 24, sm: 7 },
        itemProps: {
          labelCol: { xs: 24, sm: 11 },
          wrapperCol: { xs: 24, sm: 13 }
        },
        style: { width: "100%" }
      },
      right: {
        colProps: { xs: 24, sm: 17 },
        itemProps: {
          labelCol: { xs: 24, sm: 3 },
          wrapperCol: { xs: 24, sm: 20 }
        },
        style: { width: "100%" }
      }
    },
    "left"
  );
  return { formSchemas: [
    // 弹窗
    t(
      {
        label: "弹窗默认全屏",
        field: "modelFullscreen",
        component: "RadioButtonGroup",
        componentProps: {
          options: [
            { label: "开启", value: 1 },
            { label: "关闭", value: 0 }
          ],
          buttonStyle: "solid"
        }
      },
      "left"
    ),
    t(
      {
        label: "弹窗宽度",
        field: "modalMinWidth",
        component: "InputNumber",
        componentProps: {
          style: "width: 80%",
          placeholder: "弹窗最小宽度（单位：px）"
        },
        // update-begin--author:liaozhiyang---date:20240520---for：【TV360X-77】弹窗全屏后，输入宽度框禁用
        dynamicDisabled: ({ model: l }) => l.modelFullscreen
        // update-end--author:liaozhiyang---date:20240520---for：【TV360X-77】弹窗全屏后，输入宽度框禁用
      },
      "right"
    ),
    //----------------------------表单评论 begin-----------------------------------------
    t(
      {
        label: "开启表单评论",
        field: "commentStatus",
        component: "RadioButtonGroup",
        componentProps: {
          options: [
            { label: "开启", value: 1 },
            { label: "关闭", value: 0 }
          ],
          buttonStyle: "solid"
        }
      },
      "left"
    ),
    // 此处为占位符
    t(
      {
        label: "",
        field: "commentStatus",
        component: "InputNumber",
        render: () => ""
      },
      "right"
    ),
    // 启用联合查询
    t(
      {
        label: "启用联合查询",
        field: "joinQuery",
        component: "RadioButtonGroup",
        componentProps: {
          options: [
            { label: "开启", value: 1 },
            { label: "关闭", value: 0 }
          ],
          buttonStyle: "solid",
          onChange: n.onJoinQueryChange
        }
      },
      "left"
    ),
    // 此处为占位符
    t(
      {
        label: "",
        field: "joinQuery",
        component: "InputNumber",
        render: () => ""
      },
      "right"
    ),
    // 积木报表打印
    t(
      {
        label: "集成积木报表",
        field: "reportPrintShow",
        component: "RadioButtonGroup",
        componentProps: {
          options: [
            { label: "开启", value: 1 },
            { label: "关闭", value: 0 }
          ],
          buttonStyle: "solid",
          onChange: n.onReportPrintShowChange
        }
      },
      "left"
    ),
    t(
      {
        label: "报表地址",
        field: "reportPrintUrl",
        component: "Input",
        componentProps: {
          style: "width: 80%"
        },
        dynamicDisabled: ({ model: l }) => !l.reportPrintShow,
        dynamicRules: ({ model: l }) => [
          { required: !!l.reportPrintShow, message: "请输入报表地址！" },
          {
            validator({}, r) {
              return /\/jmreport\/view\/{积木报表ID}/.test(r) ? Promise.reject("请将{积木报表ID}替换为真实的积木报表ID！") : Promise.resolve();
            }
          }
        ]
      },
      "right"
    ),
    // update-begin--author:liaozhiyang---date:20231213---for：【QQYUN-7421】vue3先注释集成设计表单功能
    // mapFormSchema(
    //   {
    //     label: '集成设计表单',
    //     field: 'isDesForm',
    //     component: 'RadioButtonGroup',
    //     componentProps: {
    //       options: [
    //         { label: '开启', value: 'Y' },
    //         { label: '关闭', value: 'N' },
    //       ],
    //       buttonStyle: 'solid',
    //       onChange: handlers.onIsDesformChange,
    //     },
    //   },
    //   'left'
    // ),
    // mapFormSchema(
    //   {
    //     label: '表单编码',
    //     field: 'desFormCode',
    //     component: 'Input',
    //     componentProps: {
    //       style: 'width: 80%',
    //     },
    //     dynamicDisabled: ({ model }) => model.isDesForm !== 'Y',
    //     dynamicRules: ({ model }) => {
    //       return [{ required: model.isDesForm === 'Y', message: '请输入表单编码！' }];
    //     },
    //   },
    //   'right'
    // ),
    // update-end--author:liaozhiyang---date:20231213---for：【QQYUN-7421】vue3先注释集成设计表单功能
    // 列表操作列
    t(
      {
        label: "固定操作列",
        field: "tableFixedAction",
        component: "RadioButtonGroup",
        componentProps: {
          options: [
            { label: "开启", value: 1 },
            { label: "关闭", value: 0 }
          ],
          buttonStyle: "solid"
        },
        defaultValue: 1
      },
      "left"
    ),
    t(
      {
        label: "固定方式",
        field: "tableFixedActionType",
        component: "Select",
        componentProps: {
          options: [
            { label: "固定到右侧", value: "right" },
            { label: "固定到左侧", value: "left" }
          ],
          style: "width: 80%"
        },
        defaultValue: "right",
        dynamicDisabled: ({ model: l }) => !l.tableFixedAction,
        dynamicRules: ({ model: l }) => [{ required: !!l.tableFixedAction, message: "请选择固定方式！" }]
      },
      "right"
    ),
    //--------------------------表单评论 end-----------------------------------------
    // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
    t(
      {
        label: "表单Label长度",
        field: "formLabelLengthShow",
        component: "RadioButtonGroup",
        componentProps: {
          options: [
            { label: "开启", value: 1 },
            { label: "关闭", value: 0 }
          ],
          buttonStyle: "solid",
          onChange: n.onFormLabelLengthShow
        }
      },
      "left"
    ),
    t(
      {
        label: "Label长度",
        field: "formLabelLength",
        component: "InputNumber",
        componentProps: {
          style: "width: 80%",
          placeholder: "自定义表单Label长度"
        },
        dynamicDisabled: ({ model: l }) => !l.formLabelLengthShow,
        dynamicRules: ({ model: l }) => [{ required: !!l.formLabelLengthShow, message: "请填写表单label长度" }]
      },
      "right"
    )
    // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
    // mapFormSchema(
    //   {
    //     label: '启用外部链接',
    //     field: 'enableExternalLink',
    //     component: 'RadioButtonGroup',
    //     componentProps: {
    //       options: [
    //         {label: '开启', value: 1},
    //         {label: '关闭', value: 0},
    //       ],
    //       buttonStyle: 'solid',
    //       defaultValue: 0,
    //       // onChange: handlers.onFormLabelLengthShow,
    //     },
    //   },
    //   'left'
    // ),
    // mapFormSchema(
    //   {
    //     label: '允许的操作',
    //     field: 'externalLinkActions',
    //     component: 'JCheckbox',
    //     componentProps: {
    //       options: [
    //         {label: '新增', value: 'add'},
    //         {label: '编辑', value: 'edit'},
    //         {label: '详情', value: 'detail'},
    //       ],
    //     },
    //     dynamicDisabled: ({model}) => !model.enableExternalLink,
    //   },
    //   'right'
    // ),
  ] };
}
function N(d, n, t) {
  const e = c(
    {
      // 一列
      one: {
        colProps: { xs: 24, sm: 24 },
        itemProps: {
          labelCol: { xs: 24, sm: 5 },
          wrapperCol: { xs: 24, sm: 16 }
        }
      },
      // 两列中的一列
      towOne: {
        colProps: { xs: 24, sm: 24 },
        itemProps: {
          labelCol: { xs: 24, sm: 3 },
          wrapperCol: { xs: 24, sm: 20 }
        }
      },
      // 两列
      tow: {
        colProps: { xs: 24, sm: 12 },
        itemProps: {
          labelCol: { xs: 24, sm: 6 },
          wrapperCol: { xs: 24, sm: 16 }
        }
      }
    },
    "one"
  ), l = b(() => t.value ? "one" : "tow");
  return { formSchemas: b(() => [
    e(
      {
        label: "代码生成目录",
        field: "projectPath",
        render: ({ model: o, field: a }) => m(
          f.Search,
          {
            value: o[a],
            onChange: (s) => {
              o[a] = s.target.value, n.onProjectPathChange(s);
            },
            onSearch: n.onProjectPathSearch
          },
          {
            enterButton: () => m(
              h,
              {
                preIcon: "ant-design:folder-open"
              },
              {
                default: () => "浏览",
                icon: () => m(P)
              }
            )
          }
        ),
        component: "InputSearch",
        required: !0
        // 如果版本号为1 表示未曾修改 未曾同步 可以修改表名
      },
      t.value ? "one" : "towOne"
    ),
    e(
      {
        label: "页面风格",
        field: "jspMode",
        component: "Select",
        componentProps: {
          options: n.jspModeOptions.value,
          // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-895】页面风格去掉x
          allowClear: !1
          // update-end--author:liaozhiyang---date:20240603---for：【TV360X-895】页面风格去掉x
        }
      },
      l.value
    ),
    e(
      {
        label: "功能说明",
        field: "ftlDescription",
        component: "Input"
      },
      l.value
    ),
    { label: "数据模型", field: "jformType", component: "Input", show: !1 },
    e(
      {
        label: "表名",
        field: "tableName_tmp",
        required: !0,
        dynamicDisabled: !0,
        component: "Input"
      },
      l.value
    ),
    e(
      {
        label: "实体类名",
        field: "entityName",
        required: !0,
        component: "Input",
        componentProps: {
          placeholder: "请输入实体类名(首字母大写)"
        }
      },
      l.value
    ),
    e(
      {
        label: "包名(小写)",
        field: "entityPackage",
        component: "Input",
        rules: [{ required: !0, pattern: /^[a-zA-Z0-9._]*$/, message: "包名必填，且只允许字母、数字、下划线、小数点组合" }]
      },
      l.value
    ),
    e(
      {
        label: "代码分层样式",
        field: "packageStyle",
        component: "Select",
        componentProps: {
          disabled: !0,
          options: [
            { label: "业务分层", value: "service" },
            { label: "代码分层", value: "project" }
          ]
        }
      },
      l.value
    ),
    e(
      {
        label: "页面代码",
        field: "vueStyle",
        required: !0,
        component: "Input",
        defaultValue: "vue3",
        slot: "pageCode"
        // componentProps: {
        //   options: [
        //     { label: 'Vue3(BasicForm)', value: 'vue3' },
        //     { label: 'Vue3原生(a-form)', value: 'vue3Native' },
        //     { label: 'Vue2', value: 'vue' },
        //   ],
        // },
        // update-begin--author:liaozhiyang---date:20240612---for：【TV360X-1057】代码生成页面代码鼠标移入给说明
        // dynamicPropskey: 'options',
        // dynamicPropsVal: ({ model }) => {
        //   if (model.jspMode && (model.jspMode == 'innerTable' || model.jspMode == 'tab')) {
        //     return [
        //       { value: 'vue3', label: '封装表单(BasicForm)' },
        //     ];
        //   } else {
        //     return [
        //       { value: 'vue3', label: '封装表单(BasicForm)' },
        //       { value: 'vue3Native', label: '原生表单(a-form)' },
        //     ];
        //   }
        // },
        // update-end--author:liaozhiyang---date:20240612---for：【TV360X-1057】代码生成页面代码鼠标移入给说明
      },
      l.value
    ),
    { label: "需要生成的代码", field: "codeTypes", component: "Input", show: !1 }
  ]) };
}
export {
  R as a,
  N as b,
  F as u
};
