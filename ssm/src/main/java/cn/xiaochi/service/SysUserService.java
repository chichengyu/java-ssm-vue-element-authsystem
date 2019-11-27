package cn.xiaochi.service;

import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.dao.SysUserDao;
import cn.xiaochi.exception.ParamException;
import cn.xiaochi.model.SysUser;
import cn.xiaochi.param.UserParam;
import cn.xiaochi.util.IpUtil;
import cn.xiaochi.util.MD5Util;
import cn.xiaochi.util.MailUtil;
import cn.xiaochi.util.PasswordUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class SysUserService {

    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private SysLogService sysLogService;

    public List<SysUser> selectAll(int deptId){
        return sysUserDao.selectAll(deptId);
    }

    /**
     * 添加用户
     * @param userParam
     */
    public void save(UserParam userParam) throws Exception {
        // 判断手机与邮箱是否存在
        if (checkTelExist(userParam.getTelephone(),userParam.getId())){
            throw new ParamException("手机已经占用");
        }
        if (checkTelExist(userParam.getMail(),userParam.getId())){
            throw new ParamException("邮箱已经占用");
        }
         // String password = MD5Util.encryptByMD5(DigestUtils.md5Hex(PasswordUtil.randomPassword()));
        String password = MD5Util.encryptByMD5(DigestUtils.md5Hex("123456"));
        SysUser user = SysUser.builder().username(userParam.getUsername()).password(password).telephone(userParam.getTelephone()).mail(userParam.getMail()).deptId(userParam.getDeptId()).status(userParam.getStatus()).remark(userParam.getRemark()).build();
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());

        // TODO: sendEmail
        // HashSet<String> receivers = new HashSet<>();
        // receivers.add("875267425@qq.com");
        // MailUtil.send("主题","<span>这是正文</span>",receivers);

        sysUserDao.insertSelective(user);

        // 记录日志
        sysLogService.saveUserLog(null,user);
    }

    /**
     * 更新用户
     * @param userParam
     */
    public void update(UserParam userParam) throws Exception {
        if (ObjectUtils.isEmpty(userParam.getId())){
            throw new ParamException("id不能为空");
        }
        // 判断手机与邮箱是否存在
        if (checkTelExist(userParam.getTelephone(),userParam.getId())){
            throw new ParamException("手机已经占用");
        }
        if (checkTelExist(userParam.getMail(),userParam.getId())){
            throw new ParamException("邮箱已经占用");
        }
        // 验证用户是否存在
        SysUser before = sysUserDao.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(before,"待更新的用户不存在");

        SysUser after = SysUser.builder().id(userParam.getId()).username(userParam.getUsername()).telephone(userParam.getTelephone()).mail(userParam.getMail()).deptId(userParam.getDeptId()).status(userParam.getStatus()).remark(userParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysUserDao.updateByPrimaryKeySelective(after);

        // 记录日志
        sysLogService.saveUserLog(before,after);
    }

    /**
     * 判断手机是否存在
     * @param tel
     * @param id
     * @return
     */
    private Boolean checkTelExist(String tel,Integer id){
        return sysUserDao.countByTelphone(tel,id) > 0;
    }

    /**
     * 判断邮箱是否存在
     * @param email
     * @param id
     * @return
     */
    private Boolean checkEmailExist(String email,Integer id){
        return sysUserDao.countByMail(email,id) > 0;
    }

    /**
     * 登录查询用户
     * @param username
     * @return
     */
    public SysUser findByKeyWord(String username) {
        return sysUserDao.findByKeyWord(username);
    }
}
