package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import java.security.Key;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SecurityManager {

    public static final int HASH_WIDTH = 92;

    private static final String HEADER_PROPERTY_TYPE = "typ";
    private static final String HEADER_PROPERTY_ALGORITHM = "alg";

    private static final String CLAIM_PROPERTY_SUBJECT = "sub";
    private static final String CLAIM_PROPERTY_USER_ROLE = "role";
    private static final String CLAIM_PROPERTY_EXPIRY_TIME = "exp";

    private static final String TOKEN_TYPE = "JWT";
    private static final int SECONDS_CONVERSION_DIVIDER = 1000;
    private static final int SESSION_LENGTH_IN_SECONDS = 3600;

    private PasswordService passwordService;
    private SignatureAlgorithm signatureAlgorithm;
    private Key signatureKey;

    public SecurityManager(SignatureAlgorithm signatureAlgorithm, Key signatureKey) {
        this.passwordService = new DefaultPasswordService();
        this.signatureAlgorithm = signatureAlgorithm;
        this.signatureKey = signatureKey;
    }

    public String login(User user, String plaintextPassword) {
        Password password = user.getPassword();

        if (!doesPasswordMatch(plaintextPassword, password)) {
            throw new AuthenticationException();
        }

        return createAuthorizationToken(user);
    }

    public Password hashPassword(String plaintext) {
        String passwordHash = passwordService.encryptPassword(plaintext);

        return new Password(passwordHash);
    }

    private boolean doesPasswordMatch(String plaintext, Password password) {
        return passwordService.passwordsMatch(plaintext, password.getHash());
    }

    private String createAuthorizationToken(User user) {
        return Jwts.builder()
            .setHeader(createHeader())
            .setClaims(createClaims(user))
            .signWith(signatureAlgorithm, signatureKey)
            .compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> headers = new HashMap<>();

        headers.put(HEADER_PROPERTY_TYPE, TOKEN_TYPE);
        headers.put(HEADER_PROPERTY_ALGORITHM, signatureAlgorithm.name());

        return headers;
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();

        UserId userId = user.getUserId();
        claims.put(CLAIM_PROPERTY_SUBJECT, userId.getValue());

        UserRole userRole = user.getUserRole();
        claims.put(CLAIM_PROPERTY_USER_ROLE, userRole.name());

        long expirationTime = (Calendar.getInstance().getTimeInMillis() / SECONDS_CONVERSION_DIVIDER) +
            SESSION_LENGTH_IN_SECONDS;
        claims.put(CLAIM_PROPERTY_EXPIRY_TIME, expirationTime);

        return claims;
    }

}
