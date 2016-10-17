package com.wit.contacts.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.bean.NetUser;
import com.wit.contacts.presenter.RegisterPresenter;
import com.wit.contacts.view.viewInterface.IRegisterView;

/**
 * Created by wnw on 2016/10/17.
 */

public class RegisterActivity extends RegisterBaseActivity<IRegisterView, RegisterPresenter> implements
        View.OnClickListener, IRegisterView{
    private EditText name;
    private EditText phone;
    private EditText password;
    private Button register;
    private NetUser netUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView(){
        name = (EditText)findViewById(R.id.register_name);
        phone = (EditText)findViewById(R.id.register_phone);
        password = (EditText)findViewById(R.id.register_password);
        register = (Button)findViewById(R.id.btn_register);
        register.setOnClickListener(this);

        netUser = new NetUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                if(validateEditText()){
                    Toast.makeText(this, "用户名，手机和密码都不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    netUser.setPhone(phone.getText().toString().trim());
                    netUser.setName(name.getText().toString().trim());
                    netUser.setPassword(password.getText().toString().trim());
                    mPresenter.register(this, netUser);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证两个EditText是否都已经不为空了
     * */
    private boolean validateEditText(){
        return phone.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()
                || name.getText().toString().trim().isEmpty();
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void showDialog() {
        Toast.makeText(this, "正在拼命注册中", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void register(boolean isSuccess) {
        if(isSuccess){
            finish();
        }else {
            Toast.makeText(this, "注册失败！", Toast.LENGTH_SHORT).show();
        }
    }
}
