package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.crypto.hash.Hash;

public class PasswordManager {

    public static final int HASH_WIDTH = 44;
    public static final int SALT_WIDTH = 24;

    private HashingPasswordService passwordService;
    private HashedCredentialsMatcher credentialsMatcher;

    public PasswordManager() {
        passwordService = new DefaultPasswordService();
        credentialsMatcher = new HashedCredentialsMatcher();

    }

    public Password hashPassword(String plaintext) {
        Hash passwordHash = passwordService.hashPassword(plaintext);
        String passwordHashBase64 = passwordHash.toBase64();
        String saltBase64 = passwordHash.getSalt().toBase64();

        return new Password(passwordHashBase64, saltBase64);
    }

}
