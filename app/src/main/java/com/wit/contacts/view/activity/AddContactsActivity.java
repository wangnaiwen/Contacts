package com.wit.contacts.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wit.contacts.R;

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
        userGroup = (TextView)findViewById(R.id.contact_group);

        backArrow = (ImageView)findViewById(R.id.back_arrow);
        finishInput = (Button)findViewById(R.id.finish_input);
        backArrow.setOnClickListener(this);
        finishInput.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_arrow:
                finish();
                break;
            case R.id.finish_input:
                /**
                 * 判断输入是否正确
                 * 1. 手机号码，名字是否已经输入
                 * 2. 手机号码格式是否正确
                 * 3. 如果都正确，就将数据保存到数据库中
                 * */
                if(isPhoneNums(userPhone.getText().toString())){
                    finish();
                }else {
                    Toast.makeText(AddContactsActivity.this,"手机号码不正确", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 判断手机号码是否合理
     * 1. 判断字符串的位数
     * 2. 验证手机号码格式
     * */
    public boolean isPhoneNums(String phoneNums){
        if(isMatchLength(phoneNums,11) && isMobileNums(phoneNums)){
            return true;
        }
        return false;
    }

    /**
     * 判断字符串的位数是不是
     * */
    public static boolean isMatchLength(String str,int length){
        if(str.isEmpty()){
            return false;
        }else{
            return str.length() == length ? true: false;
        }
    }

    /**
     * 验证手机格式
     * */
    public static boolean isMobileNums(String mobileNums){
        /**
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */

        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][358]\\d{9}";

        if(TextUtils.isEmpty(mobileNums)){
            return false;
        }else{
            return mobileNums.matches(telRegex);
        }
    }
}
