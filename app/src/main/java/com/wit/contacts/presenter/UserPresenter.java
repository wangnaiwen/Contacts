package com.wit.contacts.presenter;

import android.util.Log;
import android.view.View;

import com.wit.contacts.bean.Group;
import com.wit.contacts.model.IUserModel;
import com.wit.contacts.model.UserModelImp;
import com.wit.contacts.view.IUserView;

import java.util.List;

/**
 * Created by wnw on 2016/9/7.
 */
public class UserPresenter{
    /**
     * 得到Model对象
     * */
    private IUserModel userModel = new UserModelImp();

    /**
     * 得到View,并从model中加载数据，然后显示到view
     * */
    private IUserView mUserView;
    public UserPresenter(IUserView userView){
        this.mUserView = userView;
    }

    public void load(){
        //显示进度条
        mUserView.showLoading();
        if(userModel != null){
            userModel.loadUser(new IUserModel.UserLoadingListener() {
                @Override
                public void complete(List<Group> groups) {
                    //数据加载完后，显示到View中
                    mUserView.showUsers(groups);
                    Log.d("wnw", groups.get(0).getName());
                    Log.d("wnw", "you come here?");
                }
            });
        }
    }
}
