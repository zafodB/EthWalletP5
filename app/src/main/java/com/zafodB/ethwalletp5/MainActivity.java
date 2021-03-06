package com.zafodB.ethwalletp5;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.zafodB.ethwalletp5.UI.EnterPinFragment;

import java.security.Provider;
import java.security.Security;
import java.util.Set;

public class MainActivity extends Activity implements FragmentChangerClass.FragmentChanger {

    public static final String TAG = "userValidTag";

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

//        TODO If WRAPPER_ERROR, display user message
//        TODO If WRAPPER_SUCCESS, proceed
//        TODO offload work from main thread
//        TODO Display loading dialogs


    /**
     * This method doesn't do anything anymore. It was the first method in the app and
     * can't be removed due to huge sentimental value
     * to the lead Android programmer.
     *
     * @throws Exception
     */
    void doCryptoMagic() throws Exception {

    }

    public static String getUserPin() {
        return userPin;
    }

    public static void setUserPin(String pin){
        userPin = pin;
    }

}