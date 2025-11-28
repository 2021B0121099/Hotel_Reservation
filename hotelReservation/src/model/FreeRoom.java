package model;


public class FreeRoom extends Room {

    // THIS CONSTRUCTOR IS NOW CORRECTLY MATCHING the Room constructor
    public FreeRoom(String roomNumber, RoomType roomType) {
        super(roomNumber, 0.0, roomType); 
    }

    @Override
    public String toString() {
        return String.format("FREE ROOM: %s (Type: %s)", getRoomNumber(), getRoomType().name());
    }
}