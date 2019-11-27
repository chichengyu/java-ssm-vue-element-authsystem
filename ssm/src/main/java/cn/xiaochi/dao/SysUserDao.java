package cn.xiaochi.dao;

import cn.xiaochi.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findByKeyWord(@Param("username") String username);

    int countByTelphone(@Param("telephone") String telephone,@Param("id") Integer id);

    int countByMail(@Param("mail") String mail,@Param("id") Integer id);

    List<SysUser> selectAll(@Param("deptId") int deptId);
}