package com.untref.robotica.robotcontroller.presenter;

import com.untref.robotica.robotcontroller.interactor.NavigateInteractor;

public class NavigatePresenter extends Presenter<NavigatePresenter.View> {

    private NavigateInteractor navigateInteractor;

    public NavigatePresenter(NavigateInteractor navigateInteractor) {
        this.navigateInteractor = navigateInteractor;
    }

    public void sendNavigate() {
        getView().disableNavigate();
        navigateInteractor.navigate();
    }

    public interface View extends Presenter.View {
        void disableNavigate();
    }
}
