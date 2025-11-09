import java.util.*;

class OptimalFIFO{
    public static int predict(int[] pages, ArrayList<Integer> frame, int index) {
        int res = -1, farthest = index;
        for (int i = 0; i < frame.size(); i++) {
            int j;
            for (j = index; j < pages.length; j++) {
                if (frame.get(i) == pages[j]) {
                    if (j > farthest) {
                        farthest = j;
                        res = i;
                    }
                    break;
                }
            }
            if (j == pages.length) return i;
        }
        return (res == -1) ? 0 : res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter length of reference string: ");
        int n = sc.nextInt();

        int[] pages = new int[n];
        System.out.println("Enter reference string:");
        for (int i = 0; i < n; i++) pages[i] = sc.nextInt();

        ArrayList<Integer> frame = new ArrayList<>();
        int faults = 0;

        System.out.println("\nRef String\tFrames");
        for (int i = 0; i < n; i++) {
            if (frame.contains(pages[i])) {
                System.out.print(pages[i] + "\t\t" + frame + "\n");
                continue;
            }

            if (frame.size() < frames) {
                frame.add(pages[i]);
            } else {
                int j = predict(pages, frame, i + 1);
                frame.set(j, pages[i]);
            }

            faults++;
            System.out.print(pages[i] + "\t\t" + frame + "\n");
        }

        System.out.println("\nTotal Page Faults: " + faults);
        System.out.printf("Page Fault Rate: %.2f%%\n", (faults * 100.0 / n));
    }
}
