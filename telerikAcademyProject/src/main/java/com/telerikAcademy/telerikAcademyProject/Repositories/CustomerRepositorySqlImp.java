package com.telerikAcademy.telerikAcademyProject.Repositories;

import com.telerikAcademy.telerikAcademyProject.Models.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepositorySqlImp implements CustomerRepository{
    private SessionFactory sessionFactory;

    public CustomerRepositorySqlImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addCustomer(Customer customer) {
        try(Session session = sessionFactory.openSession()) {
            session.save(customer);
        }

    }

    @Override
    public Customer getCustomerById(int id) {
        try(Session session = sessionFactory.openSession()) {
            return session.get(Customer.class,id);
        }
    }

    @Override
    public void updateCustomerEmailById(int id, String email) throws Exception {
        try(Session session = sessionFactory.openSession()){
            Customer customer = session.get(Customer.class, id);
            if(customer!=null){
                customer.setEmail(email);
                session.update(customer);
            }else {
                throw new Exception("Customer not exist");
            }
        }
    }

    @Override
    public void deleteCustomerById(int id) throws Exception {
        try(Session session = sessionFactory.openSession()){
            Customer customer = session.get(Customer.class, id);
            if(customer!=null){
                session.delete(customer);
            }else {
                throw new Exception("Customer not exist");
            }
        }
    }
}
