PATIENTS LIST INTERFACE

/**
 * Created by ahmedayaz on 3/26/17.
 */
public interface ListInterface {
    boolean find(Patients element);

    int size();
    // Returns the number of elements on the list.

    void add(Patients element);
    // Adds an element to this list.

    //otherwise, return false.
    boolean remove(Patients element);
    // Removes an element e from this list such that e.equals(element)
    // and returns true; if no such element exists, returns false.
    Patients get(Patients element);
    // Returns an element e from this list such that e.equals(element);
    // if no such element exists, return null.
    String toString();
    //Returns a nicely formatted string that represents this list.
    void reset();
    // Initializes current position for en iteration through this list,
    // to the first element on this list
    Patients getNext(int i);
    //Precondition: The list is not empty. The list has been reset;
    //              The list has not been modified since most recent reset.
    // Returns the element at the current position on this list. If the current
    // position is the last element, then it advances the value of the current
    // position to the first element; othewise, it advances the value of the
    // current position to the next element


}
