package com.thalesgroup.optet.devenv.popup.actions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;


public class RunAllTests {

    public static void run(String jarfile) {
    	
//        JUnitCore runner = new JUnitCore();
//        runner.addListener(new TextListener(System.out));
//        runner.run(MaClasseDeTest.class);
        
        
        String[] tests = findTests(jarfile);
        org.junit.runner.JUnitCore.main(tests);
    }

    private static String[] findTests(String jarfile) {
        ArrayList<String> tests = new ArrayList<String>();
        try {
            JarFile jf = new JarFile(jarfile);
            for (Enumeration<JarEntry> e = jf.entries(); e.hasMoreElements();) {
                String name = e.nextElement().getName();
                if (name.startsWith("suneido/") && name.endsWith("Test.class")
                        && !name.contains("$"))
                    tests.add(name.replaceAll("/", ".")
                            .substring(0, name.length() - 6));
            }
            jf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tests.toArray(new String[0]);
    }

    public static void main(String[] args) {
        run("jsuneido.jar");
    }

}