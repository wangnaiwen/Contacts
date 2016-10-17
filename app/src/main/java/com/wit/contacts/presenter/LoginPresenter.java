package com.wit.contacts.presenter;

import com.wit.contacts.bean.NetUser;
import com.wit.contacts.model.ILoginModel;
import com.wit.contacts.model.LoginModelImp;
import com.wit.contacts.view.viewInterface.ILoginView;

/**
 * Created by wnw on 2016/10/17.
 */

public class LoginPresenter extends BasePresenter<ILoginView> {

    ILoginModel mLoginModel = new LoginModelImp();


    //加载数据
    public void validate(NetUser netUser) {
        //加载进度条

        getView().showDialog();
        //model进行数据获取
        if (mLoginModel != null) {
            mLoginModel.loadNetUser(netUser, new ILoginModel.NetUserLoadingListener() {
                @Override
                public void complete(NetUser netUser) {
                    //返回给view
                    getView().validate(netUser);
                }
            });
        }

    }
}