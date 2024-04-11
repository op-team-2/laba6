public class MultiThreading {
    public static void main(String[] args) {
        int n = 2;
        long N = 100_000_000L;

        long sumByFormula = sumByFormula(n, N);
        System.out.println("Сума за допомогою формули: " + sumByFormula);

        long sumByLoopSingleThread = sumByLoopSingleThread(n, N);
        System.out.println("Сума циклу в одному потоці: " + sumByLoopSingleThread);

        int[] threadCounts = {2, 4, 8, 16, 32};
        for (int k : threadCounts) {
            long sumByLoopMultipleThreads = sumByLoopMultipleThreads(n, N, k);
            System.out.println("Сума циклу в " + k + " потоках: " + sumByLoopMultipleThreads);
        }
    }

    public static long sumByFormula(int n, long N) {
        return (N / 2) * (2 * n + (N - 1) * n);
    }

    public static long sumByLoopSingleThread(int n, long N) {
        long sum = 0;
        for (long i = 1; i <= N; i++) {
            sum += i * n;
        }
        return sum;
    }

    public static long sumByLoopMultipleThreads(int n, long N, int k) {
        SumThread[] threads = new SumThread[k];
        long sum = 0;
        long chunkSize = N / k;

        try {
            for (int i = 0; i < k; i++) {
                long start = i * chunkSize + 1;
                long end = (i == k - 1) ? N : (i + 1) * chunkSize;
                threads[i] = new SumThread(n, start, end);
                threads[i].start();
            }

            for (int i = 0; i < k; i++) {
                threads[i].join();
                sum += threads[i].getSum();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return sum;
    }
}

class SumThread extends Thread {
    private int n;
    private long start;
    private long end;
    private long sum;

    public SumThread(int n, long start, long end) {
        this.n = n;
        this.start = start;
        this.end = end;
        this.sum = 0;
    }

    @Override
    public void run() {
        for (long i = start; i <= end; i++) {
            sum += i * n;
        }
    }

    public long getSum() {
        return sum;
    }
}
