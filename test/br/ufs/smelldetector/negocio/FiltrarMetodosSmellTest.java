package br.ufs.smelldetector.negocio;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import br.ufs.smelldetector.model.DadosMetodoSmell;
import br.ufs.smelldetector.model.LimiarMetrica;
import br.ufs.smelldetector.model.LimiarMetricaKey;

public class FiltrarMetodosSmellTest {

	private final int LIMIAR_MAX_LOC = 10;
	private final int LIMIAR_MAX_CC = 10;
	private final int LIMIAR_MAX_EFFERENT = 20;
	private final int LIMIAR_MAX_NOP = 5;

	private ArrayList<String> projetosBenchMark = new ArrayList<String>();
	private ArrayList<String> projetosAnalisar = new ArrayList<String>();
	
	@Test
	public void testFiltrarPorValoresPredefinidos() {
		projetosAnalisar.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
		
		HashMap<LimiarMetricaKey, LimiarMetrica> mapLimiarMetrica = GerenciadorLimiares
				.obterLimiarPreDefinidoGlobal(LIMIAR_MAX_LOC, LIMIAR_MAX_CC, LIMIAR_MAX_EFFERENT, LIMIAR_MAX_NOP);
		FiltrarMetodosSmell filtrarMetodosSmell = new FiltrarMetodosSmell();
		ArrayList<DadosMetodoSmell> metodosSmell = filtrarMetodosSmell.filtrar(projetosAnalisar, mapLimiarMetrica);

		exibeMetodosLongos(metodosSmell, "Filtrar por valor limiar");

		assertTrue(metodosSmell.size() > 0);
	}

	@Test
	public void testFiltrarPorBenchmark() {
		projetosAnalisar.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
		projetosBenchMark.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
		final int percentil = 90;
		
		HashMap<LimiarMetricaKey, LimiarMetrica> mapLimiarMetrica = GerenciadorLimiares
				.obterLimiarBenchMarkPercentil(projetosBenchMark, percentil);
		
		FiltrarMetodosSmell filtrarMetodosSmell = new FiltrarMetodosSmell();
		ArrayList<DadosMetodoSmell> metodosLongos = filtrarMetodosSmell.filtrar(projetosAnalisar, mapLimiarMetrica);

		exibeMetodosLongos(metodosLongos, "Filtrar Por BenchMark");

		assertTrue(metodosLongos.size() > 0);
	}

	@Test
	public void testFiltrarPorBenchMarkDesignRole() {
		projetosAnalisar.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
		projetosBenchMark.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
		final int percentil = 90;
		
		HashMap<LimiarMetricaKey, LimiarMetrica> mapLimiarMetrica = GerenciadorLimiares
				.obterLimiarBenchMarkDesignRole(projetosBenchMark, percentil);
		
		FiltrarMetodosSmell filtrarMetodosSmell = new FiltrarMetodosSmell();
		ArrayList<DadosMetodoSmell> metodosLongos = filtrarMetodosSmell.filtrar(projetosAnalisar, mapLimiarMetrica);

		exibeMetodosLongos(metodosLongos, "Filtrar Por BenchMark");

		assertTrue(metodosLongos.size() > 0);
	}

	private void exibeMetodosLongos(ArrayList<DadosMetodoSmell> metodosSmell, String descricaoMetodo) {
		System.out.println("\n\n\n" + descricaoMetodo);
		System.out.println("Classe   |     M�todo       |     Linhas C�digo     |  CC  | Efferent |  NOP");
		for (DadosMetodoSmell metodoSmell : metodosSmell) {
			System.out.println(metodoSmell.getNomeClasse() + "  | " + metodoSmell.getNomeMetodo() + " | "
					+ metodoSmell.getLinesOfCode() + "|" + metodoSmell.getComplexity() + " | "
					+ metodoSmell.getEfferent() + " | " + metodoSmell.getNumberOfParameters());
		}
		System.out.println("Total de m�todos longos: " + metodosSmell.size());
	}

}