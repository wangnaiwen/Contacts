/*
package com.wit.contacts.presenter;

import java.lang.ref.WeakReference;

*/
/**
 * Created by wnw on 2016/9/7.
 *//*


public abstract class BasePresenter<T>  {
    //当内存不足时，就释放
    protected WeakReference<T> mWeakReference;

    //与view进行关联
    public void attachView(T view){
        mWeakReference = new WeakReference<T>(view);
    }

    //与view解除关联
    public void detachView(){
        if(mWeakReference != null){
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    //暴露一个接口
    public T getView(){
        return mWeakReference.get();
    }
}
*/
