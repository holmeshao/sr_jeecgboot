/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.io.FileUtil
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.commons.io.FilenameUtils
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.aspect.annotation.AutoLog
 *  org.jeecg.common.exception.JeecgBootException
 *  org.jeecg.common.system.query.QueryGenerator
 *  org.jeecg.common.util.FileDownloadUtils
 *  org.jeecg.common.util.SqlInjectionUtil
 *  org.jeecg.common.util.UUIDGenerator
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
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

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.File;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.FileDownloadUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.config.exception.a;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="onlCgformFieldController")
@RequestMapping(value={"/online/cgform/field"})
public class OnlCgformFieldController {
    private static final Logger a = LoggerFactory.getLogger(c.class);
    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;
    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;
    @Value(value="${jeecg.path.upload}")
    private String uploadpath;

    @GetMapping(value={"/listByHeadCode"})
    public Result<?> a(@RequestParam(value="headCode") String string) {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformHead::getTableName, (Object)string);
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)lambdaQueryWrapper);
        if (onlCgformHead == null) {
            return Result.error((String)("\u8868\u540d[" + string + "]\u4e0d\u5b58\u5728\uff01"));
        }
        return this.b(onlCgformHead.getId());
    }

    @GetMapping(value={"/listByHeadId"})
    public Result<?> b(@RequestParam(value="headId") String string) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq((Object)"cgform_head_id", (Object)string);
        queryWrapper.orderByAsc((Object)"order_num");
        List list = this.onlCgformFieldService.list((Wrapper)queryWrapper);
        return Result.ok((Object)list);
    }

    @GetMapping(value={"/list"})
    public Result<IPage<OnlCgformField>> a(OnlCgformField onlCgformField, @RequestParam(name="pageNo", defaultValue="1") Integer n, @RequestParam(name="pageSize", defaultValue="10") Integer n2, HttpServletRequest httpServletRequest) {
        Result result = new Result();
        QueryWrapper queryWrapper = QueryGenerator.initQueryWrapper((Object)onlCgformField, (Map)httpServletRequest.getParameterMap());
        Page page = new Page((long)n.intValue(), (long)n2.intValue());
        IPage iPage = this.onlCgformFieldService.page((IPage)page, (Wrapper)queryWrapper);
        result.setSuccess(true);
        result.setResult((Object)iPage);
        return result;
    }

    @PostMapping(value={"/add"})
    public Result<OnlCgformField> a(@RequestBody OnlCgformField onlCgformField) {
        Result result = new Result();
        try {
            this.onlCgformFieldService.save(onlCgformField);
            result.success("\u6dfb\u52a0\u6210\u529f\uff01");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            result.error500("\u64cd\u4f5c\u5931\u8d25");
        }
        return result;
    }

    @PutMapping(value={"/edit"})
    public Result<OnlCgformField> b(@RequestBody OnlCgformField onlCgformField) {
        Result result = new Result();
        OnlCgformField onlCgformField2 = (OnlCgformField)this.onlCgformFieldService.getById((Serializable)((Object)onlCgformField.getId()));
        if (onlCgformField2 == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            boolean bl = this.onlCgformFieldService.updateById(onlCgformField);
            if (bl) {
                result.success("\u4fee\u6539\u6210\u529f!");
            }
        }
        return result;
    }

    @DeleteMapping(value={"/delete"})
    public Result<OnlCgformField> c(@RequestParam(name="id", required=true) String string) {
        Result result = new Result();
        OnlCgformField onlCgformField = (OnlCgformField)this.onlCgformFieldService.getById((Serializable)((Object)string));
        if (onlCgformField == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            boolean bl = this.onlCgformFieldService.removeById((Serializable)((Object)string));
            if (bl) {
                result.success("\u5220\u9664\u6210\u529f!");
            }
        }
        return result;
    }

    @DeleteMapping(value={"/deleteBatch"})
    public Result<OnlCgformField> d(@RequestParam(name="ids", required=true) String string) {
        Result result = new Result();
        if (string == null || "".equals(string.trim())) {
            result.error500("\u53c2\u6570\u4e0d\u8bc6\u522b\uff01");
        } else {
            this.onlCgformFieldService.removeByIds(Arrays.asList(string.split(",")));
            result.success("\u5220\u9664\u6210\u529f!");
        }
        return result;
    }

    @GetMapping(value={"/queryById"})
    public Result<OnlCgformField> e(@RequestParam(name="id", required=true) String string) {
        Result result = new Result();
        OnlCgformField onlCgformField = (OnlCgformField)this.onlCgformFieldService.getById((Serializable)((Object)string));
        if (onlCgformField == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            result.setResult((Object)onlCgformField);
            result.setSuccess(true);
        }
        return result;
    }

    @AutoLog(operateType=1, value="online\u8868\u5355\u6279\u91cf\u5355\u5b57\u6bb5\u4e0b\u8f7d\u6587\u4ef6")
    @GetMapping(value={"/download/{code}/{id}/{field}"})
    public void a(@PathVariable(value="code") String string, @PathVariable(value="id") String string3, @PathVariable(value="field") String string4, HttpServletResponse httpServletResponse) {
        try {
            a.info("[\u6279\u91cf\u4e0b\u8f7d\u6587\u4ef6]\u5f00\u59cb\u6279\u91cf\u4e0b\u8f7d\u6587\u4ef6:code=" + string + ",id=" + string3 + ",field=" + string4);
            SqlInjectionUtil.filterContent((String)string3, (String)"'");
            OnlCgformHead onlCgformHead = this.onlCgformHeadService.getTable(string);
            List<OnlCgformField> list = this.onlCgformFieldService.queryFormFields(onlCgformHead.getId(), true);
            if (list == null || list.isEmpty()) {
                throw new a("\u627e\u4e0d\u5230\u5b57\u6bb5\uff0c\u8bf7\u786e\u8ba4\u914d\u7f6e\u662f\u5426\u6b63\u786e!");
            }
            List<OnlCgformField> list2 = list.stream().filter(onlCgformField -> onlCgformField.getDbFieldName().equals(string4)).collect(Collectors.toList());
            if (list2.isEmpty()) {
                throw new a("\u627e\u4e0d\u5230\u5b57\u6bb5!");
            }
            String string5 = org.jeecg.modules.online.cgform.d.c.f(onlCgformHead.getTableName());
            Map<String, Object> map = this.onlCgformFieldService.queryFormData(list2, string5, string3);
            String string6 = org.jeecg.modules.online.cgform.d.c.a(map, string4);
            if (null == string6 || string6.isEmpty()) {
                return;
            }
            List<String> list3 = new ArrayList<String>();
            if (string6.contains(",")) {
                list3.addAll(Arrays.asList(string6.split(",")));
            } else {
                list3.add(string6);
            }
            String string7 = this.uploadpath + File.separator + "tmp" + File.separator + UUIDGenerator.generate() + File.separator;
            list3 = list3.stream().map(string2 -> {
                Pattern pattern = Pattern.compile("^(http|https)://.*");
                Matcher matcher = pattern.matcher((CharSequence)string2);
                if (matcher.matches()) {
                    String string3 = string2;
                    if (string3.contains("?")) {
                        string3 = string3.substring(0, string3.indexOf("?"));
                    }
                    string3 = FilenameUtils.getName((String)string3);
                    return FileDownloadUtils.download2DiskFromNet((String)string2, (String)(string7 + string3));
                }
                return this.uploadpath + File.separator + string2;
            }).collect(Collectors.toList());
            String string8 = list2.get(0).getDbFieldTxt() + "_" + string3;
            FileDownloadUtils.downloadFileMulti((HttpServletResponse)httpServletResponse, list3, (String)string8);
            new Thread(() -> {
                try {
                    Thread.sleep(10000L);
                    FileUtil.del((String)string7);
                }
                catch (InterruptedException interruptedException) {
                    a.error(interruptedException.getMessage(), (Throwable)interruptedException);
                }
            }).start();
        }
        catch (Exception exception) {
            a.error("online\u8868\u5355\u6279\u91cf\u5355\u5b57\u6bb5\u4e0b\u8f7d\u6587\u4ef6\uff1a" + exception.getMessage(), (Throwable)exception);
            throw new JeecgBootException((Throwable)exception);
        }
    }

    private static /* synthetic */ Object a(SerializedLambda serializedLambda) {
        switch (serializedLambda.getImplMethodName()) {
            case "getTableName": {
                if (serializedLambda.getImplMethodKind() != 5 || !serializedLambda.getFunctionalInterfaceClass().equals("com/baomidou/mybatisplus/core/toolkit/support/SFunction") || !serializedLambda.getFunctionalInterfaceMethodName().equals("apply") || !serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") || !serializedLambda.getImplClass().equals("org/jeecg/modules/online/cgform/entity/OnlCgformHead") || !serializedLambda.getImplMethodSignature().equals("()Ljava/lang/String;")) break;
                return OnlCgformHead::getTableName;
            }
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }
}

