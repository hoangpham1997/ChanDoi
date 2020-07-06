package vn.softdreams.ebweb.service.Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * @Author Hautv
 */
public class AESCrypt implements ICrypt {

    private static final int PASSWORD_ITERATIONS = 65536; // vs brute force
    private static final int KEY_LENGTH = 256;

    private char[] pass = "password".toCharArray(); // hardcoded or read me from a file
    private byte[] salt = new byte[16]; // for more confusion
    private byte[] ivBytes = null;

    public AESCrypt() {
        //
        // INIT SALT Random
        //

    }

    private void getSaltRandom() {
        SecureRandom secureRandom = new SecureRandom(); // seed is 0
        secureRandom.setSeed(secureRandom.generateSeed(16));
        secureRandom.nextBytes(salt);
    }

    private Cipher createCipher(boolean encryptMode) throws Exception {

        if (!encryptMode && ivBytes == null) {
            throw new IllegalStateException("ivBytes is null");
        }
        if (encryptMode) {
            getSaltRandom();
        }

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(pass, salt, PASSWORD_ITERATIONS, KEY_LENGTH);

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        int mode = encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        if (ivBytes == null) {

            cipher.init(mode, secret);
            AlgorithmParameters params = cipher.getParameters();
            ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();

        } else {

            cipher.init(mode, secret, new IvParameterSpec(ivBytes));
        }

        return cipher;
    }

    @Override
    public String encode(String plainText) throws Exception {

        Cipher cipher = createCipher(true);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        // concatenate salt + iv + ciphertext
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(salt);
        outputStream.write(ivBytes);
        outputStream.write(encryptedBytes);
        return DatatypeConverter.printBase64Binary(outputStream.toByteArray());

    }

    @Override
    public String decode(String encodedText) throws Exception {
        byte[] encodedBytes = DatatypeConverter.parseBase64Binary(encodedText);
        salt = Arrays.copyOfRange(encodedBytes, 0, 16);
        ivBytes = Arrays.copyOfRange(encodedBytes, 16, 32);
        byte[] ct = Arrays.copyOfRange(encodedBytes, 32, encodedBytes.length);
        Cipher cipher = createCipher(false);
        return new String(cipher.doFinal(ct), "UTF-8");
    }

    @Override
    public String encode(String plainText, String password) throws Exception {
        pass = password.toCharArray();
        Cipher cipher = createCipher(true);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        // concatenate salt + iv + ciphertext
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(salt);
        outputStream.write(ivBytes);
        outputStream.write(encryptedBytes);
        return DatatypeConverter.printBase64Binary(outputStream.toByteArray());
    }

    @Override
    public String decode(String encodedText, String password) throws Exception {
        pass = password.toCharArray();
        byte[] encodedBytes = DatatypeConverter.parseBase64Binary(encodedText);
        salt = Arrays.copyOfRange(encodedBytes, 0, 16);
        ivBytes = Arrays.copyOfRange(encodedBytes, 16, 32);
        byte[] ct = Arrays.copyOfRange(encodedBytes, 32, encodedBytes.length);
        Cipher cipher = createCipher(false);
        return new String(cipher.doFinal(ct), "UTF-8");
    }
}
