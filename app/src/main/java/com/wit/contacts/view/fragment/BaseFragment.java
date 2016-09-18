/*
package com.wit.contacts.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wit.contacts.presenter.BasePresenter;

*/
/**
 * Created by wnw on 2016/9/7.
 *//*

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment{

    protected T mUserPresenter;
    private V wu;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserPresenter = createPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //关联view
        mUserPresenter.attachView((V)BaseFragment.this);
        Log.d("wnw", "you come here? onCreateView");

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    protected abstract  T createPresenter();

    @Override
    public void onDestroyView() {
        mUserPresenter.detachView();
        super.onDestroyView();
    }

}
*/
