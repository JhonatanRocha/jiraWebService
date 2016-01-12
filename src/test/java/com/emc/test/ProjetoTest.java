package com.emc.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import com.emc.model.Projeto;


/**
 * 
 * @author Jhonatan Rocha
 *
 */

public class ProjetoTest {

	@Test
	public void setAttributes(){
		
		Date dataAtual = DateTime.now().toDate();
		
		Projeto projeto = new Projeto(1l, "Projeto Piloto", dataAtual);
		
		assertEquals(1l, projeto.getId());
		assertEquals("Projeto Piloto", projeto.getProject());
		assertEquals(dataAtual, projeto.getDataCreate());
	}
}
