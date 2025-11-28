
package Driver;
public class Driver {
    
    public static void main(String[] args) {
        // Must use the full package name to access the Customer class
        System.out.println("--- Test Case 1: Valid Email ---");
        try {
            model.Customer customer = new model.Customer("first", "second", "j@domain.com");
            System.out.println("Customer successfully created:");
            System.out.println(customer); 
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Unexpected exception for valid email.");
            System.err.println(e.getMessage());
        }
        
        System.out.println("\n----------------------------------");
        
        System.out.println("--- Test Case 2: Invalid Email ('email') ---");
        try {
            model.Customer invalidCustomer = new model.Customer("first", "second", "email");
            System.out.println("Customer created unexpectedly: " + invalidCustomer);
        } catch (IllegalArgumentException e) {
            System.out.println("SUCCESS: Caught expected exception for invalid email!");
            System.out.println("Error Message: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR: Caught wrong type of exception: " + e.getClass().getSimpleName());
            System.err.println(e.getMessage());
        }
    }
}