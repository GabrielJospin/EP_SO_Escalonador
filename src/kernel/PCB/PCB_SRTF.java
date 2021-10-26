package kernel.PCB;

import operacoes.Operacao;

public class PCB_SRTF extends PCB{

    long processosFaltantes;

    public PCB_SRTF(Operacao[] codigo) {
        super(codigo);
        this.processosFaltantes = codigo.length;
    }


    @Override
    public int compare(PCB o1, PCB o2) {
        if(!(o1 instanceof PCB_SRTF) || !(o2 instanceof PCB_SRTF) )
            throw new RuntimeException("Objetos não comparaveis");

        return (int) (((PCB_SRTF) o1).processosFaltantes - ((PCB_SRTF) o2).processosFaltantes);
    }
}
