/*
 * Copyright (c) 2012, Converger Co.,ltd.
 * All rights reserved.
 *
 * Created by Broche on 9/30/14 11:13 AM
 */

package com.xxworkshop.dao.datamanager;

import com.xxworkshop.dao.DataManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by brochexu on 9/30/14.
 */
public class MySqlDataManager implements DataManager {
    private final String Driver = "com.mysql.jdbc.Driver";
    private String host;
    private String port;
    private String username;
    private String password;
    private String database;

    public MySqlDataManager(String host, String port, String username, String password, String database) throws ClassNotFoundException {
        Class.forName(Driver);

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    public List<Hashtable<String, Object>> query(String sql) {
        List<Hashtable<String, Object>> results = new ArrayList<Hashtable<String, Object>>();
        try {
            Connection connection = prepareConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Hashtable<String, Object> item = new Hashtable<String, Object>();
                for (int i = 1; i < columnCount + 1; i++) {
                    item.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                results.add(item);
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public Hashtable<String, Object> fetch(String sql) {
        Hashtable<String, Object> result = null;
        try {
            Connection connection = prepareConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                result = new Hashtable<String, Object>();
                for (int i = 1; i < columnCount + 1; i++) {
                    result.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Object scalar(String sql) {
        Object result = null;
        try {
            Connection connection = prepareConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                result = rs.getObject(1);
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Connection prepareConnection() throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }
}
