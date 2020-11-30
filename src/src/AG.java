package src;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AG {
		private int tamanhoPopulacao;
		private int tamCromossomo;
		private int chanceMutacao;
		private final Random r = new Random();

		public AG(int tamanhoPopulacao, int tamCromossomo, int chanceMutacao) {
				this.tamanhoPopulacao = tamanhoPopulacao;
				this.tamCromossomo = tamCromossomo;
				this.chanceMutacao = chanceMutacao;
		}

		public Cromossomo genetica(int qtdGeracoesPrinte) {
				System.out.println(LocalDateTime.now());
				List<Cromossomo> populacao = iniciaPopulacao();
				Cromossomo vencedor = null;
				int i = 0;

				//Ponto de parada: achar a saida
				while (true) {

						//aqui roda o jogo para todos os cromossomos da populacao
						populacao.parallelStream().forEach(this::aptidao);

						//filtra dentro da populacao se algum cromossomo encontrou a saida -> se nao encontrou -> vencedor = null
						vencedor = populacao.stream().filter(Cromossomo::isChegou).findFirst().orElse(null);
						if (vencedor != null) {
								System.out.println("VENCEDOR!!!! SCORE " + vencedor.getScore() + " GERACAO: " + i);
								System.out.println(LocalDateTime.now());

	//							populacao.forEach(it -> {
	//									System.out.println("\n" + it.getScore());
//										it.getPosicoes().forEach(o -> {
//												System.out.print(o.toString());
//										});
//								});
	//							System.out.println();


								return vencedor;
						}

						//printa o andamento do algoritmo a cada X geracoes que recebeu por parametro
						if (i % qtdGeracoesPrinte == 0) {
								List<Cromossomo> ordenadaPorAptidao = populacao.stream().parallel().sorted(Comparator.comparing(Cromossomo::getScore).reversed()).collect(Collectors.toList());
								System.out.println("Geracao: " + i);
								Cromossomo melhorCromossomo = ordenadaPorAptidao.get(0);
								System.out.println("Melhor cromossomo com score: " + melhorCromossomo.getScore()
										+ " Comandos para cima: " + melhorCromossomo.getAgente().qtdComandosCima
										+ " Comandos para esquerda: " + melhorCromossomo.getAgente().qtdComandosEsquerda
										+ " Comandos para baixo: " + melhorCromossomo.getAgente().qtdComandosBaixo
										+ " Comandos para direita: " + melhorCromossomo.getAgente().qtdComandosDireita
								);
								melhorCromossomo.getAgente().getCaminhoPercorrido().forEach(posicao -> System.out.print(posicao.toString() + ", "));
								System.out.println("\n --------------------------------------------------------------------------- \n");

						}

						populacao = crossOver(populacao);
						i++;
				}
		}

		//cria uma lista de Cromossomos, com pesos randomizado, tamanho da populacao e tamanho do vetor de movimentos parametrizado
		public List<Cromossomo> iniciaPopulacao() {
				List<Cromossomo> populacao = new ArrayList<>();
				for (int i = 0; i < tamanhoPopulacao; i++) {
						double[] pesos = r.doubles(tamCromossomo).toArray();
						populacao.add(new Cromossomo(pesos));
				}
				return populacao;
		}

		//inicio o jogo e busca o score de aptidao do cromossomo
		public int aptidao(Cromossomo cromossomo) {
				cromossomo.iniciaJogo();
				return cromossomo.getScore();
		}

		//faz a mutacao de um cromossomo, se cair na porcentagem de chance de mutacao, pega X genes e atribui pesos aleatorios
		public Cromossomo mutacao(double[] pesos) {
				if (r.nextInt(101) < chanceMutacao) {
						//aqui no i indica quantos pesos serão mutados
						for(int i =0; i < 4; i ++) {
								pesos[r.nextInt(pesos.length)] = r.nextDouble();
						}
				}
				return new Cromossomo(pesos);
		}

		//pega 2 cromossomos aleatorios da populacao e retorna o cromossomo com melhor score de aptidao
		public Cromossomo torneio(List<Cromossomo> populacao) {
				int l1 = r.nextInt(populacao.size());
				int l2 = r.nextInt(populacao.size());
				return populacao.get(l1).getScore() < populacao.get(l2).getScore() ? populacao.get(l1) : populacao.get(l2);
		}

		/*
		 * Metodo que recebe uma populacao e retorna uma nova populacao, fazendo crossover, torneio e mutacao
		 * Para cada novo cromossomo da nova populacao:
		 * 	Seleciona 2 cromossomos da antiga populacao por torneio
		 * 	Faz a media dos pesos do pai e da mae para cada indice do cromossomo
		 * 	Faz mutacao dos pesos deste novo cromossomo
		 * 	Adiciona o novo cromossomo na nova populacao
		 * */
		public List<Cromossomo> crossOver(List<Cromossomo> populacao) {
				List<Cromossomo> novaPopulacao = new ArrayList<>();

				//passa o melhor cromossomo direto para a proxima populacao
				Cromossomo melhorCromossomo = populacao.stream().sorted(Comparator.comparing(Cromossomo::getScore).reversed()).collect(Collectors.toList()).get(0);
				Cromossomo melhorCromoDuplicado = new Cromossomo(Arrays.copyOf(melhorCromossomo.getPesos(), 108));
				novaPopulacao.add(melhorCromoDuplicado);

				for (int i = 0; i < tamanhoPopulacao - 1; i++) {
						double[] pai;
						double[] mae;

						pai = Arrays.copyOf(torneio(populacao).getPesos(), 108);
						mae = Arrays.copyOf(torneio(populacao).getPesos(), 108);

						double[] novosPesos = new double[tamCromossomo];

						//faz a media dos pesos do pai e da mae
						for (int j = 0; j < tamCromossomo; j++) {
								novosPesos[j] = (pai[j] + mae[j])/2;
						}
						//faz mutacao no novo cromossomo
						Cromossomo novoCromossomo = mutacao(novosPesos);
						novaPopulacao.add(novoCromossomo);
				}
				return novaPopulacao;
		}
}
