package com.example.nivit_expbook;
import java.io.Serializable;

public class Expense implements Serializable {
    private String name;
    private String month;
    private double monthlyCharge;
    private String comment;

    public Expense(String name, String month, double monthlyCharge, String comment) {
        this.name = name;
        this.month = month;
        this.monthlyCharge = monthlyCharge;
        this.comment = comment;
    }

    // Getter and setter methods for expense fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getMonthlyCharge() {
        return monthlyCharge;
    }

    public void setMonthlyCharge(double monthlyCharge) {
        this.monthlyCharge = monthlyCharge;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
