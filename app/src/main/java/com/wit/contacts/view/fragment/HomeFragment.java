package com.wit.contacts.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.adapter.UserAdapter;
import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.User;
import com.wit.contacts.presenter.UserPresenter;
import com.wit.contacts.view.IUserView;
import com.wit.contacts.view.activity.UserDetailInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/8/15.
 */

public class HomeFragment extends Fragment implements IUserView, ExpandableListView.OnChildClickListener{

    private View mView = null;
    private ExpandableListView mExpandableListView;
    private static UserPresenter userPresenter;
    private List<Group> groupList = new ArrayList<>();
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
        loadGroup();
        return mView;
    }

    public static void loadGroup(){
        userPresenter.load();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGroup();
    }

    private void initView(){
        mExpandableListView = (ExpandableListView)mView.findViewById(R.id.user_list);
        mExpandableListView.setOnChildClickListener(this);
        userPresenter = new UserPresenter(this);
    }

    @Override
    public void showLoading() {
        Toast.makeText(getContext(),"正在加载中...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUsers(List<Group> groups) {
        groupList = groups;
        UserAdapter mUserAdapter = new UserAdapter(getContext(), groups);
        mExpandableListView.setAdapter(mUserAdapter);
        mUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        Intent intent = new Intent(getContext(), UserDetailInfoActivity.class);
        Group group = groupList.get(i);
        User user = group.getUserList().get(i1);
        intent.putExtra("id", user.getId());
        intent.putExtra("name", user.getName());
        intent.putExtra("phone", user.getPhone());
        intent.putExtra("position", user.getPosition());
        intent.putExtra("groupId", user.getGroupId());
        startActivity(intent);
        return true;
    }

}
