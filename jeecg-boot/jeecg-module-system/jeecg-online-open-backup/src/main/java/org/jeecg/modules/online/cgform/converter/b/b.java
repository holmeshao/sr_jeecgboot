/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  org.jeecg.common.util.DateUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.jetbrains.annotations.NotNull
 */
package org.jeecg.modules.online.cgform.converter.b;

import com.alibaba.fastjson.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.converter.FieldCommentConverter;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jetbrains.annotations.NotNull;

public class b
implements FieldCommentConverter {
    String a;
    private static final String b = "year";
    private static final SimpleDateFormat c = new SimpleDateFormat("yyyy");
    private static final String d = "month";
    private static final SimpleDateFormat e = new SimpleDateFormat("yyyy-MM");
    private static final String f = "week";
    private static final String g = "quarter";
    private static final String h = "default";
    private static final List<b> i = new ArrayList<b>();

    @NotNull
    private static Date b(int n, int n2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(n, 0, 1);
        int n3 = calendar.get(7);
        if (n3 > 3) {
            n3 = 9 - n3;
        }
        calendar.set(6, n3);
        calendar.add(6, (n2 - 1) * 7);
        return calendar.getTime();
    }

    public b(OnlCgformField onlCgformField) {
        JSONObject jSONObject;
        String string = onlCgformField.getFieldExtendJson();
        if (oConvertUtils.isNotEmpty((Object)string) && (jSONObject = JSONObject.parseObject((String)string)).containsKey((Object)"picker") && null != jSONObject.get((Object)"picker")) {
            this.a = jSONObject.getString("picker");
        }
    }

    @Override
    public String converterToVal(String txt) {
        if (null == this.a || this.a.isEmpty()) {
            return txt;
        }
        if (null == txt || txt.isEmpty()) {
            return txt;
        }
        Date date = this.a(txt);
        if (null == date) {
            return "";
        }
        return ((SimpleDateFormat)DateUtils.date_sdf.get()).format(date);
    }

    private Date a(String string) {
        for (b b2 : i) {
            if (!b2.b(string)) continue;
            try {
                return b2.a(string);
            }
            catch (ParseException parseException) {
                throw new RuntimeException(parseException);
            }
        }
        return null;
    }

    @Override
    public String converterToTxt(String val) {
        if (null == this.a || this.a.isEmpty()) {
            return val;
        }
        if (null == val || val.isEmpty()) {
            return val;
        }
        try {
            Date date = ((SimpleDateFormat)DateUtils.date_sdf.get()).parse(val);
            switch (this.a) {
                case "year": {
                    return c.format(date);
                }
                case "month": {
                    return e.format(date);
                }
                case "week": 
                case "quarter": {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setFirstDayOfWeek(1);
                    calendar.setTime(date);
                    int n = calendar.get(1);
                    if (this.a.equals(f)) {
                        calendar.set(n, 0, 1);
                        int n2 = calendar.get(7);
                        if (n2 > 3) {
                            n2 = 8 - n2;
                        }
                        calendar.setTime(date);
                        int n3 = calendar.get(6);
                        int n4 = (n3 - n2) / 7 + 1;
                        return String.format("%d-%d\u5468", n, n4);
                    }
                    int n5 = calendar.get(2) + 1;
                    int n6 = (int)Math.ceil((double)n5 / 3.0);
                    return String.format("%d-Q%d", n, n6);
                }
            }
            return val;
        }
        catch (ParseException parseException) {
            throw new RuntimeException(parseException);
        }
    }

    @Override
    public Map<String, String> getConfig() {
        return Collections.emptyMap();
    }

    static {
        i.add(new c("^\\d{4}$", "yyyy"));
        i.add(new c("^\\d{4}\u5e74$", "yyyy\u5e74"));
        i.add(new c("^\\d{4}\u5e74\\d{1,2}\u6708$", "yyyy\u5e74MM\u6708"));
        i.add(new c("^\\d{4}-\\d{1,2}$", "yyyy-MM"));
        i.add(new c("^\\d{4}\\/\\d{1,2}$", "yyyy/MM"));
        i.add(new a("^\\d{4}-\\d{1,2}\u5468$"){

            @Override
            public Date a(String string) {
                int n = Integer.parseInt(string.substring(0, string.indexOf("-")));
                int n2 = Integer.parseInt(string.substring(string.indexOf("-") + 1, string.indexOf("\u5468")));
                return org.jeecg.modules.online.cgform.converter.b.b.b(n, n2);
            }
        });
        i.add(new a("^\\d{4}\u5e74\\d{1,2}\u5468$"){

            @Override
            public Date a(String string) {
                int n = Integer.parseInt(string.substring(0, string.indexOf("\u5e74")));
                int n2 = Integer.parseInt(string.substring(string.indexOf("\u5e74") + 1, string.indexOf("\u5468")));
                return org.jeecg.modules.online.cgform.converter.b.b.b(n, n2);
            }
        });
        i.add(new a("^\\d{4}-Q\\d{1}$"){

            @Override
            public Date a(String string) {
                Calendar calendar = Calendar.getInstance();
                int n = Integer.parseInt(string.substring(0, string.indexOf("-")));
                calendar.set(1, n);
                int n2 = Integer.parseInt(string.substring(string.indexOf("-Q") + 2));
                int n3 = n2 * 3;
                calendar.set(2, n3 - 1);
                return calendar.getTime();
            }
        });
        i.add(new a("^\\d{4}\u5e74Q\\d{1}$"){

            @Override
            public Date a(String string) {
                Calendar calendar = Calendar.getInstance();
                int n = Integer.parseInt(string.substring(0, string.indexOf("\u5e74")));
                calendar.set(1, n);
                int n2 = Integer.parseInt(string.substring(string.indexOf("\u5e74Q") + 2));
                int n3 = n2 * 3;
                calendar.set(2, n3 - 1);
                return calendar.getTime();
            }
        });
        i.add(new a("^\\d{4}\u5e74\\d{1}\u5b63\u5ea6$"){

            @Override
            public Date a(String string) {
                Calendar calendar = Calendar.getInstance();
                int n = Integer.parseInt(string.substring(0, string.indexOf("\u5e74")));
                calendar.set(1, n);
                int n2 = Integer.parseInt(string.substring(string.indexOf("\u5e74") + 1, string.indexOf("\u5b63\u5ea6")));
                int n3 = n2 * 3;
                calendar.set(2, n3 - 1);
                return calendar.getTime();
            }
        });
        i.add(new c("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd"));
        i.add(new c("^\\d{4}\u5e74\\d{1,2}\u6708\\d{1,2}\u65e5$", "yyyy\u5e74MM\u6708dd\u65e5"));
    }

    static class c
    extends a {
        SimpleDateFormat a;

        public c(String string, String string2) {
            super(string);
            this.a = new SimpleDateFormat(string2);
        }

        @Override
        public Date a(String string) throws ParseException {
            return this.a.parse(string);
        }
    }

    static abstract class a
    implements b {
        private final String a;

        public a(String string) {
            this.a = string;
        }

        @Override
        public boolean b(String string) {
            if (null == string || string.isEmpty()) {
                return false;
            }
            return string.matches(this.a);
        }
    }

    static interface b {
        public boolean b(String var1);

        public Date a(String var1) throws ParseException;
    }
}

