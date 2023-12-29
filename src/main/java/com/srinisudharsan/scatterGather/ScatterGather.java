package com.srinisudharsan.scatterGather;

import java.util.concurrent.ExecutionException;

/*
 * Interface for scatter gather
 * @param <I> - Type of the input. Typically a TaskProcessor is given to the scatter gather that takes I as input. See ExecutorScatterGatherBuilder for an example.
 * @param <O> - Type of the result of the task. Typically a TaskProcessor is given to the scatter gather that returns O as result. See ExecutorScatterGatherBuilder for an example.
 * @param <G> - Type of the result of the gather. Typically a ResultAggregator is given to the scatter gather that returns G as result. See ExecutorScatterGatherBuilder for an example.
 */
public interface ScatterGather<I, O, G> {
    void scatter(I input);
    G gather() throws InterruptedException, ExecutionException;
}
