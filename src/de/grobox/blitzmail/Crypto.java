package de.grobox.blitzmail;

import android.content.Context;
import android.provider.Settings;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

class Crypto {

	private Context context;
	private static final String UTF8 = "utf-8";

	Crypto(Context context) {
		this.context = context;
	}

	@SuppressWarnings("deprecation")
	String encrypt( String value ) {
		try {
			final byte[] bytes = value!=null ? value.getBytes(UTF8) : new byte[0];
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PrivateConstants.CryptoKey));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(),Settings.System.ANDROID_ID).getBytes(UTF8), 20));
			return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP),UTF8);

		} catch( Exception e ) {
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings("deprecation")
	String decrypt(String value){
		try {
			final byte[] bytes = value!=null ? Base64.decode(value,Base64.DEFAULT) : new byte[0];
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PrivateConstants.CryptoKey));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(),Settings.System.ANDROID_ID).getBytes(UTF8), 20));
			return new String(pbeCipher.doFinal(bytes),UTF8);

		} catch( Exception e) {
			throw new RuntimeException(e);
		}
	}
}
