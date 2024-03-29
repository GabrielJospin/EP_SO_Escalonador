package kernel;
import java.util.*;


import kernel.PCB.*;
import kernel.PCB.PCB.Estado;
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
	List<Integer> filaProntos;
	List<Integer> filaEsperando;
	int idProcessoAtual;
	int idProcessoNovo;
	int indiceOperacao;
	int ciclo;

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
		this.filaEsperando = new LinkedList<>();
		this.filaProntos = new LinkedList<>();
		this.idProcessoAtual = -1;
		this.indiceOperacao = -1;
		this.ciclo = 0;
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
		PCB_SJF processo = new PCB_SJF(codigo,ciclo ,5);
		processos.add(processo);
		processos.sort(processo);
	}

	private void criaProcessoRR(Operacao[] codigo) {
		PCB_RR processo = new PCB_RR(codigo, ciclo);
		processos.add(processo);
		processos.sort(processo);
	}

	private void criaProcessoSRTF(Operacao[] codigo){
		PCB_SRTF processo = new PCB_SRTF(codigo, ciclo);
		processos.add(processo);
		processos.sort(processo);
	}
	private void criaProcessoFCFS(Operacao[] codigo){
		PCB_FCFS processo = new PCB_FCFS(codigo, this.ciclo);
		processos.add(processo);
		processos.sort(processo);
	}


	@Override
	protected void trocaContexto(PCB pcbAtual, PCB pcbProximo) {
		trocasDeProcesso++;
		int idAtual = processos.indexOf(pcbAtual);
		int idProximo = processos.indexOf(pcbProximo);
		if(pcbProximo.estado.equals(PCB.Estado.ESPERANDO)){
			return;
		}

		if(pcbAtual.operacoesFeitas == pcbAtual.codigo.length) {
			pcbAtual.updateEstado(PCB.Estado.TERMINADO);
			trocasDeProcesso--;
		}
		else if((pcbAtual.codigo[pcbAtual.operacoesFeitas] instanceof OperacaoES)) {
			pcbAtual.updateEstado(PCB.Estado.ESPERANDO);
		}
		else
			pcbAtual.updateEstado(PCB.Estado.PRONTO);

		pcbProximo.updateEstado(PCB.Estado.EXECUTANDO);
		this.idProcessoAtual = pcbProximo.idProcesso;
		processos.set(idAtual, pcbAtual);
		processos.set(idProximo, pcbProximo);


	}

	@Override
	// Assuma que 0 <= idDispositivo <= 4
	protected OperacaoES proximaOperacaoES(int idDispositivo) {
		if(idProcessosEsperando().size() == 0)
			return null;

		PCB processo = getPCBespera(idDispositivo);
		//if(processo != null)
			//System.out.println("Prox op e/s" + processo.idProcesso);
		if(processo == null || processo.codigo.length == processo.operacoesFeitas)
			return null;
		if(!processo.estado.equals(PCB.Estado.ESPERANDO)) {
			if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED) {
				if(!this.filaEsperando.contains(processo.idProcesso))
					this.filaEsperando.add(filaEsperando.indexOf(processo.idProcesso));
			} else if(this.escalonador == Escalonador.SHORTEST_JOB_FIRST) {
				if(processo.estado.equals(PCB.Estado.EXECUTANDO)) {
					processo.mediaExponencial();
				}
			}
			processo.updateEstado(PCB.Estado.ESPERANDO);
		}
		Operacao op = processo.codigo[processo.operacoesFeitas];
		if(!( op instanceof OperacaoES))
			return null;

		processo.ciclosExecutando++;
		return (OperacaoES) op;

	}

	private PCB getPCBespera(int idDispositivo) {
		gerateLists();
		if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED) {
			for(int idPEsperando: filaEsperando){
				for( PCB processo: processos){
					if(processo.idProcesso == idPEsperando) {
						if(! (processo.codigo.length == processo.operacoesFeitas) && (processo.idProcesso != this.ciclo - 1)){
							if((processo.estado != Estado.EXECUTANDO)) {
								Operacao op =  processo.codigo[processo.operacoesFeitas];
									if(op instanceof OperacaoES && ((OperacaoES) op).idDispositivo == idDispositivo)
										return processo;
							}
							
						}
					}
					
				}
			}
			
			return null;
		}
		for( PCB processo: processos){
			if(! (processo.codigo.length == processo.operacoesFeitas) && (processo.idProcesso != this.ciclo - 1)){
				if((processo.estado != Estado.EXECUTANDO)) {
					if(this.escalonador.equals(Escalonador.FIRST_COME_FIRST_SERVED)) {
						Operacao op =  processo.codigo[processo.operacoesFeitas];
						if(op instanceof OperacaoES && ((OperacaoES) op).idDispositivo == idDispositivo)
							return processo;
					}
					Operacao op =  processo.codigo[processo.operacoesFeitas];
					if(op instanceof OperacaoES && ((OperacaoES) op).idDispositivo == idDispositivo)
						return processo;
				}
				
			}
		}
		return null;
	}

	@Override
	protected Operacao proximaOperacaoCPU() {
		PCB PCBatual = getPCBAtual();
		if((PCBatual != null) && (PCBatual.operacoesFeitas  < PCBatual.codigo.length) && !(PCBatual.estado.equals(Estado.NOVO))){
			int pos = PCBatual.operacoesFeitas;
			int local = processos.indexOf(PCBatual);
			Operacao answer = PCBatual.codigo[pos];
			if(answer instanceof OperacaoES){
				for(PCB processo: processos){
					if(processo.operacoesFeitas < processo.codigo.length
							&&(! (processo.codigo[processo.operacoesFeitas] instanceof OperacaoES) && !(processo.estado.equals(Estado.NOVO)))){
						answer = processo.codigo[processo.operacoesFeitas];
						processo.operacoesFeitas++;
						processo.updateEstado(PCB.Estado.EXECUTANDO);
						this.idProcessoAtual = processo.idProcesso;
						PCBatual.ciclosExecutando++;
						return answer;
					}
				}
				return null;
			}
			if(! PCBatual.estado.equals(PCB.Estado.EXECUTANDO)){
				if(PCBatual.estado.equals(PCB.Estado.ESPERANDO)) {
					if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED) {
						if(filaEsperando.contains(PCBatual.idProcesso))
							filaEsperando.remove((Integer) PCBatual.idProcesso);
					}
				}
				PCBatual.updateEstado(PCB.Estado.EXECUTANDO);
				if(this.escalonador == Escalonador.SHORTEST_JOB_FIRST) {
					PCBatual.contadorBurst++;
				}
				this.idProcessoAtual = PCBatual.idProcesso;
			}
			processos.set(local, PCBatual);
			PCBatual.ciclosExecutando ++;
			PCBatual.operacoesFeitas++;
			return answer;
		} 
		else
			return null;


	}

	@Override
	protected void executaCicloKernel() {
		this.ciclo++;
		gerateLists();
		boolean temNovosTerminados = false;
		List<PCB> terminadosList = new ArrayList<>();
		for(PCB pcb: processos){
			//System.out.println("\nProcesso: " + pcb.idProcesso);
			if(pcb.operacoesFeitas == pcb.codigo.length) {
				pcb.updateEstado(PCB.Estado.TERMINADO);
				temNovosTerminados = true;
				terminadosList.add(pcb);
			}else{
				if(pcb.estado.equals(PCB.Estado.NOVO) && (pcb.idProcesso == this.ciclo - 1)){
					continue;
				}
				Operacao op = pcb.codigo[pcb.operacoesFeitas];
				if(op instanceof OperacaoES) {
					if(((OperacaoES) op).ciclos == 0) { 
						if(cpuExecutando()) {
							if((pcb.operacoesFeitas + 1) < pcb.codigo.length && !(pcb.codigo[pcb.operacoesFeitas + 1] instanceof OperacaoES)) {
								
								pcb.updateEstado(PCB.Estado.PRONTO);
								if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED) {
									filaProntos.add(pcb.idProcesso);
									filaEsperando.remove((Integer) pcb.idProcesso);
								}
								processosProntos.add(pcb.idProcesso);
							}
						} else if(filaEsperando.size() > 0 && pcb.idProcesso == filaEsperando.get(0)){
							pcb.updateEstado(PCB.Estado.EXECUTANDO);
							//System.out.println("Executando");
							if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED) {
								filaEsperando.remove((Integer) pcb.idProcesso);
							} else if(this.escalonador == Escalonador.SHORTEST_JOB_FIRST) {
								((PCB_SJF)pcb).contadorBurst++;
							}
						} else {
							pcb.updateEstado(PCB.Estado.ESPERANDO);
							pcb.contadorBurst = 0;
							if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED) {
								if(!filaEsperando.contains(pcb.idProcesso))
									filaEsperando.add(pcb.idProcesso);
							} 
							processosEmEspera.add(pcb.idProcesso);
						}
						pcb.operacoesFeitas += 1;
					} else {
						if(pcb.estado == PCB.Estado.EXECUTANDO) {
							this.idProcessoAtual = -1;
							if(this.escalonador == Escalonador.SHORTEST_JOB_FIRST) {
								pcb.mediaExponencial();
								((PCB_SJF)pcb).contadorBurst = 0;
							}
						}
						pcb.updateEstado(PCB.Estado.ESPERANDO);
						processosEmEspera.add(pcb.idProcesso);
						if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED) {
							if(!filaEsperando.contains(pcb.idProcesso))
								filaEsperando.add(pcb.idProcesso);
							//System.out.println("\n" + pcb.idProcesso);
						}
					}
				}
				else {
					if(pcb.estado.equals(PCB.Estado.NOVO)){
						pcb.updateEstado(PCB.Estado.PRONTO);
						if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED)
							filaProntos.add(pcb.idProcesso);
					}
					if(pcb.estado.equals(PCB.Estado.PRONTO)){
						if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED) {
							if(!cpuExecutando() && filaProntos.get(0) == pcb.idProcesso) {
								pcb.updateEstado(PCB.Estado.EXECUTANDO);
								filaProntos.remove(0);	
							}
						} else if(this.escalonador == Escalonador.SHORTEST_JOB_FIRST) {
							((PCB_SJF)pcb).contadorBurst = 0;
							if(!cpuExecutando() && processosProntos.size() > 0
							&& processosProntos.get(0) == pcb.idProcesso) {
								pcb.updateEstado(PCB.Estado.EXECUTANDO);
								processosProntos.remove(0);
								((PCB_SJF)pcb).contadorBurst++;
							}
							processos.sort(pcb);
							pcb.contadorBurst = 0;
							int aux = -1;
							for(PCB processo: processos){
								if(processo.operacoesFeitas < processo.codigo.length) {
									if(!(processo.codigo[processo.operacoesFeitas] instanceof OperacaoES)) {
										aux = processo.idProcesso;
										break;
									}
								}
							}
							if(processosProntos.size() > 0) {
								if(!cpuExecutando() && aux == pcb.idProcesso) {
									pcb.updateEstado(PCB.Estado.EXECUTANDO);
									processosProntos.remove(0);
									pcb.contadorBurst++;
								}
							}
						} else if(!cpuExecutando() && processos.get(0).idProcesso == pcb.idProcesso ) {
							pcb.updateEstado(PCB.Estado.EXECUTANDO);
						}
					} else if(pcb.estado.equals(PCB.Estado.EXECUTANDO)){
						if(this.escalonador.equals(Escalonador.FIRST_COME_FIRST_SERVED)) {
							continue;
						} else if(this.escalonador.equals(Escalonador.SHORTEST_JOB_FIRST)) {
							pcb.contadorBurst++;
							continue;
						} else if(processos.get(0).idProcesso != pcb.idProcesso){
							trocaContexto(pcb, processos.get(0));
						}
					}
						
							
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
		//System.out.println("Processo atual na CPU" + idProcessoAtual);
		imprimirFila(this.filaProntos, "prontos");
		imprimirFila(this.filaEsperando, "esperando");

	}

	private void imprimirFila(List<Integer> fila, String nome) {
		//System.out.println("Imprimindo fila" + fila.toString() + nome);
	}

	private boolean cpuExecutando() {
		// PCB PCBAtual = getPCBAtual();
		// assert PCBAtual != null;
		// return PCBAtual.idProcesso == this.idProcessoAtual;
		return this.idProcessoAtual > -1;
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
		processosProntos.sort(Comparator.comparingInt(o -> o));
		return this.processosProntos;
	}

	@Override
	protected Integer idProcessoExecutando() {
		return this.idProcessoAtual;
	}

	@Override
	protected List<Integer> idProcessosEsperando() {
		gerateLists();
		processosEmEspera.sort(Comparator.comparingInt(o -> o));
		return this.processosEmEspera;
	}

	@Override
	protected List<Integer> idProcessosTerminados() {
		gerateLists();
		processosTerminados.sort(Comparator.comparingInt(o -> o));
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
		idProcessoNovo = -1;
		idProcessoAtual = -1;
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
		if(this.escalonador == Escalonador.FIRST_COME_FIRST_SERVED) {
			for(PCB processo: processos) {
				if(processo.idProcesso == idProcessoAtual) {
					return processo;
				}				
			}
		} 
		for(PCB processo: processos) {
			if(filaProntos.size() > 0){
				if (processo.idProcesso == filaProntos.get(0)){
					filaProntos.remove(0);
					return processo;
				}
			}
		}
		return processos.size() != 0 ?  processos.get(0): null;
	}

}
