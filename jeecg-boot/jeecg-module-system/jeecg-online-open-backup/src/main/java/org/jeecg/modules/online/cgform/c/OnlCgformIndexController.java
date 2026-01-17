/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  javax.servlet.http.HttpServletRequest
 *  org.jeecg.common.api.vo.Result
 *  org.jeecg.common.constant.CommonConstant
 *  org.jeecg.common.system.query.QueryGenerator
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package org.jeecg.modules.online.cgform.c;

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
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.online.cgform.entity.OnlCgformIndex;
import org.jeecg.modules.online.cgform.service.IOnlCgformIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="onlCgformIndexController")
@RequestMapping(value={"/online/cgform/index"})
public class OnlCgformIndexController {
    private static final Logger a = LoggerFactory.getLogger(e.class);
    @Autowired
    private IOnlCgformIndexService onlCgformIndexService;

    @GetMapping(value={"/listByHeadId"})
    public Result<?> a(@RequestParam(value="headId") String string) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq((Object)"cgform_head_id", (Object)string);
        queryWrapper.eq((Object)"del_flag", (Object)CommonConstant.DEL_FLAG_0);
        queryWrapper.orderByDesc((Object)"create_time");
        List list = this.onlCgformIndexService.list((Wrapper)queryWrapper);
        return Result.ok((Object)list);
    }

    @GetMapping(value={"/list"})
    public Result<IPage<OnlCgformIndex>> a(OnlCgformIndex onlCgformIndex, @RequestParam(name="pageNo", defaultValue="1") Integer n, @RequestParam(name="pageSize", defaultValue="10") Integer n2, HttpServletRequest httpServletRequest) {
        Result result = new Result();
        QueryWrapper queryWrapper = QueryGenerator.initQueryWrapper((Object)onlCgformIndex, (Map)httpServletRequest.getParameterMap());
        Page page = new Page((long)n.intValue(), (long)n2.intValue());
        IPage iPage = this.onlCgformIndexService.page((IPage)page, (Wrapper)queryWrapper);
        result.setSuccess(true);
        result.setResult((Object)iPage);
        return result;
    }

    @PostMapping(value={"/add"})
    public Result<OnlCgformIndex> a(@RequestBody OnlCgformIndex onlCgformIndex) {
        Result result = new Result();
        try {
            this.onlCgformIndexService.save(onlCgformIndex);
            result.success("\u6dfb\u52a0\u6210\u529f\uff01");
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
            result.error500("\u64cd\u4f5c\u5931\u8d25");
        }
        return result;
    }

    @PutMapping(value={"/edit"})
    public Result<OnlCgformIndex> b(@RequestBody OnlCgformIndex onlCgformIndex) {
        Result result = new Result();
        OnlCgformIndex onlCgformIndex2 = (OnlCgformIndex)this.onlCgformIndexService.getById((Serializable)((Object)onlCgformIndex.getId()));
        if (onlCgformIndex2 == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            boolean bl = this.onlCgformIndexService.updateById(onlCgformIndex);
            if (bl) {
                result.success("\u4fee\u6539\u6210\u529f!");
            }
        }
        return result;
    }

    @DeleteMapping(value={"/delete"})
    public Result<OnlCgformIndex> b(@RequestParam(name="id", required=true) String string) {
        Result result = new Result();
        OnlCgformIndex onlCgformIndex = (OnlCgformIndex)this.onlCgformIndexService.getById((Serializable)((Object)string));
        if (onlCgformIndex == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            boolean bl = this.onlCgformIndexService.removeById((Serializable)((Object)string));
            if (bl) {
                result.success("\u5220\u9664\u6210\u529f!");
            }
        }
        return result;
    }

    @DeleteMapping(value={"/deleteBatch"})
    public Result<OnlCgformIndex> c(@RequestParam(name="ids", required=true) String string) {
        Result result = new Result();
        if (string == null || "".equals(string.trim())) {
            result.error500("\u53c2\u6570\u4e0d\u8bc6\u522b\uff01");
        } else {
            this.onlCgformIndexService.removeByIds(Arrays.asList(string.split(",")));
            result.success("\u5220\u9664\u6210\u529f!");
        }
        return result;
    }

    @GetMapping(value={"/queryById"})
    public Result<OnlCgformIndex> d(@RequestParam(name="id", required=true) String string) {
        Result result = new Result();
        OnlCgformIndex onlCgformIndex = (OnlCgformIndex)this.onlCgformIndexService.getById((Serializable)((Object)string));
        if (onlCgformIndex == null) {
            result.error500("\u672a\u627e\u5230\u5bf9\u5e94\u5b9e\u4f53");
        } else {
            result.setResult((Object)onlCgformIndex);
            result.setSuccess(true);
        }
        return result;
    }
}

