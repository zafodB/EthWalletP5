package com.example.filip.ethwalletp5.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.filip.ethwalletp5.Crypto.WalletWrapper;
import com.example.filip.ethwalletp5.FragmentChangerClass;
import com.example.filip.ethwalletp5.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by filip on 12/12/2017.
 */

public class FrontPageFragment extends Fragment {
    Button createNewWalletBtn;
    Button restoreWalletBtn;

    //    static String[] walletnames = {"test", "normal", "another test", "yet another test"};
    ArrayList<String> wallets;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.front_page_fragment, container, false);

        createNewWalletBtn = view.findViewById(R.id.create_new_wallet_btn);
        restoreWalletBtn = view.findViewById(R.id.restore_wallet_button);


        wallets = WalletWrapper.getWalletNames(getContext());

        ListAdapter adapter;

        if (wallets == null) {
            System.out.println("No wallets to be shown");
            //TODO Handle if there is no wallets.
        } else {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, wallets);

            ListView walletsList = view.findViewById(R.id.list_wallets);
            walletsList.setAdapter(adapter);

            walletsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();

                    WalletWrapper walletWrapper = new WalletWrapper();


                    Toast.makeText(getContext(), walletWrapper.getWalletFilename(getContext(), wallets.get(i)), Toast.LENGTH_SHORT).show();

                    Bundle args = new Bundle();
                    args.putString("name", wallets.get(i));

                    Fragment walletOpenFragment = new WalletOpenFragment();
                    walletOpenFragment.setArguments(args);

                    FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
                    changer.ChangeFragments(walletOpenFragment);


                }
            });
        }

        createNewWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateWalletFragment createWalletFrag = new CreateWalletFragment();

                FragmentChangerClass.FragmentChanger fragmentChanger = (FragmentChangerClass.FragmentChanger) getActivity();

                fragmentChanger.ChangeFragments(createWalletFrag);
            }
        });

        restoreWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment restoreWalletFragment = new RestoreWalletFragment();
                FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger) getActivity();
                changer.ChangeFragments(restoreWalletFragment);
            }
        });

        return view;
    }


}