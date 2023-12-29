package com.srinisudharsan.scatterGather;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.ArrayList;

/*
 * A scatter-gather implementation that uses an ExecutorService to execute the tasks.
 * @param <I> - Type of the input. TaskProcessor in the constructor is given to the scatter gather that takes I as input.
 * @param <O> - Type of the result of the task. TaskProcessor in the constructor is given to the scatter gather that returns O as result.
 * @param <G> - Type of the result of the gather. ResultAggregator in the constructor is given to the scatter gather that returns G as result.
 */
class ExecutorScatterGather<I, O, G> implements ScatterGather<I, O, G> {
    private final ExecutorService executorService;
    private final CompletionService<O> completionService;
    private final List<Future<O>> futures = new ArrayList<>();
    private final Function<I, O> taskProcessor;
    private final Function<List<O>, G> resultAggregator;
    private final long timeout;
    private final TimeUnit timeUnit;
    private boolean isScatterDone = false;
    private final Object lock = new Object();

    public ExecutorScatterGather(int threadPoolSize, Function<I, O> taskProcessor, Function<List<O>, G> resultAggregator, long timeout, TimeUnit timeUnit) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.completionService = new ExecutorCompletionService<>(executorService);
        this.taskProcessor = taskProcessor;
        this.resultAggregator = resultAggregator;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public void scatter(I input) {
        synchronized (lock) {
            if (isScatterDone) {
                throw new IllegalStateException("Cannot submit tasks after gather has been called.");
            }
            futures.add(completionService.submit(() -> taskProcessor.apply(input)));
        }
    }

    public G gather() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            if (isScatterDone) {
                throw new IllegalStateException("Gather has already been called.");
            }
            isScatterDone = true;
        }

        List<O> results = new ArrayList<>();
        for (Future<O> future : this.futures) {
            results.add(completionService.take().get());
        }

        shutdownAndAwaitTermination(executorService);

        return resultAggregator.apply(results);
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); 
        try {
            if (!pool.awaitTermination(timeout, timeUnit)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(timeout, timeUnit))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
