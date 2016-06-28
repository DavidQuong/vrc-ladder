package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

public class PasswordManager {

    public static final int HASH_WIDTH = 92;

    private PasswordService passwordService;

    public PasswordManager() {
        passwordService = new DefaultPasswordService();
    }

    public void login(User user, String plaintextPassword) {
        Password password = user.getPassword();

        if (!doesPasswordMatch(plaintextPassword, password)) {
            throw new AuthenticationException();
        }
    }

    public Password hashPassword(String plaintext) {
        String passwordHash = passwordService.encryptPassword(plaintext);

        return new Password(passwordHash);
    }

    public boolean doesPasswordMatch(String plaintext, Password password) {
        return passwordService.passwordsMatch(plaintext, password.getHash());
    }

}
