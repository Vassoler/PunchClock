package org.scraper.enumeration;

public enum StatusCode {

	OK(200),
	Found(302),
	NOT_FOUNT(400);

	private Integer code;

	StatusCode(Integer code){
		setCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
