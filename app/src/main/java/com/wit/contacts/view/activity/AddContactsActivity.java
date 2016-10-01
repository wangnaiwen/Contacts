package com.wit.contacts.view.activity;

import android.app.Activity;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/9/4.
 */

public class AddContactsActivity extends Activity implements View.OnClickListener{
    private EditText userName;
    private EditText userPhone;
    private EditText userPosition;
    private TextView userGroup;

    private ImageView backArrow;
    private Button finishInput;
    private AlertDialog myDialog;
    private String mCurrentGroup = null;
    private int mCurrentGroupId;

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
        userGroup = (TextView) findViewById(R.id.pick_group_name);

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
                    userGroup.setHint("请选择分组");
                    userGroup.setHintTextColor(Color.RED);
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
            case R.id.pick_group_name:
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
                userGroup.setText(mCurrentGroup);
                mCurrentGroupId = groups.get(i).getId();
                myDialog.dismiss();
            }
        });

        myDialog = new AlertDialog.Builder(this).create();
        myDialog.setView(linearLayout);
        myDialog.show();
    }
}
