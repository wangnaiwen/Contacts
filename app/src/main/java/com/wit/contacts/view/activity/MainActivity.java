package com.wit.contacts.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.bean.Group;
import com.wit.contacts.dao.GroupDao;
import com.wit.contacts.dao.GroupDaoImp;
import com.wit.contacts.data.ContactDatabaseHelper;
import com.wit.contacts.view.fragment.ColleagueFragment;
import com.wit.contacts.view.fragment.DiscoverFragment;
import com.wit.contacts.view.fragment.HomeFragment;
import com.wit.contacts.view.fragment.MeFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // 4 button selected code
    public static int SELECTED_HOME = 0;
    public static int SELECTED_FRIEND = 1;
    public static int SELECTED_DISCOVER = 2;
    public static int SELECTED_ME = 3;

    //current selected;
    private int SELECTED_CUR;
    private Fragment currentFragment;

    // 4 bottom on bottom_bar,  contain imageview and text view
    private LinearLayout homeBtn;
    private LinearLayout colleagueBtn;
    private LinearLayout discoverBtn;
    private LinearLayout meBtn;

    private ImageView homeImg;
    private ImageView colleagueImg;
    private ImageView discoverImg;
    private ImageView meImg;

    private TextView homeText;
    private TextView colleagueText;
    private TextView discoverText;
    private TextView meText;

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private ColleagueFragment colleagueFragment;
    private DiscoverFragment discoverFragment;
    private MeFragment meFragment;

    /**
     * 数据库操作：新建一个组到数据库中
     * */
    private GroupDao groupDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        createDB();
        setDefaultFragment();
    }

    //create database
    private void createDB(){
        ContactDatabaseHelper.getInstance(this);
    }

    //set default fragment: Homefragment
    private void setDefaultFragment(){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        homeFragment = new HomeFragment();
        transaction.replace(R.id.fragment_pager, homeFragment);
        transaction.commit();
        SELECTED_CUR = SELECTED_HOME;
        currentFragment = homeFragment;
    }

    //init 4 btn view and add listener for botton
    private void initView(){
        homeBtn = (LinearLayout)findViewById(R.id.bottom_btn_home);
        colleagueBtn = (LinearLayout)findViewById(R.id.bottom_btn_friend);
        discoverBtn = (LinearLayout)findViewById(R.id.bottom_btn_discover);
        meBtn = (LinearLayout)findViewById(R.id.bottom_btn_me);

        homeImg = (ImageView)findViewById(R.id.imageview_home);
        colleagueImg = (ImageView)findViewById(R.id.imageview_friend);
        discoverImg = (ImageView)findViewById(R.id.imageview_discover);
        meImg = (ImageView)findViewById(R.id.imageview_me);

        homeText = (TextView)findViewById(R.id.textview_home);
        colleagueText = (TextView)findViewById(R.id.textview_friend);
        discoverText = (TextView)findViewById(R.id.textview_discover);
        meText = (TextView)findViewById(R.id.textview_me);

        homeBtn.setOnClickListener(this);
        colleagueBtn.setOnClickListener(this);
        discoverBtn.setOnClickListener(this);
        meBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bottom_btn_home:
                if(SELECTED_CUR != SELECTED_HOME){
                    SELECTED_CUR = SELECTED_HOME;                //set current selected
                    resetBtn();
                    homeImg.setImageResource(R.drawable.btn_home_pred);
                    homeText.setTextColor(getResources().getColor(R.color.color_btn_selected));
                    if(homeFragment == null) {
                        homeFragment = new HomeFragment();
                    }
                    switchFragment(currentFragment, homeFragment);
                }
                break;
            case R.id.bottom_btn_friend:
                if(SELECTED_CUR != SELECTED_FRIEND){
                    SELECTED_CUR = SELECTED_FRIEND;
                    resetBtn();
                    colleagueImg.setImageResource(R.drawable.btn_colleague_pred);
                    colleagueText.setTextColor(getResources().getColor(R.color.color_btn_selected));
                    if(colleagueFragment == null) {
                        colleagueFragment = new ColleagueFragment();
                    }
                    switchFragment(currentFragment, colleagueFragment);
                }
                break;
            case R.id.bottom_btn_discover:
                if(SELECTED_CUR != SELECTED_DISCOVER){
                    SELECTED_CUR = SELECTED_DISCOVER;
                    resetBtn();
                    discoverImg.setImageResource(R.drawable.btn_discover_pred);
                    discoverText.setTextColor(getResources().getColor(R.color.color_btn_selected));
                    if(discoverFragment == null) {
                        discoverFragment = new DiscoverFragment();
                    }
                    switchFragment(currentFragment, discoverFragment);
                }
                break;
            case R.id.bottom_btn_me:
                if(SELECTED_CUR != SELECTED_ME){
                    SELECTED_CUR = SELECTED_ME;
                    resetBtn();
                    meImg.setImageResource(R.drawable.btn_me_pred);
                    meText.setTextColor(getResources().getColor(R.color.color_btn_selected));

                    if(meFragment == null) {
                        meFragment = new MeFragment();
                    }
                    switchFragment(currentFragment, meFragment);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 在这里对Fragment进行切换，这个切换方式代替了replace，以至于每次切换的时候，不用重新实例化Fragment
     * */
    public void switchFragment(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out);
            if (!to.isAdded()) {	// 先判断是否被add过
                transaction.hide(from).add(R.id.fragment_pager, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
    /**
     * reset the btn text color and imageview:
     * 1. reset all btn to normal text color and image view
     * */
    private void resetBtn(){
        homeImg.setImageResource(R.drawable.btn_home_nor);
        homeText.setTextColor(getResources().getColor(R.color.color_btn_normal));

        colleagueImg.setImageResource(R.drawable.btn_colleague_nor);
        colleagueText.setTextColor(getResources().getColor(R.color.color_btn_normal));

        discoverImg.setImageResource(R.drawable.btn_discover_nor);
        discoverText.setTextColor(getResources().getColor(R.color.color_btn_normal));

        meImg.setImageResource(R.drawable.btn_me_nor);
        meText.setTextColor(getResources().getColor(R.color.color_btn_normal));
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
            Intent intent = new Intent(MainActivity.this, AddContactsActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_add_group){
            Toast.makeText(this,"Add Group",Toast.LENGTH_SHORT).show();
            showDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog myDialog;
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = null;
         // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogView = inflater.inflate(R.layout.dialog_add_group, null);
        final EditText addGroupEditText = (EditText)dialogView.findViewById(R.id.dialog_add_group_name);

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("wnw", "null");
                        // sign in the user ...
                        if( addGroupEditText.getText().toString().trim().equals("")){
                            addGroupEditText.setHint("请输入组名称");
                            addGroupEditText.setHintTextColor(Color.RED);
                            Log.d("wnw1",addGroupEditText.getText().toString());
                        }else{
                            //插入数据库，并且销毁Dialog
                            Log.d("wnw2",addGroupEditText.getText().toString());
                            saveGroupToDB(addGroupEditText.getText().toString());
                            myDialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myDialog.dismiss();
                    }
                });
        builder.setTitle("请输入组名");
        myDialog = builder.create();
        myDialog.show();
    }

    private void saveGroupToDB(String groupName){
        if(groupDao == null){
            groupDao = new GroupDaoImp();
        }
        groupDao.insertGroup(new Group(groupName, null));
        homeFragment.loadGroup();
    }
}
