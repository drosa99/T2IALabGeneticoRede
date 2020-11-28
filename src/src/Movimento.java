package src;

import java.util.Arrays;

public enum Movimento {
		CIMA(0, "CIMA"), BAIXO(1, "BAIXO"), DIREITA(2, "DIREITA"), ESQUERDA(3, "ESQUERDA");

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
