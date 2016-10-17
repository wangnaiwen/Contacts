package com.wit.contacts.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wit.contacts.presenter.BasePresenter;

/**
 * Created by wnw on 2016/10/17.
 */

public abstract class MvpBaseActivity<V,T extends BasePresenter<V>> extends AppCompatActivity {
    protected T mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //创建presenter
        mPresenter = createPresenter();
        //内存泄露
        //关联View
        mPresenter.attachView((V) this);
    }

    protected abstract  T createPresenter();
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mPresenter.detachView();
    }
}