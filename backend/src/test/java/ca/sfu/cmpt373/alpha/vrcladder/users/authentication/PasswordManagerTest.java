package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PasswordManagerTest {

    private static final String TEST_PASSWORD = "vrcpass1234";

    private PasswordManager passwordManager;

    @Before
    public void setUp() {
        passwordManager = new PasswordManager();
    }

    @Test
    public void testHashPassword() {
        Password password = passwordManager.hashPassword(TEST_PASSWORD);

        String hash = password.getHash();
        Assert.assertNotEquals(TEST_PASSWORD, hash);
        Assert.assertEquals(PasswordManager.HASH_WIDTH, hash.length());
    }

    @Test
    public void testMatchPassword() {
        Password password = passwordManager.hashPassword(TEST_PASSWORD);

        boolean doesPasswordMatch = passwordManager.doesPasswordMatch(TEST_PASSWORD, password);
        Assert.assertTrue(doesPasswordMatch);
    }

}
