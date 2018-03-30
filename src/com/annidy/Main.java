package com.annidy;

import com.annidy.macho.MachoClass;
import com.annidy.macho.MachoFile;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        if (args.length < 1)
            return;
        MachoFile file = new MachoFile(args[0]);

        List<String> classRefs =  new ArrayList<>(Arrays.asList(file.getClassrefs()));
        List<String> classLists = new ArrayList<>(Arrays.asList(file.getClasslists()));

        classLists.removeAll(classRefs);
        System.out.println("=== Unused class ===");
        for (String cls:
                classLists) {
            System.out.println(cls);
        }

        System.out.println("\n=== Unused selector ===");

        Set<String> refSel = new HashSet<>();
        refSel.addAll(Arrays.asList(file.getSelrefs()));
        for (MachoClass cls: file.getAllClass()) {
            Set<String> sels = new HashSet<>();
            sels.addAll(cls.getSelectorListWithoutProperty());
            sels.removeAll(refSel);
            if (sels.size() > 0) {
                System.out.println(cls.getName());
                System.out.println("------------------------");
                for (String sel: sels) {
                    System.out.println(sel);
                }
                System.out.println("");
            }
        }
    }
}
