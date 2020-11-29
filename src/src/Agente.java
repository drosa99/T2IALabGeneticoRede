package src;

import java.util.ArrayList;

public class Agente {
		private final int PENALIDADE_PAREDE = -2000;
		private final int PENALIDADE_SAIU = -5000;
		private final int PENALIDADE_REPETINDO = -20;

		private final int PONTUACAO_MOEDA = 30;

		private ArrayList<Posicao> caminhoPercorrido = new ArrayList<>();
		private Posicao posicaoAtual;
		private int pontuacao = 0;
		private int moedasColetadas = 0;
		private boolean achouSaida = false;
		private Labirinto labirinto = new Labirinto();
		private boolean gameOver = false;
		private Rede rede = new Rede(8,4);

		public Agente(double[] pesos) {
				this.posicaoAtual = labirinto.getInicio();
				rede.setPesosNaRede(8, pesos);
		}

//		public void andarPraCima() {
//				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX(), posicaoAtual.getPosY() - 1);
//				movimentaAgente(novaPosicao);
//		}
//
//		public void andarPraBaixo() {
//				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX(), posicaoAtual.getPosY() + 1);
//				movimentaAgente(novaPosicao);
//		}
//
//		public void andarPraEsquerda() {
//				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX() - 1, posicaoAtual.getPosY());
//				movimentaAgente(novaPosicao);
//		}
//
//		public void andarPraDireita() {
//				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX() + 1, posicaoAtual.getPosY());
//				movimentaAgente(novaPosicao);
//		}

		public void andarPraEsquerda() {
				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX(), posicaoAtual.getPosY() - 1);
				movimentaAgente(novaPosicao);
		}

		public void andarPraDireita() {
				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX(), posicaoAtual.getPosY() + 1);
				movimentaAgente(novaPosicao);
		}

		public void andarPraCima() {
				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX() - 1, posicaoAtual.getPosY());
				movimentaAgente(novaPosicao);
		}

		public void andarPraBaixo() {
				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX() + 1, posicaoAtual.getPosY());
				movimentaAgente(novaPosicao);
		}


		private void movimentaAgente(Posicao novaPosicao) {
				if (labirinto.isSaida(novaPosicao)) {
						this.posicaoAtual = novaPosicao;
						this.achouSaida = true;
				}

				if (labirinto.isSaida(posicaoAtual)) {
						this.caminhoPercorrido.add(this.posicaoAtual);
						return;
				}

				if (labirinto.getParedes().stream().anyMatch(it -> novaPosicao.equals(it)) || labirinto.isParede(novaPosicao)) {
						this.pontuacao += PENALIDADE_PAREDE;
						this.gameOver = true;
				}

				if (labirinto.saiuLabirinto(novaPosicao)) {
						this.pontuacao += PENALIDADE_SAIU;
						this.gameOver = true;
				}

				if (caminhoPercorrido.stream().anyMatch(it -> it.equals(novaPosicao))) {
						this.pontuacao += PENALIDADE_REPETINDO;
				}

				if (labirinto.isChaoValido(novaPosicao)) {
						this.posicaoAtual = novaPosicao;
				}

				if (labirinto.isMoeda(novaPosicao)) {
						labirinto.coletarMoeda(novaPosicao);
						this.pontuacao += PONTUACAO_MOEDA;
						this.moedasColetadas++;
						this.posicaoAtual = novaPosicao;
				}

				this.caminhoPercorrido.add(this.posicaoAtual);
		}

		public Posicao getPosicaoAtual() {
				return posicaoAtual;
		}

		public int getPontuacao() {
				return pontuacao;
		}

		public boolean getAchouSaida() {
				return achouSaida;
		}

		public boolean isGameOver() {
				return gameOver;
		}

		public ArrayList<Posicao> getCaminhoPercorrido() {
				return caminhoPercorrido;
		}


		public void jogar() {
				while (!isGameOver()){
						double[] entradas = entorno(posicaoAtual.getPosX(), posicaoAtual.getPosY());
						switch (rede.propagacaoComMovimento(entradas)){
								case CIMA:
										andarPraCima();
										break;
								case BAIXO:
										andarPraBaixo();
										break;
								case DIREITA:
										andarPraDireita();
										break;
								case ESQUERDA:
										andarPraEsquerda();
						}
				}
				pontuacao -= distancia(posicaoAtual.getPosX(), posicaoAtual.getPosY());
		}


		public double[] entorno(int linhaAgente, int colunaAgente){
				double [] visao = new double[8];
				int ind=0;
				//buscando percepção
				if(linhaAgente-1 <0) visao[ind] = 1;                        //em cima
				else visao[ind] = labirinto.getLab()[linhaAgente-1][colunaAgente];    //conteúdo célula
				ind++;
				visao[ind] = distancia(linhaAgente-1,colunaAgente);  //distancia da saída
				ind++;

				if(colunaAgente-1 <0) visao[ind] = 1;                        //esquerda
				else visao[ind] = labirinto.getLab()[linhaAgente][colunaAgente-1];    //conteúdo célula
				ind++;
				visao[ind] = distancia(linhaAgente,colunaAgente-1);  //distancia da saída
				ind++;

				if(linhaAgente+1 >= labirinto.getLab().length) visao[ind] = 1;                        //abaixo
				else visao[ind] = labirinto.getLab()[linhaAgente+1][colunaAgente];    //conteúdo célula
				ind++;
				visao[ind] = distancia(linhaAgente+1,colunaAgente);  //distancia da saída
				ind++;

				if(colunaAgente+1 >= labirinto.getLab()[0].length) visao[ind] = 1;                        //abaixo
				else visao[ind] = labirinto.getLab()[linhaAgente][colunaAgente+1];    //conteúdo célula
				ind++;
				visao[ind] = distancia(linhaAgente,colunaAgente+1);  //distancia da saída
				ind++;
				return visao;
		}

		public int distancia(int linhaOrigem, int colunaOrigem){
				int linhaDestino = labirinto.getFim().getPosX();
				int colunaDestino = labirinto.getFim().getPosY();
				return Math.abs(linhaOrigem - linhaDestino) + Math.abs(colunaOrigem-colunaDestino);
		}

		public int getMoedasColetadas() {
				return moedasColetadas;
		}
}
