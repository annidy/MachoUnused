package com.annidy.macho;

import com.annidy.utils.ShellCmd;

public class OtoolParser {
    static String OTOOL = "/usr/bin/otool -arch arm64";

    public String filePath;

    public OtoolParser(String filePath) {
        this.filePath = filePath;
    }

    public String all_classlist() {
        String output =  ShellCmd.run(OTOOL+" -o "+filePath);
        return get_section(output, "__objc_classlist");
    }

    public String objc_classlist() {
        String output =  ShellCmd.run(OTOOL+"  -V -s __DATA __objc_classlist "+filePath);
        return get_section(output, "__objc_classlist");
    }

    public String objc_classrefs() {
        String output =  ShellCmd.run(OTOOL+" -V -s __DATA __objc_classrefs "+filePath);
        return get_section(output, "__objc_classrefs");
    }

    public String objc_selrefs() {
        String output =  ShellCmd.run(OTOOL+" -V -s __DATA __objc_selrefs "+filePath);
        return get_section(output, "__objc_selrefs");
    }

    protected String get_section(String text, String section) {
        String lines[] = text.split("\\n");
        StringBuilder result = null;
        for (String line: lines) {
            if (line.startsWith("Contents")) {
                if (line.contains(section)) {
                    result = new StringBuilder();
                    continue;
                }
                if (result != null) {
                    break;
                }
            }

            if (result != null) {
                result.append(line);
                result.append("\n");
            }
        }

        return result.toString();
    }
}
