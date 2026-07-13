package com.kondra.vm.vmx.data;

import com.kondra.vm.common.vmx.ext.LabelExt;

import java.util.Date;

public class LabelExtension implements LabelExt {
    private final int type;
    private final int flag;
    private Date timestamp;
    private String label;

    public LabelExtension(int type, int flag) {
        this.type = type;
        this.flag = flag;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getFlag() {
        return flag;
    }

    @Override
    public int getSize() {
        return 4 + 32;   // timestamp + string + null terminator + empty
    }
}
