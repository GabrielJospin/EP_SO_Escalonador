package kernel;
import operacoes.Operacao;

public class PCB implements Comparable {


	public enum Estado {NOVO, PRONTO, EXECUTANDO, ESPERANDO, TERMINADO};
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
	public int compareTo(Object o) {
		if(!(o instanceof PCB))
			return 404;

		if(this.proximoChute < ((PCB) o).proximoChute)
			return -1;
		else if(this.proximoChute > ((PCB) o).proximoChute)
			return 1;
		else
		if (this.idProcesso < ((PCB) o).idProcesso)
			return -1;
		else
			return 1;
	}







}
