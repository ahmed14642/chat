PATIENTS CLASS 

import java.sql.Time;
import java.time.MonthDay;
import java.time.Year;
import java.util.GregorianCalendar;
public class Patients {
    public String name;
    public String patientId;
    public String Address;
    public int Height;
    public double Weight;
    public GregorianCalendar DateOfBirth;
    public GregorianCalendar DateOfInitialVisit;
    public GregorianCalendar DateOfLastVisit;
    public GregorianCalendar currentTime;

    public Patients(){


    }

    public Patients(String name,String patientId,String Address,int Height,double Weight,GregorianCalendar DateOfBirth
            ,GregorianCalendar DateOfInitialVisit,GregorianCalendar DateOfLastVisit){
        this.name=name;
        this.patientId=patientId;
        this.Address=Address;
        this.Height=Height;
        this.Weight=Weight;
        this.DateOfBirth=DateOfBirth;
        this.DateOfInitialVisit=DateOfInitialVisit;
        this.DateOfLastVisit=DateOfLastVisit;

    }

        public String getName(){
            return name;
        }
        public void setName(String name){
            this.name=name;
        }
        public String getPatientId(){
            return patientId;
        }
        public void setPatientID(String PatientId){
            this.patientId=PatientId;
        }
        public String getAddress(){
            return Address;
        }
        public void setAddress(String Address){
            this.Address=Address;
        }
        public Integer getHeight(){
            return Height;
        }
        public void setHeight(Integer Height){
            this.Height=Height;
        }
        public Double getWeight(){
            return Weight;
        }
        public void setWeight(Double Weight){
            this.Weight=Weight;
        }
        public GregorianCalendar getDateOfBirth(){
            return DateOfBirth;
        }

        public void setDateOfBirth(int year, int month, int date){
            this.DateOfBirth= new GregorianCalendar(year,month,date);
        }
        public GregorianCalendar getDateOfInitialVisit(){
        return DateOfInitialVisit;
        }
        public void setDateOfInitialVisit(int year, int month, int date){
        this.DateOfInitialVisit= new GregorianCalendar(year,month,date);
        }
        public GregorianCalendar getDateOfLastVisit(){
        return DateOfLastVisit;
        }
        public void setDateOfLastVisit(int year, int month, int date){
        this.DateOfLastVisit= new GregorianCalendar(year,month,date);
        }
        //gets the current year subtracts from the date of birth year if month of current year is less than month of
        // birth year subtracts one from the age or
    //if month is same checks the data if current date is less than date of birth subtracts one
        public int get_Age(){
            int YearOfBirth =DateOfBirth.getWeekYear();
            currentTime=new GregorianCalendar();
            int age = currentTime.getWeekYear()-YearOfBirth;
            if(currentTime.getTime().getMonth()<DateOfBirth.getTime().getMonth()){
                age=age-1;
            }
            if(currentTime.getTime().getMonth()==DateOfBirth.getTime().getMonth()){
                if(currentTime.getTime().getDate()<DateOfBirth.getTime().getDate()){
              age=age-1;
                }
            }
            return age;
        }
        //same process as get_age

        public int get_Time_As_Patient(){
            currentTime=new GregorianCalendar();
            int currentYear= currentTime.getWeekYear();
            int YearOfInitialVisit = DateOfInitialVisit.getWeekYear();
            int TimeAsPatient= currentYear - YearOfInitialVisit;
            if(currentTime.getTime().getMonth()<DateOfInitialVisit.getTime().getMonth()){
                TimeAsPatient= TimeAsPatient-1;
            }
            if(currentTime.getTime().getMonth()==DateOfInitialVisit.getTime().getMonth()){
                if(currentTime.getTime().getDate()<DateOfInitialVisit.getTime().getDate()){
                   TimeAsPatient= TimeAsPatient-1;
                }
            }

            return TimeAsPatient;
        }
        //same process as get age
        public int get_Time_Since_Last_Visit() {
            currentTime = new GregorianCalendar();
            int currentYear = currentTime.getWeekYear();
            int yearOfLastVisit = DateOfLastVisit.getWeekYear();
            int TimeSinceVisit = currentYear - yearOfLastVisit;
            if(currentTime.getTime().getMonth()<DateOfLastVisit.getTime().getMonth()){
                TimeSinceVisit=TimeSinceVisit-1;
            }
            if(currentTime.getTime().getMonth()==DateOfLastVisit.getTime().getMonth()){
                if(currentTime.getTime().getDate()<DateOfLastVisit.getTime().getDate()){
                    TimeSinceVisit=TimeSinceVisit-1;
                }
            }
            return TimeSinceVisit;
        }
        public String toString(){

            return "Name: "+name+" "+"Patient ID: "+ patientId + " "+"Address: "+ Address+" "+"Height: "+Height+" "
                    +"Weight: " +Weight+" "+ "Date Of Birth: "+DateOfBirth.getTime()+" "+DateOfInitialVisit.getTime()+" "
                    +DateOfLastVisit.getTime()+" ";
        }
}