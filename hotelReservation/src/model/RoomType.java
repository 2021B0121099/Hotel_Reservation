// Inside file: RoomType.java

package model;

public enum RoomType {
    // Defines the display name
    SINGLE("Single bed"), 
    DOUBLE("Double bed");

    private final String displayValue;

    // Constructor
    RoomType(String displayValue) {
        this.displayValue = displayValue;
    }

    // Method for displaying the name
    public String getDisplayValue() {
        return displayValue;
    }
}