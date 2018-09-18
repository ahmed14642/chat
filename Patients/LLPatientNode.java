PATIENTS LINKED NODE 
/**
 * Created by ahmedayaz on 3/26/17.
 */
public class LLPatientNode {

    public Patients patients;
    public LLPatientNode llPatientNode;

    public LLPatientNode(Patients patients){
        this.patients=patients;
        llPatientNode=null;
    }
    public void setPatients(Patients patients)
    {
        this.patients = patients;

    }

    public Patients getPatient()
    {
        return patients;

    }

    public void setLink(LLPatientNode llPatientNode)
    {
        this.llPatientNode = llPatientNode;

    }

    public LLPatientNode getLlPatientNode()
    {
        return llPatientNode;

    }
}