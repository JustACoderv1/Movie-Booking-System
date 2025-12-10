import java.util.*;

// ========================
// Payment system
// ========================

public class PaymentAppUPI {

    static final String RESET  = Color.RESET;
    static final String BLUE   = Color.BLUE;
    static final String PURPLE = Color.PURPLE;
    static final String CYAN   = Color.CYAN;
    static final String GREEN  = Color.GREEN;
    static final String RED    = Color.RED;
    static final String YELLOW = Color.YELLOW;

    public static boolean performPayment(double amount) throws InterruptedException {
        Scanner sc = Main.sc;

        while (true) {

            System.out.println("\n--------- Select Payment Method ---------\n");

            button(1, "Google Pay", BLUE);
            button(2, "PhonePe", PURPLE);
            button(3, "Paytm", CYAN);
            button(4, "Exit", YELLOW);

            System.out.print("\nEnter your choice: ");
            String input = sc.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println(RED + "Invalid Choice!" + RESET);
                continue;
            }

            int choice = Integer.parseInt(input);

            if (choice < 1 || choice > 4) {
                System.out.println(RED + "Invalid Choice!" + RESET);
                continue;
            }

            if (choice == 4) {
                System.out.println(YELLOW + "Payment Cancelled! Exiting..." + RESET);
                return false;
            }

            if (choice >= 1 && choice <= 3) {
                return pay(sc, choice, amount);
            }
        }
    }

    private static void button(int no, String name, String color) {
        System.out.println(color + "[ " + no + " ]  " + name + RESET);
    }

    // MODIFIED: Added HOME option during PIN entry
    private static boolean pay(Scanner sc, int choice, double amount) throws InterruptedException {

        String color = (choice == 1) ? BLUE : (choice == 2) ? PURPLE : CYAN;

        System.out.print("\nPayment Amount: Rs.");
        System.out.println(amount);

        int attempts = 3;
        boolean success = false;

        while (attempts > 0) {
            // UPDATED: Added prompt for Home option
            System.out.println(YELLOW + "Enter UPI PIN (or type 'HOME'): " + RESET);
            System.out.print("Enter PIN/Choice: ");
            String input = sc.nextLine().trim();

            // Handle 'HOME' option
            if (input.equalsIgnoreCase("HOME")) {
                System.out.println(YELLOW + "" + RESET);
                return false;
            }

            // Handle UPI PIN
            if (input.equals("1234")) {
                success = true;
                break;
            } else {
                attempts--;
                if (attempts > 0)
                    System.out.println(RED + "Wrong PIN! Attempts left: " + attempts + RESET);
            }
        }

        if (!success) {
            System.out.println(RED + "Transaction Failed! Maximum attempts reached.\n" + RESET);
            return false;
        }

        // LOADING EFFECT
        System.out.print(GREEN + "\nProcessing");
        for (int i = 0; i < 12; i++) {
            Thread.sleep(250);
            System.out.print(".");
        }
        System.out.println(RESET);

        System.out.println(color + "Payment Successful! Paid Rs." + amount + RESET + "\n");

        return true;
    }
}