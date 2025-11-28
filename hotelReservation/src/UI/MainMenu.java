package UI;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import java.util.Calendar;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainMenu {

    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);
    
    // 1. Correct Date Format (MM/dd/yyyy)
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    
    static {
        dateFormat.setLenient(false);
    }

    public static void main(String[] args) {
        displayMainMenu();
    }

    public static void displayMainMenu() {
        String option;
        do {
            System.out.println("Welcome to the Hotel Reservation Application\n");
            System.out.println("----------------------------------------------");
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an Account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");
            System.out.println("----------------------------------------------");
            System.out.print("Please select a number for the menu option: ");

            option = scanner.nextLine();

            switch (option) {
                case "1":
                    findAndReserveRoom();
                    break;
                case "2":
                    seeMyReservations();
                    break;
                case "3":
                    createAccount();
                    break;
                case "4":
                    AdminMenu.displayAdminMenu(); 
                    break;
                    
                case "5":
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid input. Please select a valid option.");
            }
        } while (!option.equals("5"));
    }

    @SuppressWarnings("unused")
	private static void findAndReserveRoom() {
        Date checkIn = null;
        Date checkOut = null;

        try {
            // --- 1. Get and Validate Check-In Date ---
            while (checkIn == null) {
                System.out.print("Enter CheckIn Date mm/dd/yyyy example 02/01/2020\n");
                String checkInStr = scanner.nextLine();
                try {
                    // Strict parsing: throws ParseException for invalid dates
                    checkIn = dateFormat.parse(checkInStr);
                    
                    // Set time to midnight for comparison (only compare date, not time)
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MILLISECOND, 0);

                    // Validate Check-in Date is not in the past.
                    if (checkIn.before(today.getTime())) {
                        System.out.println("⚠️ Error: Check-in date cannot be in the past. Please enter a future date.");
                        checkIn = null; 
                    }
                } catch (ParseException e) {
                    // This message handles both invalid format and invalid calendar values (due to setLenient(false))
                    System.out.println("❌ Invalid Date Input: Please use the **MM/dd/yyyy** format, and ensure the month (1-12) and day are valid.");
                }
            }

            // --- 2. Get and Validate Check-Out Date ---
            while (checkOut == null) {
                System.out.print("Enter CheckOut Date month/date/year example 2/21/2020\n");
                String checkOutStr = scanner.nextLine();
                try {
                    // Strict parsing: throws ParseException for invalid dates
                    checkOut = dateFormat.parse(checkOutStr);
                    
                    // Validate Check-out Date is strictly after Check-in Date.
                    if (!checkOut.after(checkIn)) {
                        System.out.println("⚠️ Error: Check-out date must be strictly after the Check-in date.");
                        checkOut = null; 
                    }
                } catch (ParseException e) {
                    // This message handles both invalid format and invalid calendar values
                    System.out.println("❌ Invalid Date Input: Please use the **MM/dd/yyyy** format, and ensure the month (1-12) and day are valid.");
                }
            }
            
            // --- 3. Find Available Rooms ---
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);
            Date finalCheckIn = checkIn;
            Date finalCheckOut = checkOut;
            boolean isRecommended = false;

            // --- 4. Handle No Rooms Found (Search Recommended Dates) ---
            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for your dates. Searching for recommended dates...");

                // Calculate new dates by adding 7 days
                Calendar calendar = Calendar.getInstance();
                
                calendar.setTime(checkIn);
                calendar.add(Calendar.DATE, 7);
                Date altCheckIn = calendar.getTime();

                calendar.setTime(checkOut);
                calendar.add(Calendar.DATE, 7);
                Date altCheckOut = calendar.getTime();
                
                // Search again with the alternate dates
                Collection<IRoom> recommendedRooms = hotelResource.findARoom(altCheckIn, altCheckOut);

                if (recommendedRooms.isEmpty()) {
                    System.out.println("No rooms available on alternate dates: " 
                        + dateFormat.format(altCheckIn) + " to " 
                        + dateFormat.format(altCheckOut));
                    return;
                } else {
                    // Rooms found on alternate dates.
                    System.out.println("\nRecommended rooms available on alternative dates:");
                    System.out.println("Check-in: " + dateFormat.format(altCheckIn));
                    System.out.println("Check-out: " + dateFormat.format(altCheckOut));
                    
                    availableRooms = recommendedRooms;
                    // Update the final dates for the reservation below
                    finalCheckIn = altCheckIn;
                    finalCheckOut = altCheckOut;
                    isRecommended = true; 
                }
            }
            
            // --- 5. Display Rooms and Ask to Book ---
            if (!availableRooms.isEmpty()) {
                for (IRoom room : availableRooms) {
                    System.out.println(room);
                }
            } else {
                return; 
            }
            
            // Ask the user if they want to proceed with a reservation
            String bookRoom = "";
            do {
                System.out.print("Would you like to book a room? (y/n)\n");
                bookRoom = scanner.nextLine().toLowerCase();
            } while (!bookRoom.equals("y") && !bookRoom.equals("n"));

            if (bookRoom.equals("n")) {
                return; // User chooses not to book
            }
            
            // --- 6. Handle Account Status ---
            Customer customer = null;
            String email = "";
            
            String hasAccount = "";
            do {
                System.out.print("Do you have an account with us? (y/n)\n");
                hasAccount = scanner.nextLine().toLowerCase();
            } while (!hasAccount.equals("y") && !hasAccount.equals("n"));
            
            if (hasAccount.equals("y")) {
                // If they have an account, prompt for email and load customer
                System.out.print("Enter Email format (name@domain.com):\n");
                email = scanner.nextLine();
                customer = hotelResource.getCustomer(email);

                if (customer == null) {
                    System.out.println("No account found for this email. Please create an account first (Option 3).");
                    return;
                }
            } else {
                // If they don't have an account, suggest creating one
                System.out.println("Please create an account first (Option 3) before booking a room.");
                return;
            }

            // --- 7. Reserve Room ---
            System.out.print("What room number would you like to reserve: ");
            String roomNumber = scanner.nextLine();

            IRoom room = hotelResource.getRoom(roomNumber);
            
            // Check if room exists and if it was actually in the available list for the final dates
            if (room == null) {
                System.out.println("Invalid room number.");
                return;
            }
            
            // Check if the selected room is actually available
            boolean roomIsAvailable = availableRooms.stream().anyMatch(r -> r.getRoomNumber().equals(roomNumber));
            
            if (!roomIsAvailable) {
                System.out.println("The room number you selected is not available for the selected dates.");
                return;
            }

            // Book with final (potentially recommended) dates
            Reservation reservation = hotelResource.bookARoom(email, room, finalCheckIn, finalCheckOut);
            System.out.println("Reservation"); 
            System.out.println(reservation);

        } catch (Exception e) {
            // Catch-all for other errors
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static void seeMyReservations() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);

        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    private static void createAccount() {
        String email, firstName, lastName;
        
        // --- Validate Email ---
        do {
            System.out.print("Enter Email format (name@domain.com):\n");
            email = scanner.nextLine();
            if (email == null || email.trim().isEmpty()) {
                System.out.println("Email cannot be empty. Please try again.");
            }
        } while (email == null || email.trim().isEmpty());

        // --- Validate First Name ---
        do {
            System.out.print("First Name:\n");
            firstName = scanner.nextLine();
            if (firstName == null || firstName.trim().isEmpty()) {
                System.out.println("First Name cannot be empty. Please try again.");
            }
        } while (firstName == null || firstName.trim().isEmpty());

        // --- Validate Last Name ---
        do {
            System.out.print("Last Name:\n");
            lastName = scanner.nextLine();
            if (lastName == null || lastName.trim().isEmpty()) {
                System.out.println("Last Name cannot be empty. Please try again.");
            }
        } while (lastName == null || lastName.trim().isEmpty());

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            // The method inside HotelResource is responsible for printing success/failure
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid email format. Please try again.");
        }
    }
}