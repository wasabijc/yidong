package com.example.utils;

public class ThreadUtils {
    public static Thread newThread(Runnable runnable, String name) {
        return new Thread(runnable, name);
    }

    public static Thread newThread(Runnable runnable) {
        return new Thread(runnable);
    }

    public static boolean newThreadAndStart(Runnable runnable, String name) {
        Thread thread = new Thread(runnable, name);
        try {
            thread.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean newThreadAndStart(Runnable runnable) {
        Thread thread = new Thread(runnable);
        try {
            thread.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
