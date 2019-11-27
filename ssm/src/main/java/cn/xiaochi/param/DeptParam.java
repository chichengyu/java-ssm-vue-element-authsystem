package cn.xiaochi.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class DeptParam {

    private Integer id;

    @NotBlank(message = "部门名称不可以为空")
    @Length(max = 15,min = 2,message = "部门名称需要在2 - 15个字之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "顺序不能为空")
    private Integer seq;

    @Length(max = 150,message = "备注长度不能超过150个字")
    private String remark;

    public Integer getId() {
        return id;
    }

}
