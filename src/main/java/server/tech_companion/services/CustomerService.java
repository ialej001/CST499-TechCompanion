package server.tech_companion.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

import server.tech_companion.models.Customer;
import server.tech_companion.repositories.CustomerRepository;

@Service
@Component
@GraphQLApi
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepo = customerRepository;
    }

    public Customer fetchCustomerByServiceAddress(String serviceAddress) {
        return customerRepo.findByServiceAddress(serviceAddress);
    }

    @GraphQLQuery(name = "customer")
    public List<Customer> fetchCustomerByStreetAddress(@GraphQLArgument(name = "streetAddress") String streetAddress) {
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

    @GraphQLQuery(name = "customers")
    public List<Customer> getCustomers() {
        return customerRepo.findAll();
    }
}

