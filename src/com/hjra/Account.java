package com.hjra;

public class Account implements Comparable<Account> {

    private Long id;
    private String nama;
    private int age;
    private double balanced;
    private String no2BThreadNo;
    private String no3ThreadNo;
    private double previousBalance;
    private double averageBalance;
    private String no1ThreadNo;
    private int feeTransfer;
    private String no2AThreadNo;
    private String csvSeparator;

    public Account() {
    }

    public Account(String csvLine, String csvSeparator, String lineSeparator, int expectedLen) {
        String[] split = csvLine.split(csvSeparator);
        if (split.length != expectedLen) {
            throw new RuntimeException("Invalid data");
        }

        id = Long.parseLong(split[0]);
        nama = split[1];
        age = Integer.parseInt(split[2]);
        balanced = Double.parseDouble(split[3]);
        previousBalance = Double.parseDouble(split[4]);
        feeTransfer = Integer.parseInt(split[5]);
    }

    public double getBalanced() {
        return balanced;
    }

    public String getNo2BThreadNo() {
        if (no2BThreadNo == null) {
            return "";
        }
        return no2BThreadNo;
    }

    public void setNo2BThreadNo(String no2BThreadNo) {
        this.no2BThreadNo = no2BThreadNo;
    }

    public String getNo3ThreadNo() {
        if (no3ThreadNo == null) {
            return "";
        }
        return no3ThreadNo;
    }

    public void setNo3ThreadNo(String no3ThreadNo) {
        this.no3ThreadNo = no3ThreadNo;
    }

    public String getNo1ThreadNo() {
        if (no1ThreadNo == null) {
            return "";
        }
        return no1ThreadNo;
    }

    public void setNo1ThreadNo(String no1ThreadNo) {
        this.no1ThreadNo = no1ThreadNo;
    }

    public String getNo2AThreadNo() {
        if (no2AThreadNo == null) {
            return "";
        }
        return no2AThreadNo;
    }

    public void setNo2AThreadNo(String no2AThreadNo) {
        this.no2AThreadNo = no2AThreadNo;
    }

    public void setFeeTransfer(int feeTransfer) {
        this.feeTransfer = feeTransfer;
    }

    public Long getId() {
        return this.id;
    }

    public Long setId(Long id) {
        return this.id = id;
    }

    public void topupBalance(double increment) {
        this.balanced += increment;
    }

    public void calculateAverage() {
        this.averageBalance = (balanced + previousBalance) / 2;
    }

    @Override
    public int compareTo(Account o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public String toString() {
        return id + ";" + nama + ";" + age + ";" + balanced + ";" + getNo2BThreadNo() + ";" + getNo3ThreadNo() + ";"
                + previousBalance + ";" + averageBalance + ";" + getNo1ThreadNo()
                + ";" + feeTransfer + ";" + getNo2AThreadNo() + System.lineSeparator();
    }
}
