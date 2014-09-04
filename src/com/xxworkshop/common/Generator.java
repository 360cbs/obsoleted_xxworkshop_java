/*
 * Copyright (c) 2014 xxworkshop. All rights reserved.
 * Created by Broche Xu on 9/2/14 11:48 AM.
 */

package com.xxworkshop.common;

import java.util.UUID;

public final class Generator {
    public final static String getUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
