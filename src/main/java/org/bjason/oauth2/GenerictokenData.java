package org.bjason.oauth2;

import java.util.ArrayList;
import java.util.Set;

import org.bjason.oauth2.Database.TOKEN_STATE;

public class GenerictokenData {

	private long expires;
	private String genericToken;
	private ArrayList<String> permission = new ArrayList<String>();
	
	public GenerictokenData(String code, long expire) {
		genericToken=code;
		expires=System.currentTimeMillis()+expire*1000;
	}
	public long getExpires() {
		return expires;
	}
	public void setExpires(long expires) {
		this.expires = expires;
	}
	public String getGenericToken() {
		return genericToken;
	}
	public void setGenericToken(String accessToken) {
		this.genericToken = accessToken;
	}

	public TOKEN_STATE tokenState() {
		if ( expires < System.currentTimeMillis() ) {
    		return TOKEN_STATE.EXPIRED;
    	}
        return TOKEN_STATE.OK;
	}
	
	public void copyPermission(GenerictokenData authCodeFromUser) {
		if ( authCodeFromUser != null && authCodeFromUser.permission != null )
		permission=new ArrayList<String>(authCodeFromUser.permission);
		
	}

	@Override
	public String toString() {
		return "GenerictokenData [expires=" + expires + ", genericToken="
				+ genericToken + ", permission=" + permission + "]";
	}
	public boolean allowedTo(String what) {
		return permission.contains(what+"true");
	}
	public void addPemission(Set<String> scopes) {
		permission.addAll(scopes);
	}
	
}
