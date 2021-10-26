package kernel.PCB;

import operacoes.Operacao;

public class PCB_SRTF extends PCB{

    long processosFaltantes;

    public PCB_SRTF(Operacao[] codigo) {
        super(codigo);
        this.processosFaltantes = codigo.length;
    }

    @Override
    public int compareTo(Object o) {
        if(! (o instanceof PCB_SRTF))
            throw new RuntimeException("Objeto de comparação não é PCB SRTF");
        if(processosFaltantes < ((PCB_SRTF) o).processosFaltantes)
            return -1;
        if(processosFaltantes > ((PCB_SRTF) o).processosFaltantes)
            return +1;
        return Integer.compare(this.idProcesso, ((PCB_SRTF) o).idProcesso);
    }
}
