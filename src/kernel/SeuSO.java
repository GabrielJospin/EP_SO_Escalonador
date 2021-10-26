package kernel;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import operacoes.Operacao;
import operacoes.OperacaoES;

public class SeuSO extends SO {

	private Escalonador escalonador;

	//Variaveis de Tempo
	private int quantidadeDeProcessos;
	private Long tempoEsperaTotal; //Valor em ms
	private Long tempoRespostaTotal; // Valor em ms
	private Long tempoRetornoTotal; // valor em ms
	private int trocasDeProcesso;

	//Variaveis de Organização
	List<Integer> processosTerminados;
	List<Integer> processosPausados;
	List<Integer> processosEmEspera;
	List<Integer> processosProntos;
	int idProcessoAtual;
	int idProcessoNovo;



	public SeuSO() {
		this.escalonador = null;

		this.quantidadeDeProcessos = 0;
		this.tempoEsperaTotal = 0L;
		this.tempoRespostaTotal = 0L;
		this.tempoRetornoTotal = 0L;
		this.trocasDeProcesso = 0;

		this.processosTerminados = new ArrayList<>();
		this.processosPausados  = new ArrayList<>();
		this.processosEmEspera = new ArrayList<>();
		this.processosProntos = new ArrayList<>();
		this.idProcessoAtual = -1;
	}

	@Override
	// ATENCÃO: cria o processo mas o mesmo 
	// só estará "pronto" no proxime ciclo
    //Teste
	protected void criaProcesso(Operacao[] codigo) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void trocaContexto(PCB pcbAtual, PCB pcbProximo) {
		// TODO Auto-generated method stub
	}

	@Override
	// Assuma que 0 <= idDispositivo <= 4
	protected OperacaoES proximaOperacaoES(int idDispositivo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void executaCicloKernel() {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean temTarefasPendentes() {
		return this.processosEmEspera.isEmpty();
	}

	@Override
	protected Integer idProcessoNovo() {
		return idProcessoNovo;
	}

	@Override
	protected List<Integer> idProcessosProntos() {
		return this.processosProntos;
	}

	@Override
	protected Integer idProcessoExecutando() {
		return this.idProcessoAtual;
	}

	@Override
	protected List<Integer> idProcessosEsperando() {
		return this.processosEmEspera;
	}

	@Override
	protected List<Integer> idProcessosTerminados() {
		return this.processosTerminados;
	}

	@Override
	protected int tempoEsperaMedio() {
		if(quantidadeDeProcessos == 0)
			return -1;

		return (int) (tempoEsperaTotal/quantidadeDeProcessos);
	}

	@Override
	protected int tempoRespostaMedio() {
		if(quantidadeDeProcessos == 0)
			return -1;

		return (int) (tempoRespostaTotal/quantidadeDeProcessos);
	}

	@Override
	protected int tempoRetornoMedio() {
		if(quantidadeDeProcessos == 0)
			return -1;

		return (int) (tempoRetornoTotal/quantidadeDeProcessos);
	}

	@Override
	protected int trocasContexto() {
		return this.trocasDeProcesso;
	}

	@Override
	public void defineEscalonador(Escalonador e) {
		this.escalonador = e;
	}

	private void gerateLists(){

	}
}
