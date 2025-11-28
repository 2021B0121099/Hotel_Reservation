package UI;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.*;

public class AdminMenu {

	private static final AdminResource adminResource = AdminResource.getInstance();
	private static final Scanner scanner = new Scanner(System.in);

	public static void displayAdminMenu() {
		String option;
		do {
			System.out.println("\nAdmin Menu");
			System.out.println("------------------------------------------");
			System.out.println("1. See all Customers");
			System.out.println("2. See all Rooms");
			System.out.println("3. See all Reservations");
			System.out.println("4. Add a Room");
			System.out.println("5. Add Test Data");
			System.out.println("6. Back to Main Menu");
			System.out.println("------------------------------------------");
			System.out.print("Please select a number for the menu option:\n");

			option = scanner.nextLine();

			switch (option) {
			case "1":
				seeAllCustomers();
				break;
			case "2":
				seeAllRooms();
				break;
			case "3":
				adminResource.displayAllReservations();
				break;
			case "4":
				addRoom();
				break;
			case "5":
				System.out.println("Add Test Data functionality not implemented yet.");
				break;
			case "6":
                // Fix for the Admin Menu Loop: return to exit this function and go back to MainMenu loop
				return; 
			default:
				System.out.println("Invalid option. Please try again.");
			}
		} while (!option.equals("6"));
	}

	private static void seeAllCustomers() {
		Collection<Customer> customers = adminResource.getAllCustomers();
		if (customers.isEmpty()) {
			System.out.println("No customers found.");
		} else {
			customers.forEach(System.out::println);
		}
	}

	private static void seeAllRooms() {
		Collection<IRoom> rooms = adminResource.getAllRooms();
		if (rooms.isEmpty()) {
			System.out.println("No rooms found.");
		} else {
			rooms.forEach(System.out::println);
		}
	}

	private static void addRoom() {
	    List<IRoom> newRooms = new ArrayList<>();
	    String addMore="y"; // Correctly initialized
        
	    do {
            // --- 1. Get Room Number, Handle Empty/Null, and CHECK FOR DUPLICATES ---
            String roomNumber = null;
            boolean validRoomNumber = false;
            
            // Loop to ensure the room number is valid, non-empty, and non-duplicate
            while (!validRoomNumber) {
                System.out.print("Enter room number:\n");
                roomNumber = scanner.nextLine();
                
                // Handle Empty/Null input gracefully
                if (roomNumber == null || roomNumber.trim().isEmpty()) {
                    System.out.println("Room number cannot be empty or null. Please enter a value.");
                    continue; // Re-prompt
                }
                
                // *** NEW FUNCTIONALITY: CHECK FOR DUPLICATE ROOM ***
                // This assumes adminResource.getRoom(roomNumber) is implemented
                if (adminResource.getRoom(roomNumber) != null) {
                    System.out.println("Room already exists!");
                    continue; // Re-prompt for a new room number
                }
                
                validRoomNumber = true; // Input is valid and unique
            }
            
	        // --- 2. Get and Validate Room Price ---
	        double price = -1; 
	        boolean validPrice = false;
	        while (!validPrice) {
	            System.out.print("Enter room price per night:\n");
                String priceInput = scanner.nextLine(); // Read input as string first
                
                // Handle empty input explicitly
                if (priceInput.trim().isEmpty()) { 
                    System.out.println("Price cannot be empty. Please enter a valid number.");
                    continue;
                }
                
	            try {
	                // Try to parse the input as a double
	                price = Double.parseDouble(priceInput);
	                if (price < 0) {
	                    System.out.println("Price cannot be negative. Please enter a non-negative value.");
	                } else {
	                    validPrice = true; // Input is valid (is a number and non-negative)
	                }
	            } catch (NumberFormatException e) {
	                // Catch the exception if input is not a valid number
	                System.out.println("Invalid input. Please enter a valid number for the price.");
	            }
	        }

	        // --- 3. Get and Validate Room Type ---
	        RoomType roomType = null;
	        boolean validType = false;
	        while (!validType) {
	            System.out.print("Enter room type: 1 for single bed, 2 for double bed:\n");
	            String typeInput = scanner.nextLine();
	            if (typeInput.equals("1")) {
	                roomType = RoomType.SINGLE;
	                validType = true;
	            } else if (typeInput.equals("2")) {
	                roomType = RoomType.DOUBLE;
	                validType = true;
	            } else {
	                // Corrected error message
	                System.out.println("Invalid! Enter valid room type (1 for SINGLE, 2 for DOUBLE)");
	            }
	        }

	        // --- 4. Create the room with final Exception Handling ---
            // Wrap room creation in try-catch to prevent crash from IllegalArgumentException (e.g., negative price check in Room constructor)
            try {
                IRoom room = new Room(roomNumber, price, roomType);
                newRooms.add(room);
                System.out.println("Room added successfully!");
            } catch (IllegalArgumentException e) {
                 System.out.println("Error adding room: " + e.getMessage());
            }

	        // --- 5. Get and Validate "Add More" Prompt ---
	        do {
	            System.out.print("Would you like to add another room? (y/n):\n");
	            addMore = scanner.nextLine().toLowerCase();
	        } while (!addMore.equals("y") && !addMore.equals("n"));

	    } while (addMore.equals("y"));

	    adminResource.addRoom(newRooms);
	    
	}
}