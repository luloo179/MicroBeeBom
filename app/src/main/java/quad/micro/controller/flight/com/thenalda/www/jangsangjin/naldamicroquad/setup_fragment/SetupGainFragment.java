package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment;

import android.content.Intent;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.SplashActivity;


public class SetupGainFragment extends Fragment {

    private int type;
    private int gainA, gainB;
    private TextView gainATextview, gainBTextview;
    private SeekBar gainAseekbar, gainBseekbar;
    private ISetupGainFragment iSetupGainFragment;

    public interface ISetupGainFragment {

        void iSetupGainFragment(int type, String order1, String order2);

    }

    //check gain setting is correctly save
    private Handler setupGainHandler = new Handler() {
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

                    if (Integer.parseInt(orderType) == type - 3) {

                        if (Integer.parseInt(order1) == gainA && Integer.parseInt(order2) == gainB ) {

                            Toast.makeText(getActivity(), "Save Success", Toast.LENGTH_SHORT).show();

                            switch (type) {

                                case 4: {
                                    //roll gain

                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    editor.putInt("ROLL_GAIN_A", gainA);
                                    editor.putInt("ROLL_GAIN_B", gainB);
                                    editor.commit();

                                    break;
                                }

                                case 5: {
                                    //pitch gain

                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    editor.putInt("PITCH_GAIN_A", gainA);
                                    editor.putInt("PITCH_GAIN_B", gainB);
                                    editor.commit();

                                    break;
                                }

                                case 6: {
                                    //yaw gain

                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    editor.putInt("YAW_GAIN_A", gainA);
                                    editor.putInt("YAW_GAIN_B", gainB);
                                    editor.commit();

                                    break;
                                }

                                case 7: {
                                    //altidue gain

                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    editor.putInt("ALT_GAIN_A", gainA);
                                    editor.putInt("ALT_GAIN_B", gainB);
                                    editor.commit();

                                    break;
                                }

                                case 8: {
                                    //position gain

                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    editor.putInt("POS_GAIN_A", gainA);
                                    editor.putInt("POS_GAIN_B", gainB);
                                    editor.commit();

                                    break;
                                }

                            }

                        }

                    }


                } else {

                    if (getActivity() != null) {

                        Toast.makeText(getActivity(), "Save Error", Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(getActivity(), SplashActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }

                }

            } else {

                if (getActivity() != null) {

                    Toast.makeText(getActivity(), "Save Error", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(getActivity(), SplashActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }
            }

        }
    };

    public static SetupGainFragment newInstance(int type, ISetupGainFragment iSetupGainFragment) {

        SetupGainFragment setupGainFragment = new SetupGainFragment();
        setupGainFragment.setSetupGainFragment(type, iSetupGainFragment);

        return setupGainFragment;

    }

    public void setSetupGainFragment(int type, ISetupGainFragment iSetupGainFragment) {

        this.type = type;
        this.iSetupGainFragment = iSetupGainFragment;

    }

    public Handler getSetupGainHandler() {

        return setupGainHandler;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setup_gain, container, false);

        ImageButton backButton = (ImageButton) view.findViewById(R.id.fragment_setup_gain_backbutton);
        backButton.setOnClickListener(onClickListener);

        TextView gainTitleTextview = (TextView) view.findViewById(R.id.fragment_setup_gain_title_textview);

        switch (this.type) {

            case 4: {
                //roll gain

                gainTitleTextview.setText("Roll Gain Settings");

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                this.gainA = sharedPref.getInt("ROLL_GAIN_A", 30);
                this.gainB = sharedPref.getInt("ROLL_GAIN_B", 90);

                break;
            }

            case 5: {
                //pitch gain

                gainTitleTextview.setText("Pitch Gain Settings");

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                this.gainA = sharedPref.getInt("PITCH_GAIN_A", 30);
                this.gainB = sharedPref.getInt("PITCH_GAIN_B", 90);

                break;
            }

            case 6: {
                //yaw gain

                gainTitleTextview.setText("Yaw Gain Settings");

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                this.gainA = sharedPref.getInt("YAW_GAIN_A", 10);
                this.gainB = sharedPref.getInt("YAW_GAIN_B", 35);

                break;
            }

            case 7: {
                //altidue gain

                gainTitleTextview.setText("Altitude Gain Settings");


                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                this.gainA = sharedPref.getInt("ALT_GAIN_A", 45);
                this.gainB = sharedPref.getInt("ALT_GAIN_B", 90);

                break;
            }

            case 8: {
                //position gain

                gainTitleTextview.setText("Position Gain Settings");


                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                this.gainA = sharedPref.getInt("POS_GAIN_A", 30);
                this.gainB = sharedPref.getInt("POS_GAIN_B", 90);

                break;
            }

        }

        this.gainATextview = (TextView) view.findViewById(R.id.fragment_setup_gain_gain_a_textview);

        ImageButton gainAminusButton = (ImageButton) view.findViewById(R.id.fragment_setup_gain_gain_a_minus_imagebutton);
        gainAminusButton.setOnClickListener(onClickListener);
        ImageButton gainAPlusButton = (ImageButton) view.findViewById(R.id.fragment_setup_gain_gain_a_plus_imagebutton);
        gainAPlusButton.setOnClickListener(onClickListener);
        this.gainAseekbar = (SeekBar) view.findViewById(R.id.fragment_setup_gain_gain_a_seekbar);
        this.gainAseekbar.setMax(200);
        this.gainAseekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        this.gainAseekbar.setProgress(this.gainA);

        this.gainBTextview = (TextView) view.findViewById(R.id.fragment_setup_gain_gain_b_textview);

        ImageButton gainBminusButton = (ImageButton) view.findViewById(R.id.fragment_setup_gain_gain_b_minus_imagebutton);
        gainBminusButton.setOnClickListener(onClickListener);
        ImageButton gainBPlusButton = (ImageButton) view.findViewById(R.id.fragment_setup_gain_gain_b_plus_imagebutton);
        gainBPlusButton.setOnClickListener(onClickListener);
        this.gainBseekbar = (SeekBar) view.findViewById(R.id.fragment_setup_gain_gain_b_seekbar);
        this.gainBseekbar.setMax(400);
        this.gainBseekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        this.gainBseekbar.setProgress(this.gainB);

        Button saveButton = (Button) view.findViewById(R.id.fragment_setup_gain_save_button);
        saveButton.setOnClickListener(onClickListener);

        return view;

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.fragment_setup_gain_backbutton: {

                    if (iSetupGainFragment != null) {

                        iSetupGainFragment.iSetupGainFragment(0, "NULL", "NULL");

                    }

                    break;
                }

                case R.id.fragment_setup_gain_gain_a_minus_imagebutton: {

                    gainAseekbar.setProgress(gainA - 1);

                    break;
                }

                case R.id.fragment_setup_gain_gain_a_plus_imagebutton: {

                    gainAseekbar.setProgress(gainA + 1);

                    break;
                }

                case R.id.fragment_setup_gain_gain_b_minus_imagebutton: {

                    gainBseekbar.setProgress(gainB - 1);

                    break;
                }

                case R.id.fragment_setup_gain_gain_b_plus_imagebutton: {

                    gainBseekbar.setProgress(gainB + 1);

                    break;
                }

                case R.id.fragment_setup_gain_save_button: {

                    if (iSetupGainFragment != null) {

                        String strGainA = "";
                        String strGainB = "";

                        if (gainA < 10) {

                            strGainA = "00";
                            strGainA += Integer.toString(gainA);

                        } else if (gainA < 100) {

                            strGainA = "0";
                            strGainA += Integer.toString(gainA);

                        } else {

                            strGainA = Integer.toString(gainA);

                        }

                        if (gainB < 10) {

                            strGainB = "00";
                            strGainB += Integer.toString(gainB);

                        } else if (gainB < 100) {

                            strGainB = "0";
                            strGainB += Integer.toString(gainB);

                        } else {

                            strGainB = Integer.toString(gainB);

                        }

                        iSetupGainFragment.iSetupGainFragment(type, strGainA, strGainB);

                    }

                    break;
                }

            }

        }
    };

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (seekBar.getId() == R.id.fragment_setup_gain_gain_a_seekbar) {

                gainA = progress;
                gainATextview.setText(Integer.toString(progress));

            }

            if (seekBar.getId() == R.id.fragment_setup_gain_gain_b_seekbar) {

                gainB = progress;
                gainBTextview.setText(Integer.toString(progress));

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
