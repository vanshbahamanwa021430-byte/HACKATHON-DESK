package com.hackforge.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Stores AI provider API keys encrypted with AES-256-GCM. The AES key is generated inside the
 * Android Keystore and never leaves it; only ciphertext is written to app-private preferences.
 * Values are never logged and errors never include them.
 */
@CapacitorPlugin(name = "SecureKeys")
public class SecureKeysPlugin extends Plugin {
    private static final String KEYSTORE = "AndroidKeyStore";
    private static final String ALIAS = "hackforge_provider_keys_v1";
    private static final String PREFS = "hackforge_secure_keys";
    private static final String TRANSFORM = "AES/GCM/NoPadding";
    private static final int TAG_BITS = 128;
    private static final Pattern PROVIDER = Pattern.compile("^[a-z]{2,20}$");

    @PluginMethod
    public void get(PluginCall call) {
        String provider = provider(call);
        if (provider == null) return;
        String stored = prefs().getString(provider, null);
        JSObject ret = new JSObject();
        if (stored == null) {
            ret.put("value", JSObject.NULL);
            call.resolve(ret);
            return;
        }
        try {
            ret.put("value", decrypt(stored));
            call.resolve(ret);
        } catch (Exception e) {
            // Undecryptable (e.g. restored from a backup onto another device): drop it and report "no key".
            prefs().edit().remove(provider).apply();
            ret.put("value", JSObject.NULL);
            call.resolve(ret);
        }
    }

    @PluginMethod
    public void set(PluginCall call) {
        String provider = provider(call);
        if (provider == null) return;
        String value = call.getString("value");
        if (value == null || value.trim().isEmpty() || value.length() > 4096) {
            call.reject("Key must be 1 to 4096 characters.", "invalid_value");
            return;
        }
        try {
            prefs().edit().putString(provider, encrypt(value.trim())).apply();
            call.resolve();
        } catch (Exception e) {
            call.reject("Secure storage is unavailable on this device.", "secure_storage_unavailable");
        }
    }

    @PluginMethod
    public void remove(PluginCall call) {
        String provider = provider(call);
        if (provider == null) return;
        prefs().edit().remove(provider).apply();
        call.resolve();
    }

    private String provider(PluginCall call) {
        String p = call.getString("provider");
        if (p == null || !PROVIDER.matcher(p).matches()) {
            call.reject("Unknown provider.", "invalid_provider");
            return null;
        }
        return p;
    }

    private SharedPreferences prefs() {
        return getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private SecretKey secretKey() throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE);
        ks.load(null);
        KeyStore.Entry entry = ks.getEntry(ALIAS, null);
        if (entry instanceof KeyStore.SecretKeyEntry) {
            return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
        }
        KeyGenerator gen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE);
        gen.init(new KeyGenParameterSpec.Builder(ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build());
        return gen.generateKey();
    }

    private String encrypt(String plain) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey());
        byte[] iv = cipher.getIV();
        byte[] ct = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(iv, Base64.NO_WRAP) + ":" + Base64.encodeToString(ct, Base64.NO_WRAP);
    }

    private String decrypt(String stored) throws Exception {
        int sep = stored.indexOf(':');
        if (sep <= 0) throw new IllegalArgumentException("malformed");
        byte[] iv = Base64.decode(stored.substring(0, sep), Base64.NO_WRAP);
        byte[] ct = Base64.decode(stored.substring(sep + 1), Base64.NO_WRAP);
        Cipher cipher = Cipher.getInstance(TRANSFORM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey(), new GCMParameterSpec(TAG_BITS, iv));
        return new String(cipher.doFinal(ct), StandardCharsets.UTF_8);
    }
}
