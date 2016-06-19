package ca.sfu.cmpt373.alpha.vrcrest;

import spark.servlet.SparkApplication;


/**
 * A class for deploying Spark on Servers/Servlet containers such as TomCat or GlassFish
 * This is used primarily for Amazon Web Services Deployment
 */
public class RestApplication implements SparkApplication {

    @Override
    public void init() {
        RestSample.initRoutes();
    }

    @Override
    public void destroy() {

    }
}
