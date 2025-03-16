package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Config {
    public static final String PROVERKACHEKA_URL = "https://proverkacheka.com/api/v1/check/get";

    public static final String BOT_USERNAME = "";
    public static final String BOT_TOKEN = "";
    public static final String TOKEN_PROVERKACHEKA = "";
    private static final String URL_DB = "";
    private static final String USER_DB = "";
    private static final String PASSWORD_DB = "";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
    }



}