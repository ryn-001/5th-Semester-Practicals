import java.util.*;

class RoundRobin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] bt = new int[n];
        int[] rem_bt = new int[n];
        int[] wt = new int[n];
        int[] tat = new int[n];
        int[] pid = new int[n];

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter Burst Time for Process " + pid[i] + ": ");
            bt[i] = sc.nextInt();
            rem_bt[i] = bt[i];
        }

        System.out.print("Enter Time Quantum: ");
        int tq = sc.nextInt();

        int t = 0;
        while (true) {
            boolean done = true;
            for (int i = 0; i < n; i++) {
                if (rem_bt[i] > 0) {
                    done = false;
                    if (rem_bt[i] > tq) {
                        t += tq;
                        rem_bt[i] -= tq;
                    } else {
                        t += rem_bt[i];
                        wt[i] = t - bt[i];
                        rem_bt[i] = 0;
                    }
                }
            }
            if (done) break;
        }

        for (int i = 0; i < n; i++) tat[i] = bt[i] + wt[i];

        float avgWt = 0, avgTat = 0;
        System.out.println("\nProcess\tBT\tWT\tTAT");
        for (int i = 0; i < n; i++) {
            avgWt += wt[i];
            avgTat += tat[i];
            System.out.println(pid[i] + "\t" + bt[i] + "\t" + wt[i] + "\t" + tat[i]);
        }
        System.out.printf("\nAverage Waiting Time: %.2f", avgWt / n);
        System.out.printf("\nAverage Turnaround Time: %.2f", avgTat / n);
    }
}
