package src;

/**
 * Escreva a descrição da classe Rede aqui.
 * 
 * @author Silvia 
 * @version 12/11/2020
 */
public class Rede
{
    private Neuronio[]camadaOculta;     //rede neural 8x8x4 -> topologia sugerida em aula
    private Neuronio[]camadaSaida;
    private double []saida;             //valores de saída da propagacao
    
    public Rede(int numNeuroniosOculta, int numNeuroniosSaida){
        if(numNeuroniosOculta <=0 || numNeuroniosSaida<=0){ 
            numNeuroniosOculta = 8;
            numNeuroniosSaida = 4;
        }
        camadaOculta = new Neuronio[numNeuroniosOculta];
        camadaSaida = new Neuronio[numNeuroniosSaida];
    }
    
    public void setPesosNaRede(int numEntradas, double []pesos){
        int k=0;
        double []tmp ;
        //Setando os pesos da camada oculta
        for(int i=0; i<camadaOculta.length; i++){
            tmp = new double[numEntradas+1];  //quantidade de pesos dos neurônios da camada oculta -> numero de pesos  + bias
            for(int j=0; j<numEntradas+1; j++){
                tmp[j] = pesos[k];
                k++;
            }
            camadaOculta[i]=new Neuronio(tmp);
        }
        //Setando os pesos da camada de saida
        for(int i=0; i<camadaSaida.length; i++){
            tmp = new double[camadaOculta.length+1];  //quantidade de pesos dos neurônios da camada oculta
            for(int j=0; j<camadaOculta.length+1; j++){
                tmp[j] = pesos[k];
                k++;
            }
            camadaSaida[i]=new Neuronio(tmp);
        }
    }

    //o que tem maior valor vai ser o escolhido
    public double[] propagacao(double []x){
        if(x==null) return null;
        
        double [] saidaOculta = new double[camadaOculta.length];
        saida = new double[camadaSaida.length];
        for(int i=0; i<camadaOculta.length; i++){
            saidaOculta[i]= camadaOculta[i].calculaY(x);
        }
        for(int i=0; i<camadaSaida.length; i++){
            saida[i]= camadaSaida[i].calculaY(saidaOculta);
        }
        return saida;
    }

    public Movimento propagacaoComMovimento(double []x){
        if(x==null) return null;

        double [] saidaOculta = new double[camadaOculta.length];
        saida = new double[camadaSaida.length];
        for(int i=0; i<camadaOculta.length; i++){
            saidaOculta[i]= camadaOculta[i].calculaY(x);
        }
        for(int i=0; i<camadaSaida.length; i++){
            saida[i]= camadaSaida[i].calculaY(saidaOculta);
        }
        return decodificaMovimento(saida);
    }

    public Movimento decodificaMovimento(double[] saidas){
        return Movimento.of(getIndexOfLargest(saidas));
    }

    public int getIndexOfLargest( double[] array ) {
        if ( array == null || array.length == 0 ) return -1; // null or empty
        int largest = 0;
        for ( int i = 1; i < array.length; i++ ) {
            if ( array[i] > array[largest] ) largest = i;
        }
        return largest; // position of the first largest found
    }

    
    public String toString(){
        String msg = "Pesos da rede\n";
        msg = msg + "Camada Oculta\n";
        for(int i=0;i<camadaOculta.length; i++){
            msg = msg + "Neuronio " + i + ": " + camadaOculta[i] + "\n";
        }
        msg = msg + "Camada Saida\n";
        for(int i=0;i<camadaSaida.length; i++){
            msg = msg + "Neuronio " + i + ": " + camadaSaida[i] + "\n";
        }
        return msg;
    }
}
