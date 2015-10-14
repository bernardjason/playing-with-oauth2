/**
 * Copyright 2010 Newcastle University
 *
 * http://research.ncl.ac.uk/smart/
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.bjason.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.bjason.oauth2.Database.TOKEN_STATE;
import org.springframework.beans.factory.annotation.Autowired;


@Path("/token")
public class TokenResource {
@Autowired
private Database database;
    
    public static final String INVALID_CLIENT_DESCRIPTION = 
    		"Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).";
    
    @POST
    @Produces("application/json")
    public Response authorize(@Context HttpServletRequest request) throws OAuthSystemException {
        try {

        	
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            
            if (!checkClientId(oauthRequest.getClientId())) {
                return buildInvalidClientIdResponse();
            }

            if (!checkClientSecret(oauthRequest.getClientSecret())) {
                return buildInvalidClientSecretResponse();
            }

            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
            	TOKEN_STATE ts = checkAuthCode(oauthRequest.getParam(OAuth.OAUTH_CODE));
                if ( ts == TOKEN_STATE.BAD ) {
                    return buildBadAuthCodeResponse("invalid authorization code");
                }
                if ( ts == TOKEN_STATE.EXPIRED ) {
                    return buildBadAuthCodeResponse("expired authorization code");
                }
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.PASSWORD.toString())) {
                if (!checkUserPass(oauthRequest.getUsername(), oauthRequest.getPassword())) {
                    return buildInvalidUserPassResponse();
                }
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
                buildInvalidUserPassResponse();
            }
            
            
            final String accessToken = oauthIssuerImpl.accessToken();
            
            GenerictokenData authCodeFromUser = database.getTokenCode(oauthRequest.getParam(OAuth.OAUTH_CODE));
            GenerictokenData newAccessToken = new GenerictokenData(accessToken,Common.THIRTY_SECONDS);
            newAccessToken.copyPermission(authCodeFromUser);
            database.addAccessToken(newAccessToken);
            
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(""+newAccessToken.getExpires())
                    .buildJSONMessage();
            return Response.status(response.getResponseStatus()).entity(response.getBody()).build();

        } catch (OAuthProblemException e) {
            OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            return Response.status(res.getResponseStatus()).entity(res.getBody()).build();
        }
    }

    private Response buildInvalidClientIdResponse() throws OAuthSystemException {
        OAuthResponse response =
                OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                .setErrorDescription(INVALID_CLIENT_DESCRIPTION)
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private Response buildInvalidClientSecretResponse() throws OAuthSystemException {
        OAuthResponse response =
                OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT).setErrorDescription(INVALID_CLIENT_DESCRIPTION)
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private Response buildBadAuthCodeResponse(String description) throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription(description)
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }


    private Response buildInvalidUserPassResponse() throws OAuthSystemException {
        OAuthResponse response = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("invalid username or password")
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
    }

    private boolean checkClientId(String clientId) {
        return true;
    }

    private boolean checkClientSecret(String secret) {
        return true;
    }

    private TOKEN_STATE checkAuthCode(String authCode) {
        return database.isValidAuthCode(authCode);
    }
    

    private boolean checkUserPass(String user, String pass) {
        return Common.PASSWORD.equals(pass) && Common.USERNAME.equals(user);
    }
}
