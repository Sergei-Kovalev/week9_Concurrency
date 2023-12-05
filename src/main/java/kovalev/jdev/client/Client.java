package kovalev.jdev.client;

import kovalev.jdev.config.Config;
import kovalev.jdev.server.Server;
import kovalev.jdev.transaction.Request;
import kovalev.jdev.transaction.TransactionData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@NoArgsConstructor
public class Client implements Runnable {

    @Getter
    private final AtomicInteger accumulator = new AtomicInteger();

    @Getter
    private int n;

    private final int minDelay = Integer.parseInt(Config.getConfig().get("client").get("from"));
    private final int maxDelay = Integer.parseInt(Config.getConfig().get("client").get("to"));

    @Getter
    private List<Integer> numbersList;

    private Server server = new Server(this);

    @Getter
    private AtomicInteger counter;

    public Client(int n) {
        this.n = n;
        this.counter = new AtomicInteger();
        numbersList = fillNumbers(n);
    }

    public void remove() {
        TransactionData transactionData;
        try {
            int timeToSleep = (int) (Math.random() * (maxDelay - minDelay) + minDelay);
            System.out.println("The CLIENT sends the request with a delay = " + timeToSleep);
            Thread.sleep(timeToSleep);

            transactionData = getRandomNumberFromList();
            sendRequest(transactionData);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private TransactionData getRandomNumberFromList() {
        TransactionData transactionData;
        int randomIndex = (int) (Math.random() * numbersList.size());
        Integer numberFromList = numbersList.get(randomIndex);
        numbersList.remove(randomIndex);
        transactionData = new TransactionData(numberFromList);
        return transactionData;
    }

    private void sendRequest(TransactionData transactionData) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Request request = new Request(transactionData, server);
        executorService.execute(request);
        executorService.shutdown();
    }

    public void accumulate(TransactionData transactionData) {
        counter.incrementAndGet();
        accumulator.addAndGet(transactionData.getNumber());
    }

    private List<Integer> fillNumbers(int n) {
        List<Integer> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        return list;
    }

    @Override
    public void run() {
        while (!numbersList.isEmpty()) {
            remove();
        }
        while (counter.get() != n) {
        }
    }
}
