package com.example.filip.ethwalletp5.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.filip.ethwalletp5.Crypto.WalletWrapper;
import com.example.filip.ethwalletp5.FragmentChangerClass;
import com.example.filip.ethwalletp5.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by filip on 14/12/2017.
 */

public class EnterPinFragment extends Fragment {

    TextView enterPin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enter_pin_fragment, container, false);

        enterPin = view.findViewById(R.id.enter_pin_field);
        Button createPinBtn = view.findViewById(R.id.create_pin_btn);
        Button singInBtn = view.findViewById(R.id.sign_in_button);

        createPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePinFragment createPinFrag = new CreatePinFragment();

                WalletWrapper walletWrapper = new WalletWrapper();
                System.out.println(walletWrapper.getWalletFileAsString(getContext(), "normal"));

//                Disabled for test
//                FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
//                changer.ChangeFragments(createPinFrag);
            }
        });

        singInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (!verifyPin(enterPin.getText().toString())){
                   System.out.println("Pin is wrong or there has been an error");
                   enterPin.setText("");
               }else {
                   FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();

                   FrontPageFragment fronPageFrag = new FrontPageFragment();

                   changer.ChangeFragments(fronPageFrag);
               }
            }
        });

        return view;
    }

    private boolean verifyPin(String input){
        try {
            InputStream in = getContext().openFileInput("pinStorage");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String pin = reader.readLine();

//            TODO PIN should be secured

            reader.close();
            return pin.equals(input);

        } catch (IOException  e) {
            e.printStackTrace();
//            TODO Handle UI
            return false;
        }

    }
}
