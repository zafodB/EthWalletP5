package com.zafodB.ethwalletp5.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.zafodB.ethwalletp5.MainActivity;
import com.zafodB.ethwalletp5.R;

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

                if (email.length() < 1 | password.length() < 6) {
                    Toast.makeText(getActivity(), "Wrong details entered!", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: get encrypted key
                    WalletWrapper walletWrapper = new WalletWrapper();

                    String pin = MainActivity.getUserPin();

                    String tempWalletName = walletWrapper.reencryptWallet(getContext(), walletName, pin, password);
                    String walletFileAsString = walletWrapper.getWalletFileAsString(getContext(), tempWalletName);
                    File tempFile = new File(getContext().getFilesDir().getPath() + "/" +tempWalletName);
                    System.out.println("File deletion was successfull: " + tempFile.delete());

                    String emailHash = Hash.stringHash(email);
                    String emailPassHash = Hash.stringHash(email + password);
                    sendBackup(emailHash, emailPassHash, walletFileAsString);


                }
            }
        });

        return view;
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

                    FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();

                    FrontPageFragment frontPageFrag = new FrontPageFragment();

                    changer.ChangeFragments(frontPageFrag);

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