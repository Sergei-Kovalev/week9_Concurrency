package kovalev.jdev.transaction;

import kovalev.jdev.server.Server;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Request implements Runnable {

    private TransactionData transactionData;

    private Server server;

    @Override
    public void run() {
        server.fillList(transactionData);
    }
}
