/*
 * Copyright (c) 2014 xxworkshop. All rights reserved.
 * Created by Broche Xu on 9/2/14 11:48 AM.
 */
package com.xxworkshop.network.decoder;

import java.io.InputStream;

public interface Decoder {
    Object decode(InputStream inputStream);
}
