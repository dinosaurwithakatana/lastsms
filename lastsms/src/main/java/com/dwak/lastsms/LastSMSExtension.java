package com.dwak.lastsms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class LastSMSExtension extends DashClockExtension {

    private static final String TAG = LastSMSExtension.class.getSimpleName();
    private SMSData mSMSData;
    public static final String PREF_PRIVACY = "pref_privacy";
    private SharedPreferences mSharedPreferences;
    private boolean isInPrivacyMode;

    @Override
    protected void onUpdateData(int arg0) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        isInPrivacyMode = mSharedPreferences.getBoolean("pref_privacy", false);
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        Cursor cur2;
        Log.d(TAG, "LastSMS onUpdateData");

        if (c != null) {
            if(c.getCount() != 0){
                c.moveToFirst();
                mSMSData = new SMSData();
                mSMSData.setBody(c.getString(c.getColumnIndexOrThrow("body")));
                mSMSData.setNumber(c.getString(c.getColumnIndexOrThrow("address")));
                Log.d(TAG, mSMSData.getNumber() + " " + mSMSData.getBody());
                Uri uri2 = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("tel:" + mSMSData.getNumber()));
                cur2 = getContentResolver().query(uri2, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

                if (cur2 != null) {
                    if (cur2.moveToFirst()) {
                        mSMSData.setSenderName(cur2.getString(0));
                    } else {
                        mSMSData.setSenderName("Unknown");
                    }
                } else {
                    mSMSData.setSenderName("Unknown");
                }

                if(cur2 != null){
                    cur2.close();
                }
            }
            c.close();
        }

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        String expandedBody = isInPrivacyMode ? "" : mSMSData.getBody();
        publishUpdate(new ExtensionData()
                .visible(true)
                .icon(R.drawable.ic_launcher)
                .status(mSMSData.getSenderName())
                .expandedBody(expandedBody)
                .clickIntent(sendIntent));
    }

}
