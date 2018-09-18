import java.util.GregorianCalendar;
import java.util.Scanner;
import java.io.*;


public class Main {

    public static void main(String[] args) throws IOException {
        //file reader reads from a file entered by user
        FileReader fileReader=null;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the file name, if no file enter any key and proceed");
        String filename=in.next();
        //tries to read data from a file if file doesnt exist it asks the user if it wants to proceed with or without file
        try{
        fileReader=  new FileReader(filename+".txt");
            Scanner scanner = new Scanner(fileReader);
            PatientsList patientsList = new PatientsList();
            GetPatientsFromFile(scanner,patientsList,args);
            DisplayOptions(patientsList,in);
        }
        catch (Exception e){
            System.out.println("file does not exists or user wants to proceed without file. If proceed with loading data from file press y for yes or any key "
                    + "for no");
            String input= in.next();
            if(input.equals("y")){
            main(args);}else{
                //displays the options if file doesnt exist
                PatientsList patientsList = new PatientsList();
                DisplayOptions(patientsList,in);
            }
        }





    }
    //reads the patients data from the file and stores in the lists after making objects
    public static void GetPatientsFromFile(Scanner fl,PatientsList patientsList,String[]args){
        boolean moreVariables=true;
        try{
        while(fl.hasNextLine()) {
            String name = fl.nextLine();
            // if theres a blank line after first 14 lines it skips that line
            if(name.isEmpty()){name=fl.nextLine();}
            String patientId = fl.nextLine();
            //checks if id already exists in file, if it does it resets the list and executes the main again
            for(int i=0;i<patientsList.size();i++){
                Patients patients=patientsList.getNext(i);
                if(patientId.equals(patients.getPatientId())){
                    System.out.println("one or more patients have same ids, cannot load file");
                    patientsList.reset();
                    main(args);
                }
            }
            String Address = fl.nextLine();
            int Height = fl.nextInt();
            double Weight = fl.nextDouble();
            int YearOfBirth = fl.nextInt();
            int MonthOfBirth = fl.nextInt();
            int DayOfBirth = fl.nextInt();
            int YearOfInitialVisit = fl.nextInt();
            int MonthOfInitialVisit = fl.nextInt();
            int DayOfInitialVisit = fl.nextInt();
            int YearOfLastVisit = fl.nextInt();
            int MonthOfLastVisit = fl.nextInt();
            int DayOfLastVisit = fl.nextInt();
            Patients patients = new Patients(name, patientId, Address, Height, Weight, new GregorianCalendar(YearOfBirth
                    , MonthOfBirth-1, DayOfBirth),
                    new GregorianCalendar(YearOfInitialVisit, MonthOfInitialVisit-1, DayOfInitialVisit),
                    new GregorianCalendar(YearOfLastVisit, MonthOfLastVisit-1, DayOfLastVisit));
            patientsList.add(patients);

        }}catch (Exception e){
//when fl has empty lines it does nothing
        }


    }
    //displays the 8 options after storing patients data from file also executes input handling of the user for the option
    public static void DisplayOptions(PatientsList patientsList, Scanner in){

        System.out.println("1. Display List"+"\n"+"2. Add a new patient"+"\n"+"3. Show information for a patient"+
                "\n"+"4. Delete a pateint"+"\n"+"5. Show average patient age"+"\n"
                +"6. Show information for the youngest patient"+"\n"+"7. Show notification list"+"\n"+"8. Quit");
        input_handling(patientsList,in);

    }
    //takes the user input and selects the options accordingly, after executing the option displays the options again if
    //pressed quit programs ends
    public static void input_handling(PatientsList patientsList,Scanner in){
        int n = in.nextInt();
        if(n>8||n<1){
            System.out.println("Invalid input");
            DisplayOptions(patientsList,in);
        }
        if(n==1){
        DisplayList(patientsList);
        DisplayOptions(patientsList,in);
        }
        if(n==2){
        AddPatient(patientsList,in);
        DisplayOptions(patientsList,in);
        }
        if(n==3){
            DisplayPatientWith_ID(patientsList,in);
            DisplayOptions(patientsList,in);
        }
        if(n==4){
        DeletePatient(patientsList,in);
        DisplayOptions(patientsList,in);
        }
        if(n==5){
        ShowAverage(patientsList);
        DisplayOptions(patientsList,in);
        }
        if(n==6){
        YoungestPatient(patientsList);
        DisplayOptions(patientsList,in);
        }
        if(n==7){
        PatientsWhoAreOverdue(patientsList);
        DisplayOptions(patientsList,in);
        }
        if(n==8){
            //if pressed quit does not load the options again
        Quit(patientsList,in);
        }


    }
    //OPTION 1
    public static void DisplayList(PatientsList patientsList){
     int numberOfElements=patientsList.size();
     //loops through the list and collects the patients name and ids using list getNext(); method
     for(int i=0;i<numberOfElements;i++) {
         Patients patients= patientsList.getNext(i);
         String name = patients.getName();
         String patientid= patients.getPatientId();
         System.out.println("name: "+name+", "+"patient id: "+patientid);
     }
        }
    //OPTION 2
    public static void AddPatient(PatientsList patientsList,Scanner in){
        //takes user input checks if id is already in data it executes the Add patient method again asking the user to
        //enter again
         String name;
         String patientId;
         String  Address;
         int Height;
         double Weight;
         int YearOfBirth;
         int MonthOfbirth;
         int DayOfBirth;
         int YearOfInitialVisit;
         int MonthOfInitialVisit;
         int DayOfInitialVisit;
        int YearOfLastVisit;
        int MonthOfLastvisit;
        int DayOfLastvisit;

         GregorianCalendar DateOfBirth;
         GregorianCalendar DateOfInitialVisit;
         GregorianCalendar DateOfLastVisit;
         GregorianCalendar currentTime;
         System.out.println("Enter ID of Patient");
         patientId=in.next();
         //checks if id exists already
         for(int i =0;i<patientsList.size();i++){
             Patients patients= patientsList.getNext(i);
             if(patients.getPatientId().equals(patientId)){
                 System.out.println("Id already exists, please enter again");
                 AddPatient(patientsList,in);
             }
         }
         System.out.println("Enter Name of Patient");
         name = in.next();
         System.out.println("Enter Address of Patient");
         Address=in.next();
         System.out.println("Enter Height of Patient");
         Height=in.nextInt();
         System.out.println("Enter Weight of patient");
         Weight=in.nextDouble();
         System.out.println("ENTER year of Birth then ENTER Month of birth then ENTER Day of birth");
         YearOfBirth=in.nextInt();
         MonthOfbirth=in.nextInt();
         DayOfBirth=in.nextInt();
         System.out.println("ENTER year of InitialVisit then ENTER Month of Initial Visit then ENTER Day of initial visit");
         YearOfInitialVisit=in.nextInt();
         MonthOfInitialVisit=in.nextInt();
         DayOfInitialVisit=in.nextInt();
         System.out.println("ENTER year of Last visit then ENTER Month of last Visit then ENTER Day of last visit");
         YearOfLastVisit=in.nextInt();
         MonthOfLastvisit=in.nextInt();
         DayOfLastvisit=in.nextInt();
        Patients patient= new Patients(name,patientId,Address,Height,Weight,
                new GregorianCalendar(YearOfBirth,MonthOfbirth-1,DayOfBirth)
                ,new GregorianCalendar(YearOfInitialVisit,MonthOfInitialVisit-1,DayOfInitialVisit),
                new GregorianCalendar(YearOfLastVisit,MonthOfLastvisit-1,DayOfLastvisit));
        patientsList.add(patient);
        System.out.println("patient: "+name+" is added in the list");

    }
    //OPTION 3
    public static void DisplayPatientWith_ID(PatientsList patientsList,Scanner in){
        //takes in id if one doesnt exist prints id is wrong and lets the user input again
        String OverDue=null;
        System.out.println("please enter Patient ID");
        String patientId=in.next();
        int numberOfElements=patientsList.size();
        for(int i=0;i<numberOfElements;i++) {
            Patients patients= patientsList.getNext(i);
            if(patients.getPatientId().equals(patientId)) {
                String name = patients.getName();
                String Address = patients.getAddress();
                String patientid = patients.getPatientId();
                int Height = patients.getHeight();
                double Weight = patients.getWeight();
                int age = patients.get_Age();
                int NumberOfYearsAsPatient = patients.get_Time_As_Patient();
                int NumberOfYearsSinceVisit = patients.get_Time_Since_Last_Visit();
                if (NumberOfYearsSinceVisit >= 3) {
                    OverDue = "Patient is overdue for visit";
                }else{OverDue="Patient is not overdue for visit";}
                System.out.println("name: "+name+", "+"patient id: "+patientid+", "+"Address: "+Address+", "+"Height: "
                        +Height+", "+"Weight: "+Weight+", "+"age: "+age+", "
                        +"Number of years as patient: "+NumberOfYearsAsPatient+", "+"Number os years since last visit: "
                        +NumberOfYearsSinceVisit+", "+OverDue);
                break;
            }else if (i==numberOfElements-1){
                System.out.println("PatientID is wrong");
                DisplayPatientWith_ID(patientsList,in);
            }

        }
    }
    //OPTION 4
    public static void DeletePatient(PatientsList patientsList,Scanner in){
       //takes in id if one doesnt exist in the patients prints wrong id
        System.out.println("Enter Patient Id");
        String id = in.next();
        int numberOfElements=patientsList.size();
        for(int i=0;i<numberOfElements;i++) {
            Patients patients= patientsList.getNext(i);
            if(patients.getPatientId().equals(id)) {
                boolean deleted = patientsList.remove(patients);
                if(deleted){System.out.println("patient deleted");}
                break;
            }else if(i==numberOfElements-1){
                System.out.println("PatientID is wrong");
            }
        }
    }
    //OPTION 5
    public static void ShowAverage(PatientsList patientsList){
        //gets the users age, adds them all up and divides by the size of the list
        int AGE =  0 ;
        int numberOfElements=patientsList.size();
        for(int i=0;i<numberOfElements;i++) {
            Patients patients= patientsList.getNext(i);
            AGE = AGE +patients.get_Age();
        }
        int Average= AGE/numberOfElements;
        System.out.println("Average age of patients is "+ Average);

    }
    //OPTION 6
    public static void YoungestPatient(PatientsList patientsList){
        //loops through the list to get the smallest age
        int smallest;
        String OverDue;
        int numberOfElements=patientsList.size();
        Patients patients = patientsList.getNext(0);
        int age = patients.get_Age();
        smallest=age;
        for(int i=0;i<numberOfElements-1;i++) {
            patients=patientsList.getNext(i+1);
            age = patients.get_Age();
            if(smallest>age){
                smallest=age;
            }
        }
        //once it has the smallest it loops again to find the patient with the smallest age, prints the patients data
        for(int i=0;i<numberOfElements;i++) {
            patients=patientsList.getNext(i);
            if(patients.get_Age()==smallest){
                String name = patients.getName();
                String Address = patients.getAddress();
                String patientid = patients.getPatientId();
                int Height = patients.getHeight();
                double Weight = patients.getWeight();
                int ageof = patients.get_Age();
                int NumberOfYearsAsPatient = patients.get_Time_As_Patient();
                int NumberOfYearsSinceVisit = patients.get_Time_Since_Last_Visit();
                if (NumberOfYearsSinceVisit >= 3) {
                    OverDue = "Patient is overdue for visit";
                }else{OverDue="Patient is not overdue for visit";}
                System.out.println("name: "+ name+", "+"patient id: "+patientid+", "+"Address: "+Address+", "+"Height: "
                        +Height+", "+"Weight: "+Weight
                        +", "+"Age: "+ageof+", "+"Number of year as patient: "+NumberOfYearsAsPatient+", "
                        +"Number of years since last visit: "
                        +NumberOfYearsSinceVisit+", "+OverDue);
                break;
            }
        }

    }
    //OPTION 7
    public static void PatientsWhoAreOverdue(PatientsList patientsList){
        //loops through the list to find the user with time greater than 3 or more years since last visit
        int numberOfElements=patientsList.size();
        for(int i=0;i<numberOfElements;i++) {
            Patients patients=patientsList.getNext(i);
            if(patients.get_Time_Since_Last_Visit()>=3){
                String name = patients.getName();
                String patientid = patients.getPatientId();
                System.out.println("name: "+name+" "+"patient id: "+patientid);
            }

        }
    }
    //OPTION 8
    public static void Quit(PatientsList patientsList,Scanner in) {
        //asks the user if the want to save the changes if yes input y and enter file name to save to else do nothing
        System.out.println("Would you like to save the changes in the file Enter " + " y" + " for yes or " + "n" +
                " for quit");
        String input = in.next();
        if (input.equals("y")) {
            try {
                System.out.println("Enter file name: ");
                String filename=in.next();
                PrintWriter printWriter = new PrintWriter(new FileWriter(filename+".txt"));
                for (int i = 0; i < patientsList.size(); i++) {
                    Patients patients = patientsList.getNext(i);
                    printWriter.println(patients.getName());
                    printWriter.println(patients.getPatientId());
                    printWriter.println(patients.getAddress());
                    printWriter.println(patients.getHeight());
                    printWriter.println(patients.getWeight());
                    GregorianCalendar gregorianCalendar = patients.getDateOfBirth();
                    GregorianCalendar gregorianCalendar1 = patients.getDateOfInitialVisit();
                    GregorianCalendar gregorianCalendar2 = patients.getDateOfLastVisit();
                    int YearOfBirth = gregorianCalendar.getWeekYear();
                    int MonthOfbirth = gregorianCalendar.getTime().getMonth() + 1;
                    int DayOfBirth = gregorianCalendar.getTime().getDate();
                    int YearOfInitialVisit = gregorianCalendar1.getWeekYear();
                    int MonthOfInitialVisit = gregorianCalendar1.getTime().getMonth() + 1;
                    int DayOfInitialVisit = gregorianCalendar1.getTime().getDate();
                    int YearOfLastVisit = gregorianCalendar2.getWeekYear();
                    int MonthOfLastvisit = gregorianCalendar2.getTime().getMonth() + 1;
                    int DayOfLastvisit = gregorianCalendar2.getTime().getDate();
                    printWriter.println(YearOfBirth);
                    printWriter.println(MonthOfbirth);
                    printWriter.println(DayOfBirth);
                    printWriter.println(YearOfInitialVisit);
                    printWriter.println(MonthOfInitialVisit);
                    printWriter.println(DayOfInitialVisit);
                    printWriter.println(YearOfLastVisit);
                    printWriter.println(MonthOfLastvisit);
                    printWriter.println(DayOfLastvisit);
                }
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            return;
        }
    }
    }

