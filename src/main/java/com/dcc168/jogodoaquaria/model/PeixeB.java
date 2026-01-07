package com.dcc168.jogodoaquaria.model;

public class PeixeB extends Peixe {
    private int contagemComeu;
    private int turnosSemComer;

    public PeixeB(int x, int y) {
        super(x, y);
        this.contagemComeu = 0;
        this.turnosSemComer = 0;
    }

    public int getContagemComeu() { return contagemComeu; }
    public void incrementarContagemComeu() { this.contagemComeu++; }
    public void zerarContagemComeu() { this.contagemComeu = 0; }

    public int getTurnosSemComer() { return turnosSemComer; }
    public void incrementarTurnosSemComer() { this.turnosSemComer++; }
    public void zerarTurnosSemComer() { this.turnosSemComer = 0; }

    @Override
    public char getTipo() { return 'B'; }
}

