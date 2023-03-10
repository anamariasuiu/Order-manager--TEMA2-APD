import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class Order{
    BufferedReader reader;
    BufferedWriter writer;
    String productsFile;
    String order;
    Integer nrProducts;
    ExecutorService executor;
    AtomicInteger inQueue = new AtomicInteger(0);
    public Order(ExecutorService executorP,String productsFile, String order, Integer nrProducts,
                 BufferedWriter writerP){
        executor = executorP;
        this.productsFile = productsFile;
        this.order = order;
        this.nrProducts = nrProducts;
        writer = writerP;
        try {
            reader = new BufferedReader(new FileReader(productsFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    };
    void submit(){
        inQueue.incrementAndGet();
        executor.submit(new Thread(order,nrProducts,executor,inQueue, reader, writer));
    }
}
class Thread implements Runnable{
    BufferedReader reader;
    BufferedWriter writer;
    String order;
    Integer nrProducts;
    ExecutorService executor;
    AtomicInteger inQueue;
    public Thread(String order, Integer nrProducts, ExecutorService executor,
            AtomicInteger inQueue, BufferedReader reader,BufferedWriter writer){
        this.order = order;
        this.nrProducts = nrProducts;
        this.executor = executor;
        this.inQueue = inQueue;
        this.reader = reader;
        this.writer = writer;
    };
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
            synchronized (this) {
                if (tokens[0].equals(order)) {
                    try {
                        writer.write(line + ",shipped\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
           }
            inQueue.incrementAndGet();
            executor.submit(new Thread(order,nrProducts,executor,inQueue,reader,writer));
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        int left = inQueue.decrementAndGet();
        if (left == 0) {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            executor.shutdown();
        }
    }
}
