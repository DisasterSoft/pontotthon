package com.ewulusen.disastersoft.pontottohon;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.database.Cursor;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.*;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import java.util.List;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fooldal extends Fragment {
    public fooldal() {
        // Required empty public constructor
    }


    public TextView welcome, name, mainosszeg;
    public static String id;
    List<Finance_Income> listItemsIn = new ArrayList<>();
    List<Finance_Out> listItemsOut = new ArrayList<>();
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    GoogleSignInClient mGoogleSignInClient;
    databaseHelper db;
    public User user;
    public int kiadasS=0;
    public int bevetelS=0;
    public Finance_Income income;
    public Finance_Out outcome;
    RecyclerView incomListV, outComeListV;
    public static Intent intent;
    private incomAdapter adapterIn;
    private outcomeAdapter adapterOut;
    private  Cursor bev,kia;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fooldal, container, false);
        intent = getActivity().getIntent();
        Log.d("muvelet", "fooldal megnyitva");
        id = intent.getStringExtra("datas");
        Toast.makeText(view.getContext(),view.getContext().getString(R.string.welcomemainscreen), Toast.LENGTH_LONG).show();
        db = new databaseHelper(view.getContext());
        bev = db.getBevetelUser(id,"0");
        kia = db.getKiadasUser(id,"0");
        mAdView =view.findViewById(R.id.adViewFooldal);
        MobileAds.initialize(view.getContext(),getString(R.string.admode));
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        incomListV = view.findViewById(R.id.BevList);
        outComeListV = view.findViewById(R.id.KiadList);
        mainosszeg = view.findViewById(R.id.mainOsszeg);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        incomListV.setLayoutManager(mLayoutManager);
        incomListV.setItemAnimator(new DefaultItemAnimator());
        incomListV.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapterIn = new incomAdapter(listItemsIn, view.getContext());
        incomListV.setAdapter(adapterIn);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackIn = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof incomAdapter.MyViewHolder) {
                    // get the removed item name to display it in snack bar
                    String name = listItemsIn.get(viewHolder.getAdapterPosition()).getName();
                    String cost = listItemsIn.get(viewHolder.getAdapterPosition()).getCost();

                    // backup of removed item for undo purpose
                    final Finance_Income deletedItem = listItemsIn.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    adapterIn.removeItem(viewHolder.getAdapterPosition());
                    db.deleteFromBevetel(name,cost,id);
                    countWage();
                }
            }
            };
        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackOut = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof outcomeAdapter.MyViewHolder) {
                    // get the removed item name to display it in snack bar
                    String name = listItemsOut.get(viewHolder.getAdapterPosition()).getName();
                    String cost = listItemsOut.get(viewHolder.getAdapterPosition()).getCost();

                    // backup of removed item for undo purpose
                    final Finance_Out deletedItem = listItemsOut.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    adapterOut.removeItem(viewHolder.getAdapterPosition());
                    db.deleteFromKiadas(name,cost,id);
                    countWage();
                }
            }
            };

            // attaching the touch helper to recycler view
            new ItemTouchHelper(itemTouchHelperCallbackIn).attachToRecyclerView(incomListV);
            new ItemTouchHelper(itemTouchHelperCallbackOut).attachToRecyclerView(outComeListV);
        RecyclerView.LayoutManager oLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        outComeListV.setLayoutManager(oLayoutManager);
        outComeListV.setItemAnimator(new DefaultItemAnimator());
        outComeListV.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapterOut = new outcomeAdapter(listItemsOut, view.getContext());
        outComeListV.setAdapter(adapterOut);

        mainosszeg.setText(getText(R.string.mainOsszeg) + " ");
        fillBevetel(bev);
        fillKiadas(kia);
        mainosszeg.setText(Integer.toString(bevetelS-kiadasS)+".-Ft A havi mérleged");
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        mAdView.loadAd(adRequest);
        return view;
    }

    public void fillBevetel(Cursor bevetel) {
        if (bevetel.getCount() > 0) {
            while (bevetel.moveToNext()) {
                Finance_Income i = new Finance_Income(bevetel.getString(bevetel.getColumnIndex("Name")), bevetel.getString(bevetel.getColumnIndex("Cost")),bevetel.getString(bevetel.getColumnIndex("ID")));
                listItemsIn.add(i);
                bevetelS=bevetelS+Integer.parseInt(bevetel.getString(bevetel.getColumnIndex("Cost")));
            }
            adapterIn.notifyDataSetChanged();

        }
    }

    public void fillKiadas(Cursor kiadas) {
        if (kiadas.getCount() > 0) {

            while (kiadas.moveToNext()) {
                Finance_Out i = new Finance_Out(kiadas.getString(kiadas.getColumnIndex("Name")), kiadas.getString(kiadas.getColumnIndex("Cost")), kiadas.getString(kiadas.getColumnIndex("ID")));
                listItemsOut.add(i);
                kiadasS=kiadasS+Integer.parseInt(kiadas.getString(kiadas.getColumnIndex("Cost")));
            }
            adapterOut.notifyDataSetChanged();
        }

    }
    public void countWage()
    {
        kia=db.getKiadasUser(id,"0");
        bev=db.getBevetelUser(id,"0");
        kiadasS=0;
        bevetelS=0;
        if (kia.getCount() > 0) {

            while (kia.moveToNext()) {
                kiadasS=kiadasS+Integer.parseInt(kia.getString(kia.getColumnIndex("Cost")));
            }
            adapterOut.notifyDataSetChanged();
        }
        if (bev.getCount() > 0) {
            while (bev.moveToNext()) {

                bevetelS=bevetelS+Integer.parseInt(bev.getString(bev.getColumnIndex("Cost")));
            }

        }
        mainosszeg.setText(Integer.toString(bevetelS-kiadasS)+".-Ft A havi mérleged");
    }


}