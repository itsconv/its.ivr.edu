package com.itsconv.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final String URL =
            "jdbc:mariadb://192.168.40.200:3306/ivr";
    private static final String USER = "root";
    private static final String PASSWORD = "ivr123!";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MariaDB JDBC Driver를 찾을 수 없습니다.",
                    e
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("DB 연결 성공!");
        } catch (SQLException e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}