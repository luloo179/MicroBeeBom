package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetupBiasFragment extends Fragment {

    private int type;
    private ISetupBiasFragment iSetupBiasFragment;
    private float x, y, z;
    private TextView xTextview, yTextview, zTextview;

    public interface ISetupBiasFragment {

        void iSetupBiasFragment(int type);

    }

    private Handler setupBiasHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String payload = msg.getData().getString("SETUP", "NULL");

            if (payload.compareTo("NULL") != 0) {

                if (payload.length() == 9) {

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(payload.charAt(0));
                    stringBuilder.append(payload.charAt(1));
                    stringBuilder.append(payload.charAt(2));
                    String orderType = stringBuilder.toString();

                    stringBuilder = new StringBuilder();
                    stringBuilder.append(payload.charAt(3));
                    stringBuilder.append(payload.charAt(4));
                    stringBuilder.append(payload.charAt(5));
                    String order1 = stringBuilder.toString();

                    stringBuilder = new StringBuilder();
                    stringBuilder.append(payload.charAt(6));
                    stringBuilder.append(payload.charAt(7));
                    stringBuilder.append(payload.charAt(8));
                    String order2 = stringBuilder.toString();

                    Log.i("SETUP", orderType + "," + order1 + "," + order2);

                    if (Integer.parseInt(order1) == 0) {
                        //x

                        int value = (Integer.parseInt(order2) - 500);
                        xTextview.setText(Float.toString(value / 10.0f));

                    }

                    if (Integer.parseInt(order1) == 1) {

                        int value = Integer.parseInt(order2) - 500;
                        yTextview.setText(Float.toString(value / 10.0f));

                    }

                    if (Integer.parseInt(order1) == 2) {

                        int value = Integer.parseInt(order2) - 500;
                        zTextview.setText(Float.toString(value / 10.0f));

                    }

                    if (Integer.parseInt(order1) == 3) {

                        int value = Integer.parseInt(order2) - 500;
                        xTextview.setText(Float.toString(value / 10.0f));

                        if (type == 10) {

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putFloat("ACE_X", value / 10.0f);
                            editor.commit();

                        }

                        if (type == 11) {

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putFloat("GYRO_X", value / 10.0f);
                            editor.commit();

                        }

                        if (type == 12) {

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putFloat("MAG_X", value / 10.0f);
                            editor.commit();

                        }

                    }

                    if (Integer.parseInt(order1) == 4) {

                        int value = Integer.parseInt(order2) - 500;
                        yTextview.setText(Float.toString(value / 10.0f));

                        if (type == 10) {

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putFloat("ACE_Y", value / 10.0f);
                            editor.commit();

                        }

                        if (type == 11) {

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putFloat("GYRO_Y", value / 10.0f);
                            editor.commit();

                        }

                        if (type == 12) {

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putFloat("MAG_Y", value / 10.0f);
                            editor.commit();

                        }

                    }

                    if (Integer.parseInt(order1) == 5) {

                        int value = Integer.parseInt(order2) - 500;
                        zTextview.setText(Float.toString(value / 10.0f));

                        if (type == 10) {

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putFloat("ACE_Z", value / 10.0f);
                            editor.commit();

                        }

                        if (type == 11) {

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putFloat("GYRO_Z", value / 10.0f);
                            editor.commit();

                        }

                        if (type == 12) {

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putFloat("MAG_Z", value / 10.0f);
                            editor.commit();

                        }

                    }

                }

            }

        }
    };

    public static SetupBiasFragment newInstance(int type, ISetupBiasFragment iSetupBiasFragment) {

        SetupBiasFragment setupBiasFragment = new SetupBiasFragment();
        setupBiasFragment.setSetupBiasFragment(type, iSetupBiasFragment);

        return setupBiasFragment;

    }

    public void setSetupBiasFragment(int type, ISetupBiasFragment iSetupBiasFragment) {

        this.type = type;
        this.iSetupBiasFragment = iSetupBiasFragment;

    }

    public Handler getSetupBiasHandler() {

        return setupBiasHandler;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setup_bias, container, false);

        TextView titleTextveiw = (TextView) view.findViewById(R.id.fragment_setup_bias_title_textview);

        if (this.type == 10) {

            titleTextveiw.setText("Acceleration Calibration Settings");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            this.x = sharedPref.getFloat("ACE_X", 0.0f);
            this.y = sharedPref.getFloat("ACE_Y", 0.0f);
            this.z = sharedPref.getFloat("ACE_Z", 1.0f);

        } else if (this.type == 11) {

            titleTextveiw.setText("Gyroscope Calibration Settings");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            this.x = sharedPref.getFloat("GYRO_X", 0.0f);
            this.y = sharedPref.getFloat("GYRO_Y", 0.0f);
            this.z = sharedPref.getFloat("GYRO_Z", 0.0f);


        } else if (type == 12) {

            titleTextveiw.setText("Magnetic Calibration Settings");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            this.x = sharedPref.getFloat("MAG_X", 0.0f);
            this.y = sharedPref.getFloat("MAG_Y", 0.0f);
            this.z = sharedPref.getFloat("MAG_Z", 0.0f);

        }

        ImageButton backButton = (ImageButton) view.findViewById(R.id.fragment_setup_bais_backbutton);
        backButton.setOnClickListener(onClickListener);

        this.xTextview = (TextView) view.findViewById(R.id.fragment_setup_bias_x_textview);
        this.xTextview.setText(Float.toString(this.x));

        this.yTextview = (TextView) view.findViewById(R.id.fragment_setup_bias_y_textview);
        this.yTextview.setText(Float.toString(this.y));

        this.zTextview = (TextView) view.findViewById(R.id.fragment_setup_bias_z_textview);
        this.zTextview.setText(Float.toString(this.z));

        Button calibButton = (Button) view.findViewById(R.id.fragment_setup_bias_calib_button);
        calibButton.setOnClickListener(onClickListener);

        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.fragment_setup_bais_backbutton: {

                    if (iSetupBiasFragment != null) {

                        iSetupBiasFragment.iSetupBiasFragment(0);

                    }

                    break;

                }

                case R.id.fragment_setup_bias_calib_button: {

                    if (iSetupBiasFragment != null) {

                        iSetupBiasFragment.iSetupBiasFragment(type);

                    }

                }

            }

        }
    };

}
