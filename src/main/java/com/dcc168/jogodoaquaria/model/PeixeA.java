package com.dcc168.jogodoaquaria.model;

public class PeixeA extends Peixe {
    private int movimentosConsecutivos;
    private int turnosSemMover;

    public PeixeA(int x, int y) {
        super(x, y);
        this.movimentosConsecutivos = 0;
        this.turnosSemMover = 0;
    }

    public int getMovimentosConsecutivos() { return movimentosConsecutivos; }
    public void incrementarMovimentosConsecutivos() { this.movimentosConsecutivos++; }
    public void zerarMovimentosConsecutivos() { this.movimentosConsecutivos = 0; }

    public int getTurnosSemMover() { return turnosSemMover; }
    public void incrementarTurnosSemMover() { this.turnosSemMover++; }
    public void zerarTurnosSemMover() { this.turnosSemMover = 0; }

    @Override
    public char getTipo() { return 'A'; }
}

