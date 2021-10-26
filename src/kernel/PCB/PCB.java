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
	 e criar um método chamado compareTo(Object o)
	 nele vc vai ter o seguinte esquema
	 	-> Testar se for do tipo PCB
	 		-> se não for, gera um erro
	 		-> Se for:
	 			-> Caso seja mais prioritário o objeto O, ou seja vem antes no array, retorna -1;
	 			-> Caso seja menos prioritario, ou seja, venha dps. retorna +1;
	 			-> Caso empate 0, mas isso não deve ocorre no nosso caso

	 Exemplo simples de compareTo()

	 public int compareTo(Object o){
	 	if(! o instance of Integer){
	 		throw new RuntimeException("Objeto inválido");
	 	}

	 	if( o.valor < this.valor)
	 		return -1;
		if( o.valor > this.valor)
			return 1;
	 	else // o.valor == this.valor
	 		return 0;
	 }

*/
}
