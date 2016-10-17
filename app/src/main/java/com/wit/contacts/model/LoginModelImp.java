package com.wit.contacts.model;

import com.wit.contacts.bean.NetUser;

/**
 * Created by wnw on 2016/10/17.
 */

public class LoginModelImp implements ILoginModel{
    @Override
    public void loadNetUser(NetUser netUser, NetUserLoadingListener loadingListener) {
        if(netUser.getPhone().equals("123") && netUser.getPassword().equals("123")){
            netUser.setEmail("123");
            if(loadingListener != null){
                loadingListener.complete(netUser);
            }
        }else{
            if(loadingListener != null){
                loadingListener.complete(netUser);
            }
        }
    }
}
