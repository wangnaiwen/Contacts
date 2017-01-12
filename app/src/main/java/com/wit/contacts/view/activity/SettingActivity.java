package com.wit.contacts.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.dao.CurrentUserDAO;
import com.wit.contacts.dao.CurrentUserImpl;
import com.wit.contacts.presenter.SynPresenter;
import com.wit.contacts.view.viewInterface.ISynView;

/**
 * Created by wnw on 2016/12/22.
 */

public class SettingActivity extends Activity implements View.OnClickListener,ISynView{

    private TextView checkNewVersion;
    private TextView aboutContacts;
    private TextView exitContacts;
    private ImageView backSetting;
    private CurrentUserDAO currentUserDAO;
    private SynPresenter synPresenter = new SynPresenter(this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        currentUserDAO = new CurrentUserImpl();

        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void initView(){
        checkNewVersion = (TextView)findViewById(R.id.check_new_version);
        aboutContacts = (TextView)findViewById(R.id.about_contacts);
        exitContacts = (TextView)findViewById(R.id.exit_contacts);
        backSetting = (ImageView)findViewById(R.id.back_setting);

        checkNewVersion.setOnClickListener(this);
        aboutContacts.setOnClickListener(this);
        exitContacts.setOnClickListener(this);
        backSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.check_new_version:
                Toast.makeText(this, "当前已经是最新版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_contacts:
                Intent intent = new Intent(this, AboutContactsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.exit_contacts:
                showExitDialog();
                break;
            case R.id.back_setting:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            default:
                break;
        }
    }


    /**
     * show the dialog of delete the address
     * */
    private void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("退出账号");
        builder.setMessage("是否退成当前账号？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                synPresenter.load();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog deleteDialog = builder.create();
        deleteDialog.show();
        //Window window = deleteDialog.getWindow();
        // window.setWindowAnimations(R.style.dialog_anim);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void showLoading() {
        Toast.makeText(this, "正在退出当前用户",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFinishLoading() {
        currentUserDAO.deleteUser();
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        ActivityCollector.finishAll();
    }
}
