package mx.unam.ciencias.edd.proyecto3;
import mx.unam.ciencias.edd.*;

import java.util.NoSuchElementException;
import java.util.Random;
public class CreadorLaberintos {

    private class Celda implements Comparable{
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
                    ", valor=" + (valor&0xFF) +
                    '}';
        }

        @Override
        public int compareTo(Object o) {
            if(this.equals(o))
                return 0;
            @SuppressWarnings("unchecked") Celda c = (Celda) o;
            return c.valor-valor;
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
        for(int i=0; i<filas;i++)
            for(int j=0; j<columnas;j++){
                Celda celda = new Celda(j,i);
                grafica.agrega(celda);
                arbolGenerador.agrega(celda);
            }
        grafica=graficaConexa();
        //System.out.println(esCiclo(grafica));
        //System.out.println(esCiclo(arbolGenerador));
        kruskal();
        int m=r.nextInt(filas);
        int f=r.nextInt(filas);
        int i=0;
        for(Celda c: arbolGenerador){
            if(c.getX()==0 && c.getY()==m)
                c.setValor((byte)(c.valor^4));
            else if(c.getX()==columnas-1 && c.getY()==f)
                c.setValor((byte)(c.valor^1));
            laberinto[i]=c.valor;
            System.out.println(laberinto[i]&0xFF&15);
            i++;
        }
        return laberinto;
    }

    private Grafica<Celda> graficaConexa(){
        int i=0;
        for(Celda c : grafica){
            try{
                Random n= new Random();
                c.setValor((byte)((n.nextInt(15)<<4 )| 15));
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
    private boolean esCiclo(Grafica<Celda> celdas){
        Lista<Celda> visitados= new Lista<>();
        for(Celda v: celdas){
            boolean b=(tieneCiclo(celdas,v,null,visitados));
            if(!(visitados.contiene(v) )&& tieneCiclo(celdas ,v,null,visitados))
                return true;
        }
        return false;
    }

    private boolean tieneCiclo(Grafica<Celda>celdas,Celda c, Celda padre, Lista<Celda> visitados){
        visitados.agrega(c);
        for(VerticeGrafica vecino : celdas.vertice(c).vecinos()){
            Celda celda = (Celda) vecino.get();
            if(!visitados.contiene(celda))
                if(tieneCiclo(celdas,celda,c,visitados)){
                    return true;}
            else if(celda.equals(padre)){
                return false;}
            else{
                return true;}
        }
        return false;
    }
    private void kruskal(){
        for(Celda c : grafica){
            Lista<Celda> vecinos= new Lista<>();
            for(VerticeGrafica ci: grafica.vertice(c).vecinos()){
                Celda celda = (Celda) ci.get();
                vecinos.agrega(celda);
            }
            int m = r.nextInt(vecinos.getElementos());
            Celda ve= vecinos.get(m);
            if(!arbolGenerador.sonVecinos(c,ve))
                arbolGenerador.conecta(c,ve);
            while((esCiclo(arbolGenerador) || arbolGenerador.sonVecinos(c,ve) )&& vecinos.esVacia()){
                arbolGenerador.desconecta(c,ve);
                vecinos.elimina(ve);
                if(m>=vecinos.getElementos()&&vecinos.getElementos()!=0)
                    m--;
                if (vecinos.getElementos()==0)
                    break;
                ve=vecinos.get(m);
                if(!arbolGenerador.sonVecinos(c,ve))
                    arbolGenerador.conecta(c,ve);
            }
            byte va = (byte)(c.valor&15);//(byte)((c.valor<<4)|15);
            byte otro= (byte)(ve.valor&15);
            System.out.println(va);
            System.out.println(otro);
            byte ors= analiza(c,ve);
            byte l=0;
            va&=ors;
            va|=240;
            switch (ors){
                case 14:
                    l=11;
                    break;
                case 11:
                    l=14;
                    break;
                case 7:
                    l=13;
                    break;
                case 13:
                    l=7;
                    break;
            }
            otro&=l;
            otro|=240;
            System.out.println(c);
            System.out.println(ve);
            System.out.println((byte)(va&0xFF));
            System.out.println((byte)(otro&0XFF));
            c.setValor((byte)(va&c.valor));
            ve.setValor((byte)(otro&ve.valor));
            System.out.println(c);
            System.out.println(ve);

        }
        System.out.println(arbolGenerador);
    }

    private int suma(int valor, int valor2){
        int suma=valor+valor2;
        return (suma > 0)? suma : suma*-1;
    }

    private byte analiza(Celda c, Celda v){
        byte m=0;
        if(c.getX()==v.getX()-1)
            m=14;
        if(c.getX()==v.getX()+1)
            m=11;
        if(c.getY()==v.getY()-1)
            m=7;
        if(c.getY()==v.getY()+1)
            m=13;
        return m;
    }



}
