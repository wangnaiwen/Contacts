package com.wit.contacts.view.tab;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.adapter.SortGroupMemberAdapter;
import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.GroupMemberBean;
import com.wit.contacts.bean.SystemContacts;
import com.wit.contacts.presenter.SystemContactsPresenter;
import com.wit.contacts.util.CharacterParser;
import com.wit.contacts.util.PinyinComparator;
import com.wit.contacts.view.activity.SystemContactsInfoActivity;
import com.wit.contacts.view.custom.ClearEditText;
import com.wit.contacts.view.custom.SideBar;
import com.wit.contacts.view.viewInterface.ISystemContactsView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wnw on 2016/10/1.
 */

public class SystemContactsTab implements ISystemContactsView, SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener,SectionIndexer {

    private View mView;
    private LayoutInflater mInflater;
    private Context mContext;

    private SwipeRefreshLayout mSwipeLayout;
    //private ListView mListView;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortGroupMemberAdapter adapter;
    private ClearEditText mClearEditText;

    private LinearLayout titleLayout;
    private TextView title;
    private TextView tvNofriends;

    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */

    private int lastFirstVisibleItem = -1;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<GroupMemberBean> SourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private SystemContactsPresenter presenter;

    private List<SystemContacts> mSystemContactsList = null;

    //private List<String> contactsList = new ArrayList<>();

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
        //mListView = (ListView)mView.findViewById(R.id.lv_system_contacts);
        sortListView = (ListView)mView.findViewById(R.id.lv_system_contacts);
        titleLayout = (LinearLayout)mView.findViewById(R.id.title_layout);
        title = (TextView)mView.findViewById(R.id.title_layout_catalog);
        tvNofriends = (TextView) mView.findViewById(R.id.title_layout_no_friends);
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) mView.findViewById(R.id.sidrbar);
        dialog = (TextView)mView. findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        mClearEditText = (ClearEditText) mView.findViewById(R.id.filter_edit);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        sortListView.setOnItemClickListener(this);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 这个时候不需要挤压效果 就把他隐藏掉
                if(s.toString().isEmpty()){
                    titleLayout.setVisibility(View.VISIBLE);
                }else {
                    titleLayout.setVisibility(View.GONE);
                }
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        SourceDateList = new ArrayList<>();
        loadSystemContactsFromDatabase();
    }

    /**
     * sort the data source
     * */
    private void sortDataSource(){
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortGroupMemberAdapter(mContext, SourceDateList);
        sortListView.setAdapter(adapter);
        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /**
                 * 当连续滑动ListView的时候，就隐藏搜索框
                 * */
                switch (scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        //接触屏幕
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        //开始滚动
                        mClearEditText.setVisibility(View.GONE);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        mClearEditText.setVisibility(View.VISIBLE);
                        //结束滚动
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int section = getSectionForPosition(firstVisibleItem);
                int nextSection = getSectionForPosition(firstVisibleItem + 1);
                int nextSecPosition = getPositionForSection(+nextSection);
                if (firstVisibleItem != lastFirstVisibleItem) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                            .getLayoutParams();
                    params.topMargin = 0;
                    titleLayout.setLayoutParams(params);
                    title.setText(SourceDateList.get(
                            getPositionForSection(section)).getSortLetters());
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = titleLayout.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            titleLayout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                titleLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;

                /**
                 * 处理滑动的时候ListView和SwipeLayout的冲突
                 * */
                View firstView = view.getChildAt(firstVisibleItem);
                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
                    mSwipeLayout.setEnabled(true);
                } else {
                    mSwipeLayout.setEnabled(false);
                }
            }
        });
    }

    /**
     * 为ListView填充数据
     *
     * @param dataList
     * @return
     */
    private List<GroupMemberBean> filledData(List<SystemContacts> dataList) {
        List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

        for (int i = 0; i < dataList.size(); i++) {
            GroupMemberBean sortModel = new GroupMemberBean();
            SystemContacts contacts = dataList.get(i);
            sortModel.setSystemContacts(contacts);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(contacts.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

        if (TextUtils.isEmpty(filterStr)) {                //输入为空
            filterDateList = SourceDateList;
            tvNofriends.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (GroupMemberBean sortModel : SourceDateList) {
                String name = sortModel.getSystemContacts().getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
        if (filterDateList.size() == 0) {
            tvNofriends.setVisibility(View.VISIBLE);
        }
    }

    /**
     * implements the function of SectionIndexer
     * */

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int i) {
        for (int j = 0; j < SourceDateList.size(); j++) {
            String sortStr = SourceDateList.get(j).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == i) {
                return j;
            }
        }
        return -1;
    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getSectionForPosition(int i) {
        GroupMemberBean bean = SourceDateList.get(i);
        String a = bean.getSortLetters();
        return a.charAt(0);
    }

    /**
     * loading the data from database
     * */
    private void loadSystemContactsFromDatabase(){
        presenter.loadSystemContacts();
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
        Log.d("wnw", contactses.size()+"");
        /*adapter.notifyDataSetChanged();*/
        loadSystemContacts();
    }

    @Override
    public void onRefresh() {
        //reload the data and update the data, ui
        new LoadSysContactsAsyncTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        GroupMemberBean bean = (GroupMemberBean)adapter.getItem(i);
        SystemContacts contacts = bean.getSystemContacts();
        Intent intent = new Intent(mContext, SystemContactsInfoActivity.class);
        intent.putExtra("id", contacts.getId());
        intent.putExtra("name", contacts.getName());
        intent.putExtra("phone", contacts.getPhone());
        intent.putExtra("phonemore", contacts.getPhoneMore());
        intent.putExtra("email", contacts.getEmail());

        mContext.startActivity(intent);
    }

    /**
     * return the view for HomeFragment
     * */
    public View getView(){
        return mView;
    }


    private void loadSystemContacts(){
        SourceDateList = filledData(mSystemContactsList);
        if(SourceDateList.size() != 0){
            sortDataSource();
            sideBar.setVisibility(View.VISIBLE);
        }else {
            sideBar.setVisibility(View.GONE);
        }
        //adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,contactsList);
    }

    /**
     * insert the data to databse
     * */
    private void insertSystemContacts(){
        presenter.insertSystemContacts(mSystemContactsList);
    }


    /**
     * use the thread to read system contacts
     * */
    private class LoadSysContactsAsyncTask extends AsyncTask<Void, Integer, List<SystemContacts>> {

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
        }

        @Override
        protected List<SystemContacts> doInBackground(Void... voids) {
            mSystemContactsList = readContacts();  // to use the function of read contacts
            insertSystemContacts();
            return mSystemContactsList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<SystemContacts> systemContactses) {
            mSwipeLayout.setRefreshing(false);
            loadSystemContacts();
        }
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
        /**
         * if read system contacts finish, and insert data todatabase  in this thread
         * */
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
