package kernel;
import java.util.*;

import kernel.PCB.PCB;
import kernel.PCB.PCB_SRTF;
import operacoes.Carrega;
import operacoes.Operacao;
import operacoes.OperacaoES;
import operacoes.Soma;

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
		if(escalonador.equals(Escalonador.SHORTEST_REMANING_TIME_FIRST)){
			criaProcessoSRTF(codigo);
		}
	}

	private void criaProcessoSRTF(Operacao[] codigo){
		PCB_SRTF processo = new PCB_SRTF(codigo);
		processo.estado = PCB.Estado.ESPERANDO;
		processos.add(processo);
		Collections.sort(processos, processo);
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
		PCB PCBatual = getPCBAtual();

		gerateLists();
		if(escalonador.equals(Escalonador.SHORTEST_REMANING_TIME_FIRST)){
			if(idProcessoAtual != processos.get(0).idProcesso){

				if (PCBatual.operacoesFeitas == PCBatual.codigo.length)
					PCBatual.estado = PCB.Estado.TERMINADO;
				else
					PCBatual.estado = PCB.Estado.ESPERANDO;

				PCB PCBproximo = processos.get(0);
				trocaContexto(PCBatual, PCBproximo);
				return;
			}

			if(PCBatual.estado.equals(PCB.Estado.NOVO)){
				PCBatual.estado = PCB.Estado.PRONTO;
				return;
			}

			if(! PCBatual.estado.equals(PCB.Estado.EXECUTANDO))
				PCBatual.estado = PCB.Estado.EXECUTANDO;

			Operacao operacaoAtual = PCBatual.codigo[PCBatual.operacoesFeitas];
			executaOperacao(operacaoAtual, PCBatual);
			PCBatual.operacoesFeitas++;
		}
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

	private PCB getPCBAtual(){
		for (PCB processo : processos)
			if (processo.idProcesso == idProcessoAtual)
				return processo;

		throw new RuntimeException("PCb atual nulo");
	}

	private static void executaOperacao(Operacao operacao, PCB pcb){
		if(operacao instanceof Soma)
			excutaSoma((Soma) operacao, pcb);
		if(operacao instanceof Carrega)
			executaCarrega((Carrega) operacao, pcb);
		if(operacao instanceof OperacaoES)
			executaOperacaoES((OperacaoES) operacao, pcb);
		else
			throw new RuntimeException("Operador Inválido");
	}

	private static void executaOperacaoES(OperacaoES operacaoES, PCB pcb) {
		//TODO Operação ES
	}

	private static void executaCarrega(Carrega carrega, PCB pcb) {
		pcb.registradores[carrega.registrador] = carrega.valor;
	}

	private static void excutaSoma(Soma soma, PCB pcb) {
		int parcela1 = pcb.registradores[soma.registradorParcela1];
		int parcela2 = pcb.registradores[soma.registradorParcela2];
		int result = parcela1 + parcela2;
		pcb.registradores[soma.registradorTotal] = result;
	}
}
