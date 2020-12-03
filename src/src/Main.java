package src;

//Grupo: Daniela Amaral e Vinicius Lima
public class Main {

    public static void main(String[] args) {
        AG ag = new AG(300, 108, 80);
        Cromossomo vencedor = ag.genetica(5000);
        System.out.println("Moedas coletadas: " + vencedor.getAgente().getMoedasColetadas());

        System.out.println("Caminho percorrido: ");
        vencedor.getAgente().getCaminhoPercorrido().forEach(it -> System.out.print(it.toString() + ", "));
    }
}
