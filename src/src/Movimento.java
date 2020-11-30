package src;

import java.util.Arrays;

public enum Movimento {
		CIMA(2, "CIMA"), BAIXO(0, "BAIXO"), DIREITA(1, "DIREITA"), ESQUERDA(3, "ESQUERDA");

		int valor;
		String movimento;

		Movimento(int valor, String movimento) {
				this.valor = valor;
				this.movimento = movimento;
		}

		public static Movimento of (int valor){
				return Arrays.stream(Movimento.values()).filter(it -> it.valor == valor).findFirst().get();
		}
}
