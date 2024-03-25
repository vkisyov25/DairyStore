package com.telerikAcademy.telerikAcademyProject.Services;

import com.telerikAcademy.telerikAcademyProject.Models.Customer;

public interface CustomerService {
    public void addCustomer(Customer customer);
    public Customer getCustomerById(int id);
    void updateCustomerEmailById(int id, String email) throws Exception;
    public void deleteCustomerById(int id) throws Exception;
}
