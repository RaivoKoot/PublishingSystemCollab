package helpers;

import exceptions.NoDigitInPasswordException;
import exceptions.PasswordToLongException;
import exceptions.PasswordTooShortException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {

    private static MessageDigest encryptor;

    private final static int MINIMUM_LENGTH = 8;

    private final static int PASSWORD_FORCED_LENGTH = 20;
    private final static char SALT_CHARACTER = '0';

    public static String encryptPassword(String input) throws NoSuchAlgorithmException, NoDigitInPasswordException, PasswordTooShortException, PasswordToLongException {
        checkPasswordValidity(input);

        input = addSalt(input);

        byte[] hash = encryptString(input);

        return bytesToString(hash);
    }

    private static String addSalt(String input) {
        StringBuilder password = new StringBuilder(input);

        while(password.length() != PASSWORD_FORCED_LENGTH){
            password.append(SALT_CHARACTER);
        }

        return password.toString();
    }

    private static byte[] encryptString(String input) throws NoSuchAlgorithmException {
        encryptor = MessageDigest.getInstance("SHA-256");

        return encryptor.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /*
    Taken from StackOverflow
     */
    private static String bytesToString(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static void checkPasswordValidity(String input) throws PasswordTooShortException, PasswordToLongException, NoDigitInPasswordException {
        if(input.length() < MINIMUM_LENGTH)
            throw new PasswordTooShortException();

        if(input.length()>PASSWORD_FORCED_LENGTH)
            throw new PasswordToLongException();

        if(!input.matches(".*\\d.*"))
            throw new NoDigitInPasswordException();

    }
}
