package com.digitalchannelforum.drupalcon.model.data;

import java.util.ArrayList;

public class Data {
    ArrayList<Long> data = new ArrayList<>();

    public Data() {
        this.data.add(32l);
    }

    public ArrayList<Long> getData() {
        return data;
    }

    public void setData(ArrayList<Long> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Data{" +
                "data=" + data +
                '}';
    }
}
