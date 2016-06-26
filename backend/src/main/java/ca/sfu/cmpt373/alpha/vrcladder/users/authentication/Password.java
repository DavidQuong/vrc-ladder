package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {

    private static final int HASH_CODE_PRIME = 31;

    @Column(name = PersistenceConstants.COLUMN_PASSWORD_HASH, nullable = false)
    private String hash;

    @Column(name = PersistenceConstants.COLUMN_PASSWORD_SALT, nullable = false)
    private String salt;


    public Password() {
        // Required by Hibernate.
    }

    public Password(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }

        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }

        Password otherPassword = (Password) otherObject;

        if (hash != null ? !hash.equals(otherPassword.hash) : otherPassword.hash != null)  {
            return false;
        }

        return (salt != null ? salt.equals(otherPassword.salt) : otherPassword.salt == null);
    }

    @Override
    public int hashCode() {
        int result = hash != null ? hash.hashCode() : 0;
        result = HASH_CODE_PRIME * result + (salt != null ? salt.hashCode() : 0);
        return result;
    }
}
