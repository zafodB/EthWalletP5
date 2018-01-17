package com.zafodB.ethwalletp5.Crypto;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zafodB.ethwalletp5.FragmentChangerClass;
import com.zafodB.ethwalletp5.MainActivity;
import com.zafodB.ethwalletp5.UI.WalletOpenFragment;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.concurrent.ExecutionException;

/**
 * Created by filip on 06/12/2017.
 */

public class web3jWrapper {

    final static Web3j web3;

    public static final int WRAPPER_ERROR = 44;
    public static final int WRAPPER_SUCCESS = 45;

    public static String getTxHash() {
        return txHash;
    }

    static String txHash;

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

    public static int sendTransaction(Context context, String from, String to, String gasPrice, String valueInEth) {

        Credentials credentials = new WalletWrapper().getWalletCredentials(context, from, MainActivity.getUserPin());

        BigInteger nonce = getNonce(credentials.getAddress());
        if (nonce == null) {
            System.out.println("Could not load nonce.");
            Log.e(AddressBook.TAG_SECURITY, "Could not load nonce.");
            return WRAPPER_ERROR;
        }

        System.out.printf("\nNonce for address: %s \nis this: %d\n", credentials.getAddress(), nonce);

        String recipientAddr = "0xC589a27fCC1b1De994cEE8910c33FF74E2Dd649E";

        BigInteger sGasPrice = gWeiToWei(gasPrice);
        BigInteger gasLimit = new BigInteger("21000");
        BigInteger value = ethToWei(valueInEth);

        RawTransaction rawTrx = RawTransaction.createEtherTransaction(nonce, sGasPrice, gasLimit, recipientAddr, value);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTrx, credentials);
        String hexMessage = Numeric.toHexString(signedMessage);


        try {
            EthSendTransaction ethSendTx;
            ethSendTx = web3.ethSendRawTransaction(hexMessage).sendAsync().get();

            if (!ethSendTx.hasError()) {
                txHash = ethSendTx.getTransactionHash();
                System.out.println("Transaction hash is:" + txHash);

                return WRAPPER_SUCCESS;
            } else {
                Toast.makeText(context, "There was an error with following details: \"" + ethSendTx.getError().getMessage() + "\"", Toast.LENGTH_LONG).show();

                Log.e(MainActivity.TAG, "There was an error:" + ethSendTx.getError().getMessage() + "\n" + ethSendTx.getError().getData());

                return WRAPPER_ERROR;
            }

        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(context, "There was an unspecified error. Please try again.", Toast.LENGTH_LONG).show();
            Log.e(MainActivity.TAG, "Exception was thrown.");
            Log.e(MainActivity.TAG, e.getMessage());
            e.printStackTrace();
            return WRAPPER_ERROR;
        }
    }

    private static BigInteger getNonce(String address) {

        EthGetTransactionCount trxCount;
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

    public static BigInteger getBallance(String publicKey) {

        String prefix = "0x";
        String address = prefix.concat(publicKey);
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

    public static BigInteger ethToWei(String amount) {
        double ethAmount = Double.parseDouble(amount);
        ethAmount = ethAmount * 1000000 * 1000000 * 1000000;

        return new BigDecimal(ethAmount).toBigInteger();

    }

    public static BigInteger gWeiToWei(String amount) {
        double gweiAmount = Double.parseDouble(amount);
        gweiAmount = gweiAmount * 1000000000;

        return new BigDecimal(gweiAmount).toBigInteger();

    }

}
