package com.wit.contacts.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.wit.contacts.bean.User;
import com.wit.contacts.dao.UserDao;
import com.wit.contacts.dao.UserDaoImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/9/20.
 */
public class UserDetailInfoActivity  extends AppCompatActivity implements View.OnClickListener{

    private User mUser;
    private Toolbar mToolbar;
    private Button mDialContact;
    private ImageView mSendMsg;
    private Button mEmail;
    private Button mEditContact;
    private Button mShapeContact;
    private Button mAddBlackList;
    private Button mDeleteContact;

    /**
     * 如果有两个号码,才会显示的第二个号码的布局
     * */
    private LinearLayout phoneMoreLayout;
    private Button phoneMoreBtn;
    private ImageView phoneMoreSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        getUserInfo();
        initView();
    }

    /**
     * 得到从HomeFragment传递过来的用户信息
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
    }

    private void initView(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar_user_info);
        mToolbar.setTitle(mUser.getName());
        setSupportActionBar(mToolbar);


        mDialContact = (Button)findViewById(R.id.btn_dial);
        mDialContact.setText(mUser.getPhone());

        mSendMsg = (ImageView)findViewById(R.id.icon_send_msg);
        mEmail = (Button)findViewById(R.id.edit_email);
        mEditContact = (Button)findViewById(R.id.edit_contact);
        mShapeContact = (Button)findViewById(R.id.shape_contact);
        mAddBlackList = (Button)findViewById(R.id.add_contact_blacklist);
        mDeleteContact = (Button)findViewById(R.id.delete_contact);

        mDialContact.setOnClickListener(this);
        mSendMsg.setOnClickListener(this);
        mEmail.setOnClickListener(this);
        mEditContact.setOnClickListener(this);
        mShapeContact.setOnClickListener(this);
        mAddBlackList.setOnClickListener(this);
        mDeleteContact.setOnClickListener(this);

        /**
         * 如果emil为空,就不现实email的那个按钮了
         * */
        if(mUser.getEmail() == null || mUser.getEmail().isEmpty()){
            mEmail.setVisibility(View.GONE);
        }else{
            mEmail.setText(mUser.getEmail());
        }

        phoneMoreLayout = (LinearLayout)findViewById(R.id.phone_more_layout);
        phoneMoreBtn = (Button)findViewById(R.id.btn_phone_more);
        phoneMoreSms = (ImageView)findViewById(R.id.icon_send_msg_more);

        phoneMoreBtn.setOnClickListener(this);
        phoneMoreSms.setOnClickListener(this);

        /**
         * 如果有第二个号码，才会显示第二个号码的布局
         * */
        if(mUser.getPhoneMore() == null || mUser.getPhoneMore().isEmpty()){
            phoneMoreLayout.setVisibility(View.GONE);
        }else {
            phoneMoreLayout.setVisibility(View.VISIBLE);
            phoneMoreBtn.setText(mUser.getPhoneMore());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_dial:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mUser.getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.icon_send_msg:
                //跳转到发送短信页面
                Uri smsToUri = Uri.parse("smsto://"+mUser.getPhone());
                Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );
                startActivity(mIntent);
                break;
            case R.id.btn_phone_more:
                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mUser.getPhoneMore()));
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;
            case R.id.icon_send_msg_more:
                Uri smsToUri1 = Uri.parse("smsto://"+mUser.getPhoneMore());
                Intent mIntent1 = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri1);
                startActivity(mIntent1);
                break;
            case R.id.edit_email:
                Uri uri = Uri.parse("mailto:"+mUser.getEmail());
                Intent intent1 = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent1);
                break;
            case R.id.edit_contact:
                editContact();
                break;
            case R.id.shape_contact:

                break;
            case R.id.add_contact_blacklist:
                showBlackListDialog();
                break;
            case R.id.delete_contact:
                showDeleteDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 点击编辑联系人的时候
     * */
    public int REQUEST_CODE = 1;
    private void editContact() {
        Intent intent = new Intent(UserDetailInfoActivity.this, EditContactActivity.class);
        intent.putExtra("id", mUser.getId());
        intent.putExtra("name", mUser.getName());
        intent.putExtra("phone", mUser.getPhone());
        intent.putExtra("phonemore", mUser.getPhoneMore());
        intent.putExtra("email", mUser.getEmail());
        intent.putExtra("position", mUser.getPosition());
        intent.putExtra("groupId", mUser.getGroupId());
        startActivityForResult(intent, REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(REQUEST_CODE == requestCode){
            if(resultCode == RESULT_OK){
                mUser.setId(data.getIntExtra("id", -1));
                mUser.setName(data.getStringExtra("name"));
                mUser.setPhone(data.getStringExtra("phone"));
                mUser.setPhoneMore(data.getStringExtra("phonemore"));
                mUser.setEmail(data.getStringExtra("email"));
                mUser.setPosition(data.getStringExtra("position"));
                mUser.setGroupId(data.getIntExtra("groupId", -1));

                mToolbar.setTitle(mUser.getName());
                mDialContact.setText(mUser.getPhone());
                /**
                 * 如果email为空，就不现实email按钮，如果不为空，就显示
                 * */
                if(mUser.getEmail().isEmpty()){
                    mEmail.setVisibility(View.GONE);
                }else {
                    mEmail.setVisibility(View.VISIBLE);
                    mEmail.setText(mUser.getEmail());
                }

                /**
                 * 如果有第二个号码，才会显示第二个号码的布局
                 * */
                if(mUser.getPhoneMore().isEmpty()){
                    phoneMoreLayout.setVisibility(View.GONE);
                }else {
                    phoneMoreLayout.setVisibility(View.VISIBLE);
                    phoneMoreBtn.setText(mUser.getPhoneMore());
                }
            }
        }else {

        }
    }

    /**
     * 点击删除的时候，就弹出这个提示框
     * */
    private AlertDialog deleteDialog;
    private void showDeleteDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout linearLayout =(LinearLayout) inflater.inflate(R.layout.dialog_delete,null);
        Button deleteBtn = (Button)linearLayout.findViewById(R.id.dialog_btn_delete);
        Button cancelBtn = (Button)linearLayout.findViewById(R.id.dialog_btn_cancel);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除联系人
                deleteContact();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(linearLayout);
        deleteDialog.show();
    }

    private void deleteContact(){
        UserDao userDao = new UserDaoImp();
        userDao.deleteUser(mUser.getId());
        finish();
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
                deleteContact();
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
        blackList.setName(mUser.getName());
        blackList.setPhone(mUser.getPhone());
        blackList.setPhoneMore(mUser.getPhoneMore());
        blackList.setEmail(mUser.getEmail());
        blackList.setPosition(mUser.getPosition());
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
