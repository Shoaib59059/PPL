package Lab10;

// Write a Java program to create a task to find the sum of the elements of an array using divide-and-conquer approach, where each subtask is executed in Fork-Join Pool.

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ArraySum {

    static final int THRESHOLD = 10000; // The threshold for splitting the task

    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ForkJoinPool pool = new ForkJoinPool();
        int sum = pool.invoke(new SumTask(array, 0, array.length));
        System.out.println("Sum: " + sum);
    }

    static class SumTask extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start;
        private final int end;

        SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= THRESHOLD) { // if the task is small enough, compute the sum directly
                int sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else { // otherwise, split the task into two subtasks and execute them in parallel
                int mid = start + (end - start) / 2;
                SumTask leftTask = new SumTask(array, start, mid);
                SumTask rightTask = new SumTask(array, mid, end);
                leftTask.fork();
                int rightSum = rightTask.compute();
                int leftSum = leftTask.join();
                return leftSum + rightSum;
            }
        }
    }
}