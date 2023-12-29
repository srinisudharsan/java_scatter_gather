package com.srinisudharsan.scatterGather;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/*
 * Abstract builder for scatter gather. Added for extensiblity to add more implementations of scatter gather.
 */
public abstract class ScatterGatherBuilder<T, S, G, B extends ScatterGatherBuilder<T, S, G, B>> {
    protected int maxParallelization = Runtime.getRuntime().availableProcessors();
    protected long timeout = 60;
    protected TimeUnit timeUnit = TimeUnit.SECONDS;
    protected final Function<T, S> taskProcessor;
    protected final Function<List<S>, G> resultAggregator;

    /*
     * @param taskProcessor - Function that processes a task and returns a result
     * @param resultAggregator - Function that aggregates the results of all the tasks
     */
    protected ScatterGatherBuilder(Function<T, S> taskProcessor, Function<List<S>, G> resultAggregator) {
        if (taskProcessor == null || resultAggregator == null) {
            throw new IllegalArgumentException("Task processor and result aggregator cannot be null");
        }
        this.taskProcessor = taskProcessor;
        this.resultAggregator = resultAggregator;
    }

    /*
     * @param maxParallelization - Maximum number of tasks that can be executed in parallel
     */
    public B setMaxParallelization(int maxParallelization) {
        this.maxParallelization = maxParallelization;
        return self();
    }

    /*
     * @param timeout - Maximum time to wait for the tasks to complete
     * @param timeUnit - TimeUnit for the timeout
     */
    public B setTimeout(long timeout, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        return self();
    }

    protected abstract B self();

    /*
     * Builds a ScatterGather instance
     */
    public abstract ScatterGather<T, S, G> build();
}
