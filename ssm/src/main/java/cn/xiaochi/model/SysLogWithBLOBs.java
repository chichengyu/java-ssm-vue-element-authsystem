package cn.xiaochi.model;

import java.util.Date;

public class SysLogWithBLOBs extends SysLog {
    private String oldValue;

    private String newValue;


    public SysLogWithBLOBs(String oldValue, String newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public SysLogWithBLOBs() {
        super(id, type, targetId, operator, operateTime, operateIp, status);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue == null ? null : oldValue.trim();
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue == null ? null : newValue.trim();
    }
}