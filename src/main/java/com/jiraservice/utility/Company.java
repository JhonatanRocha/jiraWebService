package com.jiraservice.utility;

public enum Company {
	
	VIV("Vivo"), TIM("Tim Brasil"), BRA("Bradesco");
	
	public String valor;
	
	private Company(String valor) {
		this.valor = valor;
	}
	
	public String getValor(){
		return this.valor;
	}
}