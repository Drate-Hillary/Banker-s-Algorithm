import java.util.Scanner;

public class BankersAlgorithm {
    private int numProcesses, numResources; // Number of processes and resource types
    private int[][] maximum; // Maximum demand for each process
    private int[][] allocation; // Resources currently allocated to each process
    private int[][] need; // Remaining resources needed for each process
    private int[] available; // Currently available resources

    // Constructor to initialize the Banker's algorithm data structures
    BankersAlgorithm(int numProcesses, int numResources, int[][] maximum, int[][] allocation, int[] available) {
        this.numProcesses = numProcesses; // Set number of processes
        this.numResources = numResources; // Set number of resource types
        this.maximum = maximum; // Set maximum resource matrix
        this.allocation = allocation; // Set allocation matrix
        this.available = available; // Set available resources
        this.need = new int[numProcesses][numResources]; // Initialize need matrix

        // Calculate Need Matrix: Need[i][j] = Maximum[i][j] - Allocation[i][j]
        for (int i = 0; i < this.numProcesses; i++) {
            for (int j = 0; j < this.numResources; j++) {
                this.need[i][j] = this.maximum[i][j] - this.allocation[i][j];
            }
        }
    }

    // Check if the system is in a safe state using Banker's algorithm
    public boolean isSafe() {
        boolean[] finished = new boolean[this.numProcesses]; // Tracks which processes have completed
        int[] work = available.clone(); // Working copy of available resources
        int[] safeSequence = new int[this.numProcesses]; // Stores the safe sequence of processes
        int count = 0; // Counter for number of processes in safe sequence

        // Continue until all processes are in the safe sequence
        while (count < this.numProcesses) {
            boolean found = false; // Flag to check if we found a process that can run

            // Check each process
            for (int i = 0; i < this.numProcesses; i++) {
                if (!finished[i]) { // If process hasn't finished yet
                    boolean canAllocate = true; // Flag to check if resources can be allocated

                    // Check if current need can be satisfied with available resources
                    for (int j = 0; j < this.numResources; j++) {
                        if (this.need[i][j] > work[j]) { // If need exceeds available
                            canAllocate = false;
                            break;
                        }
                    }

                    // If resources can be allocated to this process
                    if (canAllocate) {
                        // Simulate resource allocation by adding allocated resources back
                        for (int j = 0; j < this.numResources; j++) {
                            work[j] += this.allocation[i][j];
                        }

                        safeSequence[count++] = i; // Add process to safe sequence
                        finished[i] = true; // Mark process as finished
                        found = true; // Indicate we found a process that can run
                    }
                }
            }

            // If no process was found that can run, system is unsafe
            if (!found) {
                System.out.println("System is in an unsafe state.");
                return false;
            }
        }

        // If we reach here, system is safe - print the safe sequence
        System.out.println("System is in a safe state. Safe sequence: ");
        for (int i = 0; i < this.numProcesses; i++) {
            System.out.print("P" + safeSequence[i] + " "); // Print process numbers
        }
        System.out.println();
        // Print final Work array (available resources after safe execution)
        System.out.println("Final Available Resources: ");
        for (int j = 0; j < this.numResources; j++) {
            System.out.print(work[j] + " ");
        }

        System.out.println();
        return true;
    }

    // Main method to run the program and get user input
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Get number of processes and resources
            System.out.print("Enter number of processes: ");
            int numProcesses = scanner.nextInt();
            System.out.print("Enter number of resources: ");
            int numResources = scanner.nextInt();

            // Initialize matrices and arrays
            int[][] maximum = new int[numProcesses][numResources];
            int[][] allocation = new int[numProcesses][numResources];
            int[] available = new int[numResources];

            // Input maximum resource matrix
            System.out.println("Enter the Maximum resource matrix:");
            for (int i = 0; i < numProcesses; i++) {
                for (int j = 0; j < numResources; j++) {
                    maximum[i][j] = scanner.nextInt(); // Read maximum needs
                }
            }

            // Input allocation matrix
            System.out.println("Enter the Allocation matrix:");
            for (int i = 0; i < numProcesses; i++) {
                for (int j = 0; j < numResources; j++) {
                    allocation[i][j] = scanner.nextInt(); // Read current allocations
                }
            }

            // Input available resources
            System.out.println("Enter the Available resources:");
            for (int i = 0; i < numResources; i++) {
                available[i] = scanner.nextInt(); // Read available resources
            }

            // Create Bankers object and check if system is safe
            BankersAlgorithm bankers = new BankersAlgorithm(numProcesses, numResources, maximum, allocation, available);
            bankers.isSafe();
        }
    }
}

/*
        Output
    ----------------
    Enter number of processes: 5
    Enter number of resources: 3
    Enter the Maximum resource matrix:
    7 5 3
    3 2 2
    9 0 2
    2 2 2
    4 3 3
    Enter the Allocation matrix:
    0 1 0
    2 0 0
    3 0 2
    2 1 1
    0 0 2
    Enter the Available resources:
    3 3 2
    System is in a safe state. Safe sequence: 
    P1 P3 P4 P0 P2 
    Final Available Resources:
    10 5 7
*/ 