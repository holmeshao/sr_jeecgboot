/*
 * Decompiled with CFR 0.152.
 */
package org.jeecg.modules.online.cgform.enums;

public enum CgformValidPatternEnum {
    ONLY("only", "only", ""),
    NUM6_16("n6-16", "^\\d{6,16}$|^(?=\\d+\\.\\d+)[\\d.]{7,17}$", "\u8bf7\u8f93\u51656-16\u4f4d\u7684\u6570\u5b57"),
    STRING6_16("*6-16", "^.{6,16}$", "\u8bf7\u8f93\u51656-16\u4f4d\u4efb\u610f\u5b57\u7b26"),
    LETTER6_18("s6-18", "^[a-z|A-Z]{6,18}$", "\u8bf7\u8f93\u51656-18\u4f4d\u5b57\u6bcd"),
    URL("url", "^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:\\/~+#]*[\\w\\-@?^=%&\\/~+#])?$", "\u8bf7\u8f93\u5165\u6b63\u89c4\u7684\u7f51\u5740"),
    MOBILE("m", "^1[3456789]\\d{9}$", "\u8bf7\u8f93\u5165\u6b63\u89c4\u7684\u624b\u673a\u53f7\u7801"),
    POSTAL("p", "^[0-9]{6}$", "\u8bf7\u8f93\u5165\u6b63\u89c4\u7684\u90ae\u653f\u7f16\u7801"),
    LETTER("s", "^[A-Z|a-z]+$", "\u8bf7\u8f93\u5165\u5b57\u6bcd"),
    NUMBER("n", "^-?\\d+(\\.?\\d+|\\d?)$", "\u8bf7\u8f93\u5165\u6570\u5b57"),
    INTEGER("z", "z", "\u8bf7\u8f93\u5165\u6574\u6570"),
    NOTNULL("*", "^.+$", "\u8be5\u5b57\u6bb5\u4e0d\u80fd\u4e3a\u7a7a"),
    EMAIL("e", "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", "\u8bf7\u8f93\u5165\u6b63\u786e\u683c\u5f0f\u7684\u90ae\u7bb1\u5730\u5740"),
    MONEY("money", "^(([1-9][0-9]*)|([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,5}))$", "\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u91d1\u989d");

    String type;
    String pattern;
    String msg;

    private CgformValidPatternEnum(String type, String pattern, String msg) {
        this.pattern = pattern;
        this.msg = msg;
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static CgformValidPatternEnum getPatternInfoByType(String type) {
        for (CgformValidPatternEnum cgformValidPatternEnum : CgformValidPatternEnum.values()) {
            if (!cgformValidPatternEnum.type.equals(type)) continue;
            return cgformValidPatternEnum;
        }
        return null;
    }
}

