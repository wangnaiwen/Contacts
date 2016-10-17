package com.wit.contacts.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.bean.NetUser;
import com.wit.contacts.presenter.LoginPresenter;
import com.wit.contacts.view.viewInterface.ILoginView;

/**
 * Created by wnw on 2016/10/17.
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
        initView();
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
            Toast.makeText(this, netUser.getEmail(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showDialog() {
        //在这里显示进度条
        Toast.makeText(this, "正在拼命加载中", Toast.LENGTH_SHORT).show();
    }
}
