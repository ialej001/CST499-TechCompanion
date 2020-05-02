package server.tech_companion.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.tech_companion.models.Customer;
import server.tech_companion.repositories.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public Customer fetchCustomerByServiceAddress(String serviceAddress) {
        return customerRepo.findByServiceAddress(serviceAddress);
    }

    public List<Customer> fetchCustomerByStreetAddress(String streetAddress) {
        return customerRepo.findByStreetAddress(streetAddress);
    }

    public void upsertCustomer(Customer customer) {
        if (customer.get_id() == null) {
            customer.set_id(ObjectId.get());
        }
        if (customer.getServiceAddress() == null) {
            customer.setServiceAddress(customer.getStreetAddress() + " " + customer.getCity() + ", CA " + customer.getZipCode());
        }
        customerRepo.save(customer);
    }
}