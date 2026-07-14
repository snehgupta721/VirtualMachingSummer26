package com.kondra.vm.vmx;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxException;
import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.vmx.data.LabelExtension;

import java.io.File;
import java.util.Date;
import java.util.List;

public class VmxService {

    public MyVmxFile getVmxFile(File file, Integer major, Integer minor, Integer build, String label) throws VmxException {
        MyVmxFile vmx = new MyVmxFile(file);

        if (major != null && minor != null) {
            int currentBuild = vmx.getVersion().getBuildNum();
            vmx.setVersion(new Version(major, minor, currentBuild));
        }
        if (build != null) {
            vmx.setVersion(new Version(vmx.getVersion().getMajor(), vmx.getVersion().getMinor(), build));
        }
        if (label != null) {
            vmx.setLabel(label, new Date());
        }

        return vmx;
    }

    public void printDetails(MyVmxFile vmx) {
        System.out.println("Detailed information:");
        List<VmxExt> exts = vmx.getExtensions();
        System.out.println("extensions : " + exts.size());
        for (VmxExt ext : exts) {
            System.out.println("   (" + ext.getType() + ") " + vmx.getExtensionName(ext));
        }
        System.out.println();
    }

    public void printInfo(MyVmxFile vmx) {
        System.out.println("Version information:");
        Version version = vmx.getVersion();
        System.out.println("   version      : " + version.getMajor() + "." + version.getMinor());
        System.out.println("   build number : " + version.getBuildNum());

        LabelExtension lext = (LabelExtension) vmx.getExtension(VmxExt.TYPE_LABEL);
        if (lext == null) {
            System.out.println("No label extension available");
        } else {
            System.out.println("Label extension:");
            System.out.println("   timestamp : " + lext.getTimestamp());
            System.out.println("   label     : " + lext.getLabel());
        }
        System.out.println();
    }

    public void printPreloads(MyVmxFile vmx) {
        System.out.println("required preloads:");
        for (String name : vmx.getPreloadSymbols()) {
            System.out.println("   " + name);
        }
        System.out.println();
    }

    public void printImports(MyVmxFile vmx) {
        System.out.println("imported symbols:");
        for (String symbol : vmx.getImportedSymbols()) {
            System.out.println("   " + symbol);
        }
        System.out.println();
    }

    public void printExports(MyVmxFile vmx) {
        System.out.println("exported symbols:");
        for (String symbol : vmx.getExportedSymbols()) {
            System.out.println("   " + symbol);
        }
        System.out.println();
    }
}
