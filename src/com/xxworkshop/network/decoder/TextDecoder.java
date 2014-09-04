/*
 * Copyright (c) 2014 xxworkshop. All rights reserved.
 * Created by Broche Xu on 9/2/14 11:48 AM.
 */
package com.xxworkshop.network.decoder;

import java.io.*;

public class TextDecoder implements Decoder {
    private String encode = "UTF-8";

    public TextDecoder(String encode) {
        this.encode = encode;
    }

    public TextDecoder() {

    }

    @Override
    public Object decode(InputStream inputStream) {
        String result = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, encode));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while (null != (line = br.readLine())) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
            result = sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }
}
