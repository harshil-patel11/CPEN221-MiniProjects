package ca.ubc.ece.cpen221.graphs.one;

import ca.ubc.ece.cpen221.graphs.core.Graph;
import ca.ubc.ece.cpen221.graphs.core.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TwitterAnalysis {
    private File file;
    private Set<String> twitterFollows;
    private Graph<String> twitterFollowNetwork;

    /*
     * Rep Invariant:
     * The file is non-null.
     * Each row in the file contains an entry of the form a -> b, where a and b are two strings
     * representing users.
     * Each vertex in twitterFollowNetwork must have a unique label that corresponds to a user in
     * file, and all users in file must be represented in twitterFollowNetwork as a vertex. If a
     * line in File has content a -> b, then twitterFollowNetwork must have an edge from a to b.
     * Each String in twitterFollows must correspond to a line in file.
     *
     * Abstraction Function:
     * A vertex in twitterFollowNetwork maps to a user (represented by a string in file), and an edge
     * from userA to userB represents userB following userA.
     * twitterFollows represents the set of lines in the file whose name is inputted as an argument
     * to the constructor
     */

    /**
     * Initializes all fields of TwitterAnalysis according to the rep invariant, where file is the
     * file with name fileName.
     *
     * @param fileName the name of the file to be analyzed.
     * @throws IOException thrown at runtime if file cannot be parsed successfully.
     */
    public TwitterAnalysis(String fileName) throws IOException {
        String pathName = "datasets/" + fileName;

        file = new File(pathName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        twitterFollows = new HashSet<>();

        twitterFollows = bufferedReader.lines().collect(Collectors.toSet());
        twitterFollowNetwork = new AdjacencyListGraph<>();

        for (String line : twitterFollows) {
            updateGraph(line, twitterFollowNetwork);
        }
    }

    /**
     * Updates the graph instance with a given line of data
     * in with the same format as the lines in twitter.txt.
     *
     * @param line  from the file to be parsed.
     * @param graph the graph to be updated
     */
    private void updateGraph(String line, Graph<String> graph) {
        String[] usersString = parseString(line, "->");

        Vertex<String> a = new Vertex<>(usersString[0], usersString[0]);
        Vertex<String> b = new Vertex<>(usersString[1], usersString[1]);

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addEdge(a, b);
    }

    /**
     * Returns a set all influencers that both userA and userB follow.
     * The set is empty if both users do not have common influencers.
     *
     * @param userA the first user. Must be a valid user.
     * @param userB the second user. Must be a valid user.
     * @return Returns a set all influencers that both userA and userB follow
     */
    public Set<String> commonInfluencers(String userA, String userB) {
        Set<String> userAInfluencers = new HashSet<>();
        Set<String> userBInfluencers = new HashSet<>();

        String[] edge;

        for (String follow : twitterFollows) {
            edge = parseString(follow, "->");

            if (edge[1].equals(userA)) {
                userAInfluencers.add(edge[0]);
            }
            if (edge[1].equals(userB)) {
                userBInfluencers.add(edge[0]);
            }
        }

        return userAInfluencers.stream().filter
            (x -> userBInfluencers.contains(x)).collect(Collectors.toSet());
    }

    /**
     * Returns the minimum number of retweets that it would take for a tweet from userA's feed to
     * appear in userB's feed. A tweet will appear in a user's feed if it was tweeted/retweeted by
     * someone that the user follows. If userA is userB, or userB follows userA directly, then no
     * retweets are needed for userA's tweets to appear in userB's feed.
     *
     * @param userA The string representing the user who sends the original tweet. Must represent a
     *              valid user.
     * @param userB The string representing the end user. Must represent a valid user.
     * @return the number of retweets it will take for a tweet from userA to appear in userB's feed
     */
    public int numRetweets(String userA, String userB) {
        Vertex<String> userAVertex = new Vertex<>(userA, userA);
        Vertex<String> userBVertex = new Vertex<>(userB, userB);

        System.out.println("Finished making graph");

        return followerDistance(userAVertex, userBVertex);
    }

    /**
     * Finds the minimum number of users in between initialUser and finalUser defined as follows (no pun
     * intended); If a follows b, b follows c, and c follows d, then the number of users between a
     * and b is 0, a and c is 1, b and d is 1, and a and d is 2. If there is no path from
     * initial user to final user, the number returned is "infinity" (the number of users between
     * c and a, for example, is "infinity"). initialUser and targetUser must be in
     * twitterFollowNetwork
     *
     * @return the number of users between initialUser and targetUser. If no path in the directed
     * graph twitterFollowNetwork exists from initialUser to targetUser, returns "infinity".
     */
    private int followerDistance(Vertex<String> initialUser,
                                 Vertex<String> targetUser) {
        HashSet<Vertex<String>> followers = new HashSet<>();
        HashSet<Vertex<String>> previousRow =
            new HashSet<>(twitterFollowNetwork.getNeighbors(initialUser));
        HashSet<Vertex<String>> nextRow = new HashSet<>();

        int distance = 0;

        if (initialUser.equals(targetUser)) {
            return 0;
        }

        while (previousRow.size() > 0) {
            followers.addAll(previousRow);

            for (Vertex<String> follower : previousRow) {
                if (follower.equals(targetUser)) {
                    return distance;
                } else {
                    nextRow.addAll(twitterFollowNetwork.getNeighbors(follower));
                }
            }

            distance++;
            nextRow.removeIf(followers::contains);

            previousRow.clear();
            previousRow.addAll(nextRow);
            nextRow.clear();
        }

        return Algorithms.INFINITE_DISTANCE;
    }

    /**
     * Takes a String as input and uses the delimiter to split the String
     * and saves the result into an array
     *
     * @param str   The string to be parsed, not null
     * @param delim delimiter, not null
     * @return array of parsed Strings
     */
    private String[] parseString(String str, String delim) {
        str = str.replaceAll("\\s+", "");
        String[] users = str.split(delim);
        return users;
    }

    /**
     * Takes args and runs the appropriate method on the two users. If the method name in args is
     * numRetweets, the number of retweets from the first user to the second user is what will be
     * printed. args[2] and args[3] must be strings that represent the name of valid users userA and
     * userB in the file with filename args[0].
     *
     * @param args the arguments of the function. Must contain four Strings that represent the name
     *             of the file to examine, the operation to perform, the identifier for the first
     *             user, and the identifier for the second user, respectively.
     */
    public static void main(String[] args) {
        /*
            main() should take four arguments:
            - The first argument should be a filename for a file holding
              a Twitter dataset.
            - The second should be one of "commonInfluencers" or "numRetweets".
            - The next two arguments should be identifiers for userA and userB.
            Then main() should invoke the appropriate method and write the result to
            standard output (often, the terminal). For "numRetweets", the output to
            standard output should be just the int. For "commonInfluencers", each
            of the influencers should be written to standard output, one per line,
            with no other text.
         */

        TwitterAnalysis tweet;

        try {
            tweet = new TwitterAnalysis(args[0]);
        } catch (Exception e) {
            throw new RuntimeException("IO Exception in Main");
        }

        if (args[1].equals("numRetweets")) {
            System.out.println(tweet.numRetweets(args[2], args[3]));
        } else if (args[1].equals("commonInfluencers")) {
            Set<String> influencers = tweet.commonInfluencers(args[2], args[3]);

            for (String s : influencers) {
                System.out.println(s);
            }
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }

}
