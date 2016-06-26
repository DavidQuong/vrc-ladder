package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PasswordManagerTest {

    private PasswordManager passwordManager;

    @Before
    public void setUp() {
        passwordManager = new PasswordManager();
    }

    @Test
    public void testHashPassword() {
        final String testPassword = "vrcpass1234";

        Password password = passwordManager.hashPassword(testPassword);

        String hash = password.getHash();
        Assert.assertEquals(PasswordManager.HASH_WIDTH, hash.length());

        String salt = password.getSalt();
        Assert.assertEquals(PasswordManager.SALT_WIDTH, salt.length());
    }

    @Test
    public void testShiro() {
        SecurityManager securityManager = new IniSecurityManagerFactory().getInstance();
    }

}
