package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

public class PasswordManager {

    public static final int HASH_WIDTH = 92;

    private PasswordService passwordService;

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
