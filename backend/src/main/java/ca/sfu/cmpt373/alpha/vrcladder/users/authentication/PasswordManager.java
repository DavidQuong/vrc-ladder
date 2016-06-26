package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.crypto.hash.format.DefaultHashFormatFactory;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.mgt.SecurityManager;

public class PasswordManager {

    public static final int HASH_WIDTH = 92;

    private HashingPasswordService passwordService;

    public PasswordManager() {
        passwordService = new DefaultPasswordService();
    }

    public Password hashPassword(String plaintext) {
        String passwordHash = passwordService.encryptPassword(plaintext);

        return new Password(passwordHash);
    }

    public boolean doesPasswordMatch(String plaintext, Password password) {
        return passwordService.passwordsMatch(plaintext, password.getHash());
    }

}
