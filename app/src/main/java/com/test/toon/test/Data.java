package com.test.toon.test;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by abasile on 07/09/2015.
 */
public class Data extends BaseObservable {

    private String temp;

    public Data(String temp) {
        this.temp = temp;
    }

    @Bindable
    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
