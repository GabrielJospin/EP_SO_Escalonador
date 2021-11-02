package kernel.PCB;

import operacoes.Operacao;

public class PCB_FCFS extends PCB{


    public PCB_FCFS(Operacao[] codigo, int cicloEntrada) {
        super(codigo, cicloEntrada);
    }


    @Override
    public int compare(PCB o1, PCB o2) {
        if(!(o1 instanceof PCB_FCFS) || !(o2 instanceof PCB_FCFS) )
            throw new RuntimeException("Objetos não comparaveis");

        return o1.cicloEntrada - o2.cicloEntrada;
    }
}
