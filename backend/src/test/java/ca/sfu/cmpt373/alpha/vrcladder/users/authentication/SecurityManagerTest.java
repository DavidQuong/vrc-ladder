package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SecurityManagerTest {

    private static final String TEST_PASSWORD = "vrcpass1234";

    private SecurityManager securityManager;

    @Before
    public void setUp() {
        securityManager = new SecurityManager();
    }

    @Test
    public void testHashPassword() {
        Password password = securityManager.hashPassword(TEST_PASSWORD);

        String hash = password.getHash();
        Assert.assertNotEquals(TEST_PASSWORD, hash);
        Assert.assertEquals(SecurityManager.HASH_WIDTH, hash.length());
    }

    @Test
    public void testHashPasswordTwice() {
        Password firstPassword = securityManager.hashPassword(TEST_PASSWORD);
        Password secondPassword = securityManager.hashPassword(TEST_PASSWORD);

        Assert.assertNotEquals(firstPassword, secondPassword);
    }

    @Test
    public void testMatchPassword() {
        Password password = securityManager.hashPassword(TEST_PASSWORD);

        boolean doesPasswordMatch = securityManager.doesPasswordMatch(TEST_PASSWORD, password);
        Assert.assertTrue(doesPasswordMatch);
    }

}
