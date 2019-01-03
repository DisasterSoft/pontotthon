package com.ewulusen.disastersoft.pontottohon;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.Log;
public class Finance_Income {

    public String fix;
    public String name;
    public String cost;
    public String HID="0";
    public String UID="0";
    public String EID="0";
    public String Created;
    public String type;
    public String ID;

    public Finance_Income() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Finance_Income(String name,String cost,String ID) {
        this.name = name;
        this.cost = cost;
        this.ID = ID;
    }
    public void addFinance(String name, String fix, String cost, String HID, String UID, String EID)
    {
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("y-M-d' 'HH:m:s");
        this.fix = fix;
        this.name = name;
        this.cost = cost;
        this.UID =UID;
        this.EID = EID;
        this.HID = HID;
        this.Created=dateFormatter.format(now).toString();
        this.type="income";
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }
    public String getCost()
    {
        return this.cost;
    }
    public String getID()
    {
        return this.ID;
    }
    public void setID(String ID)
    {
        this.ID=ID;
    }
}