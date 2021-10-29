package kernel;
import java.util.*;

import kernel.PCB.*;
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
	int indiceOperacao;


	public SeuSO() {

		PCB.processosfeitos = 0;

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
		this.idProcessoAtual = 0;
		this.indiceOperacao = -1;
		}

	@Override
	// ATENCÃO: cria o processo mas o mesmo 
	// só estará "pronto" no proxime ciclo
    //Teste
	protected void criaProcesso(Operacao[] codigo) {
		if(escalonador.equals(Escalonador.SHORTEST_REMANING_TIME_FIRST)){
			criaProcessoSRTF(codigo);
		} else if(escalonador.equals(Escalonador.FIRST_COME_FIRST_SERVED)) {
			criaProcessoFCFS(codigo);
		} else if(escalonador.equals(Escalonador.ROUND_ROBIN_QUANTUM_5)){
			criaProcessoRR(codigo);
		}else if(escalonador.equals(Escalonador.SHORTEST_JOB_FIRST)){
			criaProcessoSJF(codigo);
		}else
			throw new RuntimeException("Escalonador não identificado");
	}

	private void criaProcessoSJF(Operacao[] codigo) {
		PCB_SJF processo = new PCB_SJF(codigo, 5);
		processos.add(processo);
		Collections.sort(processos, processo);
	}

	private void criaProcessoRR(Operacao[] codigo) {
		PCB_RR processo = new PCB_RR(codigo);
		processos.add(processo);
		Collections.sort(processos, processo);
	}

	private void criaProcessoSRTF(Operacao[] codigo){
		PCB_SRTF processo = new PCB_SRTF(codigo);
		processos.add(processo);
		Collections.sort(processos, processo);
	}
	private void criaProcessoFCFS(Operacao[] codigo){
		PCB_FCFS processo = new PCB_FCFS(codigo);
		processos.add(processo);
		Collections.sort(processos, processo);
	}


	@Override
	protected void trocaContexto(PCB pcbAtual, PCB pcbProximo) {
		int idAtual = processos.indexOf(pcbAtual);
		int idProximo = processos.indexOf(pcbProximo);

		if(pcbAtual.operacoesFeitas == pcbAtual.codigo.length)
			pcbAtual.estado = PCB.Estado.TERMINADO;
		else
			pcbAtual.estado = PCB.Estado.PRONTO;

		pcbProximo.estado = PCB.Estado.EXECUTANDO;
		processos.set(idAtual, pcbAtual);
		processos.set(idProximo, pcbProximo);
	}

	@Override
	// Assuma que 0 <= idDispositivo <= 4
	protected OperacaoES proximaOperacaoES(int idDispositivo) {
		PCB pcbAtual = null;

		Integer pcb = idProcessosEsperando().get(0);
		for(PCB processo: processos){
			if(processo.idProcesso == pcb)
				pcbAtual = processo;
		}

		if(pcbAtual == null)
			return null;
		Operacao answer =  pcbAtual.codigo[pcbAtual.operacoesFeitas];

		if(! (answer instanceof  OperacaoES))
			return null;

		pcbAtual.operacoesFeitas += ((OperacaoES) answer).ciclos;
		return (OperacaoES) answer;
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		PCB PCBatual = getPCBAtual();
		if(PCBatual.operacoesFeitas + 1 < (PCBatual.codigo.length - 1)){
			processos.set(idProcessoAtual, PCBatual);
			Operacao answer = PCBatual.codigo[PCBatual.operacoesFeitas];
			PCBatual.operacoesFeitas++;
			return answer instanceof OperacaoES? null: answer;
		} 
		else
			return null;


	}

	@Override
	protected void executaCicloKernel() {
		gerateLists();
		int i = 0;
		for(PCB pcb: processos){


			if(pcb.operacoesFeitas == pcb.codigo.length)
				pcb.estado = PCB.Estado.TERMINADO;

			if(pcb.codigo[pcb.operacoesFeitas] instanceof OperacaoES) {
				pcb.estado = PCB.Estado.ESPERANDO;
			}
			else {
				if(pcb.estado.equals(PCB.Estado.NOVO)){
					pcb.estado = PCB.Estado.PRONTO;
				}if(pcb.estado.equals(PCB.Estado.PRONTO)){
					if(!cpuExecutando() && processos.get(0).idProcesso == pcb.idProcesso)
						pcb.estado = PCB.Estado.EXECUTANDO;
				}else if(pcb.estado.equals(PCB.Estado.EXECUTANDO))
					 if(processos.get(0).idProcesso != pcb.idProcesso)
						trocaContexto(pcb, processos.get(0));
			}
			processos.set(i, pcb);
			i++;
		}
		gerateLists();
	}

	private boolean cpuExecutando() {
		PCB PCBAtual = processos.get(idProcessoExecutando());
		return ! (PCBAtual.operacoesFeitas == PCBAtual.codigo.length);
	}

	@Override
	protected boolean temTarefasPendentes() {
		boolean isFinish = false;
		for(PCB processo: processos){
			if (!processo.estado.equals(PCB.Estado.TERMINADO)) {
				isFinish = true;
				break;
			}
		}

		return isFinish;
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

		processosProntos = new LinkedList<>();
		processosEmEspera = new LinkedList<>();
		processosTerminados = new LinkedList<>();

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
		gerateLists();
		for (PCB processo : processos)
			if (processo.idProcesso == idProcessoAtual)
				return processo;

		throw new RuntimeException("PCB nulo");
	}

}
