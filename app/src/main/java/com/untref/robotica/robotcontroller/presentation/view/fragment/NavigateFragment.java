package com.untref.robotica.robotcontroller.presentation.view.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.untref.robotica.robotcontroller.R;
import com.untref.robotica.robotcontroller.core.bluetoothclient.BluetoothClient;
import com.untref.robotica.robotcontroller.core.interactor.NavigateInteractor;
import com.untref.robotica.robotcontroller.presentation.presenter.NavigatePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigateFragment extends Fragment implements NavigatePresenter.View {

    @BindView(R.id.btn_navigate)
    Button btn_navigate;
    @BindView(R.id.btn_stop)
    Button btn_stop;
    @BindView(R.id.btn_disconnect)
    Button btn_disconnect;
    private NavigatePresenter navigatePresenter;

    public NavigateFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigatePresenter = new NavigatePresenter(new NavigateInteractor(
                new BluetoothClient(BluetoothAdapter.getDefaultAdapter())));
        navigatePresenter.setView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        btn_navigate.setOnClickListener(v -> navigatePresenter.sendNavigate());
        btn_stop.setOnClickListener(v -> navigatePresenter.sendStop());
        btn_disconnect.setOnClickListener(v -> navigatePresenter.sendDisconnect());
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void disableNavigate() {
//        btn_navigate.setEnabled(false);
    }

    @Override
    public void goToDevicesFragment() {
        Fragment fragment = new DevicesFragment();

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction
                .add(R.id.devices_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();

//        transaction.replace(R.id.devices_fragment, fragment);
  //      transaction.commit();
    }
}
