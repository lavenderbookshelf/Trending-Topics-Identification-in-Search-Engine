import java.util.*;
import java.io.*;

public class keywordcounter {
  /*
      Major 3 Steps:
    1.  Read the content from file
    2.  Calculate the keywordcounter using Fibonacci Heap
    3.  Print output to output_file.txt
 */
     public static void main(String args[]) {

        // assigning filename to 'fileName' String from CMD line argument
            String fileName = args[0];

            MaxFibonacciHeap mfHeap = new MaxFibonacciHeap();
            //  keywords are keys and value is the pointer to the corresponding node in the Max Fibonacci heap
            Hashtable<String, DDGNode> hashtable = new Hashtable<String, DDGNode>();

           try {
              // Read from File and Buffer
               BufferedReader bReader = new BufferedReader(new FileReader(fileName));

              // Write to a file (output_file.txt) and write buffer
               BufferedWriter bWriter = new BufferedWriter(new FileWriter("output_file.txt"));

               String temp ;
               StringBuffer sBuffer   = new StringBuffer();

               // read file content from file
               while((temp = bReader.readLine()) != null) {
          	     //splitting the string to separate keyword and frequency
                    String strArray[] = temp.split(" ");
                    String keyword = strArray[0];
                    String checkStop = keyword.toLowerCase();

                    if(checkStop.equals("stop")) {
              	         break;
                    }

                    char x = keyword.charAt(0);
                    //check if first character is $
                    if(x == '$') {
              	      int frequency = Integer.parseInt(strArray[1]);
              	      //if keyword is not in hashtable then insert it
              	      if(!hashtable.containsKey(keyword)) {
                        DDGNode ddgnode = new DDGNode(frequency, keyword);
              		      hashtable.put(keyword, ddgnode);
              		      mfHeap.insert(ddgnode);
              	      }

                      // if keyword is already in the hashtable, update its frequency
              	      else {
              		      // get node in Fibonacci Heap
              		      DDGNode node = hashtable.get(keyword);

                        // update the frequency in Fibnacci Heap
              		      int newfrequency = node.key + frequency;
              		      mfHeap.increaseKey(node, newfrequency);

                        // put the new (key, frequency) pair in hashtable
              		      hashtable.put(keyword, node);
              	      }
                    }

                  // For Handling Query String i.e. if Number at the 1st position of input string
                    else {
              	      int queryNum = Integer.parseInt(keyword);             // Query Number
              	      int maxKey[] = new int[queryNum];                   //store max frequency

                      String keywordArray[] = new String[queryNum];		    //store max key
                      String outputKeyword;

              	      // For printing 'queryNum' number of most popular keywords
                      int i = 0;
                      while(i < queryNum){
                        //get the DDGNode with Maximum frequency
              		      DDGNode node = mfHeap.fetchMaxDDGNode();
              		      //get the keyword associated with above frequency
                            maxKey[i] = mfHeap.fetchMaxKey();
                            keywordArray[i] = node.keyword;

                            //print keyword in the output file
                            if(i < queryNum - 1) {
                      	      bWriter.append(keywordArray[i].substring(1) + ",");
                            }
                            else {
                  		        bWriter.append(keywordArray[i].substring(1));
                            }
                            // remove current Max from Fibnacci Heap and the Hashtable
                            mfHeap.removeMax();
                            hashtable.remove(keywordArray[i]);

                            i++;
                      }

          		      bWriter.newLine();

                    // insert the DDGNode back to heap and hashtable
                      int j = 0;
                      while(j < queryNum){
                        DDGNode node = new DDGNode(maxKey[j], keywordArray[j]);
                        mfHeap.insert(node);
                        hashtable.put(keywordArray[j], node);
                        j++;
                      }

                   }
              }

            // closing the stream releasing any system resources associated with it
           bReader.close();

           // writing the output string to file
           bWriter.write(sBuffer.toString());
           bWriter.close();

           }
           catch(FileNotFoundException e) {
             System.out.println("Error:\n"+e);
           }
           catch(IOException e) {
             System.out.println("Error:\n"+e);
           }
     }


}
