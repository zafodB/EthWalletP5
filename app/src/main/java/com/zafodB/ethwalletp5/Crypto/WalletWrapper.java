package com.zafodB.ethwalletp5.Crypto;

import android.content.Context;
import android.util.Log;

import com.zafodB.ethwalletp5.MainActivity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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


    public String createBrandNewWallet(Context context, String newPassword, String walletName, KeyPair keyPair) {
        ECKeyPair ecKeyPair = ECKeyPair.create(keyPair);

        String fileName = createWallet(context, newPassword, walletName, ecKeyPair, false);

        saveWalletFilename(context, walletName, fileName);

        return null;
    }

    public String createWallet(Context context, String newPassword, String walletName, ECKeyPair ecKeyPair, boolean temp) {

//        String password = MainActivity.getUserPin();
        String publicKey;
        try {
//            walletFilename = WalletUtils.generateWalletFile(password, ecKeyPair, new File(context.getFilesDir().getPath()), true);

//            Edits to wallet creation here
            WalletFile walletFile = Wallet.createLight(newPassword, ecKeyPair);

            String fileName = walletFile.getAddress();

            File destination;

            if (temp){
                destination = new File(context.getCacheDir().getPath(), fileName);
            } else {
                destination = new File(context.getFilesDir().getPath(), fileName);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            objectMapper.writeValue(destination, walletFile);

            publicKey = walletFile.getAddress();

            Log.i(AddressBook.TAG_SECURITY, "Successfully created wallet.");
            return publicKey;

//            Edits end here

        } catch (Exception e) {

            Log.i(AddressBook.TAG_SECURITY, e.getMessage());
            Log.i(AddressBook.TAG_SECURITY, e.getStackTrace().toString());
            e.printStackTrace();
            return null;
        }
    }

    private static int saveWalletFilename(Context context, String walletName, String filename) {
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

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return web3jWrapper.WRAPPER_ERROR;
        }

        return web3jWrapper.WRAPPER_SUCCESS;
    }

    public Credentials getWalletCredentials(Context context, String walletName, String password) {

//        web3jWrapper.getInstance();
        String walletFilename;
        Credentials walletCreds;


//        walletCreds.getEcKeyPair()
        try {
            walletFilename = getWalletFilename(context, walletName);
            walletCreds = WalletUtils.loadCredentials(password, context.getFilesDir().getPath() + "/" + walletFilename);
        } catch (IOException | CipherException e) {
            e.printStackTrace();

            return null;
        }

        return walletCreds;
    }

    public String getWalletFilename(Context context, String walletName) {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    public String getWalletFileAsString(Context context, String walletFile) {

        try {
            FileInputStream in = new FileInputStream (new File(context.getCacheDir().getPath(), walletFile));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            return reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int saveWalletFileFromString(Context context, String walletFile, String password, String walletName) {

        String fileName = walletFile.substring(12, 52);

//        File wallet = new File(context.getFilesDir().getPath(), fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(new File(context.getCacheDir().getPath(), fileName));
            PrintWriter writer = new PrintWriter(outputStream);

            writer.write(walletFile);
            writer.flush();
            writer.close();

            saveWalletFilename(context, walletName, fileName);

            reencryptWallet(context, walletName, password, MainActivity.getUserPin(), false);

            return WALLET_WRAPPER_SUCCESS;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return WALLET_WRAPPER_ERROR;
        }
    }

    public String reencryptWallet(Context context, String walletName, String oldPass, String newPass, boolean temp) {

        try {
            String walletFileName = getWalletFilename(context, walletName);

            Credentials walletCreds;
            if (temp) {
                walletCreds = WalletUtils.loadCredentials(oldPass, context.getFilesDir().getPath() + "/" + walletFileName);
            } else {
                walletCreds = WalletUtils.loadCredentials(oldPass, context.getCacheDir().getPath() + "/" + walletFileName);
            }
            ECKeyPair keyPair = walletCreds.getEcKeyPair();

//            return WalletUtils.generateWalletFile(newPass, keyPair, context.getFilesDir(), false);
            return createWallet(context, newPass, walletName, keyPair, temp);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
//            TODO error handling;
            return null;
        }
    }

    public int deleteAllWallets(Context context){

//        File dir = new File(context.getFilesDir().getPath());
//        if (dir.isDirectory())
//        {
//            String[] children = dir.list();
//            foreach(children i)
//            {
//                new File(dir, children[i]).delete();
//            }
//        }

        return WALLET_WRAPPER_ERROR;
    }

}
