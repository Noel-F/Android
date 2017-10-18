package com.fullsail.franceschinoel_ce07.utils;

import android.support.annotation.Nullable;

import java.io.Serializable;

// Noel Franceschi
// MDF3 1610
// People.java

public class People implements Serializable {

    public String firstName;
    public String lastName;
    public String age;
    public String id;

    public People(String firstName, String lastName, String age, @Nullable String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.id = id;
    }
}

