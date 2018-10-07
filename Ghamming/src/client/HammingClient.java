package client;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class HammingClient {

	private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Hamming Code Simulator");
    private JTextField dataField = new JTextField(30);
    private JTextArea messageArea = new JTextArea(10, 50);
    private JLabel labelInfo = new JLabel("Enter a bit stream of any length");
    JPanel panel1 = new JPanel();
    JButton sendInfo = new JButton("Send Information");  
    
    JPanel panel2 = new JPanel();
    JPanel panelSpace = new JPanel();

    public HammingClient() {
    	
    	// Layout GUI
        messageArea.setEditable(false);
        
        panelSpace.add(Box.createVerticalStrut(100));
      
        panel1.setBorder(BorderFactory.createTitledBorder("CLIENT"));
        panel2.setBorder(BorderFactory.createTitledBorder("SERVER"));
        
        BoxLayout layout1 = new BoxLayout(panel1, BoxLayout.Y_AXIS);
		BoxLayout layout2 = new BoxLayout(panel2, BoxLayout.Y_AXIS);
		
		
		panel1.setLayout(layout1);
		panel2.setLayout(layout2);
		
		
		// Add the buttons into the panel with three different alignment options
		dataField.setAlignmentX(Component.CENTER_ALIGNMENT);
		sendInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
		labelInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
		new JScrollPane(messageArea).setAlignmentX(Component.CENTER_ALIGNMENT);
		panel1.add(labelInfo);
		panel1.add(dataField);
		panel1.add(sendInfo);
		
		
		panel2.add(new JScrollPane(messageArea));

		frame.setLayout(new FlowLayout());
		
		frame.getContentPane().add(panel1);
		frame.getContentPane().add(panelSpace);
		frame.getContentPane().add(panel2);

				
        // Add Listeners
        ActionListener listener = new ActionListener() {
            /**
             * Responds to pressing the enter key or pressing the button in the textfield
             * by sending the contents of the text field to the
             * server and displaying the response from the server
             * in the text area.  If the response is "." we exit
             * the whole application, which closes all sockets,
             * streams and windows.
             */
            public void actionPerformed(ActionEvent e) {
              
            	//get the userinput
            	String userInput = dataField.getText();
            	
            	int n = userInput.length();
            	
            	int a[] = new int[n];
            	
            	String arrayString[] = userInput.split("(?!^)");
            	
            	int[] array = Arrays.stream(arrayString).mapToInt(Integer::parseInt).toArray();
            	
            	
            	//invert array order 
            	  for (int i = 0; i < array.length / 2; i++) {
            	        Object temp = array[i];
            	        array[i] = array[array.length - 1 - i];
            	        array[array.length - 1 - i] = (int) temp;
            	    }
            	  
            	  
            	//calls the function to generate the hamming code
            	int b[] = generateCode(array);
            	

            	String arrConv = "";
            	for (int k=0; k<b.length;k++) {
            		
            		arrConv += b[k];
            		
            	}
            	
            	//random bit changed 
            	int error = getRandomNumberInRange(1, n);
            	
            	b[error-1] = (b[error-1]+1)%2;
            	
            	String errorArr = "";
            	for (int k=0; k<b.length;k++) {
            		
            		errorArr += b[k];
            		
            	}
            	
            	
            	String showProgress = "Generated Hamming Code: "+ arrConv +"\nRandom error at Bit Position "+(b.length-error)+" was made \nTherefore the Sent Code: "+errorArr;
            			
            	JOptionPane.showMessageDialog(frame, showProgress);
         
            	
            	//sends the adjusted hamming code to the server side
            	out.println(Arrays.toString(b)+"\t"+b.length+"\t"+a.length);
            	
                String response = null;
                
              
                try {

                	response = ReadBigStringIn(in);
                   
                    if (response == null || response.equals("")) {
                          System.exit(0);
                    }
                    
                } catch (IOException ex) {
                     response = "Error: " + ex;
                }
                messageArea.append(response + "\n");
                dataField.selectAll();
            }
        };
   
        //button actions added
        sendInfo.addActionListener(listener);
    	dataField.addActionListener(listener);
    }
    
    public void returnArray(int[] array) {
    	
    }
  
    private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
    
    public String ReadBigStringIn(BufferedReader buffIn) throws IOException {
        StringBuilder everything = new StringBuilder();
        String line;

        
        for(int j=0;j<=3;j++) {
        	line = buffIn.readLine();
        	everything.append(line+"\n");
        }
        
        return everything.toString();
    }
    
    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Hamming Code Simulator",
            JOptionPane.QUESTION_MESSAGE);

        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
		 ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 3; i++) {
            messageArea.append(in.readLine() + "\n");
        }
    }
    
    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        HammingClient client = new HammingClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setBounds(100, 100, 640, 380);
        //client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();
    }
    
    
    static int[] generateCode(int a[]) {
		// We will return the array 'b'.
		int b[];
		
		// We find the number of parity bits required:
		int i=0, parity_count=0 ,j=0, k=0;
		while(i<a.length) {
			// 2^(parity bits) must equal the current position
			// Current position is (number of bits traversed + number of parity bits + 1).
			// +1 is needed since array indices start from 0 whereas we need to start from 1.
			
			if(Math.pow(2,parity_count) == i+parity_count + 1) {
				parity_count++;
			}
			else {
				i++;
			}
		}
		
		// Length of 'b' is length of original data (a) + number of parity bits.
		b = new int[a.length + parity_count];
		
		// Initialize this array with '2' to indicate an 'unset' value in parity bit locations:
		
		for(i=1 ; i<=b.length ; i++) {
			if(Math.pow(2, j) == i) {
				// Found a parity bit location.
				// Adjusting with (-1) to account for array indices starting from 0 instead of 1.
				
				b[i-1] = 2;
				j++;
			}
			else {
				b[k+j] = a[k++];
			}
		}
		for(i=0 ; i<parity_count ; i++) {
			// Setting even parity bits at parity bit locations:
			
			b[((int) Math.pow(2, i))-1] = getParity(b, i);
		}
		return b;
	}
	
	static int getParity(int b[], int power) {
		int parity = 0;
		for(int i=0 ; i < b.length ; i++) {
			if(b[i] != 2) {
				// If 'i' doesn't contain an unset value,
				// We will save that index value in k, increase it by 1,
				// Then we convert it into binary:
				
				int k = i+1;
				String s = Integer.toBinaryString(k);
				
				// Now if the bit at the 2^(power) location of the binary value of index is 1,
				// Then we need to check the value stored at that location.
				// Checking if that value is 1 or 0, we will calculate the parity value.
				
				int x = ((Integer.parseInt(s))/((int) Math.pow(10, power)))%10;
				if(x == 1) {
					if(b[i] == 1) {
						parity = (parity+1)%2;
					}
				}
			}
		}
		return parity;
	}
	
	
    
}