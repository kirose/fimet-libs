
package com.fimet.usecase;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class ECodeStore implements ICode {
	private String useCaseName;
	private String authCode;
	private String responseCode;
	public ECodeStore() {
		super();
	}
	public ECodeStore(String useCaseName, String authCode, String responseCode) {
		super();
		this.useCaseName = useCaseName;
		this.authCode = authCode;
		this.responseCode = responseCode;
	}
	public String getUseCaseName() {
		return useCaseName;
	}
	public void setUseCaseName(String useCaseName) {
		this.useCaseName = useCaseName;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
}
