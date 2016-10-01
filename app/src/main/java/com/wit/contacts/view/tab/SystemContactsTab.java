package com.wit.contacts.view.tab;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.bean.SystemContacts;
import com.wit.contacts.presenter.SystemContactsPresenter;
import com.wit.contacts.view.viewInterface.ISystemContactsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/10/1.
 */

public class SystemContactsTab implements ISystemContactsView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{

    private View mView;
    private LayoutInflater mInflater;
    private Context mContext;

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;

    private SystemContactsPresenter presenter;

    private List<SystemContacts> mSystemContactsList = null;
    private ArrayAdapter<String> adapter;
    private List<String> contactsList = new ArrayList<>();

    public SystemContactsTab(LayoutInflater inflater){
        this.mInflater = inflater;
        mContext = mInflater.getContext();
        mView = mInflater.inflate(R.layout.tab_system_contacts, null);
        presenter = new SystemContactsPresenter(this);
        initView();
    }

    /**
     * init the view and add the listener for view
     * */
    private void initView(){
        mSwipeLayout = (SwipeRefreshLayout)mView.findViewById(R.id.swipe_layout);
        mListView = (ListView)mView.findViewById(R.id.lv_system_contacts);

        mSwipeLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                View firstView = absListView.getChildAt(i);

                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (i == 0 && (firstView == null || firstView.getTop() == 0)) {
                    mSwipeLayout.setEnabled(true);
                } else {
                    mSwipeLayout.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        //reload the data and update the data, ui
        loadSystemContacts();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    /**
     * return the view for HomeFragment
     * */
    public View getView(){
        return mView;
    }

    /**
     * loading the data from database
     * */
    private void loadSystemContacts(){
        mSystemContactsList = readContacts();
        int length = mSystemContactsList.size();
        List<String> list = new ArrayList<>();
        for(int i = 0 ; i < length; i++){
            list.add(mSystemContactsList.get(i).getName()+"\n"+mSystemContactsList.get(i).getPhone());
        }
        insertSystemContacts();
        contactsList = list;
        for(int i  = 0; i < contactsList.size(); i++){
            Log.d("wnw",contactsList.get(i));
        }
        adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,contactsList);
        mListView.setAdapter(adapter);
        mSwipeLayout.setRefreshing(false);
    }

    /**
     * insert the data to databse
     * */
    private void insertSystemContacts(){
        presenter.insertSystemContacts(mSystemContactsList);
    }

    @Override
    public void showLoading() {
        Toast.makeText(mContext, "loading system contacts", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUsers(List<SystemContacts> contactses) {
        if(mSystemContactsList == null){
            mSystemContactsList = new ArrayList<>();
        }
        mSystemContactsList = contactses;
        adapter.notifyDataSetChanged();
    }

    /**
     * use the content provider to load the system contacts
     * */
    private List<SystemContacts> readContacts() {
        List<SystemContacts> contactsList = new ArrayList<>();
        Cursor cursor = null;
        try {

            //查询联系人数据
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            cursor = mContext.getContentResolver().query(uri, null, null, null, null);

            while (cursor.moveToNext()) {
                SystemContacts systemContact = new SystemContacts();
                //get the contacts' id, name and email
                String contactsId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                systemContact.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                String email =readEmail(contactsId);
                systemContact.setEmail(email);

                //get the count of number of this contacts
                int count = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if(count > 0){
                    Uri uri1 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    Cursor phoneCursor = mContext.getContentResolver().query(uri1,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +"=" + contactsId , null, null);
                    int i = 0;
                    while (phoneCursor.moveToNext()){
                        if(i == 0){
                            systemContact.setPhone(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        }else if(i == 1){
                            systemContact.setPhoneMore(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        }
                        i ++;
                    }
                    phoneCursor.close();
                    contactsList.add(systemContact);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contactsList;
    }

    /**
     * get the email fo the contacts base on contacts's id
     * */
    private String  readEmail(String id){
        String email = "";
        Uri uri1 = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(uri1, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + id, null, null);
        while (cursor.moveToNext()) {
            email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }
        return email;
    }
}
