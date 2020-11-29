package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Labirinto {
		public static final String ANSI_GREEN = "\u001B[32m";
		public static final String ANSI_BLUE = "\u001B[34m";
		public static final String ANSI_RED = "\u001b[31m";
		public static final String ANSI_YELLOW = "\u001b[33;1m";
		public static final String ANSI_WHITE = "\u001b[37m";
		public static final String ANSI_RESET = "\u001b[0m";

		private int[][] lab;
		private Posicao inicio;
		private Posicao fim;
		private List<Posicao> paredes = new ArrayList<>();
		private int qtdZeros = 0;

		public Labirinto() {
				this.lab = new int[][]
						{{9,8,1,1,1,0,0,8,1,1},       //9 : entrada/saida;   8: moeda;    1:parede;  0:caminho livre
						{0,0,8,0,1,1,0,0,1,1},
						{1,1,1,0,0,8,0,0,1,0},
						{0,0,1,0,8,0,0,1,0,0},
						{0,1,1,0,8,8,0,1,0,0},
						{0,1,0,1,1,0,8,1,0,0},
						{0,1,1,0,8,0,0,1,0,0},
						{0,0,1,8,0,0,0,1,1,0},
						{0,0,1,0,8,0,0,8,0,0},
						{0,0,0,8,0,0,8,0,0,9}
				};

				inicio = new Posicao(0,0);
				fim = new Posicao(9,9);

		}

		public boolean isParede(Posicao p) {
				if (saiuLabirinto(p)) {
						return false;
				}
				int celula = lab[p.getPosX()][p.getPosY()];
				return celula == 1;
		}

		public boolean isChaoValido(Posicao p) {
				if (saiuLabirinto(p)) {
						return false;
				}
				int celula = lab[p.getPosX()][p.getPosY()];
				return celula == 0;
		}

		public boolean isMoeda(Posicao p) {
				if (saiuLabirinto(p)) {
						return false;
				}
				int celula = lab[p.getPosX()][p.getPosY()];
				return celula == 8;
		}

		public void coletarMoeda(Posicao p){
				if(isMoeda(p)){
						lab[p.getPosX()][p.getPosY()] = 0;
				}
		}

		public boolean saiuLabirinto(Posicao p) {
				boolean saiuX = p.getPosX() < inicio.getPosX() || p.getPosX() > fim.getPosX();
				boolean saiuY = p.getPosY() < inicio.getPosY() || p.getPosY() > fim.getPosY();
				return saiuX || saiuY;
		}

		public boolean isSaida(Posicao p) {
				return fim.equals(p);
		}

		public List<Posicao> getParedes() {
				return paredes;
		}

		public void addParede(Posicao p) {
				this.paredes.add(p);
		}

		public String printaLabirinto(int[][] labirinto) {
				String retorno = "\n \n ";
				for (int y = 0; y < labirinto.length; y++) {
						for (int x = 0; x < labirinto[y].length; x++) {
								if (labirinto[x][y] == 2 || labirinto[x][y] == 3) {
										System.out.print(ANSI_YELLOW + labirinto[x][y]);
								} else if (labirinto[x][y] == 0) {
										System.out.print(ANSI_WHITE + "0");
								} else if (labirinto[x][y] == 4) {
										System.out.print(ANSI_GREEN + labirinto[x][y]);
								} else if (labirinto[x][y] == 9) {
										System.out.print(ANSI_BLUE + labirinto[x][y]);
								} else {
										System.out.print(ANSI_RED + labirinto[x][y]);
								}
								System.out.print(" " + ANSI_RESET);
								retorno = retorno.concat(labirinto[x][y] + " ");
						}
						System.out.println(" \t" + ANSI_RESET);
						retorno = retorno.concat("\n ");
				}
				System.out.println("____________________________________________" + ANSI_RESET);
				return retorno.concat("___________________________________________ \n");
		}


		public Posicao getInicio() {
				return inicio;
		}

		public Posicao getFim() {
				return fim;
		}

		public int[][] getLab() {
				return lab;
		}
}
