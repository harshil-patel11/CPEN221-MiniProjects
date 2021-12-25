package cpen221.mp3;

import cpen221.mp3.fsftbuffer.BufferableInteger;
import cpen221.mp3.fsftbuffer.FSFTBuffer;
import cpen221.mp3.fsftbuffer.IDNotFoundException;

import static org.junit.Assert.*;

public class Task2Test implements Runnable {
    FSFTBuffer<BufferableInteger> buffer;

    public Task2Test(FSFTBuffer<BufferableInteger> b) {
        buffer = b;
    }

    @Override
    public void run() {
        BufferableInteger t1 = new BufferableInteger(1);
        BufferableInteger t2 = new BufferableInteger(2);
        BufferableInteger t3 = new BufferableInteger(3);
        BufferableInteger t4 = new BufferableInteger(4);
        BufferableInteger t5 = new BufferableInteger(5);
        BufferableInteger t6 = new BufferableInteger(3);

        try {
            buffer.put(t1);
            Thread.sleep(1);
            buffer.put(t2);
            Thread.sleep(1);
            buffer.put(t3);
            Thread.sleep(1);
            buffer.put(t4);
            Thread.sleep(1);
            buffer.put(t5);

            buffer.touch("3");
            buffer.update(t6);
        } catch (InterruptedException ie) {
            fail("Threw an exception when it shouldn't have");
        }

        try {
            buffer.get("1");
            buffer.get("2");
            buffer.get("3");
            buffer.touch("4");
            buffer.get("4");
            buffer.get("5");
        } catch (IDNotFoundException idnfe) {
            buffer.update(t2);
        }
    }
}

