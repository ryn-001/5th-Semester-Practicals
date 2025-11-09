import java.util.*;

class LRU {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter length of reference string: ");
        int n = sc.nextInt();

        int[] pages = new int[n];
        System.out.println("Enter reference string:");
        for (int i = 0; i < n; i++) pages[i] = sc.nextInt();

        ArrayList<Integer> memory = new ArrayList<>();
        int faults = 0;

        System.out.println("\nRef String\tFrames");
        for (int i = 0; i < n; i++) {
            if (!memory.contains(pages[i])) {
                if (memory.size() < frames) {
                    memory.add(pages[i]);
                } else {
                    memory.remove(0);
                    memory.add(pages[i]);
                }
                faults++;
            } else {
                memory.remove((Integer) pages[i]);
                memory.add(pages[i]);
            }
            System.out.print(pages[i] + "\t\t" + memory + "\n");
        }

        System.out.println("\nTotal Page Faults: " + faults);
        System.out.printf("Page Fault Rate: %.2f%%\n", (faults * 100.0 / n));
    }
}
