/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  javax.servlet.http.HttpServletRequest
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.util.RestUtil
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.HttpMethod
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 */
package org.jeecg.modules.online.cgform.enhance.impl.http.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.RestUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJava;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface CgformEnhanceHttpInter {
    public static final Logger logger = LoggerFactory.getLogger(CgformEnhanceHttpInter.class);

    default public void execute(String tableName, JSONObject json, OnlCgformEnhanceJava enhance) {
    }

    default public void execute(String tableName, List<Map<String, Object>> dataList, OnlCgformEnhanceJava enhance) {
    }

    default public Object sendPost(JSONObject params, OnlCgformEnhanceJava enhance) {
        String string;
        HttpHeaders httpHeaders;
        ResponseEntity responseEntity;
        Object object;
        if (enhance == null) {
            return null;
        }
        String string2 = enhance.getCgJavaValue();
        if (oConvertUtils.isEmpty((Object)string2)) {
            return null;
        }
        if (!string2.startsWith("http") && !string2.startsWith("https")) {
            object = RestUtil.getBaseUrl();
            string2 = string2.startsWith("/") ? (String)object + string2 : (String)object + "/" + string2;
        }
        if ((responseEntity = RestUtil.request((String)string2, (HttpMethod)HttpMethod.POST, (HttpHeaders)(httpHeaders = this.getHeaders((HttpServletRequest)(object = SpringContextUtils.getHttpServletRequest()))), null, (Object)params, String.class)).getStatusCode() == HttpStatus.OK && oConvertUtils.isNotEmpty((Object)(string = (String)responseEntity.getBody()))) {
            try {
                JSONObject jSONObject = JSON.parseObject((String)string);
                if (jSONObject.getBoolean("success").booleanValue()) {
                    return jSONObject.get((Object)"result");
                }
                throw new JeecgBootException(jSONObject.getString("message"));
            }
            catch (JeecgBootException jeecgBootException) {
                throw jeecgBootException;
            }
            catch (Exception exception) {
                logger.warn("\u8bf7\u6c42Online\u8868\u5355Java\u589e\u5f3ahttp\u63a5\u53e3\u65f6\u8f6c\u6362\u6570\u636e\u51fa\u9519\uff1a" + exception.getMessage() + "\n body: " + string);
                throw new JeecgBootException("Online\u8868\u5355Java\u589e\u5f3ahttp\u63a5\u53e3JSON\u8f6c\u6362\u5931\u8d25\uff01");
            }
        }
        return null;
    }

    default public HttpHeaders getHeaders(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Enumeration enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            Enumeration enumeration2 = request.getHeaders(string);
            while (enumeration2.hasMoreElements()) {
                httpHeaders.set(string, (String)enumeration2.nextElement());
            }
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.remove((Object)"Accept-Encoding");
        httpHeaders.remove((Object)"accept-encoding");
        httpHeaders.remove((Object)"Accept");
        httpHeaders.add("Accept", "application/json");
        return httpHeaders;
    }
}

