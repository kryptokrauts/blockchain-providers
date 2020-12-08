package network.arkane.provider.infrastructure;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public final class Threading {

    public static void runInNewThreadPool(Runnable runnable) {
        runInNewThreadPool(null, runnable);
    }

    public static <T> T runInNewThreadPool(Callable<T> callable) {
        return runInNewThreadPool(null, callable);
    }

    public static void runInNewThreadPool(Integer maxParallelism,
                                          Runnable runnable) {
        ForkJoinPool threadPool = new ForkJoinPool();
        if (maxParallelism != null && threadPool.getParallelism() > maxParallelism) {
            threadPool = new ForkJoinPool(maxParallelism);
        }
        try {
            threadPool.submit(runnable).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T runInNewThreadPool(Integer maxParallelism,
                                           Callable<T> callable) {
        ForkJoinPool threadPool = new ForkJoinPool();
        if (maxParallelism != null && threadPool.getParallelism() > maxParallelism) {
            threadPool = new ForkJoinPool(maxParallelism);
        }
        try {
            T result = threadPool.submit(callable).get();
            threadPool.shutdown();
            return result;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
