package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.network;

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.MainActivity;

/**
 * Created by jangsangjin on 2017. 2. 22..
 */

public class MqttThread extends AsyncTask<Void, Void, Void> implements MainActivity.IMqttThread {

    private int killCode;
    private MqttNalda mqttNalda;
    private MqttAndroidClient mqttAndroidClient;
    private String arm, mode, roll, pitch, yaw, throttle;
    private byte[] controlData = new byte[7];

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

    public MqttThread(MqttAndroidClient mqttAndroidClient) {

        this.killCode = 0;
        this.arm = "00";
        this.mode = "00";
        this.roll = "50";
        this.pitch = "50";
        this.yaw = "50";
        this.throttle = "00";

        this.mqttNalda = mqttNalda;
        this.mqttAndroidClient = mqttAndroidClient;

        Log.i("PUB", "START");
    }

    public MainActivity.IMqttThread getIMqttThread() {

        return MqttThread.this;

    }

    private boolean mqttPublish(byte[] payload) {

        String topic = "pilot/beebom";

        try {

            MqttMessage message = new MqttMessage(payload);
            message.setQos(0);

            if (this.mqttAndroidClient != null) {

                this.mqttAndroidClient.publish(topic, payload, 0, false);

                return true;

            }

            return false;

        } catch (MqttException e) {

            Log.i("ERROR", e.toString());

            return false;

        }

    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.i("PUB", "MAIN");

        while (true) {

            try {

                synchronized (MqttThread.this) {

                    if (killCode == 1) {

                        break;
                    }

                    if (isCancelled()) {

                        break;

                    }

                    String payload = this.arm + this.mode + this.roll + this.pitch + this.yaw + this.throttle;
                    this.mqttNalda.setMqttPublishControl(payload);

                    mqttPublish(controlData);

                }

                Thread.sleep(10);

            } catch (Exception e) {


            }

        }

        return null;

    }
}
