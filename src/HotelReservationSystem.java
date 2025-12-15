import java.lang.Exception;
import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/Hotel_DB";
    private static final String username = "root";
    private static final String password = "Viru@123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Scanner sc = new Scanner(System.in);
            while(true) {
                System.out.println();

                System.out.println("!!...HOTEL MANAGEMENT SYSTEM...!!");
                System.out.println();
                System.out.println("1.  Reserve a Room\n2.  View Reservations\n3.  Get room Number\n4.  Update Reservation\n5.  Delete Reservation\n6.  Exit");
                System.out.print("Select the Option : ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        reserveRoom(connection, sc);
                        break;

                    case 2:
                        viewReservations(connection, sc);
                        break;

                    case 3:
                        getRoomNumber(connection, sc);
                        break;

                    case 4:
                        updateReservation(connection, sc);
                        break;

                    case 5:
                        deleteReservation(connection, sc);
                        break;

                    case 6:
                        exit();

                    default:
                        System.out.println("Invalid choice... try again");
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
            catch(InterruptedException e) {
                throw new RuntimeException(e);
        }
    }
    private static void reserveRoom(Connection connection, Scanner sc) throws SQLException {
        try{
            Statement statement = connection.createStatement();

            System.out.println("Enter guest_name : ");
            sc.nextLine();
            String guestName = sc.nextLine();
            System.out.println("Enter room_number : ");
            int roomNumber = sc.nextInt();
            System.out.println("Enter contact_number : ");
            String contactNumber = sc.next();

            String query = "INSERT INTO reservations(guest_name, room_number, contact_number) VALUES ('"
                    + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

            int rowsaffected = statement.executeUpdate(query);

            if(rowsaffected > 0) {
                System.out.println("INFO inserted Successfully...");
            } else {
                System.out.println("INFO not inserted...");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private static void viewReservations(Connection connection,Scanner sc) throws SQLException {
        try{
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM reservations";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("Current Reservations ");
            System.out.println();

            while(resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n", reservationId, guestName, roomNumber, contactNumber, reservationDate);
                System.out.println();
            }


        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private static void getRoomNumber(Connection connection,Scanner sc) {
        try{
            Statement statement = connection.createStatement();

            System.out.println();
            System.out.print("Enter the guest ID : ");
            int guestId = sc.nextInt();

            System.out.println();
            System.out.print("Enter the guest Name : ");
            sc.nextLine();
            String guestName = sc.nextLine();


            String query = "SELECT room_number FROM reservations WHERE reservation_id = "+guestId+" AND guest_name = '"+guestName+"'";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                int roomNumber = resultSet.getInt("room_number");
                System.out.println("Room number is : "+roomNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection connection,Scanner sc) throws SQLException {
        try{
            Statement statement = connection.createStatement();

            System.out.println();
            System.out.print("Enter the id for Updation : ");
            int guestId = sc.nextInt();

            if(!reservationExist(connection,guestId)) {
                System.out.println("Entered ID does not Exist...");
                return;
            }
            System.out.println();
            System.out.print("Enter new guest name : ");
            sc.nextLine();
            String newGuestName = sc.nextLine();
            System.out.println();
            System.out.print("Enter new room number : ");
            int newRoomNumber = sc.nextInt();
            System.out.println();
            System.out.print("Enter new contact number : ");
            String newContactNumber = sc.next();

            String query = "UPDATE reservations SET guest_name = '" + newGuestName +
                    "', room_number = " + newRoomNumber +
                    ", contact_number = '" + newContactNumber +
                    "' WHERE reservation_id = " + guestId;


            int rowaffected = statement.executeUpdate(query);

            if(rowaffected > 0) {
                System.out.println("Data updated successfully...");
            } else {
                System.out.println("Reservation update failed...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void deleteReservation(Connection connection,Scanner sc) throws SQLException {
        try{
            Statement statement = connection.createStatement();
            System.out.println();
            System.out.print("Enter guest Id for delete : ");
            int guestID = sc.nextInt();

            if(!reservationExist(connection,guestID)) {
                System.out.println("Entered ID does not Exist...");
                return;
            }

            String query = "DELETE FROM reservations WHERE reservation_id = "+guestID;

            int rowsaffected = statement.executeUpdate(query);

            if(rowsaffected > 0) {
                System.out.println("ID "+guestID+" deleted Successfully...");
            } else {
                System.out.println("ID deletion failed...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean reservationExist(Connection connection, int reservationID) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT reservation_id FROM reservations WHERE reservation_id =" + reservationID;
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void exit() throws InterruptedException {
        System.out.print("Exiting From System");
        int i = 5;
        while(i != 0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using Hotel Management System.");
        System.exit(0);
    }
}
