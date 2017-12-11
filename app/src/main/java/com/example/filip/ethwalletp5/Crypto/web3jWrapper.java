package com.example.filip.ethwalletp5.Crypto;

import android.content.Context;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * Created by filip on 06/12/2017.
 */

public class web3jWrapper {

    static Web3j web3;

    public static final int WRAPPER_ERROR = 44;
    public static final int WRAPPER_SUCCESS = 45;

    static {
        web3 = Web3jFactory.build(new HttpService("https://kovan.infura.io/Ceux1wHF7EsQWKb9p8da"));
    }

    public static Web3j getInstance() {
        return web3;
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

    public static int sendTransaction(Context context /*, String to*/) {

        Credentials credentials = new WalletWrapper().getWallet(context);


        BigInteger nonce = getNonce(credentials.getAddress());
        if (nonce == null) {
            System.out.println("Could not load nonce.");
            Log.e(AddressBook.TAG_SECURITY, "Could not load nonce.");
            return WRAPPER_ERROR;
        }

        System.out.printf("\nNonce for address: %s \nis this: %d\n", credentials.getAddress(), nonce);

        BigInteger gasPrice = new BigInteger("20000000000");
        BigInteger gasLimit = new BigInteger("400000");
        String to = "0xC589a27fCC1b1De994cEE8910c33FF74E2Dd649E";
        BigInteger value = new BigInteger("3500000000");


        RawTransaction rawTrx = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTrx, credentials);
        String hexMessage = Numeric.toHexString(signedMessage);

        try {
            EthSendTransaction ethSendTx;

            ethSendTx = web3.ethSendRawTransaction(hexMessage).sendAsync().get();


            String txHash = ethSendTx.getTransactionHash();

//            TODO if implement, there is an error with the message
            System.out.println("Transaction hash is:" + txHash);

            return WRAPPER_SUCCESS;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return WRAPPER_ERROR;
        }

//        return WRAPPER_ERROR;
    }

    private static BigInteger getNonce(String address) {

        EthGetTransactionCount trxCount = null;
        try {
            trxCount = web3
                    .ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            return trxCount.getTransactionCount();

        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

            return null;
        }
    }

}
