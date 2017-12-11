package com.example.filip.ethwalletp5.Crypto;

import android.content.Context;
import android.util.Log;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyPair;

/**
 * Created by filip on 10/12/2017.
 */

public class WalletWrapper {

    public static final int WALLET_WRAPPER_ERROR = 46;
    public static final int WALLET_WRAPPER_SUCCESS = 47;

    public static int createWallet(KeyPair keyPair, Context context) {

        context = context;

        Log.i(AddressBook.TAG_SECURITY, "1");

        ECKeyPair ecKeyPair = ECKeyPair.create(keyPair);

        String walletFilename;

        try {
           walletFilename  = WalletUtils.generateWalletFile("asdfghjk", ecKeyPair, new File(context.getFilesDir().getPath()) , true);

        } catch (Exception e) {

            Log.i(AddressBook.TAG_SECURITY, e.getMessage());
            Log.i(AddressBook.TAG_SECURITY, e.getStackTrace().toString());
            e.printStackTrace();
            return web3jWrapper.WRAPPER_ERROR;
        }

        saveWalletFilename(walletFilename, context);

        Log.i(AddressBook.TAG_SECURITY, "Successfully created wallet.");
        return web3jWrapper.WRAPPER_SUCCESS;
    }

    //    TODO Move to WalletWrapper
    //    TODO Handle multiple wallets
        private static int saveWalletFilename(String filename, Context context){
            File wallets = new File (context.getFilesDir().getPath(), "wallets");

            if (!wallets.exists()){
                try {
                    wallets.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return web3jWrapper.WRAPPER_ERROR;
                }
            }

            try {
                FileOutputStream outputStream = context.openFileOutput(wallets.getName(), Context.MODE_APPEND);
                PrintWriter printWriter = new PrintWriter(outputStream);
                printWriter.write("UTC--2017-12-09T10-52-10.277--e87bd5722e14a0be1553e67796f3e922f3c1143a.json");
    //            printWriter.write(filename);
                printWriter.println();
                printWriter.flush();
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                return web3jWrapper.WRAPPER_ERROR;
            }

            return web3jWrapper.WRAPPER_SUCCESS;
        }

    public Credentials getWallet(Context context) {

//        web3jWrapper.getInstance();
        String walletFilename;
        Credentials walletCreds;

        try {
            walletFilename =  getWalletFilename(context);
            walletCreds = WalletUtils.loadCredentials("asdfghjk", context.getFilesDir().getPath() + "/" + walletFilename);
        } catch (IOException | CipherException e) {
            e.printStackTrace();

            return null;
        }

        return walletCreds;


    }

    //    TODO Handle multiple wallets (right now - the first wallet in file is hardcoded)
    private String getWalletFilename(Context context) throws IOException {

        InputStream in = context.openFileInput("wallets");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String walletFilename = reader.readLine();

        reader.close();
        return walletFilename;
    }

    public int getPublicKey() {

        return WALLET_WRAPPER_ERROR;
    }


}
