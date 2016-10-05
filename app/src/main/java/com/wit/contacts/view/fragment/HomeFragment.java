package com.wit.contacts.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.adapter.TabPagerAdapter;
import com.wit.contacts.view.activity.AddContactsActivity;
import com.wit.contacts.view.tab.LocalContactsTab;
import com.wit.contacts.view.tab.SystemContactsTab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/8/15.
 */

public class HomeFragment extends Fragment{

    private View mView = null;
    private TabLayout mTabLayout = null;
    private ViewPager mViewPager = null;
    private LayoutInflater mInflater = null;

    private List<View> mTabViewList = new ArrayList<>();   // 页卡View集合
    private List<String> mTabTitleList = new ArrayList();  //页卡title集合

    private LocalContactsTab mLocalContactsTab = null;    // 两个页卡View
    private SystemContactsTab mSystemContactsTab = null;

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
            showAGroupDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog addGroupDialog;
    private void showAGroupDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = null;
        dialogView = inflater.inflate(R.layout.dialog_add_group, null);
        final EditText addGroupEditText = (EditText)dialogView.findViewById(R.id.dialog_add_group_name);

        builder.setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if( addGroupEditText.getText().toString().trim().equals("")){
                            addGroupEditText.setHint("请输入组名称");
                            addGroupEditText.setHintTextColor(Color.RED);
                        }else{
                            //插入数据库，并且销毁Dialog
                            mLocalContactsTab.insertGroup(addGroupEditText.getText().toString());
                            addGroupDialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addGroupDialog.dismiss();
                    }
                });
        builder.setTitle("请输入组名");
        addGroupDialog = builder.create();
        addGroupDialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mInflater = inflater;
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }

        Toolbar toolbar = (Toolbar)mView.findViewById(R.id.toolbar_home);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        initView();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocalContactsTab.reLoadData();
    }

    private void initView(){
        mTabLayout = (TabLayout)mView.findViewById(R.id.layout_tab);
        mViewPager = (ViewPager)mView.findViewById(R.id.vp_view);

        mLocalContactsTab = new LocalContactsTab(mInflater);     //初始化
        mSystemContactsTab = new SystemContactsTab(mInflater);

        mTabViewList.add(mSystemContactsTab.getView());
        mTabViewList.add(mLocalContactsTab.getView());           //将View添加到List中

        mTabTitleList.add("系统联系人");
        mTabTitleList.add("本地联系人");                         //将Title添加到List中

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);       //设置页卡模式

        mTabLayout.addTab(mTabLayout.newTab().setText(mTabTitleList.get(0)));   //将卡页添加到View中
        mTabLayout.addTab(mTabLayout.newTab().setText(mTabTitleList.get(1)));

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(mTabViewList, mTabTitleList);
        mViewPager.setAdapter(tabPagerAdapter);              //给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);           //将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(tabPagerAdapter); //给Tabs设置适配器

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
