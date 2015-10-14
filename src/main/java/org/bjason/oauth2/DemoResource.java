package org.bjason.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.bjason.oauth2.Database.TOKEN_STATE;
import org.springframework.beans.factory.annotation.Autowired;


@Path("/demo")
public class DemoResource {
    @Autowired
    private Database database;

    @Path("/1")
    @GET()
    @Produces("text/html")
    public Response get1(@Context HttpServletRequest request) throws OAuthSystemException {
        return commonResource("resource1",request);
    }
    @Path("/2")
    @GET()
    @Produces("text/html")
    public Response get2(@Context HttpServletRequest request) throws OAuthSystemException {
        return commonResource("resource2",request);
    }
    @Path("/3")
    @GET()
    @Produces("text/html")
    public Response get3(@Context HttpServletRequest request) throws OAuthSystemException {
        return commonResource("resource3",request);
    }

	private Response commonResource(String what, HttpServletRequest request)
			throws OAuthSystemException {
		try {
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
            String accessToken = oauthRequest.getAccessToken();
           
            TOKEN_STATE ts = database.isValidAccessToken(accessToken);
            if ( ts == TOKEN_STATE.EXPIRED) {
            	 return errorResponse(OAuthError.ResourceResponse.EXPIRED_TOKEN);
            }  else if ( ts == TOKEN_STATE.BAD ) {
            	return errorResponse(OAuthError.ResourceResponse.INVALID_TOKEN);
            } else   if ( database.getAccessToken(accessToken).allowedTo(what) == false ) {
            	return errorResponse("permission denied");
            }

            return Response.status(Response.Status.OK).entity("ALL OK "+what+" "+System.currentTimeMillis()+" "+accessToken).build();
        } catch (OAuthProblemException e) {
            // Check if the error code has been set
            String errorCode = e.getError();
            if (OAuthUtils.isEmpty(errorCode)) {
            	return errorResponse(null);
            }

            OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm(Common.RESOURCE_SERVER_NAME)
                    .setError(e.getError())
                    .setErrorDescription(e.getDescription())
                    .setErrorUri(e.getUri())
                    .buildHeaderMessage();

            return Response.status(Response.Status.BAD_REQUEST)
                    .header(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
                    .build();
        }
	}
	private Response errorResponse(String errorCode) throws OAuthSystemException {
		OAuthErrorResponseBuilder builder =  OAuthRSResponse
		         .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
		         .setRealm(Common.RESOURCE_SERVER_NAME);
		if ( errorCode != null ) builder.setError(errorCode);
		
		 //return Response.status(Response.Status.UNAUTHORIZED).build();
		 return Response.status(Response.Status.UNAUTHORIZED)
		         .header(OAuth.HeaderType.WWW_AUTHENTICATE,
		        		 builder.buildHeaderMessage().getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
		         .build();
	}
}
