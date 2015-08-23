package com.cdhz.cdhz_1;

import java.io.Serializable;

import com.hyc.androidcore.annotation.HDColumn;
import com.hyc.androidcore.annotation.HDEntity;
import com.hyc.androidcore.annotation.HDId;
import com.hyc.androidcore.annotation.HDMaxLength;

@HDEntity(tableName = "abs")
public class Propertity implements Serializable {

	public Propertity() {

	}

	public Propertity(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@HDId(auoIncreateMent = false)
	@HDColumn(name = "a")
	private String key;

	@HDMaxLength(value = 1024)
	@HDColumn(name = "b")
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
