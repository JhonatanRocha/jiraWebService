package com.emc.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import com.emc.model.Recurso;


/**
 * 
 * @author Jhonatan Rocha
 *
 */

public class RecursoTest {

	@Test
	public void setAttributes(){
		
		Date dataAtual = DateTime.now().toDate();
		
		Recurso recurso = new Recurso(1l, "fulano", "fulano da silva", dataAtual, dataAtual, "alocado");
		
		assertEquals(1l, recurso.getId());
		assertEquals("fulano", recurso.getUserName());
		assertEquals("fulano da silva", recurso.getFullName());
		assertEquals(dataAtual, recurso.getDataInicio());
		assertEquals(dataAtual, recurso.getDataFinal());
		assertEquals("alocado", recurso.getStatus());
	}
}
