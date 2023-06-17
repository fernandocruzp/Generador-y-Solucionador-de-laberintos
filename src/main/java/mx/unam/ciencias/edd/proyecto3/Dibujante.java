package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;

public class Dibujante{
    byte[][] arreglo;
    public Dibujante(byte[][] arreglo){
        this.arreglo = arreglo;
    }
    public String dibujaLaberintoSolucion(){
        String cuadricula = "";
        for(int i = 0; i < arreglo.length; i++)
            for(int j = 0; j < arreglo[0].length; j++)
                cuadricula += dibujarRecuadro(arreglo[j][i], i+1, j+1);
        RealizadorLaberintos r = new RealizadorLaberintos(arreglo);
        r.conectar();
        Lista<Integer[]> sol= r.resolver();
        String solucion="";
        solucion = dibujarSolucion(sol);
        return abrirSVG(arreglo[0].length*60, arreglo.length*60) + cuadricula + solucion+ cerrarSVG();
    }
    private String abrirSVG(int tamanoX, int tamanoY){
        return "<?xml version='1.0' encoding='UTF-8' ?>" + '\n' +"<svg width='" + (tamanoX+100)+ "' height='" + (tamanoY+100) + "' >" + '\n' + "<g>";
    }
    private String cerrarSVG(){
        return "</g>" +'\n' + "</svg>" + '\n';
    }
    private String dibujarRecuadro(int valor, int posicionX, int posicionY){
        String res = "";
        if(puertaArriba(valor))
            res += dibujarArriba(posicionX, posicionY);
        if(puertaIzquierda(valor))
            res += dibujarIzquierda(posicionX, posicionY);
        if(puertaDerecha(valor))
            res += dibujarDerecha(posicionX, posicionY);
        if(puertaAbajo(valor))
            res += dibujarAbajo(posicionX, posicionY);
        return res;
    }

    private String dibujarSolucion(Lista<Integer[]> solucion){
        int i = 0;
        String r="";
        for(Integer[] m: solucion){
            if(i<solucion.getElementos()-1)
                r+=dibujaLinea(m,solucion.get(i+1));
            i++;
        }
        return r;
    }

    private String dibujaLinea(Integer[] m, Integer[] n){
        return "<line x1='" + ((m[0]+1.5)*60)+ "' y1='" + ((m[1]+1.5)*60) + "' x2='" + ((n[0]+1.5)*60) + "' y2='" + ((n[1]+1.5)*60) + "' stroke='red' stroke-width='6' /> " + '\n';
    }
    private String dibujarArriba(int posicionX, int posicionY){
        return "<line x1='" + (posicionX*60)+ "' y1='" + (posicionY*60) + "' x2='" + ((posicionX+1)*60) + "' y2='" + (posicionY*60) + "' stroke='black' stroke-width='6' /> " + '\n';
    }
    private String dibujarAbajo(int posicionX, int posicionY){
        return "<line x1='" + (posicionX*60)+ "' y1='" + ((posicionY+1)*60) + "' x2='" + ((posicionX+1)*60) + "' y2='" + ((posicionY+1)*60) + "' stroke='black' stroke-width='6' /> " + '\n';
    }
    private String dibujarDerecha(int posicionX, int posicionY){
        return "<line x1='" + ((posicionX+1)*60)+ "' y1='" + (posicionY*60) + "' x2='" + ((posicionX+1)*60) + "' y2='" + ((posicionY+1)*60) + "' stroke='black' stroke-width='6' /> " + '\n';
    }
    private String dibujarIzquierda(int posicionX, int posicionY){
        return "<line x1='" + (posicionX*60)+ "' y1='" + (posicionY*60) + "' x2='" + (posicionX*60) + "' y2='" + ((posicionY+1)*60) + "' stroke='black' stroke-width='6' /> " + '\n';
    }
    private boolean puertaArriba(int valor){
        return (valor & 2) != 0;
    }
    private boolean puertaAbajo(int valor){
        return (valor & 8) != 0;
    }
    private boolean puertaDerecha(int valor){
        return (valor & 1) != 0;
    }
    private boolean puertaIzquierda(int valor){
        return (valor & 4) != 0;
    }

}
