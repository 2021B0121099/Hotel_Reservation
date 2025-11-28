package service;

import model.Customer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {

	private static Map<String, Customer> customers = new HashMap<>();

	//Static reference
	private static final CustomerService instance = new CustomerService();

	private CustomerService() {
	}

	public static CustomerService getInstance() {
		return instance;
	}

	public void addCustomer(String email, String firstName, String lastName) {
		Customer customer = new Customer(firstName, lastName, email);
		customers.put(email, customer);
	}

	public Customer getCustomer(String customerEmail) {
		return customers.get(customerEmail);
	}

	public Collection<Customer> getAllCustomers() {
		return customers.values();
	}
}
