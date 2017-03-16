package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetupConnectionFragment extends Fragment {


    public SetupConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setup_connection, container, false);
    }

}
