
package com.fimet.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "CodeStore")
public class ECodeStore {
	@DatabaseField(id=true)
	private String useCaseName;
	@DatabaseField(canBeNull = false)
	private String authCode;
	@DatabaseField(canBeNull = false)
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
