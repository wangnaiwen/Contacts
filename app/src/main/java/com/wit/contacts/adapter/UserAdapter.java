package com.wit.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.wit.contacts.R;
import com.wit.contacts.bean.Group;

import java.util.List;
import java.util.Random;

/**
 * Created by wnw on 2016/9/7.
 */

public class UserAdapter extends BaseExpandableListAdapter{

    public static int userImgList[] = {R.drawable.user_img_1,
            R.drawable.user_img_2,
            R.drawable.user_img_3,
            R.drawable.user_img_4,
            R.drawable.user_img_5
    };

    private List<Group> mUserList;
    private Context mContext;

    public UserAdapter(Context context, List<Group> groups){
        this.mContext = context;
        this.mUserList = groups;
    }

    public void setDatas(List<Group> groups){
       this.mUserList = groups;
    }

    @Override
    public int getChildrenCount(int i) {
        return mUserList.get(i).getUserList().size();
    }

    @Override
    public int getGroupCount() {
        return mUserList.size();
    }

    @Override
    public Object getGroup(int i) {
        return mUserList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mUserList.get(i).getUserList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ChildHolder childHolder = null;
        if(view == null){
            view = inflater.inflate(R.layout.item_child, null);
            childHolder = new ChildHolder();
            childHolder.userImg = (TextView)view.findViewById(R.id.item_user_img);
            childHolder.userNameText = (TextView)view.findViewById(R.id.item_user_name);
            view.setTag(childHolder);
            view.setTag(R.id.group_id, i);
            view.setTag(R.id.child_id, i1);
        }else {
            childHolder = (ChildHolder)view.getTag();
        }
        childHolder.userImg.setBackgroundResource(userImgList[i1%5]);
        childHolder.userImg.setText(mUserList.get(i).getUserList().get(i1).getName().substring(0,1));
        childHolder.userNameText.setText(mUserList.get(i).getUserList().get(i1).getName());
        return view;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        GroupHolder groupHolder = null;
        if(view == null){
            view = inflater.inflate(R.layout.item_group, null);
            groupHolder = new GroupHolder();
            groupHolder.groupNameText = (TextView)view.findViewById(R.id.item_group_name);
            groupHolder.childCountText = (TextView)view.findViewById(R.id.item_group_child_count);
            view.setTag(groupHolder);
            view.setTag(R.id.group_id, i);
            view.setTag(R.id.child_id, -1);
        }else {
            groupHolder = (GroupHolder) view.getTag();
        }
        groupHolder.groupNameText.setText(mUserList.get(i).getName());
        groupHolder.childCountText.setText(getChildrenCount(i)+"");
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private static class ChildHolder{
        TextView userImg;
        TextView userNameText;
    }

    private static class GroupHolder{
        TextView groupNameText;
        TextView childCountText;
    }
}
