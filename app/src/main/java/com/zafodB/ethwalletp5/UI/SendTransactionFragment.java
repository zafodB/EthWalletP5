package com.zafodB.ethwalletp5.UI;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zafodB.ethwalletp5.Crypto.web3jWrapper;
import com.zafodB.ethwalletp5.FragmentChangerClass;
import com.zafodB.ethwalletp5.R;

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

                String toAddress = toAddressText.getText().toString();
                String gasPrice = gasPriceText.getText().toString();
                String amount = amountText.getText().toString();

                if (toAddress.isEmpty() || toAddress.equals("To Address")){
                    toast("Please enter a valid address.", false);
                    return;
                } else if (gasPrice.isEmpty() || gasPrice.equals("Gas Price (20 GWEI)")){
                    toast("Please enter valid gas price.", false);
                    return;
                } else if (amount.isEmpty() || amount.equals("Amount (in ETH)")){
                    toast("Please enter valid amount.", false);
                    return;
                }

               if ((web3jWrapper.sendTransaction(getContext(), walletName, toAddress, gasPrice, amount) == web3jWrapper.WRAPPER_SUCCESS)){

                   Bundle args = new Bundle();
                   args.putString("txHash", web3jWrapper.getTxHash());
                   args.putString("name", walletName);

                   Fragment txSuccessFragment = new TransactionSuccessFragment();
                   txSuccessFragment.setArguments(args);

                   FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
                   changer.ChangeFragments(txSuccessFragment);
               }

            }
        });

        return view;
    }

    void toast (String message, boolean isLong){
        if (isLong){
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
