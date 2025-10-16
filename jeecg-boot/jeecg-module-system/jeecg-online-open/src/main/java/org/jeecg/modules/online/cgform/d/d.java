/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.cgform.d;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJs;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class d {
    private static final Logger e = LoggerFactory.getLogger(d.class);
    private static final String f = "setup,beforeSubmit,beforeAdd,beforeEdit,afterAdd,afterEdit,beforeDelete,afterDelete,mounted,created,show,loaded";
    private static final String g = "\\}\\s*\r*\n*\\s*";
    private static final String h = ",";
    public static final Pattern a = Pattern.compile("^import\\s+(.*)\\s+from\\s+(['\"].*['\"])[;]?$");
    public static final String b = "import";
    public static final String c = "customImport";
    public static final String d = "_hook";

    public static String a(String string, String string2) {
        String string3 = "(" + string2 + "\\s*\\(row\\)\\s*\\{)";
        String string4 = string2 + ":function(that,row){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction,useMessage = this._useMessage;";
        String string5 = org.jeecg.modules.online.cgform.d.d.b(string, g + string3, "}," + string4);
        string = string5 == null ? org.jeecg.modules.online.cgform.d.d.c(string, string3, string4) : string5;
        string = org.jeecg.modules.online.cgform.d.d.a(string, string2, null);
        return string;
    }

    public static String a(String string, String string2, String string3) {
        String string4 = "(" + oConvertUtils.getString((String)string3) + string2 + "\\s*\\(\\)\\s*\\{)";
        String string5 = string2 + ":function(that){const getAction=this._getAction,postAction=this._postAction,deleteAction=this._deleteAction,useMessage = this._useMessage;";
        String string6 = org.jeecg.modules.online.cgform.d.d.b(string, g + string4, "}," + string5);
        string = string6 == null ? org.jeecg.modules.online.cgform.d.d.c(string, string4, string5) : string6;
        return string;
    }

    public static String b(String string, String string2, String string3) {
        Pattern pattern = Pattern.compile(string2);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            string = string.replace(matcher.group(0), string3);
            return string;
        }
        return null;
    }

    public static String c(String string, String string2, String string3) {
        String string4 = org.jeecg.modules.online.cgform.d.d.b(string, string2, string3);
        if (string4 != null) {
            return string4;
        }
        return string;
    }

    public static String a(String string, List<OnlCgformButton> list) {
        return "class OnlineEnhanceJs{constructor(getAction,postAction,deleteAction){this._getAction=getAction;this._postAction=postAction;this._deleteAction=deleteAction;}" + string + "}";
    }

    public static String b(String string, String string2) {
        String string3 = "([ \\t]+" + string2 + "\\s*\\(\\)\\s*\\{)";
        String string4 = string2 + ":function(that,event){";
        String string5 = org.jeecg.modules.online.cgform.d.d.b(string, g + string3, "}," + string4);
        string = string5 == null ? org.jeecg.modules.online.cgform.d.d.c(string, string3, string4) : string5;
        return string;
    }

    public static String a(String string) {
        String string2 = "function OnlineEnhanceJs(getAction,postAction,deleteAction){return {_getAction:getAction,_postAction:postAction,_deleteAction:deleteAction," + string + "}}";
        return string2;
    }

    public static String b(String string, List<OnlCgformButton> list) {
        string = org.jeecg.modules.online.cgform.d.d.c(string, list);
        String string2 = "function OnlineEnhanceJs(getAction,postAction,deleteAction){return {_getAction:getAction,_postAction:postAction,_deleteAction:deleteAction," + string + "}}";
        return string2;
    }

    public static String c(String string, List<OnlCgformButton> list) {
        string = org.jeecg.modules.online.cgform.d.d.b(string);
        if (list != null) {
            for (OnlCgformButton onlCgformButton : list) {
                String string2 = onlCgformButton.getButtonCode();
                if ("link".equals(onlCgformButton.getButtonStyle())) {
                    string = org.jeecg.modules.online.cgform.d.d.a(string, string2);
                    string = org.jeecg.modules.online.cgform.d.d.a(string, string2 + d);
                    continue;
                }
                if (!"button".equals(onlCgformButton.getButtonStyle()) && !"form".equals(onlCgformButton.getButtonStyle())) continue;
                string = org.jeecg.modules.online.cgform.d.d.a(string, string2, null);
                string = org.jeecg.modules.online.cgform.d.d.a(string, string2 + d, null);
            }
        }
        for (String string3 : f.split(h)) {
            string = "setup,beforeAdd,afterAdd,mounted,created,show,loaded".indexOf(string3) >= 0 ? org.jeecg.modules.online.cgform.d.d.a(string, string3, null) : org.jeecg.modules.online.cgform.d.d.a(string, string3);
        }
        return string;
    }

    public static void a(OnlCgformEnhanceJs onlCgformEnhanceJs, String string, List<OnlCgformField> list) {
        if (onlCgformEnhanceJs == null || oConvertUtils.isEmpty((Object)onlCgformEnhanceJs.getCgJs())) {
            return;
        }
        String string2 = " " + onlCgformEnhanceJs.getCgJs();
        e.debug("one enhanceJs begin==> " + string2);
        Pattern pattern = Pattern.compile("(\\s{1}onlChange\\s*\\(\\)\\s*\\{)");
        Matcher matcher = pattern.matcher(string2);
        if (matcher.find()) {
            e.debug("---JS \u589e\u5f3a\u8f6c\u6362-main--enhanceJsFunctionName----onlChange");
            string2 = org.jeecg.modules.online.cgform.d.d.a(string2, "onlChange", "\\s{1}");
            for (OnlCgformField onlCgformField : list) {
                string2 = org.jeecg.modules.online.cgform.d.d.b(string2, onlCgformField.getDbFieldName());
            }
        }
        e.debug("one enhanceJs end==> " + string2);
        onlCgformEnhanceJs.setCgJs(string2);
    }

    public static void b(OnlCgformEnhanceJs onlCgformEnhanceJs, String string, List<OnlCgformField> list) {
        if (onlCgformEnhanceJs == null || oConvertUtils.isEmpty((Object)onlCgformEnhanceJs.getCgJs())) {
            return;
        }
        String string2 = onlCgformEnhanceJs.getCgJs();
        String string3 = string + "_" + "onlChange";
        Pattern pattern = Pattern.compile("(" + string3 + "\\s*\\(\\)\\s*\\{)");
        Matcher matcher = pattern.matcher(string2);
        if (matcher.find()) {
            string2 = org.jeecg.modules.online.cgform.d.d.a(string2, string3, null);
            for (OnlCgformField onlCgformField : list) {
                string2 = org.jeecg.modules.online.cgform.d.d.b(string2, onlCgformField.getDbFieldName());
            }
        }
        onlCgformEnhanceJs.setCgJs(string2);
    }

    private static String b(String string) {
        String string2 = "\n";
        CharSequence[] charSequenceArray = string.split(string2);
        for (int i2 = 0; i2 < charSequenceArray.length; ++i2) {
            Matcher matcher;
            String string3 = charSequenceArray[i2].trim();
            if (!string3.startsWith(b) || !(matcher = a.matcher(string3)).find()) continue;
            String string4 = String.format("const %s = %s(%s)", matcher.group(1), c, matcher.group(2));
            charSequenceArray[i2] = ((String)charSequenceArray[i2]).replace(string3, string4);
        }
        return String.join((CharSequence)string2, charSequenceArray);
    }
}

