package com.example.filip.ethwalletp5.API;
import com.google.gson.annotations.SerializedName;


public class Models {

    public static class Backup extends Models {
        @SerializedName("id") String id;
        @SerializedName("password") String password;
        @SerializedName("wallet_file") String walletFileAsString;

        public void setId(String id) {
            this.id = id;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setWalletFileAsString(String walletFileAsString) {
            this.walletFileAsString = walletFileAsString;
        }
    }
}
