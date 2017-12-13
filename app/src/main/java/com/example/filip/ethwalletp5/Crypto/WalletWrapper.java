package com.example.filip.ethwalletp5.Crypto;

import android.content.Context;
import android.util.Log;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.util.ArrayList;

/**
 * Created by filip on 10/12/2017.
 */

public class WalletWrapper {

    public static final int WALLET_WRAPPER_ERROR = 46;
    public static final int WALLET_WRAPPER_SUCCESS = 47;

    public static int createWallet(String walletName, KeyPair keyPair, Context context) {

        context = context;

        Log.i(AddressBook.TAG_SECURITY, "1");

        ECKeyPair ecKeyPair = ECKeyPair.create(keyPair);

        String walletFilename;

        try {
            walletFilename = WalletUtils.generateWalletFile("asdfghjk", ecKeyPair, new File(context.getFilesDir().getPath()), true);

        } catch (Exception e) {

            Log.i(AddressBook.TAG_SECURITY, e.getMessage());
            Log.i(AddressBook.TAG_SECURITY, e.getStackTrace().toString());
            e.printStackTrace();
            return web3jWrapper.WRAPPER_ERROR;
        }

        saveWalletFilename(walletName, walletFilename, context);

        Log.i(AddressBook.TAG_SECURITY, "Successfully created wallet.");
        return web3jWrapper.WRAPPER_SUCCESS;
    }

    //    TODO Move to WalletWrapper
    //    TODO Handle multiple wallets
    private static int saveWalletFilename(String walletName, String filename, Context context) {
        File wallets = new File(context.getFilesDir().getPath(), "wallets");

        if (!wallets.exists()) {
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

            printWriter.write(walletName);
            printWriter.println();
            printWriter.write(filename);
            printWriter.println();

//                printWriter.write("normal");
//                printWriter.println();
//                printWriter.write("UTC--2017-12-09T10-52-10.277--e87bd5722e14a0be1553e67796f3e922f3c1143a.json");
//                printWriter.println();
//
//                printWriter.write("another test");
//                printWriter.println();
//                printWriter.write("anothertestwalletfile.json");
//                printWriter.println();
//
//                printWriter.write("yet another test");
//                printWriter.println();
//                printWriter.write("yetanothertestwalletfile.json");
//                printWriter.println();

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return web3jWrapper.WRAPPER_ERROR;
        }

        return web3jWrapper.WRAPPER_SUCCESS;
    }

    public Credentials getWallet(Context context, String walletname, String password) {

//        web3jWrapper.getInstance();
        String walletFilename;
        Credentials walletCreds;

        try {
            walletFilename = getWalletFilename(context, walletname);
            walletCreds = WalletUtils.loadCredentials(password, context.getFilesDir().getPath() + "/" + walletFilename);
        } catch (IOException | CipherException e) {
            e.printStackTrace();

            return null;
        }

        return walletCreds;


    }

    public String getWalletFilename(Context context, String walletName) throws IOException {
        InputStream in = context.openFileInput("wallets");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String start = reader.readLine();

        while (start != null) {
            if (walletName.equals(start)) {
                break;
            } else {
                reader.readLine();
                start = reader.readLine();
            }
        }

        String walletFilename = reader.readLine();

        reader.close();
        return walletFilename;
    }

    public boolean WalletFileExists(Context context) {
        File file = new File(context.getFilesDir() + "/wallets");
        return file.exists();
    }

    public static ArrayList<String> getWalletNames(Context context) {
        ArrayList<String> walletNames = new ArrayList<>();

        try {
            InputStream in = context.openFileInput("wallets");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String start = reader.readLine();

            while (start != null) {
                walletNames.add(start);
                reader.readLine();
                start = reader.readLine();
            }
            return walletNames;
        } catch (IOException e) {
            System.out.println("Could not load wallet names.");
            Log.e(AddressBook.TAG_SECURITY, "Could not load wallet names.");
            return null;
        }
    }

    public int getPublicKey() {

        return WALLET_WRAPPER_ERROR;
    }


}
