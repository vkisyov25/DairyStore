package com.telerikAcademy.telerikAcademyProject.Repositories;

import com.telerikAcademy.telerikAcademyProject.Models.Customer;

public interface CustomerRepository {
    public void addCustomer(Customer customer);
    public Customer getCustomerById(int id);

    void updateCustomerEmailById(int id, String email) throws Exception;

    public void deleteCustomerById(int id) throws Exception;
}
