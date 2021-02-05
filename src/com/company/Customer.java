package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Customer {

    public static void addCustomer() throws SQLException {
        try {
            Scanner input = new Scanner(System.in);

            try {
                int availability;
                String phoneNumber;
                String fullName;
                int customerID;
                System.out.print("Enter first and last name: ");
                fullName = input.nextLine();
                System.out.print("Enter phonenumber: ");
                phoneNumber = input.nextLine();
                localSQL.ListTable("rooms");
                System.out.print("Enter room number: ");
                int roomNumber = input.nextInt();


                ResultSet res1 = localSQL.sqlStatement.executeQuery("SELECT * FROM hotelproject.rooms WHERE RoomNumber=" + roomNumber + ";");
                availability = SQLmethods.getColumnValue(res1, "rooms.Availability");

                if (availability == 1) {

                    localSQL.sqlStatement.executeUpdate("INSERT INTO hotelproject.customers (CustomerName, PhoneNumber) VALUES ('" + fullName + "', '" + phoneNumber + "');");
                    ResultSet res = localSQL.sqlStatement.executeQuery("SELECT * FROM hotelproject.customers\n" +
                            "WHERE CustomerName='" + fullName + "';");
                    customerID = SQLmethods.getColumnValue(res, "customers.CustomerID");

                    localSQL.sqlStatement.executeUpdate("INSERT INTO `hotelproject`.`customer_room`\n" +
                            "(`CustomerID`,\n" +
                            "`RoomNumber`)\n" +
                            "VALUES\n(" +
                            customerID + ", " +
                            roomNumber + ");");
                    localSQL.sqlStatement.executeUpdate("UPDATE `hotelproject`.`rooms` SET`Availability` = 0 WHERE `RoomNumber` = " + roomNumber + ";");

                    Booking.checkIn(customerID, roomNumber);
                    System.out.println("Check in complete!");

                } else {
                    System.out.println("Room not available...");
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers are allowed...");
                input.next();
            }
        } catch (SQLException var2) {
            var2.printStackTrace();
        }
    }

    public static void searchCustomerByID() throws SQLException {
        Scanner input = new Scanner(System.in);
        try {
            try {

                localSQL.ListTable("customers");
                System.out.print("Enter guest ID: ");
                int id = input.nextInt();
                ResultSet result = localSQL.sqlStatement.executeQuery("SELECT customers.CustomerName, customer_room.RoomNumber, booking.CheckedIn, booking.CheckedOut, booking.BookingID, customers.PhoneNumber FROM hotelproject.booking Right JOIN\n" +
                        "customer_room on customer_room.CustomerRoomID = booking.CustomerRoomID\n" +
                        "Right Join\n" +
                        "customers on customers.CustomerID = customer_room.CustomerID\n" +
                        "WHERE customers.CustomerID=" + id + ";");
                while (result.next()) {
                    if (result.isFirst()) {
                        System.out.println("Name: " + result.getString("customers.CustomerName"));
                        System.out.println("Room number: " + result.getInt("customer_room.RoomNumber"));
                        System.out.println("Phone number: " + result.getString("customers.PhoneNumber"));
                        System.out.println("Check in: " + result.getString("booking.CheckedIn"));
                        System.out.println("Check out: " + result.getString("booking.CheckedOut"));
                        System.out.println("Booking ID: " + result.getInt("booking.BookingID"));
                        //show info of customer from database(tables: customers, booking, orders, customer_room)
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers are allowed...");
                input.next();
            }
        } catch (SQLException var2) {
            var2.printStackTrace();
        }


    }

    public static void searchCustomerByName() throws SQLException {
        //add regex method for searching
        try {
            Scanner input1 = new Scanner(System.in);
            localSQL.ListTable("customers");
            System.out.print("Enter guest name: ");
            String name = input1.nextLine();
            ResultSet result = localSQL.sqlStatement.executeQuery("SELECT customers.CustomerID, customer_room.RoomNumber, booking.CheckedIn, booking.CheckedOut, booking.BookingID, customers.PhoneNumber FROM hotelproject.booking Right JOIN\n" +
                    "customer_room on customer_room.CustomerRoomID = booking.CustomerRoomID\n" +
                    "Right Join\n" +
                    "customers on customers.CustomerID = customer_room.CustomerID\n" +
                    "WHERE customers.CustomerName LIKE'%" + name + "%';");
            while (result.next()) {
                if (result.isFirst()) {
                    System.out.println("ID: " + result.getInt("customers.CustomerID"));
                    System.out.println("Room number: " + result.getInt("customer_room.RoomNumber"));
                    System.out.println("Phone number: " + result.getString("customers.PhoneNumber"));
                    System.out.println("Check in: " + result.getString("booking.CheckedIn"));
                    System.out.println("Check out: " + result.getString("booking.CheckedOut"));
                    System.out.println("Booking ID: " + result.getInt("booking.BookingID"));
                    //show info of customer from database(tables: customers, booking, orders, customer_room)
                }
            }
            //show info of customer from database(tables: customers, booking, orders, customer_room)
        } catch (SQLException var2) {
            var2.printStackTrace();
        }
    }

    public static void updateCustomerInfo() throws SQLException {
        Scanner input = new Scanner(System.in);
        localSQL.ListTable("customers");
        System.out.print("Enter guest ID:");
        int customerID = input.nextInt();
        System.out.println("What would you like to change?");
        System.out.println("1. Name");
        System.out.println("2. Phone number");
        System.out.println("3. Check out date");
        int choice = input.nextInt();
        switch (choice) {
            case 1:
                Scanner input1 = new Scanner(System.in);
                System.out.print("Enter new name: ");
                String newName = input1.nextLine();
                localSQL.sqlStatement.executeUpdate("UPDATE `hotelproject`.`customers` SET`CustomerName` = '"+newName+"' WHERE `CustomerID` = "+customerID+"");
                System.out.println("Guest with id "+customerID+" has successfully changed name to "+newName);
                break;
            case 2:
                Scanner input2 = new Scanner(System.in);
                System.out.print("Enter new number: ");
                String newNumber = input2.nextLine();
                localSQL.sqlStatement.executeUpdate("UPDATE `hotelproject`.`customers` SET`PhoneNumber` = '"+newNumber+"' WHERE `CustomerID` = "+customerID+"");
                System.out.println("Guest with id "+customerID+" has successfully changed phone number to "+newNumber);
                break;
            case 3:
                Scanner input3 = new Scanner(System.in);
                boolean runtime = true;
                while(runtime) {
                    try {
                        try {
                            try {
                                System.out.print("Choose checkout time(YYYY-MM-DD): ");
                                String checkOutDate = input3.nextLine();
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate date = LocalDate.parse(checkOutDate, dtf);
                                LocalDate now = LocalDate.now();
                                long hotelNights = now.until(date, ChronoUnit.DAYS);

                                if (hotelNights > 0) {
                                    int customerRoomID;
                                    ResultSet res = localSQL.sqlStatement.executeQuery("SELECT CustomerRoomID FROM hotelproject.customer_room WHERE CustomerID=" + customerID);
                                    customerRoomID = SQLmethods.getColumnValue(res, "customer_room.CustomerRoomID");

                                    localSQL.sqlStatement.executeUpdate("UPDATE `hotelproject`.`booking`\n" +
                                            "SET" +
                                            "`CheckedOut` = '"+checkOutDate+"'" +
                                            "WHERE `CustomerRoomID` = "+customerRoomID+"");
                                    System.out.println("You have booked "+checkOutDate+" as a new check out time.");
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
                break;
            default:

                break;

        }
    }
}