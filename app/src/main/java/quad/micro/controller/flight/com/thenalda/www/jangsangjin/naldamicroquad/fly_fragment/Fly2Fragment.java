package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.fly_fragment;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegView;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.MainActivity;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.joystick.RollPitchJoystick;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.joystick.ThrottleSeekbar;


public class Fly2Fragment extends android.support.v4.app.Fragment {

    private Math math;
    private TextView connectionStateTextview;
    private IFlay2Fragment iFlay2Fragment;
    private MainActivity.IMqttThread iMqttThread;
    private int connectionState;
    private int armedState;
    private ImageButton armedButton;
    private TextView altTextview;

    private float rollPitchMaxAngle = 10.0f;

    private ThrottleSeekbar throttleSeekbar;

    private int mode;
    private RadioButton modeRadioButton[] = new RadioButton[3];

    public interface IFlay2Fragment {

        void iFly2Fragment(int type, String data);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int type = msg.getData().getInt("TYPE", -1);

            if (type == 0) {
                //connection relate

            }

            if (type == 1) {
                //state

                String payload = msg.getData().getString("STATE", "NULL");

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(payload.charAt(0));
                stringBuilder.append(payload.charAt(1));
                stringBuilder.append(payload.charAt(2));
                String valueType = stringBuilder.toString();

                if (valueType.compareTo("001") == 0) {

                    int start = payload.indexOf('{');
                    int end = payload.indexOf('}');
                    String alt = payload.substring(start + 1, end);

                    if (altTextview != null) {

                        altTextview.setText(Float.parseFloat(alt) / 10.0f + " M");

                    }

                }

            }

        }
    };

    public static Fly2Fragment newInstance(int connectionState, IFlay2Fragment iFlay2Fragment, MainActivity.IMqttThread iMqttThread) {

        Fly2Fragment fly2Fragment = new Fly2Fragment();
        fly2Fragment.setFly2Fragment(connectionState, iFlay2Fragment, iMqttThread);

        return fly2Fragment;

    }

    public void setFly2Fragment(int connectionState, IFlay2Fragment iFlay2Fragment, MainActivity.IMqttThread iMqttThread) {

        this.connectionState = connectionState;
        this.iFlay2Fragment = iFlay2Fragment;
        this.iMqttThread = iMqttThread;

    }

    public Handler getFly2FragmentHandler() {

        return handler;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fly2, container, false);

        ImageButton backButton = (ImageButton) view.findViewById(R.id.fragment_fly2_back_button_imagebutton);
        backButton.setOnClickListener(onClickListener);

        this.connectionStateTextview = (TextView) view.findViewById(R.id.fragment_fly2_connection_state_textview);

        if (connectionState == 0) {

            this.connectionStateTextview.setTextColor(Color.parseColor("#FFFFFF"));
            this.connectionStateTextview.setText("Not Connected");

        } else {

            this.connectionStateTextview.setTextColor(Color.parseColor("#1DDB16"));
            this.connectionStateTextview.setText("Connected");

        }

        this.armedState = 0;
        this.armedButton = (ImageButton) view.findViewById(R.id.fragment_fly2_armed_button_imagebutton);
        this.armedButton.setOnClickListener(onClickListener);

        this.altTextview = (TextView) view.findViewById(R.id.fragment_fly2_altitude_textview);
        this.altTextview.setText("0 M");

        RollPitchJoystick rollPitchJoystick = (RollPitchJoystick) view.findViewById(R.id.fragment_fly2_roll_pitch_joystick);
        rollPitchJoystick.setOnJoystickMoveListener(onJoystickMoveListener, 20);

        this.throttleSeekbar = (ThrottleSeekbar) view.findViewById(R.id.fragment_fly2_throttle_seekbar);
        this.throttleSeekbar.setMax(99);
        this.throttleSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        this.modeRadioButton[0] = (RadioButton) view.findViewById(R.id.fragment_fly2_mode_angle_radiobutton);
        this.modeRadioButton[1] = (RadioButton) view.findViewById(R.id.fragment_fly2_mode_alt_radiobutton);
        this.modeRadioButton[2] = (RadioButton) view.findViewById(R.id.fragment_fly2_mode_pos_radiobutton);
        RadioGroup modeRadioGroup = (RadioGroup) view.findViewById(R.id.fragment_fly2_mode_radiogroup);
        modeRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        modeRadioGroup.check(R.id.fragment_fly2_mode_angle_radiobutton);
        mode = 0;

        return view;

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.fragment_fly2_back_button_imagebutton: {

                    if (iMqttThread != null) {

                        iMqttThread.iMqttThread(-1, "NULL");

                    }

                    if (iFlay2Fragment != null) {

                        iFlay2Fragment.iFly2Fragment(0, "NULL");

                    }

                }

                case R.id.fragment_fly2_armed_button_imagebutton: {

                    if (armedState == 0) {

                        if (iMqttThread != null) {

                            Log.i("ARM", "DIS");

                            armedState = 1;
                            iMqttThread.iMqttThread(0, "01");
                            armedButton.setImageResource(R.drawable.ic_lock_outline_white_18dp);

                            if (throttleSeekbar != null) {

                                throttleSeekbar.setProgress(0);

                            }

                        }

                        return;

                    }

                    if (armedState == 1) {

                        if (iMqttThread != null) {

                            Log.i("ARM", "ARM");

                            armedState = 0;
                            iMqttThread.iMqttThread(0, "00");
                            armedButton.setImageResource(R.drawable.ic_lock_open_white_18dp);

                            if (throttleSeekbar != null) {

                                throttleSeekbar.setProgress(0);

                            }

                        }

                        return;

                    }

                    break;

                }

            }

        }
    };

    RollPitchJoystick.OnJoystickMoveListener onJoystickMoveListener = new RollPitchJoystick.OnJoystickMoveListener() {
        @Override
        public void onValueChanged(int angle, int power, int direction) {

            if (angle == 0) {
                Log.i("ROLlPITCH", "ZERO");

                if (iMqttThread != null) {

                    iMqttThread.iMqttThread(2, "50");
                    iMqttThread.iMqttThread(3, "50");

                }

            } else if (math.abs(angle) < 40) {
                //front
                Log.i("ROLlPITCH", "FRONT");

                if (iMqttThread != null) {

                    String pitch = new String();
                    int speedAngle = -(int) ((power / 100.0f) * rollPitchMaxAngle) + 50;

                    if (speedAngle < 10) {

                        pitch = "0";
                        pitch += Integer.toString(speedAngle);

                    } else {

                        pitch = Integer.toString(speedAngle);

                    }

                    iMqttThread.iMqttThread(2, "50");
                    iMqttThread.iMqttThread(3, pitch);

                }

            } else if (angle > 50 && angle < 130) {
                //right
                Log.i("ROLlPITCH", "RIGHT");

                if (iMqttThread != null) {

                    String roll = new String();
                    int speedAngle = (int) ((power / 100.0f) * rollPitchMaxAngle) + 50;

                    if (speedAngle < 10) {

                        roll = "0";
                        roll += Integer.toString(speedAngle);

                    } else {

                        roll = Integer.toString(speedAngle);

                    }

                    iMqttThread.iMqttThread(2, roll);
                    iMqttThread.iMqttThread(3, "50");

                }

            } else if (angle < -50 && angle > -130) {
                //left
                Log.i("ROLlPITCH", "LEFT");

                if (iMqttThread != null) {

                    if (iMqttThread != null) {

                        String roll = new String();
                        int speedAngle = -(int) ((power / 100.0f) * rollPitchMaxAngle) + 50;

                        if (speedAngle < 10) {

                            roll = "0";
                            roll += Integer.toString(speedAngle);

                        } else {

                            roll = Integer.toString(speedAngle);

                        }

                        iMqttThread.iMqttThread(2, roll);
                        iMqttThread.iMqttThread(3, "50");

                    }

                }


            } else if (math.abs(angle) > 140) {
                //back
                Log.i("ROLlPITCH", "BACK");

                if (iMqttThread != null) {

                    String pitch = new String();
                    int speedAngle = (int) ((power / 100.0f) * rollPitchMaxAngle) + 50;

                    if (speedAngle < 10) {

                        pitch = "0";
                        pitch += Integer.toString(speedAngle);

                    } else {

                        pitch = Integer.toString(speedAngle);

                    }

                    iMqttThread.iMqttThread(2, "50");
                    iMqttThread.iMqttThread(3, pitch);

                }

            } else {
                //zero
                Log.i("ROLlPITCH", "ZERO");

                if (iMqttThread != null) {

                    iMqttThread.iMqttThread(2, "50");
                    iMqttThread.iMqttThread(3, "50");

                }

            }

        }
    };

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            String throttle = new String();

            if (progress < 10) {

                throttle = "0";
                throttle += Integer.toString(progress);

            } else {

                throttle = Integer.toString(progress);

            }

            if (iMqttThread != null) {

                iMqttThread.iMqttThread(5, throttle);

            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {

                case R.id.fragment_fly2_mode_angle_radiobutton: {

                    mode = 0;
                    modeRadioButton[0].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_checked_white_24dp);
                    modeRadioButton[1].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);
                    modeRadioButton[2].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);

                    if (iMqttThread != null) {

                        iMqttThread.iMqttThread(mode, "00");

                    }

                    break;
                }

                case R.id.fragment_fly2_mode_alt_radiobutton: {

                    mode = 1;
                    modeRadioButton[0].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);
                    modeRadioButton[1].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_checked_white_24dp);
                    modeRadioButton[2].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);

                    if (iMqttThread != null) {

                        iMqttThread.iMqttThread(mode, "01");

                    }

                    break;
                }

                case R.id.fragment_fly2_mode_pos_radiobutton: {

                    mode = 2;
                    modeRadioButton[0].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);
                    modeRadioButton[1].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);
                    modeRadioButton[2].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_checked_white_24dp);

                    if (iMqttThread != null) {

                        iMqttThread.iMqttThread(mode, "02");

                    }

                    break;
                }

            }

        }
    };

    private DisplayMode calculateDisplayMode() {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE ?
                DisplayMode.FULLSCREEN : DisplayMode.BEST_FIT;
    }

    private void loadIpCam() {
        Mjpeg.newInstance()
                .open("http://192.168.42.1:9000?action=stream", 5)
                .subscribe();


    }

}
