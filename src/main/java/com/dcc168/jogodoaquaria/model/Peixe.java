package com.dcc168.jogodoaquaria.model;

public abstract class Peixe {
    protected int x;
    protected int y;
    protected boolean moveuNestaRodada;

    public Peixe(int x, int y) {
        this.x = x;
        this.y = y;
        this.moveuNestaRodada = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosicao(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean moveuNestaRodada() { return moveuNestaRodada; }
    public void setMoveuNestaRodada(boolean moveu) { this.moveuNestaRodada = moveu; }

    public abstract char getTipo();
}

