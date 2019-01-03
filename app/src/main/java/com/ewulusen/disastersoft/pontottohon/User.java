package com.ewulusen.disastersoft.pontottohon;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

    public String firs_name;
    public String last_name;
    public String email;
    public String emelet="";
    public String ajto="";
    public String HID;
    public String id;
    public String last_login;
    public String type;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firs_name, String email, String last_name, String id, String ajto, String emelet,String HID) {
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("y-M-d' 'HH:m:s");
        this.firs_name = firs_name;
        this.email = email;
        this.last_name = last_name;
        this.emelet = emelet;
        this.ajto = ajto;
        this.id = id;
        this.HID = HID;
        this.last_login=dateFormatter.format(now).toString();
        this.type="user";
    }

}