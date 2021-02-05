package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLmethods {
    public static int getColumnValue(ResultSet res, String column) throws SQLException {
        int x=0;
        while (res.next()) {
            if (res.isFirst()) {
                x = res.getInt(column);
            }
        }
        return x;
    }

    public static String getStringColumnValue(ResultSet res, String column) throws SQLException {
        String x=null;
        while (res.next()) {
            if (res.isFirst()) {
                x = res.getString(column);
            }
        }
        return x;
    }


    public static void ListTableBookings() throws SQLException {
        ResultSet result = localSQL.sqlStatement.executeQuery("SELECT booking.BookingID, rooms.RoomNumber, customers.CustomerName , booking.CheckedIn, booking.CheckedOut  FROM hotelproject.booking " +
                "join customer_room on customer_room.CustomerRoomID = booking.CustomerRoomID " +
                "join customers on customers.CustomerID = customer_room.CustomerID " +
                "join rooms on rooms.RoomNumber = customer_room.RoomNumber");
        int columnCount = result.getMetaData().getColumnCount();
        String[] columnNames = new String[columnCount];

        for(int i = 0; i < columnCount; ++i) {
            columnNames[i] = result.getMetaData().getColumnName(i + 1);
        }

        String[] var9 = columnNames;
        int var5 = columnNames.length;

        int var6;
        String columnName;
        for(var6 = 0; var6 < var5; ++var6) {
            columnName = var9[var6];
            System.out.print(PadRight(columnName));
        }

        while(result.next()) {
            System.out.println();
            var9 = columnNames;
            var5 = columnNames.length;

            for(var6 = 0; var6 < var5; ++var6) {
                columnName = var9[var6];
                String value = result.getString(columnName);
                if (value == null) {
                    value = "null";
                }

                System.out.print(PadRight(value));
            }
        }

        System.out.println();
    }
    public static void ListSpecificCustomer(int customerID) throws SQLException {
        ResultSet result = localSQL.sqlStatement.executeQuery("SELECT rooms.RoomNumber, customers.CustomerName, customers.PhoneNumber, booking.CheckedOut FROM hotelproject.customers \n" +
                "JOIN customer_room on customer_room.CustomerID = customers.CustomerID\n" +
                "JOIN rooms on rooms.RoomNumber = customer_room.RoomNumber\n" +
                "JOIN booking on booking.CustomerRoomID = customer_room.CustomerRoomID\n" +
                "WHERE customers.CustomerID="+customerID+"");
        int columnCount = result.getMetaData().getColumnCount();
        String[] columnNames = new String[columnCount];

        for (int i = 0; i < columnCount; ++i) {
            columnNames[i] = result.getMetaData().getColumnName(i + 1);
        }

        String[] var9 = columnNames;
        int var5 = columnNames.length;

        int var6;
        String columnName;
        for (var6 = 0; var6 < var5; ++var6) {
            columnName = var9[var6];
            System.out.print(PadRight(columnName));
        }

        while (result.next()) {
            System.out.println();
            var9 = columnNames;
            var5 = columnNames.length;

            for (var6 = 0; var6 < var5; ++var6) {
                columnName = var9[var6];
                String value = result.getString(columnName);
                if (value == null) {
                    value = "null";
                }

                System.out.print(PadRight(value));
            }
        }

        System.out.println();
    }
    public static String PadRight(String string) {
        int totalStringLength = 30;
        int charsToPad = totalStringLength - string.length();
        if (string.length() >= totalStringLength) {
            return string;
        } else {
            StringBuilder stringBuilder = new StringBuilder(string);

            for(int i = 0; i < charsToPad; ++i) {
                stringBuilder.append(" ");
            }

            return stringBuilder.toString();
        }
    }
}