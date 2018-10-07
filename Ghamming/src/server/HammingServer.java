package server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringTokenizer;


// Start Server and client
// Client gets an input from user
// Client generates Hamming Code
// Client changes a random bit in the bit stream (Hamming Code)
// Client sends the adjusted hamming code
// Server receives the transmission (TCP Packet)
// Server runs a verification on the received stream
// Server finds any errors in the packet
// Server fixes the errors and outputs user's input

public class HammingServer {
	
	//port 998
	public static void main(String[] args) throws Exception {
        System.out.println("The capitalization server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new Capitalizer(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }
	
	
	private static class Capitalizer extends Thread {
        private Socket socket;
        private int clientNumber;
        

        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }

        public void run() {
           
        	try {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println("Welcome to the Hamming Code Simulator \n");

             
                while (true) {
                	
                	//gets the input from the client
                    String input = in.readLine();

                    //breaks the input into tokens 
            		StringTokenizer token = new StringTokenizer(input, "\t");

            		String hammingCode = token.nextToken();
            		String bArrayLength = token.nextToken();
            		String aArrayLength = token.nextToken();
            		
                	int arrLengthDiff =Integer.parseInt(bArrayLength)-Integer.parseInt(aArrayLength);
            		
                	//string to array
            		int hammingArray[] = fromString(hammingCode);
                    
            		;
            		
                    //sends the error to the client
                    out.println(receive(hammingArray,arrLengthDiff,Integer.parseInt(bArrayLength)));
                    
                    
                }
               
                
                
            } catch (IOException e) {
                log("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }
    }
	
	
	 private static int[] fromString(String string) {
		    String[] strings = string.replace("[", "").replace("]", "").split(", ");
		    int result[] = new int[strings.length];
		    for (int i = 0; i < result.length; i++) {
		      result[i] = Integer.parseInt(strings[i]);
		    }
		    return result;
		  }
	 
	static String receive(int a[], int parity_count, int arrLength) {
		
		String output;
		// This is the receiver code. It receives a Hamming code in array 'a'.
		// We also require the number of parity bits added to the original data.
		// Now it must detect the error and correct it, if any.
		
		int power;
		// We shall use the value stored in 'power' to find the correct bits to check for parity.
		
		int parity[] = new int[parity_count];
		// 'parity' array will store the values of the parity checks.
		
		String syndrome = new String();
		// 'syndrome' string will be used to store the integer value of error location.
		
		for(power=0 ; power < parity_count ; power++) {
			// We need to check the parities, the same number of times as the number of parity bits added.
			
			for(int i=0 ; i < a.length ; i++) {
				// Extracting the bit from 2^(power):
				
				int k = i+1;
				String s = Integer.toBinaryString(k);
				int bit = ((Integer.parseInt(s))/((int) Math.pow(10, power)))%10;
				if(bit == 1) {
					if(a[i] == 1) {
						parity[power] = (parity[power]+1)%2;
					}
				}
			}
			syndrome = parity[power] + syndrome;
		}
		// This gives us the parity check equation values.
		// Using these values, we will now check if there is a single bit error and then correct it.
		
		int error_location = Integer.parseInt(syndrome, 2);
		if(error_location != 0) {
			output = "Error is at location " + (arrLength - error_location) + "." +"\n";
			a[error_location-1] = (a[error_location-1]+1)%2;
			output += ("Corrected code is:");
			for(int i=0 ; i < a.length ; i++) {
				output += a[a.length-i-1];
			}
			output += "\n";
		}
		else {
			output = "There is no error in the received data.";
		}
		
		// Finally, we shall extract the original data from the received (and corrected) code:
		output += "Original data sent was:";
		power = parity_count-1;
		for(int i=a.length ; i > 0 ; i--) {
			if(Math.pow(2, power) != i) {
				output += a[i-1];
			}
			else {
				power--;
			}
		}
		output += "\n";
		return output;
	}

	
}
