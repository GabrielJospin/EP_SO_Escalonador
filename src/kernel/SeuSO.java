package kernel;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import kernel.PCB.PCB;
import kernel.PCB.PCB_SRTF;
import operacoes.Operacao;
import operacoes.OperacaoES;

public class SeuSO extends SO {

	//Escalonador
	private Escalonador escalonador;
	List<PCB> processos;

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
		this.processos = new LinkedList<>();

		this.quantidadeDeProcessos = 0;
		this.tempoEsperaTotal = 0L;
		this.tempoRespostaTotal = 0L;
		this.tempoRetornoTotal = 0L;
		this.trocasDeProcesso = 0;

		this.processosTerminados = new LinkedList<>();
		this.processosPausados  = new LinkedList<>();
		this.processosEmEspera = new LinkedList<>();
		this.processosProntos = new LinkedList<>();
		this.idProcessoAtual = -1;
	}

	@Override
	// ATENCÃO: cria o processo mas o mesmo 
	// só estará "pronto" no proxime ciclo
    //Teste
	protected void criaProcesso(Operacao[] codigo) {
		if(escalonador.equals(Escalonador.FIRST_COME_FIRST_SERVED)){
			criaProcessoSRTF(codigo);
		}
	}

	private void criaProcessoSRTF(Operacao[] codigo){
		PCB_SRTF processo = new PCB_SRTF(codigo);
		Comparator<PCB> comparator = Comparator.comparing(PCB_SRTF::compareTo);
		processos.add(processo);
		processos.sort(comparator);
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
		gerateLists();
		return idProcessoNovo;
	}

	@Override
	protected List<Integer> idProcessosProntos() {
		gerateLists();
		return this.processosProntos;
	}

	@Override
	protected Integer idProcessoExecutando() {
		gerateLists();
		return this.idProcessoAtual;
	}

	@Override
	protected List<Integer> idProcessosEsperando() {
		gerateLists();
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
		processos.forEach(e ->{
			if(e.estado.equals(PCB.Estado.NOVO))
				idProcessoNovo = e.idProcesso;
			if(e.estado.equals(PCB.Estado.PRONTO))
				processosProntos.add(e.idProcesso);
			if(e.estado.equals(PCB.Estado.EXECUTANDO))
				idProcessoAtual = e.idProcesso;
			if(e.estado.equals(PCB.Estado.ESPERANDO))
				processosEmEspera.add(e.idProcesso);
			if(e.estado.equals(PCB.Estado.TERMINADO))
				processosTerminados.add(e.idProcesso);
		});
	}
}
