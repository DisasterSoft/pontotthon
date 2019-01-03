package com.ewulusen.disastersoft.pontottohon;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

public class incomAdapter extends RecyclerView.Adapter<incomAdapter.MyViewHolder> {
    public List<Finance_Income> incomesList;
    private Context mContext;

    View view2;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, menny;
        public RelativeLayout viewBackground;
        public RelativeLayout viewForeground;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nevIncome);
            menny = (TextView) view.findViewById(R.id.costIncome);
            viewBackground = view.findViewById(R.id.view_backgroundIncome);
            viewForeground = view.findViewById(R.id.view_foregroundIncome);
            view2=view;
        }
    }


    public incomAdapter(List<Finance_Income> incomesList,Context context) {
        this.mContext=context;
        this.incomesList = incomesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simplerowincome, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Finance_Income income = incomesList.get(position);
        holder.name.setText(income.getName());
        holder.menny.setText(income.getCost()+mContext.getString(R.string.penz));

    }

    @Override
    public int getItemCount() {
        return incomesList.size();
    }

    public  void removeItem(int position) {
        incomesList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Finance_Income income, int position) {
        incomesList.add(position, income);
        // notify item added by position
        notifyItemInserted(position);
    }
}