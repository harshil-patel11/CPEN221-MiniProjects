package cpen221.mp3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpen221.mp3.fsftbuffer.Bufferable;
import cpen221.mp3.fsftbuffer.BufferableInteger;
import cpen221.mp3.fsftbuffer.FSFTBuffer;
import cpen221.mp3.fsftbuffer.IDNotFoundException;
import cpen221.mp3.server.WikiMediatorClient;
import cpen221.mp3.server.WikiMediatorServer;
import cpen221.mp3.wikimediator.WikiMediator;
import cpen221.mp3.wikimediator.WikiMediatorInfo;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class Tests {

    /*
        You can add your tests here.
        Remember to import the packages that you need, such
        as cpen221.mp3.fsftbuffer.
     */

    // ENABLE ASSERTIONS

    @Test
    public void test_put1() {
        FSFTBuffer buffer = new FSFTBuffer(1, 100);
        Bufferable t = new BufferableInteger(1);
        assertTrue(buffer.put(t));
        try {
            Bufferable obj = buffer.get(t.id());
            assertEquals(obj, t);
        } catch (Exception e) {
            System.out.println("test failed");
        }
    }

    @Test
    public void test_put2() {
        FSFTBuffer buffer = new FSFTBuffer(1, 100);
        Bufferable t1 = new BufferableInteger(1);
        Bufferable t2 = new BufferableInteger(2);
        assertTrue(buffer.put(t1));
        assertTrue(buffer.put(t2));
        try {
            Bufferable obj = buffer.get(t2.id());
            assertEquals(obj, t2);
        } catch (Exception e) {
            System.out.println("test failed");
        }
    }

    @Test(expected = IDNotFoundException.class)
    public void test_put3() throws IDNotFoundException {
        FSFTBuffer buffer = new FSFTBuffer(2, 100);
        Bufferable t1 = new BufferableInteger(1);
        Bufferable t2 = new BufferableInteger(2);
        Bufferable t3 = new BufferableInteger(3);

        try {
            assertTrue(buffer.put(t1));
            Thread.sleep(10);
            assertTrue(buffer.put(t2));
            Thread.sleep(10);
            assertTrue(buffer.put(t3));
            Bufferable obj2 = buffer.get(t2.id());
            assertEquals(obj2, t2);
            Bufferable obj3 = buffer.get(t3.id());
            assertEquals(obj3, t3);
        } catch (Exception e) {
            fail("test failed: should not throw any exceptions");
        }

        buffer.get(t1.id());
    }

    @Test(expected = IDNotFoundException.class)
    public void test_put4() throws IDNotFoundException {
        FSFTBuffer buffer = new FSFTBuffer(2, 1);
        Bufferable t1 = new BufferableInteger(1);
        Bufferable t2 = new BufferableInteger(2);
        Bufferable t3 = new BufferableInteger(3);

        try {
            assertTrue(buffer.put(t1));
            Thread.sleep(100);
            assertTrue(buffer.put(t2));
            Thread.sleep(910);
            assertTrue(buffer.put(t3));
            Bufferable obj2 = buffer.get(t2.id());
            assertEquals(obj2, t2);
            Bufferable obj3 = buffer.get(t3.id());
            assertEquals(obj3, t3);
        } catch (Exception e) {
            fail("test failed: should not throw any exceptions");
        }

        buffer.get(t1.id());
    }

    @Test
    public void test_put5() {
        FSFTBuffer buffer = new FSFTBuffer(1, 1);
        Bufferable t = new BufferableInteger(1);
        assertTrue(buffer.put(t));
        try {
            Thread.sleep(10);
            assertFalse(buffer.put(t));
            Bufferable obj = buffer.get(t.id());
            assertEquals(obj, t);
        } catch (Exception e) {
            System.out.println("test failed");
        }
    }

    @Test
    public void test_put6() {
        FSFTBuffer buffer = new FSFTBuffer(1, 2);
        Bufferable t = new BufferableInteger(1);
        assertTrue(buffer.put(t));
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            fail("Exception not expected");
        }

        assertFalse(buffer.put(t));

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            fail("Exception not expected");
        }

        assertTrue(buffer.put(t));
    }

    @Test
    public void test_get1() {
        FSFTBuffer buffer = new FSFTBuffer(1, 1);
        Bufferable t = new BufferableInteger(1);
        assertTrue(buffer.put(t));
        try {
            Bufferable obj = buffer.get(t.id());
            assertEquals(obj, t);
        } catch (IDNotFoundException idnfe) {
            fail("test failed: should not throw any exceptions");
        }
    }

    @Test(expected = IDNotFoundException.class)
    public void test_get2() throws IDNotFoundException {
        FSFTBuffer buffer = new FSFTBuffer(1, 1);
        Bufferable t = new BufferableInteger(1);
        assertTrue(buffer.put(t));

        try {
            Thread.sleep(1010);
        } catch (InterruptedException ie) {
            fail("Threw wrong exception");
        }

        buffer.get(t.id());
    }

    @Test
    public void test_get3() {
        FSFTBuffer buffer = new FSFTBuffer(4, 1);
        Bufferable t1 = new BufferableInteger(1);
        Bufferable t2 = new BufferableInteger(2);
        Bufferable t3 = new BufferableInteger(3);
        Bufferable t4 = new BufferableInteger(4);
        Bufferable t5 = new BufferableInteger(5);

        try {
            assertTrue(buffer.put(t1));
            Thread.sleep(1010);
            assertTrue(buffer.put(t2));
            Thread.sleep(1010);
            assertTrue(buffer.put(t3));
            Thread.sleep(1010);
            assertTrue(buffer.put(t4));
        } catch (InterruptedException ie) {
            fail("Threw wrong exception");
        }

        buffer.touch(t1.id());

        assertTrue(buffer.put(t5));

        try {
            buffer.get("2");
            fail("Did not throw exception");
        } catch (IDNotFoundException idnfe) {
            assertFalse(buffer.touch("2"));
        }
    }

    @Test
    public void test_touch1() {
        FSFTBuffer buffer = new FSFTBuffer(1, 1);
        Bufferable t = new BufferableInteger(1);
        assertTrue(buffer.put(t));
        try {
            Thread.sleep(500);
            boolean touched = buffer.touch(t.id());
            Bufferable obj = buffer.get(t.id());
            assertEquals(obj, t);
            assertTrue(touched);
        } catch (Exception e) {
            fail("test failed: should not throw any exceptions");
        }
    }

    @Test
    public void test_touch2() {
        FSFTBuffer buffer = new FSFTBuffer(1, 1);
        Bufferable t = new BufferableInteger(1);
        assertTrue(buffer.put(t));
        try {
            Thread.sleep(1010);
        } catch (InterruptedException ie) {
            fail("threw wrong exception");
        }

        boolean touched = buffer.touch(t.id());
        assertFalse(touched);
    }

    @Test
    public void test_update1() {
        FSFTBuffer buffer = new FSFTBuffer(1, 1);
        Bufferable t1 = new BufferableInteger(1);
        Bufferable t2 = new BufferableInteger(1);
        assertTrue(buffer.put(t1));
        try {
            Thread.sleep(10);
        } catch (InterruptedException ie) {
            fail("threw wrong exception");
        }

        assertTrue(buffer.update(t2));
    }

    @Test
    public void test_update2() {
        FSFTBuffer buffer = new FSFTBuffer(1, 1);
        Bufferable t1 = new BufferableInteger(1);
        Bufferable t2 = new BufferableInteger(2);
        Bufferable t3 = new BufferableInteger(2);
        assertTrue(buffer.put(t1));
        try {
            Thread.sleep(1010);
            assertTrue(buffer.put(t2));
        } catch (InterruptedException ie) {
            fail("threw wrong exception");
        }

        assertFalse(buffer.update(t1));
        assertTrue(buffer.update(t3));
    }

    @Test
    public void test_defaultConstructor() {
        FSFTBuffer buffer = new FSFTBuffer();
        Bufferable t = new BufferableInteger(1);
        assertTrue(buffer.put(t));
        try {
            Thread.sleep(900);
        } catch (InterruptedException ie) {
            fail("threw wrong exception");
        }

        boolean touched = buffer.touch(t.id());
        assertTrue(touched);
    }

    @Test
    public void test_comprehensive1() {
        FSFTBuffer buffer = new FSFTBuffer(4, 1);
        Bufferable t1 = new BufferableInteger(1);
        Bufferable t2 = new BufferableInteger(2);
        Bufferable t3 = new BufferableInteger(3);
        Bufferable t4 = new BufferableInteger(4);
        Bufferable t5 = new BufferableInteger(5);
        Bufferable t6 = new BufferableInteger(2);
        Bufferable t7 = new BufferableInteger(3);
        Bufferable t8 = new BufferableInteger(1);
        Bufferable t9 = new BufferableInteger(6);

        try {
            assertTrue(buffer.put(t2));
            assertFalse(buffer.put(t6));
            Thread.sleep(10);
            assertTrue(buffer.put(t1));
            Thread.sleep(10);
            assertTrue(buffer.put(t4));
            Thread.sleep(10);
            assertTrue(buffer.update(t6));
            assertEquals(buffer.get("2"), t6);
            assertFalse(buffer.put(t1));
            Thread.sleep(10);
            assertTrue(buffer.put(t3));
            Thread.sleep(10);
            assertTrue(buffer.put(t5));

            try {
                buffer.get("1");
                fail("Did not throw exception");
            } catch (IDNotFoundException idnfe) {
            }

            Thread.sleep(1000);

            for (int i = 0; i < 7; i++) {
                assertFalse(buffer.touch("" + i));

                try {
                    buffer.get("" + i);
                    fail("Did not throw exception");
                } catch (IDNotFoundException idnfe) {
                }
            }

            assertTrue(buffer.put(t1));
            Thread.sleep(10);
            assertTrue(buffer.put(t2));
            Thread.sleep(10);
            assertTrue(buffer.put(t3));
            Thread.sleep(10);
            assertTrue(buffer.put(t4));
            Thread.sleep(10);
            assertTrue(buffer.put(t5));
            Thread.sleep(10);
            assertFalse(buffer.put(t6));
            Thread.sleep(10);
            assertFalse(buffer.put(t7));
            Thread.sleep(10);
            assertTrue(buffer.update(t6));
            Thread.sleep(10);
            assertTrue(buffer.update(t7));
            Thread.sleep(10);
            assertFalse(buffer.update(t8));

            Thread.sleep(800);

            try {
                assertEquals(buffer.get("2"), t6);
                Thread.sleep(10);
                assertEquals(buffer.get("3"), t7);
                Thread.sleep(10);
                assertEquals(buffer.get("4"), t4);
                Thread.sleep(10);
                assertEquals(buffer.get("5"), t5);
            } catch (IDNotFoundException idnfe) {
                fail("Threw exception");
            }

            try {
                buffer.get("1");
                fail("Did not throw exception");
            } catch (IDNotFoundException idnfe) {
            }

            Thread.sleep(10);
            assertTrue(buffer.update(t6));
            assertTrue(buffer.put(t9));

            try {
                assertEquals(buffer.get("2"), t6);
                Thread.sleep(10);
                assertEquals(buffer.get("5"), t5);
                Thread.sleep(10);
                assertEquals(buffer.get("4"), t4);
                Thread.sleep(10);
                assertEquals(buffer.get("6"), t9);
            } catch (IDNotFoundException idnfe) {
                fail("Threw exception");
            }

            Thread.sleep(10);
            assertTrue(buffer.update(t2));
            Thread.sleep(10);
            assertTrue(buffer.put(t8));

            try {
                buffer.get("3");
                fail("Did not throw exception");
            } catch (IDNotFoundException idnfe) {
            }

            try {
                buffer.get("5");
                fail("Did not throw exception");
            } catch (IDNotFoundException idnfe) {
            }

        } catch (Exception e) {
            fail("Threw exception");
        }
    }

    @Test
    public void test_Concurrency1() throws InterruptedException {
        FSFTBuffer buffer = new FSFTBuffer(5, 1);
        Task2Test testBuffer = new Task2Test(buffer);

        Thread testThread1 = new Thread(testBuffer);
        Thread testThread2 = new Thread(testBuffer);
        Thread testThread3 = new Thread(testBuffer);

        testThread1.start();
        testThread2.start();
        testThread3.start();
        testThread1.join();
        testThread2.join();
        testThread3.join();
    }

    @Test
    public void test_Concurrency2() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            test_Concurrency1();
        }
    }

    @Test
    public void test_Concurrency3() throws InterruptedException {
        FSFTBuffer buffer = new FSFTBuffer(3, 1);
        Task2Test testBuffer = new Task2Test(buffer);

        Thread testThread1 = new Thread(testBuffer);
        Thread testThread2 = new Thread(testBuffer);
        Thread testThread3 = new Thread(testBuffer);

        testThread1.start();
        testThread2.start();
        testThread3.start();
        testThread1.join();
        testThread2.join();
        testThread3.join();
    }

    @Test
    public void test_Concurrency4() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            test_Concurrency3();
        }
    }

    @Test
    public void test_search() {
        WikiMediator wikiMediator = new WikiMediator();

        assertTrue(wikiMediator.search("Barak Obama", 4).size() == 4);
        assertTrue(wikiMediator.search("Donald Trump", 6).size() == 6);
        assertTrue(wikiMediator.search("Melania Trump", 2).size() == 2);
        assertTrue(wikiMediator.search("Star Wars", 8).size() == 8);
        assertTrue(wikiMediator.search("Sathish Gopalakrishnan", 1).size() == 1);
    }

    @Test
    public void test_getPage() {
        WikiMediator wikiMediator = new WikiMediator();
        String santaText = "At Harvard, he served as head of the Inflammation, Immunity and " +
            "Transplantation Focus Group";

        assertTrue(wikiMediator.getPage("Star Wars").contains("Darth Vader"));
        assertEquals(wikiMediator.getPage("Sathish Gopalakrishnan").length(), 0);
        assertTrue(wikiMediator.getPage("Santa J. Ono").contains(santaText));
    }

    @Test
    public void test_zeitgeist() {
        WikiMediator wikiMediator = new WikiMediator();

        wikiMediator.getPage("Sathish");
        wikiMediator.getPage("Sathish");

        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 2);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 3);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 4);

        wikiMediator.search("Santa Ono", 3);
        wikiMediator.search("Santa Ono", 2);
        wikiMediator.search("Santa Ono", 1);

        assertEquals(List.of("Star Wars", "Santa Ono", "Sathish"), wikiMediator.zeitgeist(5));
        assertEquals(List.of("Star Wars", "Santa Ono", "Sathish"), wikiMediator.zeitgeist(3));
        assertEquals(List.of("Star Wars", "Santa Ono"), wikiMediator.zeitgeist(2));
    }

    @Test
    public void test_trending() {
        List<Boolean> passed = Collections.synchronizedList(new ArrayList<Boolean>());

        WikiMediator wikiMediator = new WikiMediator();

        wikiMediator.search("CPEN 221", 3);
        wikiMediator.search("CPEN 221", 2);
        wikiMediator.search("CPEN 221", 1);
        wikiMediator.getPage("CPEN 221");
        wikiMediator.getPage("CPEN 221");
        wikiMediator.getPage("CPEN 221");
        wikiMediator.getPage("CPEN 221");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Could not pause test execution");
        }

        wikiMediator.search("Santa Ono", 3);
        wikiMediator.search("Santa Ono", 2);
        wikiMediator.search("Santa Ono", 1);

        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 2);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 3);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 4);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Could not pause test execution");
        }

        wikiMediator.search("Donald Trump", 3);
        wikiMediator.search("Donald Trump", 2);
        wikiMediator.search("Donald Trump", 1);
        wikiMediator.search("Donald Trump", 5);
        wikiMediator.getPage("Donald Trump");
        wikiMediator.getPage("Donald Trump");
        wikiMediator.getPage("Donald Trump");
        wikiMediator.getPage("Donald Trump");

        wikiMediator.getPage("Sathish");
        wikiMediator.getPage("Sathish");

        assertEquals(
            List.of("Donald Trump", "CPEN 221", "Star Wars", "Santa Ono", "Sathish"),
            wikiMediator.trending(7));
        assertEquals(
            List.of("Donald Trump", "CPEN 221", "Star Wars", "Santa Ono", "Sathish"),
            wikiMediator.trending(5));
        assertEquals(List.of("Donald Trump", "CPEN 221", "Star Wars", "Santa Ono"),
            wikiMediator.trending(4));
        assertEquals(List.of("Donald Trump", "CPEN 221", "Star Wars"),
            wikiMediator.trending(3));
        assertEquals(List.of("Donald Trump", "CPEN 221"),
            wikiMediator.trending(2));
        assertEquals(List.of("Donald Trump"),
            wikiMediator.trending(1));

        for (Boolean b : passed) {
            assertTrue(b);
        }
    }

    @Test
    public void test_peakLoad30s() {
        List<Boolean> passed = Collections.synchronizedList(new ArrayList<Boolean>());

        Thread test1 = new Thread(new Runnable() {
            @Override
            public void run() {
                test_peakLoad30s_1(passed);
            }
        });

        Thread test2 = new Thread(new Runnable() {
            @Override
            public void run() {
                test_peakLoad30s_2(passed);
            }
        });

        Thread test3 = new Thread(new Runnable() {
            @Override
            public void run() {
                test_peakLoad30s_3(passed);
            }
        });

        Thread test4 = new Thread(new Runnable() {
            @Override
            public void run() {
                test_peakLoad30s_4(passed);
            }
        });

        test1.start();
        test2.start();
        test3.start();
        test4.start();

        try {
            test1.join();
            test2.join();
            test3.join();
            test4.join();
        } catch (InterruptedException ie) {
            throw new RuntimeException("Thread interrupted");
        }

        for (Boolean b : passed) {
            assertTrue(b);
        }
    }

    private void test_peakLoad30s_1(List<Boolean> passed) {
        WikiMediator wikiMediator = new WikiMediator();

        wikiMediator.getPage("Sathish");
        wikiMediator.getPage("Sathish");

        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 2);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 3);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 4);

        wikiMediator.search("Santa Ono", 3);
        wikiMediator.search("Santa Ono", 2);
        wikiMediator.search("Santa Ono", 1);

        passed.add(12 == wikiMediator.peakLoad30s());
    }

    private void test_peakLoad30s_2(List<Boolean> passed) {
        WikiMediator wikiMediator = new WikiMediator();

        wikiMediator.getPage("Sathish");
        wikiMediator.getPage("Sathish");

        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 2);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 3);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 4);

        wikiMediator.search("Santa Ono", 3);
        wikiMediator.search("Santa Ono", 2);
        wikiMediator.search("Santa Ono", 1);

        wikiMediator.zeitgeist(4);
        wikiMediator.zeitgeist(4);
        wikiMediator.zeitgeist(4);

        wikiMediator.trending(4);
        wikiMediator.trending(2);
        wikiMediator.trending(1);

        passed.add(18 == wikiMediator.peakLoad30s());
    }

    private void test_peakLoad30s_3(List<Boolean> passed) {
        WikiMediator wikiMediator = new WikiMediator();

        boolean initialLoadCorrect = wikiMediator.peakLoad30s() == 1;

        wikiMediator.getPage("Sathish");
        wikiMediator.getPage("Sathish");

        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 2);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 3);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 4);

        wikiMediator.search("Santa Ono", 3);
        wikiMediator.search("Santa Ono", 2);
        wikiMediator.search("Santa Ono", 1);

        try {
            Thread.sleep(24000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Could not pause test execution");
        }

        wikiMediator.zeitgeist(4);
        wikiMediator.zeitgeist(4);
        wikiMediator.zeitgeist(4);

        wikiMediator.trending(4);
        wikiMediator.trending(2);
        wikiMediator.trending(1);

        passed.add((19 == wikiMediator.peakLoad30s()) && initialLoadCorrect);
    }

    private void test_peakLoad30s_4(List<Boolean> passed) {
        WikiMediator wikiMediator = new WikiMediator();

        wikiMediator.getPage("Sathish");
        wikiMediator.getPage("Sathish");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Could not pause test execution");
        }

        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 2);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 3);
        wikiMediator.getPage("Star Wars");
        wikiMediator.search("Star Wars", 4);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Could not pause test execution");
        }

        wikiMediator.search("Santa Ono", 3);
        wikiMediator.search("Santa Ono", 2);
        wikiMediator.search("Santa Ono", 1);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Could not pause test execution");
        }

        wikiMediator.zeitgeist(4);
        wikiMediator.zeitgeist(4);
        wikiMediator.zeitgeist(4);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Could not pause test execution");
        }

        wikiMediator.trending(4);
        wikiMediator.trending(2);
        wikiMediator.trending(1);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Could not pause test execution");
        }

        passed.add(12 == wikiMediator.peakLoad30s());
    }

    Runnable serverTask = new Runnable() {
        @Override
        public void run() {
            WikiMediatorServer server = new WikiMediatorServer(WikiMediatorServer.SERVER_PORT, 1);
        }
    };


    @Test
    public void test_basic1() throws InterruptedException {
        File file = new File("local/wikiMediatorStats" + WikiMediatorServer.SERVER_PORT + ".txt");
        file.delete();

        Thread server = new Thread(serverTask);
        server.start();

        try {
            WikiMediatorClient wmc =
                new WikiMediatorClient("localhost", WikiMediatorServer.SERVER_PORT);

            List<String> jsonStrings = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            WikiMediatorInfo wmi;

            wmi = new WikiMediatorInfo("zero", "search", "Barack Obama", 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("one", "getPage", null, null, "Santa J. Ono", null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("two", "zeitgeist", null, 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("three", "trending", null, 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("four", "peakLoad30s", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("five", "search", "Google Chrome", 7, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("six", "stop", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            for (int i = 0; i < jsonStrings.size(); i++) {
                System.out.printf("\n sending %d: \n" + jsonStrings.get(i), i);
                wmc.sendRequest(jsonStrings.get(i));
            }

            for (int i = 0; i < jsonStrings.size(); i++) {
                String reply = wmc.getReply();
                wmi = gson.fromJson(reply, WikiMediatorInfo.class);

                if (wmi.getId().equals("one")) {
                    assertTrue(wmi.getResponse().contains(
                        "At Harvard, he served as head of the Inflammation, Immunity and " +
                            "Transplantation Focus Group"));
                    assertEquals("success", wmi.getStatus());
                }
                if (wmi.getId().equals("two") || wmi.getId().equals("three")) {
                    assertEquals(Arrays.asList("Barack Obama", "Santa J. Ono").toString(),
                        wmi.getResponse());
                }
                if (wmi.getId().equals("four")) {
                    assertEquals("5", wmi.getResponse());
                }
                if (wmi.getId().equals("five")) {
                    assertTrue(wmi.getResponse().contains("Google Chrome"));
                }
                if (wmi.getId().equals("six")) {
                    assertEquals("bye", wmi.getResponse());
                }
            }

            wmc.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        server.join();
    }

    @Test
    public void test_basic2() throws InterruptedException {
        File file = new File("local/wikiMediatorStats" + WikiMediatorServer.SERVER_PORT + ".txt");
        file.delete();

        Thread server = new Thread(serverTask);
        server.start();

        try {
            WikiMediatorClient wmc =
                new WikiMediatorClient("localhost", WikiMediatorServer.SERVER_PORT);

            List<String> jsonStrings = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            WikiMediatorInfo wmi;

            wmi = new WikiMediatorInfo("zero", "search", "Barack Obama", 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("one", "getPage", null, null, "Santa J. Ono", null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("two", "zeitgeist", null, 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("three", "trending", null, 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("four", "peakLoad30s", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("five", "search", "Google Chrome", 7, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("six", "stop", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            for (int i = 0; i < jsonStrings.size(); i++) {
                System.out.printf("\n sending %d: \n" + jsonStrings.get(i), i);
                wmc.sendRequest(jsonStrings.get(i));
            }

            for (int i = 0; i < jsonStrings.size(); i++) {
                String reply = wmc.getReply();
                wmi = gson.fromJson(reply, WikiMediatorInfo.class);

                if (wmi.getId().equals("one")) {
                    assertTrue(wmi.getResponse().contains(
                        "At Harvard, he served as head of the Inflammation, Immunity and " +
                            "Transplantation Focus Group"));
                    assertEquals("success", wmi.getStatus());
                }
                if (wmi.getId().equals("two") || wmi.getId().equals("three")) {
                    assertEquals(Arrays.asList("Barack Obama", "Santa J. Ono").toString(),
                        wmi.getResponse());
                }
                if (wmi.getId().equals("four")) {
                    assertEquals("5", wmi.getResponse());
                }
                if (wmi.getId().equals("five")) {
                    assertTrue(wmi.getResponse().contains("Google Chrome"));
                }
                if (wmi.getId().equals("six")) {
                    assertEquals("bye", wmi.getResponse());
                }
            }

            wmc.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        server.join();

        server = new Thread(serverTask);
        server.start();

        try {
            WikiMediatorClient wmc =
                new WikiMediatorClient("localhost", WikiMediatorServer.SERVER_PORT);

            List<String> jsonStrings = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            WikiMediatorInfo wmi;

            wmi = new WikiMediatorInfo("zero", "search", "Barack Obama", 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("one", "getPage", null, null, "Santa J. Ono", null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("two", "zeitgeist", null, 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("three", "trending", null, 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("four", "peakLoad30s", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("five", "search", "Google Chrome", 7, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("six", "stop", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            for (int i = 0; i < jsonStrings.size(); i++) {
                System.out.printf("\n sending %d: \n" + jsonStrings.get(i), i);
                wmc.sendRequest(jsonStrings.get(i));
            }

            for (int i = 0; i < jsonStrings.size(); i++) {
                String reply = wmc.getReply();
                wmi = gson.fromJson(reply, WikiMediatorInfo.class);

                if (wmi.getId().equals("one")) {
                    assertTrue(wmi.getResponse().contains(
                        "At Harvard, he served as head of the Inflammation, Immunity and " +
                            "Transplantation Focus Group"));
                    assertEquals("success", wmi.getStatus());
                }
                if (wmi.getId().equals("two") || wmi.getId().equals("three")) {
                    assertEquals(
                        Arrays.asList("Barack Obama", "Santa J. Ono", "Google Chrome").toString(),
                        wmi.getResponse());
                }
                if (wmi.getId().equals("four")) {
                    assertEquals("8", wmi.getResponse());
                }
                if (wmi.getId().equals("five")) {
                    assertTrue(wmi.getResponse().contains("Google Chrome"));
                }
                if (wmi.getId().equals("six")) {
                    assertEquals("bye", wmi.getResponse());
                }
            }

            wmc.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        server.join();
    }


    @Test
    public void test_timeout() throws InterruptedException {
        File file = new File("local/wikiMediatorStats" + WikiMediatorServer.SERVER_PORT + ".txt");
        file.delete();

        Thread server = new Thread(serverTask);
        server.start();

        try {
            WikiMediatorClient wmc =
                new WikiMediatorClient("localhost", WikiMediatorServer.SERVER_PORT);

            List<String> jsonStrings = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            WikiMediatorInfo wmi;

            wmi = new WikiMediatorInfo("0", "search", "Computer", 1000000, null, 1);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("1", "getPage", null, null,
                "List of later historians of the Crusades", null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("2", "stop", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            for (int i = 0; i < jsonStrings.size(); i++) {
                System.out.printf("\n sending %d: \n" + jsonStrings.get(i), i);
                wmc.sendRequest(jsonStrings.get(i));
            }

            for (int i = 0; i < jsonStrings.size(); i++) {
                String reply = wmc.getReply();
                wmi = gson.fromJson(reply, WikiMediatorInfo.class);

                if (wmi.getId().equals("0")) {
                    assertEquals("Operation timed out", wmi.getResponse());
                }
                if (wmi.getId().equals("1")) {
                    assertTrue(
                        wmi.getResponse().contains("list of later historians of the Crusades"));
                }
            }

            wmc.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        server.join();
    }

    @Test
    public void test_multipleClients() throws InterruptedException {
        File file = new File("local/wikiMediatorStats" + WikiMediatorServer.SERVER_PORT + ".txt");
        file.delete();

        Thread server = new Thread(serverTask);
        server.start();


        try {
            WikiMediatorClient wmc1 =
                new WikiMediatorClient("localhost", WikiMediatorServer.SERVER_PORT);
            WikiMediatorClient wmc2 =
                new WikiMediatorClient("localhost", WikiMediatorServer.SERVER_PORT);

            List<String> jsonStrings1 = new ArrayList<>();
            List<String> jsonStrings2 = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            WikiMediatorInfo wmi1, wmi2;

            wmi1 = new WikiMediatorInfo("0", "search", "Computer", 1000000, null, 1);
            jsonStrings1.add(gson.toJson(wmi1));

            wmi1 = new WikiMediatorInfo("1", "getPage", null, null,
                "Google Chrome", null);
            jsonStrings1.add(gson.toJson(wmi1));

            wmi1 = new WikiMediatorInfo("2", "stop", null, null, null, null);
            jsonStrings1.add(gson.toJson(wmi1));

            wmi2 = new WikiMediatorInfo("5", "search", "Barack Obama", 5, null, null);
            jsonStrings2.add(gson.toJson(wmi2));

            wmi2 = new WikiMediatorInfo("4", "getPage", null, null, "Santa J. Ono", null);
            jsonStrings2.add(gson.toJson(wmi2));

            for (int i = 0; i < 3; i++) {
                wmc1.sendRequest(jsonStrings1.get(i));
                if (i < 2) {
                    wmc2.sendRequest(jsonStrings2.get(i));
                }
            }

            for (int i = 0; i < 2; i++) {
                String reply1 = wmc1.getReply();
                String reply2 = wmc2.getReply();
                wmi1 = gson.fromJson(reply1, WikiMediatorInfo.class);
                wmi2 = gson.fromJson(reply2, WikiMediatorInfo.class);

                if (wmi1.getId().equals("0")) {
                    assertEquals("Operation timed out", wmi1.getResponse());
                }
                if (wmi1.getId().equals("1")) {
                    assertTrue(wmi1.getResponse().contains("Google Chrome"));
                }

                if (wmi2.getId().equals("5")) {
                    assertTrue(wmi2.getResponse().contains("Barack Obama"));
                }
                if (wmi2.getId().equals("4")) {
                    assertTrue(wmi2.getResponse().contains(
                        "At Harvard, he served as head of the Inflammation, Immunity and " +
                            "Transplantation Focus Group"));
                }
            }

            wmc1.close();
            wmc2.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        server.join();
    }

    //fails to connect to server when running test with coverage
    @Test
    public void test_invalidType() throws InterruptedException {
        File file = new File("local/wikiMediatorStats" + WikiMediatorServer.SERVER_PORT + ".txt");
        file.delete();

        Thread server = new Thread(serverTask);
        server.start();

        try {
            WikiMediatorClient wmc =
                new WikiMediatorClient("localhost", WikiMediatorServer.SERVER_PORT);

            List<String> jsonStrings = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            WikiMediatorInfo wmi;

            wmi = new WikiMediatorInfo("zero", "searc", "Barack Obama", 5, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("six", "stop", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            for (int i = 0; i < jsonStrings.size(); i++) {
                System.out.printf("\n sending %d: \n" + jsonStrings.get(i), i);
                wmc.sendRequest(jsonStrings.get(i));
            }

            for (int i = 0; i < jsonStrings.size(); i++) {
                String reply = wmc.getReply();
                wmi = gson.fromJson(reply, WikiMediatorInfo.class);

                if (wmi.getId().equals("one")) {
                    assertTrue(wmi.getResponse().contains("Invalid Request"));
                    assertEquals("failed", wmi.getStatus());
                }

                if (wmi.getId().equals("six")) {
                    assertEquals("bye", wmi.getResponse());
                }
            }

            wmc.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        server.join();
    }

    @Test
    public void test_missingArguments() throws InterruptedException {
        File file = new File("local/wikiMediatorStats" + WikiMediatorServer.SERVER_PORT + ".txt");
        file.delete();

        Thread server = new Thread(serverTask);
        server.start();

        try {
            WikiMediatorClient wmc =
                new WikiMediatorClient("localhost", WikiMediatorServer.SERVER_PORT);

            List<String> jsonStrings = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            WikiMediatorInfo wmi;

            wmi = new WikiMediatorInfo("zero", "search", "Barack Obama", null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("7", "search", null, 1, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("8", "search", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("9", "search", "algdhsgadhjgdsagasdg", 1, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("one", "getPage", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("two", "zeitgeist", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("three", "trending", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            wmi = new WikiMediatorInfo("six", "stop", null, null, null, null);
            jsonStrings.add(gson.toJson(wmi));

            for (int i = 0; i < jsonStrings.size(); i++) {
                System.out.printf("\n sending %d: \n" + jsonStrings.get(i), i);
                wmc.sendRequest(jsonStrings.get(i));
            }

            for (int i = 0; i < jsonStrings.size(); i++) {
                String reply = wmc.getReply();
                wmi = gson.fromJson(reply, WikiMediatorInfo.class);

                if (wmi.getId().equals("zero") || wmi.getId().equals("one") ||
                    wmi.getId().equals("two") || wmi.getId().equals("three")) {
                    assertEquals("Missing or incorrect arguments", wmi.getResponse());
                }

                if (wmi.getId().equals("9")) {
                    assertEquals("[]", wmi.getResponse());
                }
            }

            wmc.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        server.join();
    }

}
