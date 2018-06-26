package com.evergrande.TestBase;

import java.util.LinkedHashMap;

/**
* @author huangxiao
* @version 创建时间：2018年5月24日 下午2:24:04
* 类说明
*/
public class TestUnit extends TestBase{
	private LinkedHashMap<String, TestCase> caseMap;

	public LinkedHashMap<String, TestCase> getCaseMap() {
		return caseMap;
	}

	public void setCaseMap(LinkedHashMap<String, TestCase> caseMap) {
		this.caseMap = caseMap;
	}
}
