package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.network;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;

/**
 * Created by jangsangjin on 2017. 2. 19..
 */

public class MqttNalda implements MqttCallback {

    private Context context;
    private Handler mainActivityHandler;

    public MqttNalda(Context context) {

        this.context = context;

    }

    public void setMainActivityHandler(Handler handler) {

        this.mainActivityHandler = handler;

    }

    public void createConnection(int type) {

        if (type == 0) {
            //local connection

            if(MqttConnection.getClient() != null){

                return;

            }

            String clientId = MqttClient.generateClientId();
            MqttConnection.setClient(new MqttAndroidClient(context, "tcp://192.168.42.1:1883", clientId));

            try {

                IMqttToken token = MqttConnection.getClient().connect();
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                        if (mainActivityHandler != null) {

                            MqttConnection.setConnectionState(1);

                            Message message = new Message();

                            Bundle bundle = new Bundle();
                            bundle.putInt("TYPE", 0);
                            bundle.putInt("CONN", 1);
                            message.setData(bundle);

                            mainActivityHandler.sendMessage(message);

                        }

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                        if (mainActivityHandler != null) {

                            MqttConnection.setConnectionState(0);

                            Message message = new Message();

                            Bundle bundle = new Bundle();
                            bundle.putInt("TYPE", 0);
                            bundle.putInt("CONN", 0);
                            message.setData(bundle);

                            mainActivityHandler.sendMessage(message);

                        }

                        Log.i("MQTT", exception.toString());

                    }
                });

            } catch (MqttException e) {

                e.printStackTrace();

            }

        }

        if (type == 1) {
            //global connection

            String clientId = MqttClient.generateClientId();
            MqttConnection.setClient(new MqttAndroidClient(context, "tcp://192.168.42.1:1883", clientId));

            try {

                IMqttToken token = MqttConnection.getClient().connect();
                token.setActionCallback(new IMqttActionListener() {

                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                        if (mainActivityHandler != null) {

                            MqttConnection.setConnectionState(1);

                            Message message = new Message();

                            Bundle bundle = new Bundle();
                            bundle.putInt("TYPE", 0);
                            bundle.putInt("CONN", 1);
                            message.setData(bundle);

                            mainActivityHandler.sendMessage(message);

                        }

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                        if (mainActivityHandler != null) {

                            MqttConnection.setConnectionState(0);

                            Message message = new Message();

                            Bundle bundle = new Bundle();
                            bundle.putInt("TYPE", 0);
                            bundle.putInt("CONN", 0);
                            message.setData(bundle);

                            mainActivityHandler.sendMessage(message);

                        }

                        Log.i("MQTT", exception.toString());

                    }
                });

            } catch (MqttException e) {

                e.printStackTrace();

            }

        }

    }

    public void disConnect() {

        try {

            IMqttToken disconToken = MqttConnection.getClient().disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    if (mainActivityHandler != null) {

                        MqttConnection.setClient(null);
                        MqttConnection.setConnectionState(0);

                        Message message = new Message();

                        Bundle bundle = new Bundle();
                        bundle.putInt("TYPE", 0);
                        bundle.putInt("CONN", 0);
                        message.setData(bundle);

                        mainActivityHandler.sendMessage(message);

                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void setMqttPublishControl(String payloadData) {

        String topic = "pilot/beebom";
        String payload = payloadData;

        byte[] encodedPayload = new byte[8];

        try {

            //encodedPayload = payload.getBytes("UTF-8");
            encodedPayload[0] = 0x00;
            encodedPayload[1] = 0x00;
            encodedPayload[2] = 0x3F;
            encodedPayload[3] = -20;
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);
            MqttConnection.getClient().publish(topic, message);

        } catch (MqttException e) {

            e.printStackTrace();

        }

    }

    public void setMqttPublishSetting(String payloadData) {

        String topic = "pilot/beebom";
        String payload = payloadData;

        byte[] encodedPayload = new byte[0];

        try {

            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);
            MqttConnection.getClient().publish(topic, message);

        } catch (UnsupportedEncodingException | MqttException e) {

            Log.i("ERROR", e.toString());

        }

    }

    public void setMqttSub() {

        int qos = 0;
        String topic = "n/b/f/v/+";

        try {

            IMqttToken subToken = MqttConnection.getClient().subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    if (asyncActionToken == null) {

                        Log.i("GET", "NULL");

                    } else {


                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    Log.i("GET", exception.toString());

                }
            });

        } catch (MqttException e) {

            Log.i("GET", e.toString());

        }

        MqttConnection.getClient().setCallback(MqttNalda.this);

    }

    @Override
    public void connectionLost(Throwable cause) {

        Log.i("CONNECTION", "FALSE");

        if (cause != null) {

            if (mainActivityHandler != null) {

                MqttConnection.setConnectionState(0);

                Message message = new Message();

                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", 0);
                bundle.putInt("CONN", 0);
                message.setData(bundle);

                mainActivityHandler.sendMessage(message);

            }

        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        String payload = new String(message.getPayload());

        Log.i("GAINRAW",payload);

        if (mainActivityHandler != null) {

            if(topic.compareTo("n/b/f/v/v") == 0) {

                Message messag = new Message();

                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", 1);
                bundle.putString("STATE", payload);
                messag.setData(bundle);

                mainActivityHandler.sendMessage(messag);

            }

            if(topic.compareTo("n/b/f/v/s") == 0){

                Message messag = new Message();

                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", 1);
                bundle.putString("SETUP", payload);
                messag.setData(bundle);

                mainActivityHandler.sendMessage(messag);

            }

        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}
