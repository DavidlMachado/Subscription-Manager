package com.subtracker.model.enums;

/**
 * Represents the current state of a subscription.
 */
public enum SubscriptionStatus {
    ACTIVE,    // The subscription is running and billing normally
    PAUSED,    // The user temporarily suspended the subscription
    CANCELLED  // The subscription is permanently closed
}