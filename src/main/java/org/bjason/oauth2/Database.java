/**
 * original author @author jdlee see http://blogs.steeplesoft.com/posts/2013/a-simple-oauth2-client-and-server-example-part-i.html
 */
package org.bjason.oauth2;

import java.util.HashMap;


public class Database {

	public enum TOKEN_STATE { OK, EXPIRED, BAD};

    private HashMap<String,GenerictokenData> tokenCodes = new HashMap<String,GenerictokenData>();
    private HashMap<String,GenerictokenData> accessTokenCodes = new HashMap<String,GenerictokenData>();

    public void addTokenCode(GenerictokenData tokenCode) {
    	tokenCodes.put(tokenCode.getGenericToken(),tokenCode);
    }
    public GenerictokenData getTokenCode(String tokenCode) {
    	return tokenCodes.get(tokenCode);
    }

    public TOKEN_STATE isValidAuthCode(String authCode) {
        GenerictokenData t = tokenCodes.get(authCode);
    	if ( t == null ) {
    		return TOKEN_STATE.BAD;
    	}
    	return t.tokenState();
    }

    public void addAccessToken(GenerictokenData accessToken) {
        accessTokenCodes.put(accessToken.getGenericToken(),accessToken);
    }
    
    public TOKEN_STATE isValidAccessToken(String token) {
    	GenerictokenData t = accessTokenCodes.get(token);
    	if ( t == null ) return TOKEN_STATE.BAD;
    	return t.tokenState();
    }
	public GenerictokenData getAccessToken(String accessToken) {
		return accessTokenCodes.get(accessToken);
	}

}
