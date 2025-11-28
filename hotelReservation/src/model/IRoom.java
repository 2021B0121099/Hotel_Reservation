package model;


public interface IRoom {
    // Removed 'final' from methods as it is redundant/non-standard in interfaces
    public String getRoomNumber();

    public Double getRoomPrice();

    public RoomType getRoomType();

    public boolean isFree();
}