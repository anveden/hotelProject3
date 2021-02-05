package com.company;

import java.beans.XMLEncoder;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import java.util.*;

public class Booking {


    public static void upgradeGuest() throws SQLException {
        Scanner input = new Scanner(System.in);
        localSQL.ListTable("customers");
        System.out.print("Enter guest ID:");
        int customerID = input.nextInt();
        localSQL.ListTable("rooms");
        System.out.print("Enter new room number: ");
        int roomNumber = input.nextInt();
        ResultSet res1 = localSQL.sqlStatement.executeQuery("SELECT * FROM hotelproject.rooms WHERE RoomNumber=" + roomNumber + ";");
        int availability = SQLmethods.getColumnValue(res1, "rooms.Availability");
        ResultSet res = localSQL.sqlStatement.executeQuery("SELECT * FROM hotelproject.rooms \n" +
                "JOIN customer_room on customer_room.RoomNumber = rooms.RoomNumber\n" +
                "JOIN customers on customers.CustomerID = customer_room.CustomerID\n" +
                "WHERE customers.CustomerID=" + customerID + "");
        int oldRoomNumber = SQLmethods.getColumnValue(res, "rooms.RoomNumber");
        if (availability == 1) {
            localSQL.sqlStatement.executeUpdate("UPDATE `hotelproject`.`rooms` SET`Availability` = 0 WHERE `RoomNumber` = " + roomNumber + ";");
            localSQL.sqlStatement.executeUpdate("UPDATE `hotelproject`.`rooms` SET`Availability` = 1 WHERE `RoomNumber` = " + oldRoomNumber + ";");
            localSQL.sqlStatement.executeUpdate("UPDATE `hotelproject`.`customer_room` SET`RoomNumber` = " + roomNumber + " WHERE `RoomNumber` = " + oldRoomNumber + ";");
            System.out.println("Customer with ID " + customerID + " has successfully been rebooked from room number " + oldRoomNumber + " to room number " + roomNumber);
        } else {
            System.out.println("Room not available");
        }
    }

    public static void sendFoodOrder(int order, int customerID) throws IOException {
        FileWriter fw = new FileWriter(customerID + ".txt", true);
        fw.write(String.valueOf(order));
        fw.write(System.lineSeparator());
        fw.close();
    }

    public static void checkIn(int customerID, int roomNumber) throws SQLException {
        Scanner input = new Scanner(System.in);

        boolean runtime = true;
        while (runtime) {
            try {
                try {
                    try {
                        System.out.print("Choose checkout time(YYYY-MM-DD): ");
                        String checkOutDate = input.nextLine();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate date = LocalDate.parse(checkOutDate, dtf);
                        LocalDate now = LocalDate.now();
                        int hotelNights = (int) now.until(date, ChronoUnit.DAYS);
                        try {
                            FileWriter fw = new FileWriter(customerID + ".txt");
                            fw.write(String.valueOf(hotelNights));
                            fw.write(System.lineSeparator());

                            fw.close();


                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        if (hotelNights > 0) {
                            int customerRoomID;
                            ResultSet res = localSQL.sqlStatement.executeQuery("SELECT CustomerRoomID FROM hotelproject.customer_room WHERE CustomerID=" + customerID + " and RoomNumber=" + roomNumber + "");
                            customerRoomID = SQLmethods.getColumnValue(res, "customer_room.CustomerRoomID");

                            localSQL.sqlStatement.executeUpdate("INSERT INTO `hotelproject`.`booking`\n" +
                                    "(`CheckedIn`,\n" +
                                    "`CheckedOut`,\n" +
                                    "`CustomerRoomID`)\n" +
                                    "VALUES\n" +
                                    "('" + dtf.format(now) + "', '" + checkOutDate + "', " + customerRoomID + ");");
                            System.out.println("You have booked " + hotelNights + " nights.");
                            runtime = false;
                        } else {
                            System.out.println("Incorrect date. Must be at least 1 night from todays date.");
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Incorrect input. Format must be (YYYY-MM-DD)");
                    }
                } catch (SQLException var2) {
                    var2.printStackTrace();
                }
            } catch (
                    InputMismatchException e) {
                input.next();
            }
        }

    }

    public static void checkOut() throws SQLException {

        //add OrderID
        //add writeReceipt method

        SQLmethods.ListTableBookings();
        Scanner input = new Scanner(System.in);
        try {

            System.out.print("Enter booking ID for checkout:");
            int bookingID = input.nextInt();
            ResultSet result = localSQL.sqlStatement.executeQuery("SELECT * FROM hotelproject.booking WHERE BookingID=" + bookingID + ";");
            int customerRoomID = SQLmethods.getColumnValue(result, "booking.CustomerRoomID");

            ResultSet result1 = localSQL.sqlStatement.executeQuery("SELECT * FROM hotelproject.customer_room WHERE CustomerRoomID=" + customerRoomID + ";");
            int customerID = SQLmethods.getColumnValue(result1, "customer_room.CustomerID");

            ResultSet result2 = localSQL.sqlStatement.executeQuery("SELECT * FROM hotelproject.customer_room WHERE CustomerRoomID=" + customerRoomID + ";");
            int roomNumber = SQLmethods.getColumnValue(result2, "customer_room.RoomNumber");

            ResultSet result3 = localSQL.sqlStatement.executeQuery("SELECT * FROM hotelproject.rooms WHERE RoomNumber=" + roomNumber + ";");
            int price = SQLmethods.getColumnValue(result3, "rooms.Price");

            System.out.println("Guest with ID " + customerID + " is checking out from room " + roomNumber);
            localSQL.sqlStatement.executeUpdate("UPDATE `hotelproject`.`rooms` SET`Availability` = 1 WHERE `RoomNumber` = " + roomNumber + ";");

            LinkedList<Integer> recList = new LinkedList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(customerID + ".txt"));
                int FileHotelNights = Integer.parseInt(br.readLine());

                String FileFood = br.readLine();
                while (FileFood != null) {
                    try {
                        recList.add(Integer.parseInt(FileFood));
                        FileFood = br.readLine();
                        recList.add(Integer.parseInt(FileFood));
                    } catch (NumberFormatException e) {
                        System.out.println("");
                    }
                }
                System.out.println("");
                int FileFoodSum = sum(recList);
                int total = (price * FileHotelNights) + FileFoodSum;
                br.close();
                System.out.println("--------RECEIPT--------");
                System.out.println("Room price: " + price + "kr\nNights stayed: " + FileHotelNights + "\nFood orders: " + FileFoodSum + "kr\n-----------------------\nSum: " + total + "kr\n");
            } catch (FileNotFoundException e) {
                System.err.println("Receipt could not be printed");
            }
            localSQL.sqlStatement.executeUpdate("Delete from customers where CustomerID=" + customerID + ";");
            localSQL.sqlStatement.executeUpdate("Delete from customer_room where CustomerRoomID=" + customerRoomID + ";");
            localSQL.sqlStatement.executeUpdate("Delete from booking where BookingID=" + bookingID + ";");
            System.out.println("Check out complete. Have a nice day!");
        } catch (
                InputMismatchException e) {
            System.out.println("Only numbers are allowed...");
            input.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int sum(List<Integer> list) {
        int sum = 0;
        for (int i : list) {
            sum += i;
        }
        return sum;
    }
}