package com.ewulusen.disastersoft.pontottohon;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

public class outcomeAdapter extends RecyclerView.Adapter<outcomeAdapter.MyViewHolder> {
    private List<Finance_Out> outList;
    private Context mContext;

    View view2;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, menny;
        public RelativeLayout viewBackground;
        public RelativeLayout viewForeground;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nevOutcome);
            menny = (TextView) view.findViewById(R.id.costOutcome);
            viewBackground = view.findViewById(R.id.view_backgroundOutcome);
            viewForeground = view.findViewById(R.id.view_foregroundOutcome);
            view2=view;
        }
    }


    public outcomeAdapter(List<Finance_Out> moviesList,Context cn) {
        this.mContext=cn;
        this.outList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simplerowout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Finance_Out outCome = outList.get(position);
        holder.name.setText(outCome.getName());
        holder.menny.setText(outCome.getCost()+mContext.getString(R.string.penz));
    }

    @Override
    public int getItemCount() {
        return outList.size();
    }

    public void removeItem(int position) {
        outList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Finance_Out outCome, int position) {
        outList.add(position, outCome);
        // notify item added by position
        notifyItemInserted(position);
    }
}