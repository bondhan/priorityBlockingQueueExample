package com.hjra;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;

public class ConsumerWriteToFile implements Runnable {

    volatile Boolean isDone = false;
    java.util.concurrent.PriorityBlockingQueue<Account> queueWriter = null;
    BufferedWriter bw = null;
    Account lastData = null;

    public ConsumerWriteToFile(PriorityBlockingQueue<Account> writeQueue,
                               String fileName) {

        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            bw.write("id;Nama;Age;Balanced;No 2b Thread-No;No 3 Thread-No;Previous Balanced;Average Balanced;No 1 Thread-No;Free Transfer;No 2a Thread-No\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.lastData = lastData;
        this.isDone = isDone;
        this.queueWriter = writeQueue;
    }

    @Override
    public void run() {
        while (!isDone) {
            try {
                Account acc = queueWriter.take();

                if (acc.getId() == Long.MAX_VALUE) {
                    synchronized (this) {
                        isDone = true;
                    }
                    continue;
                }

                System.out.println("Write" + acc);

                try {
                    if (bw != null) {
                        bw.write(acc.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
