package com.example.filip.ethwalletp5.API;
import com.google.gson.annotations.SerializedName;


public class Models {

    public static class Backup extends Models {
        @SerializedName("id") String id;
        @SerializedName("password") String password;
        @SerializedName("priv_key") String encryptedKey;

        public void setId(String id) {
            this.id = id;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setEncryptedKey(String encryptedKey) {
            this.encryptedKey = encryptedKey;
        }
    }
}
