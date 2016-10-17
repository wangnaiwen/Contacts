package com.wit.contacts.presenter;

import android.content.Context;

import com.wit.contacts.bean.NetUser;
import com.wit.contacts.model.ILoginModel;
import com.wit.contacts.model.IRegisterModel;
import com.wit.contacts.model.LoginModelImp;
import com.wit.contacts.model.RegisterModelImp;
import com.wit.contacts.view.viewInterface.IRegisterView;

/**
 * Created by wnw on 2016/10/17.
 */

public class RegisterPresenter extends RegisterBasePresenter<IRegisterView> {
    IRegisterModel mRegisterModel = new RegisterModelImp();


    //加载数据
    public void register(Context context, NetUser netUser) {
        //加载进度条

        getView().showDialog();
        //model进行数据获取
        if (mRegisterModel != null) {
            mRegisterModel.registerNetUser(context, netUser, new IRegisterModel.NetUserRegisterListener(){
                @Override
                public void complete(boolean isSuccess) {
                    //返回给view
                    getView().register(isSuccess);
                }
            });
        }
    }
}
