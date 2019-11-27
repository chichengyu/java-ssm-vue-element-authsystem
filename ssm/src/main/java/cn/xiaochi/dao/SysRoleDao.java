package cn.xiaochi.dao;

import cn.xiaochi.model.SysRole;

import java.util.List;

public interface SysRoleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    int countByName(String name, Integer id);

    List<SysRole> selectAll();
}