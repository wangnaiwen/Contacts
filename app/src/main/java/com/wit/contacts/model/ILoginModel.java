package com.wit.contacts.model;

import android.content.Context;

import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.NetUser;

import java.util.List;

/**
 * Created by wnw on 2016/10/17.
 */

public interface ILoginModel {
    /**
     * 加载数据
     * */
    void loadNetUser(Context context, NetUser netUser, ILoginModel.NetUserLoadingListener loadingListener);

    /**
     * 加载数据完成的回调
     * */
    interface NetUserLoadingListener{
        void complete(NetUser netUser);
    }
}
