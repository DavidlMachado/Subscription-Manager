package com.subtracker.model.enums;

/**
 * Represents the state of a specific payment/invoice.
 */
public enum PaymentStatus {
    PENDING,    // the bill is generated but not paid
    PAID,       // the bill was paid on time
    LATE,       // the curent date is past the due date 
    MISSED      // the bill was not paid in time
}