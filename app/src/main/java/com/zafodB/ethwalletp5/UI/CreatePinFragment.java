package com.zafodB.ethwalletp5.UI;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zafodB.ethwalletp5.Crypto.WalletWrapper;
import com.zafodB.ethwalletp5.FragmentChangerClass;
import com.zafodB.ethwalletp5.MainActivity;
import com.zafodB.ethwalletp5.R;

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
                    Toast.makeText(getActivity(), "Pin must be length of 6!", Toast.LENGTH_SHORT).show();
                } else if (!inputPin.equals(confirmPin.getText().toString())){
                    Toast.makeText(getActivity(), "Pins don't match!", Toast.LENGTH_SHORT).show();
                } else {
                    pin = inputPin;

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Creating new PIN will delete previous wallets on device. Do you want to continue?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    WalletWrapper ww = new WalletWrapper();
                                    int walletsDeleted = ww.deleteAllWallets(getContext());

                                    boolean success = writePinToFile(pin);

                                    if (success) {
                                        FrontPageFragment frontPageFrag = new FrontPageFragment();

                                        FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
                                        changer.ChangeFragments(frontPageFrag);

                                        MainActivity.setUserPin(pin);
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


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
            Toast.makeText(getActivity(), "Pin could not be created - " + e.getMessage(), Toast.LENGTH_SHORT).show();

            return false;
        }

    }

    String getPin(){
        return pin;
    }
}
