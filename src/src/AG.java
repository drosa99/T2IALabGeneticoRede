package src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
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
				boolean achouSaida = false;
				List<Cromossomo> populacao = iniciaPopulacao();
				Cromossomo vencedor = null;
				int i = 0;

				//Ponto de parada: solucao otima -> achar a saida; apos um certo numero de geracoes -> recomeca
				//todo mudar aqui para a condicao de parada
				while (!achouSaida) {

						//calcula a aptidao de todos os cromossomos da populacao
						populacao.forEach(this::aptidao);

						//ordenada os cromossomos da populacao por aptida em order ascendente -> melhor primeiro
						//List<Cromossomo> ordenadaPorAptidao = populacao.stream().sorted(Comparator.comparing(Cromossomo::getScore).reversed()).collect(Collectors.toList());

						//filtra dentro da populacao se algum cromossomo encontrou a saida -> se nao encontrou -> vencedor = null
						//vencedor = populacao.stream().filter(Cromossomo::isChegou).findFirst().orElse(null);
						if (vencedor != null) {
								achouSaida = true;
								System.out.println("VENCEDOR!!!! SCORE " + vencedor.getScore() + " GERACAO: " + i);
								System.out.println(LocalDateTime.now());

								populacao.forEach(it -> {
										System.out.println("\n" + it.getScore());
//										it.getPosicoes().forEach(o -> {
//												System.out.print(o.toString());
//										});
								});
								System.out.println();


								return vencedor;
						}

						//printa o andamento do algoritmo a cada X geracoes que recebeu por parametro
						if (i % qtdGeracoesPrinte == 0) {
								List<Cromossomo> ordenadaPorAptidao = populacao.stream().sorted(Comparator.comparing(Cromossomo::getScore).reversed()).collect(Collectors.toList());
								System.out.println("Geracao: " + i);
								System.out.println("Melhor cromossomo com score: " + ordenadaPorAptidao.get(0).getScore());
								ordenadaPorAptidao.get(0).getAgente().getCaminhoPercorrido().forEach(posicao -> System.out.print(posicao.toString() + ", "));
								System.out.println("--------------------------------------------------------------------------- \n");
						}

						populacao = crossOver(populacao);
						i++;
				}
				return vencedor;

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
						for(int i =0; i < 20; i ++) {
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

				for (int i = 0; i < tamanhoPopulacao; i++) {
						double[] pai;
						double[] mae;

						pai = torneio(populacao).getPesos();
						mae = torneio(populacao).getPesos();

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
