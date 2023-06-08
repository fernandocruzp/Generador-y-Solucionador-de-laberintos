package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase para montículos de Dijkstra con arreglos.
 */
public class MonticuloArreglo<T extends ComparableIndexable<T>>
    implements MonticuloDijkstra<T> {

    /* Número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arreglo;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor para montículo de Dijkstra con un arreglo a partir de una
     * colección.
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloArreglo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
	arreglo = nuevoArreglo(coleccion.getElementos());
	elementos = coleccion.getElementos();
	int j = 0;
	for(T e : coleccion){
	    arreglo[j] = e;
	    arreglo[j].setIndice(j);
	    j++;
	}
    }

    /**
     * Construye un nuevo para montículo de Dijkstra con arreglo a partir de un
     * iterable.
     * @param iterable el iterable a partir de la cual construir el montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloArreglo(Iterable<T> iterable, int n) {
        arreglo = nuevoArreglo(n);
	elementos = n;
	int j = 0;
	for(T e : iterable){
	    arreglo[j] = e;
	    arreglo[j].setIndice(j);
	    j++;
	}
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
       if (esVacia()) {
        throw new IllegalStateException();
      }
      T minimo = null;
      int j = 0;
      for (T e : arreglo) {
        if (e != null) {
          minimo = e;
          break;
        }
        j++;
      }
      for (int i = j; i < arreglo.length; i++) {
        if (arreglo[i] != null) {
          if (arreglo[i].compareTo(minimo) < 0) {
            minimo = arreglo[i];
            j = i;
          }
        }
      }
      arreglo[j].setIndice(-1);
      arreglo[j] = null;
      elementos--;
      return minimo;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del arreglo.
     * @param i el índice del elemento que queremos.
     * @return el <i>i</i>-ésimo elemento del arreglo.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
       if (i < 0 || i >= getElementos()){
        throw new NoSuchElementException();
      }
      return arreglo[i];
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        if (elementos == 0) 
	    return true;
	return false;
    }

    /**
     * Regresa el número de elementos en el montículo.
     * @return el número de elementos en el montículo.
     */
    @Override public int getElementos() {
        return elementos;
    }
}
