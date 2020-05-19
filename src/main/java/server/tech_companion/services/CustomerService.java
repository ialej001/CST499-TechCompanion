package server.tech_companion.services;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
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

    public Customer upsertCustomer(Customer customer) {
        if (customer.get_id() == null) {
        	copyNonNullProperties(new Customer(), customer);
        	ObjectId newId = ObjectId.get();
            customer.set_id(newId);
            customer.setString_id(newId.toString());
            customer.setServiceAddress(
    				customer.getStreetAddress() + " " + 
    				customer.getCity() + ", CA " + 
    				customer.getZipCode());
            customerRepo.save(customer);
            return customer;
        } else {
        	Customer updateCustomer = customerRepo.findBy_id(
        			new ObjectId(customer.getString_id()));
        	copyNonNullProperties(customer, updateCustomer);
        	customerRepo.save(updateCustomer);
        	return updateCustomer;
        }
    }

    @GraphQLQuery(name = "customers")
    public List<Customer> getCustomers() {
        return customerRepo.findAll();
    }
    
    // working 5/18
    public void deleteCustomer(ObjectId _id) {
    	customerRepo.delete(customerRepo.findBy_id(_id));
    }
    
    // some helper methods - code refactored
    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}

