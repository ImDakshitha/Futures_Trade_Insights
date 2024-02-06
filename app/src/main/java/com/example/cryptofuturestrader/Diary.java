package com.example.cryptofuturestrader;

public class Diary {
    String Pair;
    String Date;
    String Time;
    String EntryPrice;
    String ExitPrice;
    String Qty;



    String Direction;

    public Diary() {
    }

    public String getPair() {
        return Pair;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getEntryPrice() {
        return EntryPrice;
    }

    public String getExitPrice() {
        return ExitPrice;
    }

    public String getQty() {
        return Qty;
    }

    public String getDirection() {
        return Direction;
    }

    public void setPair(String pair) {
        Pair = pair;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setEntryPrice(String entryPrice) {
        EntryPrice = entryPrice;
    }

    public void setExitPrice(String exitPrice) {
        ExitPrice = exitPrice;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }
}

