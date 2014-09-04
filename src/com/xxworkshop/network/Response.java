/*
 * Copyright (c) 2014 xxworkshop. All rights reserved.
 * Created by Broche Xu on 9/2/14 11:48 AM.
 */

package com.xxworkshop.network;

public class Response {
    public int statusCode;
    public Object result;
    public Request request;

    public Response(Request request, int statusCode, Object result) {
        this.request = request;
        this.statusCode = statusCode;
        this.result = result;
    }

    public boolean isValid() {
        boolean isvalid = true;
        if (statusCode < 200 || statusCode >= 400) {
            isvalid = false;
        }
        return isvalid;
    }
}
