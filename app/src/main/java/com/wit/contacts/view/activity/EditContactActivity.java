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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/9/20.
 */
public class EditContactActivity extends Activity implements View.OnClickListener {
    private EditText userName;
    private EditText userPhone;
    private EditText userPhoneMore;
    private EditText userEmail;
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
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        initView();
        getUserInfo();
    }

    private void initView(){
        userName = (EditText)findViewById(R.id.contact_name_edit);
        userPhone = (EditText)findViewById(R.id.contact_phone_edit);
        userPhoneMore = (EditText)findViewById(R.id.contact_phone_more_edit);
        userEmail = (EditText)findViewById(R.id.contact_email_edit);
        userPosition = (EditText)findViewById(R.id.contact_position_edit);
        userGroup = (TextView) findViewById(R.id.contact_group_edit);

        backArrow = (ImageView)findViewById(R.id.back_arrow_edit);
        finishInput = (Button)findViewById(R.id.finish_edit);
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
         mUser.setPhoneMore(intent.getStringExtra("phonemore"));
         mUser.setEmail(intent.getStringExtra("email"));
         mUser.setPosition(intent.getStringExtra("position"));
         mUser.setGroupId(intent.getIntExtra("groupId", -1));

         userName.setText(mUser.getName());
         userPhone.setText(mUser.getPhone());
         userPhoneMore.setText(mUser.getPhoneMore());
         userEmail.setText(mUser.getEmail());
         userPosition.setText(mUser.getPosition());
         userGroup.setText(getGroupName(mUser.getGroupId()));
         mCurrentGroupId = mUser.getGroupId();
    }

    /**
     * 通过组的ID得到组的名称
     * */
    private String getGroupName(int id){
        String groupName = null;
        groupName = groupDao.selectGroupNameById(id);
        mCurrentGroup = groupName;
        return groupName;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_arrow_edit:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.finish_edit:
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
                    user.setId(mUser.getId());
                    user.setName(userName.getText().toString());
                    user.setPhone(userPhone.getText().toString());
                    user.setPhoneMore(userPhoneMore.getText().toString());
                    user.setEmail(userEmail.getText().toString());
                    user.setPosition(userPosition.getText().toString());
                    user.setGroupId(mCurrentGroupId);
                    userDao.updateUser(user);

                    Intent intent = new Intent();
                    intent.putExtra("id", user.getId());
                    intent.putExtra("name", user.getName());
                    intent.putExtra("phone", user.getPhone());
                    intent.putExtra("phonemore", user.getPhoneMore());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("position", user.getPosition());
                    intent.putExtra("groupId", user.getGroupId());
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                break;
            case R.id.contact_group_edit:
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
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
