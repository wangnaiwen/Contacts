package com.wit.contacts.model;

import com.wit.contacts.bean.SystemContacts;
import com.wit.contacts.dao.SystemContactsDao;
import com.wit.contacts.dao.SystemContactsDapImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/10/1.
 */

public class SystemContactsModelImp implements ISystemContactsModel {

    private SystemContactsDao systemContactsDao;

    public SystemContactsModelImp(){
        systemContactsDao = new SystemContactsDapImp();
    }

    /**
     * get the data from database
     **/
    @Override
    public void loadSystemContacts(SystemLoadingListener systemLoadingListener) {

        List<SystemContacts> systemContactses = new ArrayList<>();
        systemContactses = systemContactsDao.selectAllSystemContacts();

        if (systemLoadingListener != null){
            systemLoadingListener.complete(systemContactses);
        }
    }

    /**
     * before insert the data , delete all system contacts from database
     * */
    @Override
    public void insertContacts(List<SystemContacts> contactsList) {
        systemContactsDao.deleteAllSystemContacts();
        if(contactsList != null){
            int length = contactsList.size();
            for (int i = 0 ; i < length; i++){
                systemContactsDao.insertSystemContacts(contactsList.get(i));
            }
        }
    }
}
