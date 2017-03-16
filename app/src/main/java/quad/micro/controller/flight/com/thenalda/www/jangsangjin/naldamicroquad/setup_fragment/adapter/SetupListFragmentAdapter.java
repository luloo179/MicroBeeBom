package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;

/**
 * Created by jangsangjin on 2017. 2. 24..
 */

public class SetupListFragmentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> setupArrayList;

    public SetupListFragmentAdapter(Context context) {

        this.context = context;
        this.setupArrayList = new ArrayList<String>();
        this.setupArrayList.add("NULL");
        this.setupArrayList.add("Account");
        this.setupArrayList.add("NULL");
        this.setupArrayList.add("Connection");
        this.setupArrayList.add("NULL");
        this.setupArrayList.add("Interface");
        this.setupArrayList.add("NULL");
        this.setupArrayList.add("Roll Gain");
        this.setupArrayList.add("Pitch Gain");
        this.setupArrayList.add("Yaw Gain");
        this.setupArrayList.add("Altitude Gain");
        this.setupArrayList.add("Position Gain");
        this.setupArrayList.add("NULL");
        this.setupArrayList.add("Manual Trim Setup");
        this.setupArrayList.add("NULL");
        this.setupArrayList.add("Acceleration Calibration");
        this.setupArrayList.add("Gyroscope Calibration");
        this.setupArrayList.add("Magnetic Calibration");
        this.setupArrayList.add("NULL");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return this.setupArrayList.get(position);
    }

    @Override
    public int getCount() {
        return this.setupArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.fragment_setup_list_title_cell, null);
        TextView textView = (TextView) v.findViewById(R.id.fragment_setup_list_title_cell_textview);
        RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.fragment_setup_list_title_cell_linearlayout);

        if (this.setupArrayList.get(position).compareTo("NULL") == 0) {

            v = inflater.inflate(R.layout.fragment_setup_list_title_space, null);

        } else {

            textView.setText(this.setupArrayList.get(position));
            textView.setTextSize(15);

            relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }

        return v;
    }
}
