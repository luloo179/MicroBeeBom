package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;
import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment.adapter.SetupListFragmentAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetupListFragment extends Fragment {

    private ISetupListFragment iSetupListFragment;

    public interface ISetupListFragment{

        void iSetupListFragment(int type);

    }

    public static SetupListFragment newInstance(ISetupListFragment iSetupListFragment){

        SetupListFragment setupListFragment = new SetupListFragment();
        setupListFragment.setSetupListFragment(iSetupListFragment);

        return setupListFragment;

    }

    public void setSetupListFragment(ISetupListFragment iSetupListFragment){

        this.iSetupListFragment = iSetupListFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setup_list, container, false);

        ImageButton backButton = (ImageButton)view.findViewById(R.id.fragment_setup_backbutton);
        backButton.setOnClickListener(onClickListener);

        ListView listView = (ListView)view.findViewById(R.id.fragment_setup_listview);
        SetupListFragmentAdapter setupListFragmentAdapter = new SetupListFragmentAdapter(getActivity());
        listView.setAdapter(setupListFragmentAdapter);
        listView.setOnItemClickListener(onItemClickListener);

        return view;

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(iSetupListFragment != null){

                iSetupListFragment.iSetupListFragment(0);

            }

        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (position){

                case 1:{
                    //account

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(1);

                    }

                    break;
                }

                case 3:{
                    //connection

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(2);

                    }

                    break;
                }

                case 5:{
                    //interface

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(3);

                    }

                    break;
                }

                case 7:{
                    //roll gain

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(4);

                    }

                    break;
                }

                case 8:{
                    //pitch gain

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(5);

                    }

                    break;
                }

                case 9:{
                    //yaw gain

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(6);

                    }

                    break;
                }

                case 10:{
                    //altitude gain

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(7);

                    }

                    break;
                }

                case 11:{
                    //postion gain

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(8);

                    }

                    break;
                }

                case 13:{
                    //trim setting

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(9);

                    }

                    break;
                }

                case 15:{
                    //accel calibration

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(10);

                    }

                    break;
                }

                case 16:{
                    //gyro calibration

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(11);

                    }

                    break;
                }

                case 17:{
                    //mag calibration

                    if(iSetupListFragment != null){

                        iSetupListFragment.iSetupListFragment(12);

                    }

                    break;
                }

            }

        }
    };

}
