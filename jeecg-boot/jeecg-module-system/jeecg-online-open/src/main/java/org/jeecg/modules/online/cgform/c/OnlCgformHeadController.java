/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.aspect.annotation.PermissionData
 *  org.jeecg.common.constant.enums.CgformEnum
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.query.QueryGenerator
 *  org.jeecg.common.system.util.JwtUtil
 *  org.jeecg.common.util.TokenUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.jeecg.config.mybatis.MybatisPlusSaasConfig
 *  org.jeecgframework.codegenerate.database.DbReadTableUtil
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.cache.annotation.CacheEvict
 *  org.springframework.core.io.Resource
 *  org.springframework.core.io.ResourceLoader
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.constant.enums.CgformEnum;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJava;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceJs;
import org.jeecg.modules.online.cgform.entity.OnlCgformEnhanceSql;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformEnhanceService;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.config.exception.a;
import org.jeecgframework.codegenerate.database.DbReadTableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="onlCgformHeadController")
@RequestMapping(value={"/online/cgform/head"})
public class OnlCgformHeadController {
    private static final Logger a = LoggerFactory.getLogger(OnlCgformHeadController.class);
    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;
    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;
    @Autowired
    private IOnlCgformEnhanceService onlCgformEnhanceService;
    private static List<String> b = null;
    @Autowired
    ResourceLoader resourceLoader;
    private static String c;

    @GetMapping(value={"/list"})
    @PermissionData
    public Result<IPage<OnlCgformHead>> a(OnlCgformHead onlCgformHead, @RequestParam(name="pageNo", defaultValue="1") Integer n, @RequestParam(name="pageSize", defaultValue="10") Integer n2, HttpServletRequest httpServletRequest) {
        String string;
        Result result = new Result();
        QueryWrapper queryWrapper = QueryGenerator.initQueryWrapper((Object)onlCgformHead, (Map)httpServletRequest.getParameterMap());
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL.booleanValue() && oConvertUtils.isNotEmpty((Object)(string = TokenUtils.getTenantIdByRequest((HttpServletRequest)httpServletRequest)))) {
            // 兼容历史数据：除当前租户数据外，允许显示未设置租户的公共数据
            queryWrapper.and(queryWrapper2 -> {
                QueryWrapper cfr_ignored_0 = (QueryWrapper)((QueryWrapper)((QueryWrapper)queryWrapper2.eq((Object)"tenant_id", (Object)string)).or()).isNull((Object)"tenant_id");
            });
        }
        string = new Page((long)n.intValue(), (long)n2.intValue());
        IPage iPage = this.onlCgformHeadService.page((IPage)string, (Wrapper)queryWrapper);
        if (onlCgformHead.getCopyType() != null && onlCgformHead.getCopyType() == 0) {
            this.onlCgformHeadService.initCopyState(iPage.getRecords());
        }
        result.setSuccess(true);
        result.setResult((Object)iPage);
        return result;
    }

    @PostMapping(value={"/add"})
    @RequiresPermissions(value={"online:form:add"})
    public Result<OnlCgformHead> a(@RequestBody OnlCgformHead onlCgformHead) {
        Result result = new Result();
        try {
            this.onlCgformHeadService.save(onlCgformHead);
            result.success("\u6dfb\u52a0\u6210\u529f\uff01");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            result.error500("\u64cd\u4f5c\u5931\u8d25");
        }
        return result;
    }

    @PutMapping(value={"/edit"})
    @RequiresPermissions(value={"online:form:edit"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<OnlCgformHead> b(@RequestBody OnlCgformHead onlCgformHead) {
        Result result = new Result();
        OnlCgformHead onlCgformHead2 = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)onlCgformHead.getId()));
        if (onlCgformHead2 == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            boolean bl = this.onlCgformHeadService.updateById(onlCgformHead);
            if (bl) {
                result.success("\u4fee\u6539\u6210\u529f!");
            }
        }
        return result;
    }

    @RequiresPermissions(value={"online:form:delete"})
    @DeleteMapping(value={"/delete"})
    public Result<?> a(@RequestParam(name="id", required=true) String string) {
        try {
            this.onlCgformHeadService.deleteRecordAndTable(string);
        }
        catch (a a2) {
            return Result.error((String)("\u5220\u9664\u5931\u8d25" + a2.getMessage()));
        }
        catch (SQLException sQLException) {
            return Result.error((String)("\u5220\u9664\u5931\u8d25" + sQLException.getMessage()));
        }
        return Result.ok((String)"\u5220\u9664\u6210\u529f!");
    }

    @RequiresPermissions(value={"online:form:remove"})
    @DeleteMapping(value={"/removeRecord"})
    public Result<?> b(@RequestParam(name="id", required=true) String string) {
        try {
            this.onlCgformHeadService.deleteRecord(string);
        }
        catch (a a2) {
            return Result.error((String)("\u79fb\u9664\u5931\u8d25" + a2.getMessage()));
        }
        catch (SQLException sQLException) {
            return Result.error((String)("\u79fb\u9664\u5931\u8d25" + sQLException.getMessage()));
        }
        return Result.ok((String)"\u79fb\u9664\u6210\u529f!");
    }

    @RequiresPermissions(value={"online:form:deleteBatch"})
    @DeleteMapping(value={"/deleteBatch"})
    public Result<OnlCgformHead> a(@RequestParam(name="ids", required=true) String string, @RequestParam(name="flag") String string2) {
        Result result = new Result();
        if (string == null || "".equals(string.trim())) {
            result.error500("\u53c2\u6570\u4e0d\u8bc6\u522b\uff01");
        } else {
            this.onlCgformHeadService.deleteBatch(string, string2);
            if ("1".equals(string2)) {
                result.success("\u5220\u9664\u6210\u529f!");
            } else {
                result.success("\u79fb\u9664\u6210\u529f!");
            }
        }
        return result;
    }

    @GetMapping(value={"/queryById"})
    public Result<OnlCgformHead> c(@RequestParam(name="id", required=true) String string) {
        Result result = new Result();
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
        if (onlCgformHead == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            result.setResult((Object)onlCgformHead);
            result.setSuccess(true);
        }
        return result;
    }

    @GetMapping(value={"/queryByTableNames"})
    public Result<?> d(@RequestParam(name="tableNames", required=true) String string) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        String[] stringArray = string.split(",");
        lambdaQueryWrapper.in(OnlCgformHead::getTableName, Arrays.asList(stringArray));
        List list = this.onlCgformHeadService.list((Wrapper)lambdaQueryWrapper);
        if (list == null) {
            return Result.error((String)"\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        }
        return Result.ok((Object)list);
    }

    @PostMapping(value={"/enhanceJs/{code}"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> a(@PathVariable(value="code") String string, @RequestBody OnlCgformEnhanceJs onlCgformEnhanceJs) {
        try {
            onlCgformEnhanceJs.setCgformHeadId(string);
            this.onlCgformHeadService.saveEnhance(onlCgformEnhanceJs);
            return Result.ok((String)"\u4fdd\u5b58\u6210\u529f!");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25!");
        }
    }

    @GetMapping(value={"/enhanceJs/{code}"})
    public Result<?> a(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        try {
            String string2 = httpServletRequest.getParameter("type");
            OnlCgformEnhanceJs onlCgformEnhanceJs = this.onlCgformHeadService.queryEnhance(string, string2);
            if (onlCgformEnhanceJs == null) {
                return Result.error((String)"\u67e5\u8be2\u4e3a\u7a7a");
            }
            return Result.ok((Object)onlCgformEnhanceJs);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u67e5\u8be2\u5931\u8d25!");
        }
    }

    @PutMapping(value={"/enhanceJs/{code}"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> b(@PathVariable(value="code") String string, @RequestBody OnlCgformEnhanceJs onlCgformEnhanceJs) {
        try {
            onlCgformEnhanceJs.setCgformHeadId(string);
            this.onlCgformHeadService.editEnhance(onlCgformEnhanceJs);
            return Result.ok((String)"\u4fdd\u5b58\u6210\u529f!");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25!");
        }
    }

    @GetMapping(value={"/enhanceButton/{formId}"})
    public Result<?> b(@PathVariable(value="formId") String string, HttpServletRequest httpServletRequest) {
        try {
            List<OnlCgformButton> list = this.onlCgformHeadService.queryButtonList(string);
            if (list == null || list.size() == 0) {
                return Result.error((String)"\u67e5\u8be2\u4e3a\u7a7a");
            }
            return Result.ok(list);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u67e5\u8be2\u5931\u8d25!");
        }
    }

    @GetMapping(value={"/enhanceSql/{formId}"})
    public Result<?> c(@PathVariable(value="formId") String string, HttpServletRequest httpServletRequest) {
        List<OnlCgformEnhanceSql> list = this.onlCgformEnhanceService.queryEnhanceSqlList(string);
        return Result.OK(list);
    }

    @RequiresPermissions(value={"online:form:enhanceSql:save"})
    @PostMapping(value={"/enhanceSql/{formId}"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> a(@PathVariable(value="formId") String string, @RequestBody OnlCgformEnhanceSql onlCgformEnhanceSql) {
        try {
            onlCgformEnhanceSql.setCgformHeadId(string);
            if (this.onlCgformEnhanceService.checkOnlyEnhance(onlCgformEnhanceSql)) {
                this.onlCgformEnhanceService.saveEnhanceSql(onlCgformEnhanceSql);
                return Result.ok((String)"\u4fdd\u5b58\u6210\u529f!");
            }
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25,\u8be5\u6309\u94ae\u5df2\u5b58\u5728\u589e\u5f3a\u914d\u7f6e!");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25!");
        }
    }

    @RequiresPermissions(value={"online:form:enhanceSql:edit"})
    @PutMapping(value={"/enhanceSql/{formId}"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> b(@PathVariable(value="formId") String string, @RequestBody OnlCgformEnhanceSql onlCgformEnhanceSql) {
        try {
            onlCgformEnhanceSql.setCgformHeadId(string);
            if (this.onlCgformEnhanceService.checkOnlyEnhance(onlCgformEnhanceSql)) {
                this.onlCgformEnhanceService.updateEnhanceSql(onlCgformEnhanceSql);
                return Result.ok((String)"\u4fdd\u5b58\u6210\u529f!");
            }
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25,\u8be5\u6309\u94ae\u5df2\u5b58\u5728\u589e\u5f3a\u914d\u7f6e!");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25!");
        }
    }

    @RequiresPermissions(value={"online:form:enhanceSql:delete"})
    @DeleteMapping(value={"/enhanceSql"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> e(@RequestParam(name="id", required=true) String string) {
        try {
            this.onlCgformEnhanceService.deleteEnhanceSql(string);
            return Result.ok((String)"\u5220\u9664\u6210\u529f");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u5220\u9664\u5931\u8d25!");
        }
    }

    @RequiresPermissions(value={"online:form:enhanceSql:batchDelete"})
    @DeleteMapping(value={"/deletebatchEnhanceSql"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> f(@RequestParam(name="ids", required=true) String string) {
        try {
            List<String> list = Arrays.asList(string.split(","));
            this.onlCgformEnhanceService.deleteBatchEnhanceSql(list);
            return Result.ok((String)"\u5220\u9664\u6210\u529f");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u5220\u9664\u5931\u8d25!");
        }
    }

    @GetMapping(value={"/enhanceJava/{formId}"})
    public Result<?> a(@PathVariable(value="formId") String string, OnlCgformEnhanceJava onlCgformEnhanceJava) {
        List<OnlCgformEnhanceJava> list = this.onlCgformEnhanceService.queryEnhanceJavaList(string);
        return Result.OK(list);
    }

    @RequiresPermissions(value={"online:form:enhanceJava:save"})
    @PostMapping(value={"/enhanceJava/{formId}"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> b(@PathVariable(value="formId") String string, @RequestBody OnlCgformEnhanceJava onlCgformEnhanceJava) {
        try {
            if ("1".equals(onlCgformEnhanceJava.getActiveStatus()) && !org.jeecg.modules.online.cgform.d.c.a(onlCgformEnhanceJava)) {
                return Result.error((String)"\u7c7b\u5b9e\u4f8b\u5316\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5!");
            }
            onlCgformEnhanceJava.setCgformHeadId(string);
            String string2 = onlCgformEnhanceJava.getButtonCode();
            if ("import".equals(string2) || "export".equals(string2) || "query".equals(string2)) {
                onlCgformEnhanceJava.setEvent("start");
            }
            if (this.onlCgformEnhanceService.checkOnlyEnhance(onlCgformEnhanceJava)) {
                this.onlCgformEnhanceService.saveEnhanceJava(onlCgformEnhanceJava);
                return Result.ok((String)"\u4fdd\u5b58\u6210\u529f!");
            }
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25\uff1a\u4e00\u4e2a\u6309\u94ae\u3001\u4e8b\u4ef6\u53ea\u80fd\u6709\u4e00\u4e2a\u589e\u5f3a\uff01");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25!");
        }
    }

    @RequiresPermissions(value={"online:form:enhanceJava:edit"})
    @PutMapping(value={"/enhanceJava/{formId}"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> c(@PathVariable(value="formId") String string, @RequestBody OnlCgformEnhanceJava onlCgformEnhanceJava) {
        try {
            if ("1".equals(onlCgformEnhanceJava.getActiveStatus()) && !org.jeecg.modules.online.cgform.d.c.a(onlCgformEnhanceJava)) {
                return Result.error((String)"\u7c7b\u5b9e\u4f8b\u5316\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5!");
            }
            onlCgformEnhanceJava.setCgformHeadId(string);
            String string2 = onlCgformEnhanceJava.getButtonCode();
            if ("import".equals(string2) || "export".equals(string2) || "query".equals(string2)) {
                onlCgformEnhanceJava.setEvent("start");
            }
            if (this.onlCgformEnhanceService.checkOnlyEnhance(onlCgformEnhanceJava)) {
                this.onlCgformEnhanceService.updateEnhanceJava(onlCgformEnhanceJava);
                return Result.ok((String)"\u4fdd\u5b58\u6210\u529f!");
            }
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25\uff1a\u4e00\u4e2a\u6309\u94ae\u3001\u4e8b\u4ef6\u53ea\u80fd\u6709\u4e00\u4e2a\u589e\u5f3a\uff01");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25!");
        }
    }

    @DeleteMapping(value={"/enhanceJava"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> g(@RequestParam(name="id", required=true) String string) {
        try {
            this.onlCgformEnhanceService.deleteEnhanceJava(string);
            return Result.ok((String)"\u5220\u9664\u6210\u529f");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u5220\u9664\u5931\u8d25!");
        }
    }

    @DeleteMapping(value={"/deleteBatchEnhanceJava"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> h(@RequestParam(name="ids", required=true) String string) {
        try {
            List<String> list = Arrays.asList(string.split(","));
            this.onlCgformEnhanceService.deleteBatchEnhanceJava(list);
            return Result.ok((String)"\u5220\u9664\u6210\u529f");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u5220\u9664\u5931\u8d25!");
        }
    }

    @RequiresPermissions(value={"online:form:queryTables"})
    @GetMapping(value={"/queryTables"})
    public Result<?> a(@RequestParam(name="tableName", required=false) String string, @RequestParam(name="pageNo", defaultValue="1") Integer n, @RequestParam(name="pageSize", defaultValue="10") Integer n2, HttpServletRequest httpServletRequest) {
        String string2 = JwtUtil.getUserNameByToken((HttpServletRequest)httpServletRequest);
        List<Object> list = new ArrayList();
        try {
            list = DbReadTableUtil.readAllTableNames();
        }
        catch (SQLException sQLException) {
            a.error(sQLException.getMessage(), (Throwable)sQLException);
            return Result.error((String)"\u540c\u6b65\u5931\u8d25\uff0c\u672a\u83b7\u53d6\u6570\u636e\u5e93\u8868\u4fe1\u606f");
        }
        org.jeecg.modules.online.cgform.d.c.b(list);
        list = org.jeecg.modules.online.cgform.d.c.f(list);
        List<String> list2 = this.onlCgformHeadService.queryOnlinetables();
        this.b();
        list.removeAll(list2);
        ArrayList arrayList = new ArrayList();
        for (String string3 : list) {
            if (this.l(string3)) continue;
            HashMap<String, String> hashMap = new HashMap<String, String>(5);
            hashMap.put("id", string3);
            arrayList.add(hashMap);
        }
        return Result.ok(arrayList);
    }

    @RequiresPermissions(value={"online:form:importTable"})
    @PostMapping(value={"/transTables/{tbnames}"})
    public Result<?> d(@PathVariable(value="tbnames") String string, HttpServletRequest httpServletRequest) {
        String string2 = JwtUtil.getUserNameByToken((HttpServletRequest)httpServletRequest);
        if (oConvertUtils.isEmpty((Object)string)) {
            return Result.error((String)"\u672a\u8bc6\u522b\u7684\u8868\u540d\u4fe1\u606f");
        }
        if (c != null && c.equals(string)) {
            return Result.error((String)"\u4e0d\u5141\u8bb8\u91cd\u590d\u751f\u6210!");
        }
        c = string;
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = false;
        String[] stringArray = string.split(",");
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            Long l2;
            String string3 = stringArray[i2];
            if (!oConvertUtils.isNotEmpty((Object)string3) || (l2 = Long.valueOf(this.onlCgformHeadService.count((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string3)))) > 0L) continue;
            a.info("[IP] [online\u6570\u636e\u5e93\u5bfc\u5165\u8868]   --\u8868\u540d\uff1a" + string3);
            try {
                this.onlCgformHeadService.saveDbTable2Online(string3);
                stringBuilder.append("\u8868[").append(string3).append("]\u5bfc\u5165\u6210\u529f\u3002<br>");
                continue;
            }
            catch (Exception exception) {
                bl = true;
                stringBuilder.append("\u8868[").append(string3).append("]\u5bfc\u5165\u5931\u8d25\uff1a").append(exception.getMessage()).append("<br>");
            }
        }
        c = null;
        if (bl) {
            return Result.error((String)("\u5bfc\u5165\u5b8c\u6210\uff0c\u4f46\u6709\u9519\u8bef\uff1a<br>" + stringBuilder));
        }
        return Result.ok((String)"\u5168\u90e8\u5bfc\u5165\u5b8c\u6210\uff01");
    }

    @RequiresPermissions(value={"online:codeGenerate:projectPath"})
    @GetMapping(value={"/rootFile"})
    public Result<?> a() {
        File[] fileArray;
        JSONArray jSONArray = new JSONArray();
        for (File file : fileArray = File.listRoots()) {
            JSONObject jSONObject = new JSONObject();
            if (!file.isDirectory()) continue;
            jSONObject.put("key", (Object)file.getAbsolutePath());
            jSONObject.put("title", (Object)file.getPath());
            jSONObject.put("opened", (Object)false);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("icon", (Object)"custom");
            jSONObject.put("scopedSlots", (Object)jSONObject2);
            jSONObject.put("isLeaf", (Object)(file.listFiles() == null || file.listFiles().length == 0 ? 1 : 0));
            jSONArray.add((Object)jSONObject);
        }
        return Result.ok((Object)jSONArray);
    }

    @RequiresPermissions(value={"online:codeGenerate:projectPath"})
    @GetMapping(value={"/fileTree"})
    public Result<?> i(@RequestParam(name="parentPath", required=true) String string) {
        File[] fileArray;
        JSONArray jSONArray = new JSONArray();
        File file = new File(string);
        for (File file2 : fileArray = file.listFiles()) {
            if (!file2.isDirectory() || file2.isHidden() || !oConvertUtils.isNotEmpty((Object)file2.getPath())) continue;
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("key", (Object)file2.getAbsolutePath());
            jSONObject.put("title", (Object)file2.getPath().substring(file2.getPath().lastIndexOf(File.separator) + 1));
            jSONObject.put("isLeaf", (Object)(file2.listFiles() == null || file2.listFiles().length == 0 ? 1 : 0));
            jSONObject.put("opened", (Object)false);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("icon", (Object)"custom");
            jSONObject.put("scopedSlots", (Object)jSONObject2);
            jSONArray.add((Object)jSONObject);
        }
        return Result.ok((Object)jSONArray);
    }

    @GetMapping(value={"/tableInfo"})
    public Result<?> j(@RequestParam(name="code", required=true) String string) {
        List<OnlCgformHead> list;
        Object object;
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
        if (onlCgformHead == null) {
            return Result.error((String)"\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        }
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("main", onlCgformHead);
        if (onlCgformHead.getTableType() == 2 && oConvertUtils.isNotEmpty((Object)(object = onlCgformHead.getSubTableStr()))) {
            String[] stringArray;
            list = new ArrayList();
            for (String string2 : stringArray = ((String)object).split(",")) {
                LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
                lambdaQueryWrapper.eq(OnlCgformHead::getTableName, (Object)string2);
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)lambdaQueryWrapper);
                list.add(onlCgformHead2);
            }
            Collections.sort(list, new Comparator<OnlCgformHead>(){

                public int a(OnlCgformHead onlCgformHead, OnlCgformHead onlCgformHead2) {
                    Integer n;
                    Integer n2 = onlCgformHead.getTabOrderNum();
                    if (n2 == null) {
                        n2 = 0;
                    }
                    if ((n = onlCgformHead2.getTabOrderNum()) == null) {
                        n = 0;
                    }
                    return n2.compareTo(n);
                }

                @Override
                public /* synthetic */ int compare(Object object, Object object2) {
                    return this.a((OnlCgformHead)object, (OnlCgformHead)object2);
                }
            });
            hashMap.put("sub", list);
        }
        object = onlCgformHead.getTableType();
        if ("Y".equals(onlCgformHead.getIsTree())) {
            object = 3;
        }
        list = CgformEnum.getJspModelList((int)((Integer)object));
        hashMap.put("jspModeList", list);
        hashMap.put("projectPath", DbReadTableUtil.getProjectPath());
        return Result.ok(hashMap);
    }

    @PostMapping(value={"/copyOnline"})
    public Result<?> k(@RequestParam(name="code", required=true) String string) {
        try {
            OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
            if (onlCgformHead == null) {
                return Result.error((String)"\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
            }
            this.onlCgformHeadService.copyOnlineTableConfig(onlCgformHead);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return Result.ok();
    }

    @GetMapping(value={"/copyOnlineTable/{id}"})
    public Result<?> b(@PathVariable(value="id") String string, @RequestParam(name="tableName") String string2) {
        try {
            this.onlCgformHeadService.copyOnlineTable(string, string2);
        }
        catch (JeecgBootException jeecgBootException) {
            return Result.error((String)jeecgBootException.getMessage());
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)exception.getMessage());
        }
        return Result.ok();
    }

    private boolean l(String string) {
        if (b != null) {
            for (String string2 : b) {
                if (!string.startsWith(string2) && !string.startsWith(string2.toUpperCase())) continue;
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void b() {
        if (b == null) {
            Object object;
            ResourceBundle resourceBundle = org.jeecg.modules.online.config.c.d.d("jeecg/jeecg_config");
            if (resourceBundle != null && resourceBundle.containsKey("exclude_table") && (object = resourceBundle.getString("exclude_table")) != null) {
                b = Arrays.asList(((String)object).split(","));
                return;
            }
            object = null;
            try {
                Resource resource = this.resourceLoader.getResource("classpath:jeecg" + File.separator + "jeecg_config.properties");
                object = resource.getInputStream();
                Properties properties = new Properties();
                properties.load((InputStream)object);
                String string = properties.getProperty("exclude_table");
                if (string != null) {
                    b = Arrays.asList(string.split(","));
                }
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            finally {
                if (object != null) {
                    try {
                        ((InputStream)object).close();
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                }
            }
        }
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getTableName": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getTableName;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

