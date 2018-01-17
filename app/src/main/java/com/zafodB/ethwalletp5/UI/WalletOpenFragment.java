package com.zafodB.ethwalletp5.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zafodB.ethwalletp5.Crypto.WalletWrapper;
import com.zafodB.ethwalletp5.Crypto.web3jWrapper;
import com.zafodB.ethwalletp5.FragmentChangerClass;
import com.zafodB.ethwalletp5.MainActivity;
import com.zafodB.ethwalletp5.R;

import org.spongycastle.util.encoders.Hex;
import org.web3j.protocol.Web3j;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;

/**
 * Created by filip on 13/12/2017.
 */

public class WalletOpenFragment extends Fragment {

    String walletName;

    Button createBackupBtn;


    @Override
    public void setArguments(Bundle args) {
        walletName = args.getString("name");
        super.setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wallet_open_fragment, container, false);

        createBackupBtn = view.findViewById(R.id.createBackupBtn);

        TextView walletNameText = view.findViewById(R.id.wallet_name_textview);
        walletNameText.setText(walletName);

        TextView walletBalance = view.findViewById(R.id.eth_balance_textview);
//        TODO add loading dialog
        WalletWrapper walletWrapper = new WalletWrapper();
        String publicKey = walletWrapper.getWalletFilename(getContext(), walletName);

        TextView publicKeyText = view.findViewById(R.id.wallet_public_key_textview);
        publicKeyText.setText("0x" + publicKey);
        walletBalance.setText(balanceToString(web3jWrapper.getBallance(publicKey)));

        Button sendTransactionBtn = view.findViewById(R.id.send_transaction_button);
        sendTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putString("name", walletName);

                Fragment sendTransactionFragment = new SendTransactionFragment();
                sendTransactionFragment.setArguments(args);

                FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
                changer.ChangeFragments(sendTransactionFragment);


//                try {
//                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//
//                    String pass = "12345";
//                    messageDigest.update(pass.getBytes());
//                    byte[] hashed = messageDigest.digest();
//
//                    System.out.println(Hex.toHexString(hashed));
//                    System.out.println(messageDigest.toString());
//
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
            }
        });


        // TODO: if backup already created display 'Restore wallet' option
        createBackupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment createBackupFragment = new CreateBackupFragment();
                Bundle args = new Bundle();
                args.putString("name", walletName);

                createBackupFragment.setArguments(args);

                FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
                changer.ChangeFragments(createBackupFragment);
            }
        });

        return view;
    }

    String balanceToString(BigInteger balance) {

        BigDecimal divisor = new BigDecimal("1000000000000000000");
        BigDecimal balanceD = new BigDecimal(balance);
        balanceD = balanceD.divide(divisor);
        balanceD = balanceD.setScale(5, BigDecimal.ROUND_HALF_UP);

//        System.out.print(balanceD);

        return balanceD.toPlainString() + "ETH";

    }
}
