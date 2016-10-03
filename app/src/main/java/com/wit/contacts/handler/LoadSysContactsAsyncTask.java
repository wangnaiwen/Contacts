package com.wit.contacts.handler;

import android.os.AsyncTask;

import com.wit.contacts.bean.SystemContacts;

import java.util.List;

/**
 * Created by wnw on 2016/10/2.
 */

public class LoadSysContactsAsyncTask extends AsyncTask<Void, Integer, List<SystemContacts>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<SystemContacts> systemContactses) {
        super.onPostExecute(systemContactses);
    }

    @Override
    protected List<SystemContacts> doInBackground(Void... voids) {
        return null;
    }

}
