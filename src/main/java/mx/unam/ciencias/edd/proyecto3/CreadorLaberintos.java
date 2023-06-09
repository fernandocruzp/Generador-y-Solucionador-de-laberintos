package mx.unam.ciencias.edd.proyecto3;
import mx.unam.ciencias.edd.*;

import java.util.NoSuchElementException;
import java.util.Random;
public class CreadorLaberintos {

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
    private int columnas, filas;
    Random r;

    private Grafica<Celda> grafica, arbolGenerador;
    public CreadorLaberintos(int c, int f){
        columnas=c;
        filas=f;
        r=new Random();
        grafica=new Grafica<Celda>();
        arbolGenerador=new Grafica<>();
    }

    public CreadorLaberintos(int c, int f, long semilla){
        columnas=c;
        filas=f;
        r=new Random(semilla);
        grafica=new Grafica<Celda>();
        arbolGenerador=new Grafica<>();
    }

    public byte[] generarLaberinto(){
        int cc=columnas*filas;
        byte [] laberinto=new byte[cc];
        for(int i=0; i<columnas;i++)
            for(int j=0; j<filas;j++){
                Celda celda = new Celda(i,j);
                grafica.agrega(celda);
                arbolGenerador.agrega(celda);
            }
        grafica=graficaConexa();
        arbolGenerador=krusttal();
        int m=r.nextInt(filas);
        int f=r.nextInt(filas);
        int i=0;
        for(Celda c: arbolGenerador){
            if(c.getX()==0 && c.getY()==m)
                c.setValor((byte)(c.valor^4));
            else if(c.getX()==columnas-1 && c.getY()==f)
                c.setValor((byte)(c.valor^1));
            laberinto[i]=c.valor;
            i++;
        }
        return laberinto;
    }

    private Grafica<Celda> graficaConexa(){
        int i=0;
        for(Celda c : grafica){
            try{
                //c.setValor((byte) (r.nextInt(14) +1));
                if(c.getX()-1>=0)
                    grafica.conecta(c,new Celda(c.getX()-1,c.getY()),1);
                if(c.getX()+1<columnas)
                    grafica.conecta(c,new Celda(c.getX()+1,c.getY()),1);
                if(c.getY()-1>=0)
                    grafica.conecta(c,new Celda(c.getX(),c.getY()-1),1);
                if (c.getY()+1<filas)
                    grafica.conecta(c,new Celda(c.getX(),c.getY()+1),1);
            }
            catch (NoSuchElementException e){
            }
            catch (IllegalArgumentException il){
            }
            i++;
        }
        //System.out.println(arbolGenerador);
        return grafica;
    }
    private boolean esCiclo(Celda c, Celda c2){
        if(arbolGenerador.sonVecinos(c,c2))
            return true;
        return false;
    }
    private Grafica<Celda> krusttal(){
        for(Celda c : grafica){
            boolean arriba=false;
            int m = r.nextInt(grafica.vertice(c).getGrado());
            if(m>=grafica.vertice(c).getGrado()) {
                arriba=true;
            }
            int i=0;
            Pila<Celda> vecinos=new Pila<>();
            for(VerticeGrafica ci: grafica.vertice(c).vecinos()){
                Celda celda = (Celda)ci.get();
                vecinos.mete(celda);
                if(i==m) {
                    if(esCiclo(celda,c)&&arriba) {
                        Celda celda1=vecinos.saca();
                        int suma= suma(celda1.valor,c.valor);
                        byte va = 15;//(byte)((c.valor<<4)|15);
                        va|=analiza(c,celda1);
                        c.setValor((byte)va);
                        arbolGenerador.conecta(c,celda1,suma);
                        break;
                    }
                    else if(esCiclo(celda,c))
                        continue;
                    else{
                        int suma=suma(celda.valor,c.valor)+1;
                        byte va = 15;//(byte)((c.valor<<4)|15);
                        va|=analiza(c,celda);
                        c.setValor((byte)va);
                        arbolGenerador.conecta(c, celda,suma);
                        break;
                    }
                }
                i++;
            }
        }
        System.out.println(arbolGenerador);
        return arbolGenerador;
    }

    private int suma(int valor, int valor2){
        int suma=valor+valor2;
        return (suma > 0)? suma : suma*-1;
    }

    private byte analiza(Celda c, Celda v){
        byte m=0;
        if(c.getX()==v.getX()-1)
            m=1;
        if(c.getX()==v.getX()+1)
            m=4;
        if(c.getX()==v.getY()-1)
            m=8;
        if(c.getX()==v.getY()+1)
            m=2;
        return m;
    }



}
