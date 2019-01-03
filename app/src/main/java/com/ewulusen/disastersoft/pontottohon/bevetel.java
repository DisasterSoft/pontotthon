package com.ewulusen.disastersoft.pontottohon;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;


public class bevetel extends Fragment {
Finance_Income bevetel_bean;
databaseHelper db;
TextView bevetelName,bevetelCost;
RecyclerView lista;
CheckBox fix;
Button hozzad;
List<Finance_Income> listItemsIn = new ArrayList<>();
private incomAdapter adapterIn;
private Cursor bev;
public static String id;
public static Intent intent;
    private AdView mAdView;
    public bevetel() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_bevetel,container,false);
        db=new databaseHelper(view.getContext());
        Toast.makeText(view.getContext(),view.getContext().getString(R.string.fixItemsShow), Toast.LENGTH_LONG).show();
        intent = getActivity().getIntent();
        id = intent.getStringExtra("datas");
        bev = db.getBevetelUser(id,"1");
        lista=view.findViewById(R.id.bevetelList);
        bevetel_bean=new Finance_Income();
        bevetelCost=view.findViewById(R.id.BevetelErtek);
        bevetelName=view.findViewById(R.id.BevetelName);
        hozzad=view.findViewById(R.id.addBevetel);
        fix=view.findViewById(R.id.checkBoxBevetel);
        mAdView =view.findViewById(R.id.adViewBevetel);
        MobileAds.initialize(view.getContext(),getString(R.string.admode));
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        mAdView.loadAd(adRequest);
        hozzad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fix_e;
                if(fix.isChecked())
                {
                    fix_e="1";
                }
                else
                {
                    fix_e="0";
                }
                db.addBevetelUser(id,bevetelName.getText().toString(),fix_e,bevetelCost.getText().toString());
                bevetelName.setText("");
                bevetelCost.setText("");
                fix.setChecked(false);
                bev = db.getBevetelUser(id,"1");
                listItemsIn.clear();
                fillBevetel(bev);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        lista.setLayoutManager(mLayoutManager);
        lista.setItemAnimator(new DefaultItemAnimator());
        lista.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapterIn = new incomAdapter(listItemsIn, view.getContext());
        lista.setAdapter(adapterIn);
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
                    String IID=listItemsIn.get(viewHolder.getAdapterPosition()).getID();

                    // backup of removed item for undo purpose
                    final Finance_Income deletedItem = listItemsIn.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    adapterIn.removeItem(viewHolder.getAdapterPosition());
                    db.deleteFromBevetelByID(IID);
                }
            }
        };
        new ItemTouchHelper(itemTouchHelperCallbackIn).attachToRecyclerView(lista);
        fillBevetel(bev);
        return view;

    }
    public void fillBevetel(Cursor bevetel) {
        if (bevetel.getCount() > 0) {
            while (bevetel.moveToNext()) {
                Finance_Income i = new Finance_Income(bevetel.getString(bevetel.getColumnIndex("Name")), bevetel.getString(bevetel.getColumnIndex("Cost")),bevetel.getString(bevetel.getColumnIndex("ID")));
                listItemsIn.add(i);
            }
            adapterIn.notifyDataSetChanged();

        }
    }


}
