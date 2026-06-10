package com.subtracker.model.enums;

/**
 * Supported currencies for subscription billing.
 * 
 * Major fiat currencies: EUR, USD, GBP, CHF, JPY, CAD, AUD, SEK, NOK, DKK
 * Popular crypto: BTC, ETH
 */
public enum Currency {
    // Europe
    EUR,  // Euro
    GBP,  // British Pound
    CHF,  // Swiss Franc
    SEK,  // Swedish Krona
    NOK,  // Norwegian Krone
    DKK,  // Danish Krone
    PLN,  // Polish Zloty

    // Americas
    USD,  // US Dollar
    CAD,  // Canadian Dollar
    BRL,  // Brazilian Real
    MXN,  // Mexican Peso

    // Asia & Oceania
    JPY,  // Japanese Yen
    AUD,  // Australian Dollar
    NZD,  // New Zealand Dollar
    SGD,  // Singapore Dollar
    HKD,  // Hong Kong Dollar
    INR,  // Indian Rupee
    KRW,  // South Korean Won

    // Crypto
    BTC,  // Bitcoin
    ETH   // Ethereum
}