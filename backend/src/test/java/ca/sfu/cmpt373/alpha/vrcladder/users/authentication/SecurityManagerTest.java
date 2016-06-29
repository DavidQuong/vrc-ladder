package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockUserGenerator;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.shiro.authc.AuthenticationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;

public class SecurityManagerTest {

    private static final String TEST_PASSWORD = "vrcpass1234";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final Key SIGNATURE_KEY = MacProvider.generateKey();

    private SecurityManager securityManager;

    @Before
    public void setUp() {
        securityManager = new SecurityManager(SIGNATURE_ALGORITHM, SIGNATURE_KEY);
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
    public void testLoginSuccessful() {
        User mockUser = MockUserGenerator.generatePlayer();
        Password mockPassword = securityManager.hashPassword(TEST_PASSWORD);
        mockUser.setPassword(mockPassword);

        String authorizationToken = securityManager.login(mockUser, TEST_PASSWORD);
        Assert.assertNotNull(authorizationToken);
    }

    @Test(expected = AuthenticationException.class)
    public void testLoginFailed() {
        User mockUser = MockUserGenerator.generatePlayer();

        securityManager.login(mockUser, TEST_PASSWORD);
    }

}
