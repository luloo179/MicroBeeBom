package quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.setup_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import quad.micro.controller.flight.com.thenalda.www.jangsangjin.naldamicroquad.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetupInterfaceFragment extends Fragment {

    private ISetupInterfaceFragment iSetupInterfaceFragment;

    public interface ISetupInterfaceFragment{

        void iSetupInterfaceFragment(int type);

    }

    public static SetupInterfaceFragment newInstance(ISetupInterfaceFragment iSetupInterfaceFragment) {

        SetupInterfaceFragment setupInterfaceFragment = new SetupInterfaceFragment();
        setupInterfaceFragment.setiSetupInterfaceFragment(iSetupInterfaceFragment);

        return setupInterfaceFragment;

    }

    public void setiSetupInterfaceFragment(ISetupInterfaceFragment iSetupInterfaceFragment){

        this.iSetupInterfaceFragment = iSetupInterfaceFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setup_interface, container, false);

        ImageButton backButton = (ImageButton)view.findViewById(R.id.fragment_setup_interface_backbutton);
        backButton.setOnClickListener(onClickListener);

        return view;

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.fragment_setup_interface_backbutton){

                if(iSetupInterfaceFragment != null){

                    iSetupInterfaceFragment.iSetupInterfaceFragment(0);

                }

            }

        }
    };

}
