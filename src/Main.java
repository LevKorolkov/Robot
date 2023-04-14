import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {

        String[] robotRouts = new String[1000];

        for (int i = 0; i < robotRouts.length; i++) {
            robotRouts[i] = generateRoute("RLRFR", 100);
        }

        List<Thread> threads = new ArrayList<>();

        for (String rout : robotRouts) {
            Runnable robot = () -> {
                Integer rQuantity = 0;
                for (int i = 0; i < rout.length(); i++) {
                    if (rout.charAt(i) == 'R') {
                        rQuantity++;
                    }
                }
                System.out.println(rout.substring(0, 100) + " -> " + rQuantity);

                synchronized (rQuantity) {
                    if (sizeToFreq.containsKey(rQuantity)) {
                        sizeToFreq.put(rQuantity, sizeToFreq.get(rQuantity) + 1);
                    } else {
                        sizeToFreq.put(rQuantity, 1);
                    }
                }
            };
            Thread thread = new Thread(robot);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join(); // зависаем, ждём когда поток объект которого лежит в thread завершится
        }

        int maxValue = 0;
        Integer maxKey = 0;
        for (Integer key : sizeToFreq.keySet()) {
            if (sizeToFreq.get(key) > maxValue) {
                maxValue = sizeToFreq.get(key);
                maxKey = key;
            }
        }
        System.out.println();
        System.out.println("Самое частое количество повторений " + maxKey + " (встретилось " + maxValue + " раз)");
        sizeToFreq.remove(maxKey);

        System.out.println("Другие размеры:");
        for (Integer key : sizeToFreq.keySet()) {
            int value = sizeToFreq.get(key);
            System.out.println("- " + key + " (" + value + " раз)");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
