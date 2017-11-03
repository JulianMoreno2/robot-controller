package com.untref.robotica.robotcontroller.presentation.view.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.untref.robotica.robotcontroller.R;
import com.untref.robotica.robotcontroller.core.bluetoothclient.BluetoothClient;
import com.untref.robotica.robotcontroller.core.interactor.NavigateInteractor;
import com.untref.robotica.robotcontroller.presentation.presenter.NavigatePresenter;
import com.untref.robotica.robotcontroller.core.provider.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigateFragment extends Fragment implements NavigatePresenter.View {

    public static final String INCOMMING_MESSAGES = "Incomming messages ...";
    @BindView(R.id.btn_navigate)
    Button btn_navigate;
//    @BindView(R.id.log_textview)
    TextView logTextView;
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
                new BluetoothClient(BluetoothAdapter.getDefaultAdapter(),
                        Provider.provideBluetoothReaderPublishSubject())),
                Provider.provideBluetoothReaderPublishSubject());
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
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        logTextView.setText(INCOMMING_MESSAGES);
        navigatePresenter.readFromBluetooth();
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
    public void writeIncommingMessage(String message) {
        this.logTextView.append(message);
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
