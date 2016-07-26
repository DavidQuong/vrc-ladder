package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationException;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecurityManager {

    public static final int HASH_WIDTH = 92;
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid credentials (user ID or password).";

    private static final String ERROR_INVALID_TOKEN = "The provided authorization token is not valid";
    private static final String ERROR_EXPIRED_TOKEN = "The provided authorization token has expired.";

    private static final String HEADER_PROPERTY_TYPE = "typ";
    private static final String HEADER_PROPERTY_ALGORITHM = "alg";

    private static final String ERROR_FORMAT_INVALID_AUTHORIZATION = "This user is not authorized to %s.";

    private static final String TOKEN_TYPE = "JWT";
    private static final int TTL_IN_SECONDS = 3600;
    private static final int MILLISECONDS_TO_SECONDS_MULTIPLIER = 1000;

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
            throw new AuthenticationException(ERROR_INVALID_CREDENTIALS);
        }

        return createAuthorizationToken(user);
    }

    /**
     * Validate the provided authorization token by first parsing it to a JSON web token format.
     * The expiration time of the JSON web token is then validated (not expired), the subject
     * (User ID) is then returned if still valid.
     */
    public UserId parseToken(String authorizationToken) {
        Jwt jsonWebToken;
        try {
            jsonWebToken = parseAuthorizationToken(authorizationToken);
        } catch (ExpiredJwtException ex) {
            throw new AuthorizationException(ERROR_EXPIRED_TOKEN);
        } catch (Exception ex) {
            throw new AuthorizationException(ERROR_INVALID_TOKEN);
        }

        Claims containedClaims = (Claims) jsonWebToken.getBody();
        String subject = containedClaims.getSubject();

        return new UserId(subject);
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

    private Claims createClaims(User user) {
        Claims claims = new DefaultClaims();

        UserId userId = user.getUserId();
        claims.setSubject(userId.getValue());

        Date expirationDate = new Date();
        expirationDate.setTime(expirationDate.getTime() + (TTL_IN_SECONDS * MILLISECONDS_TO_SECONDS_MULTIPLIER));
        claims.setExpiration(expirationDate);

        return claims;
    }

    private Jwt parseAuthorizationToken(String authorizationToken) {
        return Jwts.parser()
            .setSigningKey(signatureKey)
            .parse(authorizationToken);
    }

}