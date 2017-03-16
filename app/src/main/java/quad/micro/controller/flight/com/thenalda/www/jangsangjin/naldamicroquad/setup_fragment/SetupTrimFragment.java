package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetupTrimFragment extends Fragment {

    private int type;
    private int rollTrim, pitchTrim;
    private ISetupTrimFragment iSetupTrimFragment;
    private TextView rollTrimTextview, pitchTrimTextview;
    private SeekBar rollTrimSeekbar, pitchTrimSeekbar;

    private Handler setupTrimHandler = new Handler() {
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

                    if (Integer.parseInt(order1) == rollTrim && Integer.parseInt(order2) == pitchTrim) {

                        Toast.makeText(getActivity(), "Save Success", Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();

                        editor.putInt("ROLL_TRIM", rollTrim);
                        editor.putInt("PITCH_TRIM", pitchTrim);
                        editor.commit();

                    } else {

                        Toast.makeText(getActivity(), "Save Error", Toast.LENGTH_SHORT).show();

                    }

                }

            }

        }
    };

    public interface ISetupTrimFragment {

        void iSetupTrimFragment(int type, String order1, String order2);

    }

    public static SetupTrimFragment newInstance(int type, ISetupTrimFragment iSetupTrimFragment) {

        SetupTrimFragment setupTrimFragment = new SetupTrimFragment();
        setupTrimFragment.setTrimFragment(type, iSetupTrimFragment);

        return setupTrimFragment;

    }

    public void setTrimFragment(int type, ISetupTrimFragment iSetupTrimFragment) {

        this.type = type;
        this.iSetupTrimFragment = iSetupTrimFragment;

    }

    public Handler getSetupTrimHandler() {

        return setupTrimHandler;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setup_trim, container, false);

        ImageButton backButton = (ImageButton) view.findViewById(R.id.fragment_setup_trim_backbutton);
        backButton.setOnClickListener(onClickListener);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        this.rollTrim = sharedPref.getInt("ROLL_TRIM", 100);
        this.pitchTrim = sharedPref.getInt("PITCH_TRIM", 100);

        this.rollTrimTextview = (TextView) view.findViewById(R.id.fragment_setup_trim_roll_textview);
        this.rollTrimSeekbar = (SeekBar) view.findViewById(R.id.fragment_setup_trim_roll_seekbar);
        this.rollTrimSeekbar.setMax(200);
        this.rollTrimSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        this.rollTrimSeekbar.setProgress(this.rollTrim);

        this.pitchTrimTextview = (TextView) view.findViewById(R.id.fragment_setup_trim_pitch_textview);
        this.pitchTrimSeekbar = (SeekBar) view.findViewById(R.id.fragment_setup_trim_pitch_seekbar);
        this.pitchTrimSeekbar.setMax(200);
        this.pitchTrimSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        this.pitchTrimSeekbar.setProgress(this.pitchTrim);

        ImageButton rollPluse = (ImageButton) view.findViewById(R.id.fragment_setup_trim_roll_plus_imagebutton);
        rollPluse.setOnClickListener(onClickListener);
        ImageButton rollMinus = (ImageButton) view.findViewById(R.id.fragment_setup_trim_roll_minus_imagebutton);
        rollMinus.setOnClickListener(onClickListener);

        ImageButton pitchPlus = (ImageButton) view.findViewById(R.id.fragment_setup_trim_pitch_plus_imagebutton);
        pitchPlus.setOnClickListener(onClickListener);
        ImageButton pitchMinus = (ImageButton) view.findViewById(R.id.fragment_setup_trim_pitch_minus_imagebutton);
        pitchMinus.setOnClickListener(onClickListener);

        Button saveButton = (Button) view.findViewById(R.id.fragment_setup_trim_save_button);
        saveButton.setOnClickListener(onClickListener);

        return view;

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.fragment_setup_trim_backbutton: {

                    if (iSetupTrimFragment != null) {

                        iSetupTrimFragment.iSetupTrimFragment(0, "NULL", "NULL");

                    }

                    break;

                }

                case R.id.fragment_setup_trim_roll_minus_imagebutton: {

                    rollTrimSeekbar.setProgress(rollTrim - 1);

                    break;

                }

                case R.id.fragment_setup_trim_roll_plus_imagebutton: {

                    rollTrimSeekbar.setProgress(rollTrim + 1);

                    break;

                }

                case R.id.fragment_setup_trim_pitch_minus_imagebutton: {

                    pitchTrimSeekbar.setProgress(pitchTrim - 1);

                    break;

                }

                case R.id.fragment_setup_trim_pitch_plus_imagebutton: {

                    pitchTrimSeekbar.setProgress(pitchTrim + 1);

                    break;

                }

                case R.id.fragment_setup_trim_save_button: {

                    if (iSetupTrimFragment != null) {

                        String order1 = "";
                        String order2 = "";

                        if (rollTrim < 10) {

                            order1 = "00";
                            order1 += Integer.toString(rollTrim);

                        } else if (rollTrim < 100) {

                            order1 = "0";
                            order1 += Integer.toString(rollTrim);

                        } else {

                            order1 += Integer.toString(rollTrim);

                        }

                        if (pitchTrim < 10) {

                            order2 = "00";
                            order2 += Integer.toString(pitchTrim);

                        } else if (pitchTrim < 100) {

                            order2 = "0";
                            order2 += Integer.toString(pitchTrim);

                        } else {

                            order2 += Integer.toString(pitchTrim);

                        }

                        iSetupTrimFragment.iSetupTrimFragment(type, order1, order2);

                    }

                }

            }

        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            switch (seekBar.getId()) {

                case R.id.fragment_setup_trim_roll_seekbar: {

                    rollTrim = progress;

                    rollTrimTextview.setText(Float.toString((progress - 100) / 10.0f));

                    break;
                }

                case R.id.fragment_setup_trim_pitch_seekbar: {

                    pitchTrim = progress;

                    pitchTrimTextview.setText(Float.toString((progress - 100) / 10.0f));

                    break;
                }

            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

}
