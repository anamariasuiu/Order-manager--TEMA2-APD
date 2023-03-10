import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Tema2 {
    public static void main(String[] args) {
        File director = new File(args[0]);
        File[] inputs = director.listFiles();
        FileReader fr = null;
        try {
            fr = new FileReader(args[0] + "/orders.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        BufferedWriter writerP = null, writerO = null;
        Integer P = Integer.parseInt(args[1]);
        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(P);
        ExecutorService executorP = Executors.newFixedThreadPool(P);
        String products = args[0] + "/order_products.txt";
        File productsOut = new File("order_products_out.txt");
        File ordersOut = new File("orders_out.txt");
        try {
            ordersOut.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            productsOut.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writerP = new BufferedWriter(new FileWriter(productsOut));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writerO = new BufferedWriter(new FileWriter(ordersOut));
        } catch (IOException e) {
            e.printStackTrace();
        }
        inQueue.incrementAndGet();
        executor.submit(new MyRunnable(reader, writerP, writerO, executor, executorP, inQueue,products,
                args[0] + "/orders.txt"));
    }
}

class MyRunnable implements Runnable {
    ExecutorService executor;
    ExecutorService executorP;
    AtomicInteger inQueue;
    BufferedReader reader, readerO;
    BufferedWriter writerO;
    BufferedWriter writerP;
    String products;
    String OrdersFile;
    public MyRunnable(BufferedReader reader, BufferedWriter writerP, BufferedWriter writerO, ExecutorService executor,
                      ExecutorService executorP, AtomicInteger inQueue, String products, String OrdersFile)  {
        this.writerO = writerO;
        this.writerP = writerP;
        this.executor = executor;
        this.executorP = executorP;
        this.inQueue = inQueue;
        this.products = products;
        this.reader = reader;
        this.OrdersFile = OrdersFile;
        try {
            readerO = new BufferedReader(new FileReader(OrdersFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line = "";
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(line != null) {
            String[] tokens = line.split(",");
            inQueue.incrementAndGet();
            executor.submit(new MyRunnable(reader,writerP, writerO, executor, executorP, inQueue,products, OrdersFile));
            Integer nrP =   Integer.parseInt(tokens[1]);
            if(nrP != 0) {
                Order order = new Order(executorP, products, tokens[0], nrP, writerP);
                order.submit();
            }
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int left = inQueue.decrementAndGet();
        if (left == 0) {
            String oneLine ="";
            System.out.println(oneLine);
            try {
                oneLine = readerO.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(oneLine != null){
                String[] tokens = oneLine.split(",");
                if(!tokens[1].equals("0")) {
                    try {
                        writerO.write(oneLine + ",shipped");
                        writerO.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    oneLine = readerO.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                writerO.close();
                readerO.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            executor.shutdown();
        }
    }
}