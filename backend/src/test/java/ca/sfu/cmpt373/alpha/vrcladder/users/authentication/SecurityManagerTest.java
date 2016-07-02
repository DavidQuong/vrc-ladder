package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockUserGenerator;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;

public class SecurityManagerTest {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final Key SIGNATURE_KEY = MacProvider.generateKey();
    private static final String TEST_PASSWORD = "vrcpass1234";
    private static final String TEST_INVALID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxND" +
        "Y3MzI0OTUxfQ.E5PL4RaEBOaPgVr5oFs1Q94mSvO9TGFGEYsdzVSySu8";

    private SecurityManager securityManager;
    private User userFixture;

    @Before
    public void setUp() {
        securityManager = new SecurityManager(SIGNATURE_ALGORITHM, SIGNATURE_KEY);

        userFixture = MockUserGenerator.generatePlayer();
        Password mockPassword = securityManager.hashPassword(TEST_PASSWORD);
        userFixture.setPassword(mockPassword);
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
        String authorizationToken = securityManager.login(userFixture, TEST_PASSWORD);

        Assert.assertNotNull(authorizationToken);
    }

    @Test(expected = AuthenticationException.class)
    public void testLoginFailed() {
        User mockUser = MockUserGenerator.generatePlayer();
        securityManager.login(mockUser, TEST_PASSWORD);
    }

    @Test
    public void testValidToken() {
        String authorizationToken = securityManager.login(userFixture, TEST_PASSWORD);

        UserId userId = securityManager.parseToken(authorizationToken);
        Assert.assertNotNull(userId);
        Assert.assertEquals(userFixture.getUserId(), userId);
    }

    @Test(expected = AuthorizationException.class)
    public void testInvalidToken() {
        securityManager.parseToken(TEST_INVALID_TOKEN);
    }

    @Test(expected = AuthorizationException.class)
    public void testMalformedToken() {
        securityManager.parseToken(TEST_PASSWORD);
    }

}
