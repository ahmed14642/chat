PATIENTS LIST CLASS

/**
 * Created by ahmedayaz on 3/26/17.
 */
public class PatientsList implements ListInterface {

    LLPatientNode Head;
    LLPatientNode Tail;
    LLPatientNode Location;
    LLPatientNode previous;
    boolean moreValues;
    public int size;
    public boolean found;


    @Override
    public boolean find(Patients target) {
        //when location pointer returns null it sets moreValues to false ending the while loop
        Location = Head;
        found = false;
        moreValues=true;
        while (Head != null && !found && moreValues) {

            if (Location.getPatient().equals(target)) {
                found = true;
            } else {
                previous = Location;
                Location = Location.getLlPatientNode();
                if (Location == null) {
                    moreValues = false;
                }
                found = false;
            }
        }
        return found;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(Patients element) {
        //checks if head is null if yes sets the element to head
        //if not checks if tail is null if yes points the head to the element and sets the tail to the element
        //if not sets the tail to point to the element and sets the tail to the element
        LLPatientNode llPatientNode = new LLPatientNode(element);

        if (Head == null) {
            Head = llPatientNode;
            Location=Head;
        } else if (Tail == null) {
            Head.setLink(llPatientNode);
            Tail = llPatientNode;
            Location=Tail;
        } else {
            Tail.setLink(llPatientNode);
            Tail = llPatientNode;
            Location=Tail;
        }

        size++;
    }


    @Override
    public boolean remove(Patients element) {
        boolean removeSuccessful = false;
        //finds the element if yes checks if element equals head in that case sets the head to the node after that
        //else it gets the previous that was set from find method and points to the link of the location
        if (find(element)) {
            if (element.equals(Head.getPatient())) {
                Head = Head.getLlPatientNode();
                removeSuccessful = true;
                size--;
            } else {
                previous.setLink(Location.getLlPatientNode());
                removeSuccessful = true;
                size--;
            }

        }
        return removeSuccessful;
    }

    @Override
    public Patients get(Patients element) {
        if (find(element)) {
            return Location.getPatient();
        } else {


            return null;
        }
    }

    @Override
    public void reset() {
        Head = null;
        Tail = null;
        Location = null;
        previous = null;
        size = 0;

    }

    @Override
    public Patients getNext(int i) {
        //in order to make sure it gets the first element from the list when executed first we can enter zero to set the
        //location to zero and return the first element
       if(i==0){
        Location=Head;
        return Head.getPatient();}
        else{
        if (Location != null) {
            if (Location.getPatient().equals(Tail.getPatient())) {
                Location=Head;
                return Head.getPatient();

            } else {
                Patients patients=Location.getLlPatientNode().getPatient();
                Location=Location.getLlPatientNode();
                return patients;

            }

        } else {
            return null;
        }
       }
    }

    @Override
    public String toString() {
        Location = Head;
        String n = null;
        moreValues=true;
        while (Head != null && moreValues) {
            n = Location.getPatient().toString() + n;
            Location = Location.getLlPatientNode();
            if (Location == null) {
            moreValues=false;
            }
        }

        return n;

    }
}