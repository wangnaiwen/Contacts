package com.wit.contacts.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.adapter.TabPagerAdapter;
import com.wit.contacts.bean.NetUser;
import com.wit.contacts.bean.SystemContacts;
import com.wit.contacts.bean.User;
import com.wit.contacts.dao.SystemContactsDao;
import com.wit.contacts.dao.SystemContactsDapImp;
import com.wit.contacts.dao.UserDao;
import com.wit.contacts.dao.UserDaoImp;
import com.wit.contacts.data.ContactDatabaseHelper;
import com.wit.contacts.data.FileRecord;
import com.wit.contacts.presenter.SynPresenter;
import com.wit.contacts.view.tab.LocalContactsTab;
import com.wit.contacts.view.tab.SystemContactsTab;
import com.wit.contacts.view.viewInterface.ISynView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ISynView{

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private TabLayout mTabLayout = null;
    private TextView userName;
    private TextView phone;

    private ViewPager mViewPager = null;
    private LayoutInflater mInflater = null;
    private Toolbar toolbar;

    private List<View> mTabViewList = new ArrayList<>();   // 页卡View集合
    private List<String> mTabTitleList = new ArrayList();  //页卡title集合

    private LocalContactsTab mLocalContactsTab = null;    // 两个页卡View
    private SystemContactsTab mSystemContactsTab = null;

    private UserDao userDao;
    private SystemContactsDao systemContactsDao;

    private SynPresenter synPresenter;

    private NetUser netUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        netUser = (NetUser)getIntent().getSerializableExtra("netUser");
        initView();

        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalContactsTab.reLoadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_contacts) {
            Toast.makeText(this,"Add One",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AddContactsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            return true;
        }else if(id == R.id.action_add_group){
            Toast.makeText(this,"Add Group",Toast.LENGTH_SHORT).show();
            showAGroupDialog();
            return true;
        }/*else if(id == R.id.action_read_file){
            //open another activity
            Intent intent = new Intent(this, ReadFileActivity.class);
            startActivity(intent);
        }else if (id == R.id.action_save_local){
            Toast.makeText(this, "正在拼命保存中",Toast.LENGTH_SHORT).show();
            saveLocal();
        }*/
        else if(id == android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){

        synPresenter = new SynPresenter(this, this);

        toolbar = (Toolbar)findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        /*ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }*/
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_syn:
                        mDrawerLayout.closeDrawers();
                        synPresenter.load();
                        break;
                    case R.id.nav_setting:
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                }
                return false;
            }
        });
        userName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.username);
        phone = (TextView)navigationView.getHeaderView(0).findViewById(R.id.phone);
        phone.setText(netUser.getPhone());
        userName.setText(netUser.getName());

        mTabLayout = (TabLayout)findViewById(R.id.layout_tab);
        mViewPager = (ViewPager)findViewById(R.id.vp_view);

        mInflater = LayoutInflater.from(this);

        mLocalContactsTab = new LocalContactsTab(mInflater);     //初始化
        mSystemContactsTab = new SystemContactsTab(mInflater);

        mTabViewList.add(mSystemContactsTab.getView());
        mTabViewList.add(mLocalContactsTab.getView());           //将View添加到List中

        mTabTitleList.add("系统联系人");
        mTabTitleList.add("本地联系人");                         //将Title添加到List中

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);       //设置页卡模式

        mTabLayout.addTab(mTabLayout.newTab().setText(mTabTitleList.get(0)));   //将卡页添加到View中
        mTabLayout.addTab(mTabLayout.newTab().setText(mTabTitleList.get(1)));

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(mTabViewList, mTabTitleList);
        mViewPager.setAdapter(tabPagerAdapter);              //给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);           //将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(tabPagerAdapter); //给Tabs设置适配器

        userDao = new UserDaoImp();
        systemContactsDao = new SystemContactsDapImp();

        mLocalContactsTab.reLoadData();
    }
    private AlertDialog addGroupDialog;
    private void showAGroupDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = null;
        dialogView = inflater.inflate(R.layout.dialog_add_group, null);
        final EditText addGroupEditText = (EditText)dialogView.findViewById(R.id.dialog_add_group_name);

        builder.setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if( addGroupEditText.getText().toString().trim().equals("")){
                            addGroupEditText.setHint("请输入组名称");
                            addGroupEditText.setHintTextColor(Color.RED);
                        }else{
                            //插入数据库，并且销毁Dialog
                            mLocalContactsTab.insertGroup(addGroupEditText.getText().toString());
                            addGroupDialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addGroupDialog.dismiss();
                    }
                });
        builder.setTitle("请输入组名");
        addGroupDialog = builder.create();
        addGroupDialog.show();
    }

    private void saveLocal(){
        List<User> users = userDao.selectAllUser();
        List<SystemContacts> systemContactses = systemContactsDao.selectAllSystemContacts();
        if(users != null){
            for(int i = 0; i < users.size(); i ++){
                String name = users.get(i).getName();
                String phone = users.get(i).getPhone();
                recordData(name);
                recordData(phone);
            }
        }
        if(systemContactses != null){
            for(int i = 0; i < systemContactses.size(); i ++){
                String name = systemContactses.get(i).getName();
                String phone = systemContactses.get(i).getPhone();
                recordData(name);
                recordData(phone);
            }
        }
        Toast.makeText(this, "已保存到文件管理的根目录Contacts.txt文件",Toast.LENGTH_LONG).show();
    }
    FileRecord fileRecord = null;
    private void recordData(String data){
        try{
            if(fileRecord == null){
                fileRecord = new FileRecord("/mnt/sdcard/Contacts.txt");
            }
            fileRecord.Write(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void showLoading() {
        Toast.makeText(this, "正在同步....",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showFinishLoading() {
        Toast.makeText(this, "同步完成",Toast.LENGTH_SHORT).show();
    }
}
