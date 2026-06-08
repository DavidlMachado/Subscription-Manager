package com.subtracker.model.entity;

import com.subtracker.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a single payment event for a subscription.
 * Functions as an invoice or receipt history.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRecord {
    @Id //sets as a primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // sets to auto-increment
    private Integer id;

    /**
     * The subscription this record belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    /**
     * The exact date the payment is expected to be made
     */
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    /**
     * The date the payment was made
     */
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    /**
     * The exact amount that was paid
     */
    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    /**
     * Method that marks a payment as paid, and
     * checks if it was paid on time
     */
    public void markAsPaid(LocalDate datePaid) {
        if(this.status == PaymentStatus.PAID || this.status == PaymentStatus.MISSED) {
            throw new IllegalStateException("Cannot pay a record that is already paid or missed.");
        }

        // gets the grace period for this payment
        Integer gracePeriod = this.subscription.getGracePeriodDays();
        LocalDate absoluteDeadline = this.dueDate.plusDays(gracePeriod);

        // if the payment date is past the due date plus grace period it is considered a missed payment
        if(datePaid.isAfter(absoluteDeadline)) {
            throw new IllegalStateException("Payment is past the grace period. This record is MISSED.");
        }

        this.paymentDate = datePaid;

        if(datePaid.isAfter(this.dueDate)) {
            // if it is paid after the due date but before the grace period it is just a late payment
            this.status = PaymentStatus.LATE;
        } else {
            // if it is paid on time it is just marked as paid
            this.status = PaymentStatus.PAID;
        }
    }

    /**
     * Method that marks a payment as missed
     */
    public void markAsMissed() {
        if (this.status == PaymentStatus.PAID || this.status == PaymentStatus.LATE) {
            throw new IllegalStateException("Cannot mark a paid record as missed.");
        }
        this.status = PaymentStatus.MISSED;
    }
}