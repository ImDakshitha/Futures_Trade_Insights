package com.example.cryptofuturestrader;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    float tsum;
    private List<Trades> tradeList;
    private TextView tvPNLExt;

    public PostAdapter(List<Trades> tradesList,TextView extPNL) {
        this.tradeList = tradesList;
        this.tvPNLExt = extPNL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int count = getItemCount();
        tsum = 0;
        for (int i = 0; i < count; i++){
            float price = Float.parseFloat(tradeList.get(i).getRealizedPnl());
            tsum = tsum + price;
        }

        tvPNLExt.setText(String.valueOf(tsum) );
        if(tradeList.get(position).getSide().equals("BUY") && tradeList.get(position).getPositionSide().equals("LONG")||
                tradeList.get(position).getSide().equals("SELL") && tradeList.get(position).getPositionSide().equals("SHORT")){
            holder.itemView.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0;
            holder.itemView.setLayoutParams(params);
//            tradeList.remove(holder.getAdapterPosition());
//            notifyItemRemoved(holder.getAdapterPosition());
//            notifyItemRangeChanged(holder.getAdapterPosition(), tradeList.size());

        }
        else{
            holder.tvpair.setText(tradeList.get(position).getSymbol());
            if(tradeList.get(position).getSide().equals("BUY")){
                holder.tvdirection.setText("LONG");
                holder.tvdirection.setTextColor(Color.parseColor("#A4C639"));
            }
            else {
                holder.tvdirection.setText("SHORT");
                holder.tvdirection.setTextColor(Color.parseColor("#FF0000"));
            }
            if(Float.parseFloat(tradeList.get(position).getRealizedPnl())>0){
                holder.tv_pnl.setText(tradeList.get(position).getRealizedPnl());
                holder.tv_pnl.setTextColor(Color.parseColor("#A4C639"));
            }
            else {
                holder.tv_pnl.setTextColor(Color.parseColor("#FF0000"));
                holder.tv_pnl.setText(tradeList.get(position).getRealizedPnl());
            }
//            holder.tvdirection.setText(tradeList.get(position).getSide());
            holder.tv_open_price.setText(tradeList.get(position).getPrice());
            holder.tv_closed_price.setText(tradeList.get(position).getQty());
            holder.tv_date.setText(tradeList.get(position).getTime());
        }
    }


    @Override
    public int getItemCount() {
        return tradeList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvpair;
        TextView tvdirection;
        TextView tv_pnl;

        TextView tv_open_price;
        TextView tv_closed_price;
        TextView tv_date;
        TextView tv_summary;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvpair = itemView.findViewById(R.id.tv_pair);
            tvdirection =itemView.findViewById(R.id.tv_buy_sell);
            tv_pnl= itemView.findViewById(R.id.tv_pnl);
            tv_open_price =itemView.findViewById(R.id.tv_entry_price);
            tv_closed_price = itemView.findViewById(R.id.tv_exit_price);
            tv_date =itemView.findViewById(R.id.tv_trade_date);


        }

    }
}
