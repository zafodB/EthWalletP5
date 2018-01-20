package com.zafodB.ethwalletp5.UI;

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

import com.zafodB.ethwalletp5.API.APIClient;
import com.zafodB.ethwalletp5.API.APIInterface;
import com.zafodB.ethwalletp5.API.Models;
import com.zafodB.ethwalletp5.Crypto.Hash;
import com.zafodB.ethwalletp5.Crypto.WalletWrapper;
import com.zafodB.ethwalletp5.FragmentChangerClass;
import com.zafodB.ethwalletp5.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestoreWalletFragment extends Fragment {
    EditText emailInput;
    EditText passwordInput;
    Button restoreWalletBtn;

    String password;

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
                password = passwordInput.getText().toString();


                // TODO: Email and password validation
                if (email.length() == 0 || password.length() == 0) {
                    Toast.makeText(getActivity(), "Provide your email and password", Toast.LENGTH_SHORT).show();
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
        Models.Backup backupRequest = new Models.Backup(emailHash, emailPassHash);

        APIInterface apiService = APIClient.getInstance();
        Call<Models.Backup> call = apiService.restoreWallet(backupRequest);

        call.enqueue(new Callback<Models.Backup>() {
            @Override
            public void onResponse(Call<Models.Backup> call, Response<Models.Backup> response) {
                if (response.code() == 200) {
                    String encryptedWalletFile = response.body().getWalletFile();
                    WalletWrapper walletWrapper = new WalletWrapper();
                    walletWrapper.saveWalletFileFromString(getContext(), encryptedWalletFile, password,"Restored Wallet");

                    FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
                    FrontPageFragment frontPageFrag = new FrontPageFragment();
                    changer.ChangeFragments(frontPageFrag);

                    Toast.makeText(getActivity(), "Wallet restored successfully", Toast.LENGTH_SHORT).show();

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