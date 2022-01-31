package com.example.bankmanagement.BankApp.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Generator {

    public final static long generateAccountNumber() {

        return (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
    }

    public final static long generateCustomerID() {

        return (long) Math.floor(Math.random() * 900_000L) + 100_000L;
    }

    public final static long generateTransactionReferenceNumber() {

        return (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
    }

    public final static LocalDateTime generateCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return now;
    }
}
