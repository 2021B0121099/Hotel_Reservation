package model;

import java.util.Objects;


public class Room implements IRoom {

    private final String roomNumber;
    private final Double price;
    private final RoomType roomType;

    public Room(String roomNumber, Double price, RoomType roomType) {
    	// --- START: NEW VALIDATION LOGIC ---
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be empty or null.");
        }
        // --- END: NEW VALIDATION LOGIC ---
        if (price < 0) {
            throw new IllegalArgumentException("Room price cannot be negative.");
        }
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    @Override
    public final String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public final Double getRoomPrice() {
        return price;
    }

    @Override
    public final RoomType getRoomType() {
        return roomType;
    }

    @Override
    public final boolean isFree() {
        // Use equals for double comparison
        return price.equals(0.0);
    }

    @Override
    public String toString() {
        String displayPrice = (isFree()) ? "Free" : String.format("$%.1f", price);
        

        return String.format("Room Number: %s  %s Room Price: %s", roomNumber, roomType.getDisplayValue(), displayPrice);
    }
    
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room other = (Room) obj;
        return Objects.equals(roomNumber, other.roomNumber);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(roomNumber);
    }
}