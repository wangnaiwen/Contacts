package com.wit.contacts.model;

import android.content.Context;

import com.wit.contacts.bean.NetUser;

/**
 * Created by wnw on 2016/10/17.
 */

public interface IRegisterModel {
    /**
     * 加载数据
     * */
    void registerNetUser(Context context, NetUser netUser, IRegisterModel.NetUserRegisterListener netUserRegisterListener);

    /**
     * 加载数据完成的回调
     * */
    interface NetUserRegisterListener{
        void complete(boolean isSuccess);
    }
}
