import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.io.*; // NEW: Required for persistence (Serialization)

// ========================
// Ticket Class (for History)
// ========================

class Ticket implements java.io.Serializable { // NEW: Added Serializable
    String bookingId;
    String movieName;
    String theatre;
    String showTime;
    String releaseDate;
    String seatsList;
    String seatType;
    int numTickets;
    int totalPrice;
    String user;
    String phone;
    String date; // This now only holds the date (dd-MM-yyyy)

    public Ticket(String bookingId, String movieName, String theatre, String showTime, String releaseDate,
                  String seatsList, String seatType, int numTickets, int totalPrice, String user, String phone, String date) {
        this.bookingId = bookingId;
        this.movieName = movieName;
        this.theatre = theatre;
        this.showTime = showTime;
        this.releaseDate = releaseDate;
        this.seatsList = seatsList;
        this.seatType = seatType;
        this.numTickets = numTickets;
        this.totalPrice = totalPrice;
        this.user = user;
        this.phone = phone;
        this.date = date;
    }

    // Small ticket print method for history
    public void printSmallTicket() {
        final String RESET = Color.RESET;
        final String CYAN = Color.CYAN;
        final String GREEN = Color.GREEN;
        final String BOLD = Color.BOLD;
        final String GOLD = Color.GOLD;

        System.out.println(BOLD + GOLD + "\n========================================" + RESET);
        System.out.println(BOLD + GOLD + "|         TICKET CONFIRMED             |" + RESET);
        System.out.println(BOLD + GOLD + "========================================" + RESET);
        System.out.println(CYAN + String.format("| %-14s : %-19s|", "Booking ID", bookingId) + RESET);
        // Shorten long strings for small format
        String shortMovie = movieName.substring(0, Math.min(19, movieName.length()));
        String shortTheatre = theatre.substring(0, Math.min(19, theatre.length()));
        String shortSeats = seatsList.substring(0, Math.min(19, seatsList.length()));

        System.out.println(GREEN + String.format("| %-14s : %-19s|", "Movie", shortMovie) + RESET);
        System.out.println(GREEN + String.format("| %-14s : %-19s|", "Theatre", shortTheatre) + RESET);
        System.out.println(CYAN + String.format("| %-14s : %-19s|", "Booking Date", date) + RESET); // MODIFIED: Date only
        System.out.println(CYAN + String.format("| %-14s : %-19s|", "Show Time", showTime) + RESET); // MODIFIED: Time separate
        System.out.println(CYAN + String.format("| %-14s : %-19s|", "Rel. Date", releaseDate) + RESET);
        System.out.println(GREEN + String.format("| %-14s : %-19s|", "Seats/Type", numTickets + " (" + seatType + ")") + RESET);
        System.out.println(GREEN + String.format("| %-14s : %-19s|", "Seat Nos.", shortSeats) + RESET);
        System.out.println(GOLD + String.format("| %-14s : Rs. %-15s|", "Total Price", totalPrice) + RESET);
        System.out.println(BOLD + GOLD + "========================================" + RESET);
    }
}

// ========================
// Centralized color constants
// ========================
class Color {
    public static final String RESET  = "\033[0m";
    public static final String RED    = "\033[0;31m";
    public static final String GREEN  = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE   = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN   = "\033[0;36m";
    public static final String GOLD   = "\033[38;5;220m"; // golden color
    public static final String BOLD   = "\033[1m";
    public static final String WHITE  = "\033[0;37m"; // Added white color constant
    public static final String MAGENTA = "\033[0;35m"; // Added magenta for consistent headers
}

// ========================
// Main orchestrator
// =======================

public class Main {
    public static final Scanner sc = new Scanner(System.in);
	public static void printPreConfirmationDetails(
        String movieName,
        String theatre,
        String show,
        String releaseDate,
        String seatsList,
        String seatTypeName,
        int seatCount,
        int totalPrice) {

    System.out.println("\n========== PRE-CONFIRMATION DETAILS ==========");
    System.out.println("Movie Name       : " + movieName);
    System.out.println("Theatre          : " + theatre);
    System.out.println("Show Time        : " + show);
    System.out.println("Release Date     : " + releaseDate);
    System.out.println("Seat Type        : " + seatTypeName);
    System.out.println("Selected Seats   : " + seatsList);
    System.out.println("Seats Count      : " + seatCount);
    System.out.println("Total Price      : Rs." + totalPrice);
    System.out.println("================================================");
}


    // NEW: Persistence constants and static Movie instance
    private static final String HISTORY_FILE = "history.ser";
    private static final String SEATS_FILE = "seats.ser";
    private static Movie booking = new Movie(); // Made static and initialized here

    // History list to store confirmed tickets (remains static)
    static List<Ticket> history = new ArrayList<>();

    // Define date formatter for consistency (dd-MM-yyyy format)
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // NEW: Helper method to load data from files
    @SuppressWarnings("unchecked")
    private static void loadData() {
        // Load Seats Array
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SEATS_FILE))) {
            booking.seats = (boolean[][][][]) ois.readObject();
            System.out.println(Color.GREEN + "" + Color.RESET);
        } catch (FileNotFoundException e) {
            System.out.println(Color.YELLOW + "" + Color.RESET);
        } catch (Exception e) {
            System.out.println(Color.RED + "Error loading seat data: " + e.getMessage() + Color.RESET);
        }

        // Load History List
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HISTORY_FILE))) {
            history = (List<Ticket>) ois.readObject();
            System.out.println(Color.GREEN + "" + Color.RESET);
        } catch (FileNotFoundException e) {
            System.out.println(Color.YELLOW + "" + Color.RESET);
        } catch (Exception e) {
            System.out.println(Color.RED + "Error loading history data: " + e.getMessage() + Color.RESET);
        }
    }

    // NEW: Helper method to save data to files
    private static void saveData() {
        // Save Seats Array
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SEATS_FILE))) {
            oos.writeObject(booking.seats);
        } catch (Exception e) {
            System.out.println(Color.RED + "Error saving seat data: " + e.getMessage() + Color.RESET);
        }

        // Save History List
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HISTORY_FILE))) {
            oos.writeObject(history);
        } catch (Exception e) {
            System.out.println(Color.RED + "Error saving history data: " + e.getMessage() + Color.RESET);
        }
        System.out.println(Color.GREEN + "\nData persistence complete." + Color.RESET);
    }


    // Helper method to generate a simple unique Booking ID
    private static String generateBookingId() {
        // Simple 8-character alphanumeric ID for console application
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return "BMS" + sb.toString();
    }

    // New method to print pre-payment confirmation details
   private static void printFinalTicket(String movieName, String theatre, String show, String releaseDate,
                                     String seatsList, String seatType, int numTickets, int totalPrice) {

    String bookingId = generateBookingId();

    // NEW LOGIN SYSTEM
    String user = Login.getCurrentUser();
    String phone = Login.getCurrentPhone();

    String date = LocalDate.now().format(DATE_FORMATTER);

    // Store in history
    history.add(new Ticket(bookingId, movieName, theatre, show, releaseDate, seatsList,
            seatType, numTickets, totalPrice, user, phone, date));

    final String RESET = Color.RESET;
    final String CYAN = Color.CYAN;
    final String GREEN = Color.GREEN;
    final String YELLOW = Color.YELLOW;
    final String BOLD = Color.BOLD;
    final String GOLD = Color.GOLD;

    System.out.println(BOLD + GOLD + "\n==================================================================" + RESET);
    System.out.println(BOLD + GOLD + "|                        TICKET CONFIRMED                        |" + RESET);
    System.out.println(BOLD + GOLD + "==================================================================" + RESET);

    System.out.println(CYAN + String.format("| %-20s : %-39s |", "BOOKING ID", bookingId) + RESET);
    System.out.println(CYAN + String.format("| %-20s : %-39s |", "DATE OF BOOKING", date) + RESET);

    System.out.println(BOLD + GOLD + "|----------------------------------------------------------------|" + RESET);
    System.out.println(GREEN + String.format("| %-20s : %-39s |", "MOVIE", movieName) + RESET);
    System.out.println(GREEN + String.format("| %-20s : %-39s |", "THEATRE", theatre) + RESET);
    System.out.println(GREEN + String.format("| %-20s : %-39s |", "SHOW TIME", show) + RESET);
    System.out.println(CYAN + String.format("| %-20s : %-39s |", "RELEASE DATE", releaseDate) + RESET);
    System.out.println(GREEN + String.format("| %-20s : %-39s |", "SEAT TYPE", seatType) + RESET);
    System.out.println(GREEN + String.format("| %-20s : %-39s |", "NO. OF TICKETS", numTickets) + RESET);
    System.out.println(GREEN + String.format("| %-20s : %-39s |",
            "SEAT NUMBERS",
            seatsList.substring(0, Math.min(39, seatsList.length()))
    ) + RESET);

    System.out.println(BOLD + GOLD + "|----------------------------------------------------------------|" + RESET);
    System.out.println(YELLOW + String.format("| %-20s : Rs. %-35s |", "TOTAL PRICE", totalPrice) + RESET);
    System.out.println(BOLD + GOLD + "|----------------------------------------------------------------|" + RESET);

    // USER DETAILS
    System.out.println(CYAN + String.format("| %-20s : %-39s |", "BOOKED BY", user) + RESET);
    System.out.println(CYAN + String.format("| %-20s : %-39s |", "CONTACT NO", phone) + RESET);

    System.out.println(BOLD + GOLD + "==================================================================" + RESET);
    System.out.println(BOLD + GOLD + "|       SHOW THIS BOOKING ID AT THE BOX OFFICE. ENJOY!           |" + RESET);
    System.out.println(BOLD + GOLD + "==================================================================" + RESET);
}
public static void displayHistory() {
    System.out.println(Color.PURPLE + "\n=========================================================" + Color.RESET);
    System.out.println(Color.PURPLE + Color.BOLD + "|                   BOOKING HISTORY                   |" + Color.RESET);
    System.out.println(Color.PURPLE + "=========================================================" + Color.RESET);

    String user = Login.getCurrentUser();

    // USER must be logged in
    if (user == null) {
        System.out.println(Color.RED + "ERROR: Please LOG IN first to view history." + Color.RESET);
        return;
    }

    boolean found = false;

    for (Ticket ticket : history) {
        if (ticket.user.equals(user)) {
            ticket.printSmallTicket();
            found = true;
        }
    }

    if (!found) {
        System.out.println(Color.YELLOW + "\nNO BOOKING HISTORY FOUND FOR " + user.toUpperCase() + "." + Color.RESET);
    }

    System.out.println(Color.PURPLE + "=========================================================" + Color.RESET);
}



    public static void main(String[] args) {
        // Movie booking = new Movie(); // OLD: Removed, now 'booking' is a static field.

        // NEW: Load persistence data on startup
        loadData();

        mainMenu:
        while (true) {
            System.out.println("\n==== WELCOME TO BOOK-MY-SHOW ====");
            System.out.println("1. USER");
            System.out.println("2. ADMIN");
            System.out.println("3. EXIT"); // HISTORY removed, EXIT renumbered to 3
            System.out.print("Select: ");

            String opt = sc.nextLine().trim();

            
                switch (opt) {

            case "1": // USER
                boolean loggedIn = Login.startLogin();
                if (!loggedIn) continue;

                //  ADDED — LOGIN LOADING EFFECT
                System.out.print(Color.YELLOW + "Logging you in");
                for (int i = 0; i < 3; i++) {
                    try { Thread.sleep(500); } catch (Exception e) {}
                    System.out.print(".");
                }
                System.out.println("\nLoading your dashboard...\n" + Color.RESET);
                try { Thread.sleep(700); } catch (Exception e) {}
                    userMenuLoop: // New loop for post-login user options
                    while (true) {
                        System.out.println(Color.PURPLE + "\n--- USER MENU ---" + Color.RESET);
                        System.out.println("1. BOOK MOVIE");
                        System.out.println("2. VIEW HISTORY"); // History is now here
                        System.out.println("3. LOGOUT");
                        System.out.print(Color.YELLOW + "SELECT OPTION: " + Color.RESET);

                        String userOpt = sc.nextLine().trim();

                        switch (userOpt) {
                            case "1": // BOOK MOVIE - existing booking flow
                                userBooking: // Label for the movie selection loop
                                while (true) {
                                    // --- Movie selection ---
                                    booking.display_movies();
                                    int maxMovieOption = MovieSystem.movies.size() + 1;
                                    int a;
                                    while (true) {
                                        System.out.print("SELECT MOVIE (1-" + maxMovieOption + "): ");
                                        try { a = Integer.parseInt(sc.nextLine()); }
                                        catch (Exception e) { System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET); continue; }
                                        if (a >= 1 && a <= maxMovieOption) break;
                                        System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET);
                                    }
                                    if (a == maxMovieOption) break userBooking; // Back to userMenuLoop

                                    String movieName = booking.select_movie(a);
                                    String releaseDate = booking.getMovieReleaseDate(a); // Fetch release date
                                    if (movieName.equals("invalid")) {
                                        System.out.println(Color.RED + "Invalid movie selection. Returning to movie list." + Color.RESET);
                                        continue;
                                    }

                                    // HOME / BOOK
                                    int b;
                                    while (true) {
                                        System.out.println("1 : HOME\n2 : BOOK TICKET");
                                        try { b = Integer.parseInt(sc.nextLine()); }
                                        catch (Exception e) { System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET); continue; }
                                        if (b == 1 || b == 2) break;
                                        System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET);
                                    }
                                    if (b == 1) continue;

                                    // --- Theatre selection ---
                                    booking.theatres();
                                    int d;
                                    while (true) {
                                        System.out.println("SELECT THE THEATRE (1-5), 0 TO GO BACK");
                                        try { d = Integer.parseInt(sc.nextLine()); }
                                        catch (Exception e) { System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET); continue; }
                                        if (d >= 0 && d <= 5) break;
                                        System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET);
                                    }
                                    if (d == 0) continue;
                                    String theatre = booking.select_theatre(d);

                                    // --- Show selection ---
                                    int c;
                                    while (true) {
                                        System.out.println("SELECT SHOW (1-4)");
                                        try { c = Integer.parseInt(sc.nextLine()); }
                                        catch (Exception e) { System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET); continue; }
                                        if (c >= 1 && c <= 4) break;
                                        System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET);
                                    }
                                    String show = booking.select_show(c);

                                    booking.printTheatreLayout(a, d, c, theatre);
                                    int availableOverall = booking.getAvailableSeats(a, d, c);
                                    System.out.println(Color.YELLOW + "TOTAL AVAILABLE SEATS: " + availableOverall + Color.RESET);
                                    if (availableOverall == 0) { System.out.println(Color.RED + "Show fully booked." + Color.RESET); continue; }

                                    // --- Seat type selection ---
                                    int normalAvail = booking.getAvailableSeatsByType(a,d,c,1);
                                    int premiumAvail = booking.getAvailableSeatsByType(a,d,c,2);
                                    int vipAvail = booking.getAvailableSeatsByType(a,d,c,3);

                                    System.out.println("\nTICKET PRICES:");
                                    System.out.println("1. " + (normalAvail>0?Color.GREEN:Color.RED) + "NORMAL (1-20) Rs.150 - Available: "+normalAvail+Color.RESET);
                                    System.out.println("2. " + (premiumAvail>0?Color.GOLD:Color.RED) + "PREMIUM (21-35) Rs.250 - Available: "+premiumAvail+Color.RESET);
                                    System.out.println("3. " + (vipAvail>0?Color.BLUE:Color.RED) + "VIP (36-50) Rs.350 - Available: "+vipAvail+Color.RESET);

                                    // MODIFIED: Added structured input for seat type
                                    int seatType;
                                    while (true) {
                                        System.out.println(Color.CYAN + "0 : HOME (Go Back to Movie Selection)" + Color.RESET);
                                        System.out.print(Color.YELLOW + "ENTER CHOICE (1-3 or 0): " + Color.RESET);
                                        try { seatType = Integer.parseInt(sc.nextLine()); }
                                        catch (Exception e) { System.out.println(Color.RED + "INVALID INPUT" + Color.RESET); continue; }

                                        if (seatType == 0) continue userBooking; // Go back to movie selection

                                        if (seatType>=1 && seatType<=3) {
                                            int availableSeats = booking.getAvailableSeatsByType(a,d,c,seatType);
                                            if (availableSeats <= 0) {
                                                System.out.println(Color.RED + "NO SEATS AVAILABLE in this category. Select another." + Color.RESET);
                                                continue;
                                            }
                                            break;
                                        }
                                        System.out.println(Color.RED + "INVALID INPUT" + Color.RESET);
                                    }

                                    String seatTypeName = seatType==1?"NORMAL":seatType==2?"PREMIUM":"VIP";
                                    List<Integer> selectedSeats = booking.select_seat(a,d,c,seatType);
                                    if (selectedSeats == null || selectedSeats.isEmpty()) continue; // Go back if user chose 0

                                    int totalPrice = booking.getPricePerType(seatType) * selectedSeats.size();
                                    String seatsList = selectedSeats.toString().replace("[","").replace("]","");

                                    // --- Pre-Payment Confirmation ---
                                    Main.printPreConfirmationDetails(movieName, theatre, show, releaseDate, seatsList, seatTypeName, selectedSeats.size(), totalPrice);

                                    // --- Confirmation & Payment ---
                                    paymentLoop:
                                    while (true) {
                                        System.out.println(Color.YELLOW + "\nDO YOU WANT TO CONTINUE THIS BOOKING OR CANCEL IT?" + Color.RESET);
                                        System.out.println(Color.CYAN + "1 : CONTINUE FOR PAYMENT" + Color.RESET);
                                        System.out.println(Color.CYAN + "2 : CANCEL" + Color.RESET);
                                        System.out.print(Color.YELLOW + "ENTER CHOICE: " + Color.RESET);
                                        String confirm = sc.nextLine();

                                        if (confirm.equals("1")) {
                                            boolean paid = false;
                                            try { paid = PaymentAppUPI.performPayment(totalPrice); }
                                            catch (InterruptedException ie) { System.out.println("Payment interrupted."); }

                                            if (paid) {
                                                for (int seatNo : selectedSeats) booking.seats[a][d][c][seatNo] = true;

                                                // Store and Print the structured ticket
                                                Main.printFinalTicket(movieName, theatre, show, releaseDate, seatsList, seatTypeName, selectedSeats.size(), totalPrice);

                                                // NEW: Save all persistent data after successful booking
                                                saveData();

                                                // Post-Booking Options
                                                String postBookingDecision;
                                                while (true) {
                                                    System.out.println(Color.YELLOW + "\nBOOKING COMPLETE. WHAT NEXT?" + Color.RESET);
                                                    System.out.println(Color.CYAN + "1 : BOOK AGAIN (Recommended Movies)" + Color.RESET);
                                                    System.out.println(Color.CYAN + "2 : LOGOUT (User Menu)" + Color.RESET); // Updated text
                                                    System.out.print(Color.YELLOW + "ENTER CHOICE: " + Color.RESET);
                                                    postBookingDecision = sc.nextLine();

                                                    if (postBookingDecision.equals("1")) {
                                                        break paymentLoop; // Exit payment loop, continue userBooking (Book Again)
                                                    } else if (postBookingDecision.equals("2")) {
                                                        continue userMenuLoop; // Exit user session (Main Menu)
                                                    } else {
                                                        System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET);
                                                    }
                                                }
                                            } else {
                                                System.out.println(Color.RED + "Payment failed/cancelled. Seats released." + Color.RESET);
                                                break paymentLoop; // Back to movie list (userBooking continue)
                                            }

                                        } else if (confirm.equals("2")) {
                                            System.out.println(Color.YELLOW + "BOOKING CANCELLED! Seats released." + Color.RESET);
                                            break paymentLoop; // Back to movie list (userBooking continue)
                                        } else {
                                            System.out.println(Color.RED + "INVALID INPUT, ENTER AGAIN" + Color.RESET);
                                        }
                                    }
                                } // END userBooking loop
                                break;

                            case "2": // VIEW HISTORY
                                displayHistory();
                                break;

                            case "3": // LOGOUT
                                System.out.println(Color.GREEN + "LOGOUT SUCCESSFUL!" + Color.RESET);
                                break userMenuLoop; // Go back to the mainMenu loop
                            default:
                                System.out.println(Color.RED + "INVALID OPTION!" + Color.RESET);
                        }
                    } // END userMenuLoop
                    break;

                case "2":
                    MovieSystem.adminLogin();
                    break;

                case "3": // EXIT
                    // NEW: Save all persistent data before exit
                    saveData();
                    System.out.println("THANK YOU!");
                    break mainMenu;

                default:
                    System.out.println(Color.RED + "INVALID CHOICE! " + Color.RESET);
                    break;
            } // END switch
        } // END mainMenu loop
    } // END main()
} // END Main class