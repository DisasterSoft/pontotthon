package com.ewulusen.disastersoft.pontottohon;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.Log;
public class Finance_Out {

    public String fix;
    public String name;
    public String cost;
    public String HID="0";
    public String UID="0";
    public String EID="0";
    public String ID;
    public String Created;
    public String type;

    public Finance_Out() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Finance_Out(String name, String cost,String ID) {
Log.d("kiadas",name+" "+cost);
        this.name = name;
        this.cost = cost;
        this.ID = ID;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCost()
    {
        return this.cost;
    }
    public String getID()
    {
        return this.ID;
    }


}