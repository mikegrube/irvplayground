package com.camas.irv.candidate;

public enum Affiliation {

	BLUE ("B"),
	RED ("R"),
	GREEN ("G"),
	YELLOW ("Y");

	private final String code;

	Affiliation (String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
