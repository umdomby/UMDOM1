package me.vshat.androidserver;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettings extends Fragment implements View.OnClickListener {

    public FragmentSettings() {
        // Required empty public constructor
    }

    EditText dataBluetoothSettings, dataPortSettings;
    Button btnSave1, btnLoad1,btnSave2, btnLoad2;
    SharedPreferences settings;
    final String SAVED_TEXT = "bluetooth";
    final String SAVED_TEXT2 = "port";
    public static String txtBluetoothData;
    public static String txtPortData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        dataBluetoothSettings = getActivity().findViewById(R.id.dataBluetoothSettings);
        btnSave1 = getActivity().findViewById(R.id.btnSave1);
        btnSave1.setOnClickListener(this);
        btnLoad1 = getActivity().findViewById(R.id.btnLoad1);
        btnLoad1.setOnClickListener(this);
        loadBluetoothData();


        dataPortSettings = getActivity().findViewById(R.id.dataPortSettings);
        btnSave2 = getActivity().findViewById(R.id.btnSave2);
        btnSave2.setOnClickListener(this);
        btnLoad2 = getActivity().findViewById(R.id.btnLoad2);
        btnLoad2.setOnClickListener(this);
        loadPortData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave1:
                saveBluetoothData();
                break;
            case R.id.btnLoad1:
                loadBluetoothData();
                break;
            case R.id.btnSave2:
                saveBluetoothData();
                break;
            case R.id.btnLoad2:
                loadBluetoothData();
                break;
            default:
                break;
        }
    }

    public void saveBluetoothData() {
        settings = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SAVED_TEXT, dataBluetoothSettings.getText().toString());
        editor.commit();
    }

    public void loadBluetoothData() {
        settings = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
        txtBluetoothData = settings.getString(SAVED_TEXT, "");
        dataBluetoothSettings.setText(txtBluetoothData);
    }

    public void savePortData() {
        settings = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SAVED_TEXT2, dataPortSettings.getText().toString());
        editor.commit();
    }

    public void loadPortData() {
        settings = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
        txtPortData = settings.getString(SAVED_TEXT2, "");
        dataPortSettings.setText(txtPortData);
    }

    public static String dataBluetooth(){
        return txtBluetoothData;
    }

    public static int dataPort(){
        return Integer.parseInt(txtPortData);
    }

//     public String adressBluetooth(){
//        String a = savedText;
//         return a;
//     }


//    public void dataBluetoothStaticMethod(){
//
//        EventBus.getDefault().post(new PreferencesEvent(savedText));
//    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveBluetoothData();
        savePortData();
    }
}