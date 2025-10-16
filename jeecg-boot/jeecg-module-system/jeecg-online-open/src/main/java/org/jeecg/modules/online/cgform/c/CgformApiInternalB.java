/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  javax.servlet.http.HttpServletRequest
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.query.QueryGenerator
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.cache.annotation.CacheEvict
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package org.jeecg.modules.online.cgform.c;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.online.cgform.d.a;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;
import org.jeecg.modules.online.cgform.service.IOnlCgformButtonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="onlCgformButtonController")
@RequestMapping(value={"/online/cgform/button"})
public class CgformApiInternalB {
    private static final Logger a = LoggerFactory.getLogger(b.class);
    @Autowired
    private IOnlCgformButtonService onlCgformButtonService;

    @GetMapping(value={"/list/{code}"})
    public Result<IPage<OnlCgformButton>> a(OnlCgformButton onlCgformButton, @RequestParam(name="pageNo", defaultValue="1") Integer n, @RequestParam(name="pageSize", defaultValue="10") Integer n2, HttpServletRequest httpServletRequest, @PathVariable(value="code") String string) {
        Result result = new Result();
        onlCgformButton.setCgformHeadId(string);
        QueryWrapper queryWrapper = QueryGenerator.initQueryWrapper((Object)onlCgformButton, (Map)httpServletRequest.getParameterMap());
        queryWrapper.notIn((Object)"button_code", org.jeecg.modules.online.cgform.d.a.getButtonCodeSet());
        Page page = new Page((long)n.intValue(), (long)n2.intValue());
        IPage iPage = this.onlCgformButtonService.page((IPage)page, (Wrapper)queryWrapper);
        result.setSuccess(true);
        result.setResult((Object)iPage);
        return result;
    }

    @PostMapping(value={"/add"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<OnlCgformButton> a(@RequestBody OnlCgformButton onlCgformButton) {
        Result result = new Result();
        try {
            this.onlCgformButtonService.saveButton(onlCgformButton);
            if (org.jeecg.modules.online.cgform.d.a.getButtonCodeSet().contains(onlCgformButton.getButtonCode())) {
                result.success("\u4fee\u6539\u6210\u529f\uff01");
            } else {
                result.success("\u6dfb\u52a0\u6210\u529f\uff01");
            }
        }
        catch (JeecgBootException jeecgBootException) {
            result.error500(jeecgBootException.getMessage());
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            result.error500("\u64cd\u4f5c\u5931\u8d25");
        }
        return result;
    }

    @PostMapping(value={"/aitest"})
    public Result<OnlCgformButton> a(@RequestBody JSONArray jSONArray) {
        Result result = new Result();
        try {
            for (int i2 = 0; i2 < jSONArray.size(); ++i2) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                OnlCgformButton onlCgformButton = (OnlCgformButton)JSONObject.parseObject((String)jSONObject.toJSONString(), OnlCgformButton.class);
                this.onlCgformButtonService.saveButton(onlCgformButton);
            }
            result.success("\u6dfb\u52a0\u6210\u529f\uff01");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            result.error500("\u64cd\u4f5c\u5931\u8d25");
        }
        return result;
    }

    @PutMapping(value={"/edit"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<OnlCgformButton> b(@RequestBody OnlCgformButton onlCgformButton) {
        return this.onlCgformButtonService.editButton(onlCgformButton);
    }

    @DeleteMapping(value={"/delete"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<OnlCgformButton> a(@RequestParam(name="id", required=true) String string) {
        Result result = new Result();
        OnlCgformButton onlCgformButton = (OnlCgformButton)this.onlCgformButtonService.getById((Serializable)((Object)string));
        if (onlCgformButton == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            boolean bl = this.onlCgformButtonService.removeById((Serializable)((Object)string));
            if (bl) {
                result.success("\u5220\u9664\u6210\u529f!");
            }
        }
        return result;
    }

    @DeleteMapping(value={"/deleteBatch"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<OnlCgformButton> b(@RequestParam(name="ids", required=true) String string) {
        Result result = new Result();
        if (string == null || "".equals(string.trim())) {
            result.error500("\u53c2\u6570\u4e0d\u8bc6\u522b\uff01");
        } else {
            this.onlCgformButtonService.removeByIds(Arrays.asList(string.split(",")));
            result.success("\u5220\u9664\u6210\u529f!");
        }
        return result;
    }

    @GetMapping(value={"/queryById"})
    public Result<OnlCgformButton> c(@RequestParam(name="id", required=true) String string) {
        Result result = new Result();
        OnlCgformButton onlCgformButton = (OnlCgformButton)this.onlCgformButtonService.getById((Serializable)((Object)string));
        if (onlCgformButton == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            result.setResult((Object)onlCgformButton);
            result.setSuccess(true);
        }
        return result;
    }

    @GetMapping(value={"/builtInList/{formId}"})
    public Result<List<OnlCgformButton>> d(@PathVariable(value="formId") String string) {
        List<OnlCgformButton> list = this.onlCgformButtonService.queryBuiltInButtonList(string);
        return Result.OK(list);
    }
}

