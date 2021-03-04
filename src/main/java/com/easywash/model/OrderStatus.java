package com.easywash.model;

import javax.validation.constraints.Size;

public class OrderStatus {
	 @Size(min=1, max=10,message="Enter either CID or 10 digit registered Mobile")
	private String inputPar;

	public String getInputPar() {
		return inputPar;
	}

	public void setInputPar(String inputPar) {
		this.inputPar = inputPar;
	}

	
}
