package org.jeecg.modules.workflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 快速用户查询（按角色编码/部门ID）
 */
@Mapper
public interface WorkflowUserLookupMapper {

    @Select("SELECT u.id FROM sys_user u \n" +
            "JOIN sys_user_role ur ON u.id = ur.user_id \n" +
            "JOIN sys_role r ON ur.role_id = r.id \n" +
            "WHERE r.role_code = #{roleCode} AND (u.del_flag = 0 OR u.del_flag IS NULL)")
    List<String> selectUserIdsByRoleCode(@Param("roleCode") String roleCode);

    @Select("SELECT user_id FROM sys_user_depart WHERE dep_id = #{deptId}")
    List<String> selectUserIdsByDeptId(@Param("deptId") String deptId);
}


