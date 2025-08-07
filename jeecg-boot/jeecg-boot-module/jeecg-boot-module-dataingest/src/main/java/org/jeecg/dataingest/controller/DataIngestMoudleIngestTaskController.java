package org.jeecg.dataingest.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.dataingest.entity.DataIngestMoudleDataSourceConfig;
import org.jeecg.dataingest.entity.DataIngestMoudleFieldMapping;
import org.jeecg.dataingest.entity.DataIngestMoudleDataCdcTable;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;
import org.jeecg.dataingest.vo.DataIngestMoudleIngestTaskPage;
import org.jeecg.dataingest.service.IDataIngestMoudleIngestTaskService;
import org.jeecg.dataingest.service.IDataIngestMoudleDataSourceConfigService;
import org.jeecg.dataingest.service.IDataIngestMoudleFieldMappingService;
import org.jeecg.dataingest.service.IDataIngestMoudleDataCdcTableService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;


 /**
 * @Description: 数据接入模块的任务主信息表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Tag(name="数据接入模块的任务主信息表")
@RestController
@RequestMapping("/org/jeecg/dataingest/dataIngestMoudleIngestTask")
@Slf4j
public class DataIngestMoudleIngestTaskController {
	@Autowired
	private IDataIngestMoudleIngestTaskService dataIngestMoudleIngestTaskService;
	@Autowired
	private IDataIngestMoudleDataSourceConfigService dataIngestMoudleDataSourceConfigService;
	@Autowired
	private IDataIngestMoudleFieldMappingService dataIngestMoudleFieldMappingService;
	@Autowired
	private IDataIngestMoudleDataCdcTableService dataIngestMoudleDataCdcTableService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dataIngestMoudleIngestTask
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "数据接入模块的任务主信息表-分页列表查询")
	@Operation(summary="数据接入模块的任务主信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DataIngestMoudleIngestTask>> queryPageList(DataIngestMoudleIngestTask dataIngestMoudleIngestTask,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<DataIngestMoudleIngestTask> queryWrapper = QueryGenerator.initQueryWrapper(dataIngestMoudleIngestTask, req.getParameterMap());
		Page<DataIngestMoudleIngestTask> page = new Page<DataIngestMoudleIngestTask>(pageNo, pageSize);
		IPage<DataIngestMoudleIngestTask> pageList = dataIngestMoudleIngestTaskService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param dataIngestMoudleIngestTaskPage
	 * @return
	 */
	@AutoLog(value = "数据接入模块的任务主信息表-添加")
	@Operation(summary="数据接入模块的任务主信息表-添加")
    @RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_task:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DataIngestMoudleIngestTaskPage dataIngestMoudleIngestTaskPage) {
		DataIngestMoudleIngestTask dataIngestMoudleIngestTask = new DataIngestMoudleIngestTask();
		BeanUtils.copyProperties(dataIngestMoudleIngestTaskPage, dataIngestMoudleIngestTask);
		dataIngestMoudleIngestTaskService.saveMain(dataIngestMoudleIngestTask, dataIngestMoudleIngestTaskPage.getDataIngestMoudleDataSourceConfigList(),dataIngestMoudleIngestTaskPage.getDataIngestMoudleFieldMappingList(),dataIngestMoudleIngestTaskPage.getDataIngestMoudleDataCdcTableList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param dataIngestMoudleIngestTaskPage
	 * @return
	 */
	@AutoLog(value = "数据接入模块的任务主信息表-编辑")
	@Operation(summary="数据接入模块的任务主信息表-编辑")
    @RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_task:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DataIngestMoudleIngestTaskPage dataIngestMoudleIngestTaskPage) {
		DataIngestMoudleIngestTask dataIngestMoudleIngestTask = new DataIngestMoudleIngestTask();
		BeanUtils.copyProperties(dataIngestMoudleIngestTaskPage, dataIngestMoudleIngestTask);
		DataIngestMoudleIngestTask dataIngestMoudleIngestTaskEntity = dataIngestMoudleIngestTaskService.getById(dataIngestMoudleIngestTask.getId());
		if(dataIngestMoudleIngestTaskEntity==null) {
			return Result.error("未找到对应数据");
		}
		dataIngestMoudleIngestTaskService.updateMain(dataIngestMoudleIngestTask, dataIngestMoudleIngestTaskPage.getDataIngestMoudleDataSourceConfigList(),dataIngestMoudleIngestTaskPage.getDataIngestMoudleFieldMappingList(),dataIngestMoudleIngestTaskPage.getDataIngestMoudleDataCdcTableList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据接入模块的任务主信息表-通过id删除")
	@Operation(summary="数据接入模块的任务主信息表-通过id删除")
    @RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_task:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		dataIngestMoudleIngestTaskService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "数据接入模块的任务主信息表-批量删除")
	@Operation(summary="数据接入模块的任务主信息表-批量删除")
    @RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_task:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dataIngestMoudleIngestTaskService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "数据接入模块的任务主信息表-通过id查询")
	@Operation(summary="数据接入模块的任务主信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DataIngestMoudleIngestTask> queryById(@RequestParam(name="id",required=true) String id) {
		DataIngestMoudleIngestTask dataIngestMoudleIngestTask = dataIngestMoudleIngestTaskService.getById(id);
		if(dataIngestMoudleIngestTask==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(dataIngestMoudleIngestTask);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "数据接入模块的数据源配置通过主表ID查询")
	@Operation(summary="数据接入模块的数据源配置主表ID查询")
	@GetMapping(value = "/queryDataIngestMoudleDataSourceConfigByMainId")
	public Result<List<DataIngestMoudleDataSourceConfig>> queryDataIngestMoudleDataSourceConfigListByMainId(@RequestParam(name="id",required=true) String id) {
		List<DataIngestMoudleDataSourceConfig> dataIngestMoudleDataSourceConfigList = dataIngestMoudleDataSourceConfigService.selectByMainId(id);
		return Result.OK(dataIngestMoudleDataSourceConfigList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "数据接入模块的字段映射表通过主表ID查询")
	@Operation(summary="数据接入模块的字段映射表主表ID查询")
	@GetMapping(value = "/queryDataIngestMoudleFieldMappingByMainId")
	public Result<List<DataIngestMoudleFieldMapping>> queryDataIngestMoudleFieldMappingListByMainId(@RequestParam(name="id",required=true) String id) {
		List<DataIngestMoudleFieldMapping> dataIngestMoudleFieldMappingList = dataIngestMoudleFieldMappingService.selectByMainId(id);
		return Result.OK(dataIngestMoudleFieldMappingList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "数据接入模块的cdc对接表通过主表ID查询")
	@Operation(summary="数据接入模块的cdc对接表主表ID查询")
	@GetMapping(value = "/queryDataIngestMoudleDataCdcTableByMainId")
	public Result<List<DataIngestMoudleDataCdcTable>> queryDataIngestMoudleDataCdcTableListByMainId(@RequestParam(name="id",required=true) String id) {
		List<DataIngestMoudleDataCdcTable> dataIngestMoudleDataCdcTableList = dataIngestMoudleDataCdcTableService.selectByMainId(id);
		return Result.OK(dataIngestMoudleDataCdcTableList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dataIngestMoudleIngestTask
    */
    @RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_task:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DataIngestMoudleIngestTask dataIngestMoudleIngestTask) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<DataIngestMoudleIngestTask> queryWrapper = QueryGenerator.initQueryWrapper(dataIngestMoudleIngestTask, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<DataIngestMoudleIngestTask> dataIngestMoudleIngestTaskList = dataIngestMoudleIngestTaskService.list(queryWrapper);

      // Step.3 组装pageList
      List<DataIngestMoudleIngestTaskPage> pageList = new ArrayList<DataIngestMoudleIngestTaskPage>();
      for (DataIngestMoudleIngestTask main : dataIngestMoudleIngestTaskList) {
          DataIngestMoudleIngestTaskPage vo = new DataIngestMoudleIngestTaskPage();
          BeanUtils.copyProperties(main, vo);
          List<DataIngestMoudleDataSourceConfig> dataIngestMoudleDataSourceConfigList = dataIngestMoudleDataSourceConfigService.selectByMainId(main.getId());
          vo.setDataIngestMoudleDataSourceConfigList(dataIngestMoudleDataSourceConfigList);
          List<DataIngestMoudleFieldMapping> dataIngestMoudleFieldMappingList = dataIngestMoudleFieldMappingService.selectByMainId(main.getId());
          vo.setDataIngestMoudleFieldMappingList(dataIngestMoudleFieldMappingList);
          List<DataIngestMoudleDataCdcTable> dataIngestMoudleDataCdcTableList = dataIngestMoudleDataCdcTableService.selectByMainId(main.getId());
          vo.setDataIngestMoudleDataCdcTableList(dataIngestMoudleDataCdcTableList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "数据接入模块的任务主信息表列表");
      mv.addObject(NormalExcelConstants.CLASS, DataIngestMoudleIngestTaskPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("数据接入模块的任务主信息表数据", "导出人:"+sysUser.getRealname(), "数据接入模块的任务主信息表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("org.jeecg.dataingest:data_ingest_moudle_ingest_task:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          // 获取上传文件对象
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<DataIngestMoudleIngestTaskPage> list = ExcelImportUtil.importExcel(file.getInputStream(), DataIngestMoudleIngestTaskPage.class, params);
              for (DataIngestMoudleIngestTaskPage page : list) {
                  DataIngestMoudleIngestTask po = new DataIngestMoudleIngestTask();
                  BeanUtils.copyProperties(page, po);
                  dataIngestMoudleIngestTaskService.saveMain(po, page.getDataIngestMoudleDataSourceConfigList(),page.getDataIngestMoudleFieldMappingList(),page.getDataIngestMoudleDataCdcTableList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
