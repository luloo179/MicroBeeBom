package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.main_fragment;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.MainActivity;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.network.MqttNalda;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private IMainFragment iMainFragment;
    private VideoView backGroundVideoview;
    private TextView connectionTextview;
    private Button connectButton;
    private int mqttConnectionState;

    public interface IMainFragment {

        void iMainFragment(int type, Handler handler);

    }

    public static MainFragment newInstance(int mqttConnectionState, IMainFragment iMainFragment) {

        MainFragment mainFragment = new MainFragment();
        mainFragment.setMainFragment(mqttConnectionState, iMainFragment);

        return mainFragment;

    }

    public void setMainFragment(int mqttConnectionState, IMainFragment iMainFragment) {

        this.mqttConnectionState = mqttConnectionState;
        this.iMainFragment = iMainFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.backGroundVideoview = (VideoView) view.findViewById(R.id.fragment_main_background_videoview);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.solo_background;
        this.backGroundVideoview.setVideoURI(Uri.parse(path));
        this.backGroundVideoview.setOnPreparedListener(onPreparedListener);
        this.backGroundVideoview.start();

        this.connectButton = (Button) view.findViewById(R.id.fragment_main_connection_button);
        this.connectionTextview = (TextView) view.findViewById(R.id.fragment_main_connection_state_textview);

        if(mqttConnectionState == 0){

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#FFFFFF"));
                connectionTextview.setText("Not Connected");

            }

            if(connectButton != null) {

                connectButton.setText("Local Connect");

            }

        }

        if (mqttConnectionState == 1) {

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#1DDB16"));
                connectionTextview.setText("Connected");

            }

            if(connectButton != null) {

                connectButton.setText("Local Disconnect");

            }

        }

        if(mqttConnectionState == 2){

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#FFFFFF"));
                connectionTextview.setText("Not Connected");

            }

            if(connectButton != null) {

                connectButton.setText("Global Connect");

            }

        }

        if (mqttConnectionState == 3) {

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#1DDB16"));
                connectionTextview.setText("Connected");

            }

            if(connectButton != null) {

                connectButton.setText("Global Disconnect");

            }

        }

        if(mqttConnectionState == 4){

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#1DDB16"));
                connectionTextview.setText("Connection Lost");

            }

            if(connectButton != null) {

                connectButton.setText("Disconnect");

            }

        }

        this.connectButton.setOnClickListener(mainFragmentOnClickListener);

        Button flyButton = (Button) view.findViewById(R.id.fragment_main_fly_button);
        flyButton.setOnClickListener(mainFragmentOnClickListener);

        Button settingButton = (Button) view.findViewById(R.id.fragment_main_setting_button);
        settingButton.setOnClickListener(mainFragmentOnClickListener);

        Button connectUsButton = (Button) view.findViewById(R.id.fragment_main_connect_us_button);
        connectUsButton.setOnClickListener(mainFragmentOnClickListener);

        return view;

    }

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

            mp.setLooping(true);

        }
    };

    public void updateConnectionButton(int mqttConnectionState){

        if(mqttConnectionState == 0){

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#FFFFFF"));
                connectionTextview.setText("Not Connected");

            }

            if(connectButton != null) {

                connectButton.setText("Local Connect");

            }

        }

        if (mqttConnectionState == 1) {

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#1DDB16"));
                connectionTextview.setText("Connected");

            }

            if(connectButton != null) {

                connectButton.setText("Local Disconnect");

            }

        }

        if(mqttConnectionState == 2){

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#FFFFFF"));
                connectionTextview.setText("Not Connected");

            }

            if(connectButton != null) {

                connectButton.setText("Global Connect");

            }

        }

        if (mqttConnectionState == 3) {

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#1DDB16"));
                connectionTextview.setText("Connected");

            }

            if(connectButton != null) {

                connectButton.setText("Global Disconnect");

            }

        }

        if(mqttConnectionState == 4){

            if(connectionTextview != null) {

                connectionTextview.setTextColor(Color.parseColor("#1DDB16"));
                connectionTextview.setText("Connection Lost");

            }

            if(connectButton != null) {

                connectButton.setText("Disconnect");

            }

        }

    }

    private View.OnClickListener mainFragmentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.fragment_main_connection_button: {

                    if(mqttConnectionState == 0 || mqttConnectionState == 1){

                        WifiManager wifiManager = (WifiManager)getActivity().getSystemService(getContext().WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        String ssid = wifiInfo.getSSID();

                        if (ssid != null) {

                            if (ssid.contains("Nalda_")) {

                                if (iMainFragment != null) {

                                    iMainFragment.iMainFragment(0, null);

                                    return;

                                }

                            } else {

                                Toast.makeText(getActivity(), "WiFi Error Check WiFi", Toast.LENGTH_SHORT).show();

                            }

                        } else {

                            Toast.makeText(getActivity(), "WiFi Error Check WiFi", Toast.LENGTH_SHORT).show();

                        }

                    }else{

                        if (iMainFragment != null) {

                            iMainFragment.iMainFragment(0, null);

                            return;

                        }

                    }

                    break;
                }

                case R.id.fragment_main_fly_button: {

                    if (iMainFragment != null) {

                        iMainFragment.iMainFragment(1, null);

                    }

                    break;
                }

                case R.id.fragment_main_setting_button: {

                    if (iMainFragment != null) {

                        iMainFragment.iMainFragment(2, null);

                    }

                    break;
                }

                case R.id.fragment_main_connect_us_button: {

                    if (iMainFragment != null) {

                        iMainFragment.iMainFragment(3, null);

                    }

                    break;
                }

            }

        }
    };

}
