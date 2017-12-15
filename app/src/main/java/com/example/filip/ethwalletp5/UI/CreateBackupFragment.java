package com.example.filip.ethwalletp5.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filip.ethwalletp5.FragmentChangerClass;
import com.example.filip.ethwalletp5.R;


public class CreateBackupFragment extends Fragment{
    EditText emailInput;
    EditText passwordInput;
    Button createBackupBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.create_backup_fragment, container, false);

        emailInput = view.findViewById(R.id.backup_email_input);
        passwordInput = view.findViewById(R.id.backup_password_input);
        createBackupBtn = view.findViewById(R.id.create_backup_button);

        createBackupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: hash email and email+pass and get encrypted private key
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // TODO: Email validation
                if (email.length() < 1){
                    System.out.println("No email provided");
                } else {
                    confirmBackupCreation(email, password);
//                    Toast.makeText(getContext(), email + ' ' + password, Toast.LENGTH_SHORT).show();
                    // TODO: POST https://eth-wallet-api.herokuapp.com/users/
//                    FrontPageFragment frontPageFrag = new FrontPageFragment();
//
//                    FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
//                    changer.ChangeFragments(frontPageFrag);
                }
            }
        });

        return view;
    }

    private void confirmBackupCreation(String email, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm backup details");
        builder.setMessage("Make sure entered details are right before confirm\n\n" + "Email - " + email + "\nPassword - " + password);
        builder.setCancelable(true);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Backup sent to database!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
