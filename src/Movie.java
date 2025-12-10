import java.util.*;

// ========================
// Movie booking
// ========================

class Movie implements java.io.Serializable { // NEW: Added Serializable
    // Use Main's static scanner
    static Scanner sc = Main.sc;

    // local ANSI COLORS
    static final String RESET = Color.RESET;
    static final String YELLOW = Color.YELLOW;
    static final String CYAN = Color.CYAN;
    static final String GREEN = Color.GREEN;
    static final String RED = Color.RED;
    static final String MAGENTA = Color.PURPLE;
    static final String BLUE = Color.BLUE;
    static final String BOLD = Color.BOLD;
    static final String GOLD = Color.GOLD;

    // seats[movieIndex][theatre][show][seatNumber]
    // NOTE: first dimension expanded to support many movies (up to 100)
    // The seats array state is loaded/saved in Main using the instance field booking.seats.
    boolean[][][][] seats = new boolean[101][6][5][51];

    // Total seat capacity for each category
    final int NORMAL_TOTAL = 20;
    final int PREMIUM_TOTAL = 15;
    final int VIP_TOTAL = 15;
    final int TOTAL_SEATS = NORMAL_TOTAL + PREMIUM_TOTAL + VIP_TOTAL; // 50

    // Max seats allowed per transaction
    final int MAX_SEATS_PER_BOOKING = 6;

    // Helper method to determine total available seats for a specific type/show
    int getAvailableSeatsByType(int movie, int theatre, int show, int seatType) {
        int minSeat = 1, maxSeat = 50, total = 0, booked = 0;
        if (seatType == 1) {
            minSeat = 1;
            maxSeat = 20;
            total = NORMAL_TOTAL;
        } else if (seatType == 2) {
            minSeat = 21;
            maxSeat = 35;
            total = PREMIUM_TOTAL;
        } else if (seatType == 3) {
            minSeat = 36;
            maxSeat = 50;
            total = VIP_TOTAL;
        } else return 0;

        // guard: ensure movie index is within bounds
        if (movie < 1 || movie > MovieSystem.movies.size()) return 0;

        for (int i = minSeat; i <= maxSeat; i++) {
            if (seats[movie][theatre][show][i]) {
                booked++;
            }
        }
        return total - booked;
    }

    int getPricePerType(int seatType) {
        if (seatType == 1) return 150;
        if (seatType == 2) return 250;
        if (seatType == 3) return 350;
        return 0;
    }

    // Displays the small, category-specific layout (No Legend)
    void displayCategoryLayout(int movie, int theatre, int show, int seatType) {

        int minSeat = 1, maxSeat = 50;
        String typeName, color;

        if (seatType == 1) {
            minSeat = 1;
            maxSeat = 20;
            typeName = "NORMAL";
            color = GREEN;
        } else if (seatType == 2) {
            minSeat = 21;
            maxSeat = 35;
            typeName = "PREMIUM";
            color = GOLD;
        } else {
            minSeat = 36;
            maxSeat = 50;
            typeName = "VIP";
            color = BLUE;
        }

        int available = getAvailableSeatsByType(movie, theatre, show, seatType);

        System.out.println(BOLD + "\n--- " + typeName + " SEAT LAYOUT (Seats " + minSeat + "-" + maxSeat + ") ---" + RESET);
        System.out.println(CYAN + "AVAILABLE SEATS: " + available + RESET);

        if (available == 0) {
            System.out.println(RED + "ALL SEATS BOOKED IN THIS CATEGORY." + RESET);
            return;
        }

        System.out.println("----------------------------------------");

        // Print seats in the category (descending for compact view)
        int count = 0;
        for (int i = maxSeat; i >= minSeat; i--) {
            if (seats[movie][theatre][show][i]) {
                // Booked: print seat number as [XX]
                System.out.print(RED + BOLD + "[XX]" + RESET + " ");
            } else {
                // Available: print seat number
                System.out.print(color + BOLD + String.format("[%02d]", i) + RESET + " ");
            }

            count++;
            // Print 5 seats per line for a neat, compact view
            if (count % 5 == 0) {
                System.out.println();
            }
        }
        if (count % 5 != 0) System.out.println(); // Newline if last row wasn't full
        System.out.println("----------------------------------------");
    }

    // Helper method to determine total available seats for a specific show (used in Main)
    int getAvailableSeats(int movie, int theatre, int show) {
        int count = 0;
        if (movie < 1 || movie > MovieSystem.movies.size() || theatre < 1 || theatre > 5 || show < 1 || show > 4) return 0;
        for (int i = 1; i <= TOTAL_SEATS; i++) {
            if (!seats[movie][theatre][show][i]) {
                count++;
            }
        }
        return count;
    }

    void display_movies() {
        MovieSystem.displayMovies();
    }

    // Select movie now uses MovieSystem.movies dynamically and prints its full details
    String select_movie(int a) {
        int movieCount = MovieSystem.movies.size();
        if (a < 1 || a > movieCount) return "invalid";

        AdminMovie m = MovieSystem.movies.get(a - 1);

        System.out.println("\n---------------------------------");
        System.out.println(YELLOW + "          " + m.name + RESET);
        System.out.println("          --------");
        System.out.println("DURATION            :- " + (m.duration == null ? "N/A" : m.duration));
        System.out.println("GENRE               :- " + (m.genre == null ? "N/A" : m.genre));
        System.out.println("MOVIE CERTIFICATION :- " + (m.certification == null ? "N/A" : m.certification));
        System.out.println("RELEASE DATE        :- " + (m.releaseDate == null ? "N/A" : m.releaseDate));
        System.out.println("---------------------------------");
        System.out.println("DIRECTOR       :- " + (m.director == null ? "N/A" : m.director));
        System.out.println("MUSIC DIRECTOR :- " + (m.musicDirector == null ? "N/A" : m.musicDirector));
        System.out.println("---------------------------------");
        System.out.println("CAST");
        System.out.println("----");
        if (m.cast == null || m.cast.isEmpty()) {
            System.out.println(" (No cast data)");
        } else {
            for (String actor : m.cast) {
                System.out.println("ACTOR   :- " + actor);
            }
        }
        System.out.println("---------------------------------");

        return m.name;
    }
    String getMovieReleaseDate(int index) {
        if (index < 1 || index > MovieSystem.movies.size()) return "N/A";
        return MovieSystem.movies.get(index - 1).releaseDate;
    }

    void theatres() {
        System.out.println(MAGENTA + "                AVAILABLE THEATRES" + RESET);
        System.out.println(MAGENTA + "                ___________________" + RESET);
        System.out.println();
        System.out.println("1 : " + CYAN + "PRASADS IMAX" + RESET);
        System.out.println("2 : " + CYAN + "SANDHYA 70 MM (RTC X ROADS)" + RESET);
        System.out.println("3 : " + CYAN + "SUDHARSHAN 35 MM (4k DOLBY ATMOS)" + RESET);
        System.out.println("4 : " + CYAN + "VISHWANATH 70MM KUKATPALLY" + RESET);
        System.out.println("5 : " + CYAN + "AMB CINEMAS HYDERABAD" + RESET);
        System.out.println("0 : HOME");
    }

    String[] theatreNames = {
            "", "PRASADS IMAX", "SANDHYA 70 MM (RTC X ROADS)",
            "SUDHARSHAN 35 MM (4k DOLBY ATMOS)",
            "VISHWANATH 70MM KUKATPALLY", "AMB CINEMAS HYDERABAD"
    };

    String select_theatre(int a) {
        if (a < 1 || a > 5) return "invalid";
        String name = theatreNames[a];
        System.out.println(CYAN + "\n                   " + name + RESET);
        showTimings();
        return name;
    }

    void showTimings() {
        System.out.println();
        System.out.println(MAGENTA + "                         AVAILABLE SHOWS" + RESET);
        System.out.println(MAGENTA + "                         ---------------" + RESET);
        System.out.println("    --------------- --------------- --------------- ---------------");
        System.out.println("    | 1:- 10:20AM | | 2:- 01:00PM | | 3:- 04:25PM | | 4:- 07:20PM |");
        System.out.println("    --------------- --------------- --------------- ---------------");
        System.out.println("0 : HOME");
        System.out.println("SELECT THE SHOW");
    }

    String select_show(int a) {
        if (a == 1) return "10:20AM";
        if (a == 2) return "01:00PM";
        if (a == 3) return "04:25PM";
        if (a == 4) return "07:20PM";
        return "invalid type";
    }

    // Seat Selection Logic - Returns List of Selected Seats on Success
    List<Integer> select_seat(int movie, int theatre, int show, int seatType) {
        if (seatType < 1 || seatType > 3) return null;

        // validate movie index against dynamic list
        if (movie < 1 || movie > MovieSystem.movies.size()) {
            System.out.println(RED + "INVALID MOVIE SELECTED" + RESET);
            return null;
        }

        int minSeat = 1, maxSeat = 50;
        String typeName;

        if (seatType == 1) {
            minSeat = 1;
            maxSeat = 20;
            typeName = "NORMAL";
        } else if (seatType == 2) {
            minSeat = 21;
            maxSeat = 35;
            typeName = "PREMIUM";
        } else {
            minSeat = 36;
            maxSeat = 50;
            typeName = "VIP";
        }

        int totalAvailableHere = getAvailableSeatsByType(movie, theatre, show, seatType);

        System.out.println(YELLOW + "\n--- " + typeName + " SEAT SELECTION ---" + RESET);

        // 1. Display the small category layout
        displayCategoryLayout(movie, theatre, show, seatType);

        if (totalAvailableHere <= 0) {
            return null;
        }

        int maxSelectable = Math.min(totalAvailableHere, MAX_SEATS_PER_BOOKING);

        int numSeats;
        while (true) {
            // MODIFIED: Added specific prompts for number of seats selection
            System.out.println(YELLOW + "ENTER HOW MANY SEATS (Max " + maxSelectable + " seats):" + RESET);
            System.out.println(CYAN + "0 : HOME (Go Back to Movie Selection)" + RESET);
            System.out.print(YELLOW + "ENTER CHOICE (1-" + maxSelectable + " or 0): " + RESET);

            try {
                // Use nextLine() and parse
                numSeats = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println(RED + "INVALID INPUT, ENTER AGAIN" + RESET);
                continue;
            }

            if (numSeats == 0) return null; // User chose to go back (Home)

            if (numSeats >= 1 && numSeats <= MAX_SEATS_PER_BOOKING) {
                if (numSeats > totalAvailableHere) {
                    System.out.println(RED + "ONLY " + totalAvailableHere + " SEAT(S) AVAILABLE, ENTER AGAIN" + RESET);
                    continue;
                }
                break;
            }
            System.out.println(RED + "INVALID INPUT (Must be between 1 and " + MAX_SEATS_PER_BOOKING + "), ENTER AGAIN" + RESET);
        }

        List<Integer> selectedSeats = new ArrayList<>();

        System.out.println("\nCHOOSE OPTION");
        System.out.println(CYAN + "SELECT SEAT NUMBERS (" + minSeat + " to " + maxSeat + ") ONE BY ONE" + RESET);
        System.out.println(CYAN + "ENTER 0 TO GO BACK" + RESET);
        System.out.println("-------------------------------------------------------------------");

        for (int i = 1; i <= numSeats; i++) {
            System.out.print("Seat " + i + ": ");
            int seatNo;
            try {
                seatNo = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println(RED + "INVALID INPUT, ENTER AGAIN" + RESET);
                i--;
                continue;
            }
            if (seatNo == 0) return null; // User chose to go back

            if (seatNo < minSeat || seatNo > maxSeat) {
                System.out.println(RED + "INVALID SEAT NUMBER FOR THIS SEAT TYPE, ENTER AGAIN" + RESET);
                i--;
                continue;
            }

            // Check if already booked in the array for this specific show
            if (seats[movie][theatre][show][seatNo]) {
                System.out.println(RED + "THIS SEAT IS ALREADY BOOKED — CHOOSE ANOTHER SEAT" + RESET);
                i--;
                continue;
            }

            // Check if already selected in this current transaction
            if (selectedSeats.contains(seatNo)) {
                System.out.println(RED + "THIS SEAT IS ALREADY SELECTED — CHOOSE ANOTHER SEAT" + RESET);
                i--;
                continue;
            }

            selectedSeats.add(seatNo);
            System.out.println(GREEN + "Seat " + seatNo + " selected." + RESET);
        }

        return selectedSeats;
    }

    // Prints the full theatre layout (No Legend text)
    void printTheatreLayout(int movie, int theatre, int show, String theatreName) {

        // 1. --- THEATRE NAME HEADER ---
        System.out.println(Color.PURPLE + "\n" + Color.BOLD + "=========================================================" + Color.RESET);
        System.out.println(Color.PURPLE + Color.BOLD + String.format("%-57s", "  " + theatreName.toUpperCase()) + Color.RESET);
        System.out.println(Color.PURPLE + Color.BOLD + "=========================================================" + Color.RESET);

        // 2. --- ENTRANCE (BACK OF THE THEATRE) ---
        System.out.println(Color.CYAN + "\n" + Color.BOLD + "                       ENTRANCE" + Color.RESET);
        System.out.println(Color.CYAN + "---------------------------------------------------------" + Color.RESET);

        // 3. --- SEAT LAYOUT (10 rows of 5 seats) ---
        int currentSeat = 50;

        for (int r = 10; r >= 1; r--) {

            String categoryLabel = "";

            // Assign seat category label for clarity (These labels are kept for spatial awareness)
            if (r == 10) categoryLabel = Color.BOLD + Color.BLUE + "VIP (36-50)  " + Color.RESET;
            else if (r == 7) categoryLabel = Color.BOLD + Color.GOLD + "PREMIUM (21-35)" + Color.RESET;
            else if (r == 4) categoryLabel = Color.BOLD + Color.GREEN + "NORMAL (1-20) " + Color.RESET;
            else categoryLabel = "               ";

            // Print category label (left aligned)
            System.out.printf("%s ", categoryLabel);

            // Print seats in the row (5 seats per row)
            for (int seatInRow = 1; seatInRow <= 5; seatInRow++) {

                String display;
                String finalColor;

                if (seats[movie][theatre][show][currentSeat]) {
                    // Booked Seat
                    display = String.format("%-2s", "XX");
                    finalColor = Color.RED + Color.BOLD;
                } else {
                    // Available Seat
                    display = String.format("%02d", currentSeat);

                    if (currentSeat >= 36) finalColor = Color.BLUE + Color.BOLD; // VIP: 36-50
                    else if (currentSeat >= 21) finalColor = Color.GOLD + Color.BOLD; // Premium: 21-35
                    else finalColor = Color.GREEN + Color.BOLD; // Normal: 1-20
                }

                System.out.print(finalColor + "[" + display + "]" + Color.RESET + "  ");
                currentSeat--;
            }

            System.out.println(); // Newline after each row

        }

        // 4. --- SCREEN/STAGE (FRONT) ---
        System.out.println(Color.CYAN + "---------------------------------------------------------" + Color.RESET);
        System.out.println(Color.CYAN + Color.BOLD + "                       SCREEN THIS WAY" + Color.RESET);
        System.out.println(Color.CYAN + "---------------------------------------------------------" + Color.RESET);
    }
}