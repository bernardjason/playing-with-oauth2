package org.bjason.oauth2;

/**
 * original author @author jdlee see http://blogs.steeplesoft.com/posts/2013/a-simple-oauth2-client-and-server-example-part-i.html
 */
public class Common {
    public static String CLIENT_ID = "oauth2test";
    public static String CLIENT_SECRET = "oauth2clientsecret";
    public static String AUTHORIZATION_CODE = "oauth2authcode";
    public static String USERNAME = "user";
    public static String PASSWORD = "pass";
    public static String RESOURCE_SERVER_NAME = "demo";
    public static final String ACCESS_TOKEN_VALID = "access_token_valid";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_HEADER_OAUTH2 = "Bearer " + ACCESS_TOKEN_VALID;
	public final static long SIXTY_SECONDS = 60L;
	public final static long FIVE_MINUTES = 3600L;
	public final static long THIRTY_SECONDS = 30L;

}
