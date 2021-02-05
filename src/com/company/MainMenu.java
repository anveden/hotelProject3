package com.company;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.company.Receptionist.book;

public class MainMenu {
    static Scanner input = new Scanner(System.in);
    static Receptionist rep;
    static Customer cust;
    static FoodOrders fOrders;

    public static void start() throws SQLException {
        boolean exit = false;

        while (!exit) {
            System.out.println("      \n\033[1mWelcome To the Main Menu\033[0m       ");
            System.out.println();
            System.out.println("Please choose from the following:");
            System.out.println("1. Login area");
            System.out.println("0. Exit Program");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    authenticateLogin();

                    break;
                default:
                    System.out.println(" The Program will now end.... ");
                    System.exit(0);

            }
        }

    }

    public static void authenticateLogin() throws SQLException {
        String admin = "admin";
        String guest = "guest";

        try{
            System.out.println();
            System.out.println("---------------PLEASE ENTER YOUR INFORMATION BELOW TO PROCEED-------");
            System.out.println();
            System.out.println("\n\033[1mEnter your password: \033[0m");
            String username = input.nextLine();

            if (username.equals(admin)) {
                System.out.println("-------------------     LOGIN SUCCESS!     ----------------------");
                receptionistView();
            } else if (username.equals(guest)) {
                System.out.println("-------------------     LOGIN SUCCESS!     ----------------------");
                guestView();

            } else if ((!username.equals(admin)) || (!username.equals(guest)) ) {
                System.err.println("Invalid password. Would you like to try again: y/n ");
                String retry = input.nextLine();
                if (retry.startsWith("y")) {
                    authenticateLogin();
                } else {
                    System.out.println("Redirecting to Main Menu...");
                    start();
                }
            }
        } catch(Exception e){
            e.getMessage();
        }
    }

    public static void guestView() throws SQLException{
        localSQL.connection = DriverManager.getConnection(localSQL.url, localSQL.user, localSQL.password);
        boolean runtime = true;
        while (runtime) {
            try {
                localSQL.sqlStatement = localSQL.connection.createStatement();
                System.out.println("\n\033[1m---------------------   GUEST ACCESS AREA  -----------------------");
                System.out.println("1. Add and check in guest");
                System.out.println("2. Search guest by name");
                System.out.println("3. Update guest info");
                System.out.println("4. Check out");
                System.out.println("0. Log out and go to previous menu.\033[0m");

                int choice = input.nextInt();

                switch (choice) {
                    case 1:
                        Customer.addCustomer();
                        break;
                    case 2:
                        Customer.searchCustomerByName();
                        break;
                    case 3:
                        Customer.updateCustomerInfo();
                        break;
                    case 4:
                        Booking.checkOut();
                        break;
                    default:
                        runtime = false;
                }

            } catch (SQLException var2) {
                var2.printStackTrace();
            }
        }
    }
    public static void receptionistView() throws SQLException {

        localSQL.connection = DriverManager.getConnection(localSQL.url, localSQL.user, localSQL.password);
        boolean loop = true;

        while (loop) {
            try {
                localSQL.sqlStatement = localSQL.connection.createStatement();
                System.out.println("\n\033[1m---------------------   ADMIN ACCESS AREA  -----------------------");
                System.out.println("What would you like to do?");
                System.out.println("1. List all rooms and their availability.");
                System.out.println("2. Display all guests booked into the hotel.");
                System.out.println("3. Search for a guest by ID.");
                System.out.println("4. Register a new guest + book room.");
                System.out.println("5. Check-out a guest.");
                System.out.println("6. Order food for a guest.");
                System.out.println("7. Upgrade a room to a guest");
                System.out.println("0. Log out and go to previous menu.\033[0m");

                int choice = input.nextInt();

                switch (choice) {
                    case 1:
                        rep.roomAvailability();
                        break;
                    case 2:
                        rep.totalGuests();
                        break;
                    case 3:
                        cust.searchCustomerByID();
                        break;
                    case 4:
                        cust.addCustomer();
                        break;
                    case 5:
                        book.checkOut();
                        break;
                    case 6:
                        fOrders.viewFoodItems();

                        break;
                    case 7:
                        Booking.upgradeGuest();
                        break;
                        /*
                    case 8:

                        break;*/
                    default:
                        System.out.println(" Redirecting to main menu...");
                        loop = false;
                        //break;

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
