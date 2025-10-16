/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.io.FileUtil
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.google.common.collect.Lists
 *  io.swagger.v3.oas.annotations.Operation
 *  io.swagger.v3.oas.annotations.tags.Tag
 *  javax.servlet.ServletOutputStream
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.commons.io.IOUtils
 *  org.apache.poi.ss.usermodel.Workbook
 *  org.apache.poi.xssf.usermodel.XSSFWorkbook
 *  org.apache.shiro.SecurityUtils
 *  org.apache.shiro.authz.annotation.Logical
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.apache.shiro.authz.annotation.RequiresRoles
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.aspect.annotation.AutoLog
 *  org.jeecg.common.aspect.annotation.OnlineAuth
 *  org.jeecg.common.aspect.annotation.PermissionData
 *  org.jeecg.common.constant.enums.ModuleType
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.util.JwtUtil
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.system.vo.LoginUser
 *  org.jeecg.common.util.BrowserUtils
 *  org.jeecg.common.util.RedisUtil
 *  org.jeecg.common.util.SpringContextUtils
 *  org.jeecg.common.util.SqlInjectionUtil
 *  org.jeecg.common.util.TokenUtils
 *  org.jeecg.common.util.oConvertUtils
 *  org.jeecg.config.JeecgBaseConfig
 *  org.jeecgframework.codegenerate.database.DbReadTableUtil
 *  org.jeecgframework.poi.excel.ExcelExportUtil
 *  org.jeecgframework.poi.excel.ExcelImportUtil
 *  org.jeecgframework.poi.excel.entity.ExportParams
 *  org.jeecgframework.poi.excel.entity.ImportParams
 *  org.jeecgframework.poi.excel.entity.enmus.ExcelType
 *  org.jeecgframework.poi.handler.inter.IExcelDataHandler
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.cache.annotation.CacheEvict
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer
 *  org.springframework.jdbc.support.incrementer.PostgreSQLSequenceMaxValueIncrementer
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 *  org.springframework.web.multipart.MultipartHttpServletRequest
 */
package org.jeecg.modules.online.cgform.c;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.SerializedLambda;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.OnlineAuth;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.constant.enums.ModuleType;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.BrowserUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.online.auth.service.IOnlAuthPageService;
import org.jeecg.modules.online.cgform.converter.b;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.d.f;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.model.TreeModel;
import org.jeecg.modules.online.cgform.service.IOnlCgformAiService;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlCgformSqlService;
import org.jeecg.modules.online.cgform.service.IOnlineJoinQueryService;
import org.jeecg.modules.online.cgform.service.IOnlineService;
import org.jeecg.modules.online.config.c.d;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.jeecgframework.codegenerate.database.DbReadTableUtil;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.handler.inter.IExcelDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.PostgreSQLSequenceMaxValueIncrementer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.SerializedLambda;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.OnlineAuth;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.constant.enums.ModuleType;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.BrowserUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.online.auth.service.IOnlAuthPageService;
import org.jeecg.modules.online.cgform.converter.b;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.d.f;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.model.TreeModel;
import org.jeecg.modules.online.cgform.service.IOnlCgformAiService;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlCgformSqlService;
import org.jeecg.modules.online.cgform.service.IOnlineJoinQueryService;
import org.jeecg.modules.online.cgform.service.IOnlineService;
import org.jeecg.modules.online.config.c.d;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.jeecgframework.codegenerate.database.DbReadTableUtil;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.handler.inter.IExcelDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.PostgreSQLSequenceMaxValueIncrementer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Tag(name="Online\u8868\u5355\u5f00\u53d1")
@RestController(value="onlCgformApiController")
@RequestMapping(value={"/online/cgform/api"})
public class CgformApiInternalA {
    private static final Logger a = LoggerFactory.getLogger(CgformApiInternalA.class);
    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;
    @Autowired
    IOnlineJoinQueryService onlineJoinQueryService;
    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;
    @Autowired
    private IOnlCgformSqlService onlCgformSqlService;
    @Autowired
    private IOnlAuthPageService onlAuthPageService;
    @Lazy
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private IOnlineService onlineService;
    @Value(value="${jeecg.path.upload}")
    private String upLoadPath;
    @Value(value="${jeecg.uploadType}")
    private String uploadType;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JeecgBaseConfig jeecgBaseConfig;
    @Autowired
    private IOnlCgformAiService iOnlCgformAiService;

    @PostMapping(value={"/addAll"})
    public Result<?> a(@RequestBody org.jeecg.modules.online.cgform.model.a a2) {
        try {
            String string = a2.getHead().getTableName();
            if (d.a(string).booleanValue()) {
                return Result.error((String)("\u6570\u636e\u5e93\u8868[" + string + "]\u5df2\u5b58\u5728,\u8bf7\u4ece\u6570\u636e\u5e93\u5bfc\u5165\u8868\u5355"));
            }
            if (a2.getHead().getTableType() == 3) {
                if (oConvertUtils.isEmpty((Object)a2.getHead().getRelationType())) {
                    return Result.error((String)"\u9644\u8868\u5fc5\u987b\u9009\u62e9\u6620\u5c04\u5173\u7cfb\uff01");
                }
                if (oConvertUtils.isEmpty((Object)a2.getHead().getTabOrderNum())) {
                    return Result.error((String)"\u9644\u8868\u5fc5\u987b\u586b\u5199\u6392\u5e8f\u5e8f\u53f7\uff01");
                }
            }
            return this.onlCgformHeadService.addAll(a2);
        }
        catch (Exception exception) {
            a.error("OnlCgformApiController.addAll()\u53d1\u751f\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u64cd\u4f5c\u5931\u8d25");
        }
    }

    @PostMapping(value={"/aigc"})
    @Operation(summary="\u901a\u8fc7AI\u751f\u6210\u4e00\u4e2a\u6a21\u5757\u7684\u8868\u8bbe\u8ba1")
    public Result<?> a(@RequestParam(name="prompt", required=true) String string) {
        long l2 = System.currentTimeMillis();
        a.info("online\u751f\u6210\u8868\u8bbe\u8ba1\u5f00\u59cb:{}", (Object)l2);
        Result<?> result = this.iOnlCgformAiService.genSingleSchema4Modules(string);
        a.info("online\u751f\u6210\u8868\u8bbe\u8ba1\u7ed3\u675f,\u8017\u65f6{}", (Object)(System.currentTimeMillis() - l2));
        return result;
    }

    @PostMapping(value={"/aigc/fields"})
    public Result<?> a(@RequestParam(name="code", required=false) String string, @RequestParam(name="prompt", required=true) String string2) {
        long l2 = System.currentTimeMillis();
        a.info("online\u751f\u6210\u8868\u5b57\u6bb5\u5f00\u59cb:{}", (Object)l2);
        Result<?> result = this.iOnlCgformAiService.aiGenFields(string, string2);
        a.info("online\u751f\u6210\u8868\u5b57\u6bb5\u7ed3\u675f,\u8017\u65f6{}", (Object)(System.currentTimeMillis() - l2));
        return result;
    }

    @RequiresRoles(value={"admin", "lowdeveloper"}, logical=Logical.OR)
    @PostMapping(value={"/aigc/mock/data/{code}"})
    public Result<?> a(@PathVariable(value="code") String string, @RequestParam(value="count", required=false, defaultValue="3") Integer n) {
        long l2 = System.currentTimeMillis();
        a.info("online\u751f\u6210\u6570\u636e\u5f00\u59cb:{}", (Object)l2);
        Result<?> result = this.iOnlCgformAiService.aiGenMockData(string, n);
        a.info("online\u751f\u6210\u6570\u636e\u7ed3\u675f,\u8017\u65f6{}", (Object)(System.currentTimeMillis() - l2));
        return result;
    }

    @PutMapping(value={"/editAll"})
    @CacheEvict(value={"sys:cache:online:list", "sys:cache:online:form"}, allEntries=true, beforeInvocation=true)
    public Result<?> b(@RequestBody org.jeecg.modules.online.cgform.model.a a2) {
        try {
            if (a2.getHead().getTableType() == 3) {
                if (oConvertUtils.isEmpty((Object)a2.getHead().getRelationType())) {
                    return Result.error((String)"\u9644\u8868\u5fc5\u987b\u9009\u62e9\u6620\u5c04\u5173\u7cfb\uff01");
                }
                if (oConvertUtils.isEmpty((Object)a2.getHead().getTabOrderNum())) {
                    return Result.error((String)"\u9644\u8868\u5fc5\u987b\u586b\u5199\u6392\u5e8f\u5e8f\u53f7\uff01");
                }
            }
            return this.onlCgformHeadService.editAll(a2);
        }
        catch (Exception exception) {
            a.error("OnlCgformApiController.editAll()\u53d1\u751f\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u64cd\u4f5c\u5931\u8d25");
        }
    }

    @AutoLog(operateType=1, value="online\u5217\u8868\u52a0\u8f7d", module=ModuleType.ONLINE)
    @OnlineAuth(value="getColumns")
    @GetMapping(value={"/getColumns/{code}"})
    public Result<org.jeecg.modules.online.cgform.model.b> a(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        Result result = new Result();
        OnlCgformHead onlCgformHead = null;
        try {
            onlCgformHead = this.onlCgformHeadService.getTable(string);
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            result.error500("Online\u8868\u5355\u4e0d\u5b58\u5728\uff01");
            return result;
        }
        String string2 = httpServletRequest.getParameter("linkTableSelectFields");
        if (oConvertUtils.isNotEmpty((Object)string2)) {
            onlCgformHead.setSelectFieldString(string2);
        }
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        org.jeecg.modules.online.cgform.model.b b2 = this.onlineService.queryOnlineConfig(onlCgformHead, loginUser.getUsername());
        b2.setIsDesForm(onlCgformHead.getIsDesForm());
        b2.setDesFormCode(onlCgformHead.getDesFormCode());
        result.setResult((Object)b2);
        result.setOnlTable(onlCgformHead.getTableName());
        return result;
    }

    @PermissionData
    @OnlineAuth(value="getData")
    @GetMapping(value={"/getData/{code}"})
    public Result<Map<String, Object>> b(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        Result result = new Result();
        OnlCgformHead onlCgformHead = null;
        try {
            onlCgformHead = this.onlCgformHeadService.getTable(string);
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            result.error500("\u5b9e\u4f53\u4e0d\u5b58\u5728");
            return result;
        }
        if (oConvertUtils.isEmpty((Object)onlCgformHead.getPhysicId()) && "N".equals(onlCgformHead.getIsDbSynch())) {
            result.error500("NO_DB_SYNC");
            return result;
        }
        String string2 = httpServletRequest.getParameter("linkTableSelectFields");
        if (oConvertUtils.isNotEmpty((Object)string2)) {
            onlCgformHead.setSelectFieldString(string2);
        }
        Map<String, Object> map = null;
        try {
            Map<String, Object> map2 = c.a(httpServletRequest);
            boolean bl = c.a(onlCgformHead);
            map = bl ? this.onlineJoinQueryService.pageList(onlCgformHead, map2) : this.onlCgformFieldService.queryAutolistPage(onlCgformHead, map2, null);
            this.a(onlCgformHead, map);
            result.setResult(map);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            result.error500("\u6570\u636e\u5e93\u67e5\u8be2\u5931\u8d25\uff0c" + exception.getMessage());
        }
        result.setOnlTable(onlCgformHead.getTableName());
        return result;
    }

    @PermissionData
    @GetMapping(value={"/4jmbi/getLinkData"})
    public Result<Map<String, Object>> a(@RequestParam(value="code") String string, @RequestParam(value="field") String string2, HttpServletRequest httpServletRequest) {
        OnlCgformHead onlCgformHead;
        try {
            onlCgformHead = this.onlCgformHeadService.getTable(string);
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            Result.error((String)"\u5b9e\u4f53\u4e0d\u5b58\u5728");
            return Result.ok();
        }
        OnlCgformField onlCgformField = this.onlCgformFieldService.queryFormFieldByTableNameAndField(onlCgformHead.getTableName(), string2);
        if (oConvertUtils.isNotEmpty((Object)onlCgformField) && oConvertUtils.isNotEmpty((Object)onlCgformField.getDictTable()) && "link_table".equalsIgnoreCase(onlCgformField.getFieldShowType())) {
            return this.b(onlCgformField.getDictTable(), httpServletRequest);
        }
        return Result.ok();
    }

    @AutoLog(operateType=1, value="online\u8868\u5355\u52a0\u8f7d", module=ModuleType.ONLINE)
    @OnlineAuth(value="getFormItem")
    @GetMapping(value={"/getFormItem/{code}"})
    public Result<?> c(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        JSONObject jSONObject;
        OnlCgformHead onlCgformHead = null;
        try {
            onlCgformHead = this.onlCgformHeadService.getTable(string);
        }
        catch (org.jeecg.modules.online.config.exception.a a2) {
            return Result.error((String)"Online\u8868\u5355\u4e0d\u5b58\u5728\uff01");
        }
        Result result = new Result();
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String string2 = httpServletRequest.getParameter("selectFields");
        if (oConvertUtils.isNotEmpty((Object)string2)) {
            jSONObject = Arrays.asList(string2.split(","));
        }
        jSONObject = this.onlineService.queryOnlineFormItem(onlCgformHead, loginUser.getUsername());
        result.setResult((Object)c.b(jSONObject));
        result.setOnlTable(onlCgformHead.getTableName());
        return result;
    }

    @AutoLog(operateType=1, value="online\u6839\u636e\u8868\u540d\u52a0\u8f7d\u8868\u5355", module=ModuleType.ONLINE)
    @GetMapping(value={"/getFormItemBytbname/{table}"})
    public Result<?> b(@PathVariable(value="table") String string, @RequestParam(name="taskId", required=false) String string2) {
        Result result = new Result();
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformHead::getTableName, (Object)string);
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)lambdaQueryWrapper);
        if (onlCgformHead == null) {
            Result.error((String)"Online\u8868\u5355\u4e0d\u5b58\u5728\uff01");
        }
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        JSONObject jSONObject = this.onlineService.queryFlowOnlineFormItem(onlCgformHead, loginUser.getUsername(), string2);
        result.setResult((Object)c.b(jSONObject));
        result.setOnlTable(string);
        return result;
    }

    @OnlineAuth(value="getEnhanceJs")
    @GetMapping(value={"/getEnhanceJs/{code}"})
    public Result<?> d(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        String string2 = this.onlineService.queryEnahcneJsString(string, "form");
        return Result.ok((String)string2);
    }

    @AutoLog(operateType=1, value="online\u8868\u5355\u6570\u636e\u67e5\u8be2")
    @GetMapping(value={"/form/{code}/{id}"})
    public Result<?> c(@PathVariable(value="code") String string, @PathVariable(value="id") String string2) {
        try {
            SqlInjectionUtil.filterContent((String)string2, (String)"'");
            Map<String, Object> map = this.onlCgformHeadService.queryManyFormData(string, string2);
            return Result.ok(c.a(map));
        }
        catch (Exception exception) {
            a.error("Online\u8868\u5355\u67e5\u8be2\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)("\u67e5\u8be2\u5931\u8d25\uff0c" + exception.getMessage()));
        }
    }

    @AutoLog(operateType=1, value="online\u8868\u5355\u6570\u636e\u67e5\u8be2")
    @GetMapping(value={"/detail/{code}/{id}"})
    public Result<?> d(@PathVariable(value="code") String string, @PathVariable(value="id") String string2) {
        try {
            SqlInjectionUtil.filterContent((String)string2, (String)"'");
            Map<String, Object> map = this.onlCgformHeadService.queryManyFormData(string, string2);
            ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
            arrayList.add(c.a(map));
            OnlCgformHead onlCgformHead = this.onlCgformHeadService.getTable(string);
            this.onlCgformFieldService.handleLinkTableDictData(onlCgformHead.getId(), arrayList);
            return Result.ok(arrayList.get(0));
        }
        catch (Exception exception) {
            a.error("Online\u8868\u5355\u67e5\u8be2\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)("\u67e5\u8be2\u5931\u8d25\uff0c" + exception.getMessage()));
        }
    }

    @GetMapping(value={"/subform/{table}/{mainId}"})
    public Result<?> e(@PathVariable(value="table") String string, @PathVariable(value="mainId") String string2) {
        try {
            SqlInjectionUtil.filterContent((String)string2, (String)"'");
            Map<String, Object> map = this.onlCgformHeadService.querySubFormData(string, string2);
            return Result.ok(c.a(map));
        }
        catch (Exception exception) {
            a.error("Online\u8868\u5355\u67e5\u8be2\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)("\u67e5\u8be2\u5931\u8d25\uff0c" + exception.getMessage()));
        }
    }

    @GetMapping(value={"/subform/list/{table}/{mainId}"})
    public Result<?> f(@PathVariable(value="table") String string, @PathVariable(value="mainId") String string2) {
        try {
            SqlInjectionUtil.filterContent((String)string2, (String)"'");
            return Result.ok(this.onlCgformHeadService.queryManySubFormData(string, string2));
        }
        catch (Exception exception) {
            a.error("Online\u8868\u5355\u67e5\u8be2\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)("\u67e5\u8be2\u5931\u8d25\uff0c" + exception.getMessage()));
        }
    }

    @AutoLog(operateType=1, value="online\u6839\u636e\u8868\u540d\u67e5\u8be2\u8868\u5355\u6570\u636e", module=ModuleType.ONLINE)
    @GetMapping(value={"/form/table_name/{tableName}/{dataId}"})
    public Result<?> g(@PathVariable(value="tableName") String string, @PathVariable(value="dataId") String string2) {
        try {
            LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(OnlCgformHead::getTableName, (Object)string);
            OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)lambdaQueryWrapper);
            if (onlCgformHead == null) {
                throw new Exception("OnlCgform tableName: " + string + " \u4e0d\u5b58\u5728\uff01");
            }
            SqlInjectionUtil.filterContent((String)string2, (String)"'");
            Result<?> result = this.c(onlCgformHead.getId(), string2);
            result.setOnlTable(string);
            return result;
        }
        catch (Exception exception) {
            a.error("Online\u8868\u5355\u67e5\u8be2\u5f02\u5e38\uff0c" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)("\u67e5\u8be2\u5931\u8d25\uff0c" + exception.getMessage()));
        }
    }

    @AutoLog(operateType=2, value="online\u65b0\u589e\u6570\u636e", module=ModuleType.ONLINE)
    @OnlineAuth(value="form")
    @PostMapping(value={"/form/{code}"})
    @CacheEvict(value={"sys:cache:online:linkTable"}, allEntries=true)
    public Result<String> a(@PathVariable(value="code") String string, @RequestBody JSONObject jSONObject, HttpServletRequest httpServletRequest) {
        Result result = new Result();
        try {
            String string2 = c.a();
            jSONObject.put("id", (Object)string2);
            String string3 = TokenUtils.getTokenByRequest((HttpServletRequest)httpServletRequest);
            String string4 = this.onlCgformHeadService.saveManyFormData(string, jSONObject, string3);
            result.setSuccess(true);
            result.setResult((Object)string2);
            result.setOnlTable(string4);
            result.setMessage("\u6dfb\u52a0\u6210\u529f!");
        }
        catch (Exception exception) {
            a.error("OnlCgformApiController.formAdd()\u53d1\u751f\u5f02\u5e38\uff1a", (Throwable)exception);
            result.setSuccess(false);
            result.setMessage("\u4fdd\u5b58\u5931\u8d25\uff0c" + c.a(exception));
        }
        return result;
    }

    @AutoLog(operateType=3, value="online\u4fee\u6539\u6570\u636e", module=ModuleType.ONLINE)
    @OnlineAuth(value="form")
    @PutMapping(value={"/form/{code}"})
    @CacheEvict(value={"sys:cache:online:linkTable"}, allEntries=true)
    public Result<?> a(@PathVariable(value="code") String string, @RequestBody JSONObject jSONObject) {
        try {
            String string2 = this.onlCgformHeadService.editManyFormData(string, jSONObject);
            Result result = Result.ok((String)"\u4fee\u6539\u6210\u529f\uff01");
            result.setOnlTable(string2);
            return result;
        }
        catch (Exception exception) {
            a.error("OnlCgformApiController.formEdit()\u53d1\u751f\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)("\u4fee\u6539\u5931\u8d25\uff0c" + c.a(exception)));
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @AutoLog(operateType=4, value="online\u5220\u9664\u6570\u636e", module=ModuleType.ONLINE)
    @OnlineAuth(value="form")
    @DeleteMapping(value={"/form/{code}/{id}"})
    public Result<?> h(@PathVariable(value="code") String string, @PathVariable(value="id") String string2) {
        String string3;
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
        if (onlCgformHead == null) {
            return Result.error((String)"\u5b9e\u4f53\u4e0d\u5b58\u5728");
        }
        try {
            string3 = "";
            if ("Y".equals(onlCgformHead.getIsTree())) {
                string2 = this.onlCgformFieldService.queryTreeChildIds(onlCgformHead, string2);
                string3 = this.onlCgformFieldService.queryTreePids(onlCgformHead, string2);
            }
            if (string2.indexOf(",") > 0) {
                String string4;
                if (onlCgformHead.getTableType() == 2) {
                    this.onlCgformFieldService.deleteAutoListMainAndSub(onlCgformHead, string2);
                } else {
                    string4 = onlCgformHead.getTableName();
                    this.onlCgformFieldService.deleteAutoListById(string4, string2);
                }
                if ("Y".equals(onlCgformHead.getIsTree())) {
                    String[] stringArray;
                    string4 = onlCgformHead.getTableName();
                    String string5 = onlCgformHead.getTreeIdField();
                    for (String string6 : stringArray = string3.split(",")) {
                        this.onlCgformFieldService.updateTreeNodeNoChild(string4, string5, string6);
                    }
                }
            } else {
                this.onlCgformHeadService.deleteOneTableInfo(string, string2);
            }
            if (oConvertUtils.isNotEmpty((Object)onlCgformHead.getIsDesForm()) && !"1".equals(onlCgformHead.getIsDesForm())) {
                // empty if block
            }
        }
        catch (Exception exception) {
            a.error("OnlCgformApiController.formEdit()\u53d1\u751f\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)("\u5220\u9664\u5931\u8d25," + exception.getMessage()));
        }
        string3 = Result.ok((String)"\u5220\u9664\u6210\u529f!");
        string3.setOnlTable(onlCgformHead.getTableName());
        return string3;
    }

    @AutoLog(operateType=4, value="online\u5220\u9664\u6570\u636e", module=ModuleType.ONLINE)
    @DeleteMapping(value={"/formByCode/{code}/{id}"})
    public Result<?> i(@PathVariable(value="code") String string, @PathVariable(value="id") String string2) {
        try {
            String string3 = this.onlCgformHeadService.deleteDataByCode(string, string2);
            Result result = Result.OK((String)"\u5220\u9664\u6210\u529f!", (Object)string3);
            result.setOnlTable(string3);
            return result;
        }
        catch (JeecgBootException jeecgBootException) {
            return Result.error((String)jeecgBootException.getMessage());
        }
    }

    @OnlineAuth(value="getQueryInfo")
    @GetMapping(value={"/getQueryInfo/{code}"})
    public Result<?> b(@PathVariable(value="code") String string) {
        try {
            List<Map<String, String>> list = this.onlCgformFieldService.getAutoListQueryInfo(string);
            return Result.ok(list);
        }
        catch (Exception exception) {
            a.error("OnlCgformApiController.getQueryInfo()\u53d1\u751f\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u67e5\u8be2\u5931\u8d25");
        }
    }

    @GetMapping(value={"/getQueryInfoVue3/{code}"})
    public Result<?> c(@PathVariable(value="code") String string) {
        try {
            JSONObject jSONObject = this.onlineService.getOnlineVue3QueryInfo(string);
            return Result.ok((Object)jSONObject);
        }
        catch (Exception exception) {
            a.error("\u83b7\u53d6online\u67e5\u8be2\u914d\u7f6e\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u83b7\u53d6Online\u8868\u5355\u7684\u67e5\u8be2\u6761\u4ef6\u5931\u8d25!");
        }
    }

    @PostMapping(value={"/doDbSynch/{code}/{synMethod}"})
    @RequiresPermissions(value={"online:form:syncDb"})
    public Result<?> j(@PathVariable(value="code") String string, @PathVariable(value="synMethod") String string2) {
        try {
            long l2 = System.currentTimeMillis();
            this.onlCgformHeadService.doDbSynch(string, string2);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)("\u540c\u6b65\u6570\u636e\u5e93\u5931\u8d25\uff0c" + c.a(exception)));
        }
        return Result.ok((String)"\u540c\u6b65\u6570\u636e\u5e93\u6210\u529f!");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @OnlineAuth(value="exportXls")
    @PermissionData
    @GetMapping(value={"/exportXls/{code}"})
    public void a(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
        if (onlCgformHead == null) {
            return;
        }
        String string2 = onlCgformHead.getTableTxt();
        String string3 = httpServletRequest.getParameter("paramsStr");
        Map<String, Object> map = new HashMap<String, Object>(5);
        Object var8_8 = null;
        if (oConvertUtils.isNotEmpty((Object)string3)) {
            map = (Map)JSONObject.parseObject((String)string3, Map.class);
        }
        XSSFWorkbook xSSFWorkbook = this.onlineJoinQueryService.handleOnlineExport(onlCgformHead, map);
        OutputStream outputStream = null;
        try {
            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String string4 = BrowserUtils.checkBrowse((HttpServletRequest)httpServletRequest);
            String string5 = onlCgformHead.getTableTxt() + "-v" + onlCgformHead.getTableVersion();
            if ("MSIE".equalsIgnoreCase(string4.substring(0, 4))) {
                httpServletResponse.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(string5, "UTF-8") + ".xlsx");
            } else {
                String string6 = new String(string5.getBytes("UTF-8"), "ISO8859-1");
                httpServletResponse.setHeader("content-disposition", "attachment;filename=" + string6 + ".xlsx");
            }
            outputStream = httpServletResponse.getOutputStream();
            xSSFWorkbook.write(outputStream);
            httpServletResponse.flushBuffer();
        }
        catch (Exception exception) {
            a.error("--\u901a\u8fc7\u6d41\u7684\u65b9\u5f0f\u83b7\u53d6\u6587\u4ef6\u5f02\u5e38--" + exception.getMessage(), (Throwable)exception);
        }
        finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                }
                catch (IOException iOException) {
                    a.error(iOException.getMessage(), (Throwable)iOException);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @OnlineAuth(value="exportXlsOld")
    @PermissionData
    @GetMapping(value={"/exportXlsOld/{code}"})
    public void b(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Workbook workbook;
        String string2;
        List<String> list;
        String string3;
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
        if (onlCgformHead == null) {
            return;
        }
        String string4 = onlCgformHead.getTableTxt();
        String string5 = httpServletRequest.getParameter("paramsStr");
        HashMap<String, Object> hashMap = new HashMap<String, Integer>(5);
        Object var8_8 = null;
        if (oConvertUtils.isNotEmpty((Object)string5)) {
            hashMap = (Map)JSONObject.parseObject((String)string5, Map.class);
        }
        hashMap.put("pageSize", -521);
        boolean bl = c.a(onlCgformHead);
        Map<String, Object> map2 = null;
        map2 = bl ? this.onlineJoinQueryService.pageList(onlCgformHead, hashMap, true) : this.onlCgformFieldService.queryAutolistPage(onlCgformHead, hashMap, null);
        List list2 = (List)map2.get("fieldList");
        ArrayList arrayList = (ArrayList)map2.get("records");
        List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
        String string6 = string3 = hashMap.get("selections") == null ? null : hashMap.get("selections").toString();
        if (oConvertUtils.isNotEmpty((Object)string3)) {
            list = c.h(string3);
            list3 = arrayList.stream().filter(map -> list.contains(map.get("id"))).collect(Collectors.toList());
        } else {
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            list3.addAll(arrayList);
        }
        b.a(1, list3, list2);
        try {
            this.onlCgformHeadService.executeEnhanceExport(onlCgformHead, list3);
        }
        catch (BusinessException businessException) {
            a.error("\u5bfc\u51fajava\u589e\u5f3a\u5904\u7406\u51fa\u9519", (Object)businessException.getMessage());
        }
        list = c.b(list2, "id", this.upLoadPath);
        if (onlCgformHead.getTableType() == 2 && oConvertUtils.isEmpty(hashMap.get("exportSingleOnly")) && oConvertUtils.isNotEmpty((Object)(string2 = onlCgformHead.getSubTableStr()))) {
            for (Object object : workbook = string2.split(",")) {
                this.onlineJoinQueryService.addAllSubTableDate((String)object, hashMap, list3, list, false);
            }
        }
        string4 = oConvertUtils.getNormalString((String)string4);
        string2 = new ExportParams(null, string4);
        string2.setType(ExcelType.XSSF);
        workbook = ExcelExportUtil.exportExcel((ExportParams)string2, list, list3);
        Workbook workbook2 = null;
        try {
            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String string7 = BrowserUtils.checkBrowse((HttpServletRequest)httpServletRequest);
            String string8 = onlCgformHead.getTableTxt() + "-v" + onlCgformHead.getTableVersion();
            string8 = oConvertUtils.getNormalString((String)string8);
            if ("MSIE".equalsIgnoreCase(string7.substring(0, 4))) {
                httpServletResponse.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(string8, "UTF-8") + ".xlsx");
            } else {
                Object object;
                object = new String(string8.getBytes("UTF-8"), "ISO8859-1");
                httpServletResponse.setHeader("content-disposition", "attachment;filename=" + (String)object + ".xlsx");
            }
            workbook2 = httpServletResponse.getOutputStream();
            workbook.write((OutputStream)workbook2);
            httpServletResponse.flushBuffer();
        }
        catch (Exception exception) {
            a.error("--\u901a\u8fc7\u6d41\u7684\u65b9\u5f0f\u83b7\u53d6\u6587\u4ef6\u5f02\u5e38--" + exception.getMessage(), (Throwable)exception);
        }
        finally {
            if (workbook2 != null) {
                try {
                    workbook2.close();
                }
                catch (IOException iOException) {
                    a.error(iOException.getMessage(), (Throwable)iOException);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive exception aggregation
     */
    @OnlineAuth(value="importXls")
    @PostMapping(value={"/importXls/{code}"})
    public Result<?> c(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        long l2 = System.currentTimeMillis();
        Result result = new Result();
        String string2 = "";
        String string3 = httpServletRequest.getParameter("validateStatus");
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Object object;
            List<String> list;
            Object object2;
            OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
            if (onlCgformHead == null) {
                return Result.error((String)"\u6570\u636e\u5e93\u4e0d\u5b58\u5728\u8be5\u8868\u8bb0\u5f55");
            }
            LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, (Object)string);
            List list2 = this.onlCgformFieldService.list((Wrapper)lambdaQueryWrapper);
            String string4 = httpServletRequest.getParameter("isSingleTableImport");
            List<String> list3 = c.e(list2);
            if (oConvertUtils.isEmpty((Object)string4) && onlCgformHead.getTableType() == 2 && oConvertUtils.isNotEmpty((Object)onlCgformHead.getSubTableStr())) {
                for (String string5 : onlCgformHead.getSubTableStr().split(",")) {
                    object2 = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string5));
                    if (object2 == null || (list = c.c((List<OnlCgformField>)(object = this.onlCgformFieldService.list((Wrapper)new LambdaQueryWrapper().eq(OnlCgformField::getCgformHeadId, (Object)((OnlCgformHead)object2).getId()))), ((OnlCgformHead)object2).getTableTxt())).size() <= 0) continue;
                    list3.addAll(list);
                }
            }
            String[] stringArray = null;
            String string6 = httpServletRequest.getParameter("foreignKeys");
            if (oConvertUtils.isNotEmpty((Object)string6)) {
                stringArray = JSONObject.parseObject((String)string6);
            }
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)httpServletRequest;
            Map map = multipartHttpServletRequest.getFileMap();
            object2 = (DataSource)SpringContextUtils.getApplicationContext().getBean(DataSource.class);
            object = d.a((DataSource)object2);
            for (Map.Entry entry : map.entrySet()) {
                Object object3;
                MultipartFile multipartFile = (MultipartFile)entry.getValue();
                ImportParams importParams = new ImportParams();
                importParams.setDataHanlder((IExcelDataHandler)new org.jeecg.modules.online.cgform.d.b(list2, this.upLoadPath, this.uploadType));
                InputStream inputStream = multipartFile.getInputStream();
                try {
                    Object object4;
                    Serializable serializable;
                    List list4 = ExcelImportUtil.importExcel((InputStream)inputStream, Map.class, (ImportParams)importParams);
                    if (list4 == null) {
                        string2 = "\u8bc6\u522b\u6a21\u7248\u6570\u636e\u9519\u8bef";
                        a.error(string2);
                        continue;
                    }
                    if (org.jeecg.modules.online.cgform.enums.a.c.equals(onlCgformHead.getTableType()) && onlCgformHead.getRelationType() == 1) {
                        if (list4.size() > 1) {
                            object3 = Result.error((String)"\u4e00\u5bf9\u4e00\u7684\u8868\u53ea\u80fd\u5bfc\u5165\u4e00\u6761\u6570\u636e!");
                            return object3;
                        }
                        object3 = this.onlCgformFieldService.queryCountBySql(c.f(onlCgformHead.getTableName()), null, null);
                        if (null != object3 && (Integer)object3 > 1) {
                            throw new JeecgBootException("\u4e00\u5bf9\u4e00\u7684\u8868\u53ea\u80fd\u5bfc\u5165\u4e00\u6761\u6570\u636e!");
                        }
                    }
                    object3 = "";
                    ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
                    for (Object object5 : list4) {
                        int n = 0;
                        Set set = object5.keySet();
                        serializable = new HashMap<String, Object>(5);
                        for (Object object6 : set) {
                            if (((String)object6).indexOf("$subTable$") != -1) continue;
                            if (((String)object6).indexOf("$mainTable$") != -1 && oConvertUtils.isNotEmpty((Object)object5.get(object6).toString())) {
                                n = 1;
                                object3 = this.a(onlCgformHead, (DataSource)object2, (String)object);
                            }
                            serializable.put(((String)object6).replace("$mainTable$", ""), object5.get(object6));
                        }
                        if ("Y".equals(onlCgformHead.getIsTree())) {
                            if (oConvertUtils.isEmpty(serializable.get(onlCgformHead.getTreeParentIdField()))) {
                                serializable.put(onlCgformHead.getTreeParentIdField(), "0");
                            }
                            if (oConvertUtils.isEmpty(serializable.get(onlCgformHead.getTreeIdField()))) {
                                serializable.put(onlCgformHead.getTreeIdField(), "0");
                            }
                        }
                        if (n != 0) {
                            serializable.put("id", object3);
                            arrayList.add((Map<String, Object>)((Object)serializable));
                            object3 = serializable.get("id");
                        }
                        if (stringArray != null) {
                            for (Object object6 : stringArray.keySet()) {
                                System.out.println((String)object6 + "=" + stringArray.getString((String)object6));
                                serializable.put(object6, stringArray.getString((String)object6));
                            }
                        }
                        object5.put("$mainTable$id", object3);
                    }
                    if (arrayList == null || arrayList.size() == 0) {
                        result.setSuccess(false);
                        result.setMessage("\u5bfc\u5165\u5931\u8d25\uff0c\u5339\u914d\u7684\u6570\u636e\u6761\u6570\u4e3a\u96f6!");
                        object4 = result;
                        return object4;
                    }
                    if ("1".equals(string3)) {
                        Object object5;
                        object4 = this.onlCgformSqlService.saveOnlineImportDataWithValidate(onlCgformHead, list2, arrayList);
                        object5 = (String)object4.get("error");
                        string2 = (String)object4.get("tip");
                        if (object5 != null && ((String)object5).length() > 0) {
                            stringBuffer.append(onlCgformHead.getTableTxt() + "\u5bfc\u5165\u6821\u9a8c," + string2 + ",\u8be6\u60c5\u5982\u4e0b:\r\n" + (String)object5);
                        }
                    } else {
                        this.onlCgformSqlService.saveBatchOnlineTable(onlCgformHead, list2, arrayList);
                    }
                    if (!oConvertUtils.isEmpty((Object)string4) || onlCgformHead.getTableType() != 2 || !oConvertUtils.isNotEmpty((Object)onlCgformHead.getSubTableStr())) continue;
                    for (String string7 : onlCgformHead.getSubTableStr().split(",")) {
                        Object object6;
                        serializable = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string7));
                        if (serializable == null) continue;
                        Iterator<Object> iterator = new LambdaQueryWrapper();
                        iterator.eq(OnlCgformField::getCgformHeadId, ((OnlCgformHead)serializable).getId());
                        object6 = this.onlCgformFieldService.list((Wrapper)iterator);
                        ArrayList<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();
                        String string8 = ((OnlCgformHead)serializable).getTableTxt();
                        for (Object object7 : list4) {
                            boolean bl = false;
                            HashMap<String, Object> hashMap = new HashMap<String, Object>();
                            Iterator iterator2 = object6.iterator();
                            while (iterator2.hasNext()) {
                                Object v;
                                OnlCgformField onlCgformField = (OnlCgformField)iterator2.next();
                                String string9 = onlCgformField.getMainTable();
                                String string10 = onlCgformField.getMainField();
                                boolean bl2 = onlCgformHead.getTableName().equals(string9) && oConvertUtils.isNotEmpty((Object)string10);
                                String string11 = string8 + "_" + onlCgformField.getDbFieldTxt();
                                if (bl2) {
                                    hashMap.put(onlCgformField.getDbFieldName(), object7.get("$mainTable$" + string10));
                                }
                                if (null == (v = object7.get("$subTable$" + string11)) || !oConvertUtils.isNotEmpty((Object)v.toString())) continue;
                                bl = true;
                                hashMap.put(onlCgformField.getDbFieldName(), v);
                            }
                            if (!bl) continue;
                            hashMap.put("id", this.a((OnlCgformHead)serializable, (DataSource)object2, (String)object));
                            arrayList2.add(hashMap);
                        }
                        if (arrayList2.size() <= 0) continue;
                        if ("1".equals(string3)) {
                            Object object7;
                            Map<String, String> map2 = this.onlCgformSqlService.saveOnlineImportDataWithValidate((OnlCgformHead)serializable, (List<OnlCgformField>)object6, arrayList2);
                            object7 = (String)map2.get("error");
                            String string12 = (String)map2.get("tip");
                            if (object7 == null || ((String)object7).length() <= 0) continue;
                            stringBuffer.append(((OnlCgformHead)serializable).getTableTxt() + "\u5bfc\u5165\u6821\u9a8c," + string12 + ",\u8be6\u60c5\u5982\u4e0b:\r\n" + (String)object7);
                            continue;
                        }
                        this.onlCgformSqlService.saveBatchOnlineTable((OnlCgformHead)serializable, (List<OnlCgformField>)object6, arrayList2);
                    }
                }
                catch (Exception exception) {
                    result.setSuccess(false);
                    result.setMessage("\u5bfc\u5165\u5931\u8d25\uff0c" + exception.getMessage());
                    object3 = result;
                    return object3;
                }
                finally {
                    if (inputStream == null) continue;
                    IOUtils.closeQuietly((InputStream)inputStream);
                }
            }
            result.setSuccess(true);
            if ("1".equals(string3) && stringBuffer.length() > 0) {
                list = c.a(this.upLoadPath, onlCgformHead.getTableTxt(), stringBuffer);
                result.setResult((Object)list);
                result.setMessage(string2);
                result.setCode(Integer.valueOf(201));
            } else {
                result.setMessage("\u5bfc\u5165\u6210\u529f!");
            }
        }
        catch (Exception exception) {
            result.setSuccess(false);
            result.setMessage(exception.getMessage());
            a.error(exception.getMessage(), (Throwable)exception);
        }
        a.info("=====online\u5bfc\u5165\u6570\u636e\u5b8c\u6210,\u8017\u65f6:" + (System.currentTimeMillis() - l2) + "\u6beb\u79d2=====");
        return result;
    }

    @PostMapping(value={"/doButton"})
    public Result<?> a(@RequestBody JSONObject jSONObject) {
        String string = jSONObject.getString("formId");
        String string2 = jSONObject.getString("dataId");
        String string3 = jSONObject.getString("buttonCode");
        JSONObject jSONObject2 = jSONObject.getJSONObject("uiFormData");
        try {
            this.onlCgformHeadService.executeCustomerButton(string3, string, string2);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)("\u6267\u884c\u5931\u8d25," + exception.getMessage()));
        }
        return Result.ok((String)"\u6267\u884c\u6210\u529f!");
    }

    public Object a(OnlCgformHead onlCgformHead, DataSource dataSource, String string) throws SQLException, org.jeecg.modules.online.config.exception.a {
        Object object = null;
        String string2 = onlCgformHead.getIdType();
        String string3 = onlCgformHead.getIdSequence();
        if (oConvertUtils.isNotEmpty((Object)string2) && "UUID".equalsIgnoreCase(string2)) {
            object = c.a();
        } else if (oConvertUtils.isNotEmpty((Object)string2) && "NATIVE".equalsIgnoreCase(string2)) {
            if (oConvertUtils.isNotEmpty((Object)string) && "oracle".equalsIgnoreCase(string)) {
                OracleSequenceMaxValueIncrementer oracleSequenceMaxValueIncrementer = new OracleSequenceMaxValueIncrementer(dataSource, "HIBERNATE_SEQUENCE");
                try {
                    object = oracleSequenceMaxValueIncrementer.nextLongValue();
                }
                catch (Exception exception) {
                    a.error(exception.getMessage(), (Throwable)exception);
                }
            } else if (oConvertUtils.isNotEmpty((Object)string) && "postgres".equalsIgnoreCase(string)) {
                PostgreSQLSequenceMaxValueIncrementer postgreSQLSequenceMaxValueIncrementer = new PostgreSQLSequenceMaxValueIncrementer(dataSource, "HIBERNATE_SEQUENCE");
                try {
                    object = postgreSQLSequenceMaxValueIncrementer.nextLongValue();
                }
                catch (Exception exception) {
                    a.error(exception.getMessage(), (Throwable)exception);
                }
            } else {
                object = null;
            }
        } else if (oConvertUtils.isNotEmpty((Object)string2) && "SEQUENCE".equalsIgnoreCase(string2)) {
            if (oConvertUtils.isNotEmpty((Object)string) && "oracle".equalsIgnoreCase(string)) {
                OracleSequenceMaxValueIncrementer oracleSequenceMaxValueIncrementer = new OracleSequenceMaxValueIncrementer(dataSource, string3);
                try {
                    object = oracleSequenceMaxValueIncrementer.nextLongValue();
                }
                catch (Exception exception) {
                    a.error(exception.getMessage(), (Throwable)exception);
                }
            } else if (oConvertUtils.isNotEmpty((Object)string) && "postgres".equalsIgnoreCase(string)) {
                PostgreSQLSequenceMaxValueIncrementer postgreSQLSequenceMaxValueIncrementer = new PostgreSQLSequenceMaxValueIncrementer(dataSource, string3);
                try {
                    object = postgreSQLSequenceMaxValueIncrementer.nextLongValue();
                }
                catch (Exception exception) {
                    a.error(exception.getMessage(), (Throwable)exception);
                }
            } else {
                object = null;
            }
        } else {
            object = c.a();
        }
        return object;
    }

    private void a(Map map, List<OnlCgformField> list) {
        for (OnlCgformField onlCgformField : list) {
            String string = onlCgformField.getDictTable();
            String string2 = onlCgformField.getDictField();
            String string3 = onlCgformField.getDictText();
            if (oConvertUtils.isEmpty((Object)string) && oConvertUtils.isEmpty((Object)string2) || "popup".equals(onlCgformField.getFieldShowType())) continue;
            String string4 = String.valueOf(map.get(onlCgformField.getDbFieldName()));
            List list2 = oConvertUtils.isEmpty((Object)string) ? this.sysBaseAPI.queryDictItemsByCode(string2) : this.sysBaseAPI.queryTableDictItemsByCode(string, string3, string2);
            for (DictModel dictModel : list2) {
                if (!string4.equals(dictModel.getText())) continue;
                map.put(onlCgformField.getDbFieldName(), dictModel.getValue());
            }
        }
    }

    @GetMapping(value={"/checkOnlyTable"})
    public Result<?> k(@RequestParam(value="tbname") String string, @RequestParam(value="id") String string2) {
        if (oConvertUtils.isEmpty((Object)string2)) {
            if (d.a(string).booleanValue()) {
                return Result.ok((Object)-1);
            }
            OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string));
            if (oConvertUtils.isNotEmpty((Object)onlCgformHead)) {
                return Result.ok((Object)-1);
            }
        } else {
            OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string2));
            if (!string.equals(onlCgformHead.getTableName()) && d.a(string).booleanValue()) {
                return Result.ok((Object)-1);
            }
        }
        return Result.ok((Object)1);
    }

    @RequiresPermissions(value={"online:form:generateCode"})
    @PostMapping(value={"/codeGenerate"})
    public Result<?> b(@RequestBody JSONObject jSONObject) {
        boolean bl;
        org.jeecg.modules.online.cgform.model.d d2 = (org.jeecg.modules.online.cgform.model.d)JSONObject.parseObject((String)jSONObject.toJSONString(), org.jeecg.modules.online.cgform.model.d.class);
        boolean bl2 = bl = this.jeecgBaseConfig.getFirewall() != null ? this.jeecgBaseConfig.getFirewall().getDataSourceSafe() : false;
        if (bl && !DbReadTableUtil.getProjectPath().equals(d2.getProjectPath())) {
            d2.setProjectPath(DbReadTableUtil.getProjectPath());
            a.warn("\u6570\u636e\u6e90\u5b89\u5168\u6a21\u5f0f\u4e0b\uff0c\u81ea\u5b9a\u4e49\u4ee3\u7801\u751f\u6210\u8def\u5f84\u65e0\u6548\uff0c\u4f7f\u7528\u5168\u5c40\u914d\u7f6e\u7684\u8def\u5f84 ::{}", (Object)DbReadTableUtil.getProjectPath());
        }
        List<String> list = null;
        try {
            list = "1".equals(d2.getJformType()) ? this.onlCgformHeadService.generateCode(d2) : this.onlCgformHeadService.generateOneToMany(d2);
            LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            String string = loginUser.getUsername() + d2.getTableName() + oConvertUtils.randomGen((int)16);
            Result result = Result.ok(list);
            String string2 = d2.getProjectPath().replaceAll("\\\\", "/");
            this.redisUtil.set(string, (Object)string2, 1800L);
            result.setMessage(string);
            return result;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return Result.error((String)exception.getMessage());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @GetMapping(value={"/codeView"})
    public void a(@RequestParam(name="path") String string, @RequestParam(name="pathKey") String string2, HttpServletResponse httpServletResponse) {
        String string3 = "";
        try {
            string3 = URLDecoder.decode(string, "UTF-8");
            if (string3.indexOf("src/main/java") == -1 && string3.indexOf("src%5Cmain%5Cjava") == -1 && string3.indexOf("src\\main\\java") == -1) {
                String string4 = "\u4ee3\u7801\u4e0d\u5728`src/main/java`\u76ee\u5f55\u4e2d\uff0c\u4e0d\u5141\u8bb8\u9884\u89c8";
                a.error(string4);
                JwtUtil.responseError((ServletResponse)httpServletResponse, (Integer)200, (String)string4);
                return;
            }
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            a.error(" path \u4e0d\u5408\u6cd5\uff01\uff01\uff01", (Object)unsupportedEncodingException.getMessage());
        }
        Object object = this.redisUtil.get(string2);
        if (object == null) {
            String string5 = "\u8def\u5f84\u5931\u6548\uff0c\u8bf7\u91cd\u65b0\u64cd\u4f5c!";
            a.error(string5);
            JwtUtil.responseError((ServletResponse)httpServletResponse, (Integer)500, (String)string5);
            return;
        }
        String string6 = object.toString();
        String string7 = string3.replaceAll("\\\\", "/");
        if (string7.indexOf(string6) < 0) {
            String string8 = "\u975e\u6cd5\u7684\u8bf7\u6c42\u8def\u5f84\uff0c\u8bf7\u91cd\u65b0\u64cd\u4f5c!";
            a.error(string8);
            JwtUtil.responseError((ServletResponse)httpServletResponse, (Integer)500, (String)string8);
            return;
        }
        String string9 = string3.substring(string3.lastIndexOf("/") + 1);
        File file = new File(string3);
        if (file.exists()) {
            httpServletResponse.setContentType("application/force-download");
            httpServletResponse.addHeader("Content-Disposition", "attachment;fileName=" + string9);
            byte[] byArray = new byte[1024];
            FileInputStream fileInputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                int n = bufferedInputStream.read(byArray);
                while (n != -1) {
                    servletOutputStream.write(byArray, 0, n);
                    n = bufferedInputStream.read(byArray);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            finally {
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Could not resolve type clashes
     * Loose catch block
     */
    @RequiresPermissions(value={"online:form:generateCode"})
    @PostMapping(value={"/downGenerateCode"})
    public void a(@RequestBody JSONObject jSONObject, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            String fileList = jSONObject.getString("fileList");
            String pathKey = jSONObject.getString("pathKey");
            try { fileList = URLDecoder.decode(fileList, "UTF-8"); } catch (UnsupportedEncodingException ignore) {}
            Object cache = this.redisUtil.get(pathKey);
            if (cache == null) {
                JwtUtil.responseError(httpServletResponse, 500, "路径失效，请重新操作!");
                return;
            }
            String fileName = "生成代码_" + System.currentTimeMillis() + ".zip";
            try { fileName = URLEncoder.encode(fileName, "UTF-8"); } catch (UnsupportedEncodingException ignore) {}
            httpServletResponse.setContentType("application/force-download");
            httpServletResponse.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            ServletOutputStream out = httpServletResponse.getOutputStream();
            out.flush();
        } catch (Exception e) {
            a.error("downGenerateCode error", e);
        }
    }

    @GetMapping(value={"/getTreeData/{code}"})
    @PermissionData
    public Result<Map<String, Object>> e(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        Result result = new Result();
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
        if (onlCgformHead == null) {
            result.error500("\u5b9e\u4f53\u4e0d\u5b58\u5728");
            return result;
        }
        try {
            String string2 = onlCgformHead.getTableName();
            String string3 = onlCgformHead.getTreeIdField();
            String string4 = onlCgformHead.getTreeParentIdField();
            ArrayList arrayList = Lists.newArrayList((Object[])new String[]{string3, string4});
            Map<String, Object> map = c.a(httpServletRequest);
            String string5 = null;
            if (map.get(string3) != null) {
                string5 = map.get(string3).toString();
            }
            if (map.get("hasQuery") != null && "false".equals(map.get("hasQuery")) && map.get(string4) == null) {
                map.put(string4, "0");
            } else {
                map.put("pageSize", -521);
                map.put(string4, map.get(string4));
            }
            map.put(string3, null);
            Map<String, Object> map2 = this.onlCgformFieldService.queryAutoTreeNoPage(string2, string, map, arrayList, string4);
            this.a(onlCgformHead, map2);
            result.setResult(map2);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            result.error500("\u6570\u636e\u5e93\u67e5\u8be2\u5931\u8d25" + exception.getMessage());
        }
        result.setOnlTable(onlCgformHead.getTableName());
        return result;
    }

    private void a(OnlCgformHead onlCgformHead, Map<String, Object> map) throws BusinessException {
        List list = (List)map.get("records");
        this.onlCgformHeadService.executeEnhanceList(onlCgformHead, "query", list);
    }

    @PostMapping(value={"/crazyForm/{name}"})
    public Result<?> b(@PathVariable(value="name") String string, @RequestBody JSONObject jSONObject) {
        Result result = new Result();
        try {
            String string2 = c.a();
            jSONObject.put("id", (Object)string2);
            this.onlCgformHeadService.addCrazyFormData(string, jSONObject);
            result.setResult((Object)string2);
            result.setMessage("\u4fdd\u5b58\u6210\u529f");
        }
        catch (Exception exception) {
            a.error("OnlCgformApiController.formAddForDesigner()\u53d1\u751f\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25");
        }
        return result;
    }

    @PutMapping(value={"/crazyForm/{name}"})
    public Result<?> c(@PathVariable(value="name") String string, @RequestBody JSONObject jSONObject) {
        try {
            jSONObject.remove((Object)"create_by");
            jSONObject.remove((Object)"create_time");
            jSONObject.remove((Object)"update_by");
            jSONObject.remove((Object)"update_time");
            this.onlCgformHeadService.editCrazyFormData(string, jSONObject);
        }
        catch (Exception exception) {
            a.error("OnlCgformApiController.formEditForDesigner()\u53d1\u751f\u5f02\u5e38\uff1a" + exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u4fdd\u5b58\u5931\u8d25");
        }
        return Result.ok((String)"\u4fdd\u5b58\u6210\u529f!");
    }

    @AutoLog(operateType=1, value="online\u5217\u8868\u52a0\u8f7d", module=ModuleType.ONLINE)
    @GetMapping(value={"/getErpColumns/{code}"})
    public Result<Map<String, Object>> d(@PathVariable(value="code") String string) {
        String string2;
        Result result = new Result();
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
        if (onlCgformHead == null) {
            result.error500("\u5b9e\u4f53\u4e0d\u5b58\u5728");
            return result;
        }
        HashMap<String, Serializable> hashMap = new HashMap<String, Serializable>(5);
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        org.jeecg.modules.online.cgform.model.b b2 = this.onlineService.queryOnlineConfig(onlCgformHead, loginUser.getUsername());
        hashMap.put("main", b2);
        if ("erp".equals(onlCgformHead.getThemeTemplate()) && onlCgformHead.getTableType() == 2 && oConvertUtils.isNotEmpty((Object)(string2 = onlCgformHead.getSubTableStr()))) {
            ArrayList<org.jeecg.modules.online.cgform.model.b> arrayList = new ArrayList<org.jeecg.modules.online.cgform.model.b>();
            for (String string3 : string2.split(",")) {
                OnlCgformHead onlCgformHead2 = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)new LambdaQueryWrapper().eq(OnlCgformHead::getTableName, (Object)string3));
                if (onlCgformHead2 == null) continue;
                arrayList.add(this.onlineService.queryOnlineConfig(onlCgformHead2, loginUser.getUsername()));
            }
            if (arrayList.size() > 0) {
                hashMap.put("subList", arrayList);
            }
        }
        result.setOnlTable(onlCgformHead.getTableName());
        result.setResult(hashMap);
        result.setSuccess(true);
        return result;
    }

    @AutoLog(operateType=1, value="online\u8868\u5355\u52a0\u8f7d", module=ModuleType.ONLINE)
    @GetMapping(value={"/getErpFormItem/{code}"})
    public Result<?> f(@PathVariable(value="code") String string, HttpServletRequest httpServletRequest) {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById((Serializable)((Object)string));
        if (onlCgformHead == null) {
            Result.error((String)"\u8868\u4e0d\u5b58\u5728");
        }
        Result result = new Result();
        LoginUser loginUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        JSONObject jSONObject = this.onlineService.queryOnlineFormObj(onlCgformHead, loginUser.getUsername());
        result.setResult((Object)c.b(jSONObject));
        result.setOnlTable(onlCgformHead.getTableName());
        return result;
    }

    @GetMapping(value={"/querySelectOptions"})
    public Result<List<TreeModel>> a(@ModelAttribute org.jeecg.modules.online.cgform.a.a a2) {
        Result result = new Result();
        try {
            List<TreeModel> list = this.onlCgformFieldService.queryDataListByLinkDown(a2);
            result.setResult(list);
            result.setSuccess(true);
        }
        catch (Exception exception) {
            a.warn("online\u7ea7\u8054\u4e0b\u62c9\u6570\u636e\u52a0\u8f7d\u5931\u8d25\uff1a{}", (Object)exception.getMessage());
            exception.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    @GetMapping(value={"/data/{tableName}/queryById"})
    public JSONObject a(@PathVariable(value="tableName") String string, @RequestParam(name="mock", required=false) Boolean bl, HttpServletRequest httpServletRequest) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformHead::getTableName, (Object)string);
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)lambdaQueryWrapper);
        if (onlCgformHead == null) {
            throw new JeecgBootException("Online\u8868\u5355 " + string + " \u4e0d\u5b58\u5728");
        }
        try {
            Object object;
            Map<String, Object> map = c.a(httpServletRequest);
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add("id");
            Map<String, Object> map2 = this.onlCgformFieldService.queryAutolistPage(onlCgformHead, map, arrayList);
            this.a(onlCgformHead, map2);
            List<Object> list = c.a(map2.get("records"), Object.class);
            if (Boolean.TRUE.equals(bl) && (list == null || list.size() == 0)) {
                object = this.onlCgformFieldService.generateMockData(onlCgformHead.getTableName());
                list = new ArrayList<Object>();
                list.add(object);
                map2.put("records", list);
            }
            object = new JSONObject();
            object.put("data", map2.get("records"));
            return object;
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            throw new JeecgBootException("\u6570\u636e\u5e93\u67e5\u8be2\u5931\u8d25\uff0c" + exception.getMessage());
        }
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getCgformHeadId": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformField::getCgformHeadId;
                }
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformField") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformField::getCgformHeadId;
            }
            case "getTableName": {
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
                if (serializedLambda.getImplMethodKind() == 5 && serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") && serializedLambda.getFunctionalInterfaceMethodName().equals("apply") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") && serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) {
                    return OnlCgformHead::getTableName;
                }
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

