package com.wit.contacts.view.viewInterface;

/**
 * Created by wnw on 2016/10/17.
 */

public interface IRegisterView {
    /**
     * 显示进度条
     * */
    void showDialog();

    /**
     * 返回数据
     * */
    void register(boolean isSuccess);
}
