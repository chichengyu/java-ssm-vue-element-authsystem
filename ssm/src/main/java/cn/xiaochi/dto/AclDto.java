package cn.xiaochi.dto;

import cn.xiaochi.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@ToString
public class AclDto extends SysAcl {

    // 是否要默认选中
    private boolean checked = false;

    // 是否有权限操作
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl sysAcl){
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(sysAcl,aclDto);
        return aclDto;
    }
}
