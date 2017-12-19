package com.zafodB.ethwalletp5.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zafodB.ethwalletp5.Crypto.web3jWrapper;
import com.zafodB.ethwalletp5.R;

import java.io.BufferedReader;
import java.math.BigInteger;

import static com.zafodB.ethwalletp5.Crypto.web3jWrapper.sendTransaction;

/**
 * Created by filip on 13/12/2017.
 */

public class SendTransactionFragment extends Fragment{

    String walletName;

    TextView toAddressText;
    TextView gasPriceText;
    TextView amountText;


    @Override
    public void setArguments(Bundle args) {
        walletName = args.getString("name");
        super.setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_transaction_fragment, container, false);

        toAddressText = view.findViewById(R.id.to_address_field);

        gasPriceText = view.findViewById(R.id.gas_price_field);

        amountText = view.findViewById(R.id.amount_field);

        Button sendTxBtn = view.findViewById(R.id.send_out_tx);

        sendTxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                TODO Do input validation
                String toAddress = toAddressText.getText().toString();
                BigInteger gasPrice = new BigInteger(gasPriceText.getText().toString());
                BigInteger amount = new BigInteger(amountText.getText().toString());


//                TODO handle transaction result
               web3jWrapper.sendTransaction(getContext(), walletName, toAddress, gasPrice, amount);

            }
        });


        return view;
    }
}
