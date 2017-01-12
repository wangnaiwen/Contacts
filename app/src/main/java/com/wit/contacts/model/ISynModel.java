package com.wit.contacts.model;

import android.content.Context;

/**
 * Created by wnw on 2017/1/1.
 */

public interface ISynModel {
    /**
     * 加载数据
     * */
    void syn(Context context, SynLoadingListener synLoadingListener);

    /**
     * 加载数据完成的回调
     * */
    interface SynLoadingListener{
        void complete();
    }
}
