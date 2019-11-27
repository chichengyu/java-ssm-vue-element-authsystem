package cn.xiaochi.dao;

import cn.xiaochi.model.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    // 判断同模块下权限点名称是否存在
    int countNyAclModuleId(Integer aclModuleId, String name, Integer id);

    // 通过权限点id列表查询所有权限点
    List<SysAcl> getByIdList(@Param("idList")List<Integer> idList);

    // 查询所有权限点
    List<SysAcl> getAll();

    // 通过url查询
    List<SysAcl> getByUrl(@Param("url")String url);
}