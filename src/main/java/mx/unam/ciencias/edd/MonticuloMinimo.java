package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
	    if( indice < arbol.length && arbol[indice] != null)
		return true;
	    return false;
		    
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() throws NoSuchElementException{
            if(!hasNext())
		throw new NoSuchElementException();
	    return arbol[indice++];
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
	    indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
	    return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
	    this.indice=indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            int v = 0;
	    if (elemento.compareTo(adaptador.elemento) < 0) 
		v = -1;
	     else if (elemento.compareTo(adaptador.elemento) > 0)
		v = 1;
	    return v;
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
	arbol=nuevoArreglo(100);
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
       arbol = nuevoArreglo(n);
       elementos = n;
       int j = 0;
       for(T e : iterable){
	   arbol[j] = e;
	   arbol[j].setIndice(j);
	   j++;
       }
       for(int i = (elementos - 1)/2; i >= 0; i--)
	   baja(i);
    }

    private void sube(int i){
	int ind = (i - 1)/2;
	int m = i;
	if (ind >= 0 && arbol[ind].compareTo(arbol[i]) > 0)
	    m = ind;
	if (m != i){
	    T aux = arbol[i];
	    arbol[i] = arbol[ind];
	    arbol[i].setIndice(i);
	    arbol[ind] = aux;
	    arbol[ind].setIndice(ind);
	    sube(m);
	}
    }

    private void baja(int i){
	int izq = (2*i) + 1;
	int der = (2*i) + 2;
	if (izq >= getElementos() && der >= getElementos())
	   return;
	int m = menor(izq, der);
	m = menor(i, m);
	if (m != i) {
	    T aux = arbol[i];
	    arbol[i] = arbol[m];
	    arbol[i].setIndice(i);
	    arbol[m] = aux;
	    arbol[m].setIndice(m);
	    baja(m);
	}
    }

    
    private int menor(int a, int b){
	if (b >= elementos)
	    return a;
	else if (arbol[a].compareTo(arbol[b]) < 0)
	    return a;
	return b;
    }
    
    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        T[] tree;
	if (elementos >= arbol.length) {
            tree = nuevoArreglo(arbol.length * 2);
            for (int i = 0; i < arbol.length; i++)
                tree[i] = arbol[i];
            arbol = tree;
	}
	arbol[elementos] = elemento;
	arbol[elementos].setIndice(elementos);
	sube(elementos);
	elementos++;
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() throws IllegalStateException{
	if (elementos <= 0)
	    throw new IllegalStateException();
	T min = arbol[0];
	arbol[0] = arbol[--elementos];
	arbol[0].setIndice(0);
	arbol[elementos] = min;
	arbol[elementos].setIndice(-1);
	arbol[elementos] = null;
	baja(0);
	return min;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
	if(elemento==null)
	    return;
	int index = elemento.getIndice();
	if(index<0||index>=getElementos())
	    return;
	T ind = arbol[index];
	arbol[index] = arbol[elementos-1];
	arbol[index].setIndice(index);
	arbol[elementos-1] = ind;
	ind.setIndice(-1);
	arbol[elementos-1] = null;
	elementos--;
	reordena(arbol[index]);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
	for (T elem : arbol) {
	    if (elem == null)
		continue;
	    if (elem.equals(elemento))
		return true;
	}
	return false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return elementos <= 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
	elementos = 0;
        for (int i = 0; i < arbol.length; i++)
          arbol[i] = null;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if (elemento == null)
	    return;
	int index = elemento.getIndice();
	sube(index);
	baja(index);
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
	return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if (i < 0 || i >= getElementos())
	    throw new NoSuchElementException();
	return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String s = "";
	for (int i = 0; i < arbol.length; i++)
	    s += String.format("%s, ", arbol[i].toString());
	return s;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if (elementos != monticulo.getElementos())
	    return false;
	for (int i = 0; i < elementos; i++)
	    if (!arbol[i].equals(monticulo.arbol[i]))
		return false;            
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
	Lista<Adaptador<T>> lista1 = new Lista<>();
	Lista<T> lista2 = new Lista<>();
	for (T elemento : coleccion) 
	    lista1.agrega(new Adaptador<>(elemento));
	MonticuloMinimo<Adaptador<T>> monticulo = new MonticuloMinimo<>();
	for (Adaptador<T> adaptador : lista1)
	    monticulo.agrega(adaptador);
	
	while (!monticulo.esVacia()) {
	    Adaptador<T> ada = monticulo.elimina();
	    lista2.agrega(ada.elemento);
	}
	return lista2;
    }
}
