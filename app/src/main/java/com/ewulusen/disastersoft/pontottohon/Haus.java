package com.ewulusen.disastersoft.pontottohon;
public class Haus {

    public String Adress_1;
    public String Adress_2;
    public String Adress_3;
    public String Adress_4;
    public String id;
    public String type;


    public Haus() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Haus(String Adress_1, String Adress_2, String Adress_3, String Adress_4,String id) {
        this.Adress_1 = Adress_1;
        this.Adress_2 = Adress_2;
        this.Adress_3 = Adress_3;
        this.Adress_4 = Adress_4;
        this.id = id;
        this.type="haus";

    }

}