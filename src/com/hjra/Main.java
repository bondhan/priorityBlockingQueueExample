package com.hjra;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        final String FILENAME_INPUT = "before_eod.csv";
        final String FILENAME_OUTPUT = "after_eod.csv";
        final String CSV_SEPARATOR = ";";
        final int CSV_ARR_LEN = 7;
        final int NUM_AVERAGE_THREAD = 4; //maximum eight and even
        final int NUM_BENEFIT_THREAD = NUM_AVERAGE_THREAD; //maximum eight and even
        final int NUM_BONUS_THREAD = 8; //must be eight
        final double TOTAL_BONUS = 1000;
        final double INCREMENT_BONUS = 10;
        final int NEW_FEE_TRANSFER_BENEFIT = 5;
        final double INCREMENT_BALANCE_BENEFIT = 25;

        PriorityBlockingQueue<Account> pbqAverage = new PriorityBlockingQueue<>();
        PriorityBlockingQueue<Account> pbqBenefit = new PriorityBlockingQueue<>();
        PriorityBlockingQueue<Account> pbqBonus = new PriorityBlockingQueue<>();
        PriorityBlockingQueue<Account> pbqWriteFile = new PriorityBlockingQueue<>();

        // create the executor service with fix thread pool with the total thread number
        ExecutorService executor = Executors.newFixedThreadPool(1 + NUM_AVERAGE_THREAD +
                NUM_BENEFIT_THREAD + NUM_BONUS_THREAD + 1);

        // Producer thread
        ProducerAccount pa = new ProducerAccount(pbqAverage, FILENAME_INPUT, CSV_SEPARATOR, CSV_ARR_LEN, NUM_AVERAGE_THREAD);
        executor.execute(pa);

        // Consumer thread, calculating the average
        int numSentinelMsg = NUM_BENEFIT_THREAD / NUM_AVERAGE_THREAD;
        for (int i = 0; i < NUM_AVERAGE_THREAD; i++) {
            ConsumerAverageCalculation cafc = new ConsumerAverageCalculation(pbqAverage, pbqBenefit, numSentinelMsg);
            executor.execute(cafc);
        }

        // Consumer thread, calculating the benefit
        numSentinelMsg = NUM_BONUS_THREAD / NUM_BENEFIT_THREAD;
        for (int i = 0; i < NUM_BENEFIT_THREAD; i++) {
            ConsumerBenefitCalculation cbfc = new ConsumerBenefitCalculation(pbqBenefit, pbqBonus, numSentinelMsg,
                    NEW_FEE_TRANSFER_BENEFIT, INCREMENT_BALANCE_BENEFIT);
            executor.execute(cbfc);
        }

        // Consumer thread, for calculating the bonus, predefined is 8
        for (int i = 0; i < NUM_BONUS_THREAD; i++) {
            ConsumerBonusCalculation cbc = new ConsumerBonusCalculation(pbqBonus, pbqWriteFile, TOTAL_BONUS, INCREMENT_BONUS);
            executor.execute(cbc);
        }

        // A thread for writing to csv file
        ConsumerWriteToFile cwtf = new ConsumerWriteToFile(pbqWriteFile, FILENAME_OUTPUT);
        executor.execute(cwtf);

        // end of execution
        executor.shutdown();

        try {
            // next line will block till all tasks finishes
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            long totalExecutionTime = System.currentTimeMillis() - startTime;
            System.out.println("total execution time:" + totalExecutionTime + " ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}