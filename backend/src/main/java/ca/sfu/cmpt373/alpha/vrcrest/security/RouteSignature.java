package ca.sfu.cmpt373.alpha.vrcrest.security;

import spark.Request;
import spark.route.HttpMethod;

public class RouteSignature {

    private String route;
    private HttpMethod method;

    public RouteSignature(String route, HttpMethod method) {
        this.route = route;
        this.method = method;
    }

    public RouteSignature(Request request) {
        route = request.uri();
        method = HttpMethod.get(request.requestMethod().toLowerCase());
    }

    public String getRoute() {
        return route;
    }

    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        RouteSignature otherRouteSignature = (RouteSignature) otherObj;

        if (!route.equals(otherRouteSignature.route)) {
            return false;
        }

        return (method == otherRouteSignature.method);
    }

    @Override
    public int hashCode() {
        int result = route.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }
}
