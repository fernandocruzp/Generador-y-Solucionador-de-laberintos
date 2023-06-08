package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
	    String str=elemento+" "+altura+"/"+balance();
	    return str;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return (altura == vertice.altura && super.equals(objeto));
        }

	private int balance(){
	    if(izquierdo!=null && derecho!=null)
		return izquierdo.altura() - derecho.altura();
	    if(izquierdo!=null)
		return izquierdo.altura() +1;
	    if(derecho!=null)
		return (-1)-derecho.altura();
	    return 0;
	}

	private int alturaSuper(){
	    return super.altura();
	}
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	super.agrega(elemento);
	balancea((VerticeAVL)ultimoAgregado.padre);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL eliminar = (VerticeAVL)(busca(elemento));
	if(eliminar == null)
	    return;
	elementos--;
	if(eliminar.izquierdo != null && eliminar.derecho != null)
	    eliminar=(VerticeAVL)intercambiaEliminable(eliminar);
	VerticeAVL padre = (VerticeAVL)eliminar.padre;
	eliminaVertice(eliminar);
	balancea(padre);
    }
    private int max(int a, int b){
	return a>b ? a : b;
    }
    private int alt(VerticeAVL v){
	if(v==null)
	    return -1;
	return v.altura();
    }
    private void balancea(VerticeAVL vertice){
	if(vertice==null)
	    return;
	vertice.altura=vertice.alturaSuper();
	int balance= vertice.balance();
	boolean cambio=false;
	VerticeAVL padreF=(VerticeAVL)vertice.padre;
	if(balance==-2){
	    VerticeAVL p = (VerticeAVL)vertice.izquierdo;
	    VerticeAVL q = (VerticeAVL)vertice.derecho;
	    VerticeAVL x = (VerticeAVL)q.izquierdo;
	    VerticeAVL y = (VerticeAVL)q.derecho;
	    int balanceQ=q.balance();
	    if(balanceQ==1){
		super.giraDerecha(q);
		q.altura=q.alturaSuper();
		x.altura=x.alturaSuper();
		q = (VerticeAVL)vertice.derecho;
		x = (VerticeAVL)q.izquierdo;
		y = (VerticeAVL)q.derecho;
		balanceQ=q.balance();
	    }
	    if(balanceQ==-1 || balanceQ==0 || balanceQ==-2){
		super.giraIzquierda(vertice);
		vertice.altura=vertice.alturaSuper();
		q.altura=q.alturaSuper();
		cambio=true;
	    }
	    if(cambio)
		padreF=(VerticeAVL)q.padre;
	    else
		padreF=(VerticeAVL)p.padre;
	}
	else if(balance==2){
	    VerticeAVL p = (VerticeAVL)vertice.izquierdo;
	    VerticeAVL q = (VerticeAVL)vertice.derecho;
	    VerticeAVL x = (VerticeAVL)p.izquierdo;
	    VerticeAVL y = (VerticeAVL)p.derecho;
	    int balanceP=p.balance();
	    if(balanceP==-1){
		super.giraIzquierda(p);
		p.altura=p.alturaSuper();
		y.altura=y.alturaSuper();
		p = (VerticeAVL)vertice.izquierdo;
		x = (VerticeAVL)p.izquierdo;
		y = (VerticeAVL)p.derecho;
		balanceP=p.balance();
	    }
	    if(balanceP==1 || balanceP==0 || balanceP==2){
		super.giraDerecha(vertice);
		vertice.altura=vertice.alturaSuper();
		p.altura=p.alturaSuper();
		cambio=true;
	    }
	    if(cambio)
		padreF=(VerticeAVL)p.padre;
	    else
		padreF=(VerticeAVL)vertice.padre;
	}
	balancea(padreF);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
