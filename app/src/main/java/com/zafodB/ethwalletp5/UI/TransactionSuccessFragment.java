package com.zafodB.ethwalletp5.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zafodB.ethwalletp5.FragmentChangerClass;
import com.zafodB.ethwalletp5.R;

/**
 * Created by filip on 17/01/2018.
 */

public class TransactionSuccessFragment extends Fragment {

    String txHash;
    String walletName;

    @Override
    public void setArguments(Bundle args) {
        txHash = args.getString("txHash");
        walletName = args.getString("name");
        super.setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_success_fragment, container, false);

        TextView txHashView = view.findViewById(R.id.txHashView);
        txHashView.setText(txHash);

        Button okBtn = view.findViewById(R.id.okButton);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("name", walletName);

                Fragment walletOpenFragment = new WalletOpenFragment();
                walletOpenFragment.setArguments(args);

                FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
                changer.ChangeFragments(walletOpenFragment);
            }
        });


        return view;
    }
}
