package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeGrafica;

import java.util.NoSuchElementException;

public class RealizadorLaberintos {
    private class Celda{
        int x, y;
        byte valor;

        public Celda(int x, int y){
            this.x=x;
            this.y=y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public byte getValor() {
            return valor;
        }

        public void setValor(byte valor) {
            this.valor = valor;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") Celda c = (Celda) o;
            return c.x==x && c.y==y;
        }

        @Override
        public String toString() {
            return "Celda{" +
                    "x=" + x +
                    ", y=" + y +
                    ", valor=" + valor +
                    '}';
        }
    }

    public Grafica<Celda> getGrafica() {
        return grafica;
    }

    Grafica<Celda> grafica=new Grafica<>();
    Celda ini,fin;
    byte[][] arreglo;

    public RealizadorLaberintos(byte [][]arreglo){
        this.arreglo=arreglo;
        for(int i = 0; i < arreglo.length; i++)
            for(int j = 0; j < arreglo[0].length; j++){
                Celda c = new Celda(j,i);
                c.setValor((byte) (arreglo[i][j]&0xFF));
                grafica.agrega(c);
            }
    }

    public void conectar(){
        int i=0;
        for (Celda c : grafica){
            try{
                Celda ci=null;
                if(celdaSup(c.valor)){
                    ci= grafica.vertice(new Celda(c.x,c.y-1)).get();
                    int suma=ci.getValor()&0xFF+c.getValor()&0xFF;
                    grafica.conecta(c,ci,suma);
                }
                if(celdaIzq(c.valor)){
                    if(c.getX()==0) {
                        ini=c;
                    }
                    else{
                        ci= grafica.vertice(new Celda(c.x-1,c.y)).get();
                        int suma=ci.getValor()&0xFF+c.getValor()&0XFF;
                        grafica.conecta(c,ci,suma);
                    }
                }
                if(celdaDer(c.valor)){
                    if(c.getX()==arreglo[0].length-1) {
                        fin=c;
                    }
                    else{
                        ci= grafica.vertice(new Celda(c.x+1,c.y)).get();
                        int suma=ci.getValor()&0XFF+c.getValor()&0XFF;
                        if(i<10)System.out.println(c.getValor()&0xFF);
                        grafica.conecta(c,ci,suma);
                    }
                }
                if(celdaInf(c.valor)){
                    ci= (Celda) grafica.vertice(new Celda(c.x,c.y+1)).get();
                    int suma=ci.getValor()&0XFF+c.getValor()&0XFF;
                    if(i<10)System.out.println(c.getValor()&0xFF);
                    grafica.conecta(c,ci,suma);
                }
            }
            catch (NoSuchElementException w){}catch (IllegalArgumentException m){}
            i++;

        }
        System.out.println(grafica.esConexa());
        prueba();
    }

    private void prueba(){
        int i=0;
        for(Celda c : grafica){
            if(i>100 &i<110) {
                System.out.println(c);

                System.out.println(c.valor & 2);
                System.out.println(c.valor & 4);
                System.out.println(c.valor & 1);
                System.out.println(c.valor & 8);
                System.out.println("---------------------------");
                for (VerticeGrafica<Celda> ci : grafica.vertice(c).vecinos()) {
                    System.out.println(ci.get());
                }
                System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{");

            }
            i++;
        }
    }

    public String resolver(){
        System.out.println(ini+""+fin);
        Lista<VerticeGrafica<Celda>> camino=grafica.trayectoriaMinima(ini,fin);
        System.out.println(camino);
        return camino.toString();
    }
    private boolean celdaSup(byte b){
        return (b & 2) == 0;
    }
    private boolean celdaIzq(byte b){
        return (b & 4) == 0;
    }

    private boolean celdaDer(byte b){
        return (b & 1) == 0;
    }
    private boolean celdaInf(byte b){
        return (b & 8) == 0;
    }

}
