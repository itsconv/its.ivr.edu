package com.itsconv.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.itsconv.config.DBConnector;
import com.itsconv.model.Customer;

public class CustomerRepository {

    private static final String FIND_BY_PHONE_SQL =
            "SELECT CU_CO_NAME " +
            "FROM CUST_INFO " +
            "WHERE PHONE_NO = ?";

    public Customer findByPhone(String phoneNumber) throws SQLException {

        try (
            Connection connection = DBConnector.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(FIND_BY_PHONE_SQL)
        ) {
            statement.setString(1, phoneNumber);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    String customerName =
                            resultSet.getString("CU_CO_NAME");

                    // 등록 고객
                    return new Customer(customerName);
                }

                // 미등록 고객
                return null;
            }
        }
    }
}
