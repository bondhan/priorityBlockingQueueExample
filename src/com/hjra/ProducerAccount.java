package com.hjra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;

public class ProducerAccount implements Runnable {

    PriorityBlockingQueue<Account> averageQueue = null;
    BufferedReader br = null;
    String csvSeparator = null;
    int csvDataNum;
    int numAverageThread;

    public ProducerAccount(PriorityBlockingQueue<Account> pbq,
                           String fileName,
                           String csvSeparator,
                           int csvDataNum,
                           int numAverageThread) {

        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.csvSeparator = csvSeparator;
        this.csvDataNum = csvDataNum;
        this.numAverageThread = numAverageThread;
        this.averageQueue = pbq;
    }

    /*
        read the csv file line by line and enqueue to average queue for calculation of average.
     */
    @Override
    public void run() {
        try {
            String line = br.readLine();
            Account acc = null;
            while ((line = br.readLine()) != null) {
                acc = new Account(line, csvSeparator, System.lineSeparator(), csvDataNum);

                // put to the queue
                this.averageQueue.put(acc);

                Thread.sleep(10);
            }

            //if end of file, put sentinel data to stop average thread
            Account lastAccount = new Account();
            lastAccount.setId(Long.MAX_VALUE);
            for (int i = 0; i < numAverageThread; i++) {
                this.averageQueue.put(lastAccount);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
