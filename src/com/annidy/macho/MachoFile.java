package com.annidy.macho;

import java.util.*;

public class MachoFile {
    protected String fileName;
    protected OtoolParser otoolParser;

    protected String[] classRefs;
    protected String[] classLists;
    protected String[] selRefs;
    protected MachoClass[] allClass;
    protected Map<String, String> classMap;

    public MachoFile(String fileName) {
        this.fileName = fileName;
        otoolParser = new OtoolParser(fileName);

        allClass = MachoClass.allClasses(otoolParser.all_classlist());
        classMap = new HashMap<>();
        for (MachoClass c: allClass) {
            classMap.put(c.address, c.name);
        }
    }

    public String[] getClasslists() {

        if (classLists == null) {
            String[] pointers = getPointers(otoolParser.objc_classlist());
            Set<String> names = new HashSet<>();
            for (String pointer: pointers) {
                String name = classMap.get(pointer);
                if (name != null) {
                    names.add(name);
                }
            }
            classLists = names.toArray(new String[names.size()]);
        }

        return classLists;
    }

    public String[] getClassrefs() {

        if (classRefs == null) {
            String[] pointers = getPointers(otoolParser.objc_classrefs());
            Set<String> names = new HashSet<>();
            for (String pointer: pointers) {
                String name = classMap.get(pointer);
                if (name != null) {
                    names.add(name);
                }
            }
            classRefs = names.toArray(new String[names.size()]);
        }

        return classRefs;
    }

    String[] getPointers(String text) {
        ArrayList<String> pointers = new ArrayList<>();
        for (String line: text.split("\\n")) {
            String[] pline = line.split("\\s+");

            if (pline.length > 2)
                pointers.add(formatedPointer(pline[2]+pline[1]));
            if (pline.length > 4)
                pointers.add(formatedPointer(pline[4]+pline[3]));
        }
        return pointers.toArray(new String[pointers.size()]);
    }

    String formatedPointer(String pointer) {
        for (int i = 0; i < pointer.length(); i++) {
            if (pointer.charAt(i) != '0') {
                return "0x"+pointer.substring(i);
            }
        }
        return "0x0";
    }

    public String[] getSelrefs() {
        if (selRefs == null) {
            String refs = otoolParser.objc_selrefs();
            ArrayList<String> sels = new ArrayList<>();
            for (String ref: refs.split("\\n")) {
                sels.add(ref.split("__objc_methname:")[1]);
            }
            selRefs = sels.toArray(new String[sels.size()]);
        }

        return selRefs;
    }

    public MachoClass[] getAllClass() {
        return allClass;
    }
}
