package com.example.filip.ethwalletp5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.filip.ethwalletp5.Crypto.AddressBook;
import com.example.filip.ethwalletp5.Crypto.web3jWrapper;

import java.security.KeyPair;
import java.security.Security;

public class MainActivity extends AppCompatActivity {

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        System.out.println("Added security provider");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myButton = findViewById(R.id.button1);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You're ugly.", Toast.LENGTH_SHORT).show();

                try {
                    System.out.println("Button Pressed");
                    doCryptoMagic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void doCryptoMagic() throws Exception {


//        DISABLED FOR TESTING START

        AddressBook newBook = new AddressBook();
        KeyPair myKeyPair = newBook.generateAddressPair();

        web3jWrapper.createWallet(myKeyPair, getApplicationContext());


//        END

        System.out.println("Result: " + web3jWrapper.sendTransaction());

//        TODO If WRAPPER_ERROR, display user message
//        TODO If WRAPPER_SUCCESS, proceed
//        TODO offload work from main thread
//        TOdO Display loading dialogs



    }


}