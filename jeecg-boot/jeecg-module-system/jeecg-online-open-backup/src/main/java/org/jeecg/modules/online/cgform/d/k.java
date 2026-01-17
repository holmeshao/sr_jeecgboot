/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.jeecg.common.system.query.QueryGenerator
 *  org.jeecg.common.system.query.QueryRuleEnum
 *  org.jeecg.common.system.vo.SysPermissionDataRuleModel
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.cgform.d;

import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.d.i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class k {
    private static final Logger a = LoggerFactory.getLogger(k.class);

    public static void a(StringBuilder stringBuilder, QueryRuleEnum queryRuleEnum, String string, String string2, String string3) {
        if ("date".equals(string3) && "ORACLE".equalsIgnoreCase(c.getDatabseType())) {
            string2 = (string2 = string2.replace("'", "")).length() == 10 ? c.b(string2) : c.a(string2);
        }
        switch (queryRuleEnum) {
            case GT: {
                stringBuilder.append(">").append(string2);
                break;
            }
            case GE: {
                stringBuilder.append(">=").append(string2);
                break;
            }
            case LT: {
                stringBuilder.append("<").append(string2);
                break;
            }
            case LE: {
                stringBuilder.append("<=").append(string2);
                break;
            }
            case NE: {
                stringBuilder.append("!=").append(string2);
                break;
            }
            case IN: {
                stringBuilder.append(" IN (");
                String[] stringArray = string.split(",");
                for (int i2 = 0; i2 < stringArray.length; ++i2) {
                    String string4 = stringArray[i2];
                    if (!StringUtils.isNotBlank((String)string4)) continue;
                    String string5 = k.a(string3, string4);
                    stringBuilder.append(string5);
                    if (i2 >= stringArray.length - 1) continue;
                    stringBuilder.append(",");
                }
                stringBuilder.append(")");
                break;
            }
            case LIKE: {
                stringBuilder.append(" like ").append("N").append("'").append("%").append(string).append("%").append("'");
                break;
            }
            case LEFT_LIKE: {
                stringBuilder.append(" like ").append("N").append("'").append("%").append(string).append("'");
                break;
            }
            case RIGHT_LIKE: {
                stringBuilder.append(" like ").append("N").append("'").append(string).append("%").append("'");
                break;
            }
            default: {
                stringBuilder.append("=").append(string2);
            }
        }
    }

    public static void a(String string, SysPermissionDataRuleModel sysPermissionDataRuleModel, String string2, String string3, StringBuffer stringBuffer) {
        QueryRuleEnum queryRuleEnum = QueryRuleEnum.getByValue((String)sysPermissionDataRuleModel.getRuleConditions());
        boolean bl = !i.a(string3);
        String string4 = k.a(sysPermissionDataRuleModel.getRuleValue(), bl, queryRuleEnum);
        if (string4 == null || queryRuleEnum == null) {
            return;
        }
        if ("ORACLE".equalsIgnoreCase(string) && "Date".equals(string3)) {
            string4 = (string4 = string4.replace("'", "")).length() == 10 ? c.b(string4) : c.a(string4);
        }
        switch (queryRuleEnum) {
            case GT: {
                stringBuffer.append(" AND " + string2 + ">" + string4);
                break;
            }
            case GE: {
                stringBuffer.append(" AND " + string2 + ">=" + string4);
                break;
            }
            case LT: {
                stringBuffer.append(" AND " + string2 + "<" + string4);
                break;
            }
            case LE: {
                stringBuffer.append(" AND " + string2 + "<=" + string4);
                break;
            }
            case EQ: {
                stringBuffer.append(" AND " + string2 + "=" + string4);
                break;
            }
            case NE: {
                stringBuffer.append(" AND " + string2 + " <> " + string4);
                break;
            }
            case IN: {
                stringBuffer.append(" AND " + string2 + " IN " + string4);
                break;
            }
            case LIKE: {
                stringBuffer.append(" AND " + string2 + " LIKE '%" + QueryGenerator.trimSingleQuote((String)string4) + "%'");
                break;
            }
            case LEFT_LIKE: {
                stringBuffer.append(" AND " + string2 + " LIKE '%" + QueryGenerator.trimSingleQuote((String)string4) + "'");
                break;
            }
            case RIGHT_LIKE: {
                stringBuffer.append(" AND " + string2 + " LIKE '" + QueryGenerator.trimSingleQuote((String)string4) + "%'");
                break;
            }
            default: {
                a.info("--\u67e5\u8be2\u89c4\u5219\u672a\u5339\u914d\u5230---");
            }
        }
    }

    public static String a(String string, String string2) {
        if ("int".equals(string) || "number".equals(string)) {
            return string2;
        }
        if ("date".equals(string)) {
            return "'" + string2 + "'";
        }
        if ("SQLSERVER".equals(c.getDatabseType())) {
            return "N'" + string2 + "'";
        }
        return "'" + string2 + "'";
    }

    private static String a(String string, boolean bl, QueryRuleEnum queryRuleEnum) {
        if (queryRuleEnum == QueryRuleEnum.IN) {
            return k.a(string, bl);
        }
        if (bl) {
            return "'" + QueryGenerator.converRuleValue((String)string) + "'";
        }
        return QueryGenerator.converRuleValue((String)string);
    }

    private static String a(String string, boolean bl) {
        if (string == null || string.length() == 0) {
            return "()";
        }
        string = QueryGenerator.converRuleValue((String)string);
        String[] stringArray = string.split(",");
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : stringArray) {
            if (string2 == null || string2.length() == 0) continue;
            if (bl) {
                arrayList.add("'" + string2 + "'");
                continue;
            }
            arrayList.add(string2);
        }
        return "(" + StringUtils.join(arrayList, (String)",") + ")";
    }
}

