package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

    protected SessionManager sessionManager;

    @Before
    public void setUpBase() {
        sessionManager = new SessionManager();
    }

    @After
    public void tearDownBase() {
        sessionManager.shutDown();
    }

}
