package com.itsconv.repository;

import java.sql.SQLException;

import com.itsconv.model.Customer;

public class CustomerRepositoryTest {

    public static void main(String[] args) {

        CustomerRepository repository = new CustomerRepository();

        testCustomer(repository, "01012345678");
        testCustomer(repository, "01023456789");
        testCustomer(repository, "01099998888");
    }

    private static void testCustomer(
            CustomerRepository repository,
            String phoneNumber
    ) {
        System.out.println("--------------------------------");
        System.out.println("조회 전화번호: " + phoneNumber);

        try {
            Customer customer = repository.findByPhone(phoneNumber);

            if (customer != null) {
                System.out.println("등록 고객입니다.");
                System.out.println("고객명: "
                        + customer.getCustomerName());
            } else {
                System.out.println("등록되지 않은 고객입니다.");
            }

        } catch (SQLException e) {
            System.out.println("DB 조회 중 오류가 발생했습니다.");
            System.out.println("오류 메시지: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
