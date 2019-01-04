package com.ewulusen.disastersoft.pontottohon;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class oldCashflow extends Fragment {


    public oldCashflow() {
        // Required empty public constructor
    }
    databaseHelper db;
    String oldCF;
    TextView vage;
    View view;
    Spinner listaS;
    List<Finance_Income> listItemsIn = new ArrayList<>();
    List<Finance_Out> listItemsOut = new ArrayList<>();
    List<String> years = new ArrayList<String>();
    public Finance_Income income;
    public Finance_Out outcome;
    RecyclerView incomListV, outComeListV;
    public static Intent intent;
    private incomAdapter adapterIn;
    private outcomeAdapter adapterOut;
    private spinnerAdapter adapterSpinner;
    public static String idC;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_oldcashflow, container, false);
        db = new databaseHelper(view.getContext());
        Toast.makeText(view.getContext(), view.getContext().getString(R.string.oldcashflow), Toast.LENGTH_LONG).show();
        intent = getActivity().getIntent();
        idC = intent.getStringExtra("datas");
        listaS=view.findViewById(R.id.theList);
        oldCF = db.getOldCfbyUser(idC);
        years.add(view.getContext().getString(R.string.valasz));
        adapterSpinner = new spinnerAdapter(years,view.getContext());
        listaS.setAdapter(adapterSpinner);
        fillSpinner(oldCF);
        incomListV=view.findViewById(R.id.oldBev);
        outComeListV=view.findViewById(R.id.oldKiad);
        vage=view.findViewById(R.id.oldVage);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        incomListV.setLayoutManager(mLayoutManager);
        incomListV.setItemAnimator(new DefaultItemAnimator());
        incomListV.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapterIn = new incomAdapter(listItemsIn, view.getContext());
        incomListV.setAdapter(adapterIn);
        RecyclerView.LayoutManager oLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        outComeListV.setLayoutManager(oLayoutManager);
        outComeListV.setItemAnimator(new DefaultItemAnimator());
        outComeListV.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapterOut = new outcomeAdapter(listItemsOut, view.getContext());
        outComeListV.setAdapter(adapterOut);
        vage.setText(getText(R.string.mainOsszeg) + "0");
        listaS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position!=0)
                {
                    listItemsIn.clear();
                    listItemsOut.clear();
                    String [] seged=years.get(position).split("\\.");
                    int bevetel=0;
                    int kiadas=0;
                    Cursor kiadasC=db.getOldKiadasCfByDate(idC,seged[0],seged[1]);
                    Cursor bevetelC=db.getOldBevetelCfByDate(idC,seged[0],seged[1]);
                    Log.d("bevetél szám",":"+bevetelC.getCount());
                    Log.d("kiadás szám",":"+kiadasC.getCount());
                    if(bevetelC.getCount()>0) {
                        while (bevetelC.moveToNext()) {
                            Finance_Income i = new Finance_Income(bevetelC.getString(bevetelC.getColumnIndex("Name")), bevetelC.getString(bevetelC.getColumnIndex("Cost")), bevetelC.getString(bevetelC.getColumnIndex("ID")));
                            listItemsIn.add(i);
                            bevetel = bevetel + Integer.parseInt(bevetelC.getString(bevetelC.getColumnIndex("Cost")));
                        }

                    }
                    if(kiadasC.getCount()>0){
                    while (kiadasC.moveToNext()) {
                        Finance_Out i = new Finance_Out(kiadasC.getString(kiadasC.getColumnIndex("Name")), kiadasC.getString(kiadasC.getColumnIndex("Cost")), kiadasC.getString(kiadasC.getColumnIndex("ID")));
                        listItemsOut.add(i);
                        kiadas=kiadas+Integer.parseInt(kiadasC.getString(kiadasC.getColumnIndex("Cost")));
                    }
                    }
                    adapterOut.notifyDataSetChanged();
                    adapterIn.notifyDataSetChanged();
                    vage.setText(getText(R.string.mainOsszeg)+" "+(bevetel-kiadas));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        return view;
    }
    public void fillSpinner(String cf)
    {
        String[] oldCF_a=cf.split(",");
            for (int i = 0; i < oldCF_a.length; i++) {
                years.add(oldCF_a[i] + "." + oldCF_a[(i + 1)] + ". "+view.getResources().getString(R.string.honap));
                i++;
            }

            adapterSpinner.notifyDataSetChanged();

                }
            }