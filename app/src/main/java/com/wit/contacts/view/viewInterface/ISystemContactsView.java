package com.wit.contacts.view.viewInterface;

import com.wit.contacts.bean.SystemContacts;
import java.util.List;

/**
 * Created by wnw on 2016/10/1.
 */

public interface ISystemContactsView {
    /**
     * 显示进度条
     * */
    void showLoading();

    /**
     * 显示数据
     * */
    void showUsers(List<SystemContacts> contactses);
}
