package com.hjra;

import java.util.concurrent.PriorityBlockingQueue;

public class ConsumerBenefitCalculation implements Runnable {

    int newFeeTransfer;
    double deltaBalance;

    volatile Boolean isDone = false;
    int numSentinelMsg;
    java.util.concurrent.PriorityBlockingQueue<Account> queueBenefitCalculation = null;
    java.util.concurrent.PriorityBlockingQueue<Account> queueBonusCalculation = null;

    public ConsumerBenefitCalculation(PriorityBlockingQueue<Account> benefitQueue,
                                      PriorityBlockingQueue<Account> bonusQueue,
                                      int numSentinelMsg,
                                      int newFeeTransfer,
                                      double deltaBalance) {

        this.queueBenefitCalculation = benefitQueue;
        this.queueBonusCalculation = bonusQueue;
        this.numSentinelMsg = numSentinelMsg;
        this.newFeeTransfer = newFeeTransfer;
        this.deltaBalance = deltaBalance;
    }

    @Override
    public void run() {
        while (!isDone) {
            try {
                Account acc = queueBenefitCalculation.take();

                if (acc.getId() == Long.MAX_VALUE) {
                    for (int i = 0; i < this.numSentinelMsg; i++) {
                        queueBonusCalculation.put(acc);
                    }
                    synchronized (this) {
                        isDone = true;
                    }
                    continue;
                }

                /* Soal 2.
                 a. Jika balanced di antara 100-150 akan mengupdate free transfer menjadi 5
                (Jumlah Thread bebas dan pastikan No Thread yang di gunakan tertulis di ‘No 2a
                Thread-No’)
                b. Jika balanced di atas 150 akan mendapatkan tambahan balanced sebesar 25
                (Jumlah Thread bebas dan pastikan No Thread yang di gunakan tertulis di ‘No 2b
                Thread-No’)
                 */
                String threadNameID = Thread.currentThread().getName() + "-" + Thread.currentThread().getId();

                if (acc.getBalanced() >= 100 && acc.getBalanced() <= 150) {
                    acc.setFeeTransfer(this.newFeeTransfer);
                    acc.setNo2AThreadNo(threadNameID);
                } else if (acc.getBalanced() > 150) {
                    acc.setNo2BThreadNo(threadNameID);
                    acc.topupBalance(deltaBalance);
                }

                System.out.println("Benefit Consume " + acc);

                queueBonusCalculation.put(acc);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
