package org.bjason.oauth2;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.request.OAuthRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

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


	
	public static  boolean checkClientIdandSecret(OAuthRequest request) {
		if ( CLIENT_ID.equals(request.getClientId()) == false || CLIENT_SECRET.equals(request.getClientSecret()) == false  ){
			return false;
		}

		return true;
	}
	public static Response unauthorisedResponse() throws OAuthSystemException {
		OAuthResponse response =
                OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT).setErrorDescription("client_id/client_secret bad")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
	}
	
}
