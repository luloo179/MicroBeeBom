package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.fly_fragment;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Call;
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

    private int mode;
    private Math math;
    private int armedState;
    private int connectionState;
    private float rollPitchMaxAngle = 10.0f;
    private static int timeOutput = 5;

    private IFlay2Fragment iFlay2Fragment;
    private ImageButton armedButton;
    private TextView connectionStateTextview;

    private TextView altTextview;
    private ThrottleSeekbar throttleSeekbar;
    private CallbackLoopThread callbackLoopThread;
    private RadioButton modeRadioButton[] = new RadioButton[3];

    private MjpegView quadStreamingMjpegView;

    public interface IFlay2Fragment {

        void iFly2Fragment(int type, byte data);

    }

    public static Fly2Fragment newInstance(int connectionState, IFlay2Fragment iFlay2Fragment) {

        Fly2Fragment fly2Fragment = new Fly2Fragment();
        fly2Fragment.setFly2Fragment(connectionState, iFlay2Fragment);

        return fly2Fragment;

    }

    public void setFly2Fragment(int connectionState, IFlay2Fragment iFlay2Fragment) {

        this.connectionState = connectionState;
        this.iFlay2Fragment = iFlay2Fragment;

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
        this.throttleSeekbar.setMax(250);
        this.throttleSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        this.modeRadioButton[0] = (RadioButton) view.findViewById(R.id.fragment_fly2_mode_angle_radiobutton);
        this.modeRadioButton[1] = (RadioButton) view.findViewById(R.id.fragment_fly2_mode_alt_radiobutton);
        this.modeRadioButton[2] = (RadioButton) view.findViewById(R.id.fragment_fly2_mode_pos_radiobutton);
        RadioGroup modeRadioGroup = (RadioGroup) view.findViewById(R.id.fragment_fly2_mode_radiogroup);
        modeRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        modeRadioGroup.check(R.id.fragment_fly2_mode_angle_radiobutton);
        this.mode = 0;

        this.callbackLoopThread = new CallbackLoopThread(handler);
        this.callbackLoopThread.execute();

        this.quadStreamingMjpegView = (MjpegView) view.findViewById(R.id.fragment_fly2_mjpegview);

        return view;

    }

    public void updateConnectionState(int connectionState) {


    }

    public void updateAltitude(float altitude) {

        if (altTextview != null) {

            altTextview.setText(Float.toString(altitude) + " M");

        }

    }

    public void updateBattery(int battery) {


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {

                if (iFlay2Fragment != null) {

                    iFlay2Fragment.iFly2Fragment(0, (byte) 0);

                }

            }

        }
    };

    public class CallbackLoopThread extends AsyncTask<Void, Void, Void> {

        private Handler handler;

        public CallbackLoopThread(Handler handler) {

            this.handler = handler;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            while (true) {

                try {

                    if (isCancelled()) {

                        break;

                    }

                    if (handler != null) {

                        handler.sendEmptyMessage(0);

                    }

                    Thread.sleep(15);

                } catch (Exception e) {

                    return null;

                }

            }

            return null;

        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.fragment_fly2_back_button_imagebutton: {

                    if (iFlay2Fragment != null) {

                        if (callbackLoopThread != null) {

                            callbackLoopThread.cancel(true);

                        }

                        iFlay2Fragment.iFly2Fragment(-1, (byte) 0);

                    }

                    break;

                }

                case R.id.fragment_fly2_armed_button_imagebutton: {

                    if (armedState == 0) {

                        if (iFlay2Fragment != null) {

                            Log.i("ARM", "DIS");

                            armedState = 1;
                            armedButton.setImageResource(R.drawable.ic_lock_outline_white_18dp);

                            if (throttleSeekbar != null) {

                                throttleSeekbar.setProgress(0);

                            }

                            iFlay2Fragment.iFly2Fragment(1, (byte) 1);

                        }

                        return;

                    }

                    if (armedState == 1) {

                        if (iFlay2Fragment != null) {

                            Log.i("ARM", "ARM");

                            armedState = 0;
                            iFlay2Fragment.iFly2Fragment(1, (byte) 0);
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

                if (iFlay2Fragment != null) {

                    iFlay2Fragment.iFly2Fragment(3, (byte) 0);
                    iFlay2Fragment.iFly2Fragment(4, (byte) 0);

                }

            } else if (math.abs(angle) < 40) {
                //front
                Log.i("ROLlPITCH", "FRONT");

                if (iFlay2Fragment != null) {

                    int speedAngle = -(int) ((power / 100.0f) * rollPitchMaxAngle);

                    if (speedAngle > 30) {

                        speedAngle = 30;

                    } else if (speedAngle < -30) {

                        speedAngle = -30;

                    }

                    iFlay2Fragment.iFly2Fragment(3, (byte) 0);
                    iFlay2Fragment.iFly2Fragment(4, (byte) speedAngle);

                }

            } else if (angle > 50 && angle < 130) {
                //right
                Log.i("ROLlPITCH", "RIGHT");

                if (iFlay2Fragment != null) {

                    int speedAngle = (int) ((power / 100.0f) * rollPitchMaxAngle);

                    if (speedAngle > 30) {

                        speedAngle = 30;

                    } else if (speedAngle < -30) {

                        speedAngle = -30;

                    }

                    iFlay2Fragment.iFly2Fragment(3, (byte) speedAngle);
                    iFlay2Fragment.iFly2Fragment(4, (byte) 0);

                }

            } else if (angle < -50 && angle > -130) {
                //left
                Log.i("ROLlPITCH", "LEFT");

                if (iFlay2Fragment != null) {

                    int speedAngle = -(int) ((power / 100.0f) * rollPitchMaxAngle);

                    if (speedAngle > 30) {

                        speedAngle = 30;

                    } else if (speedAngle < -30) {

                        speedAngle = -30;

                    }

                    iFlay2Fragment.iFly2Fragment(3, (byte) speedAngle);
                    iFlay2Fragment.iFly2Fragment(4, (byte) 0);

                }


            } else if (math.abs(angle) > 140) {
                //back
                Log.i("ROLlPITCH", "BACK");

                if (iFlay2Fragment != null) {

                    int speedAngle = (int) ((power / 100.0f) * rollPitchMaxAngle);

                    if (speedAngle > 30) {

                        speedAngle = 30;

                    } else if (speedAngle < -30) {

                        speedAngle = -30;

                    }

                    iFlay2Fragment.iFly2Fragment(3, (byte) 0);
                    iFlay2Fragment.iFly2Fragment(4, (byte) speedAngle);

                }

            } else {
                //zero
                Log.i("ROLlPITCH", "ZERO");

                if (iFlay2Fragment != null) {

                    iFlay2Fragment.iFly2Fragment(3, (byte) 0);
                    iFlay2Fragment.iFly2Fragment(4, (byte) 0);

                }
            }

        }
    };

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (iFlay2Fragment != null) {

                if (progress > 250) {

                    progress = 250;

                }

                iFlay2Fragment.iFly2Fragment(6, (byte) progress);

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

                    if (iFlay2Fragment != null) {

                        iFlay2Fragment.iFly2Fragment(2, (byte) 0);

                    }

                    break;
                }

                case R.id.fragment_fly2_mode_alt_radiobutton: {

                    mode = 1;
                    modeRadioButton[0].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);
                    modeRadioButton[1].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_checked_white_24dp);
                    modeRadioButton[2].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);

                    if (iFlay2Fragment != null) {

                        iFlay2Fragment.iFly2Fragment(2, (byte) 1);

                    }

                    break;
                }

                case R.id.fragment_fly2_mode_pos_radiobutton: {

                    mode = 2;
                    modeRadioButton[0].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);
                    modeRadioButton[1].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_unchecked_white_24dp);
                    modeRadioButton[2].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_radio_button_checked_white_24dp);

                    if (iFlay2Fragment != null) {

                        iFlay2Fragment.iFly2Fragment(2, (byte) 2);

                    }

                    break;
                }

            }

        }
    };

    private void loadIpCam() {
        Mjpeg.newInstance()
                .credential("", "")
                .open("http://192.168.42.1:9000/?action=stream", 5)
                .subscribe(inputStream -> {
                            quadStreamingMjpegView.setSource(inputStream);
                            quadStreamingMjpegView.setDisplayMode(DisplayMode.FULLSCREEN);
                            quadStreamingMjpegView.showFps(true);
                        },
                        throwable -> {
                            Log.e(getClass().getSimpleName(), "mjpeg error", throwable);
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadIpCam();

    }

    @Override
    public void onPause() {
        super.onPause();

        if (callbackLoopThread != null) {

            callbackLoopThread.cancel(true);

            if (iFlay2Fragment != null) {

                for (int i = 0; i > 7; i++) {

                    iFlay2Fragment.iFly2Fragment(i, (byte) 0);

                }

            }

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (callbackLoopThread != null) {

            callbackLoopThread.cancel(true);

            if (iFlay2Fragment != null) {

                for (int i = 0; i > 7; i++) {

                    iFlay2Fragment.iFly2Fragment(i, (byte) 0);

                }

            }

        }

    }
}
