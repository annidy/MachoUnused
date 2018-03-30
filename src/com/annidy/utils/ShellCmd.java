package com.annidy.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShellCmd {
    static public String run(String command) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            Process process = Runtime.getRuntime().exec(command);

            InputStream stdin = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader input = new BufferedReader(isr);

            String line;
            while ((line = input.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }

        } catch (java.io.IOException e) {
            System.err.println("IOException " + e.getMessage());
        }

        return stringBuilder.toString();
    }
}
