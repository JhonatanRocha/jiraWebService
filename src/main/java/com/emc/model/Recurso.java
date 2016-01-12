package com.emc.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * 
 * @author Jhonatan Rocha
 *
 */

@Entity
public class Recurso {

	@Id
	private String id; /*ID do Recurso*/
	
	@Column
	private String userName; /*Nome do Recurso*/
	
	@Column
	private String fullName; /*Nome Completo do Recurso*/
	
	@Column
	private Date dataInicio; /*Data de Inicio*/
	
	@Column
	private Date dataFinal; /*Data Final*/
	
	@Column
	private String status; /*Status do Recurso*/
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
