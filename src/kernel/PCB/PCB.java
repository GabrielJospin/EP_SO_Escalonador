package kernel.PCB;
import operacoes.Operacao;

//Freire comunista confirmado
public abstract class PCB implements Comparable {


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
	public abstract int compareTo(Object o);






}
