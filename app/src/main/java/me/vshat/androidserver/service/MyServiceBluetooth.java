package me.vshat.androidserver.service;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import me.vshat.androidserver.FragmentSettings;
import me.vshat.androidserver.R;
import me.vshat.androidserver.event.BluetoothEvent;
import me.vshat.androidserver.event.ClientEvent;
import me.vshat.androidserver.event.ServerEvent;

import static me.vshat.androidserver.service.NotificationHelper.CHANNEL_1_ID;
import static me.vshat.androidserver.service.NotificationHelper.CHANNEL_2_ID;


public class MyServiceBluetooth extends Service {
    private final static String TAG = MyServiceBluetooth.class.getSimpleName();
    final String LOG_TAG = "myLogs";

    //Notification2
    private NotificationManagerCompat notificationManager;


    //TCP/IP Server
    final Handler handler = new Handler();
    private boolean end = false;

    //Settingss
    FragmentSettings fs = new FragmentSettings();

    //---Bluetooth---
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter;
    private UUID myUUID;
    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;
    private StringBuilder sb = new StringBuilder();
    private BluetoothSocket btSocket = null;
    final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";
    //private static String address = "00:21:13:04:96:D0";
    //private static String address = "98:D3:32:31:59:C6";

    //public static String address = "00:21:13:04:97:D8";
    public static String address;
    String sbprint;



    String[] sbprintArrayStr; //получение данных на планшет по bluetooth
    //---End Bluetooth---

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");

        //---Bluetooth---
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "BLUETOOTH NOT support", Toast.LENGTH_LONG).show();
            return;
        }
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
            return;
        }
        //---End Bluetooth---
        EventBus.getDefault().register(this);
        //Notification2
        notificationManager = NotificationManagerCompat.from(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyServiceBluetooth onStartCommand, name = " + intent.getStringExtra("name"));
        //readFlags(flags);
        //---Bluetooth---
        address = FragmentSettings.dataBluetooth();
        Log.d(LOG_TAG, "------------------------------------------------ " + address);
        setup();

        //---End Bluetooth---
        return Service.START_STICKY;
    }


    public static void start(Context context) {
        //Notification1
        Intent starter = new Intent(context, ServerService.class);
        context.startService(starter);
    }

    private class ThreadConnectBTdevice extends Thread { // Поток для коннекта с Bluetooth
        private BluetoothSocket bluetoothSocket = null;

        private ThreadConnectBTdevice(BluetoothDevice device) {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() { // Коннект
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {

                    e1.printStackTrace();
                }
            }
            if (success) {  // Если законнектились, тогда открываем панель с кнопками и запускаем поток приёма и отправки данных
                myThreadConnected = new ThreadConnected(bluetoothSocket);
                myThreadConnected.start(); // запуск потока приёма и отправки данных
            }
        }

        public void cancel() {

            Toast.makeText(getApplicationContext(), "Close - BluetoothSocket", Toast.LENGTH_LONG).show();
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Получение адреса bluetooth
//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onEvent(PreferencesEvent event) {
//        //address = event.getData();
//        Log.d(LOG_TAG, "DataBluetooth " + event.getData());
//    }

    private void setup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Создание списка сопряжённых Bluetooth-устройств
                BluetoothDevice device2 = bluetoothAdapter.getRemoteDevice(address);
                myThreadConnectBTdevice = new ThreadConnectBTdevice(device2);
                myThreadConnectBTdevice.start();  // Запускаем поток для подключения Bluetooth
            }
        }).start();
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    private class ThreadConnected extends Thread {    // Поток - приём и отправка данных
        private final BluetoothSocket copyBtSocket;
        private final InputStream connectedInputStream; //приём
        private final OutputStream connectedOutputStream; //отправка

        public ThreadConnected(BluetoothSocket socket) {
            copyBtSocket = socket;
            InputStream in = null;
            OutputStream out = null;
            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() { // Приём данных
            while (true) {
                try {
                    byte[] buffer = new byte[1];
                    int bytes = connectedInputStream.read(buffer);
                    String strIncom = new String(buffer, 0, bytes);
                    sb.append(strIncom); // собираем символы в строку
                    int endOfLineIndex = sb.indexOf("\r\n"); // определяем конец строки
                    if (endOfLineIndex > 0) {
                        sbprint = sb.substring(0, endOfLineIndex);
                        sb.delete(0, sb.length());
                        Log.d(LOG_TAG, "***MyServiceBluetooth: " + sbprint + "***");

                        sbprintArrayStr = sbprint.split(",");

                        //оповещение газа в холле
                        if(sbprintArrayStr[14].equals("0")){
                             notificationGaz1();
                        }

                        //оповещение движение в холле[15], OnOffAlarmHoll[16]
                        if(sbprintArrayStr[15].equals("1")){
                            if(sbprintArrayStr[16].equals("E")){
                                if(sbprintArrayStr[17].equals("F")){
                                    if (myThreadConnected != null) {
                                        byte[] bytesToSend = "F".getBytes();
                                        myThreadConnected.write(bytesToSend);}
                                }
                                else {
                                    //оповещение движение в Холле цветом
                                    notificationPir1();
                                    if (myThreadConnected != null) {
                                        byte[] bytesToSend = "F".getBytes();
                                        myThreadConnected.write(bytesToSend);}
                                }

                            }
                        }


                        //отправляем данные
                        EventBus.getDefault().postSticky(new BluetoothEvent(sbprint));

                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //Приход данных------------------------Д-А-Н-Н-Ы-Е---------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ClientEvent event) {
        EventBus.getDefault().postSticky(new ServerEvent(event.getData()));

/////////////

        Log.e(LOG_TAG, "***MyServiceBluetooth_onEvent " + event.getData() + "***");
        if (myThreadConnected != null) {
            byte[] bytesToSend = event.getData().getBytes();
            myThreadConnected.write(bytesToSend);
        }
    }

//    void sendDataBluetooth(){
//        if (myThreadConnected != null) {
//        byte[] bytesToSend = "b".getBytes();
//        myThreadConnected.write(bytesToSend);}
//    }

    void notificationGaz1(){
       Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.iconsinfo)
                .setContentTitle("Сработка газа")
                .setContentText("Наличие газа в холле")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }

    void notificationPir1(){

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.iconsinfo)
                .setContentTitle("Сработка движения 1")
                .setContentText("Наличие движения в холле")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(2, notification);
    }


    @Override
    public void onDestroy() { // Закрытие приложения
        super.onDestroy();

//        if(myThreadConnectBTdevice!=null) myThreadConnectBTdevice.cancel();
//        myThreadConnected.interrupt();
//        myThreadConnectBTdevice.interrupt();
//        myThreadConnected.interrupt();
//        myThreadConnectBTdevice.interrupt();



        Log.e(LOG_TAG, "***myThreadConnectBTdevice ЗАКРЫТ ");
    }

}