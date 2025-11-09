import java.util.*;

class FIFO {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter length of reference string: ");
        int n = sc.nextInt();

        int[] pages = new int[n];
        System.out.println("Enter reference string:");
        for (int i = 0; i < n; i++) pages[i] = sc.nextInt();

        Set<Integer> s = new HashSet<>(frames);
        Queue<Integer> q = new LinkedList<>();
        int faults = 0;

        System.out.println("\nRef String\tFrames");
        for (int i = 0; i < n; i++) {
            if (!s.contains(pages[i])) {
                if (s.size() < frames) {
                    s.add(pages[i]);
                    q.add(pages[i]);
                } else {
                    int val = q.poll();
                    s.remove(val);
                    s.add(pages[i]);
                    q.add(pages[i]);
                }
                faults++;
            }

            System.out.print(pages[i] + "\t\t" + s + "\n");
        }

        System.out.println("\nTotal Page Faults: " + faults);
        System.out.printf("Page Fault Rate: %.2f%%\n", (faults * 100.0 / n));
    }
}
