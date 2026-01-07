package com.dcc168.jogodoaquaria.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Aquario {
    private int linhas;
    private int colunas;
    private Peixe[][] grade;
    private int RA, MA, RB, MB;
    private Random random;

    public Aquario(int linhas, int colunas, int RA, int MA, int RB, int MB) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.RA = RA;
        this.MA = MA;
        this.RB = RB;
        this.MB = MB;
        this.grade = new Peixe[linhas][colunas];
        this.random = new Random();
    }

    public void adicionarPeixe(Peixe peixe) {
        if (eValido(peixe.getX(), peixe.getY())) {
            grade[peixe.getX()][peixe.getY()] = peixe;
        }
    }

    public Peixe getPeixeEm(int x, int y) {
        if (eValido(x, y)) {
            return grade[x][y];
        }
        return null;
    }

    public int getLinhas() { return linhas;}
    public int getColunas() { return colunas;}

    private boolean eValido(int x, int y) {
        return x >= 0 && x < linhas && y >= 0 && y < colunas;
    }

    public void proximaIteracao() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (grade[i][j] != null) {
                    grade[i][j].setMoveuNestaRodada(false);
                }
            }
        }

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Peixe peixe = grade[i][j];
                if (peixe != null && !peixe.moveuNestaRodada()) {
                    processarPeixe(peixe);
                }
            }
        }
    }

    private void processarPeixe(Peixe peixe) {
        if (peixe instanceof PeixeA) {
            processarPeixeA((PeixeA) peixe);
        } else if (peixe instanceof PeixeB) {
            processarPeixeB((PeixeB) peixe);
        }
    }

    private void processarPeixeA(PeixeA peixe) {
        List<int[]> vizinhosLivres = getVizinhosLivres(peixe.getX(), peixe.getY());

        if (!vizinhosLivres.isEmpty()) {
            int[] movimento = vizinhosLivres.get(random.nextInt(vizinhosLivres.size()));
            int novoX = movimento[0];
            int novoY = movimento[1];
            int velhoX = peixe.getX();
            int velhoY = peixe.getY();

            grade[velhoX][velhoY] = null;
            grade[novoX][novoY] = peixe;
            peixe.setPosicao(novoX, novoY);
            peixe.setMoveuNestaRodada(true);
            peixe.incrementarMovimentosConsecutivos();
            peixe.zerarTurnosSemMover();

             if (peixe.getMovimentosConsecutivos() == RA) {
                List<int[]> vizinhosReprod = getVizinhosLivres(peixe.getX(), peixe.getY());
                if (!vizinhosReprod.isEmpty()) {
                    int[] prole = vizinhosReprod.get(random.nextInt(vizinhosReprod.size()));
                    PeixeA filho = new PeixeA(prole[0], prole[1]);
                    filho.setMoveuNestaRodada(true);
                    adicionarPeixe(filho);
                    peixe.zerarMovimentosConsecutivos();
                }
            }

        } else {
            peixe.setMoveuNestaRodada(true);
            peixe.zerarMovimentosConsecutivos();
            peixe.incrementarTurnosSemMover();

            if (peixe.getTurnosSemMover() == MA) {
                grade[peixe.getX()][peixe.getY()] = null;
            }
        }
    }

    private void processarPeixeB(PeixeB peixe) {
        List<int[]> vizinhosA = getVizinhosComTipoPeixe(peixe.getX(), peixe.getY(), 'A');

        if (!vizinhosA.isEmpty()) {
            int[] movimento = vizinhosA.get(random.nextInt(vizinhosA.size()));
            int novoX = movimento[0];
            int novoY = movimento[1];
            int velhoX = peixe.getX();
            int velhoY = peixe.getY();

            grade[velhoX][velhoY] = null;
            grade[novoX][novoY] = peixe;
            peixe.setPosicao(novoX, novoY);
            peixe.setMoveuNestaRodada(true);

            peixe.incrementarContagemComeu();
            peixe.zerarTurnosSemComer();

            if (peixe.getContagemComeu() == RB) {
                List<int[]> vizinhosB = getVizinhosComTipoPeixe(peixe.getX(), peixe.getY(), 'B');
                if (vizinhosB.isEmpty()) {
                   List<int[]> vizinhosLivres = getVizinhosLivres(peixe.getX(), peixe.getY());
                   if (!vizinhosLivres.isEmpty()) {
                       int[] prole = vizinhosLivres.get(random.nextInt(vizinhosLivres.size()));
                       PeixeB filho = new PeixeB(prole[0], prole[1]);
                       filho.setMoveuNestaRodada(true);
                       adicionarPeixe(filho);
                       peixe.zerarContagemComeu();
                   }
                }
            }

        } else {
             List<int[]> vizinhosLivres = getVizinhosLivres(peixe.getX(), peixe.getY());
             if (!vizinhosLivres.isEmpty()) {
                 int[] movimento = vizinhosLivres.get(random.nextInt(vizinhosLivres.size()));
                 int novoX = movimento[0];
                 int novoY = movimento[1];
                 int velhoX = peixe.getX();
                 int velhoY = peixe.getY();

                 grade[velhoX][velhoY] = null;
                 grade[novoX][novoY] = peixe;
                 peixe.setPosicao(novoX, novoY);
                 peixe.setMoveuNestaRodada(true);

                 peixe.incrementarTurnosSemComer();
             } else {
                 peixe.setMoveuNestaRodada(true);
                 peixe.incrementarTurnosSemComer();
             }
        }

        if (peixe.getTurnosSemComer() >= MB) {
             grade[peixe.getX()][peixe.getY()] = null;
        }
    }

    public List<int[]> getVizinhosLivres(int x, int y) {
        return getVizinhosComCondicao(x, y, (nx, ny) -> grade[nx][ny] == null);
    }

    public List<int[]> getVizinhosComTipoPeixe(int x, int y, char tipo) {
        return getVizinhosComCondicao(x, y, (nx, ny) -> grade[nx][ny] != null && grade[nx][ny].getTipo() == tipo);
    }

    @FunctionalInterface
    interface CondicaoVizinho {
        boolean test(int nx, int ny);
    }

    private List<int[]> getVizinhosComCondicao(int x, int y, CondicaoVizinho condicao) {
        List<int[]> lista = new ArrayList<>();
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int k = 0; k < 8; k++) {
            int nx = x + dx[k];
            int ny = y + dy[k];
            if (eValido(nx, ny) && condicao.test(nx, ny)) {
                lista.add(new int[]{nx, ny});
            }
        }
        return lista;
    }

    public int contarPeixeA() {
        int contagem = 0;
        for (int i = 0; i < linhas; i++) {
             for (int j = 0; j < colunas; j++) {
                 if (grade[i][j] != null && grade[i][j].getTipo() == 'A') contagem++;
             }
        }
        return contagem;
    }

    public int contarPeixeB() {
        int contagem = 0;
        for (int i = 0; i < linhas; i++) {
             for (int j = 0; j < colunas; j++) {
                 if (grade[i][j] != null && grade[i][j].getTipo() == 'B') contagem++;
             }
        }
        return contagem;
    }

      public void imprimirGrade() {
        for (int i = 0; i < linhas; i++) {
             for (int j = 0; j < colunas; j++) {
                 if (grade[i][j] == null) System.out.print(". ");
                 else System.out.print(grade[i][j].getTipo() + " ");
             }
             System.out.println();
        }
    }
}

