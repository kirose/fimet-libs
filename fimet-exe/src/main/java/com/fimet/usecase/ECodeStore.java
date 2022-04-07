
package com.fimet.usecase;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class ECodeStore implements ICode {
	private String[] values;
	public ECodeStore(int size) {
		values = new String[size];
	}
	public void setValue(int i, String value) {
		values[i] = value;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	@Override
	public String getValue(int index) {
		return values[index];
	}
}
