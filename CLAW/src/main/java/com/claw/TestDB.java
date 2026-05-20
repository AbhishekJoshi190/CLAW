package com.claw;

import com.claw.util.DBUtil;
import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        System.out.println("Testing DB Connection...");
        try {
            Connection conn = DBUtil.getConnection();
            if (conn != null) {
                System.out.println("Connection Successful!");
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}
