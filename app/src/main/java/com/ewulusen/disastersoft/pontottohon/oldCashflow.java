package com.ewulusen.disastersoft.pontottohon;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    ArrayAdapter<String> dataAdapter;
    public Finance_Income income;
    public Finance_Out outcome;
    RecyclerView incomListV, outComeListV;
    public static Intent intent;
    private incomAdapter adapterIn;
    private outcomeAdapter adapterOut;
    public static String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_oldcashflow, container, false);
        db = new databaseHelper(view.getContext());
        Toast.makeText(view.getContext(), view.getContext().getString(R.string.hitelWelcome), Toast.LENGTH_LONG).show();
        intent = getActivity().getIntent();
        id = intent.getStringExtra("datas");
        listaS=view.findViewById(R.id.theList);
        oldCF = db.getOldCfbyUser(id);
        dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, years);
        fillSpinner(oldCF);
        return view;
    }
    public void fillSpinner(String cf)
    {
        String[] oldCF_a=cf.split(",");
            for (int i = 0; i < oldCF_a.length; i++) {
                years.add(oldCF_a[i] + "." + oldCF_a[(i + 1)] + ". "+view.getResources().getString(R.string.honap));
                i++;
            }

            listaS.setAdapter(dataAdapter);

                }
            }