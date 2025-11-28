package model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a hotel customer account. 
 * Email is used as the unique identifier and validated via RegEx.
 */
public class Customer {

    private final String firstName;
    private final String lastName;
    private final String email;

    private static final String EMAIL_VALIDATION_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public Customer(String firstName, String lastName, String email) {
        if (!Pattern.matches(EMAIL_VALIDATION_REGEX, email)) {
            throw new IllegalArgumentException("Invalid email format: " + email + ". Must be name@domain.ext");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public final String getFirstName() {
        return firstName;
    }

    public final String getLastName() {
        return lastName;
    }

    public final String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        // Updated to match the "First Name: ... Last Name: ... Email: ..." format
        return String.format("First Name: %s Last Name: %s Email: %s", firstName, lastName, email);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return Objects.equals(email, customer.email);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(email);
    }
}