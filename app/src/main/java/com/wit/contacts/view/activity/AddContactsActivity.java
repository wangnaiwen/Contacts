package com.wit.contacts.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wit.contacts.R;
import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.User;
import com.wit.contacts.dao.GroupDao;
import com.wit.contacts.dao.GroupDaoImp;
import com.wit.contacts.dao.UserDao;
import com.wit.contacts.dao.UserDaoImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/9/4.
 */

public class AddContactsActivity extends Activity implements View.OnClickListener{
    private EditText userName;
    private EditText userPhone;
    private EditText userPosition;
    private RelativeLayout userGroup;

    private ImageView backArrow;
    private Button finishInput;
    private TextView pickGroupName;
    private AlertDialog myDialog;
    private String mCurrentGroup = null;
    private int mCurrentGroupId;
    List<String> list = new ArrayList<>();
    List<Group> groups;
    /**
     * 点击完成按钮插入数据库
     * */
    private GroupDao groupDao;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        initView();
    }

    private void initView(){
        userName = (EditText)findViewById(R.id.contact_name);
        userPhone = (EditText)findViewById(R.id.contact_phone);
        userPosition = (EditText)findViewById(R.id.contact_position);
        userGroup = (RelativeLayout)findViewById(R.id.contact_group);
        pickGroupName = (TextView)findViewById(R.id.pick_group_name);

        backArrow = (ImageView)findViewById(R.id.back_arrow);
        finishInput = (Button)findViewById(R.id.finish_input);
        backArrow.setOnClickListener(this);
        finishInput.setOnClickListener(this);
        userGroup.setOnClickListener(this);

        groupDao = new GroupDaoImp();
        userDao = new UserDaoImp();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_arrow:
                finish();
                break;
            case R.id.finish_input:
                if(userName.getText().toString().isEmpty()){
                    userName.setHint("请输入联系人姓名");
                    userName.setHintTextColor(Color.RED);
                }else if( userPhone.getText().toString().isEmpty()){
                    userPhone.setHint("请输入联系人电话");
                    userPhone.setHintTextColor(Color.RED);
                }else if(mCurrentGroup == null){
                    //将数据插入数据库
                    pickGroupName.setHint("请选择分组");
                    pickGroupName.setHintTextColor(Color.RED);
                }else {
                    User user = new User();
                    user.setName(userName.getText().toString());
                    user.setPhone(userPhone.getText().toString());
                    user.setPosition(userPosition.getText().toString());
                    user.setGroupId(mCurrentGroupId);
                    userDao.insertUser(user);
                    finish();
                }
                break;
            case R.id.contact_group:
                //弹出选择组别的选择框
                showDialog();
                break;
            default:
                break;
        }
    }
    private void showDialog(){
        groups = groupDao.selectAllGroup();
        for(int  i = 0; i < groups.size(); i++){
            list.add(groups.get(i).getName());
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout linearLayout =(LinearLayout) inflater.inflate(R.layout.dialog_pick_group,null);
        ListView listView = (ListView)linearLayout.findViewById(R.id.lv_dialog);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.dialog_lv_item, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentGroup = list.get(i);
                pickGroupName.setText(mCurrentGroup);
                mCurrentGroupId = groups.get(i).getId();
                myDialog.dismiss();
            }
        });
        myDialog = new AlertDialog.Builder(this).create();
        myDialog.setView(linearLayout);
        myDialog.show();
    }
/*
    *//**
     * 判断手机号码是否合理
     * 1. 判断字符串的位数
     * 2. 验证手机号码格式
     * *//*
    public boolean isPhoneNums(String phoneNums){
        if(isMatchLength(phoneNums,11) && isMobileNums(phoneNums)){
            return true;
        }
        return false;
    }

    *//**
     * 判断字符串的位数是不是
     * *//*
    public static boolean isMatchLength(String str,int length){
        if(str.isEmpty()){
            return false;
        }else{
            return str.length() == length ? true: false;
        }
    }

    *//**
     * 验证手机格式
     * *//*
    public static boolean isMobileNums(String mobileNums){
        *//**
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         *//*

        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][358]\\d{9}";

        if(TextUtils.isEmpty(mobileNums)){
            return false;
        }else{
            return mobileNums.matches(telRegex);
        }
    }*/
}
