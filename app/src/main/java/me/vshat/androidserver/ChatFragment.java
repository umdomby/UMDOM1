package me.vshat.androidserver;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.vshat.androidserver.event.BluetoothEvent;
import me.vshat.androidserver.event.ClientEvent;
import me.vshat.androidserver.event.ServerEvent;
import me.vshat.androidserver.event.TimerEvent;
import me.vshat.androidserver.server.ServerState;
import me.vshat.androidserver.server.ServerStateChangedEvent;
import me.vshat.androidserver.service.MyServiceBluetooth;
import me.vshat.androidserver.service.ServerService;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    //UMDOM1
    private TextView textViewStatus;
    private Button buttonControl;
    ImageButton ImageButton2, ImageButton3;
    private TextView textViewTimer;
    private TextView textBluetooth;
    private TextView textViewResponse;
    private ServerState serverState;
    private TextView textServerClient;
    boolean flag2 = false, flag3 = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);



        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onStart(){
        super.onStart();

        EventBus.getDefault().register(this);

        textViewStatus = getActivity().findViewById(R.id.tvStatus);
        textBluetooth = getActivity().findViewById(R.id.textBluetooth);
        buttonControl = getActivity().findViewById(R.id.btnControl);
        textViewTimer = getActivity().findViewById(R.id.tvTimer);
        textViewResponse = getActivity().findViewById(R.id.tvResponse);
        textServerClient = getActivity().findViewById(R.id.textServerClient);
        ImageButton2 = getActivity().findViewById(R.id.ImageButton2);
        detectStatus();

        buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serverState == ServerState.RUNNING) {
                    ServerService.interrupt(); //прерывание
                    getActivity().stopService(new Intent(getActivity(), MyServiceBluetooth.class));

                } else {
                    ServerService.start(getActivity());

                    getActivity().startService(new Intent(getActivity(), MyServiceBluetooth.class));
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void applyState(ServerState serverState) {
        this.serverState = serverState;

        if(serverState == ServerState.RUNNING) {
            buttonControl.setText("ONLINE");
        } else {
            buttonControl.setText("OFFLINE");
        }
    }


    private void detectStatus() {
        if(!ServerService.isRunning()) {
            textViewStatus.setText("Статус: " + ServerState.STOPPED.getText());
            applyState(ServerState.STOPPED);
        }
    }

    //Статус
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ServerStateChangedEvent event) {
        textViewStatus.setText("Статус: " + event.toString());
        applyState(event.getServerState());
    }

    //Ответ от сервиса
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ServerEvent event) {
        textViewResponse.setText("Ответ сервиса: " + event.getData());
        textServerClient.setText("Данные клиента: " + event.getData());
    }

    //Таймер
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(TimerEvent event) {
        //получение таймера из сервиса
        textViewTimer.setText("Таймер: " + event.getData());
    }

    //Bluetooth данные
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(BluetoothEvent event) {
        textBluetooth.setText("Bluetooth: " + event.getData());
    }

    //Кнопка запуска сервиса
//    public void onControlClick(View view) {
//                if(serverState == ServerState.RUNNING) {
//                    ServerService.interrupt(); //прерывание
//                    getActivity().stopService(new Intent(getActivity(), MyServiceBluetooth.class));
//
//                } else {
//                    ServerService.start(getActivity());
//
//                    getActivity().startService(new Intent(getActivity(), MyServiceBluetooth.class));
//                }
//    }

    //Опубликовать события
    public void onActionClick(View view) {
        String text = "b";//((Button) view).getText().toString();
        EventBus.getDefault().post(new ClientEvent(text));
    }

    //Опубликовать события:
    public void onActionClick2(View view) {
        String text = "B";
        EventBus.getDefault().post(new ClientEvent(text));
    }

}
