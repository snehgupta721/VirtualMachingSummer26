package com.kondra.vm.vmx;

import com.kondra.vm.vmx.data.*;
import org.apache.commons.cli.*;

import java.io.File;
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
            File file = new File(vmxFile);

            Integer major = null;
            Integer minor = null;
            Integer buildNum = null;
            VmxService service = new VmxService();
            if (vers != null) {
                String[] versions = vers.split("\\.");
                major = Integer.parseInt(versions[0]);
                minor = Integer.parseInt(versions[1]);
            }
            if (build != null) {
                buildNum = Integer.parseInt(build);
            }
            MyVmxFile vmx = service.getVmxFile(file, major, minor, buildNum, label);

            if (details) {
                service.printDetails(vmx);
            }
            if (info) {
                service.printInfo(vmx);
            }
            if (preload) {
                service.printPreloads(vmx);
            }
            if (isImport) {
                service.printImports(vmx);
            }
            if (export) {
                service.printExports(vmx);
            }
            if (output != null) {
                vmx.write(new File(output));
            } else {
                vmx.write(file);
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }
}
