import java.io.*;
import java.util.*;

class Room {
    String type;
    int price;
    boolean available;

    Room(String type, int price) {
        this.type = type;
        this.price = price;
        this.available = true;
    }
}

public class CodeAlpha_HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static final String FILE_NAME = "bookings.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Add rooms
        rooms.add(new Room("Standard", 1000));
        rooms.add(new Room("Deluxe", 2000));
        rooms.add(new Room("Suite", 3000));

        while (true) {
            System.out.println("\n--- HOTEL RESERVATION SYSTEM ---");
            System.out.println("1. View Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter a valid number!");
                continue;
            }

            switch (choice) {
                case 1:
                    viewRooms();
                    break;
                case 2:
                    bookRoom(sc);
                    break;
                case 3:
                    cancelBooking(sc);
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    System.out.println("Thank you! Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // 🔹 VIEW ROOMS
    static void viewRooms() {
        System.out.println("\n--- ROOM STATUS ---");
        for (Room r : rooms) {
            System.out.println(r.type + " - ₹" + r.price + " - " + (r.available ? "Available" : "Booked"));
        }
    }

    // 🔹 BOOK ROOM (FIXED UX 🔥)
    static void bookRoom(Scanner sc) {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.println("\nAvailable Room Types:");
        System.out.println("1. Standard");
        System.out.println("2. Deluxe");
        System.out.println("3. Suite");
        System.out.print("Enter your choice (1-3): ");

        int typeChoice;

        try {
            typeChoice = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input!");
            return;
        }

        String selectedType = "";

        switch (typeChoice) {
            case 1:
                selectedType = "Standard";
                break;
            case 2:
                selectedType = "Deluxe";
                break;
            case 3:
                selectedType = "Suite";
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        for (Room r : rooms) {
            if (r.type.equalsIgnoreCase(selectedType) && r.available) {
                r.available = false;

                try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
                    fw.write(name + "," + selectedType + ",Paid\n");
                } catch (IOException e) {
                    System.out.println("Error saving booking!");
                }

                System.out.println("Room booked successfully!");
                return;
            }
        }

        System.out.println("Room not available!");
    }

    // 🔹 CANCEL BOOKING
    static void cancelBooking(Scanner sc) {
        System.out.print("Enter your name to cancel booking: ");
        String name = sc.nextLine();

        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp.txt");

        boolean found = false;

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            FileWriter fw = new FileWriter(tempFile)
        ) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith(name + ",")) {
                    found = true;

                    String[] parts = line.split(",");
                    for (Room r : rooms) {
                        if (r.type.equalsIgnoreCase(parts[1])) {
                            r.available = true;
                        }
                    }
                } else {
                    fw.write(line + "\n");
                }
            }

        } catch (IOException e) {
            System.out.println("Error processing file!");
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        if (found) {
            System.out.println("Booking cancelled successfully!");
        } else {
            System.out.println("No booking found!");
        }
    }

    // 🔹 VIEW BOOKINGS (FIXED MESSAGE 🔥)
    static void viewBookings() {
        boolean isEmpty = true;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            System.out.println("\n--- BOOKINGS ---");

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                isEmpty = false;
            }

            if (isEmpty) {
                System.out.println("No bookings found.");
            }

        } catch (IOException e) {
            System.out.println("No bookings found.");
        }
    }
}