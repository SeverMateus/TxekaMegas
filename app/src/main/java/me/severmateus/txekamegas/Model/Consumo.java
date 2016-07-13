package me.severmateus.txekamegas.Model;

/**
 * Created by User on 06/08/2015.
 */
public class Consumo {

    private int codConsumo;
    private long consumoInicial, consumoGasto, consumoInicialCell, tempoDefinido;

    public Consumo() {
        super();
    }

    public Consumo(int codConsumo, long consumoInicial, long consumoGasto, long consumoInicialCell, long tempoDefinido) {
        super();
        this.codConsumo = codConsumo;
        this.consumoInicial = consumoInicial;
        this.consumoGasto = consumoGasto;
        this.consumoInicialCell = consumoInicialCell;
        this.tempoDefinido = tempoDefinido;
    }

    public int getCodConsumo() {
        return codConsumo;
    }

    public void setCodConsumo(int codConsumo) {
        this.codConsumo = codConsumo;
    }

    public long getConsumoInicial() {
        return consumoInicial;
    }

    public void setConsumoInicial(long consumoInicial) {
        this.consumoInicial = consumoInicial;
    }

    public long getConsumoGasto() {
        return consumoGasto;
    }

    public void setConsumoGasto(long consumoGasto) {
        this.consumoGasto = consumoGasto;
    }

    public long getConsumoInicialCell() {
        return consumoInicialCell;
    }

    public void setConsumoInicialCell(long consumoInicialCell) {
        this.consumoInicialCell = consumoInicialCell;
    }

    public long getTempoDefinido() {
        return tempoDefinido;
    }

    public void setTempoDefinido(long tempoDefinido) {
        this.tempoDefinido = tempoDefinido;
    }

    @Override
    public String toString() {
        return "Consumo{" +
                "codConsumo=" + codConsumo +
                ", consumoInicial=" + consumoInicial +
                ", consumoGasto=" + consumoGasto +
                ", consumoInicialCell=" + consumoInicialCell +
                ", tempoDefinido=" + tempoDefinido +
                '}';
    }
}
