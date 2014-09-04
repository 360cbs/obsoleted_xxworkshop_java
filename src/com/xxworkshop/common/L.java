/*
 * Copyright (c) 2014 xxworkshop. All rights reserved.
 * Created by Broche Xu on 9/2/14 11:48 AM.
 */

package com.xxworkshop.common;

public final class L {
    public final static void log(String message) {
        System.out.println(message);
    }

    public final static void log(int message) {
        log(String.valueOf(message));
    }

    public final static void log(float message) {
        log(String.valueOf(message));
    }

    public final static void log(Object message) {
        log(message.toString());
    }

    public final static void logSplitter() {
        log("====================");
    }
}
