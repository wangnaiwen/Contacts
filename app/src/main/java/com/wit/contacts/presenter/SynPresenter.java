package com.wit.contacts.presenter;

import android.content.Context;

import com.wit.contacts.model.ISynModel;
import com.wit.contacts.model.ISystemContactsModel;
import com.wit.contacts.model.SynModelImpl;
import com.wit.contacts.view.viewInterface.ISynView;

/**
 * Created by wnw on 2017/1/1.
 */

public class SynPresenter {
    /**
     * 得到Model对象
     * */
    private ISynModel synModel = new SynModelImpl();
    private Context context;
    /**
     * 得到View,并从model中加载数据，然后显示到view
     * */
    private ISynView synView;
    public SynPresenter(Context context, ISynView synView){
        this.context = context;
        this.synView = synView;
    }

    public void load(){
        //显示进度条
        synView.showLoading();
        if(synModel != null){
            synModel.syn(context, new ISynModel.SynLoadingListener() {
                @Override
                public void complete() {
                    synView.showFinishLoading();
                }
            });
        }
    }
}
