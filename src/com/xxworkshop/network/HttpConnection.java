/*
 * Copyright (c) 2014 xxworkshop. All rights reserved.
 * Created by Broche Xu on 9/2/14 11:48 AM.
 */

package com.xxworkshop.network;

import com.xxworkshop.common.F;
import com.xxworkshop.common.L;
import com.xxworkshop.common.S;
import com.xxworkshop.network.decoder.Decoder;
import com.xxworkshop.network.decoder.TextDecoder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public final class HttpConnection {
    public static final int DefaultCacheTimeout = 10;

    public String Host = "";
    public boolean Debug = true;
    public boolean SessionEnabled = true;

    public static HttpConnection newInstance() {
        return new HttpConnection();
    }

    private Hashtable<String, ArrayList<ResponseHandler>> handlers;

    private HttpConnection() {
        handlers = new Hashtable<String, ArrayList<ResponseHandler>>();
    }

    private Hashtable<String, CacheItem> caches = new Hashtable<String, CacheItem>(50);

    private String cookie = "";

    private boolean cookieUpdated = false;

    public boolean isCookieUpdated() {
        return cookieUpdated;
    }

    private Decoder defaultDecoder = new TextDecoder();

    public Response sendRequest(Request request) {
        XXHttpThread thread = new XXHttpThread(request);
        if (request.isAsynchronized) {
            thread.start();
            return null;
        } else {
            thread.run();
            return thread.getResponse();
        }
    }


    public void addResponseHandler(ResponseHandler handler, String url) {
        if (handlers.containsKey(url)) {
            ArrayList<ResponseHandler> handlerList = handlers.get(url);
            handlerList.add(handler);
        } else {
            ArrayList<ResponseHandler> handlerList = new ArrayList<ResponseHandler>();
            handlerList.add(handler);
            handlers.put(url, handlerList);
        }
    }

    public void removeResponseHandler(ResponseHandler handler, String url) {
        if (handlers.containsKey(url)) {
            ArrayList<ResponseHandler> handlerList = handlers.get(url);
            if (handlerList.contains(handler)) {
                handlerList.remove(handler);
            }
        }
    }

    private class XXHttpThread extends Thread {
        private Request request;
        private Response response = null;

        public XXHttpThread(Request request) {
            this.request = request;
        }

        public Response getResponse() {
            return response;
        }

        @Override
        public void run() {
            String surl = Host + request.url;
            String sparams = F.map2String(request.params, "=", "&");

            if (Debug) {
                L.log("==========>\nurl: " + surl + "\nparams: " + sparams + "\ncookie: " + cookie + "\nmethod: " + request.method);
            }

            String cacheKey = surl + "?" + sparams;
            if (request.useCache) {
                if (caches.containsKey(cacheKey)) {
                    CacheItem ci = caches.get(cacheKey);
                    if (S.getTimeStamp() - ci.timestamp <= request.cacheTimeout) {
                        if (Debug) {
                            L.log("<==========\nresult(cache): " + ci.content);
                        }
                        Response response = new Response(request, 200, ci.content);
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("response", response);
                        map.put("handler", request.responseHandler);
                        Message message = new Message(0, map);
                        handleMessage(message);
                        return;
                    }
                }
            }

            try {
                HttpURLConnection connection;
                if (request.method.equals(HttpMethod.Post)) {
                    if (request.isEncodePostContent) {
                        for (String key : request.params.keySet()) {
                            request.params.put(key, URLEncoder.encode(request.params.get(key)));
                        }
                    }
                    sparams = F.map2String(request.params, "=", "&");
                    connection = (HttpURLConnection) (new URL(surl)).openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod(request.method);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(10000);
                    if (SessionEnabled && !cookie.equals("")) {
                        connection.setRequestProperty("Cookie", cookie);
                    }
                    if (request.headers != null) {
                        for (String key : request.headers.keySet()) {
                            connection.setRequestProperty(key, request.headers.get(key));
                        }
                    }
                    connection.connect();

                    OutputStream os = connection.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os, request.encoding);
                    osw.write(sparams);
                    osw.flush();
                    os.flush();
                    os.close();
                    osw.close();
                } else {
                    for (String key : request.params.keySet()) {
                        request.params.put(key, URLEncoder.encode(request.params.get(key)));
                    }
                    sparams = F.map2String(request.params, "=", "&");
                    String fullurl = surl + "?" + sparams;
                    connection = (HttpURLConnection) (new URL(fullurl)).openConnection();
                    connection.setDoInput(true);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(10000);
                    if (SessionEnabled && !cookie.equals("")) {
                        connection.setRequestProperty("Cookie", cookie);
                    }
                    if (request.headers != null) {
                        for (String key : request.headers.keySet()) {
                            connection.setRequestProperty(key, request.headers.get(key));
                        }
                    }
                    connection.connect();
                }

                if (SessionEnabled) {
                    cookieUpdated = false;
                    String tcookie = connection.getHeaderField("Set-Cookie");
                    if (tcookie != null && !tcookie.equals("")) {
                        cookie = tcookie;
                        cookieUpdated = true;
                    }
                }

                int statusCode = connection.getResponseCode();
                if (statusCode >= 200 && statusCode < 400) {
                    Object result = null;
                    if (request.decoder == null) {
                        result = defaultDecoder.decode(connection.getInputStream());
                    } else {
                        result = request.decoder.decode(connection.getInputStream());
                    }
                    connection.disconnect();

                    if (Debug) {
                        L.log("<==========\nresult: " + result.toString());
                    }

                    // cache
                    if (result != null) {
                        if (caches.containsKey(cacheKey)) {
                            CacheItem ci = caches.get(cacheKey);
                            ci.content = result;
                            ci.timestamp = S.getTimeStamp();
                        } else {
                            CacheItem ci = new CacheItem();
                            ci.content = result;
                            ci.timestamp = S.getTimeStamp();
                            caches.put(cacheKey, ci);
                        }
                    }

                    response = new Response(request, statusCode, result);
                    Message message = new Message(0, response);
                    handleMessage(message);
                } else {
                    response = new Response(request, statusCode, null);
                    Message message = new Message(0, response);
                    handleMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                response = new Response(request, 499, null);
                Message message = new Message(0, response);
                handleMessage(message);
            }
        }
    }

    public void handleMessage(Message msg) {
        Response response = (Response) msg.obj;
        ResponseHandler handler = response.request.responseHandler;

        if (handlers.containsKey(response.request.url)) {
            ArrayList<ResponseHandler> handlerList = handlers.get(response.request.url);
            for (ResponseHandler thandler : handlerList) {
                thandler.handleResponse(response);
            }
        }
        if (handler != null) {
            handler.handleResponse(response);
        }
    }

    private class CacheItem {
        public double timestamp;
        public Object content;
    }

    private class Message {
        public int what;
        public Object obj;

        public Message(int what, Object obj) {
            this.what = what;
            this.obj = obj;
        }
    }
}
