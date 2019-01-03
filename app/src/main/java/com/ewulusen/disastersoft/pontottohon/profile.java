package com.ewulusen.disastersoft.pontottohon;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Map;


public class profile extends AppCompatActivity {

    TextView textName,emelet,ajto,utca,hsz,irsz,telep;
    String veznev,kereszt,id,seged,email,HID;
    Button send;
    int timeID;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference hauseDB,userDB,fincaInDB,financeOutDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        timeID = (int) (new Date().getTime()/1000);
        mDatabase = FirebaseDatabase.getInstance();
        textName = findViewById(R.id.welcome);
        emelet = findViewById(R.id.emelet);
        ajto = findViewById(R.id.ajto);
        utca = findViewById(R.id.utca);
        hsz = findViewById(R.id.hsz);
        irsz = findViewById(R.id.irsz);
        telep = findViewById(R.id.telepules);
        FirebaseUser userF = mAuth.getCurrentUser();
        id=userF.getUid();
        HID=id+timeID;
        hauseDB=mDatabase.getReference("haus_"+id);
        userDB=mDatabase.getReference("user_"+id);
        fincaInDB=mDatabase.getReference("finance_in"+id+timeID);
        financeOutDB=mDatabase.getReference("finance_out"+id+timeID);
        email=userF.getEmail();
        seged=userF.getDisplayName();
        String[] parts= seged.split(" ");
        veznev=parts[1];
        kereszt=parts[0];
        send=findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ok=checkValues();
                if (ok>3)
                {
                    insertData();

                }
                else
                    {
                        Toast.makeText(profile.this, R.string.profil_hiba, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is not logged in
        //opening the login activity
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, login.class));
        }
    }
    public int checkValues()
    {
        int ok=0;
        if(telep.getText().toString().isEmpty())
        { }
        else
        {
            ok++;
        }
        if(utca.getText().toString().isEmpty())
        {      }
        else
        {
            ok++;
        }
        if(hsz.getText().toString().isEmpty())
        { }
        else
        {
            ok++;
        }
        if(irsz.getText().toString().isEmpty())
        {        }
        else
        {
            ok++;
        }
         return ok;
    }
    public void insertData()
    {

        Haus haus = new Haus(telep.getText().toString(),utca.getText().toString(),hsz.getText().toString(),irsz.getText().toString(),HID);
        hauseDB.setValue(haus);
        /*hauseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            String name, String fix, String cost, String HID, String UID, String EID, String ID
            }
        });*/
        User user = new User(veznev,email,kereszt,id,ajto.getText().toString(),emelet.getText().toString(),HID);
        userDB.setValue(user);
        Finance_Income in = new Finance_Income("teszt","0","0");
        fincaInDB.setValue(in);
        Finance_Out out = new Finance_Out("teszt","0","0");
        financeOutDB.setValue(out);
        Intent intent2 = null;
        intent2=new Intent(profile.this, mainscreen.class);
        intent2.putExtra("datas", id);
        startActivity(intent2);
        finish();
     }
}