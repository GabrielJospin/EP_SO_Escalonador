package kernel.PCB;

import operacoes.Operacao;

public class PCB_SRTF extends PCB{


    public PCB_SRTF(Operacao[] codigo, int cicloEntrada) {
        super(codigo, cicloEntrada);
    }

    public long getProcessosFaltantes() {
        return super.proximoChute - super.ciclosExecutando;
    }

    @Override
    public int compare(PCB o1, PCB o2) {
        if(!(o1 instanceof PCB_SRTF) || !(o2 instanceof PCB_SRTF) )
            throw new RuntimeException("Objetos não comparaveis");

        int answer = (int) (((PCB_SRTF) o1).getProcessosFaltantes() - ((PCB_SRTF) o2).getProcessosFaltantes());

        return answer != 0 ? answer : o1.cicloEntrada - o2.cicloEntrada;
    }


}
