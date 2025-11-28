package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

	// Static reference
	private static final AdminResource instance = new AdminResource();

	private static final CustomerService customerService = CustomerService.getInstance();
	private static final ReservationService reservationService = ReservationService.getInstance();

	private AdminResource() {
	}

	public static AdminResource getInstance() {
		return instance;
	}

	public Customer getCustomer(String email) {
		return customerService.getCustomer(email);
	}


    public IRoom getRoom(String roomNumber) {
        // Delegates the room lookup to the service layer.
        return reservationService.getRoom(roomNumber);
    }
    
	public void addRoom(List<IRoom> rooms) {
		for (IRoom room : rooms) {
			// This call now relies on ReservationService's addRoom to perform the duplicate check
			reservationService.addRoom(room);
		}
	}

	public Collection<IRoom> getAllRooms() {
		return reservationService.getAllRooms();
	}

	public Collection<Customer> getAllCustomers() {
		return customerService.getAllCustomers();
	}

	public void displayAllReservations() {
		reservationService.printAllReservation();
	}
}