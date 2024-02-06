package com.example.cryptofuturestrader;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trades {
    public boolean buyer;
    public String commission;
    public String commissionAsset;

    public long id;
    public boolean maker;
    public long orderId;
    public String price;
    public String qty;
    public String quoteQty;
    public String realizedPnl;
    public String side;
    public String positionSide;
    public String symbol;
    public long time;

    public boolean isBuyer() {
        return buyer;
    }

    public String getCommission() {
        return commission;
    }

    public String getCommissionAsset() {
        return commissionAsset;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public boolean isMaker() {
        return maker;
    }

    public String getOrderId() {
        return String.valueOf(id);
    }

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }

    public String getQuoteQty() {
        return quoteQty;
    }

    public String getRealizedPnl() {
        String pnl = String.format("%.2g%n", Float.parseFloat(realizedPnl));
        return pnl;
    }

    public String getSide() {
        return side;
    }

    public String getPositionSide() {
        return positionSide;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTime() {

        long time1 = this.time;
        Date time= new Date(time1);
        DateFormat f = new SimpleDateFormat("dd/MM/yy");
//                hh:mm:ss");
        return String.valueOf(f.format(time));

    }
}