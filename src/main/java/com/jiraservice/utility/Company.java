package com.jiraservice.utility;

public enum Company {
	
	NENHUM(""), EMC("EMC"), TIM("TIM"), SAN("SAN");
	
	public String valor;
	
	private Company(String valor) {
		this.valor = valor;
	}
	
	public String getValor(){
		return this.valor;
	}
}