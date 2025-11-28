package model;

import java.util.Date;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Reservation {
	private static final SimpleDateFormat DATE_FORMAT = 
	        new SimpleDateFormat("E MMM dd yyyy", Locale.US);
    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
         if (checkOutDate.before(checkInDate) || checkOutDate.equals(checkInDate)) {
             throw new IllegalArgumentException("Check-out date must be strictly after Check-in date.");
         }
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public final Customer getCustomer() {
        return customer;
    }

    public final IRoom getRoom() {
        return room;
    }

    public final Date getCheckInDate() {
        return checkInDate;
    }

    public final Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        // Price formatting (using $%.1f for single decimal, assuming previous change)
        String priceDisplay = room.isFree() ? "Free" : String.format("$%.1f price per night", room.getRoomPrice());
        
        // --- COMPLETE REWRITE OF toString() TO MATCH SCREENSHOT FORMAT ---
        return String.format(
            "Reservation\n" +
            // Customer Name
            "%s %s\n" +
            // Room Info (assuming Room.toString() was updated for Single bed)
            "Room: %s - %s\n" +
            // Price Info
            "Price: %s\n" +
            // Formatted Dates
            "Checkin Date: %s\n" +
            "Checkout Date: %s",
            
            customer.getFirstName(), customer.getLastName(),
            room.getRoomNumber(), 
            // This relies on Room's toString() or another method to correctly output "Single bed" or "Double bed"
            // If RoomType.getDisplayValue() is available, use it, otherwise use the enum name as a fallback:
            getRoomTypeDisplay(room), 
            priceDisplay,
            DATE_FORMAT.format(checkInDate),
            DATE_FORMAT.format(checkOutDate)
        );
    }
    
 // Helper method to get the room type display name without modifying IRoom/RoomType unnecessarily
    private String getRoomTypeDisplay(IRoom room) {
        if (room.getRoomType() != null) {
            return switch (room.getRoomType()) {
                case SINGLE -> "Single bed";
                case DOUBLE -> "Double bed";
            };
        }
        return "N/A";
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reservation other = (Reservation) obj;
        return Objects.equals(customer, other.customer) && 
               Objects.equals(room, other.room) && 
               Objects.equals(checkInDate, other.checkInDate) && 
               Objects.equals(checkOutDate, other.checkOutDate);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(customer, room, checkInDate, checkOutDate);
    }
}