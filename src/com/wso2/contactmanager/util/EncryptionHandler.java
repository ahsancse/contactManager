package com.wso2.contactmanager.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

public class EncryptionHandler {

	private static Cipher ecipher;
	private static Cipher dcipher;
	private static SecretKey key;
	

	/*public static void main(String[] args) {
		createSecretKey();
		String encrypted = encrypt("This is a classified message!");
		String decrypted = decrypt(encrypted);
		System.out.println("Decrypted: " + decrypted);

	}*/
	
	public void createSecretKey(){
		try {
	        key = KeyGenerator.getInstance("DES").generateKey();
	        ecipher = Cipher.getInstance("DES");
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher = Cipher.getInstance("DES");
			dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
        } catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
	}

	

	public String encrypt(String str) {

		try {
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = ecipher.doFinal(utf8);
			// encode to base64
			enc = BASE64EncoderStream.encode(enc);
			return new String(enc);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	  }

	public  String decrypt(String str) {

		try {
			// decode with base64 to get bytes
			byte[] dec = BASE64DecoderStream.decode(str.getBytes());
			byte[] utf8 = dcipher.doFinal(dec);
			// create new string based on the specified charset
			return new String(utf8, "UTF8");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;

    }

}

