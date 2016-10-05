package com.wit.contacts.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wit.contacts.R;
import com.wit.contacts.bean.BlackList;
import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.SystemContacts;
import com.wit.contacts.bean.User;
import com.wit.contacts.dao.GroupDao;
import com.wit.contacts.dao.GroupDaoImp;
import com.wit.contacts.dao.UserDao;
import com.wit.contacts.dao.UserDaoImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/10/4.
 */

public class SystemContactsInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private SystemContacts mSystemContacts;
    private Toolbar mToolbar;

    private Button mDialContact;
    private ImageView mSendMsg;
    private Button mEmail;

    private Button mAddToLocal;
    private Button mShapeContact;
    private Button mAddBlackList;

    /**
     * 如果有两个号码,才会显示的第二个号码的布局
     * */
    private LinearLayout phoneMoreLayout;
    private Button phoneMoreBtn;
    private ImageView phoneMoreSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_contacts_detail);
        getUserInfo();
        initView();
    }

    /**
     * 得到从HomeFragment传递过来的用户信息
     * */
    private void getUserInfo(){
        mSystemContacts = new SystemContacts();
        Intent intent = getIntent();

        mSystemContacts.setId(intent.getIntExtra("id", -1));
        mSystemContacts.setName(intent.getStringExtra("name"));
        mSystemContacts.setPhone(intent.getStringExtra("phone"));
        mSystemContacts.setPhoneMore(intent.getStringExtra("phonemore"));
        mSystemContacts.setEmail(intent.getStringExtra("email"));
    }

    /**
     * 初始化界面
     * */
    private void initView(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar_system_contacts_info);
        mToolbar.setTitle(mSystemContacts.getName());
        setSupportActionBar(mToolbar);

        mDialContact = (Button)findViewById(R.id.btn_dial_system_contacts);
        mDialContact.setText(mSystemContacts.getPhone());

        mSendMsg = (ImageView)findViewById(R.id.icon_send_msg_system_contacts);
        mEmail = (Button)findViewById(R.id.edit_email_system_contacts);
        mAddToLocal = (Button)findViewById(R.id.add_contacts_to_local);
        mShapeContact = (Button)findViewById(R.id.shape_contact_system_contacts);
        mAddBlackList = (Button)findViewById(R.id.add_contact_blacklist_system_contacts);


        mDialContact.setOnClickListener(this);
        mSendMsg.setOnClickListener(this);
        mEmail.setOnClickListener(this);
        mAddToLocal.setOnClickListener(this);
        mShapeContact.setOnClickListener(this);
        mAddBlackList.setOnClickListener(this);

        /**
         * 如果emil为空,就不现实email的那个按钮了
         * */
        if(mSystemContacts.getEmail()== null || mSystemContacts.getEmail().isEmpty()){
            mEmail.setVisibility(View.GONE);
        }else{
            mEmail.setText(mSystemContacts.getEmail());
        }

        phoneMoreLayout = (LinearLayout)findViewById(R.id.phone_more_layout_system_contacts);
        phoneMoreBtn = (Button)findViewById(R.id.btn_phone_more_system_contacts);
        phoneMoreSms = (ImageView)findViewById(R.id.icon_send_msg_more_system_contacts);

        phoneMoreBtn.setOnClickListener(this);
        phoneMoreSms.setOnClickListener(this);

        /**
         * 如果有第二个号码，才会显示第二个号码的布局
         * */
        if(mSystemContacts.getPhoneMore() == null){
            phoneMoreLayout.setVisibility(View.GONE);
        }else {
            phoneMoreLayout.setVisibility(View.VISIBLE);
            phoneMoreBtn.setText(mSystemContacts.getPhoneMore());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_dial_system_contacts:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mSystemContacts.getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.icon_send_msg_system_contacts:
                //跳转到发送短信页面
                Uri smsToUri = Uri.parse("smsto://"+mSystemContacts.getPhone());
                Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );
                startActivity(mIntent);
                break;
            case R.id.btn_phone_more_system_contacts:
                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mSystemContacts.getPhoneMore()));
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;
            case R.id.icon_send_msg_more_system_contacts:
                Uri smsToUri1 = Uri.parse("smsto://"+mSystemContacts.getPhoneMore());
                Intent mIntent1 = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri1);
                startActivity(mIntent1);
                break;
            case R.id.edit_email_system_contacts:
                Uri uri = Uri.parse("mailto:"+mSystemContacts.getEmail());
                Intent intent1 = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent1);
                break;
            case R.id.add_contacts_to_local:
                showAddToLocalDialog();
                break;
            case R.id.shape_contact_system_contacts:

                break;
            case R.id.add_contact_blacklist_system_contacts:
                showBlackListDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 点击删除的时候，就弹出这个提示框
     * */
    private int mCurrentGroupId;

    private AlertDialog addToLocalDialog;
    private void showAddToLocalDialog(){
        final List<String> list = new ArrayList<>();
        GroupDao groupDao = new GroupDaoImp();
        final List<Group> groups = groupDao.selectAllGroup();
        for(int  i = 0; i < groups.size(); i++){
            list.add(groups.get(i).getName());
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout linearLayout =(LinearLayout) inflater.inflate(R.layout.dialog_add_to_local_system,null);
        ListView listView = (ListView)linearLayout.findViewById(R.id.dialog_lv_pick_group);
        Button cancelBtn = (Button)linearLayout.findViewById(R.id.dialog_btn_cancel_add_to_local);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.dialog_lv_item, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentGroupId = groups.get(i).getId();
                addToLocalDatabase();
                addToLocalDialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToLocalDialog.dismiss();
            }
        });

        addToLocalDialog = new AlertDialog.Builder(this).create();
        addToLocalDialog.setView(linearLayout);

        addToLocalDialog.show();
    }

    /**
     * 将这个系统联系人保存到本地数据库
     * */
    private void addToLocalDatabase(){
        UserDao userDao = new UserDaoImp();

        User user = new User();
        user.setName(mSystemContacts.getName());
        user.setPhone(mSystemContacts.getPhone());
        user.setPhoneMore(mSystemContacts.getPhoneMore());
        user.setEmail(mSystemContacts.getEmail());
        user.setGroupId(mCurrentGroupId);

        userDao.insertUser(user);
    }

    /**
     * 点击加入黑名单的时候，就弹出这个提示框
     * */
    private AlertDialog blackListDialog;
    private void showBlackListDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout linearLayout =(LinearLayout) inflater.inflate(R.layout.dialog_add_blacklist,null);
        Button deleteBtn = (Button)linearLayout.findViewById(R.id.dialog_btn_add_blaklist);
        Button cancelBtn = (Button)linearLayout.findViewById(R.id.dialog_btn_cancel_backlist);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除并且加入黑名单
                addBlackList();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blackListDialog.dismiss();
            }
        });
        blackListDialog = new AlertDialog.Builder(this).create();
        blackListDialog.setView(linearLayout);
        blackListDialog.show();
    }

    private void addBlackList(){
        BlackList blackList = new BlackList();
        blackList.setName(mSystemContacts.getName());
        blackList.setPhone(mSystemContacts.getPhone());
        blackList.setPhoneMore(mSystemContacts.getPhoneMore());
        blackList.setEmail(mSystemContacts.getEmail());
    }

}
