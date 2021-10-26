package kernel;
import java.time.LocalTime;
import java.util.List;

import operacoes.Operacao;
import operacoes.OperacaoES;

public class SeuSO extends SO {

	private Escalonador escalonador;
	private int quantidadeDeProcessos;
	private Long tempoEsperaTotal; //Valor em ms
	private Long tempoRespostaTotal; // Valor em ms
	private Long tempoRetornoTotal; // valor em ms

	public SeuSO() {
		this.escalonador = null;
		this.quantidadeDeProcessos = 0;
		this.tempoEsperaTotal = 0L;
		this.tempoRespostaTotal = 0L;
		this.tempoRetornoTotal = 0L;
	}

	@Override
	// ATENCÃO: cria o processo mas o mesmo 
	// só estará "pronto" no proxime ciclo
    //Teste
	protected void criaProcesso(Operacao[] codigo) {
		PCB pcbAtual = new PCB(codigo);
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Integer idProcessoNovo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Integer> idProcessosProntos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Integer idProcessoExecutando() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Integer> idProcessosEsperando() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Integer> idProcessosTerminados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int tempoEsperaMedio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int tempoRespostaMedio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int tempoRetornoMedio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int trocasContexto() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void defineEscalonador(Escalonador e) {
		this.escalonador = e;
	}
}
