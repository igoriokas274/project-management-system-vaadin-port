package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Customer;
import org.igorl.pma.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository theCustomerRepository) {
        customerRepository = theCustomerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findAll(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return customerRepository.findAll();
        } else {
            return customerRepository.search(searchTerm);
        }
    }

    @Override
    @Transactional
    public void save(Customer theCustomer) {
        customerRepository.save(theCustomer);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        customerRepository.deleteById(theId);
    }
}
