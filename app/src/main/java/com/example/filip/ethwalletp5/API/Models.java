package com.example.filip.ethwalletp5.API;
import com.google.gson.annotations.SerializedName;


public class Models {

    public static class Backup extends Models {
        public Backup(String emailHash, String emailPassHash, String walletFileAsString) {
            this.id = emailHash;
            this.password = emailPassHash;
            this.wallet_file = walletFileAsString;
        }

        public Backup(String emailHash, String emailPassHash) {
            this.id = emailHash;
            this.password = emailPassHash;
        }

        @SerializedName("id") String id;
        @SerializedName("password") String password;
        @SerializedName("wallet_file") String wallet_file;

        public void setId(String id) {
            this.id = id;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setWalletFile(String walletFileAsString) {
            this.wallet_file = walletFileAsString;
        }

        public String getWallet_file() {
            return wallet_file;
        }
    }
}
