package com.zafodB.ethwalletp5.UI;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zafodB.ethwalletp5.Crypto.AddressBook;
import com.zafodB.ethwalletp5.Crypto.WalletWrapper;
import com.zafodB.ethwalletp5.R;

import java.security.KeyPair;

/**
 * Created by filip on 12/12/2017.
 */

public class CreateWalletFragment extends Fragment {

    TextView walletNameInput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_wallet_fragment, container, false);

        walletNameInput = view.findViewById(R.id.wallet_name_field);
        Button createWalletButton = view.findViewById(R.id.create_wallet_button);

        createWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newWalletName = walletNameInput.getText().toString();

                if (newWalletName.length() > 0) {
                    KeyPair myKeyPair = AddressBook.generateAddressPair();

                    WalletWrapper walletWrapper = new WalletWrapper();


                walletWrapper.createWallet(getContext(), walletNameInput.getText().toString(), myKeyPair);

                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Please enter new wallet name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
