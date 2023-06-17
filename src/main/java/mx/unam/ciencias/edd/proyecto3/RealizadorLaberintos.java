package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Diccionario;
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
            Celda ci=null;
            if(celdaSup(c.valor)){
                ci= grafica.vertice(new Celda(c.x,c.y-1)).get();
                int suma=(ci.getValor()&0xFF)+(c.getValor()&0xFF);
                try{
                    grafica.conecta(c,ci,suma);
                }catch (NoSuchElementException n){} catch (IllegalArgumentException il){}
            }
            if(celdaIzq(c.valor)){
               if(c.getX()==0) {
                   ini=c;
               }
               else{
                   ci= grafica.vertice(new Celda(c.x-1,c.y)).get();
                   int suma=(ci.getValor()&0xFF)+(c.getValor()&0XFF);
                   try{
                       grafica.conecta(c,ci,suma);
                   }catch (NoSuchElementException n){} catch (IllegalArgumentException il){}
               }
            }
            if(celdaDer(c.valor)){
                if(c.getX()==arreglo[0].length-1) {
                    fin=c;
                }
                else{
                    ci= grafica.vertice(new Celda(c.x+1,c.y)).get();
                    int suma=(ci.getValor()&0XFF)+(c.getValor()&0XFF);
                    try{
                        grafica.conecta(c,ci,suma);
                    }catch (NoSuchElementException n){
                    } catch (IllegalArgumentException il){
                    }
                }
            }
            if(celdaInf(c.valor)){
                ci= (Celda) grafica.vertice(new Celda(c.x,c.y+1)).get();
                int suma=(ci.getValor()&0XFF)+(c.getValor()&0XFF);
                try{
                    grafica.conecta(c,ci,suma);
                }catch (NoSuchElementException n){} catch (IllegalArgumentException il){}
            }
            i++;
        }
    }
    public Lista<Integer[]> resolver(){
        Lista<VerticeGrafica<Celda>> camino=grafica.trayectoriaMinima(ini,fin);
        Lista<Integer[]> r = new Lista<>();
        for(VerticeGrafica c: camino){
            Integer[] m = new Integer[2];
            Celda ci = (Celda) c.get();
            m[0]=ci.getX();
            m[1]=ci.getY();
            r.agrega(m);
        }
        return r;
    }
    private boolean celdaSup(byte b){
        b&=15;
        return (b & 2) == 0;
    }
    private boolean celdaIzq(byte b){
        b&=15;
        return (b & 4) == 0;
    }

    private boolean celdaDer(byte b){
        b&=15;
        return (b & 1) == 0;
    }
    private boolean celdaInf(byte b){
        b&=15;
        return (b & 8) == 0;
    }

}
