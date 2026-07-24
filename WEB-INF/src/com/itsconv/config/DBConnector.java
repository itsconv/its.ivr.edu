package com.itsconv.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    // DB 접속 정보
    private static final String URL = "jdbc:mariadb://192.168.40.200:3306/ivr";
    private static final String USER = "root";
    private static final String PASSWORD = "ivr123!";

    /**
     * DB 연결 반환
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {

        try {
            // MariaDB JDBC Driver 로드
            Class.forName("org.mariadb.jdbc.Driver");

            Connection conn = getConnection();

            if (conn != null) {
                System.out.println("DB 연결 성공!");
                conn.close();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MariaDB JDBC Driver를 찾을 수 없습니다.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("DB 연결 실패");
            e.printStackTrace();
        }
    }
}
