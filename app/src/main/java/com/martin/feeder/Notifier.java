package com.martin.feeder;

public class Notifier {

    private static boolean safe = false;
    private static NewsFragment parent;

    public static void show(String message) {
        if (safe) {
            parent.showSnackbarMessage(message);
        }
    }

    public static void setUnsafe() {
        safe = false;
    }

    public static void setParent(NewsFragment newParent) {
        parent = newParent;
        safe = true;
    }

}
