package com.company;

import java.sql.SQLException;

public class Receptionist {
    static Booking book;

    public static void totalGuests() throws SQLException {
        localSQL.ListTable("customers");
    }
    public static void roomAvailability() throws SQLException {
        System.out.println("Total rooms in the hotel: \n\033[1m1\033[0m means \033[1mAvailable\033[0m and \033[1m0\033[0m = \033[1mBooked\033[0m");
        localSQL.ListTable("rooms");

    }


}
