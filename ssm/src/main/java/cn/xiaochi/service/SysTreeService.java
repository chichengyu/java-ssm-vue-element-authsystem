package cn.xiaochi.service;

import cn.xiaochi.dao.SysAclDao;
import cn.xiaochi.dao.SysAclModuleDao;
import cn.xiaochi.dao.SysDeptDao;
import cn.xiaochi.dto.AclDto;
import cn.xiaochi.dto.AclModuleLevelDto;
import cn.xiaochi.dto.DeptLevelDto;
import cn.xiaochi.model.SysAcl;
import cn.xiaochi.model.SysAclModule;
import cn.xiaochi.model.SysDept;
import cn.xiaochi.util.LevelUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysTreeService {

    @Resource
    private SysDeptDao sysDeptDao;
    @Resource
    private SysAclModuleDao sysAclModuleDao;
    @Resource
    private SysCoreService sysCoreService;
    @Resource
    private SysAclDao sysAclDao;

    // =========================== 权限模块与权限点结构树 ===========================

    /**
     * 获取角色权限结构树
     * @param roleId
     * @return
     */
    public List<AclModuleLevelDto> roleTree(int roleId){
        // 1.当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        // 2. 当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);

        // 使用流生成一个set集合 ，就不用遍历了 , jdk1.8 语法 lambda表达式
        Set<Integer> userAclIdList = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdList = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        List<SysAcl> allAclList = sysAclDao.getAll();
        Set<SysAcl> aclSet = new HashSet<>(allAclList);
        aclSet.addAll(userAclList);

        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : aclSet){
            AclDto dto = AclDto.adapt(acl);
            if (userAclIdList.contains(acl.getId())){
                dto.setHasAcl(true);
            }
            if (roleAclIdList.contains(acl.getId())){
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    // 生成结构树
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList){
        if (ObjectUtils.isEmpty(aclDtoList)){
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelDtoList = aclModuleTree();
        Multimap<Integer, AclDto> aclModuleMap = ArrayListMultimap.create();
        for (AclDto aclDto : aclDtoList){
            if (aclDto.getStatus() == 1){
                aclModuleMap.put(aclDto.getAclModuleId(),aclDto);
            }
        }
        // 绑定到结构树上
        bindAclsWithOrder(aclModuleLevelDtoList,aclModuleMap);
        return aclModuleLevelDtoList;
    }

    // 绑定到结构树上
    private void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList,Multimap<Integer,AclDto> moduleIdAclMap){
        if (ObjectUtils.isEmpty(aclModuleLevelList)){
            return;
        }
        for (AclModuleLevelDto dto : aclModuleLevelList){
            List<AclDto> aclDtoList = (List<AclDto>)moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)){
                // 排序
                Collections.sort(aclDtoList,aclSeqComparator);

                dto.setAclList(aclDtoList);
            }
        }
    }
    // 权限点排序
    private Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    // =========================== 权限模块结构树 ===========================

    /**
     * 返回权限模块结构树
     * @return
     */
    public List<AclModuleLevelDto> aclModuleTree(){
        List<SysAclModule> allAclModuleList = sysAclModuleDao.getAllAclModule();
        ArrayList<AclModuleLevelDto> dtoAclModuleList = Lists.newArrayList();
        for (SysAclModule aclModule : allAclModuleList){
            AclModuleLevelDto aclModuleLevelDto = AclModuleLevelDto.adept(aclModule);
            dtoAclModuleList.add(aclModuleLevelDto);
        }

        return aclModuleListToTree(dtoAclModuleList);
    }

    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> aclModuleLevelList){
        // 判空
        if (CollectionUtils.isEmpty(aclModuleLevelList)){
            return Lists.newArrayList();
        }
        // level -> [aclmodule1, aclmodule2, ...] Map<String, List<Object>>
        Multimap<String,AclModuleLevelDto> aclModuleDtoMultimap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();
        for (AclModuleLevelDto dto : aclModuleLevelList){
            aclModuleDtoMultimap.put(dto.getLevel(),dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }
        // 先排序(按照seq从小到大排序)，在递归筛选
        Collections.sort(rootList,aclModuleSeqComparator);
        // 递归生成树
        transformAclModuleTree(rootList,LevelUtil.ROOT,aclModuleDtoMultimap);
        return rootList;
    }

    private void transformAclModuleTree(List<AclModuleLevelDto> aclModuleLevelList,String level,Multimap<String,AclModuleLevelDto> levelDtoMultimap){
        for (int i = 0; i < aclModuleLevelList.size(); i++) {
            // 遍历所有一级元素
            AclModuleLevelDto aclModuleLevelDto = aclModuleLevelList.get(i);
            // 处理当前数据的层级level
            String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
            // 处理下一层
            List<AclModuleLevelDto> tempAclModuleList = (List<AclModuleLevelDto>) levelDtoMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempAclModuleList)){
                // 排序
                Collections.sort(tempAclModuleList,aclModuleSeqComparator);
                // 设置下一层部门数据
                aclModuleLevelDto.setSysAclModulesList(tempAclModuleList);
                // 递归进入到下一层处理
                transformAclModuleTree(aclModuleLevelList,level,levelDtoMultimap);
            }
        }
    }

    // 自定义排序
    private Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    // =========================== 部门结构树 ===========================

    /**
     * 返回部门结构树
     * @return
     */
    public List<DeptLevelDto> deptTree(){
        List<SysDept> deptList = sysDeptDao.getAllDept();
        List<DeptLevelDto> dtotList = Lists.newArrayList();
        for(SysDept dept : deptList){
            DeptLevelDto dto = DeptLevelDto.adept(dept);
            dtotList.add(dto);
        }
        return deptListToTree(dtotList);
    }

    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList){
        // 判空
        if (CollectionUtils.isEmpty(deptLevelList)){
            return Lists.newArrayList();
        }
        // level -> [aclmodule1, aclmodule2, ...] Map<String, List<Object>>
        Multimap<String,DeptLevelDto> levelDtoMultimap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();
        for (DeptLevelDto dto : deptLevelList){
            levelDtoMultimap.put(dto.getLevel(),dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }
        // 先排序(按照seq从小到大排序)，在递归筛选
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
            @Override
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        // 递归生成树
        transformDeptTree(rootList,LevelUtil.ROOT,levelDtoMultimap);
        return rootList;
    }

    private void transformDeptTree(List<DeptLevelDto> deptLevelList,String level,Multimap<String,DeptLevelDto> levelDtoMultimap){
        for (int i = 0; i < deptLevelList.size(); i++) {
            // 遍历所有一级元素
            DeptLevelDto deptLevelDto = deptLevelList.get(i);
            // 处理当前数据的层级level
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDtoMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)){
                // 排序
                Collections.sort(tempDeptList,deptSeqComparator);
                // 设置下一层部门数据
                deptLevelDto.setDeptList(tempDeptList);
                // 递归进入到下一层处理
                transformDeptTree(deptLevelList,nextLevel,levelDtoMultimap);
            }
        }
    }

    // 自定义排序
    private Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
