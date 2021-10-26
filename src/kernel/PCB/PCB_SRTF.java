package kernel.PCB;

import operacoes.Operacao;

public class PCB_SRTF extends PCB{

    public PCB_SRTF(Operacao[] codigo) {
        super(codigo);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
