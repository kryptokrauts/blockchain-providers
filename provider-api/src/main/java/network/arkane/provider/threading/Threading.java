package network.arkane.provider.threading;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public final class Threading {

    private static final Map<String, ForkJoinPool> threadPools = new HashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> threadPools.forEach((k, v) -> {
            v.shutdownNow();
        })));
    }

    public static void runInThreadPool(String name,
                                       Runnable runnable) {
        runInThreadPool(name, null, runnable);
    }

    public static <T> T runInThreadPool(String name,
                                        Callable<T> callable) {
        return runInThreadPool(name, null, callable);
    }

    public static void runInThreadPool(String name,
                                       @Nullable Integer maxParallelism,
                                       Runnable runnable) {
        try {
            getPool(name, maxParallelism).submit(runnable).get();
        } catch (InterruptedException | ExecutionException e) {
            ExceptionUtils.rethrow(e);
        }
    }

    public static <T> T runInThreadPool(String name,
                                        @Nullable Integer maxParallelism,
                                        Callable<T> callable) {
        try {
            return getPool(name, maxParallelism).submit(callable).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static ForkJoinPool getPool(String name,
                                        @Nullable Integer maxParallelism) {
        if (!threadPools.containsKey(name)) {
            final ForkJoinPool.ForkJoinWorkerThreadFactory factory = pool -> {
                final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                worker.setName(name + "-" + worker.getPoolIndex());
                return worker;
            };
            ForkJoinPool threadPool = new ForkJoinPool(maxParallelism == null ? Runtime.getRuntime().availableProcessors() : maxParallelism,
                                                       factory,
                                                       null,
                                                       true);

            threadPools.put(name, threadPool);
        }
        return threadPools.get(name);
    }
}
