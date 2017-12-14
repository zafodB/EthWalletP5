package com.example.filip.ethwalletp5;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.filip.ethwalletp5.Crypto.AddressBook;
import com.example.filip.ethwalletp5.Crypto.WalletWrapper;
import com.example.filip.ethwalletp5.Crypto.web3jWrapper;
import com.example.filip.ethwalletp5.UI.EnterPinFragment;
import com.example.filip.ethwalletp5.UI.FrontPageFragment;

import java.security.KeyPair;
import java.security.Security;

public class MainActivity extends Activity implements FragmentChangerClass.FragmentChanger {

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        System.out.println("Added security provider");
    }

    private static String userPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EnterPinFragment enterPinFrag = new EnterPinFragment();


        getFragmentManager().beginTransaction()
                .addToBackStack(enterPinFrag.toString())
                .replace(R.id.fragment_container, enterPinFrag).commit();
//        }

//        Button myButton = findViewById(R.id.button1);
//
//        myButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "You're ugly.", Toast.LENGTH_SHORT).show();
//
//                try {
//                    System.out.println("Button Pressed");
//                    doCryptoMagic();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        FragmentManager fm = getFragmentManager();

        userPin = "asdfghjk";

    }

    @Override
    public void ChangeFragments(Fragment newFragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(newFragment.toString())
                .commit();
    }

    void doCryptoMagic() throws Exception {


        KeyPair myKeyPair = AddressBook.generateAddressPair();

        //TODO remove test
        String testname = "test";

        WalletWrapper.createWallet(testname, myKeyPair, getApplicationContext());

//

//        System.out.println("Result: " + web3jWrapper.sendTransaction(getApplicationContext()));


//        TODO If WRAPPER_ERROR, display user message
//        TODO If WRAPPER_SUCCESS, proceed
//        TODO offload work from main thread
//        TOdO Display loading dialogs


    }

    public static String getUserPin(){
        return userPin;
    }


}