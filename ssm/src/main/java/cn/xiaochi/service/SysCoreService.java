package cn.xiaochi.service;

import cn.xiaochi.beans.CacheKeyConstants;
import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.dao.SysAclDao;
import cn.xiaochi.dao.SysRoleAclDao;
import cn.xiaochi.dao.SysRoleUserDao;
import cn.xiaochi.model.SysAcl;
import cn.xiaochi.util.JsonMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限管理核心Service
 */
@Service
public class SysCoreService {

    @Resource
    private SysRoleUserDao sysRoleUserDao;
    @Resource
    private SysRoleAclDao sysRoleAclDao;
    @Resource
    private SysAclDao sysAclDao;
    @Resource
    private SysCacheService sysCacheService;

    /**
     * 获取当前用户权限列表
     * @return
     */
    public List<SysAcl> getCurrentUserAclList(){
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    /**
     * 当前角色已经分配的权限点列表
     * @return
     */
    public List<SysAcl> getRoleAclList(int roleId){
        // 通过角色id列表查询所有权限id列表
        List<Integer> aclIdListByRoleIdList = sysRoleAclDao.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (ObjectUtils.isEmpty(aclIdListByRoleIdList)){
            return Lists.newArrayList();
        }
        return sysAclDao.getByIdList(aclIdListByRoleIdList);
    }

    /**
     * 获取当前用户拥有的所有权限点列表
     * @param userId
     * @return
     */
    private List<SysAcl> getUserAclList(int userId){
        if (isSuperAdmin()){
            return sysAclDao.getAll();
        }
        // 跟 userId 查询角色id列表
        List<Integer> roleIdListByUserId = sysRoleUserDao.getRoleIdListByUserId(userId);
        if (ObjectUtils.isEmpty(roleIdListByUserId)){
            return Lists.newArrayList();
        }
        // 通过角色id列表查询所有权限id列表
        List<Integer> aclIdListByRoleIdList = sysRoleAclDao.getAclIdListByRoleIdList(roleIdListByUserId);
        if (ObjectUtils.isEmpty(aclIdListByRoleIdList)){
            return Lists.newArrayList();
        }
        // 通过权限点id列表查询所有权限点
        return sysAclDao.getByIdList(aclIdListByRoleIdList);
    }

    /**
     * 判断是否是超级管理员
     * @return
     */
    private boolean isSuperAdmin(){
        Integer userId = RequestHolder.getCurrentUser().getId();
        if (!ObjectUtils.isEmpty(userId) && userId == 1){
            return true;
        }
        return false;
    }

    public boolean hasUrlAcl(String url){
        if (isSuperAdmin()){// 超级管理员
            return true;
        }
        List<SysAcl> aclList = sysAclDao.getByUrl(url);
        if (ObjectUtils.isEmpty(aclList)){// 公开路由
            return true;
        }
        // redis 缓存
        List<SysAcl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());
        boolean hasValidAcl = false;// 如果所有权限点都无效
        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysAcl acl : aclList){
            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1){// 权限点无效
                continue;
            }
            if (userAclIdSet.contains(acl.getId())){
                return true;
            }
        }
        if (!hasValidAcl){// 如果所有权限点都无效，也要返回 true
            return true;
        }
        return false;
    }

    /**
     * redis缓存
     * @return
     */
    public List<SysAcl> getCurrentUserAclListFromCache() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                sysCacheService.saveCache(JsonMapper.objToString(aclList), 600, CacheKeyConstants.USER_ACLS, String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.stringToObj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }
}
