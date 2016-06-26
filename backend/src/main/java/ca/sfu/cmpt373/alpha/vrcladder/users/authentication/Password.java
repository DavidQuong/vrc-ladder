package ca.sfu.cmpt373.alpha.vrcladder.users.authentication;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {

    @Column(name = PersistenceConstants.COLUMN_PASSWORD_HASH, nullable = false)
    private String hash;

    public Password() {
        // Required by Hibernate.
    }

    public Password(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        Password otherPassword = (Password) otherObj;

        return hash.equals(otherPassword.hash);
    }

    @Override
    public int hashCode() {
        return hash.hashCode();
    }
}
