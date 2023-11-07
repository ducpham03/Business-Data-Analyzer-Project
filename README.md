# Coursework Project: Business Data Analyzer Java Project

This project aims to demonstrate the understanding of lists in the collection framework in Java by developing a business data analyzer that can analyze data available in an open dataset and answer queries given by the user about the dataset.

**Requirements**
- Use of the data exported from the SF Businesses Dataset
- Executing the user commands provided as arguments and making the output accordingly
- Switch implementation to use different collection data structures of implementation - LinkedList and ArrayList based on a flag passed as an argument
- Use generics in implementation
- Use an implementation of ListIterator (LinkedList and ArrayList)
- Remember all the commands entered by the user and execute them all in order of command entry

_______________
**Reading the data**

The implementation should use the exported format as CSV and read all the data into objects of a proper class structure for business entities. The application should be run as below using ArrayList and LinkedList implementation respectively based on the flag passed as the second argument:

 -  _java BusinessAnalyzer Registered_Business_Locations_-_San_Francisco.csv AL
  
- _java BusinessAnalyzer Registered_Business_Locations_-_San_Francisco.csv LL
  
Proper collection data structures should be used for loading the data.
_______________

**Processing the contents**

Once the content is loaded, the data will be kept in a separate list for each NAICS code. This can be done by using own implementation of ArrayList and LinkedList based on the arguments provided. The NAICS code is provided as a range in the dataset (eg. 4400-4599). In the next section, the user can ask for a particular NAICS code summary and you should be able to determine which range the code falls under. Arrange data structure to answer the questions.
_______________

**Executing user commands**

After reading the contents and analyzing it, ask the user to enter commands. The sample commands are given below. The program will exit when the user enters the command quit:

<br/>
Command: Zip 94108 Summary

Will produce the following output:

94018 Business Summary

Total Businesses: 3238

Business Types: 37

Neighborhood: 38

<br/>
Command: NAICS 4855 Summary

Will produce the following output:

Total Businesses: 3255

Zip Codes: 5

Neighborhood: 38

<br/>
Command: Summary

Will produce the following output:

Total Businesses: 303118

Closed Businesses: 30000

New Business in last year: 2000
_______________
**Remembering user commands**

As the user enters commands, place them in a queue and show them to the user when they want to see them with the command “history”. Use Java collection framework Queue implementation for this purpose.

Example:

<br/>
Command: History

Will produce the following output:

Zip 94108 Summary

NAICS 4855 Summary

Summary
_______________

**Conclusion**

The Business Data Analyzer project demonstrates the effectiveness of using lists in the collection framework in Java to store and manipulate data for analysis. The project also shows how data analysis can be used to gain valuable insights into business data, and how problem-solving skills are essential for developing and using a business data analyzer.
