package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
            ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La distancia del vértice. */
        private double distancia;
        /* El índice del vértice. */
        private int indice;
        /* La lista de vecinos del vértice. */
        private Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento=elemento;
            color=Color.NINGUNO;
            vecinos=new Lista<Vecino>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice=indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if (distancia < vertice.distancia)
                return -1;
            else if (distancia > vertice.distancia)
                return 1;
            return 0;
        }
    }


    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino=vecino;
            this.peso=peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.color;
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica<T>.Vertice v, Grafica<T>.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices=new Lista<Vertice>();
        aristas=0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) throws IllegalArgumentException{
        Vertice nuevo=new Vertice(elemento);
        if(contiene(elemento) || elemento==null)
            throw new IllegalArgumentException();
        vertices.agrega(nuevo);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) throws NoSuchElementException, IllegalArgumentException{
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        Vertice aV = (Vertice)vertice(a);
        Vertice bV = (Vertice)vertice(b);
        if(a.equals(b) || sonVecinos(a,b))
            throw new IllegalArgumentException();
        aristas++;
        aV.vecinos.agrega(new Vecino(bV,1));
        bV.vecinos.agrega(new Vecino(aV,1));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        Vertice aV = (Vertice)vertice(a);
        Vertice bV = (Vertice)vertice(b);
        if(a.equals(b) || sonVecinos(a,b) || peso<=0)
            throw new IllegalArgumentException();
        aristas++;
        aV.vecinos.agrega(new Vecino(bV,peso));
        bV.vecinos.agrega(new Vecino(aV,peso));
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        Vertice aV = (Vertice)vertice(a);
        Vertice bV = (Vertice)vertice(b);
        if(a.equals(b) ||!sonVecinos(a,b))
            throw new IllegalArgumentException();
        Vecino aVecino= null;
        Vecino bVecino=null;
        for(Vecino v: aV.vecinos)
            if(v.vecino.equals(bV))
                bVecino=v;
        for(Vecino v: bV.vecinos)
            if(v.vecino.equals(aV))
                aVecino=v;


        aristas--;
        aV.vecinos.elimina(bVecino);
        bV.vecinos.elimina(aVecino);
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(Vertice v : vertices){
            if(elemento == null)
                return false;
            if(v.elemento.equals(elemento))
                return true;
        }
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) throws NoSuchElementException{
        if(!contiene(elemento))
            throw new NoSuchElementException();
        Vertice eliminado=(Vertice)vertice(elemento);
        for (Vertice vertex : vertices)
            for (Vecino n : vertex.vecinos)
                if (n.vecino.equals(eliminado)) {
                    vertex.vecinos.elimina(n);
                    aristas--;
                }
        vertices.elimina(eliminado);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) throws NoSuchElementException{
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        Vertice aV = (Vertice)vertice(a);
        Vertice bV = (Vertice)vertice(b);
        for(Vecino vecino : aV.vecinos)
            if(vecino.vecino.equals(bV))
                return true;
        return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) throws NoSuchElementException, IllegalArgumentException{
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        if(!sonVecinos(a,b))
            throw new IllegalArgumentException();
        Vertice aV = (Vertice)vertice(a);
        Vertice bV = (Vertice)vertice(b);
        for(Vecino vecino : aV.vecinos)
            if(vecino.vecino.equals(bV))
                return vecino.peso;
        return -1;
    }


    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) throws NoSuchElementException, IllegalArgumentException{
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        if(!sonVecinos(a,b)||peso<=0)
            throw new IllegalArgumentException();
        Vertice aV = (Vertice)vertice(a);
        Vertice bV = (Vertice)vertice(b);
        for(Vecino vecino : aV.vecinos)
            if(vecino.vecino.equals(bV))
                vecino.peso=peso;
        for(Vecino vecino : bV.vecinos)
            if(vecino.vecino.equals(aV))
                vecino.peso=peso;
        peso=-1;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) throws NoSuchElementException{
        if(!contiene(elemento))
            throw new NoSuchElementException();
        for(Vertice v : vertices){
            if(v.elemento.equals(elemento)){
                return v;
            }
        }
        return null;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) throws IllegalArgumentException{
        if(vertice.getClass()==Vertice.class){
            Vertice v= (Vertice)vertice;
            v.color=color;
        }
        else if(vertice.getClass()==Vecino.class){
            Vecino v= (Vecino)vertice;
            v.vecino.color=color;
        }
        else
            throw new IllegalArgumentException("Vertice Invalido");

    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        Cola<Vertice> c = new Cola<>();
        Vertice v = vertices.getPrimero();
        c.mete(v);
        while(!c.esVacia()){
            Vertice aux = c.saca();
            aux.color = Color.ROJO;
            for(Vecino v2 : aux.vecinos){
                if(v2.vecino.color != Color.ROJO){
                    v2.vecino.color = Color.ROJO;
                    c.mete(v2.vecino);
                }
            }
        }
        for(Vertice v2 : vertices){
            if(v2.color != Color.ROJO){
                return false;
            }
            v2.color = Color.NINGUNO;
        }
        return true;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice vertice : vertices)
            accion.actua(vertice);
    }

    private void recorre(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> metesaca) throws NoSuchElementException{
        if (!contiene(elemento)) {
            throw new NoSuchElementException();
        }
        Vertice vertice = (Vertice)vertice(elemento);
        vertice.color = Color.NEGRO;
        metesaca.mete(vertice);
        while (!metesaca.esVacia()) {
            Vertice v = metesaca.saca();
            v.color = Color.NEGRO;
            accion.actua(v);
            for (Vecino a : v.vecinos) {
                if (a.vecino.color != Color.NEGRO) {
                    a.vecino.color = Color.NEGRO;
                    metesaca.mete(a.vecino);
                }
            }
        }
        paraCadaVertice(ver -> setColor(ver, Color.NINGUNO));
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Cola<Vertice> cola = new Cola<>();
        recorre(elemento, accion, cola);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Pila<Vertice> pila = new Pila<>();
        recorre(elemento, accion, pila);
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas=0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        String s = "{";
        String r = "{";
        for(Vertice v : vertices){
            r+=v.elemento.toString()+", ";
            for(Vecino v2 : v.vecinos){
                if(!s.contains("(" + v2.vecino.elemento + ", " + v.elemento+")")){
                    s += "(" + v.elemento.toString() + ", " + v2.vecino.elemento.toString() + "), ";
                }
            }
        }
        s+="}";
        r+="}, "+s;
        return r;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        if(aristas != grafica.aristas || vertices.getLongitud() != grafica.vertices.getLongitud()){
            return false;
        }
        for(Vertice v : vertices){
            if(!grafica.contiene(v.elemento)){
                return false;
            }
            for(Vecino v2 : v.vecinos){
                if(!grafica.sonVecinos(v.elemento, v2.vecino.elemento)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) throws NoSuchElementException{
        if (!contiene(origen) || !contiene(destino))
            throw new NoSuchElementException();
        Lista<VerticeGrafica<T>> l = new Lista<>();
        Vertice verticeOrigen = (Vertice)vertice(origen);
        Vertice verticeDestino = (Vertice)vertice(destino);
        if (verticeOrigen.equals(verticeDestino)) {
            l.agrega(verticeDestino);
            return l;
        }

        for (Vertice v : vertices)
            v.distancia = Double.POSITIVE_INFINITY;

        verticeOrigen.distancia = 0;

        MonticuloMinimo<Vertice> mn = new MonticuloMinimo<>(vertices);
        while(!mn.esVacia()){
            Vertice u = mn.elimina();
            for (Vecino v : u.vecinos)
                if (v.vecino.distancia == Double.POSITIVE_INFINITY || u.distancia + v.peso < v.vecino.distancia) {
                    v.vecino.distancia = u.distancia + 1;
                    mn.reordena(v.vecino);
                }
        }

        if(verticeDestino.distancia != Double.POSITIVE_INFINITY){
            Vertice aux = verticeDestino;
            l=reconstruye(l,aux,verticeOrigen,false);
        }
        return l;
    }
    private Lista<VerticeGrafica<T>> reconstruye(Lista<VerticeGrafica<T>> l, Vertice aux, Vertice verticeOrigen,boolean m){
        double b=1;
        while (!aux.equals(verticeOrigen)) {
            for (Vecino vec : aux.vecinos){
                if(m)
                    b=vec.peso;
                if (aux.distancia - b == vec.vecino.distancia) {
                    l.agregaInicio(aux);
                    aux = vec.vecino;
                    break;
                }
            }
            if (aux.equals(verticeOrigen))
                l.agregaInicio(verticeOrigen);
        }
        return l;
    }



    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) throws NoSuchElementException{
        if (!contiene(origen) || !contiene(destino))
            throw new NoSuchElementException();

        Lista<VerticeGrafica<T>> l = new Lista<>();
        Vertice verticeOrigen = (Vertice)vertice(origen);
        Vertice verticeDestino = (Vertice)vertice(destino);
        if (verticeOrigen.equals(verticeDestino)) {
            l.agrega(verticeDestino);
            return l;
        }
        for (Vertice v : vertices)
            v.distancia = Double.POSITIVE_INFINITY;
        verticeOrigen.distancia = 0;

        MonticuloMinimo<Vertice> mn = new MonticuloMinimo<>(vertices);
        while(!mn.esVacia()){
            Vertice u = mn.elimina();
            for (Vecino v : u.vecinos)
                if (v.vecino.distancia == Double.POSITIVE_INFINITY || u.distancia + v.peso < v.vecino.distancia) {
                    v.vecino.distancia = u.distancia + v.peso;
                    mn.reordena(v.vecino);
                }
        }
        if(verticeDestino.distancia != Double.POSITIVE_INFINITY){
            Vertice aux = verticeDestino;
            l=reconstruye(l,aux,verticeOrigen,true);
        }
        return l;
    }
}