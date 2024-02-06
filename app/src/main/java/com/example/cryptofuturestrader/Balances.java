package com.example.cryptofuturestrader;

public class Balances {
    public String accountAlias;
    public String asset;
    public String availableBalance;
    public String balance;
    public String crossUnPnl;
    public String crossWalletBalance;
    public boolean marginAvailable;
    public String maxWithdrawAmount;
    public long updateTime;

    public String getAccountAlias() {
        return accountAlias;
    }

    public String getAsset() {
        return asset;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public String getBalance() {
        return balance;
    }

    public String getCrossUnPnl() {
        return crossUnPnl;
    }

    public String getCrossWalletBalance() {
        return crossWalletBalance;
    }

    public boolean isMarginAvailable() {
        return marginAvailable;
    }

    public String getMaxWithdrawAmount() {
        return maxWithdrawAmount;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
