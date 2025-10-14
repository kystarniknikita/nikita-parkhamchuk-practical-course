import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<PartType> partsQueue = new ArrayBlockingQueue<>(10);
        Factory factory = new Factory(partsQueue);
        Faction factionWorld = new Faction("World", partsQueue);
        Faction factionWednesday = new Faction("Wednesday", partsQueue);

        Thread factoryThread = new Thread(factory::run);
        Thread worldThread = new Thread(factionWorld::run);
        Thread wednesdayThread = new Thread(factionWednesday::run);

        factoryThread.start();
        worldThread.start();
        wednesdayThread.start();

        factoryThread.join();
        worldThread.join();
        wednesdayThread.join();


        int worldRobots = factionWorld.buildRobots();
        int wednesdayRobots = factionWednesday.buildRobots();

        if (worldRobots > wednesdayRobots) {
            System.out.println("World faction built more robots: " + worldRobots);
        } else {
            System.out.println("Wednesday faction built more robots: " + wednesdayRobots);
        }
    }
}
