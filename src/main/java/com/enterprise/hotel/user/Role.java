package com.enterprise.hotel.user;

/** Role-Based Access Control roles. Persisted as a string on the users table. */
public enum Role {
    CUSTOMER,
    HOTEL_MANAGER,
    ADMIN
}
