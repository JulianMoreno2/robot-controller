package com.untref.robotica.robotcontroller.view.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.untref.robotica.robotcontroller.R;
import com.untref.robotica.robotcontroller.data.client.BluetoothClient;
import com.untref.robotica.robotcontroller.interactor.NavigateInteractor;
import com.untref.robotica.robotcontroller.presenter.NavigatePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigateFragment extends Fragment implements NavigatePresenter.View {

    @BindView(R.id.btn_navigate)
    Button btn_navigate;
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
        try {
//            navigatePresenter.unregisterReceiver(getContext());
        } catch (IllegalArgumentException ex) {
            // If Receiver not registered
        }
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
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void disableNavigate() {
//        btn_navigate.setEnabled(false);
    }
}
