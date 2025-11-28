package api;

import model.Customer;
import model.IRoom;
import model.Reservation; // Assuming this is needed for bookARoom
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class HotelResource {

    // Static reference
    private static final HotelResource instance = new HotelResource();

    private static final CustomerService customerService = CustomerService.getInstance();
    private static final ReservationService reservationService = ReservationService.getInstance();

    private HotelResource() {
    }

    public static HotelResource getInstance() {
        return instance;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

   
    public IRoom getRoom(String roomNumber) {
        return reservationService.getRoom(roomNumber);
    }
    
    
    public void addRoom(IRoom room) {
        reservationService.addRoom(room);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = getCustomer(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found.");
        }
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = getCustomer(customerEmail);
        if (customer == null) {
             return Collections.emptyList(); // Return empty list instead of crashing
        }
        return reservationService.getCustomersReservation(customer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }
    
   
}