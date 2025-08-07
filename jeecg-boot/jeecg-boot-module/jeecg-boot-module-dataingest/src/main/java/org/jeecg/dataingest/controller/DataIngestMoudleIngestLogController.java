package org.jeecg.dataingest.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestLog;
import org.jeecg.dataingest.service.IDataIngestMoudleIngestLogService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: data_ingest_moudle_ingest_log
 * @Author: jeecg-boot
 * @Date:   2025-07-22
 * @Version: V1.0
 */
@Tag(name="data_ingest_moudle_ingest_log")
@RestController
@RequestMapping("/org/jeecg/dataingest/dataIngestMoudleIngestLog")
@Slf4j
public class DataIngestMoudleIngestLogController extends JeecgController<DataIngestMoudleIngestLog, IDataIngestMoudleIngestLogService> {
	@Autowired
	private IDataIngestMoudleIngestLogService dataIngestMoudleIngestLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dataIngestMoudleIngestLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "data_ingest_moudle_ingest_log-分页列表查询")
	@Operation(summary="data_ingest_moudle_ingest_log-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DataIngestMoudleIngestLog>> queryPageList(DataIngestMoudleIngestLog dataIngestMoudleIngestLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<DataIngestMoudleIngestLog> queryWrapper = QueryGenerator.initQueryWrapper(dataIngestMoudleIngestLog, req.getParameterMap());
		Page<DataIngestMoudleIngestLog> page = new Page<DataIngestMoudleIngestLog>(pageNo, pageSize);
		IPage<DataIngestMoudleIngestLog> pageList = dataIngestMoudleIngestLogService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param dataIngestMoudleIngestLog
	 * @return
	 */
	@AutoLog(value = "data_ingest_moudle_ingest_log-添加")
	@Operation(summary="data_ingest_moudle_ingest_log-添加")
	@RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_log:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DataIngestMoudleIngestLog dataIngestMoudleIngestLog) {
		dataIngestMoudleIngestLogService.save(dataIngestMoudleIngestLog);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param dataIngestMoudleIngestLog
	 * @return
	 */
	@AutoLog(value = "data_ingest_moudle_ingest_log-编辑")
	@Operation(summary="data_ingest_moudle_ingest_log-编辑")
	@RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_log:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DataIngestMoudleIngestLog dataIngestMoudleIngestLog) {
		dataIngestMoudleIngestLogService.updateById(dataIngestMoudleIngestLog);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "data_ingest_moudle_ingest_log-通过id删除")
	@Operation(summary="data_ingest_moudle_ingest_log-通过id删除")
	@RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_log:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		dataIngestMoudleIngestLogService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "data_ingest_moudle_ingest_log-批量删除")
	@Operation(summary="data_ingest_moudle_ingest_log-批量删除")
	@RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_log:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dataIngestMoudleIngestLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "data_ingest_moudle_ingest_log-通过id查询")
	@Operation(summary="data_ingest_moudle_ingest_log-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DataIngestMoudleIngestLog> queryById(@RequestParam(name="id",required=true) String id) {
		DataIngestMoudleIngestLog dataIngestMoudleIngestLog = dataIngestMoudleIngestLogService.getById(id);
		if(dataIngestMoudleIngestLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(dataIngestMoudleIngestLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dataIngestMoudleIngestLog
    */
    @RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_log:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DataIngestMoudleIngestLog dataIngestMoudleIngestLog) {
        return super.exportXls(request, dataIngestMoudleIngestLog, DataIngestMoudleIngestLog.class, "data_ingest_moudle_ingest_log");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_log:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DataIngestMoudleIngestLog.class);
    }

}
