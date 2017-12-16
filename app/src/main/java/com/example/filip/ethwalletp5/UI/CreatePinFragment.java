package com.example.filip.ethwalletp5.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.filip.ethwalletp5.FragmentChangerClass;
import com.example.filip.ethwalletp5.MainActivity;
import com.example.filip.ethwalletp5.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by filip on 14/12/2017.
 */

public class CreatePinFragment extends Fragment{
    TextView createPin;
    TextView confirmPin;
    Button createPinBtn;

    private String pin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.create_pin_fragment, container, false);

        createPin = view.findViewById(R.id.create_pin_input);
        confirmPin = view.findViewById(R.id.confirm_pin_input);
        createPinBtn = view.findViewById(R.id.save_pin_button);

        createPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputPin = createPin.getText().toString();

                if (inputPin.length() != 6){
                    System.out.println("Pin is too short!");
//                    TODO Handle wrong PIN size
                }else if (!inputPin.equals(confirmPin.getText().toString())){
                    System.out.println("Pins don't match!");
                } else {
                    pin = inputPin;
                    boolean success = writePinToFile(pin);
                    System.out.println("Saving pin has been successful: " + success);

                    FrontPageFragment frontPageFrag = new FrontPageFragment();

                    FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
                    changer.ChangeFragments(frontPageFrag);

                    MainActivity.setUserPin(pin);
                }



            }
        });

        return view;
    }

    private boolean writePinToFile(String input){
        try {
//            TODO PIN should be secured
            File pinStorage = new File(getContext().getFilesDir().getPath(), "pinStorage");

            FileOutputStream outputStream = getContext().openFileOutput(pinStorage.getName(), Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(outputStream);

            printWriter.write(input);
            printWriter.flush();
            printWriter.close();

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            TODO Handle UI
            return false;
        }

    }

    String getPin(){
        return pin;
    }
}
