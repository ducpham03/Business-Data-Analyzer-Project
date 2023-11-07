import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner; 
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.*;


/**
 * Business Analyzer Java Project
 * Duc Pham
 */

/**
 * Business class
 *  @String zipCode 
    @String startDate
    @String endDate
    @String naicsCode
    @String neighborhood
 */
class Business {
    
    String zipCode;
    String startDate;
    String endDate;
    String naicsCode;
    String neighborhood;

    public Business(String zipCode, String startDate, String endDate, String naicsCode, String neighborhood) {
        this.zipCode = zipCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.naicsCode = naicsCode;
        this.neighborhood = neighborhood;
    }
    
    public String getZipCode() {
        return zipCode;
    }

    public String getNaicsCode() {
        return naicsCode;
    }

    public String getNeighborhood() {
        return neighborhood;
    }
}

/**
 * BLL class - Create a LinkedList
 * @Node head
 * @int count - count the total business for Summary 
 * @HashSet<String> businessSet - Set of type of business
 * @HashSet<String> neighborhoodSet - Set of type of neighborhood
 */
class BLL {

    Node head;
    int count;
    HashSet<String> businessSet;
    HashSet<String> neighborhoodSet;
    
    public BLL() {
        head = null;
        count = 0;
        businessSet = new HashSet<>();
        neighborhoodSet = new HashSet<>();
    }
    class Node {
        Business newBusiness;
        Node next;

        Node(Business newBusiness, Node next) {
            this.newBusiness = newBusiness;
            this.next = next;
        }
        
        Node(Business a) {
            newBusiness = a;
            next = null;
        }
    } 
    
    public void add(Business newBusiness) 
    {  
        Node new_node = new Node(newBusiness); 
        
        if (head == null) { 
            head = new_node; 
        } 
        else { 
            Node last = head; 
            while (last.next != null) { 
                last = last.next; 
            } 
            last.next = new_node; 
        }   
        count++;
        businessSet.add(newBusiness.getNaicsCode()); 
        neighborhoodSet.add(newBusiness.getNeighborhood()); 
    }

    public int size() {
        return count;
    }
    public int businessSetSize() {
        return businessSet.size();
    }
    public int neighborhoodSetSize() {
        return neighborhoodSet.size();
    }
}

/**
 * BusinessLinkedList class - Manage and process data by LinkedList
 * @readFile method - read the data and store them in LinkedList
 * @command method - ask user to enter command and process the content
 */
class BusinessLinkedList {
    public static HashMap<Integer, BLL> businessNaicsCodeMap;
    public static HashMap<String, BLL> businessZipCodeMap;
    public static ArrayList<ArrayList<Integer>> naicsCodeRange;
    public static int totalSummary; 
    public static int closedBusiness;
    public static int newBusinessLastYear;

    public BusinessLinkedList() {
        businessZipCodeMap = new HashMap<>();
        businessNaicsCodeMap = new HashMap<>(); 
        naicsCodeRange = new ArrayList<>();
        totalSummary = 0;
        closedBusiness = 0;
        newBusinessLastYear = 0;
    }

    /**
     * readFile method
     * @param filePath
     * Read the data and add to HashMap by each zipCode
     * Also add to HashMap by each naicsCode
     * Count for the summary 
     */
    public static void readFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String[] firstRow = reader.readLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            String line = "";            

            while ((line = reader.readLine()) != null) {  
                totalSummary++;  
                String[] strArr = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                Business newBusiness = new Business(strArr[7], strArr[8], strArr[9], strArr[16], strArr.length == 32 ? strArr[31] : null);
            
                // Add to HashMap by zipCode
                String zipCode = newBusiness.getZipCode();
                BLL businessZip = new BLL();
                
                if (businessZipCodeMap.containsKey(zipCode)) {
                    businessZip = businessZipCodeMap.get(zipCode);
                    businessZip.add(newBusiness);
                } else {
                    businessZip = new BLL();
                    businessZip.add(newBusiness);
                }
                businessZipCodeMap.put(zipCode, businessZip);

                // Add to HashMap by naicsCode 
                String naicsCode = newBusiness.getNaicsCode();
                if (naicsCode.length()==0) {
                    continue;
                }
                String[] naicsCodeArr = naicsCode.split("-");
                int startRange = Integer.parseInt(naicsCodeArr[0]);
                BLL businessNaics = new BLL();
                if (businessNaicsCodeMap.containsKey(startRange)) {
                    businessNaics = businessNaicsCodeMap.get(startRange);
                    businessNaics.add(newBusiness);
                } else {
                    businessNaics.add(newBusiness);
                    ArrayList<Integer> range = new ArrayList<>() {
                        {
                            add(Integer.parseInt(naicsCodeArr[0]));
                            add(Integer.parseInt(naicsCodeArr[1]));
                        }
                    };
                    naicsCodeRange.add(range);
                }
                businessNaicsCodeMap.put(startRange, businessNaics);
                
                // Count for Summary
                String closedDate = strArr[9];
                if (! closedDate.equals("")) {
                    closedBusiness++;
                }
                String startDate = strArr[8];
                String[] startDateArr = startDate.split("/");
                if (startDateArr[2].equals("2022")) {
                    newBusinessLastYear++;
                }
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("ERROR! File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

    /**
     * command method - User enter command to see the content
     * If they ask for a specific zipCode, program will generate content based on the zipCode
     * If they ask for a specific naicsCode, program will find the range of naicsCode
     *      add it to a List, and find total, zip Code and neighborhood
     * If they ask for Summary, print out total business, closed business and new business
     */
    public static void command() {
        
        Queue<String> history = new LinkedList<>();
        System.out.println();
        System.out.print("Command: ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
    
            String input = scanner.nextLine();
            String[] inputArr = input.trim().split(" ");
            
            if (inputArr[0].equalsIgnoreCase("zip")) {
                history.add(input);

                String zipCode = inputArr[1];
                
                BLL myList = businessZipCodeMap.get(zipCode);

                if (myList.head != null) {
                    System.out.println("Total Businesses: " + myList.size());
                    System.out.println("Business Types: " + myList.businessSetSize());
                    System.out.println("Neighborhood: " + myList.neighborhoodSetSize());
                }
            }     

            else if (inputArr[0].equalsIgnoreCase("naics")) {
                history.add(input);
                int naicsCode = Integer.parseInt(inputArr[1]);
                for (int i=0; i<naicsCodeRange.size(); i++) {
                    if (naicsCodeRange.get(i).get(0)<=naicsCode && naicsCodeRange.get(i).get(1)>=naicsCode) {
                        int startRange = naicsCodeRange.get(i).get(0);
                        BLL myList = businessNaicsCodeMap.get(startRange);
    
                        System.out.println("Total Businesses: " + myList.size()); 
                        System.out.println("Zip Codes: " + myList.businessSetSize());
                        System.out.println("Neighborhood: " + myList.neighborhoodSetSize());
                
                    }
                }
            }

            else if (inputArr[0].equalsIgnoreCase("Summary") && inputArr.length==1) {
                history.add(input);
                System.out.println("Total Business: " + totalSummary);
                System.out.println("Closed Businesses: " + closedBusiness);
                System.out.println("New Business in last year: "+ newBusinessLastYear);
            }

            else if (inputArr[0].equalsIgnoreCase("quit") && inputArr.length==1) 
                return;

            else if (inputArr[0].equalsIgnoreCase("history") && inputArr.length==1) {
                Iterator iterator = history.iterator();
                while (iterator.hasNext()) {
                    System.out.println(iterator.next());
                }
            }
            System.out.println("");
            System.out.print("Command: ");
            
        }
    }
}




/**
 * BusinessArrayList class - Manage and process data by ArrayList
 * @readFile - read the data and store them in ArrayList
 * @command - ask user to enter command and process the content
 */
class BusinessArrayList {
    public static HashMap<String, ArrayList<Business>> businessZipCodeMap;
    public static HashMap<Integer, ArrayList<Business>> businessNaicsCodeMap;  
    public static ArrayList<ArrayList<Integer>> naicsCodeRange;
    public static int totalSummary; 
    public static int closedBusiness;
    public static int newBusinessLastYear;
    
    public BusinessArrayList() {
        businessZipCodeMap = new HashMap<>();
        businessNaicsCodeMap = new HashMap<>(); 
        naicsCodeRange = new ArrayList<>();
        totalSummary = 0;
        closedBusiness = 0;
        newBusinessLastYear = 0;
    }
    
    /**
     * readFile method
     * @param filePath
     * Read the data and add to HashMap by each zipCode
     * Also add to HashMap by each naicsCode
     * Count for the summary 
     */
    public static void readFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String[] firstRow = reader.readLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            String line = "";            
    
            while ((line = reader.readLine()) != null) {  
                totalSummary++;  
                String[] strArr = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                Business newBusiness = new Business(strArr[7], strArr[8], strArr[9], strArr[16], strArr.length == 32 ? strArr[31] : null);
            
                // Add to HashMap by zipCode
                String zipCode = newBusiness.getZipCode();
                ArrayList<Business> businessZip = new ArrayList<>();
                if (businessZipCodeMap.containsKey(zipCode)) {
                    businessZip = businessZipCodeMap.get(zipCode);
                    businessZip.add(newBusiness);
                } else {
                    businessZip.add(newBusiness);
                }
                businessZipCodeMap.put(zipCode, businessZip);
    
                // Add to HashMap by naicsCode 
                String naicsCode = newBusiness.getNaicsCode();
                if (naicsCode.length()==0) {
                    continue;
                }
                String[] naicsCodeArr = naicsCode.split("-");
                int startRange = Integer.parseInt(naicsCodeArr[0]);
                ArrayList<Business> businessNaics;
                if (businessNaicsCodeMap.containsKey(startRange)) {
                    businessNaics = businessNaicsCodeMap.get(startRange);
                    businessNaics.add(newBusiness);
                } else {
                    businessNaics = new ArrayList<>();
                    businessNaics.add(newBusiness);
                    ArrayList<Integer> range = new ArrayList<>() {
                        {
                            add(Integer.parseInt(naicsCodeArr[0]));
                            add(Integer.parseInt(naicsCodeArr[1]));
                        }
                    };
                    naicsCodeRange.add(range);
                }
                businessNaicsCodeMap.put(startRange, businessNaics);
                
                // Count for Summary
                String closedDate = strArr[9];
                if (! closedDate.equals("")) {
                    closedBusiness++;
                }
    
                String startDate = strArr[8];
                String[] startDateArr = startDate.split("/");
                if (startDateArr[2].equals("2022")) {
                    newBusinessLastYear++;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR! File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 

    /**
     * command method - User enter command to see the content
     * If they ask for a specific zipCode, program will generate content based on the zipCode
     * If they ask for a specific naicsCode, program will find the range of naicsCode
     *      add it to a List, and find total, zip Code and neighborhood
     * If they ask for Summary, print out total business, closed business and new business
     */
    public static void command() {
        
        Queue<String> history = new LinkedList<>();

        System.out.println();
        System.out.print("Command: ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
    
            String input = scanner.nextLine();
            String[] inputArr = input.trim().split(" ");

            if (inputArr[0].equalsIgnoreCase("zip")) {
                history.add(input);
                String zipCode = inputArr[1];
                ArrayList<Business> myList = businessZipCodeMap.get(zipCode);
                if (! (myList==null)) {
                    System.out.println("Total Businesses: " + myList.size()); 
                    HashSet<String> businessSet = new HashSet<>();
                    HashSet<String> neighborhoodSet = new HashSet<>();
                    for (int i=0; i<myList.size(); i++) {
                        businessSet.add(myList.get(i).getNaicsCode());
                        neighborhoodSet.add(myList.get(i).getNeighborhood());
                    }
                    System.out.println("Business Types: " + businessSet.size());
                    System.out.println("Neighborhood: " + neighborhoodSet.size());
                }
            }
            else if (inputArr[0].equalsIgnoreCase("NAICS")) {
                history.add(input);
                int naicsCode = Integer.parseInt(inputArr[1]);
                for (int i=0; i<naicsCodeRange.size(); i++) {
                    if (naicsCodeRange.get(i).get(0)<=naicsCode && naicsCodeRange.get(i).get(1)>=naicsCode) {
                        int startRange = naicsCodeRange.get(i).get(0);
                        ArrayList<Business> myList = businessNaicsCodeMap.get(startRange);
    
                        System.out.println("Total Businesses: " + myList.size()); 
                        HashSet<String> zipCodeSet = new HashSet<>();
                        HashSet<String> neighborhoodSet = new HashSet<>();
                        for (int j=0; j<myList.size(); j++) {
                            zipCodeSet.add(myList.get(j).getZipCode());
                            neighborhoodSet.add(myList.get(j).getNeighborhood());
                        }
                        System.out.println("Zip Codes: " + zipCodeSet.size());
                        System.out.println("Neighborhood: " + neighborhoodSet.size());
                    }
                }
            }
            else if (inputArr[0].equalsIgnoreCase("Summary") && inputArr.length==1) {
                history.add(input);
                System.out.println("Total Business: " + totalSummary);
                System.out.println("Closed Businesses: " + closedBusiness);
                System.out.println("New Business in last year: "+ newBusinessLastYear);
            }
            else if (inputArr[0].equalsIgnoreCase("quit") && inputArr.length==1) {
                return;
            } 
            else if (inputArr[0].equalsIgnoreCase("history") && inputArr.length==1) {
                Iterator iterator = history.iterator();
                while (iterator.hasNext()) {
                    System.out.println(iterator.next());
                }
            }    
            System.out.println("");
            System.out.print("Command: ");
        }
    }
}

/*
 * Main Class
 * @main method
 */
public class BusinessAnalyzer {

    public static void main(String[] args) {
        
        if (args.length < 2) {
            System.out.println("ERROR!");
            return;
        }

        String filePath = args[0];
        String flag = args[1];

        if (flag.equalsIgnoreCase("AL")) {
            BusinessArrayList BusinessByAL = new BusinessArrayList();
            BusinessByAL.readFile(filePath);    
            BusinessByAL.command(); 
        } 

        else if (flag.equalsIgnoreCase("LL")) {
            BusinessLinkedList BusinessByLL = new BusinessLinkedList();
            BusinessByLL.readFile(filePath);
            BusinessByLL.command();
        }
    }
}
