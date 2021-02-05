package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FoodOrders {
    static Scanner input = new Scanner(System.in);

    public static void viewFoodItems() throws SQLException {
        String foodName = " ";
        System.out.println("Would you like to make an order?  (Y/N) ");
        String retry = input.nextLine();
        if (retry.startsWith("y")) {
            addFood(foodName);
        } else {
            System.out.println();
            System.out.println("Redirecting you back to Main Menu...");
            MainMenu.start();
        }
    }

    public static void addFood(String foName) { // receptionist
        boolean bool = true;
        while(bool) {
            try{
                String foodType;
                int quanty = 0;
                int new_foodId;
                int customerID;
                int price = 0;


                localSQL.ListTable("customers");
                System.out.println("Please choose guest ID before making an order:\n ");
                int customerId = Integer.parseInt(input.nextLine());

                System.out.println("Below is the Food Menu:::\n");
                localSQL.ListTable("food");
                System.out.println("Select from menu by FoodID to order:\n");
                int foodId = Integer.parseInt(input.nextLine());
                System.out.println("How many of item["+ foodId +"]do you want to order?\n");
                int qty = Integer.parseInt(input.nextLine());

                if (customerId > 0 ){

                    ResultSet rs = localSQL.sqlStatement.executeQuery("SELECT CustomerID FROM hotelproject.customers WHERE CustomerID=" + customerId + ";");
                    customerID = SQLmethods.getColumnValue(rs, "CustomerID");

                    ResultSet rs2 = localSQL.sqlStatement.executeQuery( "SELECT FoodID,foodName,price FROM hotelproject.food WHERE FoodID=" + foodId + ";");
                    new_foodId = SQLmethods.getColumnValue(rs2,"FoodID");

                    ResultSet rs3 = localSQL.sqlStatement.executeQuery( "SELECT foodName FROM hotelproject.food WHERE FoodID=" + foodId + ";");
                    foodType = SQLmethods.getStringColumnValue(rs3, "foodName");

                    ResultSet rs4 = localSQL.sqlStatement.executeQuery( "SELECT price FROM hotelproject.food WHERE FoodID=" + foodId + ";");
                    price = SQLmethods.getColumnValue(rs4,"price");


                    localSQL.sqlStatement.executeUpdate("INSERT INTO hotelproject.orders (CustomerID,FoodID, Order_total) Values (" + customerID + ", " + new_foodId +", " + (price*qty) +");");



                    System.out.println("          \n\033[1mCustomer with ID " + customerID + " has ordered: " + "\nFood: " + foodType + "\n" +
                            "Quantity: " + qty + "\n" +
                            "Price: " + price + " kr per item\n" +
                            "Total Cost:\033[0m " + (qty*price) + "kr");

                    Booking.sendFoodOrder((qty*price), customerID);
                    System.out.println();

                } else {
                    System.out.println("You need to be registered in the hotel before making an order.");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }


    }
    public static void totalFoodCostTotalStay(){

    }
}
