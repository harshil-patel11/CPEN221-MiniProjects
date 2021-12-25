package cpen221.mp3.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * WikiMediatorClient is a client that submits requests to the WikiMediatorServer
 * and interprets its responses.
 * A new WikiMediatorClient is "open" until it is terminated using close() at which
 * point it may not be used any further.
 */

public class WikiMediatorClient {
    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;

    /*
     * RI: all the fields are not null
     *
     * AF:
     * clientSocket is the client sided socket in the network
     * in represents the input stream from the output of the server
     * out represents the output stream of requests made by the client
     */


    //Source: CPEN 221 FibonacciServer example

    /**
     * WikiMediatorClient constructor for the client end of the network.
     *
     * @param hostname - the IP/network hostname for the network interface.
     * @param port     - the number to identify different ports to listen on the server.
     * @throws IOException if failure in setting up network client.
     */
    public WikiMediatorClient(String hostname, int port) throws IOException {
        clientSocket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    //Source: CPEN 221 FibonacciServer example

    /**
     * Method to send requests to the server in the form of a JSON formatted string
     * with appropriate parameters. Each request has a type that indicates the operation
     * that needs to be performed.
     * Requires: this is "open"
     *
     * @param jsonString the request as a JSON formatted string
     * @throws IOException if network or server error or failure
     */
    public void sendRequest(String jsonString) throws IOException {
        out.println(jsonString);
        out.flush();
    }

    //Source: CPEN 221 FibonacciServer example

    /**
     * Get a reply from the next request that was submitted by a client.
     * Requires: this is "open"
     *
     * @return - the result of the requests that the server sends from each port
     * back to the client.
     * @throws IOException if network or server error or failure
     */
    public String getReply() throws IOException {
        String jsonString = in.readLine();
//        if (jsonString == null) {
//            throw new IOException("connection terminated unexpectedly");
//        }

        return jsonString;
    }

    //Source: CPEN 221 FibonacciServer example

    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     *
     * @throws IOException if close fails
     */
    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
