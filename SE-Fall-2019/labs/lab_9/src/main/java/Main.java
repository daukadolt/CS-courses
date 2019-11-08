import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.apache.commons.math3.primes.Primes;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);

        int rangeStart = 1, rangeEnd = 10000000;
        int step = 2500000;
        int numServices = rangeEnd/step;

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numServices);
        List<Future> futures = new ArrayList<>();
        for(int start = rangeStart, end = step; end<=rangeEnd; start+=step, end+=step) {
            Future future = service.submit(new myCallable(start, end, startLatch, doneLatch));
            futures.add(future);
        }

        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        doneLatch.await();
        long finishTime = System.currentTimeMillis();
        System.out.println(numServices + " threads done in " + (finishTime - startTime)/(1000d) + " seconds" );

        int numOfPrimes = 0;
        for(Future future: futures) {
            List<Boolean> range = (List<Boolean>) future.get();
            numOfPrimes += range.stream().filter((item) -> item).count();
        }

        CountDownLatch singleThreadLatch = new CountDownLatch(1);
        startLatch = new CountDownLatch(1);
        Future singleThreadFuture = service.submit(new myCallable(rangeStart, rangeEnd, startLatch, singleThreadLatch));

        startTime = System.currentTimeMillis();
        startLatch.countDown();
        singleThreadLatch.await();
        finishTime = System.currentTimeMillis();

        System.out.println("Single thread done in " + (finishTime - startTime)/(1000d) + " seconds" );

        System.out.println("There is a total of " + numOfPrimes + " primes between " + rangeStart + " and " + rangeEnd);
        service.shutdown();

    }
}

class myCallable implements Callable<List<Boolean>> {

    private int start, end;
    private CountDownLatch startLatch, doneLatch;

    myCallable(int start, int end, CountDownLatch startLatch, CountDownLatch doneLatch) {
        this.start = start;
        this.end = end;
        this.startLatch = startLatch;
        this.doneLatch = doneLatch;
    }

    public List<Boolean> call() throws InterruptedException {
        startLatch.await();
        List<Boolean> result = new ArrayList<>();
        for(int i = start; i<=end; i++) {
            result.add(Primes.isPrime(i));
        }
        doneLatch.countDown();
        return result;
    }
}