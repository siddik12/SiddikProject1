package com.rockscoder.easyphotofilters.model;

/**
 * Created by Siddiqur on 3/21/2018.
 */

public class Param {
    String name;
    String value;

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
