package Shell;
//----------------------------------------Cell----------------------------------------
//Class cell is designed to store actual data
//the Range of T is {Character,Integer,Float,Double,String}
//The T must be Comparable since we are going to sort some columns
public class Cell<T extends Comparable<T>> implements Comparable<Cell<T>>{
    private T value;
    public Cell (T value){
        this.value = value;
    }
    public int compareTo(Cell<T> cell){
        return this.value.compareTo(cell.getValue());
    }
    public T getValue() {

        return value;
    }
}
