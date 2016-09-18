package com.wit.contacts.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.adapter.UserAdapter;
import com.wit.contacts.bean.Group;
import com.wit.contacts.presenter.UserPresenter;
import com.wit.contacts.view.IUserView;

import java.util.List;

/**
 * Created by wnw on 2016/8/15.
 */

public class HomeFragment extends Fragment implements IUserView{

    private View mView = null;
    private ExpandableListView mExpandableListView;
    private UserAdapter mUserAdapter;
    private UserPresenter userPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        initView();
        userPresenter.load();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(){
        mExpandableListView = (ExpandableListView)mView.findViewById(R.id.user_list);
        userPresenter = new UserPresenter(this);
    }

    @Override
    public void showLoading() {
        Toast.makeText(getContext(),"正在加载中...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUsers(List<Group> groups) {
        Log.d("wnw", groups.get(0).getName());
        if(mUserAdapter == null){
            mUserAdapter = new UserAdapter(getContext(), groups);
        }
        mExpandableListView.setAdapter(mUserAdapter);
        mUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
