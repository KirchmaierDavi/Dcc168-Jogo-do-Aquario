package com.dcc168.jogodoaquaria;

import com.dcc168.jogodoaquaria.model.Aquario;
import com.dcc168.jogodoaquaria.model.PeixeA;
import com.dcc168.jogodoaquaria.model.PeixeB;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Scanner;

@Component
@Profile("!test")
public class GameRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Jogo do Aquario ===");

        int linhas = 10, colunas = 10;
        int x = 5, y = 5;
        int ra = 3, ma = 10, rb = 3, mb = 10;

        try {
            System.out.print("Digite as Linhas (M): ");
            if (System.console() != null || scanner.hasNext()) {
                if (scanner.hasNextInt()) linhas = scanner.nextInt();

                System.out.print("Digite as Colunas (N): ");
                if (scanner.hasNextInt()) colunas = scanner.nextInt();

                System.out.print("Digite o numero de Peixes do tipo A (X): ");
                if (scanner.hasNextInt()) x = scanner.nextInt();

                System.out.print("Digite o numero de Peixes do tipo B (Y): ");
                if (scanner.hasNextInt()) y = scanner.nextInt();

                System.out.print("Reproducao Peixe A (RA): ");
                if (scanner.hasNextInt()) ra = scanner.nextInt();

                System.out.print("Vida Peixe A (MA): ");
                if (scanner.hasNextInt()) ma = scanner.nextInt();

                System.out.print("Reproducao Peixe B (RB): ");
                if (scanner.hasNextInt()) rb = scanner.nextInt();

                System.out.print("Vida Peixe B (MB): ");
                if (scanner.hasNextInt()) mb = scanner.nextInt();

                scanner.nextLine();
            } else {
                 System.out.println("Modo não interativo detectado. Usando valores padrão.");
            }
        } catch (Exception e) {
            System.out.println("Entrada invalida, usando padroes.");
            scanner.nextLine();
        }

        Aquario aquario = new Aquario(linhas, colunas, ra, ma, rb, mb);
        Random random = new Random();

        int peixesAPosicionados = 0;
        int tentativas = 0;
        while(peixesAPosicionados < x && tentativas < (linhas * colunas * 2)) {
            int r = random.nextInt(linhas);
            int c = random.nextInt(colunas);
            if(aquario.getPeixeEm(r, c) == null) {
                aquario.adicionarPeixe(new PeixeA(r, c));
                peixesAPosicionados++;
            }
            tentativas++;
        }

        int peixesBPosicionados = 0;
        tentativas = 0;
        while(peixesBPosicionados < y && tentativas < (linhas * colunas * 2)) {
            int r = random.nextInt(linhas);
            int c = random.nextInt(colunas);
            if(aquario.getPeixeEm(r, c) == null) {
                aquario.adicionarPeixe(new PeixeB(r, c));
                peixesBPosicionados++;
            }
            tentativas++;
        }

        int iteracao = 0;
        boolean executando = true;

        while(executando && aquario.contarPeixeB() > 0) {
            System.out.println("\n--- Iteracao " + iteracao + " ---");
            System.out.println("Peixe A: " + aquario.contarPeixeA() + " | Peixe B: " + aquario.contarPeixeB());
            aquario.imprimirGrade();

            System.out.println("Pontuacao (Iteracoes): " + iteracao);
            System.out.println("Pressione Enter para continuar, 'q' para sair...");

            if (System.console() != null || scanner.hasNextLine()) {
                String entrada = scanner.nextLine();
                if(entrada.trim().equalsIgnoreCase("q")) {
                    executando = false;
                } else {
                    aquario.proximaIteracao();
                    iteracao++;
                }
            } else {
                if (iteracao > 100) executando = false;
                aquario.proximaIteracao();
                iteracao++;
            }
        }

        System.out.println("\n=== Fim de Jogo ===");
        if (aquario.contarPeixeB() == 0) {
            System.out.println("Extincao dos Peixes B.");
        }
        System.out.println("Pontuacao Final (Iteracoes): " + iteracao);
    }
}

