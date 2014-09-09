package com.xxworkshop.network.decoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by brochexu on 9/8/14.
 */
public class ByteArrayDecoder implements Decoder {
    @Override
    public Object decode(InputStream inputStream) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[256];
        int count;
        byte[] result = null;
        try {
            while ((count = inputStream.read(buffer)) > 0) {
                bos.write(buffer, 0, count);
            }
            bos.flush();
            result = bos.toByteArray();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
