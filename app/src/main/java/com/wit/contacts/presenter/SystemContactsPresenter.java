package com.wit.contacts.presenter;

import com.wit.contacts.bean.SystemContacts;
import com.wit.contacts.model.ISystemContactsModel;
import com.wit.contacts.model.SystemContactsModelImp;
import com.wit.contacts.view.viewInterface.ISystemContactsView;

import java.util.List;

/**
 * Created by wnw on 2016/10/1.
 */

public class SystemContactsPresenter {
    /**
     * get the object of systemContactsModel
     * */
    private ISystemContactsModel systemContactsModel = new SystemContactsModelImp();

    private ISystemContactsView systemContactsView;

    public SystemContactsPresenter(ISystemContactsView view){
        this.systemContactsView = view;
    }

    /**
     * load the system contacts from model
     * */
    public void loadSystemContacts(){
        systemContactsView.showLoading();
        if(systemContactsModel != null){
            systemContactsModel.loadSystemContacts(new ISystemContactsModel.SystemLoadingListener() {
                @Override
                public void complete(List<SystemContacts> contactses) {
                    systemContactsView.showUsers(contactses);
                }
            });
        }
    }

    /**
     * insert the system contacts to local database
     * */
    public void insertSystemContacts(List<SystemContacts> systemContactses){
        systemContactsModel.insertContacts(systemContactses);
    }
}
