package com.emc.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import com.emc.model.Atividade;

/**
 * 
 * @author Jhonatan Rocha
 *
 */

public class AtividadeTest {
	
	@Test
	public void setAttributes(){
		
		Date dataAtual = DateTime.now().toDate();
		
		Atividade atividade = new Atividade(1l, "key", "Issue do JIra", "bug", dataAtual, "resolucao aleatoria",
											dataAtual, dataAtual, "ciclano", "aberto", "8 horas",
											dataAtual, dataAtual, "8%", "Quarto", "fulano", "Desenvolvendo", dataAtual);
		
		assertEquals(1l, atividade.getId());
		assertEquals("key", atividade.getKey());
		assertEquals("Issue do JIra", atividade.getSummary());
		assertEquals("bug", atividade.getIssueType());
		assertEquals(dataAtual, atividade.getCreated());
		assertEquals("resolucao aleatoria", atividade.getResolution());
		assertEquals(dataAtual, atividade.getResolved());
		assertEquals(dataAtual, atividade.getUpdated());
		assertEquals("ciclano", atividade.getAssigned());
		assertEquals("aberto", atividade.getStatus());
		assertEquals("8 horas", atividade.getOriginalEstimate());
		assertEquals(dataAtual, atividade.getTimeSpent());
		assertEquals(dataAtual, atividade.getRemainingEstimate());
		assertEquals("8%", atividade.getWorkRatio());
		assertEquals("Quarto", atividade.getSprint());
		assertEquals("fulano", atividade.getCreator());
		assertEquals("Desenvolvendo", atividade.getProgress());
		assertEquals(dataAtual, atividade.getDueDate());
	}
}
