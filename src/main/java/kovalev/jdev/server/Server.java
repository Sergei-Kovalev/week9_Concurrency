package kovalev.jdev.server;

import kovalev.jdev.client.Client;
import kovalev.jdev.config.Config;
import kovalev.jdev.transaction.Response;
import kovalev.jdev.transaction.TransactionData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@AllArgsConstructor
@NoArgsConstructor
public class Server implements Runnable {

    @Getter
    private final List<Integer> list = new CopyOnWriteArrayList<>();
    private final Lock lock = new ReentrantLock();

    private final int minDelay = Integer.parseInt(Config.getConfig().get("server").get("from"));
    private final int maxDelay = Integer.parseInt(Config.getConfig().get("server").get("to"));

    private Client client;

    private static Boolean stillRunning = true;

    public void fillList(TransactionData transactionData){
        lock.lock();
        try {
            sleeping();

            list.add(transactionData.getNumber());
            System.out.println(list);

            sendResponse();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void sleeping() throws InterruptedException {
        int timeToSleep = (int) (Math.random() * (maxDelay - minDelay) + minDelay);
        System.out.println("The SERVER sends a response with a delay = " + timeToSleep);
        Thread.sleep(timeToSleep);
    }

    public void sendResponse() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Response response = new Response(client, new TransactionData(list.size()));
        Future<Boolean> future = executorService.submit(response);
        stillRunning = future.get();
        executorService.shutdown();
    }

    @Override
    public void run() {
        while (stillRunning) {

        }
    }
}
