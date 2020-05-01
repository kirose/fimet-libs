
package com.fimet.entity.sqlite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.entity.sqlite.pojo.Notice;
import com.fimet.entity.sqlite.type.ListNoticeType;
import com.fimet.entity.sqlite.type.ListValidationType;
import com.fimet.entity.sqlite.type.MapStringString;
import com.fimet.simulator.ValidationResult;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "UseCaseReport")
public class EUseCaseReport {

	@DatabaseField(id = true)
	private String path;
	@DatabaseField(canBeNull = false)
	private String useCase;
	@DatabaseField(canBeNull = true, persisterClass=MapStringString.class)
	private Map<String, String> data;
	@DatabaseField(canBeNull = true)
	private String responseCode;
	@DatabaseField(canBeNull = true)
	private String error;
	@DatabaseField(canBeNull = false)
	private String acquirer;
	@DatabaseField(canBeNull = true)
	private String issuer;
	@DatabaseField(persisterClass=ListValidationType.class, canBeNull = true)
	private List<ValidationResult> validations;
	@DatabaseField(persisterClass=ListNoticeType.class, canBeNull = true)
	private List<Notice> notices;
	public EUseCaseReport() {
		data = new HashMap<>();
	}
	public EUseCaseReport(String path, String useCase) {
		super();
		this.path = path;
		this.useCase = useCase;
		data = new HashMap<>();
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUseCase() {
		return useCase;
	}
	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<ValidationResult> getValidations() {
		return validations;
	}
	public void setValidations(List<ValidationResult> validations) {
		this.validations = validations;
	}
	public List<Notice> getNotices() {
		return notices;
	}
	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}
	public boolean has(String key) {
		return data.containsKey(key);
	}
	public String get(String key) {
		return data.get(key);
	}
	public String put(String key, String value) {
		return data.put(key, value);
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getAcquirer() {
		return acquirer;
	}
	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getName() {
		if (path != null) {
			int index = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
			return path.substring(index);
		}
		return null;
	}
}
