package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.network;

import org.eclipse.paho.android.service.MqttAndroidClient;

/**
 * Created by jangsangjin on 2017. 2. 26..
 */

public class MqttConnection {

    private static int connectionState;
    private static MqttAndroidClient client;

    public static MqttAndroidClient getClient(){

        return client;

    }

    public static void setClient(MqttAndroidClient mqttAndroidClient){

        client = mqttAndroidClient;

    }

    public static int getConnectionState(){

        return connectionState;

    }

    public static void setConnectionState(int state){

        connectionState = state;

    }

}
