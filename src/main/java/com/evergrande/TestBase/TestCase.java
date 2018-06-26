package com.evergrande.TestBase;

import java.util.List;

/**
* @author huangxiao
* @version 创建时间：2018年5月24日 下午2:24:15
* 类说明
*/
public class TestCase extends TestBase{
	private List<TestStep> stepList;
	private String name;
	private String id;
	public String getCaseId() {
		return id;
	}

	public void setCaseId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TestStep> getSteps() {
		return stepList;
	}

	public void setSteps(List<TestStep> stepList) {
		this.stepList = stepList;
	}
}
