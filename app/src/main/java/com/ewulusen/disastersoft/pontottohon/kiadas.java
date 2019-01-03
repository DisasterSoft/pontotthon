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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class kiadas extends Fragment {
    Finance_Out kiadas_bean;
    databaseHelper db;
    TextView kiadasName,kiadasCost;
    CheckBox fix;
    RecyclerView lista;
    Button hozzad;
    List<Finance_Out> listItemsOut = new ArrayList<>();
    private outcomeAdapter adapterOut;
    private Cursor kia;
    public static String id;
    public static Intent intent;
    private AdView mAdView;
    public kiadas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_kiadas,container,false);
        db=new databaseHelper(view.getContext());
        Toast.makeText(view.getContext(),view.getContext().getString(R.string.fixItemsShow), Toast.LENGTH_LONG).show();
        intent = getActivity().getIntent();
        id = intent.getStringExtra("datas");
        kia = db.getKiadasUser(id,"1");
        lista=view.findViewById(R.id.kiadasList);
        kiadas_bean=new Finance_Out();
        kiadasCost=view.findViewById(R.id.kiadasErtek);
        kiadasName=view.findViewById(R.id.kiadasName);
        hozzad=view.findViewById(R.id.addkiadas);
        fix=view.findViewById(R.id.checkBoxkiadas);
        mAdView =view.findViewById(R.id.adViewKiadas);
        MobileAds.initialize(view.getContext(),getString(R.string.admode));
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
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
                db.addKiadasUser(id,kiadasName.getText().toString(),fix_e,kiadasCost.getText().toString());
                kiadasName.setText("");
                kiadasCost.setText("");
                fix.setChecked(false);
                kia = db.getKiadasUser(id,"1");
                listItemsOut.clear();
                fillKiadas(kia);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        lista.setLayoutManager(mLayoutManager);
        lista.setItemAnimator(new DefaultItemAnimator());
        lista.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapterOut = new outcomeAdapter(listItemsOut, view.getContext());
        lista.setAdapter(adapterOut);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackIn = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof incomAdapter.MyViewHolder) {
                    // get the removed item name to display it in snack bar
                    String name = listItemsOut.get(viewHolder.getAdapterPosition()).getName();
                    String cost = listItemsOut.get(viewHolder.getAdapterPosition()).getCost();
                    String IID = listItemsOut.get(viewHolder.getAdapterPosition()).getID();

                    // backup of removed item for undo purpose
                    final Finance_Out deletedItem = listItemsOut.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    adapterOut.removeItem(viewHolder.getAdapterPosition());
                    db.deleteFromKiadasByID(IID);
                }
            }
        };
        new ItemTouchHelper(itemTouchHelperCallbackIn).attachToRecyclerView(lista);
        fillKiadas(kia);
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        mAdView.loadAd(adRequest);
        return view;

    }

    public void fillKiadas(Cursor kiadas) {

        if (kiadas.getCount() > 0) {

            while (kiadas.moveToNext()) {
                Finance_Out i = new Finance_Out(kiadas.getString(kiadas.getColumnIndex("Name")), kiadas.getString(kiadas.getColumnIndex("Cost")),kiadas.getString(kiadas.getColumnIndex("ID")));
                listItemsOut.add(i);
            }
            adapterOut.notifyDataSetChanged();
        }

    }
}
