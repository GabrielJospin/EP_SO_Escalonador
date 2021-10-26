package kernel.PCB;
import operacoes.Operacao;

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

	public PCB( Operacao[] codigo) {
		this.idProcesso = 0;
		this.estado = Estado.NOVO;
		this.registradores = new int[5];
		this.contadorDePrograma = 0;
		this.codigo = codigo;
		this.proximoChute = 5;
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
	 			-> Retorna valor 1 menos valor 2

	 Exemplo simples de compareTo()

	 public int compareTo(Object o1, Object o2){
	 	if(! o instance of Integer){
	 		throw new RuntimeException("Objeto inválido");
	 	}
	 		return o1.valor - o2.valor;

	 }

*/
}
