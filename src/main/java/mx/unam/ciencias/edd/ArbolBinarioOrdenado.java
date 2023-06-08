package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
	    pila = new Pila<Vertice>();
            if(raiz == null)
		return;
	    Vertice v = raiz;
	    while(v != null){
		pila.mete(v);
		v =  v.izquierdo;
            }
	}

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
	     Vertice v = pila.saca();
	     if(v.derecho != null){
		 pila.mete(v.derecho);
		 Vertice a =  v.derecho;
		 while(a.izquierdo != null){
		     pila.mete(a.izquierdo);
		     a =  a.izquierdo;
		 }
	     }
	     return v.elemento;
	}
    }


    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    private void agrega(Vertice nuevo, Vertice actual) {
	if(nuevo.elemento.compareTo(actual.elemento) <= 0){
	    if(actual.izquierdo == null){
		actual.izquierdo = nuevo;
		nuevo.padre = actual;
	    } else
		agrega(nuevo, actual.izquierdo);
	    
	} else{
	    if(actual.derecho == null){
		actual.derecho = nuevo;
		nuevo.padre = actual;
	    }else
		agrega(nuevo, actual.derecho);
	    
	}
    }
    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) throws IllegalArgumentException{
	if(elemento==null) throw new IllegalArgumentException();
	Vertice v = nuevoVertice(elemento);
	ultimoAgregado = v;
	elementos++;
	if(raiz == null){
	    raiz = v;
	    return;
	}
	agrega(v,raiz);   
    }
    
    private Vertice max(Vertice vertice){
	if(vertice.derecho == null)
	    return vertice;
	return max(vertice.derecho);
     }
    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice eliminar = vertice(busca(elemento));
	if(eliminar == null)
	    return;
	elementos--;
	if(eliminar.izquierdo != null && eliminar.derecho != null)
	    eliminaVertice(intercambiaEliminable(eliminar)); 
	 else
	     eliminaVertice(eliminar);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice max = max(vertice.izquierdo);
	T elemento = max.elemento;
	max.elemento = vertice.elemento;
	vertice.elemento = elemento;
	return max;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        if(vertice.izquierdo == null && vertice.derecho == null){
	    Vertice padre = vertice.padre;
	    if(padre == null){
		raiz = null;
		return;
	    }
	    if(padre.izquierdo == vertice)
		padre.izquierdo = null;
	    else 
		padre.derecho = null;
	} else if(vertice.izquierdo == null){
	    if(vertice.padre == null){
		raiz = vertice.derecho;
		raiz.padre = null;
	    } else
		eliminar(vertice.padre, vertice.derecho, vertice);
	} else{
	    if(vertice.padre == null){
		raiz = vertice.izquierdo;
		raiz.padre = null;
	    } else
		eliminar(vertice.padre, vertice.izquierdo, vertice);
	}
    }
    private void eliminar(Vertice padre, Vertice hijo, Vertice eliminar){
	if(eliminar == padre.izquierdo){
	    padre.izquierdo = hijo;
	    hijo.padre = padre;
	} else{
	    padre.derecho = hijo;
	    hijo.padre = padre;
	}
    }
    
    private VerticeArbolBinario<T> busca(Vertice v,T elemento) {
	if(v == null)
	    return null;
	if(v.elemento.equals(elemento))
	    return v;
	else if(v.elemento.compareTo(elemento) > 0)
	    return busca(v.izquierdo, elemento);
	else
	    return busca(v.derecho, elemento);
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(raiz,elemento);
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
	Vertice girar = (Vertice) vertice;
	if(girar.izquierdo == null)
	    return;
	Vertice izquierdo = girar.izquierdo;
	Vertice padre = girar.padre;
	girar.padre = izquierdo;
	if(izquierdo.derecho != null){
	    girar.izquierdo = izquierdo.derecho;
	    izquierdo.derecho.padre = girar;
	} else
	    girar.izquierdo = null;
	izquierdo.derecho = girar;
	izquierdo.padre = padre;
	if(padre == null)
	    raiz = izquierdo;
	else if (padre.derecho == girar)
	    padre.derecho = izquierdo;
	else
	    padre.izquierdo = izquierdo;
	
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        Vertice girar = (Vertice)vertice;
	if(girar.derecho == null) return;
	Vertice derecho = girar.derecho;
	Vertice padre = girar.padre;  
	girar.padre = derecho;
	if(derecho.izquierdo != null){
	    girar.derecho = derecho.izquierdo;
	    derecho.izquierdo.padre = girar;
	}else{
	    girar.derecho = null;
	}
        derecho.izquierdo = girar;
        derecho.padre = padre;
	if(padre == null)
	    raiz = derecho;
	else if (padre.derecho == girar)
	    padre.derecho = derecho;
	else 
	    padre.izquierdo = derecho;
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
	dfspreOrder(accion, raiz);
    }
    private void dfspreOrder(AccionVerticeArbolBinario<T> accion, Vertice vertice){
      if(vertice == null)
	  return;
      accion.actua(vertice);
      dfspreOrder(accion, vertice.izquierdo);
      dfspreOrder(accion, vertice.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
	dfsInOrder(accion, raiz);
    }
    private void dfsInOrder(AccionVerticeArbolBinario<T> accion, Vertice vertice) {
        if(vertice == null)
	  return;
	dfsInOrder(accion, vertice.izquierdo);
	accion.actua(vertice);
	dfsInOrder(accion, vertice.derecho);	
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(accion,raiz);
    }
    private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice vertice) {
	if(vertice == null)
	  return;
	dfsPostOrder(accion, vertice.izquierdo);
	dfsPostOrder(accion, vertice.derecho);
	accion.actua(vertice);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
