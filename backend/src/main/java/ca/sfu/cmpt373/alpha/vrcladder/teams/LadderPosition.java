package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LadderPosition implements Comparable<LadderPosition> {

    public static final Integer FIRST_POSITION = 1;
    private static final String INVALID_POSITION_FORMAT = "Invalid valid rank ladderPosition: %s.";

    private Integer ladderPosition;

    private LadderPosition() {
        // Required by Hibernate.
    }

    public LadderPosition(Integer position) {
        if (position <= 0) {
            String errorMsg = String.format(INVALID_POSITION_FORMAT, position);
            throw new ValidationException(errorMsg);
        }

        this.ladderPosition = position;
    }

    @Column(name = PersistenceConstants.COLUMN_LADDER_POSITION, nullable = false, unique = true)
    public Integer getLadderPosition() {
        return ladderPosition;
    }

    public void setLadderPosition(Integer ladderPosition) {
        this.ladderPosition = ladderPosition;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        LadderPosition otherLadderPosition = (LadderPosition) otherObj;

        return ladderPosition.equals(otherLadderPosition.ladderPosition);
    }

    @Override
    public int hashCode() {
        return ladderPosition.hashCode();
    }

    @Override
    public int compareTo(LadderPosition ladderPosition) {
        return this.ladderPosition.compareTo(ladderPosition.ladderPosition);
    }

}
