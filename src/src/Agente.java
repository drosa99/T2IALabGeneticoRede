package src;

import java.util.ArrayList;

//Grupo: Daniela Amaral e Vinicius Lima
public class Agente {
		private final int PENALIDADE_PAREDE = -5;
		private final int PENALIDADE_SAIU = -5;

		private final int PONTUACAO_MOEDA = 40;
		private final int PONTUACAO_ANDOU = 20;

		private ArrayList<Posicao> caminhoPercorrido = new ArrayList<>();
		private Posicao posicaoAtual;
		private int pontuacao = 0;
		private int moedasColetadas = 0;
		private boolean achouSaida = false;
		private Labirinto labirinto = new Labirinto();
		private boolean gameOver = false;
		private Rede rede = new Rede(8,4);


		int qtdComandosCima = 0;
		int qtdComandosBaixo = 0;
		int qtdComandosEsquerda = 0;
		int qtdComandosDireita = 0;

		public Agente(double[] pesos) {
				this.posicaoAtual = labirinto.getInicio();
				this.caminhoPercorrido.add(this.posicaoAtual);
				//coloca os pesos na rede
				rede.setPesosNaRede(8, pesos);
		}

		public void andarPraEsquerda() {
				qtdComandosEsquerda++;
				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX(), posicaoAtual.getPosY() - 1);
				movimentaAgente(novaPosicao);
		}

		public void andarPraDireita() {
				qtdComandosDireita++;
				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX(), posicaoAtual.getPosY() + 1);
				movimentaAgente(novaPosicao);
		}

		public void andarPraCima() {
				qtdComandosCima++;
				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX() - 1, posicaoAtual.getPosY());
				movimentaAgente(novaPosicao);
		}

		public void andarPraBaixo() {
				qtdComandosBaixo++;
				Posicao novaPosicao = new Posicao(posicaoAtual.getPosX() + 1, posicaoAtual.getPosY());
				movimentaAgente(novaPosicao);
		}


		//metodo que faz a movimentacao do agente para a posicao indicada, verifica as condicoes da terreno e contabiliza pontuacao
		//apos sair de uma posicao valida, coloca uma parede no lugar para evitar ciclos e repeticoes
		private void movimentaAgente(Posicao novaPosicao) {
				if (labirinto.isSaida(novaPosicao)) {
						this.posicaoAtual = novaPosicao;
						this.achouSaida = true;
				}

				if (labirinto.isSaida(posicaoAtual)) {
						this.caminhoPercorrido.add(this.posicaoAtual);
						return;
				}

				if (labirinto.isParede(novaPosicao)) {
						this.pontuacao += PENALIDADE_PAREDE;
						this.gameOver = true;
				}

				if (labirinto.saiuLabirinto(novaPosicao)) {
						this.pontuacao += PENALIDADE_SAIU;
						this.gameOver = true;
				}

				if (labirinto.isChaoValido(novaPosicao)) {
						this.pontuacao += PONTUACAO_ANDOU;
						labirinto.caminharEFecharCaminho(novaPosicao);
						this.posicaoAtual = novaPosicao;
						this.caminhoPercorrido.add(this.posicaoAtual);
				}

				if (labirinto.isMoeda(novaPosicao)) {
						labirinto.caminharEFecharCaminho(novaPosicao);
						this.pontuacao += PONTUACAO_MOEDA;
						this.moedasColetadas++;
						this.posicaoAtual = novaPosicao;
						this.caminhoPercorrido.add(this.posicaoAtual);
				}

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
				//se deu game over por bater na parede/sair do labirinto ou achou a saida -> para a execucao
				while (!isGameOver() && !this.achouSaida){
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

				//tira da pontuacao a distancia ate a saida
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

				if(colunaAgente+1 >= labirinto.getLab()[0].length) visao[ind] = 1;                        //direita
				else visao[ind] = labirinto.getLab()[linhaAgente][colunaAgente+1];    //conteúdo célula
				ind++;
				visao[ind] = distancia(linhaAgente,colunaAgente+1);  //distancia da saída
				ind++;

				for(int i = 0; i < visao.length ; i++){
						visao[i] = visao[i]/100;
				}
				return visao;
		}

		public int distancia(int linhaOrigem, int colunaOrigem){
				int linhaDestino = labirinto.getFim().getPosX();
				int colunaDestino = labirinto.getFim().getPosY();
				return (Math.abs(linhaOrigem - linhaDestino) + Math.abs(colunaOrigem-colunaDestino));

		}

		public int getMoedasColetadas() {
				return moedasColetadas;
		}
}
