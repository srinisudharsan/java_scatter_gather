package com.srinisudharsan.scatterGather;

import java.util.List;
import java.util.function.Function;

/*
 * Builds a scatter gather class that uses ExecutorService for parallel execution of tasks.
 * @param <I> - Type of the input. TaskProcessor in the constructor is given to the scatter gather that takes I as input.
 * @param <O> - Type of the result of the task. TaskProcessor in the constructor is given to the scatter gather that returns O as result.
 * @param <G> - Type of the result of the gather. ResultAggregator in the constructor is given to the scatter gather that returns G as result.
 */
public class ExecutorScatterGatherBuilder<I, O, G> extends ScatterGatherBuilder<I, O, G, ExecutorScatterGatherBuilder<I, O, G>> {
    public ExecutorScatterGatherBuilder(Function<I, O> taskProcessor, Function<List<O>, G> resultAggregator) {
        super(taskProcessor, resultAggregator);
    }

    @Override
    protected ExecutorScatterGatherBuilder<I, O, G> self() {
        return this;
    }

    @Override
    public ScatterGather<I, O, G> build() {
        // Implement the logic to create a ScatterGather instance
        return new ExecutorScatterGather<I, O, G>(
            this.maxParallelization, this.taskProcessor, this.resultAggregator, this.timeout, this.timeUnit);
    }
}
