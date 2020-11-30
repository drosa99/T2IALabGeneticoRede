package src;

import java.util.ArrayList;

public class Cromossomo {

    private double[] pesos;
    private int score;
    private boolean chegou = false;
    //private ArrayList<Posicao> posicoes;
    private Agente agente;


    public Cromossomo(double[] pesos) {
        this.pesos = pesos;
        this.agente = new Agente(pesos);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public boolean isChegou() {
        return chegou;
    }

    public void setChegou(boolean chegou) {
        this.chegou = chegou;
    }

    public double[] getPesos() {
        return pesos;
    }

    public Agente getAgente() {
        return agente;
    }

    public void iniciaJogo(){
        agente.jogar();
        setChegou(agente.getAchouSaida());
        setScore(agente.getPontuacao());
    }

    //cromossomo vai ter labirinto, agente e rede




}
