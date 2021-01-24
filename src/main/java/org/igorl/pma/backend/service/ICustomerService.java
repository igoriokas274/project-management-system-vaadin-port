package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Customer;

import java.util.List;

public interface ICustomerService {

    List<Customer> findAll();
    void save(Customer theCustomer);
    void deleteById(long theId);
}
