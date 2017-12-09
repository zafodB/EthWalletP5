package com.example.filip.ethwalletp5.Crypto;

import android.content.Context;
import android.util.Log;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.math.BigInteger;
import java.security.KeyPair;
import java.util.concurrent.ExecutionException;

/**
 * Created by filip on 06/12/2017.
 */

public class web3jWrapper {

    static Web3j web3;

    public static final int WRAPPER_ERROR = 44;
    public static final int WRAPPER_SUCCESS = 45;

    static {
        web3 = Web3jFactory.build(new HttpService("https://kovan.infura.io/13umMsrT9rOYIlk9UE8j"));

    }


    public static void getClientVersion() {
        Web3ClientVersion clientVersion;
        try {
            clientVersion = web3.web3ClientVersion().sendAsync().get();
            System.out.println(clientVersion.getWeb3ClientVersion());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int createWallet(KeyPair keyPair, Context context) {

        Log.i(AddressBook.TAG_SECURITY, "1");

        ECKeyPair ecKeyPair = ECKeyPair.create(keyPair);

        try {
            WalletUtils.generateWalletFile("asdfghjk", ecKeyPair, new File(context.getFilesDir().getPath()) , true);

        } catch (Exception e) {

            Log.i(AddressBook.TAG_SECURITY, e.getMessage());
            Log.i(AddressBook.TAG_SECURITY, e.getStackTrace().toString());
            e.printStackTrace();
            return WRAPPER_ERROR;
        }

        Log.i(AddressBook.TAG_SECURITY, "Successfully created wallet.");
        return WRAPPER_SUCCESS;
    }

    public static int sendTransaction(){

        BigInteger nonce;
        try {
            nonce = getNonce(AddressBook.getAddress());
        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return WRAPPER_ERROR;
        }

//        RawTransaction rawTrx = RawTransaction.createEtherTransaction(nonce, 50000000010, 22000, )

        return WRAPPER_ERROR;
    }

    private static BigInteger getNonce(String address) throws ExecutionException, InterruptedException{

        EthGetTransactionCount trxCount = web3.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = trxCount.getTransactionCount();

        return nonce;
    }
}
