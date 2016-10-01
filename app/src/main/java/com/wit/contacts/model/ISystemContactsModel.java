package com.wit.contacts.model;

import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.SystemContacts;

import java.util.List;

/**
 * Created by wnw on 2016/10/1.
 */

public interface ISystemContactsModel {

    /**
     * load the data
     */
    void loadSystemContacts(SystemLoadingListener systemLoadingListener);

    /**
     * the listener of finish the loading
     * */
    interface SystemLoadingListener{
        void complete(List<SystemContacts> contactses);
    }

    /**
     * insert the system contacts to databse
     * */
    void insertContacts(List<SystemContacts> contactsList);
}
