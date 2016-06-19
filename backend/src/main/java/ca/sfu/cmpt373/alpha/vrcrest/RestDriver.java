package ca.sfu.cmpt373.alpha.vrcrest;

/**
 * A class for using the Spark REST API with the embedded Jetty server
 * This is useful for rapid development, testing, and debugging
 */
public class RestDriver {

    public static void main(String[] args) {
        RestApplication restApplication = new RestApplication();
        restApplication.init();
    }
}
