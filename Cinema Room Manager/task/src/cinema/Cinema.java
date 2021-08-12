package cinema;

public class Cinema {
    public static void main(String[] args) {
        int rows = RoomManager.getInt("Enter the number of rows:");
        int seats = RoomManager.getInt("Enter the number of seats in each row:");
        RoomManager roomManager = new RoomManager(rows, seats);
        roomManager.menu();
    }
}