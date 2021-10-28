package kernel.PCB;

import operacoes.Operacao;

public class PCB_RR extends PCB{

    public PCB_RR(Operacao[] codigo) {
        super(codigo);
    }

    @Override
    public int compare(PCB o1, PCB o2) {
        int LIMITE_DE_OPERACOES = 5;
        int answer = (o1.operacoesFeitas % LIMITE_DE_OPERACOES) - (o2.operacoesFeitas % LIMITE_DE_OPERACOES);
        return answer != 0? answer : o1.idProcesso - o2.idProcesso;
    }
}
