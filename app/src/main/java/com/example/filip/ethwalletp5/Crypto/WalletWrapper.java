package com.example.filip.ethwalletp5.Crypto;

import android.content.Context;

import org.web3j.crypto.WalletUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by filip on 10/12/2017.
 */

public class WalletWrapper {

    public static final int WALLET_WRAPPER_ERROR = 46;
    public static final int WALLET_WRAPPER_SUCCESS = 47;

    public int getWallet(Context context){

//        web3jWrapper.getInstance();

        try {
            getWalletFilename(context);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        WalletUtils.loadCredentials("asdfghjk", )
        return WALLET_WRAPPER_ERROR;
    }

//    TODO Handle multiple wallets (right now - the first wallet in file is hardcoded)
    private String getWalletFilename(Context context) throws IOException {
        InputStream in = context.openFileInput(context.getFilesDir().getPath() + "wallets");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));


        String walletFilename = reader.readLine();
        return walletFilename;
//        TODO test File reading (ended up work here)

    }

    public int getPublicKey(){

        return WALLET_WRAPPER_ERROR;
    }


}
