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
import com.example.filip.ethwalletp5.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestoreWalletFragment extends Fragment {
    EditText emailInput;
    EditText passwordInput;
    Button restoreWalletBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.restore_wallet_fragment, container, false);

        emailInput = view.findViewById(R.id.backup_email_input);
        passwordInput = view.findViewById(R.id.backup_password_input);
        restoreWalletBtn = view.findViewById(R.id.restore_wallet_button);

        restoreWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // TODO: Email and password validation
                if (email.length() < 1) {
                    System.out.println("No email provided");
                } else {
                    String emailHash = Hash.stringHash(email);
                    String emailPassHash = Hash.stringHash(email + password);

                    requestBackup(emailHash, emailPassHash);
                }
            }
        });

        return view;
    }

    private void requestBackup(String emailHash, String emailPassHash) {
        Models.Backup backup = new Models.Backup(emailHash, emailPassHash);

        APIInterface service = APIClient.getInstance();
        Call<Models.Backup> call = service.restoreWallet(backup);

        call.enqueue(new Callback<Models.Backup>() {
            @Override
            public void onResponse(Call<Models.Backup> call, Response<Models.Backup> response) {
                if (response.code() == 200) {
                    String encryptedWalletFile = response.body().getWallet_file();
                    WalletWrapper walletWrapper = new WalletWrapper();
                    walletWrapper.saveWalletFileFromString(getContext(), encryptedWalletFile, "Restored Wallet");
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Models.Backup> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed" + "\t" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}