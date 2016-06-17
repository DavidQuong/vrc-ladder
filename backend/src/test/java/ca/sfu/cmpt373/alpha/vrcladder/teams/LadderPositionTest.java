package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import org.junit.Assert;
import org.junit.Test;

public class LadderPositionTest {

    private static final Integer VALID_POSITION_1 = 1;
    private static final Integer VALID_POSITION_2 = 50;
    private static final Integer INVALID_POSITION_1 = 0;
    private static final Integer INVALID_POSITION_2 = -25;

    @Test
    public void testValidLadderPosition1() {
        LadderPosition ladderPosition = new LadderPosition(VALID_POSITION_1);

        Assert.assertEquals(VALID_POSITION_1, ladderPosition.getValue());
    }

    @Test
    public void testValidLadderPosition2() {
        LadderPosition ladderPosition = new LadderPosition(VALID_POSITION_2);

        Assert.assertEquals(VALID_POSITION_2, ladderPosition.getValue());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidLadderPosition1() {
        LadderPosition ladderPosition = new LadderPosition(INVALID_POSITION_1);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidLadderPosition2() {
        LadderPosition ladderPosition = new LadderPosition(INVALID_POSITION_2);
    }

    @Test
    public void testLadderPositionEquals() {
        LadderPosition ladderPosition1 = new LadderPosition(VALID_POSITION_1);
        LadderPosition ladderPosition2 = new LadderPosition(VALID_POSITION_1);

        Assert.assertTrue(ladderPosition1.equals(ladderPosition2));
    }


    @Test
    public void testLadderPositionNotEquals() {
        LadderPosition ladderPosition1 = new LadderPosition(VALID_POSITION_1);
        LadderPosition ladderPosition2 = new LadderPosition(VALID_POSITION_2);

        Assert.assertFalse(ladderPosition1.equals(ladderPosition2));
    }

    @Test
    public void testLadderPositionCompareToEquals() {
        LadderPosition ladderPosition1 = new LadderPosition(VALID_POSITION_1);
        LadderPosition ladderPosition2 = new LadderPosition(VALID_POSITION_1);

        Assert.assertTrue(ladderPosition1.compareTo(ladderPosition2) == 0);
    }

    @Test
    public void testLadderPositionCompareToLesser() {
        LadderPosition ladderPosition1 = new LadderPosition(VALID_POSITION_1);
        LadderPosition ladderPosition2 = new LadderPosition(VALID_POSITION_2);

        Assert.assertTrue(ladderPosition1.compareTo(ladderPosition2) == -1);
    }

    @Test
    public void testLadderPositionCompareToGreater() {
        LadderPosition ladderPosition1 = new LadderPosition(VALID_POSITION_1);
        LadderPosition ladderPosition2 = new LadderPosition(VALID_POSITION_2);

        Assert.assertTrue(ladderPosition2.compareTo(ladderPosition1) == 1);
    }

}
