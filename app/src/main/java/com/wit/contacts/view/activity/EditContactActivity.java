package com.wit.contacts.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
 * Created by wnw on 2016/9/20.
 */
public class EditContactActivity extends Activity implements View.OnClickListener {
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

    List<Group> groups;
    /**
     * 点击完成按钮插入数据库
     * */
    private GroupDao groupDao;
    private UserDao userDao;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        initView();
        getUserInfo();
    }

    private void initView(){
        userName = (EditText)findViewById(R.id.edit_contact_name);
        userPhone = (EditText)findViewById(R.id.edit_contact_phone);
        userPosition = (EditText)findViewById(R.id.edit_contact_position);
        userGroup = (RelativeLayout)findViewById(R.id.edit_contact_group);
        pickGroupName = (TextView)findViewById(R.id.edit_contact_pick_group_name);

        backArrow = (ImageView)findViewById(R.id.edit_contact_back_arrow);
        finishInput = (Button)findViewById(R.id.edit_contact_finish_input);
        backArrow.setOnClickListener(this);
        finishInput.setOnClickListener(this);
        userGroup.setOnClickListener(this);

        groupDao = new GroupDaoImp();
        userDao = new UserDaoImp();
    }

    /**
     * 得到从UserDetailInfoActivity传递过来的用户的信息
     * */
     private void getUserInfo(){
         mUser = new User();
         Intent intent = getIntent();
         mUser.setId(intent.getIntExtra("id", -1));
         mUser.setName(intent.getStringExtra("name"));
         mUser.setPhone(intent.getStringExtra("phone"));
         mUser.setPosition(intent.getStringExtra("position"));
         mUser.setGroupId(intent.getIntExtra("groupId", -1));
         Log.d("wnw", mUser.getId() + mUser.getName()+ mUser.getPhone()+mUser.getPosition()+mUser.getGroupId());

         userName.setText(mUser.getName());
         userPhone.setText(mUser.getPhone());
         userPosition.setText(mUser.getPosition());
         pickGroupName.setText(getGroupName(mUser.getGroupId()));
    }

    /**
     * 通过组的ID得到组的名称
     * */
    private String getGroupName(int id){
        String groupName = null;

        return groupName;
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
                    Log.d("wnw", "you come here?");
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
        final List<String> list = new ArrayList<>();
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
}
