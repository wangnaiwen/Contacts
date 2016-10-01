package com.wit.contacts.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.adapter.UserAdapter;
import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.User;
import com.wit.contacts.presenter.UserPresenter;
import com.wit.contacts.view.IUserView;
import com.wit.contacts.view.activity.AddContactsActivity;
import com.wit.contacts.view.activity.MainActivity;
import com.wit.contacts.view.activity.UserDetailInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/8/15.
 */

public class HomeFragment extends Fragment implements IUserView, ExpandableListView.OnChildClickListener, AdapterView.OnItemLongClickListener{

    private View mView = null;
    private ExpandableListView mExpandableListView;
    private static UserPresenter userPresenter;
    private List<Group> groupList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_contacts) {
            Toast.makeText(getContext(),"Add One",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), AddContactsActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_add_group){
            Toast.makeText(getContext(),"Add Group",Toast.LENGTH_SHORT).show();
            showDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private AlertDialog myDialog;
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = null;
         // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogView = inflater.inflate(R.layout.dialog_add_group, null);
        final EditText addGroupEditText = (EditText)dialogView.findViewById(R.id.dialog_add_group_name);

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("wnw", "null");
                        // sign in the user ...
                        if( addGroupEditText.getText().toString().trim().equals("")){
                            addGroupEditText.setHint("请输入组名称");
                            addGroupEditText.setHintTextColor(Color.RED);
                            Log.d("wnw1",addGroupEditText.getText().toString());
                        }else{
                            //插入数据库，并且销毁Dialog
                            Log.d("wnw2",addGroupEditText.getText().toString());
                            saveGroupToDB(addGroupEditText.getText().toString());
                            myDialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myDialog.dismiss();
                    }
                });
        builder.setTitle("请输入组名");
        myDialog = builder.create();
        myDialog.show();
    }

    private void saveGroupToDB(String groupName){
        userPresenter.insertGroup(groupName);
        userPresenter.load();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        Toolbar toolbar = (Toolbar)mView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

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
        mExpandableListView.setOnItemLongClickListener(this);
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
            String groupName = groupList.get(i).getName();
            showUpdateGroupDialog(groupName);
        }else{
            Log.d("wnw", "you long click the child");
        }
        return true;
    }

    /**
     * show the dialog if long click the group
     * */
    private AlertDialog updateGroupDialog;
    private void showUpdateGroupDialog(String groupName){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
                            Log.d("wnw1",updateGroupText.getText().toString());
                        }else{
                            //更新数据库，并且销毁Dialog
                            updateGroupName(updateGroupText.getText().toString());
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

    /**
     * update the name of group from db
     * */
    private void updateGroupName(String name){

        int groupId = groupList.get(selectedGroupId).getId();
        Log.d("wnw", "group name is: "+ name +" " + groupId + " here");
        userPresenter.updateGroupName(name, groupId);
        loadGroup();
    }
}
