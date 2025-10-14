import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Factory implements Runnable {
    private final int PART_LIMIT = 10;
    private BlockingQueue<PartType> partsQueue;
    private Random random = new Random();

    public Factory(BlockingQueue<PartType> partsQueue) {
        this.partsQueue = partsQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            int producedParts = 0;
            while (producedParts != PART_LIMIT) {
                try {
                    partsQueue.put(PartType.values()[random.nextInt(PartType.values().length)]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                producedParts++;
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
