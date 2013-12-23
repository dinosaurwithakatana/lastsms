package com.dwak.lastsms;

/**
 * Created by vishnu on 12/22/13.
 */
public class SMSData {
    private String mNumber;
    private String mBody;
    private String mSenderName;

    public String getSenderName() {
        return mSenderName;
    }

    public void setSenderName(String senderName) {
        mSenderName = senderName;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }
}
