package com.example.filip.ethwalletp5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.spongycastle.jcajce.provider.digest.Keccak;
import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.interfaces.ECPrivateKey;
import org.spongycastle.jce.interfaces.ECPublicKey;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.jce.spec.ECPublicKeySpec;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.encoders.Hex;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

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




        System.out.printf("\nPublic Key generated X\n" + Hex.toHexString(publicKeyExpected.getQ().getAffineXCoord().getEncoded()));
        System.out.printf("\nPublic Key generated Y\n" + Hex.toHexString(publicKeyExpected.getQ().getAffineYCoord().getEncoded()));

        byte[] publicraw = concatByteArrays(publicKeyExpected.getQ().getAffineXCoord().getEncoded(), publicKeyExpected.getQ().getAffineYCoord().getEncoded());

        Keccak.Digest256 hashed = new Keccak.Digest256();

        byte[] result = hashed.digest(publicraw);

        System.out.printf("\nHashed\n" + Hex.toHexString(result));
        System.out.println();

    }

    private byte[] concatByteArrays(byte[] a, byte[] b){
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }
}