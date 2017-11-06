package com.untref.robotica.robotcontroller.presentation.presenter;

import android.util.Log;

import com.untref.robotica.robotcontroller.core.interactor.NavigateInteractor;

import io.reactivex.subjects.PublishSubject;

public class NavigatePresenter extends Presenter<NavigatePresenter.View> {

    private NavigateInteractor navigateInteractor;
    private PublishSubject<String> subject;

    public NavigatePresenter(NavigateInteractor navigateInteractor, PublishSubject<String> subject) {
        this.navigateInteractor = navigateInteractor;
        this.subject = subject;
    }

    public void sendNavigate() {
        getView().disableNavigate();
        navigateInteractor.navigate();
    }

    public void sendStop() {
        navigateInteractor.stop();
    }

    public void sendDisconnect() {
        navigateInteractor.disconnect();
        getView().goToDevicesFragment();
    }

    public void readFromBluetooth() {
        subject.subscribe(message -> {
            Log.d("DEVICE", "Message -> "+ message);
            getView().writeIncommingMessage(message);
        });
    }

    public interface View extends Presenter.View {
        void disableNavigate();

        void goToDevicesFragment();

        void writeIncommingMessage(String message);
    }
}
