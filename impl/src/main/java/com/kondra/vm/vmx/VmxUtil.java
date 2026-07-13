package com.kondra.vm.vmx;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.vmx.data.LabelExtension;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.Date;
import java.util.List;

public class VmxUtil {
    // Command line option keys
    private static final String HELP = "h";
    private static final String DETAILS = "detail";
    private static final String INFO = "info";
    private static final String PRELOAD = "preload";
    private static final String IMPORT = "import";
    private static final String EXPORT = "export";
    private static final String OUTPUT_FILE = "o";
    private static final String VERSION = "vers";
    private static final String BUILD = "build";
    private static final String LABEL = "label";

    public static void main(String [] args) {
        System.out.println("Starting VMX Tool...");
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption(HELP, "help", false, "Show this help");
        options.addOption(DETAILS, false, "Display internal details of the vmx file");
        options.addOption(INFO, false, "Display version and label information of the vmx file");
        options.addOption(PRELOAD, false, "Display the list of preload programs");
        options.addOption(IMPORT, false, "Display the list of imported symbols");
        options.addOption(EXPORT, false, "Display the list of exported symbols");
        options.addOption(OUTPUT_FILE, true, "Specify an output file");
        options.addOption(VERSION, true, "Change the version to the specified major.minor version");
        options.addOption(BUILD, true, "Change the build number to the specified numbers");
        options.addOption(LABEL, true, "Change the label to the specified string and the " +
                                                        "associated timestamp to the curren time");

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(HELP)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(VmxUtil.class.getName(), options);
                return;
            }

            // Extract values
            boolean details = cmd.hasOption(DETAILS);
            boolean info = cmd.hasOption(INFO);
            boolean preload = cmd.hasOption(PRELOAD);
            boolean isImport = cmd.hasOption(IMPORT);
            boolean export = cmd.hasOption(EXPORT);
            String output = cmd.getOptionValue(OUTPUT_FILE);
            String vers = cmd.getOptionValue(VERSION);
            String build = cmd.getOptionValue(BUILD);
            String label = cmd.getOptionValue(LABEL);

            String vmxFile = "";
            List<String> remainingArgs = cmd.getArgList();
            vmxFile = remainingArgs.get(0);


            MyVmxFile vmx = new MyVmxFile(new File(vmxFile));
            if (vers != null) {
                String[] versions = vers.split(",");
                int major = Integer.parseInt(versions[0]);
                int minor = Integer.parseInt(versions[1]);
                Version version = new Version(major, minor, vmx.getVersion().getBuildNum());
                vmx.setVersion(version);
            }
            if (build != null) {
                int buildNum = Integer.parseInt(build);
                Version version = new Version(vmx.getVersion().getMajor(), vmx.getVersion().getMinor(), buildNum);
                vmx.setVersion(version);
            }
            if (label != null) {
                vmx.setLabel(label, new Date());
            }

            if (details) {
                System.out.println("Detailed information:");
                List<VmxExt> exts = vmx.getExtensions();
                System.out.println("extensions : " + exts.size());
                for (VmxExt ext : exts) {
                    System.out.println("   (" + ext.getType() + ") " + vmx.getExtensionName(ext));
                }
            }
            if (info) {
                System.out.println("Version information:");
                Version version = vmx.getVersion();
                System.out.println("   version      : " + version.getMajor() + "." + version.getMinor());
                System.out.println("   build number : " + version.getBuildNum());

                LabelExtension lext = null;
                List<VmxExt> exts = vmx.getExtensions();
                for (VmxExt ext : exts) {
                    if (ext.getType() == VmxExt.TYPE_LABEL) {
                        lext = (LabelExtension) ext;
                    }
                }
                if (lext == null) {
                    System.out.println("No label extension available");
                } else {
                    System.out.println("Label extension:");
                    System.out.println("   timestamp : " + lext.getTimestamp());
                    System.out.println("   label     : " + lext.getLabel());
                }
            }


        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }
}
