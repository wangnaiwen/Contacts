package com.wit.contacts.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.wit.contacts.R;
import com.wit.contacts.bean.CurrentUser;
import com.wit.contacts.bean.NetUser;
import com.wit.contacts.dao.CurrentUserDAO;
import com.wit.contacts.dao.CurrentUserImpl;
import com.wit.contacts.data.ContactDatabaseHelper;
import com.wit.contacts.presenter.LoginPresenter;
import com.wit.contacts.view.viewInterface.ILoginView;

/**
 * Created by wnw on 2016/10/17.88498646
 */

public class LoginActivity extends MvpBaseActivity<ILoginView, LoginPresenter>  implements
        View.OnClickListener, ILoginView{
    private EditText phone;
    private EditText password;
    private Button login;
    private TextView newUser;
    private NetUser netUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createDB();
        isLogin();
        initView();
    }

    private void isLogin(){
        CurrentUserDAO currentUserDAO = new CurrentUserImpl();
        CurrentUser currentUser = currentUserDAO.selectCurrentUser();
        if(currentUser != null){
            netUser = new NetUser();
            netUser.setId(currentUser.getId());
            netUser.setName(currentUser.getName());
            netUser.setPhone(currentUser.getPhone());
            netUser.setPhone2(currentUser.getPhone2());
            netUser.setPassword(currentUser.getPassword());
            netUser.setEmail(currentUser.getEmail());

            openMainAty();
        }
    }

    //create database
    private void createDB(){
        ContactDatabaseHelper.getInstance(this);
    }

    private void initView(){
        phone = (EditText)findViewById(R.id.login_phone);
        password = (EditText)findViewById(R.id.login_password);
        login = (Button)findViewById(R.id.btn_login);
        newUser = (TextView)findViewById(R.id.login_new_user);

        login.setOnClickListener(this);
        newUser.setOnClickListener(this);

        netUser = new NetUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                if(validateEditText()){
                    Toast.makeText(this, "手机和密码都不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    //验证密码
                    netUser.setPhone(phone.getText().toString().trim());
                    netUser.setPassword(password.getText().toString().trim());
                    mPresenter.validate(this, netUser);   //开始获得数据
                }
                break;
            case R.id.login_new_user:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 验证两个EditText是否都已经不为空了
     * */
    private boolean validateEditText(){
        return phone.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty();
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void validate(NetUser netUser) {
        //在这里得到返回的数据
        if(netUser == null){
            Toast.makeText(this, "手机或密码错误", Toast.LENGTH_SHORT).show();
        }else {
            this.netUser = netUser;
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            /**
             * 保存这个账号的SharePreference
             * */
            //saveAccount();
            openMainAty();
        }
    }

    private void openMainAty(){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("netUser",netUser);
        startActivity(intent);
        finish();
    }

    @Override
    public void showDialog() {
        //在这里显示进度条
        Toast.makeText(this, "正在拼命登录中", Toast.LENGTH_SHORT).show();
    }

    private void saveAccount(){
        SharedPreferences.Editor editor = getSharedPreferences("account",
                MODE_PRIVATE).edit();
        editor.clear();
        editor.putInt("id", netUser.getId());
        editor.putString("name", netUser.getName());
        editor.putString("phone", netUser.getPhone());
        editor.putString("password", netUser.getPassword());
        editor.putString("phone2", netUser.getPhone2());
        editor.putString("email", netUser.getEmail());
        editor.apply();
    }
}
