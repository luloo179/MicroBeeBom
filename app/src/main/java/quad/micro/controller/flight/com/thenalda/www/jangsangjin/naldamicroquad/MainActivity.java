package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telecom.Connection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.Token;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.fly_fragment.Fly2Fragment;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.main_fragment.MainFragment;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.network.MqttConnection;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.network.MqttNalda;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.network.MqttThread;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment.SetupBiasFragment;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment.SetupGainFragment;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment.SetupInterfaceFragment;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment.SetupListFragment;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment.SetupTrimFragment;

public class MainActivity extends FragmentActivity implements MainFragment.IMainFragment, Fly2Fragment.IFlay2Fragment, SetupListFragment.ISetupListFragment, SetupGainFragment.ISetupGainFragment, SetupTrimFragment.ISetupTrimFragment, SetupBiasFragment.ISetupBiasFragment {

    private final String LOCAL_URL = "tcp://192.168.42.1:1883";
    private final String GLOBAL_URL = "tcp://192.168.42.1:1883";
    private int CONNECTION_STATE;
    private int CONNECTION_TYPE;
    private String MQTT_ID;
    private byte[] controlByteArray = new byte[7];
    private float autopilotAlt;

    private ProgressDialog progressDialog;
    private MqttAndroidClient mqttAndroidClient;

    private MainFragment mainFragment;
    private Fly2Fragment fly2Fragment;

    private MqttNalda mqttNalda;
    private MqttThread mqttThread;
    private int mqttConnectionState;
    private int fragmentIndex;
    private int connectionType;
    private Handler mainFragmentHandler;
    private Handler fly2FragmentHandler;
    private IMqttThread iMqttThread;
    private int setupTypeIndex;
    private Handler setupFragmentHandler;

    public interface IMqttThread {

        void iMqttThread(int type, String data);

    }

    private Handler mainActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //main fragment control handler
            if (fragmentIndex == 0) {

                if (mainFragmentHandler != null) {

                    int type = msg.getData().getInt("TYPE", -1);

                    if (type != -1) {

                        if (type == 0) {

                            int connectionState = msg.getData().getInt("CONN", -1);

                            if (connectionState != -1) {

                                if (connectionState == 0) {
                                    //connection lost

                                    if (mainFragmentHandler != null) {

                                        mqttConnectionState = 0;
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("CONN", 0);
                                        message.setData(bundle);

                                        mainFragmentHandler.sendMessage(message);

                                    }

                                }

                                if (connectionState == 1) {
                                    //connection success

                                    if (mainFragmentHandler != null) {

                                        mqttNalda.setMqttSub();

                                        mqttConnectionState = 1;
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("CONN", 1);
                                        message.setData(bundle);

                                        mainFragmentHandler.sendMessage(message);

                                    }

                                }

                            }

                        }

                    }

                }

            }

            //fly fragment control handler
            if (fragmentIndex == 1) {

                int type = msg.getData().getInt("TYPE", -1);
                //connection relate handler data
                if (type == 0) {

                    int connectionState = msg.getData().getInt("CONN", -1);

                    if (connectionState == 0) {
                        //disconnect

                        if (fly2FragmentHandler != null) {

                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putInt("TYPE", 0);
                            bundle.putInt("CONN", 0);
                            message.setData(bundle);

                            fly2FragmentHandler.sendMessage(message);

                        }

                    }

                    if (connectionState == 1) {
                        //connect

                        if (fly2FragmentHandler != null) {


                            if (fly2FragmentHandler != null) {

                                Message message = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putInt("TYPE", 0);
                                bundle.putInt("CONN", 1);
                                message.setData(bundle);

                                fly2FragmentHandler.sendMessage(message);

                            }

                        }

                    }

                }

                //autopilot state handler data
                if (type == 1) {

                    String payload = msg.getData().getString("STATE", "NULL");
                    Log.i("FLY", payload);

                    if (fly2FragmentHandler != null) {

                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("TYPE", 1);
                        bundle.putString("STATE", payload);
                        message.setData(bundle);

                        fly2FragmentHandler.sendMessage(message);

                    }

                }


            }

            //settings fragment
            if (fragmentIndex == 2) {

                String payload = msg.getData().getString("SETUP", "NULL");

                if (setupFragmentHandler != null) {

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("SETUP", payload);
                    message.setData(bundle);

                    setupFragmentHandler.sendMessage(message);

                }

            }

        }
    };

    @Override
    public void iMainFragment(int type, Handler handler) {
        //main activity

        switch (type) {

            case 0: {
                //connection button click

                fragmentIndex = 0;

                if (CONNECTION_STATE == 0) {
                    //not connected

                    createMqttConnection();

                } else if (CONNECTION_STATE == 1) {
                    //connected

                    unsubscribeMqtt();
                    //disconnectMqttConnection();

                } else if (CONNECTION_STATE == 2) {
                    //connect lose

                    unsubscribeMqtt();
                    //disconnectMqttConnection();

                }

                break;
            }

            case 1: {
                //fly button click

                fragmentIndex = 1;
                fragmentChange(fragmentIndex);

                break;
            }

            case 2: {
                //setup button click

                fragmentIndex = 2;
                fragmentChange(fragmentIndex);

                break;
            }

            case 3: {
                //connect us button click

                fragmentIndex = 3;

                break;
            }

        }

    }

    @Override
    public synchronized void iFly2Fragment(int type, byte data) {
        //fly fragment callback

        switch (type) {

            case -1: {

                for (int i = 0; i < 7; i++) {

                    controlByteArray[i] = 0x00;

                }

                mqttPublish(controlByteArray);

                fragmentIndex = 0;
                fragmentChange(fragmentIndex);

                break;

            }

            default: {

                controlByteArray[type] = data;
                mqttPublish(controlByteArray);

                return;

            }

        }

    }

    @Override
    public void iSetupListFragment(int type) {
        //settings fragment callback

        if (type == 0) {
            //back button

            this.fragmentIndex = 0;
            fragmentChange(type);

        } else {
            //settings menu fragment click

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (type == 1) {
                //account
                setupTypeIndex = type;

            }

            if (type == 2) {
                //connection
                setupTypeIndex = type;

            }

            if (type == 3) {
                //interface
                setupTypeIndex = type;

                SetupInterfaceFragment setupInterfaceFragment = new SetupInterfaceFragment();
                fragmentTransaction.replace(R.id.activity_main, setupInterfaceFragment);
                fragmentTransaction.commit();


            }

            if (type >= 4 && type <= 8) {
                //roll pitch yaw altitude position
                setupTypeIndex = type;
                SetupGainFragment setupGainFragment = SetupGainFragment.newInstance(setupTypeIndex, MainActivity.this);
                setupFragmentHandler = setupGainFragment.getSetupGainHandler();
                fragmentTransaction.replace(R.id.activity_main, setupGainFragment);
                fragmentTransaction.commit();

            }

            if (type == 9) {
                //trim setup
                setupTypeIndex = type;
                SetupTrimFragment setupTrimFragment = SetupTrimFragment.newInstance(setupTypeIndex, MainActivity.this);
                setupFragmentHandler = setupTrimFragment.getSetupTrimHandler();
                fragmentTransaction.replace(R.id.activity_main, setupTrimFragment);
                fragmentTransaction.commit();

            }

            if (type >= 10 && type <= 12) {
                //accel gyro mag
                setupTypeIndex = type;
                SetupBiasFragment setupBiasFragment = SetupBiasFragment.newInstance(setupTypeIndex, MainActivity.this);
                setupFragmentHandler = setupBiasFragment.getSetupBiasHandler();
                fragmentTransaction.replace(R.id.activity_main, setupBiasFragment);
                fragmentTransaction.commit();

            }

        }

    }

    @Override
    public void iSetupGainFragment(int type, int gainA, int gainB) {
        //roll pitch yaw alt pos gain setting

        Log.i("SETUP", type + " TYPE");

        if (type == 0) {
            //back button click

            this.fragmentIndex = 2;
            fragmentChange(this.fragmentIndex);

        }

        if (type == 4) {
            //roll gain

            if (CONNECTION_STATE == 1) {

                byte[] setupByteArray = new byte[6];
                setupByteArray[0] = (byte) 0x01;
                setupByteArray[1] = (byte) 1;
                setupByteArray[2] = (byte) gainA;
                setupByteArray[3] = (byte) (gainA >>> 8);
                setupByteArray[4] = (byte) gainB;
                setupByteArray[5] = (byte) (gainB >>> 8);

                mqttPublish(setupByteArray);

            } else {

                Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

            }

        }

        if (type == 5) {
            //pitch gain

            if (CONNECTION_STATE == 1) {

                byte[] setupByteArray = new byte[6];
                setupByteArray[0] = (byte) 0x01;
                setupByteArray[1] = (byte) 2;
                setupByteArray[2] = (byte) gainA;
                setupByteArray[3] = (byte) (gainA >>> 8);
                setupByteArray[4] = (byte) gainB;
                setupByteArray[5] = (byte) (gainB >>> 8);

                mqttPublish(setupByteArray);

            } else {

                Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

            }

        }

        if (type == 6) {
            //yaw gain

            if (CONNECTION_STATE == 1) {

                byte[] setupByteArray = new byte[6];
                setupByteArray[0] = (byte) 0x01;
                setupByteArray[1] = (byte) 3;
                setupByteArray[2] = (byte) gainA;
                setupByteArray[3] = (byte) (gainA >>> 8);
                setupByteArray[4] = (byte) gainB;
                setupByteArray[5] = (byte) (gainB >>> 8);

                mqttPublish(setupByteArray);

            } else {

                Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

            }

        }

        if (type == 7) {
            //altitude gain

            if (CONNECTION_STATE == 1) {

                byte[] setupByteArray = new byte[6];
                setupByteArray[0] = (byte) 0x01;
                setupByteArray[1] = (byte) 4;
                setupByteArray[2] = (byte) gainA;
                setupByteArray[3] = (byte) (gainA >>> 8);
                setupByteArray[4] = (byte) gainB;
                setupByteArray[5] = (byte) (gainB >>> 8);

                mqttPublish(setupByteArray);

            } else {

                Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

            }

        }

        if (type == 8) {
            //position gain

            if (CONNECTION_STATE == 1) {

                byte[] setupByteArray = new byte[6];
                setupByteArray[0] = (byte) 0x01;
                setupByteArray[1] = (byte) 5;
                setupByteArray[2] = (byte) gainA;
                setupByteArray[3] = (byte) (gainA >>> 8);
                setupByteArray[4] = (byte) gainB;
                setupByteArray[5] = (byte) (gainB >>> 8);

                mqttPublish(setupByteArray);

            } else {

                Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

            }

        }

    }

    @Override
    public void iSetupTrimFragment(int type, String order1, String order2) {
        //trim setup fragment callback

        Log.i("TRIM", type + " TYPE");

        if (type == 0) {
            //back button click callback

            this.fragmentIndex = 2;
            fragmentChange(this.fragmentIndex);

        } else {

            if (type == 9) {

                if (mqttConnectionState == 1) {

                    if (mqttNalda != null) {

                        mqttNalda.setMqttPublishSetting("006" + order1 + order2);

                    } else {

                        Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

                }

            }

        }

    }

    @Override
    public void iSetupBiasFragment(int type) {
        //bias setup callback

        if (type == 0) {
            //back button click

            this.fragmentIndex = 2;
            fragmentChange(this.fragmentIndex);

        } else {

            if (type == 10) {

                Log.i("ACCEL BIAS", "ACCEL");

                if (CONNECTION_STATE == 1) {

                    byte[] setupByteArray = new byte[6];
                    setupByteArray[0] = (byte) 0x01;
                    setupByteArray[1] = (byte) 7;
                    setupByteArray[2] = (byte) 0;
                    setupByteArray[3] = (byte) 0;
                    setupByteArray[4] = (byte) 0;
                    setupByteArray[5] = (byte) 0;

                    mqttPublish(setupByteArray);

                } else {

                    Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

                }

            }

            if (type == 11) {

                if (CONNECTION_STATE == 1) {

                    byte[] setupByteArray = new byte[6];
                    setupByteArray[0] = (byte) 0x01;
                    setupByteArray[1] = (byte) 8;
                    setupByteArray[2] = (byte) 0;
                    setupByteArray[3] = (byte) 0;
                    setupByteArray[4] = (byte) 0;
                    setupByteArray[5] = (byte) 0;

                    mqttPublish(setupByteArray);

                } else {

                    Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

                }

            }

            if (type == 12) {
                if (CONNECTION_STATE == 1) {

                    byte[] setupByteArray = new byte[6];
                    setupByteArray[0] = (byte) 0x01;
                    setupByteArray[1] = (byte) 9;
                    setupByteArray[2] = (byte) 0;
                    setupByteArray[3] = (byte) 0;
                    setupByteArray[4] = (byte) 0;
                    setupByteArray[5] = (byte) 0;

                    mqttPublish(setupByteArray);

                } else {

                    Toast.makeText(MainActivity.this, "BeeBom is not connected", Toast.LENGTH_SHORT).show();

                }

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.CONNECTION_STATE = sharedPref.getInt("CONNECT_STATE", 0);
        this.CONNECTION_TYPE = sharedPref.getInt("CONNECT_TYPE", 0);
        this.MQTT_ID = sharedPref.getString("MQTT_ID", "NULL");

        if (this.MQTT_ID.compareTo("NULL") == 0) {

            this.MQTT_ID = MqttClient.generateClientId();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("MQTT_ID", this.MQTT_ID);
            editor.commit();

        }

        if (this.CONNECTION_STATE == 1) {

            createMqttConnection();

        }

        this.fragmentIndex = 0;
        fragmentChange(this.fragmentIndex);

    }

    //fragment change method
    private void fragmentChange(int index) {

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (index) {

            case 0: {
                //main menu fragment

                int mqttConnectionState = 0;

                switch (CONNECTION_TYPE) {

                    case 0: {
                        //local

                        if (CONNECTION_STATE == 0) {
                            //not connect

                            mqttConnectionState = 0;

                        }

                        if (CONNECTION_STATE == 1) {
                            //connectioned

                            mqttConnectionState = 1;

                        }

                        if (CONNECTION_STATE == 2) {
                            //connect lose

                            mqttConnectionState = 4;

                        }

                        break;

                    }

                    case 1: {
                        //global

                        if (CONNECTION_STATE == 0) {
                            //not connect

                            mqttConnectionState = 2;

                        }

                        if (CONNECTION_STATE == 1) {
                            //connected

                            mqttConnectionState = 3;

                        }

                        if (CONNECTION_STATE == 2) {
                            //connect lose

                            mqttConnectionState = 4;

                        }

                        break;

                    }

                }

                this.mainFragment = MainFragment.newInstance(mqttConnectionState, MainActivity.this);
                fragmentTransaction.replace(R.id.activity_main, this.mainFragment);
                fragmentTransaction.commit();

                break;

            }

            case 1: {
                //start control thread and start viewer thread
                //handler update autopilot state info

                for (int i = 0; i < 7; i++) {

                    controlByteArray[i] = (byte) 0;

                }

                this.fly2Fragment = Fly2Fragment.newInstance(CONNECTION_STATE, MainActivity.this);
                fragmentTransaction.replace(R.id.activity_main, fly2Fragment);
                fragmentTransaction.commit();

                break;

            }

            case 2: {
                //settings listview fragment

                SetupListFragment setupListFragment = SetupListFragment.newInstance(MainActivity.this);

                fragmentTransaction.replace(R.id.activity_main, setupListFragment);
                fragmentTransaction.commit();

                break;

            }

            case 3: {
                //connect us

                break;

            }

        }

    }

    void createMqttConnection() {

        try {

            if (this.mqttAndroidClient != null) {

                CONNECTION_STATE = 1;

                int mqttConnectionState = 0;

                switch (CONNECTION_TYPE) {

                    case 0: {
                        //local

                        mqttConnectionState = 1;

                        break;


                    }

                    case 1: {
                        //global

                        mqttConnectionState = 3;

                        break;


                    }

                }

                if (mainFragment != null) {

                    mainFragment.updateConnectionButton(mqttConnectionState);

                }

                createMqttSubscribe();

                return;

            }

            Log.i("MQTT CREATE", "MQTT CREATE");

            this.progressDialog = new ProgressDialog(MainActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.progressDialog.setMessage("Please Wait...");
            this.progressDialog.show();

            if (this.mqttAndroidClient == null) {

                if (this.CONNECTION_TYPE == 0) {

                    this.mqttAndroidClient = new MqttAndroidClient(MainActivity.this, LOCAL_URL, this.MQTT_ID);

                }

                if (this.CONNECTION_TYPE == 1) {

                    this.mqttAndroidClient = new MqttAndroidClient(MainActivity.this, LOCAL_URL, this.MQTT_ID);

                }

            }

            this.mqttAndroidClient.setCallback(mqttCallbackExtended);

            IMqttToken conToken = this.mqttAndroidClient.connect();
            conToken.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    if (asyncActionToken != null) {

                        asyncActionToken.setActionCallback(null);

                    }

                    if (progressDialog != null) {

                        CONNECTION_STATE = 1;

                        if (mainFragment != null) {

                            int mqttConnectionState = 0;

                            switch (CONNECTION_TYPE) {

                                case 0: {
                                    //local

                                    mqttConnectionState = 1;

                                    break;


                                }

                                case 1: {
                                    //global

                                    mqttConnectionState = 3;

                                    break;


                                }

                            }

                            if (mainFragment != null) {

                                mainFragment.updateConnectionButton(mqttConnectionState);

                            }

                        }

                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Connection Success", Toast.LENGTH_SHORT).show();

                    }

                    Log.i("MQTT CREATE", "SUC");

                    createMqttSubscribe();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    if (asyncActionToken != null) {

                        asyncActionToken.setActionCallback(null);

                    }

                    if (progressDialog != null) {

                        CONNECTION_STATE = 0;

                        if (mainFragment != null) {

                            int mqttConnectionState = 0;

                            switch (CONNECTION_TYPE) {

                                case 0: {
                                    //local

                                    mqttConnectionState = 0;

                                    break;


                                }

                                case 1: {
                                    //global

                                    mqttConnectionState = 2;

                                    break;


                                }

                            }

                            if (mainFragment != null) {

                                mainFragment.updateConnectionButton(mqttConnectionState);

                            }

                        }

                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

                    }

                    CONNECTION_STATE = 0;

                }

            });

        } catch (MqttException e) {

            e.printStackTrace();

        }

    }

    void disconnectMqttConnection() {

        try {

            Log.i("MQTT DIS", "MQTT DIS");

            this.progressDialog = new ProgressDialog(MainActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.progressDialog.setMessage("Please Wait...");
            this.progressDialog.show();

            IMqttToken disconToken = this.mqttAndroidClient.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    if (asyncActionToken != null) {

                        asyncActionToken.setActionCallback(null);

                    }

                    if (progressDialog != null) {

                        int mqttConnectionState = 0;

                        switch (CONNECTION_TYPE) {

                            case 0: {
                                //local

                                mqttConnectionState = 0;

                                break;


                            }

                            case 1: {
                                //global

                                mqttConnectionState = 2;

                                break;


                            }

                        }

                        if (mainFragment != null) {

                            mainFragment.updateConnectionButton(mqttConnectionState);

                        }

                        progressDialog.dismiss();

                    }

                    CONNECTION_STATE = 0;

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {

                    if (asyncActionToken != null) {

                        asyncActionToken.setActionCallback(null);

                    }

                    if (progressDialog != null) {

                        int mqttConnectionState = 0;

                        switch (CONNECTION_TYPE) {

                            case 0: {
                                //local

                                mqttConnectionState = 0;

                                break;


                            }

                            case 1: {
                                //global

                                mqttConnectionState = 2;

                                break;


                            }

                        }

                        if (mainFragment != null) {

                            mainFragment.updateConnectionButton(mqttConnectionState);

                        }

                        progressDialog.dismiss();

                    }

                    CONNECTION_STATE = 0;

                }

            });

        } catch (MqttException e) {

            e.printStackTrace();

        }

    }

    public void createMqttSubscribe() {

        int qos = 0;
        String topic = "beebom/pilot";

        try {

            IMqttToken subToken = this.mqttAndroidClient.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    if (asyncActionToken != null) {

                        asyncActionToken.setActionCallback(null);

                    }

                    Log.i("MQTT CR SUB", "SUB");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    if (asyncActionToken != null) {

                        asyncActionToken.setActionCallback(null);

                    }

                }
            });

        } catch (MqttException e) {

            Log.i("GET", e.toString());

        }

    }

    private void unsubscribeMqtt() {

        Log.i("UNSUB", "UN");

        this.progressDialog = new ProgressDialog(MainActivity.this);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setMessage("Please Wait...");
        this.progressDialog.show();

        String topic = "beebom/pilot";

        try {

            IMqttToken unsubToken = mqttAndroidClient.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    if (asyncActionToken != null) {

                        asyncActionToken.setActionCallback(null);

                    }

                    if (progressDialog != null) {

                        int mqttConnectionState = 0;

                        switch (CONNECTION_TYPE) {

                            case 0: {
                                //local

                                mqttConnectionState = 0;

                                break;


                            }

                            case 1: {
                                //global

                                mqttConnectionState = 2;

                                break;


                            }

                        }

                        if (mainFragment != null) {

                            mainFragment.updateConnectionButton(mqttConnectionState);

                        }

                        progressDialog.dismiss();

                    }

                    CONNECTION_STATE = 0;

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {

                    if (asyncActionToken != null) {

                        asyncActionToken.setActionCallback(null);

                    }

                    if (progressDialog != null) {

                        int mqttConnectionState = 0;

                        switch (CONNECTION_TYPE) {

                            case 0: {
                                //local

                                mqttConnectionState = 0;

                                break;


                            }

                            case 1: {
                                //global

                                mqttConnectionState = 2;

                                break;


                            }

                        }

                        if (mainFragment != null) {

                            mainFragment.updateConnectionButton(mqttConnectionState);

                        }

                        progressDialog.dismiss();

                    }

                    CONNECTION_STATE = 0;

                }

            });

        } catch (MqttException e) {

            e.printStackTrace();

        }

    }

    private boolean mqttPublish(byte[] payload) {

        String topic = "pilot/beebom";

        try {

            MqttMessage message = new MqttMessage(payload);
            message.setQos(0);
            message.setRetained(false);

            if (this.mqttAndroidClient != null) {

                this.mqttAndroidClient.publish(topic, message);

                return true;

            }

            return false;

        } catch (MqttException e) {

            Log.i("ERROR", e.toString());

            return false;

        }

    }

    private MqttCallbackExtended mqttCallbackExtended = new MqttCallbackExtended() {

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {

        }

        @Override
        public void connectionLost(Throwable cause) {

            CONNECTION_STATE = 3;

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            Log.i("TOPIC", message.toString());

            String payload = new String(message.getPayload());

            JSONObject jsonObject = new JSONObject(payload);

            try {

                int alt = jsonObject.getInt("alt");

                autopilotAlt = alt / 10.0f;

                if(fly2Fragment != null && fragmentIndex == 1){

                    fly2Fragment.updateAltitude(autopilotAlt);

                }

            } catch (JSONException e) {

                Log.i("JSON ERROR",e.toString());

            }

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {


        }
    };


    //back button and app close
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if (fragmentIndex == 0) {

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                alert_confirm.setMessage("Exit App?").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mqttNalda != null) {

                            if (mqttConnectionState == 1) {

                                mqttNalda.disConnect();

                            }
                        }

                        finish();

                    }
                }).setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                return;

                            }
                        });

                AlertDialog alert = alert_confirm.create();
                alert.show();

            }

            if (fragmentIndex == 1) {

                fragmentIndex = 0;
                fragmentChange(fragmentIndex);

            }

            return false;

        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
