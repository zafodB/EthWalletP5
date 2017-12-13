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
import com.example.filip.ethwalletp5.R;

import java.io.BufferedReader;

/**
 * Created by filip on 13/12/2017.
 */

public class WalletOpenFragment extends Fragment {

    String walletName;

    @Override
    public void setArguments(Bundle args) {
        walletName = args.getString("name");
        super.setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wallet_open_fragment, container, false);

        TextView walletNameText = view.findViewById(R.id.wallet_name_textview);
        walletNameText.setText(walletName);

        Button sendTransactionBtn = view.findViewById(R.id.send_transaction_button);
        sendTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }
}
