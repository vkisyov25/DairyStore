package com.telerikAcademy.telerikAcademyProject.Services;

import com.telerikAcademy.telerikAcademyProject.Models.Customer;
import com.telerikAcademy.telerikAcademyProject.Repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{
    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void addCustomer(Customer customer) {
        customerRepository.addCustomer(customer);
    }

    @Override
    public Customer getCustomerById(int id) {
        return customerRepository.getCustomerById(id);
    }

    @Override
    public void updateCustomerEmailById(int id, String email) throws Exception {
        customerRepository.updateCustomerEmailById(id,email);
    }

    @Override
    public void deleteCustomerById(int id) throws Exception {
        customerRepository.deleteCustomerById(id);
    }
}
