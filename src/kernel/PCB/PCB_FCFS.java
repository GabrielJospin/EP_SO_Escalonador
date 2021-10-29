package kernel.PCB;

import operacoes.Operacao;

public class PCB_FCFS extends PCB{


    public PCB_FCFS(Operacao[] codigo) {
        super(codigo);
    }


    @Override
    public int compare(PCB o1, PCB o2) {
        if(!(o1 instanceof PCB_FCFS) || !(o2 instanceof PCB_FCFS) )
            throw new RuntimeException("Objetos não comparaveis");

        int answer = (int) (((PCB_FCFS) o1).idProcesso - ((PCB_FCFS) o2).idProcesso);

        return answer;
    }
}
