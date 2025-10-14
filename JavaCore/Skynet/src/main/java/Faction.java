import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Faction implements Runnable {
    private String name;

    private BlockingQueue<PartType> partsQueue;

    private Map<PartType, Integer> partsStorage = new HashMap<>();

    private final int PARTS_LIMIT = 5;

    public Faction(String name, BlockingQueue<PartType> partsQueue) {
        this.name = name;
        this.partsQueue = partsQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {

            int takenParts = 0;

            while (takenParts != PARTS_LIMIT) {
                PartType part = partsQueue.poll();
                if (part != null) {
                    partsStorage.put(part, partsStorage.getOrDefault(part, 0) + 1);
                    takenParts++;
                } else {
                    break;
                }
                takenParts++;
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int buildRobots() {
        boolean canBuild = true;
        int builtRobots = 0;

        while (canBuild) {
            if (hasPartsForRobot()) {
                partsStorage.put(PartType.HEAD, partsStorage.get(PartType.HEAD) - 1);
                partsStorage.put(PartType.TORSO, partsStorage.get(PartType.TORSO) - 1);
                partsStorage.put(PartType.HAND, partsStorage.get(PartType.HAND) - 2);
                partsStorage.put(PartType.FEET, partsStorage.get(PartType.FEET) - 2);
                builtRobots++;
            } else {
                canBuild = false;
            }
        }

        return builtRobots;
    }

    private boolean hasPartsForRobot() {
        return partsStorage.getOrDefault(PartType.HEAD, 0)  >= 1 &&
                partsStorage.getOrDefault(PartType.TORSO, 0) >= 1 &&
                partsStorage.getOrDefault(PartType.HAND, 0)  >= 2 &&
                partsStorage.getOrDefault(PartType.FEET, 0)  >= 2;
    }
}
