import java.util.*;

// ========================
// User Login
// ========================

class Account {
    private String username;
    private String password;
    private String phoneNumber;

    Account(String user, String pass, String phone) {
        this.username = user;
        this.password = pass;
        this.phoneNumber = phone;
    }

    String getUser() { return username; }
    String getPass() { return password; }
    String getNum()  { return phoneNumber; }
    void setPass(String password) { this.password = password; }
}

public class Login {

    // STORED ACCOUNTS
    static ArrayList<Account> accounts = new ArrayList<>();

    // GLOBAL SCANNER (uses Main.sc in your project)
    static Scanner sc = Main.sc;

    // WRONG ATTEMPT COUNTER (shared by all login methods)
    static int[] wrongAttempts = {0};

    // COLORS
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String RESET = "\033[0m";

    // MAIN LOGIN MENU (CALL FROM Main.java)
    public static boolean startLogin() {

        // Default account
        if (accounts.isEmpty()) {
            accounts.add(new Account("ram", "Ramak123", "9391573109"));
        }

        while (true) {
            System.out.println();
            System.out.println(Header());

            System.out.print(CYAN + "Enter your choice: " + RESET);

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println(RED + "Invalid choice. Try again." + RESET);
                continue;
            }

            System.out.println();

            switch (choice) {
                case 1: if (userPassLogin()) return true;  break;
                case 2: if (phoneLogin()) return true;     break;
                case 3: register();                        break;
                case 4:
                    System.out.println(GREEN + "Exiting Login..." + RESET);
                    return false;

                default:
                    System.out.println(RED + "Invalid Choice." + RESET);
            }

            if (wrongAttempts[0] == 2) {
                System.out.println(RED + "WARNING: Only ONE attempt remaining!" + RESET);
            }

            if (wrongAttempts[0] >= 3) {
                System.out.println(RED + "Too many wrong attempts. Try later." + RESET);
                return false;
            }
        }
    }

    // HEADER MENU
    public static String Header() {
       // inside Header()
return YELLOW +
"+------------------------------------------------------------+\n" +
"|                                                            |\n" +
"| 1. Login using Username and Password                       |\n" +
"| 2. Login using Phone Number                                |\n" +
"| 3. Registration                                            |\n" +
"| 4. Exit                                                    |\n" +
"|                                                            |\n" +
"+------------------------------------------------------------+\n" +
RESET;


    }

    // REGISTER
    public static void register() {
        System.out.println(YELLOW_BOLD + "---------------------- REGISTER ----------------------" + RESET);

        System.out.print(CYAN + "Enter new username: " + RESET);
        String user = sc.nextLine();

        if (findAccount(user) != null) {
            System.out.println(RED + "Username already exists! Please Login instead." + RESET);
            return;
        }

        System.out.println(Color.YELLOW +
        "Password must have:\n" +
        "- Min 8 characters\n" +
        "- At least 1 Uppercase\n" +
        "- At least 1 Number\n" +
        Color.RESET);


        String pass;
        while (true) {
            System.out.print(CYAN + "Enter Password: " + RESET);
            pass = sc.nextLine();

            if (isValidPassword(pass)) break;
            System.out.println(RED + "Invalid password. Follow the rules above." + RESET);
        }

        String phone;
        while (true) {
            System.out.print(CYAN + "Enter Phone Number: " + RESET);
            phone = sc.nextLine();

            if (!phone.matches("[6-9]\\d{9}")) {
                System.out.println(RED + "Phone must be 10 digits and start with 6-9!" + RESET);
                continue;
            }

            if (findAccountByPhone(phone) != null) {
                System.out.println(RED + "Phone already registered!" + RESET);
                continue;
            }

            break;
        }

        accounts.add(new Account(user, pass, phone));
        System.out.println(GREEN + "Registration Successful!" + RESET);
    }

    // LOGIN SUCCESS
    public static void loginSuccess() {
        System.out.println(GREEN + "Login Successful!!!" + RESET);
        System.out.println(PURPLE + "Loading..." + RESET);
    }

    // USERNAME & PASSWORD LOGIN
    public static boolean userPassLogin() {
        while (true) {
            System.out.print(CYAN + "Enter username: " + RESET);
            String user = sc.nextLine();

            System.out.print(CYAN + "Enter password: " + RESET);
            String pass = sc.nextLine();

            Account acc = findAccount(user);

            if (acc != null && acc.getPass().equals(pass)) {
    Login.setCurrentUser(acc.getUser(), acc.getNum());  // store logged-in user
    loginSuccess();
    return true;
}


            System.out.println(RED + "\nIncorrect Username or Password." + RESET);
            wrongAttempts[0]++;
            if (wrongAttempts[0] >= 3) return false;

            System.out.println(YELLOW + "1. Try Again\n2. Forgot Password\n3. Return to Menu" + RESET);
            System.out.print(CYAN + "Enter choice: " + RESET);

            int ch;

            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println(RED + "Invalid choice. Try again." + RESET);
                continue;
            }

            switch (ch) {
                case 1: continue;
                case 2:
                    if (acc == null) {
                        System.out.println(RED + "Cannot reset password. Username incorrect." + RESET);
                        continue;
                    }
                    if (forgotPassword(acc)) {
                        System.out.println(YELLOW + "Login with NEW password." + RESET);
                    }
                    continue;

                case 3: return false;

                default:
                    System.out.println(RED + "Invalid choice." + RESET);
            }
        }
    }

    // FORGOT PASSWORD
    public static boolean forgotPassword(Account acc) {
        System.out.print(CYAN + "Re-enter Username: " + RESET);
        String user = sc.nextLine();

        if (!user.equals(acc.getUser())) {
            System.out.println(RED + "Wrong Username!" + RESET);
            return false;
        }

        System.out.println(GREEN + "Username Verified." + RESET);

        String newPass;
        while (true) {
            System.out.println(Color.YELLOW +
        "Password must contain:\n" +
        "- 8+ characters\n" +
        "- 1 Uppercase\n" +
        "- 1 Number\n" +
        Color.RESET);


            System.out.print(CYAN + "New Password: " + RESET);
            newPass = sc.nextLine();

            if (isValidPassword(newPass)) break;

            System.out.println(RED + "Invalid Password." + RESET);
        }

        acc.setPass(newPass);
        System.out.println(GREEN + "Password Reset Successful!" + RESET);
        return true;
    }

    // PHONE LOGIN
    public static boolean phoneLogin() {
        System.out.print(CYAN + "Enter Phone Number: " + RESET);
        String num = sc.nextLine();

        if (!num.matches("[6-9]\\d{9}")) {
            System.out.println(RED + "Invalid phone number!" + RESET);
            return false;
        }

        Account acc = findAccountByPhone(num);
        if (acc == null) {
            System.out.println(RED + "No account found for this phone!" + RESET);
            return false;
        }

        int otp = otpGen();
        System.out.println(GREEN + "OTP sent: " + PURPLE + otp + RESET);

        while (true) {
            System.out.print(CYAN + "Enter OTP: " + RESET);
            int entered;

            try {
                entered = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println(RED + "Invalid OTP format." + RESET);
                wrongAttempts[0]++;
                return false;
            }

            if (entered == otp) {
    System.out.println(GREEN + "OTP Verified!" + RESET);
    Login.setCurrentUser(acc.getUser(), acc.getNum());  // store logged-in user
    loginSuccess();
    return true;
}


            System.out.println(RED + "Incorrect OTP." + RESET);
            wrongAttempts[0]++;
            if (wrongAttempts[0] >= 3) return false;

            System.out.println(YELLOW + "1. Try Again\n2. Resend OTP\n3. Return to Menu" + RESET);
            int ch;

            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println(RED + "Invalid choice. Try again." + RESET);
                continue;
            }

            switch (ch) {
                case 1: continue;
                case 2:
                    otp = otpGen();
                    System.out.println(YELLOW + "New OTP: " + PURPLE + otp + RESET);
                    continue;
                case 3: return false;
                default: System.out.println(RED + "Invalid Choice." + RESET);
            }
        }
    }

    // OTP GENERATOR
    public static int otpGen() {
        return new Random().nextInt(9000) + 1000;
    }

    // FIND ACCOUNT BY USERNAME
    static Account findAccount(String user) {
        for (Account a : accounts)
            if (a.getUser().equals(user)) return a;
        return null;
    }

    // FIND ACCOUNT BY PHONE
    static Account findAccountByPhone(String phone) {
        for (Account a : accounts)
            if (a.getNum().equals(phone)) return a;
        return null;
    }

    // PASSWORD RULE CHECK
    static boolean isValidPassword(String p) {
        return p.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
// ============================================================
// NEW: STORE CURRENT LOGGED-IN USER DETAILS
// ============================================================
private static String currentUser = null;
private static String currentPhone = null;

public static void setCurrentUser(String user, String phone) {
    currentUser = user;
    currentPhone = phone;
}

public static String getCurrentUser() {
    return currentUser;
}

public static String getCurrentPhone() {
    return currentPhone;
}

}