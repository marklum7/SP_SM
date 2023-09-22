
import java.util.Random;

public class Main {
    static int[] inputArray;
    static int[] resultArray;

    static void calculateSequential(int start, int end) {
        for (int i = start; i < end; i++) {
            for (int j = 0; j <= i; j++) {
                resultArray[i] += inputArray[i] * 2;
            }
        }
    }

    static void calculateConcurrent(int threadIndex, int numThreads) {
        for (int i = threadIndex; i < resultArray.length; i += numThreads) {
            for (int j = 0; j <= i; j++) {
                resultArray[i] += inputArray[i] * 2;
            }
        }
    }

    static String singleTest(int N) {
        inputArray = new int[N];
        resultArray = new int[N];
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            inputArray[i] = rand.nextInt(10);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            calculateSequential(0, N);
        }
        long endTime = System.currentTimeMillis();
        double timeInSeconds = (endTime - startTime) / 1000.0;
        return String.format("%d элементов | Один поток | %.3f секунд", N, timeInSeconds);
    }

    static String multiTest(int N, int numThreads) {
        int M = numThreads;
        inputArray = new int[N];
        resultArray = new int[N];
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            inputArray[i] = rand.nextInt(10);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Thread[] threadArray = new Thread[M];
            for (int j = 0; j < M; j++) {
                int finalJ = j;
                threadArray[j] = new Thread(() -> calculateConcurrent(finalJ, M));
                threadArray[j].start();
            }
            for (int j = 0; j < M; j++) {
                try {
                    threadArray[j].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        long endTime = System.currentTimeMillis();
        double timeInSeconds = (endTime - startTime) / 1000.0;
        return String.format("%d элементов | %d потоков | %.3f секунд", N, M, timeInSeconds);
    }

    public static void main(String[] args) {
        int[] vectorSizes = {10, 100, 100000};

        System.out.println("Количество элементов | Тест ");

        for (int N : vectorSizes) {
            System.out.println(singleTest(N));
            for (int numThreads = 2; numThreads <= 10; numThreads++) {
                System.out.println(multiTest(N, numThreads));
            }
            System.out.println("________________________________________________________");
        }
    }
}
