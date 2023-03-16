package io.github.ppetrbednar.stp;


import io.github.ppetrbednar.stp.logic.STProfiler;

public class AppLauncher {
    public static void main(String[] args) {
        //App.main(args);

        STProfiler profiler = new STProfiler();
       // profiler.profile();
        profiler.testPrint();
    }
}
