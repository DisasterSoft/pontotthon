package com.ewulusen.disastersoft.pontottohon;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class hitel extends Fragment {


    public hitel() {
        // Required empty public constructor
    }

    Finance_Out bevetel_bean;
    databaseHelper db;
    TextView hitelName, hitelCost, kezdDate, vegDate;
    RecyclerView lista;
    CheckBox fix;
    Button hozzad;
    List<Finance_Out> listItemsIn = new ArrayList<>();
    private outcomeAdapter adapterIn;
    private Cursor hitelek;
    public static String id;
    public static Intent intent;
    private AdView mAdView;
    final Calendar kezdCalendar = Calendar.getInstance();
    final Calendar vegCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_hitel, container, false);
        db = new databaseHelper(view.getContext());
        Toast.makeText(view.getContext(), view.getContext().getString(R.string.hitelWelcome), Toast.LENGTH_LONG).show();
        intent = getActivity().getIntent();
        id = intent.getStringExtra("datas");
        hitelek = db.getHitelekUser(id);
        lista = view.findViewById(R.id.hitelList);
        kezdDate = view.findViewById(R.id.kezdDateHitel);
        vegDate = view.findViewById(R.id.befDateHitel);
        hitelCost = view.findViewById(R.id.hitelErtek);
        hitelName = view.findViewById(R.id.hitelName);
        hozzad = view.findViewById(R.id.addHitel);
        mAdView = view.findViewById(R.id.adViewHitel);
        MobileAds.initialize(view.getContext(), getString(R.string.admode));
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        final AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        mAdView.loadAd(adRequest);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        lista.setLayoutManager(mLayoutManager);
        lista.setItemAnimator(new DefaultItemAnimator());
        lista.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapterIn = new outcomeAdapter(listItemsIn, view.getContext());
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
                    String IID = listItemsIn.get(viewHolder.getAdapterPosition()).getID();

                    // backup of removed item for undo purpose
                    final Finance_Out deletedItem = listItemsIn.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    adapterIn.removeItem(viewHolder.getAdapterPosition());
                    db.deleteFromHitelByID(IID, name, id);
                }
            }
        };
        new ItemTouchHelper(itemTouchHelperCallbackIn).attachToRecyclerView(lista);
        fillhitelek(hitelek);
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                kezdCalendar.set(Calendar.YEAR, year);
                kezdCalendar.set(Calendar.MONTH, monthOfYear);
                kezdCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelKezd();
            }

        };
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                vegCalendar.set(Calendar.YEAR, year);
                vegCalendar.set(Calendar.MONTH, monthOfYear);
                vegCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelVeg();

            }

        };
        kezdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(view.getContext(), date1, kezdCalendar
                        .get(Calendar.YEAR), kezdCalendar.get(Calendar.MONTH),
                        kezdCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        vegDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(view.getContext(), date2, vegCalendar
                        .get(Calendar.YEAR), vegCalendar.get(Calendar.MONTH),
                        vegCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        hozzad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!kezdDate.getText().equals("") &&
                    !vegDate.getText().equals("") &&
                    !hitelCost.getText().equals("") &&
                    !hitelName.getText().equals(""))
                {
                   String valasz=db.addHitelekUser(id,kezdDate.getText().toString(),vegDate.getText().toString(),hitelName.getText().toString(),hitelCost.getText().toString());
                   if(valasz.equals("")) {
                       Toast.makeText(view.getContext(), view.getContext().getString(R.string.hiteladded), Toast.LENGTH_LONG).show();
                       kezdDate.setText("");
                       vegDate.setText("");
                       hitelCost.setText("");
                       hitelName.setText("");
                       hitelek = db.getHitelekUser(id);
                       listItemsIn.clear();
                       fillhitelek(hitelek);
                   }
                   else
                   {
                       Toast.makeText(view.getContext(), view.getContext().getString(R.string.marvan), Toast.LENGTH_LONG).show();
                   }
                }
                else
                {
                    Toast.makeText(view.getContext(), view.getContext().getString(R.string.profil_hiba), Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    public void fillhitelek(Cursor bevetel) {
        Log.d("hitel","feltölti a listát");
        if (bevetel.getCount() > 0) {
            while (bevetel.moveToNext()) {
                Finance_Out i = new Finance_Out(bevetel.getString(bevetel.getColumnIndex("Name")), bevetel.getString(bevetel.getColumnIndex("money")), bevetel.getString(bevetel.getColumnIndex("ID")));
                listItemsIn.add(i);
            }
            adapterIn.notifyDataSetChanged();

        }
    }

    private void updateLabelKezd() {
        String myFormat = "yyyy/MM/DD"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        kezdDate.setText(sdf.format(kezdCalendar.getTime()));
    }
    private void updateLabelVeg() {
        String myFormat = "yyyy/MM/DD"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        vegDate.setText(sdf.format(vegCalendar.getTime()));
    }
}
