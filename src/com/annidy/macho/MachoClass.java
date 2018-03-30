package com.annidy.macho;

import java.util.ArrayList;

public class MachoClass {

    String address;

    public String getName() {
        return name;
    }

    String name;

    public ArrayList<String> getSelectorList() {
        return selectorList;
    }

    public ArrayList<String> getPropertyList() {
        return propertyList;
    }

    public ArrayList<String> getSelectorListWithoutProperty() {
        ArrayList<String> props = new ArrayList<>();
        for (String getter: propertyList) {
            String setter = "set"+getter.replaceFirst(getter.charAt(0)+"", Character.toUpperCase(getter.charAt(0))+"")+":";
            props.add(getter);
            props.add(setter);
        }

        ArrayList<String> tmp = new ArrayList<>(selectorList);
        tmp.removeAll(props);
        return tmp;
    }

    ArrayList<String> selectorList = new ArrayList<>();
    ArrayList<String> propertyList = new ArrayList<>();

    public static MachoClass[] allClasses(String classesText) {
        ArrayList<MachoClass> classes = new ArrayList<>();
        String[] lines = classesText.split("\\n");
        MachoClass obj = null;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.matches("^[a-f0-9]+ 0x[a-f0-9]+")) {
                obj = new MachoClass();
                obj.address = line.split("\\s+")[1];
                classes.add(obj);
                continue;
            }
            if (obj != null) {
                if (obj.name == null) {
                    // 首先取到类名
                    if (line.startsWith("name")) {
                        obj.name = line.split("\\s+")[2];
                    }
                    continue;
                }

                if (line.startsWith("baseMethods") ||
                        line.startsWith("instanceMethods") ||
                        line.startsWith("baseProperties")) {
                    i = obj.parseSelectorGroup(lines, i);
                }
                if (line.startsWith("baseProtocols")) {
                    i = obj.parseSelectorGroup(lines, i+1);
                }
            }
        }
        return classes.toArray(new MachoClass[classes.size()]);
    }

    protected int parseSelectorGroup(String[] lines, int start) {
        int count = 0;
        int index = start+2;

        String line = lines[index].trim();
        if (line.startsWith("count")) {
            count = Integer.parseInt(line.split("\\s+")[1]);
        }
        if (count == 0)
            return start;

        index++;

        for (int i = 0; i < count; i++) {
            while (lines[index] != null) {
                line = lines[index].trim();
                index++;
                if (line.startsWith("name")) {
                    String name = line.split("\\s+")[2];
                    if (name.startsWith("_")) {
                        propertyList.add(name.substring(1));
                    } else if (!name.startsWith(".")) {
                        selectorList.add(name);
                    }
                    break;
                }
            }
        }

        return index;
    }
}
