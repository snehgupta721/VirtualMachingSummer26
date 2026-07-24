package com.kondra.vm.vmx.data;

import com.kondra.vm.common.vmx.ext.Affinity;
import com.kondra.vm.common.vmx.ext.AffinityExt;

import java.util.ArrayList;
import java.util.List;

public class AffinityExtension implements AffinityExt {
    private final int type;
    private final int flag;
    private final List<Affinity> affinityRecords;

    public AffinityExtension(int type, int flag) {
        this.type = type;
        this.flag = flag;
        affinityRecords = new ArrayList<>();
    }

    public void addAffinityRecord(Affinity affinity) {
        affinityRecords.add(affinity);
    }

    @Override
    public List<Affinity> getAffinityList() {
        return affinityRecords;
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
        return AffinityRecord.SIZE * affinityRecords.size();
    }
}
