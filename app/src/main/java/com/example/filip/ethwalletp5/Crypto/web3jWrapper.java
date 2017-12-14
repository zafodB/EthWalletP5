package com.example.filip.ethwalletp5.Crypto;

import android.content.Context;
import android.util.Log;

import com.example.filip.ethwalletp5.MainActivity;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
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

    final static Web3j web3;

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

    public static int sendTransaction(Context context , String to, BigInteger gasPrice, BigInteger value) {

        String walletname = "normal";
        //TODO remove test

        Credentials credentials = new WalletWrapper().getWallet(context, walletname, MainActivity.getUserPin());


        BigInteger nonce = getNonce(credentials.getAddress());
        if (nonce == null) {
            System.out.println("Could not load nonce.");
            Log.e(AddressBook.TAG_SECURITY, "Could not load nonce.");
            return WRAPPER_ERROR;
        }

        System.out.printf("\nNonce for address: %s \nis this: %d\n", credentials.getAddress(), nonce);

//        TODO Remove Test
        BigInteger sGasPrice = new BigInteger("20000000000");
        String recipientAddr = "0xC589a27fCC1b1De994cEE8910c33FF74E2Dd649E";
//        TEST END

//        BigInteger sGasPrice = gasPrice;
        BigInteger gasLimit = new BigInteger("400000");
//        String recipientAddr = to;
//        BigInteger value = new BigInteger("3500000000");


        RawTransaction rawTrx = RawTransaction.createEtherTransaction(nonce, sGasPrice, gasLimit, recipientAddr, value);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTrx, credentials);
        String hexMessage = Numeric.toHexString(signedMessage);

        try {
            EthSendTransaction ethSendTx;

            ethSendTx = web3.ethSendRawTransaction(hexMessage).sendAsync().get();


            String txHash = ethSendTx.getTransactionHash();

//            TODO implement if, there is an error with the message
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

    public static BigInteger getBallance(Context context, String walletName, String password){

        WalletWrapper walletWrapper = new WalletWrapper();
        Credentials creds = walletWrapper.getWallet(context,walletName, password);
        String address = creds.getAddress();

        try {
            EthGetBalance ethBalance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
            return ethBalance.getBalance();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
//            TODO Add some user warning around this
            System.out.println("Could not load wallets");
            return null;
        }

    }
}
