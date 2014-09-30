/*
 * Copyright (c) 2012, Converger Co.,ltd.
 * All rights reserved.
 *
 * Created by Broche on 9/30/14 11:10 AM
 */

package com.xxworkshop.dao;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by brochexu on 9/30/14.
 */
public interface DataManager {
    public List<Hashtable<String, Object>> query(String sql);

    public Hashtable<String, Object> fetch(String sql);

    public Object scalar(String sql);
}
