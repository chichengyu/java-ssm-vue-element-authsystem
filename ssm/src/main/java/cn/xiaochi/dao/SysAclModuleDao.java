package cn.xiaochi.dao;

import cn.xiaochi.model.SysAclModule;
import cn.xiaochi.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    // 通过level获取子部门
    List<SysAclModule> getChildAclModuleListByLevel(@Param("level") String level);

    // 批量更新所有子部门 level
    void batchUpdateLevel(@Param("sysAclModulesList") List<SysAclModule> sysAclModulesList);

    // 统计同级下部门名称是否存在
    int countByNameAndParent(@Param("parentId") Integer parentId,@Param("name") String name,@Param("id") Integer id);

    List<SysAclModule> getAllAclModule();
}