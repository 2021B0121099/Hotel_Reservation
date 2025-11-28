package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {

	private static final Map<String, IRoom> rooms = new HashMap<>();
	private static final Set<Reservation> reservations = new HashSet<>();

	//Static reference
	private static final ReservationService instance = new ReservationService();

	private ReservationService() {
	}

	public static ReservationService getInstance() {
		return instance;
	}

	public void addRoom(IRoom room) {
        // CORRECTION: Enforce duplicate check at the service level.
        if (rooms.containsKey(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room number " + room.getRoomNumber() + " already exists.");
        }
		rooms.put(room.getRoomNumber(), room);
	}

    // CORRECTION: Rename getARoom to getRoom for consistency with API/Resource classes.
	public IRoom getRoom(String roomId) {
		return rooms.get(roomId);
	}

	public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
		Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
		reservations.add(reservation);
		return reservation;
	}

	public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
		List<IRoom> availableRooms = new ArrayList<>(rooms.values());

		for (Reservation reservation : reservations) {
			if (datesOverlap(checkInDate, checkOutDate, reservation.getCheckInDate(), reservation.getCheckOutDate())) {
				availableRooms.remove(reservation.getRoom());
			}
		}

		return availableRooms;
	}

	private boolean datesOverlap(Date start1, Date end1, Date start2, Date end2) {
		return start1.before(end2) && start2.before(end1);
	}

	public Collection<Reservation> getCustomersReservation(Customer customer) {
		List<Reservation> customerReservations = new ArrayList<>();
		for (Reservation reservation : reservations) {
			if (reservation.getCustomer().equals(customer)) {
				customerReservations.add(reservation);
			}
		}
		return customerReservations;
	}
	
	public Collection<IRoom> getAllRooms() {
		return new ArrayList<>(rooms.values());
	}

	public void printAllReservation() {
		if (reservations.isEmpty()) {
			System.out.println("No reservations found.");
		} else {
			for (Reservation reservation : reservations) {
				System.out.println(reservation);
			}
		}
	}
}