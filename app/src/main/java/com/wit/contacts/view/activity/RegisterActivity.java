package com.wit.contacts.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wit.contacts.R;

/**
 * Created by wnw on 2016/10/17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText name;
    private EditText phone;
    private EditText password;
    private Button register;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                if(validateEditText()){
                    Toast.makeText(this, "用户名，手机和密码都不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    //将这个用户插入到网络数据库
                    finish();
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
}
