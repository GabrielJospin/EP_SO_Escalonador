package kernel.PCB;

import operacoes.Operacao;

public class PCB_SRTF extends PCB{


    public PCB_SRTF(Operacao[] codigo) {
        super(codigo);
    }

    public long getProcessosFaltantes() {
        return super.proximoChute - super.operacoesFeitas;
    }

    @Override
    public int compare(PCB o1, PCB o2) {
        if(!(o1 instanceof PCB_SRTF) || !(o2 instanceof PCB_SRTF) )
            throw new RuntimeException("Objetos não comparaveis");

        int answer = (int) (((PCB_SRTF) o1).getProcessosFaltantes() - ((PCB_SRTF) o2).getProcessosFaltantes());

        return answer != 0 ? answer : o1.idProcesso - o2.idProcesso;
    }
}
