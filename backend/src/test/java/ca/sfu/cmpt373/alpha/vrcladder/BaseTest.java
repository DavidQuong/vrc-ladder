package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.util.ConfigurationManager;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

    protected SessionManager sessionManager;

    @Before
    public void setUpBase() {
        sessionManager = new SessionManager(new ConfigurationManager());
    }

    @After
    public void tearDownBase() {
        sessionManager.shutDown();
    }

}
