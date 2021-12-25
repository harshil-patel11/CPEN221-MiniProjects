package cpen221.mp3.server;

import cpen221.mp3.wikimediator.Query;
import cpen221.mp3.wikimediator.WikiMediator;
import cpen221.mp3.wikimediator.WikiMediatorJSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * WikiMediatorServer is a server that mediates a requested Wikipedia web page.
 * It can only handle one client at a time.
 * It handles requests in the form of a JSON formatted string.
 */

public class WikiMediatorServer {
    public static final int SERVER_PORT = 4949;
    public static final String DELIMITER = " <-> ";

    private final int maxRequests;
    private ServerSocket serverSocket;
    private WikiMediator wikiMediator;
    private boolean endServer;

    /*
     * RI: all fields are not null
     *
     * AF:
     * maxRequests is the number of clients that can concurrently send requests to the server
     * serverSocket represents the server sided socket
     * endServer represents a flag that controls the state of the server
     * (either terminated or functional)
     */

    /*
     * Thread Safety Argument:
     * This datatype is threadsafe because all the private fields are confined and there are
     * no shared variables besides immutable types.
     */

    /**
     * Start a server at a given port number, with the ability to process
     * upto n requests concurrently.
     *
     * @param port the port number to bind the server to
     *             port >= 0, port <= 65535
     * @param n    the number of concurrent requests the server can handle
     */
    public WikiMediatorServer(int port, int n) {
        maxRequests = n;
        endServer = false;

        initializeWikiMediator();

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException io) {
            throw new RuntimeException("Could not create a socket at port " + port);
        }

        try {
            serve();
        } catch (IOException e) {
            throw new RuntimeException("server socket closed unexpectedly");
        }

        createFile();
    }

    //SOURCE: FibonacciServer Example from CPEN 221

    /**
     * Run the server, listening for connections and handling them.
     *
     * @throws IOException if the main server socket is broken
     */
    synchronized private void serve() throws IOException {
        List<Socket> allClients = Collections.synchronizedList(new ArrayList<>());
        Set<Socket> currentClients = Collections.synchronizedSet(new HashSet<>());

        while (true) {
            Thread socketWaiter = new SocketWaiter();
            socketWaiter.start();

            while (socketWaiter.isAlive() && !endServer);

            if (endServer) {
                serverSocket.close();
                break;
            }

            Socket socket = ((SocketWaiter) socketWaiter).getSocket();

            while (currentClients.size() == maxRequests && !endServer);

            if (!allClients.contains(socket)) {
                allClients.add(socket);
                currentClients.add(socket);
                Thread handler =
                    new SocketHandler(currentClients, allClients.get(allClients.size() - 1));
                handler.start();
            }
        }

        allClients.clear();
        currentClients.clear();
        endServer = false;
    }

    /**
     * A class to allow the server to check for client connections in the background
     */
    private class SocketHandler extends Thread {
        private final Socket socket;
        private final Set<Socket> currentClients;

        /*
         * RI: socket and currentClients are not null
         * currentClients.contains(socket) = true;
         *
         * AF:
         * socket represents the socket from which the requests need to be extracted
         * and computed.
         * currentClients represents all the clients that are currently connected
         * to the server.
         */

        /**
         * Creates a SocketHandler object
         *
         * @param currentClients the set of clients that are currently connected to the server
         * @param socket         the socket from which the requests need to be extracted
         *                       and computed
         */
        public SocketHandler(Set<Socket> currentClients, Socket socket) {
            this.currentClients = currentClients;
            this.socket = socket;
        }

        /**
         * This method starts accepting client connections
         */
        @Override
        public void run() {
            try {
                try {
                    handle(socket);
                } finally {
                    currentClients.remove(socket);
                    socket.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace(); // but don't terminate serve()
            }
        }

    }

    /**
     * A class to allow the server to check for client connections in the background
     */
    private class SocketWaiter extends Thread {
        private Socket socket;

        /*
         * RI: socket is not null
         *
         * AF: socket represents a client sided socket that needs to connect to
         * the server
         */

        /**
         * This method starts accepting client connections
         */
        @Override
        public void run() {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Server Terminated");
            }
        }

        /**
         * This method returns the client socket that connected to the server socket
         *
         * @return client socket
         */
        public Socket getSocket() {
            return socket;
        }
    }

    //SOURCE: FibonacciServer Example from CPEN 221

    /**
     * A method to handle a single client request.
     * It returns when the client disconnects.
     *
     * @param - clientSocket socket that the client is connected to
     * @throws IOException - if the network encounters an error while handling.
     */
    private void handle(Socket clientSocket) throws IOException {
        List<String> requestsList = new ArrayList<>();

        // get the socket's input stream, and wrap converters around it
        // that convert it from a byte stream to a character stream,
        // and that buffer it so that we can read a line at a time
        BufferedReader in = new BufferedReader(new InputStreamReader(
            clientSocket.getInputStream()));

        // similarly, wrap character=>bytestream converter around the
        // socket output stream, and wrap a PrintWriter around that so
        // that we have more convenient ways to write Java primitive
        // types to it.
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
            clientSocket.getOutputStream()));

        try {
            // each request is a single line containing a number
            for (String jsonString = in.readLine(); jsonString != null;
                 jsonString = in.readLine()) {

                requestsList.add(jsonString);

                WikiMediatorJSONParser wikiMediatorJSONParser =
                    new WikiMediatorJSONParser(jsonString, wikiMediator);
                wikiMediatorJSONParser.performAction();
                boolean stop = wikiMediatorJSONParser.getStop();
                String jsonOutputString = wikiMediatorJSONParser.getJsonString();

                out.println(jsonOutputString);
                out.flush();

                if (stop || endServer) {
                    endServer = true;
                    break;
                }
            }
        } finally {
            out.close();
            in.close();
        }
    }

    /**
     * Creates a file containing statistical information about the state of the server's WikiMediator
     * Precondition: wikiMediator is not null
     */
    private void createFile() {
        File file = new File("local/wikiMediatorStats" + SERVER_PORT + ".txt");

        PrintWriter writer;

        try {
            writer = new PrintWriter(file);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not create new file");
        }

        writer.println(Long.toString(wikiMediator.peakLoad30s() - 1));

        Map<Query, Long> initialTimeMap = wikiMediator.getTimeMap();
        List<Query> pastQueries = wikiMediator.getQueries();

        for (Query q : initialTimeMap.keySet()) {
            writer.println(q.id() + DELIMITER + initialTimeMap.get(q));
            pastQueries.remove(q);
        }

        for (Query q : pastQueries) {
            writer.println(q.id());
        }

        writer.close();

    }

    /**
     * Initializes the server's wikiMediator using data written by the server before its last
     * shutdown
     */
    private void initializeWikiMediator() {
        File file = new File("local/wikiMediatorStats" + SERVER_PORT + ".txt");

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            String peakLoadString = reader.readLine();

            if (peakLoadString == null || peakLoadString.equals("")) {
                return;
            }

            Integer peakLoad = Integer.parseInt(peakLoadString);

            Map<Query, Long> initialTimeMap = new HashMap<>();
            List<Query> pastQueries = new ArrayList<>();
            String[] arguments;

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                arguments = line.split(DELIMITER);
                Query query = new Query(arguments[0]);

                pastQueries.add(query);

                if (arguments.length == 2) {
                    initialTimeMap.put(query, Long.parseLong(arguments[1]));
                }
            }

            wikiMediator = new WikiMediator(peakLoad, pastQueries, initialTimeMap);
        } catch (IOException ioe) {
            //throw new RuntimeException("Error reading file");
            System.err.println("Error reading file");
        } finally {
            file.delete();

            if (wikiMediator == null) {
                wikiMediator = new WikiMediator();
            }
        }

    }

}
