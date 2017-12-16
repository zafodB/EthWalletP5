package com.example.filip.ethwalletp5.Crypto;

import android.content.Context;
import android.util.Log;

import com.example.filip.ethwalletp5.MainActivity;
import com.example.filip.ethwalletp5.UI.CreateWalletFragment;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

        String password = MainActivity.getUserPin();

        try {
            walletFilename = WalletUtils.generateWalletFile(password, ecKeyPair, new File(context.getFilesDir().getPath()), true);

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
            FileOutputStream outputStream = context.openFileOutput(wallets.getName(), Context.MODE_PRIVATE | Context.MODE_APPEND);
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

    public Credentials getWalletCredentials(Context context, String walletname, String password) {

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

            in.close();
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

    public String getWalletFileAsString(Context context , String walletFile){

        try {
            InputStream in = context.openFileInput(walletFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            return reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public int saveWalletFileFromString(Context context, String walletFile, String walletName){

        String fileName = "ImportedWallet" + String.valueOf(System.currentTimeMillis());

        File wallet = new File(context.getFilesDir().getPath(), fileName);

        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(outputStream);

            writer.write(walletFile);
            writer.flush();
            writer.close();

            saveWalletFilename(walletName, fileName, context);

            return WALLET_WRAPPER_SUCCESS;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return WALLET_WRAPPER_ERROR;
        }
    }

    public String reencryptWallet(Context context, String walletName, String oldPass, String newPass){

        try {
            String walletFileName = getWalletFilename(context, walletName);

            Credentials walletCreds = WalletUtils.loadCredentials(oldPass, context.getFilesDir().getPath() + "/" + walletFileName);
            ECKeyPair keyPair = walletCreds.getEcKeyPair();

            return WalletUtils.generateWalletFile(newPass, keyPair, context.getFilesDir(), true);
        }
        catch (IOException | CipherException e){
            e.printStackTrace();
//            TODO error handling;
            return null;
        }
    }

//    void test(){
//        WalletUtils wu = new WalletUtils();
//        WalletFile wf = new WalletFile();
//        wf.getAddress();
//
//        Wallet.
//
//    }

}
