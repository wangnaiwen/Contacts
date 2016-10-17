package com.wit.contacts.view.viewInterface;

import com.wit.contacts.bean.NetUser;

/**
 * Created by wnw on 2016/10/17.
 */

public interface ILoginView {
    /**
     * 显示进度条
     * */
    void showDialog();

    /**
     * 返回数据
     * */
    void validate(NetUser netUser);
}
