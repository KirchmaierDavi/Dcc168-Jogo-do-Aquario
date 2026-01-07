package com.dcc168.jogodoaquaria.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AquarioTest {

    @Test
    public void testPeixeAMovimentoLivre() {
        Aquario aq = new Aquario(5, 5, 10, 10, 10, 10);
        PeixeA peixe = new PeixeA(2, 2);
        aq.adicionarPeixe(peixe);

        aq.proximaIteracao();

        PeixeA peixeMovido = (PeixeA) encontrarPeixePorTipo(aq, 'A');
        assertNotNull(peixeMovido);
        assertTrue(deslocou(2, 2, peixeMovido.getX(), peixeMovido.getY()), "Peixe deve ter mudado de posição");
        assertEquals(1, peixeMovido.getMovimentosConsecutivos());
        assertEquals(0, peixeMovido.getTurnosSemMover());
    }

    @Test
    public void testPeixeABloqueadoNaoMove() {
        Aquario aq = new Aquario(3, 3, 10, 50, 10, 10);

        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                aq.adicionarPeixe(new PeixeA(i, j));
            }
        }

        PeixeA peixeMeio = (PeixeA) aq.getPeixeEm(1, 1);

        aq.proximaIteracao();

        assertEquals(1, peixeMeio.getX());
        assertEquals(1, peixeMeio.getY());
        assertEquals(0, peixeMeio.getMovimentosConsecutivos());
        assertEquals(1, peixeMeio.getTurnosSemMover());
    }

    @Test
    public void testPeixeAReproducaoSucesso() {
        Aquario aq = new Aquario(5, 5, 1, 10, 10, 10);
        PeixeA peixe = new PeixeA(2, 2);
        aq.adicionarPeixe(peixe);

        aq.proximaIteracao();

        assertEquals(2, aq.contarPeixeA(), "Deve haver 2 Peixes A (Pai e Filho)");
    }

    @Test
    public void testPeixeAMorteFome() {
        Aquario aq = new Aquario(1, 1, 10, 2, 10, 10);
        aq.adicionarPeixe(new PeixeA(0, 0));

        aq.proximaIteracao();
        assertNotNull(aq.getPeixeEm(0, 0));

        aq.proximaIteracao();
        assertNull(aq.getPeixeEm(0, 0));
    }

    @Test
    public void testPeixeASobreviveLimiteFome() {
        Aquario aq = new Aquario(1, 1, 10, 2, 10, 10);
        PeixeA p = new PeixeA(0, 0);
        aq.adicionarPeixe(p);

        aq.proximaIteracao();
        assertNotNull(aq.getPeixeEm(0, 0));
        assertEquals(1, p.getTurnosSemMover());
    }

    @Test
    public void testPeixeBComerPrioridade() {
        Aquario aq = new Aquario(1, 3, 10, 10, 10, 10);
        PeixeB b = new PeixeB(0, 0);
        PeixeA a = new PeixeA(0, 1);
        aq.adicionarPeixe(b);
        aq.adicionarPeixe(a);

        aq = new Aquario(2, 2, 10, 10, 10, 10);
        aq.adicionarPeixe(new PeixeB(0, 0));
        aq.adicionarPeixe(new PeixeA(0, 1));

        aq.proximaIteracao();

        assertNull(aq.getPeixeEm(0, 0));
        Peixe p = aq.getPeixeEm(0, 1);
        assertNotNull(p);
        assertEquals('B', p.getTipo());
        assertEquals(0, aq.contarPeixeA());

        PeixeB pb = (PeixeB) p;
        assertEquals(1, pb.getContagemComeu());
        assertEquals(0, pb.getTurnosSemComer());
    }

    @Test
    public void testPeixeBMoveSemComida() {
        Aquario aq = new Aquario(5, 5, 10, 10, 10, 10);
        PeixeB b = new PeixeB(2, 2);
        aq.adicionarPeixe(b);

        aq.proximaIteracao();

        assertTrue((b.getX() != 2 || b.getY() != 2), "B deve mover");
        assertEquals(0, b.getContagemComeu());
        assertEquals(1, b.getTurnosSemComer(), "Fome aumenta se não comer");
    }

    @Test
    public void testPeixeBEstagnado() {
        Aquario aq = new Aquario(3, 3, 10, 10, 10, 50);
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                aq.adicionarPeixe(new PeixeB(i,j));
            }
        }

        PeixeB centro = (PeixeB) aq.getPeixeEm(1, 1);
        aq.proximaIteracao();

        assertEquals(1, centro.getX());
        assertEquals(1, centro.getY());
        assertEquals(1, centro.getTurnosSemComer());
    }

    @Test
    public void testPeixeBReproducaoSucesso() {
        Aquario aq = new Aquario(3, 3, 10, 10, 1, 10);
        PeixeB b = new PeixeB(1, 1);
        PeixeA a = new PeixeA(1, 2);
        aq.adicionarPeixe(b);
        aq.adicionarPeixe(a);

        aq.proximaIteracao();

        assertEquals(2, aq.contarPeixeB(), "Deve reproduzir após comer");
        PeixeB pai = (PeixeB) encontrarPeixeBComFomeZero(aq);
        assertNotNull(pai);
        assertEquals(0, pai.getContagemComeu(), "Contagem de comida reseta ao reproduzir");
    }

    @Test
    public void testPeixeBReproducaoFalhaPorVizinhoB() {
        Aquario aq = new Aquario(3, 3, 10, 10, 1, 10);
        aq.adicionarPeixe(new PeixeB(0, 0));
        aq.adicionarPeixe(new PeixeA(0, 1));
        aq.adicionarPeixe(new PeixeB(1, 1));

        aq.proximaIteracao();

        assertEquals(2, aq.contarPeixeB(), "Não deve reproduzir se houver vizinho B");
        assertEquals(0, aq.contarPeixeA());
    }

    @Test
    public void testPeixeBMorteFome() {
        Aquario aq = new Aquario(1, 1, 10, 10, 10, 2);
        aq.adicionarPeixe(new PeixeB(0, 0));

        aq.proximaIteracao();
        assertNotNull(aq.getPeixeEm(0, 0));

        aq.proximaIteracao();
        assertNull(aq.getPeixeEm(0, 0));
    }

    @Test
    public void testMorteFomeComidaReseta() {
        Aquario aq = new Aquario(1, 3, 10, 10, 10, 2);
        PeixeB b = new PeixeB(0, 0);
        PeixeA a = new PeixeA(0, 2);
        aq.adicionarPeixe(b);
        aq.adicionarPeixe(a);

        aq.proximaIteracao();
        PeixeB bMoved = (PeixeB) aq.getPeixeEm(0, 1);
        if (bMoved == null) {
             bMoved = (PeixeB) aq.getPeixeEm(0,0);
        }
        assertNotNull(bMoved);

        aq.proximaIteracao();
        assertEquals(0, aq.contarPeixeA(), "A deve ter sido comido");
        assertEquals(1, aq.contarPeixeB(), "B vive");

        PeixeB bSurvivor = (PeixeB) encontrarPeixePorTipo(aq, 'B');
        assertEquals(0, bSurvivor.getTurnosSemComer(), "Fome resetada");
    }

    @Test
    public void testAdicionarPeixeForaDoLimite() {
        Aquario aq = new Aquario(5, 5, 10, 10, 10, 10);
        aq.adicionarPeixe(new PeixeA(-1, 0));
        aq.adicionarPeixe(new PeixeA(0, -1));
        aq.adicionarPeixe(new PeixeA(5, 0));
        aq.adicionarPeixe(new PeixeA(0, 5));

        assertEquals(0, aq.contarPeixeA());
    }

    @Test
    public void testMovimentoNosCantos() {
        Aquario aq = new Aquario(2, 2, 10, 10, 10, 10);
        PeixeA p = new PeixeA(0, 0);
        aq.adicionarPeixe(p);

        aq.proximaIteracao();

        assertTrue(p.getX() <= 1 && p.getY() <= 1);
        assertTrue(p.getX() >= 0 && p.getY() >= 0);
        assertTrue(p.getX() != 0 || p.getY() != 0);
    }

    @Test
    public void testExtincaoTotal() {
        Aquario aq = new Aquario(2, 2, 10, 2, 10, 2);
        aq.adicionarPeixe(new PeixeA(0,0));
        aq.adicionarPeixe(new PeixeB(1,1));

        for(int i=0; i<5; i++) aq.proximaIteracao();

        assertEquals(0, aq.contarPeixeA());
        assertEquals(0, aq.contarPeixeB());
    }

    @Test
    public void testPeixeAReproducaoCicloRA() {
        Aquario aq = new Aquario(1, 10, 2, 10, 10, 10);
        PeixeA peixe = new PeixeA(0, 0);
        aq.adicionarPeixe(peixe);

        aq.proximaIteracao();
        assertEquals(1, aq.contarPeixeA(), "Nao deve reproduzir na primeira de 2 movimentacoes");

        aq.proximaIteracao();
        assertEquals(2, aq.contarPeixeA(), "Deve reproduzir na segunda movimentacao");
    }

    @Test
    public void testPeixeBReproducaoCicloRB() {
        // Reduzido para 3 colunas para impedir que o segundo Peixe A fuja para (0,3)
        Aquario aq = new Aquario(1, 3, 10, 10, 2, 10);
        aq.adicionarPeixe(new PeixeB(0, 0));
        aq.adicionarPeixe(new PeixeA(0, 1));
        aq.adicionarPeixe(new PeixeA(0, 2));

        aq.proximaIteracao();
        assertEquals(1, aq.contarPeixeB(), "Nao deve reproduzir apos comer 1 peixe (RB=2)");
        assertEquals(1, aq.contarPeixeA(), "Deve sobrar 1 peixe A");

        aq.proximaIteracao();

        assertEquals(0, aq.contarPeixeA(), "Nao deve sobrar peixes A (significa que B comeu)");
        assertEquals(2, aq.contarPeixeB(), "Deve reproduzir apos comer o segundo peixe");
    }

    @Test
    public void testGridCheioPeixesNaoMovem() {
        Aquario aq = new Aquario(3, 3, 1, 10, 10, 10);
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                aq.adicionarPeixe(new PeixeA(i, j));
            }
        }

        aq.proximaIteracao();

        assertEquals(9, aq.contarPeixeA());

        PeixeA p = (PeixeA) aq.getPeixeEm(1, 1);
        assertEquals(1, p.getTurnosSemMover());
        assertEquals(0, p.getMovimentosConsecutivos());
    }

    private Peixe encontrarPeixePorTipo(Aquario aq, char tipo) {
        for(int i=0; i<aq.getLinhas(); i++) {
            for(int j=0; j<aq.getColunas(); j++) {
                Peixe p = aq.getPeixeEm(i, j);
                if (p != null && p.getTipo() == tipo) return p;
            }
        }
        return null;
    }

    private PeixeB encontrarPeixeBComFomeZero(Aquario aq) {
        for(int i=0; i<aq.getLinhas(); i++) {
            for(int j=0; j<aq.getColunas(); j++) {
                Peixe p = aq.getPeixeEm(i, j);
                if (p instanceof PeixeB && ((PeixeB)p).getTurnosSemComer() == 0) return (PeixeB)p;
            }
        }
        return null;
    }

    private boolean deslocou(int x1, int y1, int x2, int y2) {
        return x1 != x2 || y1 != y2;
    }
}

