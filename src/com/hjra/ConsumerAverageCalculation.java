package com.hjra;

import java.util.concurrent.PriorityBlockingQueue;

public class ConsumerAverageCalculation implements Runnable {

    int newFeeTransfer;
    double deltaBalance;

    volatile Boolean isDone = false;
    int numSentinelMsg;
    java.util.concurrent.PriorityBlockingQueue<Account> queueAverageCalculation = null;
    java.util.concurrent.PriorityBlockingQueue<Account> queueBenefitCalculation = null;

    public ConsumerAverageCalculation(PriorityBlockingQueue<Account> averageQueue,
                                      PriorityBlockingQueue<Account> benefitQueue,
                                      int numSentinelMsg) {

        this.queueAverageCalculation = averageQueue;
        this.queueBenefitCalculation = benefitQueue;
        this.numSentinelMsg = numSentinelMsg;
    }

    @Override
    public void run() {
        while (!isDone) {
            try {
                Account acc = queueAverageCalculation.take();

                if (acc.getId() == Long.MAX_VALUE) {
                    for (int i = 0; i < this.numSentinelMsg; i++) {
                        queueBenefitCalculation.put(acc);
                    }
                    synchronized (this) {
                        isDone = true;
                    }
                    continue;
                }

                /* Soal 1.
                 Menghitung average:
                        averaged = (balanced + previous_balanced)/2
                 */
                String threadNameID = Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
                acc.calculateAverage();
                acc.setNo1ThreadNo(threadNameID);

                System.out.println("Average Consume " + acc);

                // put the result to benefitQueue for benefit calculation
                queueBenefitCalculation.put(acc);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
