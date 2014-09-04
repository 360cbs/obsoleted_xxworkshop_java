/*
 * Copyright (c) 2014 xxworkshop. All rights reserved.
 * Created by Broche Xu on 9/2/14 11:48 AM.
 */
package com.xxworkshop.network;

import com.xxworkshop.network.decoder.Decoder;
import com.xxworkshop.network.decoder.TextDecoder;

import java.util.Hashtable;

public class Request {
    public String url = "";
    public Hashtable<String, String> params = new Hashtable<String, String>();
    public String method = HttpMethod.Get;
    public Hashtable<String, String> headers = new Hashtable<String, String>();
    public String encoding = "UTF-8";
    public Decoder decoder = new TextDecoder();
    public ResponseHandler responseHandler = null;
    public boolean useCache = false;
    public int cacheTimeout = HttpConnection.DefaultCacheTimeout;
    public boolean isAsynchronized = true;
    public boolean isEncodePostContent = false;

    public Request(String url) {
        this.url = url;
    }

    /**
     * 设置访问地址
     * @param url
     * @return
     */
    public Request setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 设置提交参数
     * @param params
     * @return
     */
    public Request setParams(Hashtable<String, String> params) {
        this.params = params;
        return this;
    }

    /**
     * 设置访问方式Get/Post
     * @param method
     * @return
     */
    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * 设置自定义请求头部
     * @param headers
     * @return
     */
    public Request setHeaders(Hashtable<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 设置请求发送时的编码
     * @param encoding
     * @return
     */
    public Request setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * 设置响应解析器
     * @param decoder
     * @return
     */
    public Request setDecoder(Decoder decoder) {
        this.decoder = decoder;
        return this;
    }

    /**
     * 设置响应回调函数
     * @param responseHandler
     * @return
     */
    public Request setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }

    /**
     * 设置是否使用缓存
     * @param useCache
     * @return
     */
    public Request setUseCache(boolean useCache) {
        this.useCache = useCache;
        return this;
    }

    /**
     * 设置缓存时间
     * @param cacheTimeout
     * @return
     */
    public Request setCacheTimeout(int cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
        return this;
    }

    /**
     * 设置同步/异步请求
     * @param isAsynchronized
     * @return
     */
    public Request setAsynchronized(boolean isAsynchronized) {
        this.isAsynchronized = isAsynchronized;
        return this;
    }

    /**
     * 设置post方式提交时，内容是否需要做urlencode
     * @param isEncodePostContent
     * @return
     */
    public Request setEncodePostContent(boolean isEncodePostContent) {
        this.isEncodePostContent = isEncodePostContent;
        return this;
    }
}
