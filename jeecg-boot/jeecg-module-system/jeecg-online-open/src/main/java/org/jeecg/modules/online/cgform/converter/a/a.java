/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.cgform.converter.a;

import java.util.List;
import java.util.Map;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.converter.FieldCommentConverter;

public class a
implements FieldCommentConverter {
    protected ISysBaseAPI a = (ISysBaseAPI)SpringContextUtils.getBean(ISysBaseAPI.class);
    protected String b;
    protected String c;
    protected String d;
    protected String e;

    public a() {
    }

    public a(String string, String string2, String string3) {
        this();
        this.c = string;
        this.d = string2;
        this.e = string3;
    }

    public String getField() {
        return this.b;
    }

    public void setField(String field) {
        this.b = field;
    }

    public String getTable() {
        return this.c;
    }

    public void setTable(String table) {
        this.c = table;
    }

    public String getCode() {
        return this.d;
    }

    public void setCode(String code) {
        this.d = code;
    }

    public String getText() {
        return this.e;
    }

    public void setText(String text) {
        this.e = text;
    }

    @Override
    public String converterToVal(String txt) {
        if (oConvertUtils.isNotEmpty((Object)txt)) {
            String string = this.e + "= '" + txt + "'";
            String string2 = null;
            int n = this.c.indexOf("where");
            if (n > 0) {
                string2 = this.c.substring(0, n).trim();
                string = string + " and " + this.c.substring(n + 5);
            } else {
                string2 = this.c;
            }
            List list = this.a.queryFilterTableDictInfo(string2, this.e, this.d, string);
            if (list != null && list.size() > 0) {
                return ((DictModel)list.get(0)).getValue();
            }
        }
        return null;
    }

    @Override
    public String converterToTxt(String val) {
        if (oConvertUtils.isNotEmpty((Object)val)) {
            String string = this.d + "= '" + val + "'";
            String string2 = null;
            int n = this.c.indexOf("where");
            if (n > 0) {
                string2 = this.c.substring(0, n).trim();
                string = string + " and " + this.c.substring(n + 5);
            } else {
                string2 = this.c;
            }
            List list = this.a.queryFilterTableDictInfo(string2, this.e, this.d, string);
            if (list != null && list.size() > 0) {
                return ((DictModel)list.get(0)).getText();
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getConfig() {
        return null;
    }
}

