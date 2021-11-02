package kernel.PCB;

import operacoes.Operacao;

public class PCB_RR extends PCB{

    public PCB_RR(Operacao[] codigo, int cicloEntrada) {
        super(codigo, cicloEntrada);
    }

    @Override
    public int compare(PCB o1, PCB o2) {
        int LIMITE_DE_OPERACOES = 5;
        int answer = ((int) (o1.ciclosExecutando / LIMITE_DE_OPERACOES)) - ((int) o2.ciclosExecutando / LIMITE_DE_OPERACOES);
        return answer != 0? answer : o1.cicloEntrada - o2.cicloEntrada;
    }
}
