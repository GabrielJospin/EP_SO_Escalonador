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
	    quantidadeDeProcessos++;
    }

	private void criaProcessoSJF(Operacao[] codigo) {
		PCB_SJF processo = new PCB_SJF(codigo, 5);
		processos.add(processo);
		processos.sort(processo);
	}

	private void criaProcessoRR(Operacao[] codigo) {
		PCB_RR processo = new PCB_RR(codigo);
		processos.add(processo);
		processos.sort(processo);
	}

	private void criaProcessoSRTF(Operacao[] codigo){
		PCB_SRTF processo = new PCB_SRTF(codigo);
		processos.add(processo);
		processos.sort(processo);
	}
	private void criaProcessoFCFS(Operacao[] codigo){
		PCB_FCFS processo = new PCB_FCFS(codigo);
		processos.add(processo);
		processos.sort(processo);
	}


	@Override
	protected void trocaContexto(PCB pcbAtual, PCB pcbProximo) {
		int idAtual = processos.indexOf(pcbAtual);
		int idProximo = processos.indexOf(pcbProximo);

		if(pcbAtual.operacoesFeitas == pcbAtual.codigo.length)
			pcbAtual.updateEstado(PCB.Estado.TERMINADO);
		else if((pcbAtual.codigo[pcbAtual.operacoesFeitas] instanceof OperacaoES)) {
			pcbAtual.updateEstado(PCB.Estado.ESPERANDO);
		}
		else
			pcbAtual.updateEstado(PCB.Estado.PRONTO);

		pcbAtual.updateEstado(PCB.Estado.EXECUTANDO);
		processos.set(idAtual, pcbAtual);
		processos.set(idProximo, pcbProximo);

		trocasDeProcesso++;
	}

	@Override
	// Assuma que 0 <= idDispositivo <= 4
	protected OperacaoES proximaOperacaoES(int idDispositivo) {
		if(idProcessosEsperando().size() == 0)
			return null;

		PCB processo = getPCBespera(idDispositivo);

		if(processo == null || processo.codigo.length == processo.operacoesFeitas)
			return null;

		Operacao op = processo.codigo[processo.operacoesFeitas];
		if(!( op instanceof OperacaoES))
			return null;

		return (OperacaoES) op;

	}

	private PCB getPCBespera(int idDispositivo) {

		gerateLists();

		for( PCB processo: processos){
			if(processo.codigo.length == processo.operacoesFeitas)
				return null;
			Operacao op =  processo.codigo[processo.operacoesFeitas];
			if(op instanceof OperacaoES && ((OperacaoES) op).idDispositivo == idDispositivo)
				return processo;
		}
		return null;
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		PCB PCBatual = getPCBAtual();
		if(PCBatual != null && PCBatual.operacoesFeitas  < (PCBatual.codigo.length )){
			int pos = PCBatual.operacoesFeitas;
			int local = processos.indexOf(PCBatual);
			processos.set(local, PCBatual);
			Operacao answer = PCBatual.codigo[pos];

			if(answer instanceof OperacaoES){
				for(PCB processo: processos){
					if(! (processo.codigo[processo.operacoesFeitas] instanceof OperacaoES)){
						return processo.codigo[processo.operacoesFeitas];
					}
				}
				return null;
			}

			PCBatual.operacoesFeitas++;
			return answer;
		} 
		else
			return null;


	}

	@Override
	protected void executaCicloKernel() {
		gerateLists();
		boolean temNovosTerminados = false;
		List<PCB> terminadosList = new ArrayList<>();

		for(PCB pcb: processos){


			if(pcb.operacoesFeitas == pcb.codigo.length) {
				pcb.updateEstado(PCB.Estado.TERMINADO);
				temNovosTerminados = true;
				terminadosList.add(pcb);
			}else{
				Operacao op = pcb.codigo[pcb.operacoesFeitas];
				if(op instanceof OperacaoES) {
					if(pcb.codigo == processos.get(0).codigo){
						pcb.updateEstado(PCB.Estado.ESPERANDO);
					}
					if(((OperacaoES) op).ciclos == 0 ) {
						pcb.updateEstado(PCB.Estado.EXECUTANDO);
						pcb.operacoesFeitas += 1;
					}
				}
				else {
					if(pcb.estado.equals(PCB.Estado.NOVO)){
						pcb.updateEstado(PCB.Estado.PRONTO);
					}if(pcb.estado.equals(PCB.Estado.PRONTO)){
						if(!cpuExecutando() && processos.get(0).idProcesso == pcb.idProcesso) {
							pcb.updateEstado(PCB.Estado.EXECUTANDO);
						}
					}else if(pcb.estado.equals(PCB.Estado.EXECUTANDO))
						if(processos.get(0).idProcesso != pcb.idProcesso)
							trocaContexto(pcb, processos.get(0));
				}

			}

			processos.set(processos.indexOf(pcb), pcb);
		}
		if(temNovosTerminados){
			for(PCB processo : terminadosList){
				if(processo.estado.equals(PCB.Estado.TERMINADO)) {
					processos.remove(processo);
					processosTerminados.add(processo.idProcesso);
					tempoRetornoTotal += processo.tempoRetorno;
					tempoEsperaTotal += processo.tempoEspera;
					tempoRespostaTotal += processo.getTempoResposta();
				}
			}
		}
		gerateLists();

	}

	private boolean cpuExecutando() {
		PCB PCBAtual = getPCBAtual();
		assert PCBAtual != null;
		return PCBAtual.idProcesso == this.idProcessoAtual;
	}

	@Override
	protected boolean temTarefasPendentes() {

		return processos.size() != 0;
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
		return this.trocasDeProcesso/quantidadeDeProcessos;
	}

	@Override
	public void defineEscalonador(Escalonador e) {
		this.escalonador = e;
	}

	private void gerateLists(){

		processosProntos = new LinkedList<>();
		processosEmEspera = new LinkedList<>();

		for(PCB e: processos){
			if(e.estado.equals(PCB.Estado.NOVO))
				idProcessoNovo = e.idProcesso;
			if(e.estado.equals(PCB.Estado.PRONTO))
				processosProntos.add(e.idProcesso);
			if(e.estado.equals(PCB.Estado.EXECUTANDO))
				idProcessoAtual = e.idProcesso;
			if(e.estado.equals(PCB.Estado.ESPERANDO))
				processosEmEspera.add(e.idProcesso);
		}

	}

	private PCB getPCBAtual(){
		return processos.size() != 0 ?  processos.get(0): null;
	}

}
