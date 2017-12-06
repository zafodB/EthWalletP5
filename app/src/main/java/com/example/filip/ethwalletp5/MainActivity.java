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

        AddressBook newBook = new AddressBook();
        KeyPair myKeyPair = newBook.generateAddressPair();

        System.out.println("11111111111111111111");
        web3jWrapper.createWallet(myKeyPair, getApplicationContext());
        System.out.println("2222222222222222222222");
    }


}