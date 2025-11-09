import java.util.*;

class FCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] bt = new int[n];
        int[] wt = new int[n];
        int[] tat = new int[n];
        int[] pid = new int[n];

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter Burst Time for Process " + pid[i] + ": ");
            bt[i] = sc.nextInt();
        }

        wt[0] = 0;
        for (int i = 1; i < n; i++) wt[i] = wt[i - 1] + bt[i - 1];
        for (int i = 0; i < n; i++) tat[i] = wt[i] + bt[i];

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
