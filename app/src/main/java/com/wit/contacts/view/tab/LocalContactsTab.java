package com.wit.contacts.view.tab;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.wit.contacts.R;
import com.wit.contacts.adapter.UserAdapter;
import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.User;
import com.wit.contacts.presenter.UserPresenter;
import com.wit.contacts.view.viewInterface.IUserView;
import com.wit.contacts.view.activity.UserDetailInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/10/1.
 */

public class LocalContactsTab implements ExpandableListView.OnChildClickListener, AdapterView.OnItemLongClickListener, IUserView{
    private View mView;
    private LayoutInflater mInflater;
    private ExpandableListView mExpandableListView;
    private Context mContext;

    private TextView hintText;

    private UserPresenter userPresenter;
    private UserAdapter mUserAdapter;

    private List<Group> groupList = new ArrayList<>();

    public LocalContactsTab(LayoutInflater inflater){
        this.mInflater = inflater;
        mContext = mInflater.getContext();
        mView = mInflater.inflate(R.layout.tab_local_contacts, null);
        initView();
    }

    private void initView(){
        mExpandableListView = (ExpandableListView)mView.findViewById(R.id.user_list);
        hintText = (TextView)mView.findViewById(R.id.hint_text_local);
        mExpandableListView.setOnChildClickListener(this);
        mExpandableListView.setOnItemLongClickListener(this);
        userPresenter = new UserPresenter(this);
        reLoadData();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showUsers(List<Group> groups) {
        groupList = groups;
        if(mUserAdapter == null){
            mUserAdapter  = new UserAdapter(mContext, groupList);
            mExpandableListView.setAdapter(mUserAdapter);
        }

        if(groupList.size() == 0){
            hintText.setVisibility(View.VISIBLE);
        }else {
            hintText.setVisibility(View.GONE);
        }
        mUserAdapter.setDatas(groupList);
        mUserAdapter.notifyDataSetChanged();
    }
    /**
     * new a group and insert the data to database
     * */
    public void insertGroup(String groupName){
        userPresenter.insertGroup(groupName);
        userPresenter.load();
    }


    /**
     * update group name
     * */
    public void updateGroupName(String name, int groupId){
        userPresenter.updateGroupName(name, groupId);
        reLoadData();
    }

    /**
     * reload the data
     * */
    public void reLoadData(){
        userPresenter.load();
    }

    public View getView(){
        return mView;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        Intent intent = new Intent(mContext, UserDetailInfoActivity.class);
        Group group = groupList.get(i);
        User user = group.getUserList().get(i1);
        intent.putExtra("id", user.getId());
        intent.putExtra("name", user.getName());
        intent.putExtra("phone", user.getPhone());
        intent.putExtra("phonemore", user.getPhoneMore());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("position", user.getPosition());
        intent.putExtra("groupId", user.getGroupId());
        mContext.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        return true;
    }

    /**
     * add the item long click for expandable
     * if childPos is -1, is long click the group
     * if childPos is > -1, is long click the child
     * */
    private int selectedGroupId = 0 ;
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        int groupPos = (Integer)view.getTag(R.id.group_id);
        int childPos = (Integer)view.getTag(R.id.child_id);
        if(childPos == -1){
            selectedGroupId = i;
            Log.d("wnw", selectedGroupId+"");
            String groupName = groupList.get(i).getName();
            showUpdateGroupDialog(groupName);
        }else{
        }
        return true;
    }

    /**
     * show the dialog if long click the group
     * */
    private AlertDialog updateGroupDialog;
    private void showUpdateGroupDialog(String groupName){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mInflater;
        View dialogView = null;
        dialogView = inflater.inflate(R.layout.dialog_rename_group, null);
        final EditText updateGroupText = (EditText)dialogView.findViewById(R.id.dialog_group_rename);
        updateGroupText.setText(groupName);
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("重命名", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        if( updateGroupText.getText().toString().trim().equals("")){
                            updateGroupText.setHint("组名不能为空");
                            updateGroupText.setHintTextColor(Color.RED);
                        }else{
                            //更新数据库，并且销毁Dialog
                            int groupId = groupList.get(selectedGroupId).getId();
                            updateGroupName(updateGroupText.getText().toString(), groupId);
                            updateGroupDialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateGroupDialog.dismiss();
                    }
                });
        builder.setTitle("重命名对话框");
        updateGroupDialog = builder.create();
        updateGroupDialog.show();
    }
}
