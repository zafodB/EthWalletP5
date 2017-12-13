package com.example.filip.ethwalletp5.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    static String[] walletnames = {"test", "normal", "another test", "yet another test"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.front_page_fragment, null);

        ArrayList<String> wallets = WalletWrapper.getWalletNames(getContext());

        ListAdapter adapter;

        if (wallets == null) {
            System.out.println("No wallets to be shown");
            //TODO Handle if there is no wallets.
        } else {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, wallets);

            ListView walletsList = view.findViewById(R.id.listWallets);
            walletsList.setAdapter(adapter);

            walletsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();

                    WalletWrapper walletWrapper = new WalletWrapper();

                    try {

                        Toast.makeText(getContext(), walletWrapper.getWalletFilename(getContext(), walletnames[i]), Toast.LENGTH_LONG).show();


                        Bundle args = new Bundle();
                        args.putString("name", walletnames[i]);

                        Fragment walletOpenFragment = new WalletOpenFragment();
                        walletOpenFragment.setArguments(args);

                        FragmentChangerClass.FragmentChanger changer = (FragmentChangerClass.FragmentChanger)getActivity();
                        changer.ChangeFragments(walletOpenFragment);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return view;
    }


}