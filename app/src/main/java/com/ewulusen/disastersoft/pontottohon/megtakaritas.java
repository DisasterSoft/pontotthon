package com.ewulusen.disastersoft.pontottohon;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


/**
 * A simple {@link Fragment} subclass.
 */
public class megtakaritas extends Fragment {

    private AdView mAdView;
    databaseHelper db;
    TextView megtakaritas,ertek;
    Button hozzad,kivesz;
    public static String id;
    public static Intent intent;
    public megtakaritas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_megtakaritas,container,false);
        db=new databaseHelper(view.getContext());
        intent = getActivity().getIntent();
        id = intent.getStringExtra("datas");
        MobileAds.initialize(view.getContext(),getString(R.string.admode));
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        megtakaritas=view.findViewById(R.id.megtak);
        mAdView =view.findViewById(R.id.adView);
        hozzad=view.findViewById(R.id.addMegtak);
        kivesz=view.findViewById(R.id.deleteMegtak);
        ertek=view.findViewById(R.id.megtakErtek);
        megtakaritas.setText(db.getAllmegtakaritas(id));
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        mAdView.loadAd(adRequest);

        hozzad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ertek.equals(""))
                {
                    Toast.makeText(view.getContext(),view.getContext().getString(R.string.addErtek), Toast.LENGTH_LONG).show();
                }
                else
                {
                    db.addMegtakaritas(ertek.getText().toString(),id);
                    megtakaritas.setText(db.getAllmegtakaritas(id));
                    ertek.setText("");
                }
            }
        });
        kivesz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ertek.equals(""))
                {
                    Toast.makeText(view.getContext(),view.getContext().getString(R.string.addErtek), Toast.LENGTH_LONG).show();
                }
                else
                {
                    String vissza=db.getMegtakaritas(ertek.getText().toString(),id);
                    if(vissza.equals("tulsok"))
                    {
                        Toast.makeText(view.getContext(),view.getContext().getString(R.string.tulsok), Toast.LENGTH_LONG).show();
                    }
                    megtakaritas.setText(db.getAllmegtakaritas(id));
                    ertek.setText("");
                }
            }
        });
        return view;
    }

}
