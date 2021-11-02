package kernel.PCB;
import operacoes.Operacao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

//Freire comunista confirmado
public abstract class PCB implements Comparator<PCB> {


	public enum Estado {NOVO, PRONTO, EXECUTANDO, ESPERANDO, TERMINADO}
	public int idProcesso; // primeiro processo criado deve ter id = 0
	public Estado estado;
	public int[] registradores;
	public int contadorDePrograma;
	public Operacao[] codigo;
	public int proximoChute;
	public static int processosfeitos = 0;
	public int operacoesFeitas;
	public int ciclosExecutando;

	private LocalDateTime tempoEsperaInicio;
	public int tempoEspera;

	private LocalDateTime tempoRetornoInicio;
	public int tempoRetorno;

	public PCB( Operacao[] codigo) {
		this.idProcesso = processosfeitos;
		this.estado = Estado.NOVO;
		this.registradores = new int[5];
		this.contadorDePrograma = 0;
		this.codigo = codigo;
		this.proximoChute = 5;
		this.operacoesFeitas = 0;
		this.tempoEspera = 0;
		this.tempoRetornoInicio = LocalDateTime.now();
		this.tempoRetorno = 0;
		this.ciclosExecutando = 0;
		processosfeitos++;
	}

	public void updateEstado(Estado estado){
		if(estado.equals(Estado.PRONTO))
			this.tempoEsperaInicio = LocalDateTime.now();

		if(this.estado.equals(Estado.PRONTO)){
			int delta = (int) tempoEsperaInicio.until(LocalDateTime.now(), ChronoUnit.MICROS);
			this.tempoEspera += delta;
		}

		if(estado.equals(Estado.TERMINADO)){
			int delta = (int) tempoRetornoInicio.until(LocalDateTime.now(), ChronoUnit.MICROS);
			this.tempoRetorno += delta;
			if(this instanceof PCB_SRTF)
				this.proximoChute = (proximoChute+this.ciclosExecutando)/2;
		}


		this.estado = estado;
	}

	public  int getTempoResposta(){
		if(operacoesFeitas > 0)
			return (tempoRetorno - tempoEspera)/operacoesFeitas;
		return -1;
	}

	@Override
	public abstract int compare(PCB o1, PCB o2);
/*

	O COMPARE TO

	 A ideia aqui é mais simples doq parece...
	 Vc vai criar uma nova classe PCB_{Escalonador}
	 Ai nele vc deve adicionar um "Extends PCB" na classe
	 criar um contrutor com super();
	 e criar um método chamado compare(Object o)
	 nele vc vai ter o seguinte esquema
	 	-> Testar se for do tipo PCB
	 		-> se não for, gera um erro
	 		-> Se for:
	 			-> Retorna valor 1 menos valor 2 (Se o valor for 0,
	 			usar critério de desempate de ordem de chegada)

	 Exemplo simples de compareTo()

	 public int compareTo(Object o1, Object o2){
	 	if(! o instance of Integer){
	 		throw new RuntimeException("Objeto inválido");
	 	}
	 		return o1.valor - o2.valor;

	 }

*/
}
