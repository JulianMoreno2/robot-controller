package com.untref.robotica.robotcontroller.presentation.presenter;

import com.untref.robotica.robotcontroller.core.interactor.NavigateInteractor;

public class NavigatePresenter extends Presenter<NavigatePresenter.View> {

    private NavigateInteractor navigateInteractor;

    public NavigatePresenter(NavigateInteractor navigateInteractor) {
        this.navigateInteractor = navigateInteractor;
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

    public interface View extends Presenter.View {
        void disableNavigate();

        void goToDevicesFragment();
    }
}
