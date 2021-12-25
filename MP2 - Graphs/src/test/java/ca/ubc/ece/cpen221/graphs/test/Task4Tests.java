package ca.ubc.ece.cpen221.graphs.test;

import ca.ubc.ece.cpen221.graphs.one.Algorithms;
import ca.ubc.ece.cpen221.graphs.one.TwitterAnalysis;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

public class Task4Tests {
    @Test
    public void testTwitterAnalysis1() {
        TwitterAnalysis t;
        Set<String> users = Set.of("a", "b", "c", "d", "e");
        Set<String> commonInfluencers;

        try {
            t = new TwitterAnalysis("twitter1.txt");

            for (String userA : users) {
                for (String userB : users) {
                    if (!userA.equals(userB)) {
                        commonInfluencers = t.commonInfluencers(userA, userB);
                        assertEquals(0, t.numRetweets(userA, userB));
                        assertEquals(3, commonInfluencers.size());
                        assertTrue(users.containsAll(commonInfluencers) &&
                            !commonInfluencers.contains(userA) &&
                            !commonInfluencers.contains(userB));
                    }
                }
            }
        } catch (IOException ioe) {
            assertTrue(false);
        }
    }

    @Test
    public void testTwitterAnalysis2() {
        TwitterAnalysis t;
        Set<String> users = Set.of("a", "b", "c", "d", "e", "f", "g");
        Set<String> commonInfluencers;

        try {
            t = new TwitterAnalysis("twitter2.txt");

            assertEquals(0, t.numRetweets("a", "b"));
            assertEquals(0, t.numRetweets("d", "d"));
            assertEquals(0, t.numRetweets("c", "g"));
            assertEquals(1, t.numRetweets("a", "f"));
            assertEquals(1, t.numRetweets("a", "g"));
            assertEquals(Algorithms.INFINITE_DISTANCE, t.numRetweets("g", "c"));

            assertEquals(Set.of("a", "f"), t.commonInfluencers("b", "c"));
            assertEquals(Set.of(), t.commonInfluencers("f", "a"));
            assertEquals(Set.of("g", "a"), t.commonInfluencers("e", "e"));


        } catch (IOException ioe) {
            assertTrue(false);
        }
    }

    @Test
    public void testTwitterAnalysis3() {
        try {
            TwitterAnalysis.main(new String[] {"twitter2.txt", "numRetweets", "a", "f"});
        } catch (RuntimeException rte) {
            fail();
        }

        try {
            TwitterAnalysis.main(new String[] {"twitter2.txt", "commonInfluencers", "a", "f"});
        } catch (RuntimeException rte) {
            fail();
        }

        try {
            TwitterAnalysis.main(new String[] {"twitter1.txt", "commonInfluencers", "c", "b"});
        } catch (RuntimeException rte) {
            fail();
        }

        try {
            TwitterAnalysis
                .main(new String[] {"Sathish.txt", "numRetweets", "Paul", "Victoria"});
            fail("Didn't throw exception!");
        } catch (RuntimeException rte) {
            assertTrue(true);
        }

        try {
            TwitterAnalysis
                .main(new String[] {"twitter1.txt", "doSomething", "Paul", "Victoria"});
            fail("Didn't throw exception!");
        } catch (RuntimeException rte) {
            assertTrue(true);
        }
    }

    @Test
    public void testTwitterAnalysis4() {
        TwitterAnalysis t;
        Set<String> users = Set.of("a", "b", "c", "d", "e", "f");
        Set<String> commonInfluencers;

        try {
            t = new TwitterAnalysis("twitter3.txt");

            assertEquals(0, t.numRetweets("a", "b"));
            assertEquals(0, t.numRetweets("d", "d"));
            assertEquals(0, t.numRetweets("c", "d"));
            assertEquals(1, t.numRetweets("a", "c"));
            assertEquals(4, t.numRetweets("a", "f"));
            assertEquals(Algorithms.INFINITE_DISTANCE, t.numRetweets("f", "c"));

            assertEquals(Set.of(), t.commonInfluencers("b", "c"));
            assertEquals(Set.of(), t.commonInfluencers("f", "a"));
            assertEquals(Set.of("d"), t.commonInfluencers("e", "e"));


        } catch (IOException ioe) {
            assertTrue(false);
        }
    }

    @Test
    public void testTwitterAnalysis5() {
        TwitterAnalysis t;
        Set<String> users = Set.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l");
        Set<String> commonInfluencers;

        try {
            t = new TwitterAnalysis("twitter4.txt");

            assertEquals(0, t.numRetweets("a", "b"));
            assertEquals(1, t.numRetweets("a", "h"));
            assertEquals(1, t.numRetweets("l", "g"));
            assertEquals(Algorithms.INFINITE_DISTANCE, t.numRetweets("m", "y"));

            assertEquals(Set.of("c", "a", "l"), t.commonInfluencers("m", "i"));
            assertEquals(users, t.commonInfluencers("m", "m"));
            assertEquals(Set.of("b"), t.commonInfluencers("e", "e"));
        } catch (IOException ioe) {
            assertTrue(false);
        }
    }

}
