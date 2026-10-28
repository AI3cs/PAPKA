package com.sonora.guitars.entity;

public enum Role {
    VIEWER,  // Can read data, search, view reports & statistics.
    EDITOR,  // Can read data, perform CRUD operations on main entities.
    ADMIN    // Can manage users, modify roles, and access all features.
}
