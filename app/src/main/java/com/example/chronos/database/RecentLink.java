package com.example.chronos.database;

public class RecentLink {
    private long DateTime;
    private String NickName;
    private String Account;
    private String Message;
    private int UnreadTip;

    public RecentLink(long dateTime, String nickName, String account, String message, int unreadTip) {
        DateTime = dateTime;
        NickName = nickName;
        Account = account;
        Message = message;
        UnreadTip = unreadTip;
    }

    public long getDateTime() {
        return DateTime;
    }

    public void setDateTime(long dateTime) {
        DateTime = dateTime;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getUnreadTip() {
        return UnreadTip;
    }

    public void setUnreadTip(int unreadTip) {
        UnreadTip = unreadTip;
    }
}
