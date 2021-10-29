package kernel.PCB;

import operacoes.Operacao;

public class PCB_SJF extends PCB{
    public contadorBurst;
    long processosFaltantes;
    public proxChuteBurstCPU;

    public PCB_SJF(Operacao[] codigo, int proxChuteBurstCPU) {
        super(codigo);
        this.processosFaltantes = codigo.length;
        this.proxChuteBurstCPU = proxChuteBurstCPU;
    }

    @Override
    public int compare(PCB o1, PCB o2) {
        if(!(o1 instanceof PCB_SJF) || !(o2 instanceof PCB_SJF) )
            throw new RuntimeException("Objetos não comparaveis");

        int answer = (int) (((PCB_SJF) o1).proxChuteBurstCPU - ((PCB_SJF) o2).proxChuteBurstCPU);

        return answer != 0 ? answer : o1.idProcesso - o2.idProcesso;
    }
}