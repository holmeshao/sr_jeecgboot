/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.commons.lang.StringUtils
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.exception.JeecgSqlInjectionException
 *  org.jeecg.common.system.api.ISysBaseAPI
 *  org.jeecg.common.system.query.QueryGenerator
 *  org.jeecg.common.util.SqlInjectionUtil
 *  org.jeecg.common.util.oConvertUtils
 *  org.jeecg.modules.base.service.BaseCommonService
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.cache.annotation.CacheEvict
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package org.jeecg.modules.online.cgreport.a;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.exception.JeecgSqlInjectionException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportParam;
import org.jeecg.modules.online.cgreport.model.OnlCgreportModel;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportHeadService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportItemService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportParamService;
import org.jeecg.modules.online.config.b.a;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="onlCgreportHeadController")
@RequestMapping(value={"/online/cgreport/head"})
public class OnlCgreportHeadController {
    private static final Logger a = LoggerFactory.getLogger(b.class);
    @Lazy
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private IOnlCgreportHeadService onlCgreportHeadService;
    @Autowired
    private IOnlCgreportParamService onlCgreportParamService;
    @Autowired
    private IOnlCgreportItemService onlCgreportItemService;
    @Autowired
    private BaseCommonService baseCommonService;
    @Autowired
    private a onlReportQueryBlackListHandler;

    @GetMapping(value={"/parseSql"})
    @RequiresPermissions(value={"online:report:parseSql"})
    public Result<?> a(@RequestParam(name="sql") String string, @RequestParam(name="dbKey", required=false) String string2) {
        Object object;
        if (StringUtils.isNotBlank((String)string2) && (object = this.sysBaseAPI.getDynamicDbSourceByCode(string2)) == null) {
            return Result.error((String)"\u6570\u636e\u6e90\u4e0d\u5b58\u5728");
        }
        object = new HashMap(5);
        ArrayList<Serializable> arrayList = new ArrayList<Serializable>();
        ArrayList<Serializable> arrayList2 = new ArrayList<Serializable>();
        List<String> list = null;
        List<String> list2 = null;
        try {
            Serializable serializable;
            this.baseCommonService.addLog("Online\u62a5\u8868\uff0csql\u89e3\u6790\uff1a" + string, Integer.valueOf(2), Integer.valueOf(2));
            if (!this.onlReportQueryBlackListHandler.isPass(string)) {
                return Result.error((String)this.onlReportQueryBlackListHandler.getError());
            }
            SqlInjectionUtil.specialFilterContentForOnlineReport((String)string);
            list = this.onlCgreportHeadService.getSqlFields(string, string2);
            list2 = this.onlCgreportHeadService.getSqlParams(string);
            int n = 1;
            for (String string3 : list) {
                serializable = new OnlCgreportItem();
                ((OnlCgreportItem)serializable).setFieldName(string3.toLowerCase());
                ((OnlCgreportItem)serializable).setFieldTxt(string3);
                ((OnlCgreportItem)serializable).setIsShow(1);
                ((OnlCgreportItem)serializable).setOrderNum(n);
                ((OnlCgreportItem)serializable).setId(c.a());
                ((OnlCgreportItem)serializable).setFieldType("String");
                arrayList.add(serializable);
                ++n;
            }
            for (String string3 : list2) {
                serializable = new OnlCgreportParam();
                ((OnlCgreportParam)serializable).setParamName(string3);
                ((OnlCgreportParam)serializable).setParamTxt(string3);
                arrayList2.add(serializable);
            }
            object.put("fields", arrayList);
            object.put("params", arrayList2);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            String string4 = "\u89e3\u6790\u5931\u8d25\uff0c";
            int n = exception.getMessage().indexOf("Connection refused: connect");
            string4 = n != -1 || exception.getMessage().contains("Failed to obtain JDBC Connection") ? string4 + "\u6570\u636e\u6e90\u8fde\u63a5\u5931\u8d25." : (exception.getMessage().indexOf("\u503c\u53ef\u80fd\u5b58\u5728SQL\u6ce8\u5165\u98ce\u9669") != -1 ? string4 + "SQL\u53ef\u80fd\u5b58\u5728SQL\u6ce8\u5165\u98ce\u9669." : (exception.getMessage().indexOf("\u8be5\u62a5\u8868sql\u6ca1\u6709\u6570\u636e") != -1 ? string4 + "\u62a5\u8868sql\u67e5\u8be2\u6570\u636e\u4e3a\u7a7a\uff0c\u65e0\u6cd5\u89e3\u6790\u5b57\u6bb5." : (exception.getMessage().indexOf("SqlServer\u4e0d\u652f\u6301SQL\u5185\u6392\u5e8f") != -1 ? string4 + "SqlServer\u4e0d\u652f\u6301SQL\u5185\u6392\u5e8f." : (exception.getMessage().contains("Unknown column") ? string4 + "\u672a\u77e5\u7684\u5b57\u6bb5\u540d." : (exception instanceof JeecgSqlInjectionException ? string4 + exception.getMessage() : (exception instanceof JeecgBootException ? string4 + exception.getMessage() : string4 + "SQL\u8bed\u6cd5\u9519\u8bef."))))));
            return Result.error((String)string4);
        }
        return Result.ok((Object)object);
    }

    @GetMapping(value={"/list"})
    public Result<IPage<OnlCgreportHead>> a(OnlCgreportHead onlCgreportHead, @RequestParam(name="pageNo", defaultValue="1") Integer n, @RequestParam(name="pageSize", defaultValue="10") Integer n2, @RequestParam(name="keywords", required=false) String string, HttpServletRequest httpServletRequest) {
        Result result = new Result();
        QueryWrapper queryWrapper2 = QueryGenerator.initQueryWrapper((Object)onlCgreportHead, (Map)httpServletRequest.getParameterMap());
        if (oConvertUtils.isNotEmpty((Object)string)) {
            queryWrapper2.and(queryWrapper -> {
                QueryWrapper cfr_ignored_0 = (QueryWrapper)((QueryWrapper)((QueryWrapper)queryWrapper.like((Object)"code", (Object)string)).or()).like((Object)"name", (Object)string);
            });
        }
        Page page = new Page((long)n.intValue(), (long)n2.intValue());
        IPage iPage = this.onlCgreportHeadService.page((IPage)page, (Wrapper)queryWrapper2);
        result.setSuccess(true);
        result.setResult((Object)iPage);
        return result;
    }

    @RequiresPermissions(value={"online:report:add"})
    @PostMapping(value={"/add"})
    public Result<?> a(@RequestBody OnlCgreportModel onlCgreportModel) {
        Result result = new Result();
        try {
            String string = c.a();
            OnlCgreportHead onlCgreportHead = onlCgreportModel.getHead();
            List<OnlCgreportParam> list = onlCgreportModel.getParams();
            List<OnlCgreportItem> list2 = onlCgreportModel.getItems();
            onlCgreportHead.setId(string);
            for (OnlCgreportParam serializable : list) {
                serializable.setId(null);
                serializable.setCgrheadId(string);
            }
            for (OnlCgreportItem onlCgreportItem : list2) {
                onlCgreportItem.setId(null);
                onlCgreportItem.setFieldName(onlCgreportItem.getFieldName().trim().toLowerCase());
                onlCgreportItem.setCgrheadId(string);
            }
            this.onlCgreportHeadService.save(onlCgreportHead);
            this.onlCgreportParamService.saveBatch(list);
            this.onlCgreportItemService.saveBatch(list2);
            result.success("\u6dfb\u52a0\u6210\u529f\uff01");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            result.error500("\u64cd\u4f5c\u5931\u8d25");
        }
        return result;
    }

    @PutMapping(value={"/editAll"})
    @RequiresPermissions(value={"online:report:edit"})
    @CacheEvict(value={"sys:cache:online:rp"}, allEntries=true, beforeInvocation=true)
    public Result<?> b(@RequestBody OnlCgreportModel onlCgreportModel) {
        try {
            return this.onlCgreportHeadService.editAll(onlCgreportModel);
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            return Result.error((String)"\u64cd\u4f5c\u5931\u8d25");
        }
    }

    @RequiresPermissions(value={"online:report:delete"})
    @DeleteMapping(value={"/delete"})
    public Result<?> a(@RequestParam(name="id", required=true) String string) {
        return this.onlCgreportHeadService.delete(string);
    }

    @RequiresPermissions(value={"online:report:deleteBatch"})
    @DeleteMapping(value={"/deleteBatch"})
    public Result<?> b(@RequestParam(name="ids", required=true) String string) {
        return this.onlCgreportHeadService.bathDelete(string.split(","));
    }

    @GetMapping(value={"/queryById"})
    public Result<OnlCgreportHead> c(@RequestParam(name="id", required=true) String string) {
        Result result = new Result();
        OnlCgreportHead onlCgreportHead = (OnlCgreportHead)this.onlCgreportHeadService.getById((Serializable)((Object)string));
        result.setResult((Object)onlCgreportHead);
        return result;
    }
}

