package cn.xiaochi.dto;

import cn.xiaochi.model.SysDept;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;


import java.util.List;

@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {

    private List<DeptLevelDto> deptList = Lists.newArrayList();

    public static DeptLevelDto adept(SysDept sysDept){
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(sysDept,dto);
        return dto;
    }

}
