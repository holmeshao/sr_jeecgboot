import { getDictItemsByCode as l } from "/@/utils/dict";
import { filterDictText as n } from "/@/utils/dict/JDictSelectUtil";
import { buildUUID as s } from "/@/utils/uuid";
const r = "validate-failed", h = [
  {
    title: "表类型",
    align: "center",
    sorter: !0,
    dataIndex: "tableType",
    width: 140,
    customRender({ text: d, record: e }) {
      let i = l("cgform_table_type"), t = n(i, d);
      return e.isTree === "Y" && (t += "(树)"), e.themeTemplate === "innerTable" ? t += "(内嵌)" : e.themeTemplate === "erp" ? t += "(ERP)" : e.themeTemplate === "tab" && (t += "(TAB)"), t;
    }
  },
  {
    title: "表名",
    sorter: !0,
    align: "center",
    dataIndex: "tableName",
    width: 240
  },
  {
    title: "表描述",
    align: "center",
    dataIndex: "tableTxt",
    width: 220
  },
  {
    title: "版本",
    align: "center",
    dataIndex: "tableVersion",
    width: 120
  },
  {
    title: "同步状态",
    align: "center",
    sorter: !0,
    dataIndex: "isDbSynch",
    slots: { customRender: "dbSync" },
    width: 120
  },
  {
    title: "创建时间",
    align: "center",
    sorter: !0,
    dataIndex: "createTime",
    width: 240
  }
], u = [
  {
    label: "表名",
    field: "tableName",
    component: "JInput"
  },
  {
    label: "表类型",
    field: "tableType_MultiString",
    component: "JDictSelectTag",
    componentProps: {
      dictCode: "cgform_table_type",
      mode: "multiple"
    }
  },
  {
    label: "表描述",
    field: "tableTxt",
    component: "JInput"
  }
], m = {
  // 对接报表打印
  reportPrintShow: 0,
  reportPrintUrl: "",
  joinQuery: 0,
  modelFullscreen: 0,
  modalMinWidth: "",
  commentStatus: 0,
  tableFixedAction: 1,
  tableFixedActionType: "right",
  // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
  formLabelLengthShow: 0,
  formLabelLength: null,
  // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
  // 是否启用外部链接
  enableExternalLink: 0,
  externalLinkActions: "add,edit,detail"
};
function y() {
  let d = [
    {
      dbFieldName: "id",
      dbFieldTxt: "主键",
      dbLength: 36,
      dbPointLength: 0,
      dbDefaultVal: "",
      dbType: "string",
      dbIsKey: "1",
      dbIsNull: "0",
      // table2
      isShowForm: "0",
      isShowList: "0",
      isReadOnly: "1",
      fieldShowType: "text",
      fieldLength: "200",
      queryMode: "single",
      dbIsSync: "1"
    },
    {
      dbFieldName: "create_by",
      dbFieldTxt: "创建人",
      dbLength: 50,
      dbPointLength: 0,
      dbDefaultVal: "",
      dbType: "string",
      dbIsKey: "0",
      dbIsNull: "1",
      // table2
      isShowForm: "0",
      isShowList: "0",
      fieldShowType: "text",
      fieldLength: "200",
      queryMode: "single",
      dbIsSync: "1"
    },
    {
      dbFieldName: "create_time",
      dbFieldTxt: "创建日期",
      dbLength: 0,
      dbPointLength: 0,
      dbDefaultVal: "",
      dbType: "Datetime",
      dbIsKey: "0",
      dbIsNull: "1",
      // table2
      isShowForm: "0",
      isShowList: "0",
      fieldShowType: "datetime",
      fieldLength: "200",
      queryMode: "single",
      dbIsSync: "1"
    },
    {
      dbFieldName: "update_by",
      dbFieldTxt: "更新人",
      dbLength: 50,
      dbPointLength: 0,
      dbDefaultVal: "",
      dbType: "string",
      dbIsKey: "0",
      dbIsNull: "1",
      // table2
      isShowForm: "0",
      isShowList: "0",
      fieldShowType: "text",
      fieldLength: "200",
      queryMode: "single",
      dbIsSync: "1"
    },
    {
      dbFieldName: "update_time",
      dbFieldTxt: "更新日期",
      dbLength: 0,
      dbPointLength: 0,
      dbDefaultVal: "",
      dbType: "Datetime",
      dbIsKey: "0",
      dbIsNull: "1",
      // table2
      isShowForm: "0",
      isShowList: "0",
      fieldShowType: "datetime",
      fieldLength: "200",
      queryMode: "single",
      dbIsSync: "1"
    },
    {
      dbFieldName: "sys_org_code",
      dbFieldTxt: "所属部门",
      dbLength: 64,
      dbPointLength: 0,
      dbDefaultVal: "",
      dbType: "string",
      dbIsKey: "0",
      dbIsNull: "1",
      // table2
      isShowForm: "0",
      isShowList: "0",
      fieldShowType: "text",
      fieldLength: "200",
      queryMode: "single",
      dbIsSync: "1"
    }
    // {
    //   dbFieldName: 'sys_org_code',
    //   dbFieldTxt: '所属部门',
    //   dbLength: 50,
    //   dbPointLength: 0,
    //   dbDefaultVal: '',
    //   dbType: 'string',
    //   dbIsKey: false,
    //   dbIsNull: true
    // },
    // {
    //   dbFieldName: 'sys_company_code',
    //   dbFieldTxt: '所属公司',
    //   dbLength: 50,
    //   dbPointLength: 0,
    //   dbDefaultVal: '',
    //   dbType: 'string',
    //   dbIsKey: false,
    //   dbIsNull: true
    // },
    // {
    //   dbFieldName: 'bpm_status',
    //   dbFieldTxt: '流程状态',
    //   dbLength: 32,
    //   dbPointLength: 0,
    //   dbDefaultVal: '',
    //   dbType: 'string',
    //   dbIsKey: false,
    //   dbIsNull: true
    // }
  ], e = [];
  return d.forEach((i) => {
    i.id = s(), e.push(i.id);
  }), { initialData: d, tempIds: e };
}
function c() {
  return [
    {
      dbFieldName: "pid",
      dbFieldTxt: "父级节点",
      dbLength: 32,
      dbPointLength: 0,
      dbDefaultVal: "",
      dbType: "string",
      dbIsKey: "0",
      dbIsNull: "1",
      // table2
      isShowForm: "1",
      isShowList: "0",
      fieldShowType: "text",
      fieldLength: "200",
      queryMode: "single",
      dbIsSync: "1"
    },
    {
      dbFieldName: "has_child",
      dbFieldTxt: "是否有子节点",
      dbLength: 3,
      dbPointLength: 0,
      dbDefaultVal: "",
      dbType: "string",
      dbIsKey: "0",
      dbIsNull: "1",
      // table2
      isShowForm: "0",
      isShowList: "0",
      fieldShowType: "list",
      fieldLength: "200",
      queryMode: "single",
      // table3
      dictField: "yn",
      dbIsSync: "1"
    }
  ];
}
const g = [
  { code: "add", title: "新增", status: 0 },
  { code: "edit", title: "编辑", status: 0 },
  { code: "delete", title: "删除", status: 0 },
  { code: "export", title: "导出", status: 0 },
  { code: "import", title: "导入", status: 0 },
  { code: "query", title: "查询", status: 0 }
];
export {
  m as E,
  r as V,
  c as a,
  h as c,
  g as o,
  u as s,
  y as u
};
