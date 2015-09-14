# Conversion Microservices
For this exercise we will build a variety of conversion servers---programs which take a number with one input unit and output something different, e.g., converting from feet to inches.  

**You will do this exercise in groups of 2-3 students.** Each table must have three different groups if possible.

## Pick a Service. Write a Server.
Each group must pick one of the services from the following list.  No duplicates can be picked until all services have been selected once. Alternatively, you can propose a new conversion, as long as one of its units is already included in the list below.

```
1.  feet <-> inches
2.  inches <-> cm
3.  cm <-> m
4.  kg <-> g
5.  kg <-> pounds
6.  pounds <-> ounces
7.  ounces of bananas <-> dollars
8.  pounds of bananas <-> bananas
9.  bananas <-> length in inches
10. bananas <-> grams of potassium
11. dollars <-> Japanese Yen
```

To be consistent, we will use these abbreviations:
The units should use the following abbreviations:

Abbreviation | Unit
------|-----
ft | feet
in | inches
cm | centimeters
m | meters
kg | kilograms
g | grams
lbs | pounds
b | bananas
$ | dollars
y | yen

Your group must write a server which accepts requests to convert between the two units you have selected. It should be able to convert in either direction.  

**You may choose to write your server in C, Python, or Java. I suggest Python or Java.** There are example server programs which can send a welcome message to the client and receive an input string. You should copy one of these to make your program.

#### Protocol Specification

Your server program must follow this protocol:
  * Server waits for client connections
  * Client connects to server
  * Server sends a one line message to client saying what the server can do.
    * For example: `Welcome to the Feet (ft) to Inches (in) conversion server!\n`
  * Client sends a request to the server
    * This must be a space separated string: `<input unit> <output unit> <input amount>\n`
    * The string should end with a new line character. The units must be the abbreviations in the table above.
  * Server responds with the conversion result
    * This must be a string and will end with a a new line character, e.g., `"43.2356\n"`
  * The client and server close the socket
  * The server waits for another client to connect

For example the request `"ft in 1.25"` should get the response `"15"`, while `"in ft 30"` should return `"2.5"`.

You should test that your server works using `telnet` as the client.

#### Parsing Requests
You will have to use string parsing functions to extract the different portions of the request.  Here are some tips.

##### C version:
You could use:
```
char * strtok (char *restrict newstring, const char *restrict delimiters)
```

This function returns a piece of `newstring` that has been separated using the delimiter character from `delimiters`. For example:

```
const char s[2] = " "; // space as delimiter
char *token;
/* get the first token */
token = strtok(str, s);  
/* walk through other tokens */
while( token != NULL ) {
  printf( " %s\n", token )   
  token = strtok(NULL, s);
}
```

Learn more: http://www.gnu.org/software/libc/manual/html_node/Finding-Tokens-in-a-String.html

##### Java version:
You could use the "split" method as below:

```
String[] tokens = str.split("  ");
for(String s:tokens){
       System.out.println(s);
   }
```

Learn more: http://javadevnotes.com/java-string-split-tutorial-and-examples

##### Python version:
You could use split function as below:
```
mylist = inputString.split(" ")
for item in mylist:
   print item
```
Learn more: http://www.tutorialspoint.com/python/string_split.htm

**You will also need to convert from strings to floating point numbers.  You can use any websites you'd like to learn how to do that.**

## Answering Questions with Multiple Services
Using these services, we will try to answer questions like:
  - How many kilograms is 45.6 pounds?
  - If I lay 150 bananas end to end, how long will they be in feet?
  - How much would it cost in Japanese Yen for enough bananas to get 43 grams of potassium?
  - If I buy $56.34 worth of bananas and lay them end to end, how long will they be in centimeters?

To do this, you will have to make a server that is capable of talking with other servers!  Try it out!

What is the most interesting question you can answer using the servers written by groups at your table?  What if you talk with students from other tables?
