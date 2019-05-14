# Welcome to Ghamming ! [![CodeFactor](https://www.codefactor.io/repository/github/gayuru/ghamming/badge)](https://www.codefactor.io/repository/github/gayuru/ghamming)

GUI Based Hamming Code app implemented using Socket Programming for Error detection at the network layer. Socket Porgramming on both server and client sides.

What makes Ghamming Special ?

* **Bitstream of any length** Instead of a fixed bit stream size , this will have the ability
to enter a Bit stream of any length ! How cool is that?
* **Random Error Generate** With a simple function it will generate an error within the bit stream
and send it to the server side to check for it.
### Simple Pseduo Code

```sh
/*
Start Server and client
Client gets an input from user (bit stream)
Client generates Hamming Code
Client changes a random bit in the bit stream (Hamming Code)
Client sends the adjusted hamming code
Server receives the transmission (TCP Packet)
Server runs a verification on the received stream
Server finds any errors in the packet
Server fixes the errors and outputs users input
*/
```

## How to run it ..
#### Cross Platform
* As simple as it gets - unzip the **HammingCodeGUI.zip**. 
* Then run this command on Terminal ;

 ```sh
$ cd HammingCodeGUI
$ java -jar Server.jar
```

* Open a new Terminal Winwdow while the Server Terminal is running !
 ```sh
$ cd HammingCodeGUI
$ java -jar Client.jar
```
* If it gives you a prompt saying install JAVA SDK go ahead and install it. Else you're all set to experience it !

PS : If any of the process gives you an error;
* Open the Eclipse project which is in a ZIP File
* First run the Server Class 
* Then run the Client Class

## Usage

* After the server is running on the background, open the **Client.jar** using the above mentione commands
* You will be prompted with a dialogue box;

![Ghamming1](https://imgur.com/qQl75jB.png)

* Enter the LocalHost Address for testing reasons (127.0.0.1)

![Ghamming2](https://imgur.com/a6uiFG5.png)

* In the textbox given, enter a bit stream (1 or 0) of any length.

![Ghamming2](https://imgur.com/yMNOsmh.png)

* A Message box will be prompted with information regarding the procedure

![Ghamming2](https://imgur.com/RpC7GK5.png)

* Then the server will correct the error and output it.


#### Contact info

* **Developer:** [Gayuru Gunawardena](https://gayurug.com)
               


---

