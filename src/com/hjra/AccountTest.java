package com.hjra;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AccountTest {
    Account account;

    @BeforeEach
    void setUp() {
        final String line = "1;Liam;36;197;164;193;2";
        final String CSV_SEPARATOR = ";";
        final int CSV_ARR_LEN = 7;

        account = new Account(line, CSV_SEPARATOR, System.lineSeparator(), CSV_ARR_LEN);
    }

    @org.junit.jupiter.api.Test
    void testGetBalance() {
        assertEquals(197, account.getBalanced(), "correctly get balance");
    }

    @org.junit.jupiter.api.Test
    void testGetID() {
        assertEquals(1, account.getId(), "correctly get Id");
    }

    @org.junit.jupiter.api.Test
    void testSetID() {
        Long newId = new Long(11);
        assertEquals(11, account.setId(newId), "correctly get Id");
    }



}