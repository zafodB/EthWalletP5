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

import com.example.filip.ethwalletp5.API.APIClient;
import com.example.filip.ethwalletp5.API.APIInterface;
import com.example.filip.ethwalletp5.API.Models;
import com.example.filip.ethwalletp5.Crypto.Hash;
import com.example.filip.ethwalletp5.Crypto.WalletWrapper;
import com.example.filip.ethwalletp5.MainActivity;
import com.example.filip.ethwalletp5.R;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateBackupFragment extends Fragment {
    EditText emailInput;
    EditText passwordInput;
    Button createBackupBtn;
    String walletName;

    @Override
    public void setArguments(Bundle args) {

        walletName = args.getString("name");
        super.setArguments(args);

    }

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
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // TODO: Email and password validation
                if (email.length() < 1 | password.length() < 6) {
                    System.out.println("Incorrect details entered");
                } else {
                    // TODO: get encrypted key
                    WalletWrapper walletWrapper = new WalletWrapper();

                    String pin = MainActivity.getUserPin();

                    String tempWalletName = walletWrapper.reencryptWallet(getContext(), walletName, pin, password);
                    String walletFileAsString = walletWrapper.getWalletFileAsString(getContext(), tempWalletName);
                    File tempFile = new File(getContext().getFilesDir().getPath() + "/" +tempWalletName);
                    System.out.println("File deletion was successfull: " + tempFile.delete());

                    // TODO: how to know if dialog was confirmed?
//                    showConfirmationDialog(email, password);

                    String emailHash = Hash.stringHash(email);
                    String emailPassHash = Hash.stringHash(email + password);
                    sendBackup(emailHash, emailPassHash, walletFileAsString);
                }
            }
        });

        return view;
    }

    private void showConfirmationDialog(String email, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm backup details");
        builder.setMessage("Make sure entered details are right before confirm\n\n" + "Email - " + email + "\nPassword - " + password);
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: implement API call after confirm
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

    private void sendBackup(String emailHash, String emailPassHash, String walletFileAsString) {
        Models.Backup backup = new Models.Backup(emailHash, emailPassHash, walletFileAsString);

        APIInterface service = APIClient.getInstance();
        Call<Models.Backup> call = service.createBackup(backup);

        call.enqueue(new Callback<Models.Backup>() {
            @Override
            public void onResponse(Call<Models.Backup> call, Response<Models.Backup> response) {
                if (response.code() == 201) {
                    Toast.makeText(getActivity(), "Backup successfully saved in the database!", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: handle response (fail or update?)
                    Toast.makeText(getActivity(), "Something went wrong: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Models.Backup> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed" + "\t" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}