package cn.xiaochi.dao;

import cn.xiaochi.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> getAllDept();

    // 通过level获取子部门
    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    // 批量更新所有子部门 level
    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    // 统计同级下部门名称是否存在
    int countByNameAndParent(@Param("parentId") Integer parentId,@Param("name") String name,@Param("id") Integer id);

}