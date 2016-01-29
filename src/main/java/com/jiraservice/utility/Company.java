package com.jiraservice.utility;

public enum Company {
	
	NENHUM(""), TIM("Tim Brasil");
	
	public String valor;
	
	private Company(String valor) {
		this.valor = valor;
	}
	
	public String getValor(){
		return this.valor;
	}
}