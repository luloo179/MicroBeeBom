package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.network;

import android.os.AsyncTask;
import android.util.Log;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.MainActivity;

/**
 * Created by jangsangjin on 2017. 2. 22..
 */

public class MqttThread extends AsyncTask<Void, Void, Void> implements MainActivity.IMqttThread{

    private int killCode;
    private MqttNalda mqttNalda;
    private String arm, mode, roll, pitch, yaw, throttle;

    @Override
    public void iMqttThread(int type, String data) {

        synchronized (MqttThread.this) {

            if (type == -1) {

                killCode = 1;

                return;
            }

            switch (type) {

                case 0: {
                    //arm

                    this.arm = data;

                    break;
                }

                case 1: {
                    //mode

                    this.mode = data;

                    break;
                }

                case 2: {
                    //roll

                    this.pitch = data;

                    break;
                }

                case 3: {
                    //pitch

                    this.roll = data;

                    break;
                }

                case 4: {
                    //yaw

                    this.yaw = data;

                    break;
                }

                case 5: {
                    //throttle

                    this.throttle = data;

                    break;
                }

            }

        }

    }

    public MqttThread(MqttNalda mqttNalda){

        this.killCode = 0;
        this.arm = "00";
        this.mode = "00";
        this.roll = "50";
        this.pitch = "50";
        this.yaw = "50";
        this.throttle = "00";

        this.mqttNalda = mqttNalda;

        Log.i("PUB","START");
    }

    public MainActivity.IMqttThread getIMqttThread(){

        return MqttThread.this;

    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.i("PUB","MAIN");

        while(true){

            try {

                synchronized (MqttThread.this) {

                    if (killCode == 1) {

                        break;
                    }

                    if(isCancelled()) {

                        break;

                    }

                    String payload = this.arm+this.mode+this.roll+this.pitch+this.yaw+this.throttle;
                    this.mqttNalda.setMqttPublishControl(payload);

                }

                Thread.sleep(10);

            }catch (Exception e){


            }

        }

        return null;

    }
}
