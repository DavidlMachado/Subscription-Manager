package com.subtracker.model.entity;

import com.subtracker.model.enums.BillingCycle;
import com.subtracker.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an active or paused subscription (e.g, Netflix, Gym, Spotify)
 * Registers the payment amount and payment cycle, status,
 * and the date of next payment
 */
@Entity
@Table(name = "subscriptions")
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id // sets as a primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // sets to auto-increment
    private Integer id;

    @Column(name = "name",nullable = false, unique = true)
    private String name;

    /**
     * BigDecimals are used because they avoide floating point precision
     * errors when dealing with money (e.g., 0.1 + 0.2 = 0.30000000000000004).
     */
    @Column(name = "amount",nullable = false)
    private BigDecimal amount;

    /**
     * How often the subscritpion is billed
     * By using @Enumerated the value is stored as a String in the db
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle",nullable = false)
    private BillingCycle billingCycle;

    /**
     * The complete history of payments for this subscription.
     * 
     * CascadeType.ALL: If we delete the subscription, all its payment records 
     * are automatically deleted from the database.
     * orphanRemoval = true: If we remove a PaymentRecord from this list, 
     * Hibernate deletes it from the database.
     */
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PaymentRecord> paymentRecords = new ArrayList<>();

    /**
     * The activity status of the subscription
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    @Builder.Default
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    /**
     * The category this subscription belongs to.
     * FetchType.LAZY ensures we only load the category from the db
     * when we explicitly ask for it, improving application performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * The exact date of the next charge.
     */
    @Column(name = "next_payment_date",nullable = false)
    private LocalDate nextPaymentDate;

    /**
     * The number of days the service allows a payment to be delayed 
     * before suspending or cutting off access.
     */
    @Column(name = "grace_period_days", nullable = false)
    @Builder.Default
    private Integer gracePeriodDays = 0;

    /**
     * The date a subscription was paused.
     */
    @Column(name = "paused_date")
    private LocalDate pauseDate;

    /**
     * The date a subscription was cancelled.
     */
    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    /**
     * The date the user originally subscribed to the service.
     * Acts as a "Member since..." indicator and never changes.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Method that advances the payment date automatically
     * once a charge is processed
     */
    public void processRenewal() {
        switch(this.billingCycle) {
            case WEEKLY -> this.nextPaymentDate = this.nextPaymentDate.plusWeeks(1);
            case BIWEEKLY -> this.nextPaymentDate = this.nextPaymentDate.plusWeeks(2);
            case MONTHLY -> this.nextPaymentDate = this.nextPaymentDate.plusMonths(1);
            case QUARTERLY -> this.nextPaymentDate = this.nextPaymentDate.plusMonths(3);
            case BIANNUAL -> this.nextPaymentDate = this.nextPaymentDate.plusMonths(6);
            case YEARLY -> this.nextPaymentDate = this.nextPaymentDate.plusYears(1);
        }
    }

    /**
     * Method that sets the status of a subscription as cancelled
     */
    public void cancel(LocalDate cancelDate) {
        if(this.status == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Subscription is already cancelled.");
        }

        this.status = SubscriptionStatus.CANCELLED;
        this.cancelDate = cancelDate;
    }

    /**
     * Method that temporarily pauses a subscription
     */
    public void pause(LocalDate pauseDate) {
        if (this.status == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot pause a cancelled subscription.");
        }

        if (this.status == SubscriptionStatus.PAUSED) {
            throw new IllegalStateException("Subscription is already paused.");
        }

        this.status = SubscriptionStatus.PAUSED;
        this.pauseDate = pauseDate;
    }

    /**
     * Method that resumes a paused subscription
     */
    public void resume() {
        if (this.status == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot resume a cancelled subscription.");
        }

        if (this.status == SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Subscription is already active.");
        }

        this.status = SubscriptionStatus.ACTIVE;
        this.pauseDate = null;
    }

    /**
     * Method that reactivates a completely cancelled subscription.
     * Requires the new payment date, and allows updating the contract terms 
     * (price, cycle, grace period) as they might have changed.
     */
    public void reactivate(LocalDate newNextPaymentDate, BigDecimal newAmount, 
        BillingCycle newBillingCycle, Integer newGracePeriodDays) {

        if (this.status == SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Subscription is already active.");
        }
        if (this.status == SubscriptionStatus.PAUSED) {
            throw new IllegalStateException("Subscription is paused. Use resume() instead.");
        }

        this.status = SubscriptionStatus.ACTIVE;
        this.nextPaymentDate = newNextPaymentDate;
        this.cancelDate = null;

        // Updates the new business rules
        if (newAmount != null) this.amount = newAmount;
        if (newBillingCycle != null) this.billingCycle = newBillingCycle;
        if (newGracePeriodDays != null) this.gracePeriodDays = newGracePeriodDays;
    }

    @PrePersist
    @PreUpdate
    // runs before inserting or updating a value
    private void formatData() {
        if(this.name != null) {
            this.name = this.name.trim().toUpperCase();
        }
    }

}