package com.untref.robotica.robotcontroller.presentation.presenter;

import android.content.Context;

import com.untref.robotica.robotcontroller.core.interactor.HomeInteractor;
import com.untref.robotica.robotcontroller.presentation.view.activity.HomeActivity;

public class HomePresenter extends Presenter<HomePresenter.View> {

    private HomeInteractor interactor;

    public HomePresenter(HomeInteractor interactor) {
        this.interactor = interactor;
    }

    public void onEnableBluetooth(Context context, HomeActivity homeActivity) {
        if(interactor.enableBluetooth(context, homeActivity)) {
            getView().enableBluetooth();
        } else {
            getView().disableBluetooth();
        }
    }

    public interface View extends Presenter.View {

        void enableBluetooth();

        void disableBluetooth();
    }
}