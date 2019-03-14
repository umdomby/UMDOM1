package me.vshat.androidserver;

import android.content.Intent;
import android.graphics.Color;
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
import me.vshat.androidserver.server.ServerState;
import me.vshat.androidserver.server.ServerStateChangedEvent;
import me.vshat.androidserver.service.MyServiceBluetooth;
import me.vshat.androidserver.service.ServerService;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHol extends Fragment  {

    String[] sbprintArrayStr;
    //UMDOM1
    private TextView textViewStatus;
    private Button buttonControl,textViewButton1, textViewButton2, textViewButton3;
    ImageButton ImageButton1, ImageButton2, ImageButton3, ImageButtonPir1, ImageButtonGaz1;
    private ServerState serverState;
    private TextView textServerClient, textView1, textView2, textView3, textBluetooth, textView4;
    boolean flagStart = true,flagImageButton1 = true, flagImageButton2 = true, flagImageButton3 = true, flagImageButtonPir1 = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_hol, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);

        textViewStatus = getActivity().findViewById(R.id.tvStatus);
        textBluetooth = getActivity().findViewById(R.id.textBluetooth);
        buttonControl = getActivity().findViewById(R.id.btnControl);

        //textViewTimer = getActivity().findViewById(R.id.tvTimer);
        //textViewResponse = getActivity().findViewById(R.id.tvResponse);

        textServerClient = getActivity().findViewById(R.id.textServerClient);

        textView1 = getActivity().findViewById(R.id.textView1); //температура
        textView2 = getActivity().findViewById(R.id.textView2); //влажность
        textView3 = getActivity().findViewById(R.id.textView3); //colorAlarmHoll
        textView4 = getActivity().findViewById(R.id.textView4); //газ1

        textViewButton1 = getActivity().findViewById(R.id.textViewButton1);
        textViewButton2 = getActivity().findViewById(R.id.textViewButton2);
        textViewButton3 = getActivity().findViewById(R.id.textViewButton3);

        ImageButtonPir1 = getActivity().findViewById(R.id.ImageButtonPir1);
        ImageButtonGaz1 = getActivity().findViewById(R.id.ImageButtonGaz1);

        ImageButton1 = getActivity().findViewById(R.id.ImageButton1);
        ImageButton2 = getActivity().findViewById(R.id.ImageButton2);
        ImageButton3 = getActivity().findViewById(R.id.ImageButton3);
        detectStatus();

        buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(/*serverState == ServerState.RUNNING || */flagStart == false) {
                    ServerService.interrupt(); //прерывание
                    getActivity().stopService(new Intent(getActivity(), MyServiceBluetooth.class));
                    flagStart = true;
                } else {
                    ServerService.start(getActivity());
                    getActivity().startService(new Intent(getActivity(), MyServiceBluetooth.class));
                    flagStart = false;
                }
            }
        });


        //включение на оповещение
        ImageButtonPir1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagImageButtonPir1 == false) {
                    String text = "e";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButtonPir1.setImageResource(R.drawable.iconsmotion);
                    textView3.setText("OK");
                    textView3.setBackgroundColor(Color.parseColor("#22ae54"));
                    flagImageButtonPir1 = true;
                    EventBus.getDefault().post(new ClientEvent("f"));
                }
                else {
                    String text = "E";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButtonPir1.setImageResource(R.drawable.iconsmotiong);
                    textView3.setText("OK");
                    textView3.setBackgroundColor(Color.parseColor("#22ae54"));
                    flagImageButtonPir1 = false;
                    EventBus.getDefault().post(new ClientEvent("f"));
                }
            }
        });

        //вернуть цвет оповещение газа
        ImageButtonGaz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView4.setText("OK");
                textView4.setBackgroundColor(Color.parseColor("#22ae54"));
            }
        });

        textViewButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagImageButton1 == false) {
                    String text = "b";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton1.setImageResource(R.drawable.iconsonoff60);
                    flagImageButton1 = true;
                }
                else {
                    String text = "B";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton1.setImageResource(R.drawable.iconsonoff60g);
                    flagImageButton1 = false;
                }
            }
        });

        textViewButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagImageButton2 == false) {
                    String text = "c";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton2.setImageResource(R.drawable.iconsonoff60);
                    flagImageButton2 = true;
                }
                else {
                    String text = "C";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton2.setImageResource(R.drawable.iconsonoff60g);
                    flagImageButton2 = false;
                }
            }
        });

        textViewButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagImageButton3 == false) {
                    String text = "d";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton3.setImageResource(R.drawable.iconsonoff60);
                    flagImageButton3 = true;
                }
                else {
                    String text = "D";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton3.setImageResource(R.drawable.iconsonoff60g);
                    flagImageButton3 = false;
                }
            }
        });

        ImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagImageButton1 == false) {
                    String text = "b";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton1.setImageResource(R.drawable.iconsonoff60);
                    flagImageButton1 = true;
                }
                else {
                    String text = "B";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton1.setImageResource(R.drawable.iconsonoff60g);
                    flagImageButton1 = false;
                }
            }
        });

        ImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagImageButton2 == false) {
                    String text = "c";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton2.setImageResource(R.drawable.iconsonoff60);
                    flagImageButton2 = true;
                }
                else {
                    String text = "C";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton2.setImageResource(R.drawable.iconsonoff60g);
                    flagImageButton2 = false;
                }
            }
        });

        ImageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagImageButton3 == false) {
                    String text = "d";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton3.setImageResource(R.drawable.iconsonoff60);
                    flagImageButton3 = true;
                }
                else {
                    String text = "D";
                    EventBus.getDefault().post(new ClientEvent(text));
                    ImageButton3.setImageResource(R.drawable.iconsonoff60g);
                    flagImageButton3 = false;
                }
            }
        });
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
        //textViewResponse.setText("Ответ сервиса: " + event.getData());
        textServerClient.setText("Данные клиента: " + event.getData());
        switch (event.getData()) {
            case ("b"):
                ImageButton1.setImageResource(R.drawable.iconsonoff60);
                flagImageButton1 = true;
                break;
            case ("B"):
                ImageButton1.setImageResource(R.drawable.iconsonoff60g);
                flagImageButton1 = false;
                break;
            case ("c"):
                ImageButton2.setImageResource(R.drawable.iconsonoff60);
                flagImageButton2 = true;
                break;
            case ("C"):
                ImageButton2.setImageResource(R.drawable.iconsonoff60g);
                flagImageButton2 = false;
                break;
            case ("d"):
                ImageButton3.setImageResource(R.drawable.iconsonoff60);
                flagImageButton3 = true;
                break;
            case ("D"):
                ImageButton3.setImageResource(R.drawable.iconsonoff60g);
                flagImageButton3 = false;
                break;
                default:
                    break;
        }
    }

    //Таймер
//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onEvent(TimerEvent event) {
//        //получение таймера из сервиса
//        //textViewTimer.setText("Таймер: " + event.getData());
//    }

    //Bluetooth данные
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
        public void onEvent(BluetoothEvent event) {
        textBluetooth.setText("Bluetooth: " + event.getData());

        sbprintArrayStr = event.getData().split(",");
                        if(sbprintArrayStr[1].equals("b")) {
                            ImageButton1.setImageResource(R.drawable.iconsonoff60);
                            flagImageButton1 = true;
                        }
                        if(sbprintArrayStr[1].equals("B")){
                            ImageButton1.setImageResource(R.drawable.iconsonoff60g);
                            flagImageButton1 = false;
                        }
                        if(sbprintArrayStr[2].equals("c")) {
                            ImageButton2.setImageResource(R.drawable.iconsonoff60);
                            flagImageButton2 = true;
                        }
                        if(sbprintArrayStr[2].equals("C")) {
                            ImageButton2.setImageResource(R.drawable.iconsonoff60g);
                            flagImageButton2 = false;
                        }
                        if(sbprintArrayStr[3].equals("d")) {
                            ImageButton3.setImageResource(R.drawable.iconsonoff60);
                            flagImageButton3 = true;
                        }
                        if(sbprintArrayStr[3].equals("D")) {
                            ImageButton3.setImageResource(R.drawable.iconsonoff60g);
                            flagImageButton3 = false;
                        }
                        //оповещение газа в холле цвет и уведомление
                        if(sbprintArrayStr[14].equals("0")) {
                            textView4.setText("NO");
                            textView4.setBackgroundColor(Color.parseColor("#ff0000"));
                        }

                        if(sbprintArrayStr[14].equals("1")) {
//                            textView4.setText("OK");
//                            textView4.setBackgroundColor(Color.parseColor("#22ae54"));
                        }

                        if(sbprintArrayStr[16].equals("e")) {
                            ImageButtonPir1.setImageResource(R.drawable.iconsmotion);
                        }
                        if(sbprintArrayStr[16].equals("E")) {
                            ImageButtonPir1.setImageResource(R.drawable.iconsmotiong);;
                        }

                        if(sbprintArrayStr[17].equals("f")) {
                            textView3.setText("OK");
                            textView3.setBackgroundColor(Color.parseColor("#22ae54"));
                        }
                        if(sbprintArrayStr[17].equals("F")) {
                            textView3.setText("NO");
                            textView3.setBackgroundColor(Color.parseColor("#ff0000"));
                        }

                        textView1.setText(sbprintArrayStr[9].substring(0,2));
                        textView2.setText(sbprintArrayStr[10].substring(0,2) + "%");
    }

    //Опубликовать события
    public void onActionClick(View view) {
        String text = "b";
        EventBus.getDefault().post(new ClientEvent(text));
    }

    //Опубликовать события:
    public void onActionClick2(View view) {
        String text = "B";
        EventBus.getDefault().post(new ClientEvent(text));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
