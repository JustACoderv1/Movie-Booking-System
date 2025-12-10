import java.util.*;

// ========================
// Admin movie holder
// ========================
class AdminMovie implements java.io.Serializable { // Added Serializable for consistency
    String name;
    String rating;
    String duration;
    String genre;
    String certification;
    String releaseDate;
    String director;
    String musicDirector;
    ArrayList<String> cast;

    AdminMovie(String name, String rating, String duration, String genre,
               String certification, String releaseDate, String director,
               String musicDirector, ArrayList<String> cast) {

        this.name = name;
        this.rating = rating;
        this.duration = duration;
        this.genre = genre;
        this.certification = certification;
        this.releaseDate = releaseDate;
        this.director = director;
        this.musicDirector = musicDirector;
        this.cast = cast;
    }
}

// ========================
// Admin panel + movie list
// ========================
public class MovieSystem {
    static ArrayList<AdminMovie> movies = new ArrayList<>();

    static {
        // UPDATED: Release Dates as requested (dd-MM-yyyy format)
        movies.add(new AdminMovie(
                "VARANASI",
                "10/10",
                "2h 45m",
                "ACTION/DRAMA",
                "UA16+",
                "20-12-2025", // UPDATED
                "SS.RAJAMOULI",
                "MM.KEERAVANI",
                new ArrayList<>(Arrays.asList("MAHESH BABU", "PRIDVI RAJ SUKUMARAN"))
        ));

        movies.add(new AdminMovie(
                "PUSHPA - PART 3",
                "10/10",
                "3h 05m",
                "ACTION/THRILLER",
                "UA16+",
                "21-12-2025", // UPDATED
                "SUKUMAR",
                "DSP",
                new ArrayList<>(Arrays.asList("Allu Arjun", "Rashmika Mandanna", "Sunil"))
        ));

        movies.add(new AdminMovie(
                "SALAAR - PART 2",
                "10/10",
                "2h 55m",
                "ACTION",
                "UA16+",
                "22-12-2025", // UPDATED
                "PRASHANTH NEEL",
                "RAVI BASRUR",
                new ArrayList<>(Arrays.asList("Prabhas", "Prithviraj Sukumaran", "Shruti Haasan"))
        ));

        movies.add(new AdminMovie(
                "DEVARA - PART 2",
                "10/10",
                "2h 50m",
                "ACTION/DRAMA",
                "UA16+",
                "23-12-2025", // UPDATED
                "KORATALA SIVA",
                "ANIRUDH",
                new ArrayList<>(Arrays.asList("NTR Jr", "Janhvi Kapoor"))
        ));

        movies.add(new AdminMovie(
                "OG - PART 2",
                "10/10",
                "2h 55m",
                "ACTION/THRILLER",
                "UA16+",
                "24-12-2025", // UPDATED
                "SUJEETH",
                "SS THAMAN",
                new ArrayList<>(Arrays.asList("Pawan Kalyan", "Imraan Hashmi", "Arjun Das"))
        ));
    }



    // UPDATED: Structured display for recommended movies
    static void displayMovies() {
        System.out.println(Color.MAGENTA + "\n                        RECOMMENDED MOVIES" + Color.RESET);
        System.out.println(Color.MAGENTA + "                        ------------------" + Color.RESET);
        // Header Row
        System.out.println(Color.CYAN + "+---------------------------------------------------+" + Color.RESET);
        System.out.println(Color.CYAN + "| No. | Movie Title                      | Rating  |" + Color.RESET);
        System.out.println(Color.CYAN + "+---------------------------------------------------+" + Color.RESET);
        int index = 1;
        for (AdminMovie m : movies) {
            // Alignment: Index (3 chars), Name (32 chars), Rating (7 chars)
            String output = String.format("| %-3d | %-32s | %-7s |", index, m.name, m.rating);
            System.out.println(Color.WHITE + output + Color.RESET);
            index++;
        }
        // Footer and Exit Row
        System.out.println(Color.CYAN + "+---------------------------------------------------+" + Color.RESET);
        // Exit is option 'index' which is 1 + movies.size()
        String exitOutput = String.format("| %-3d | %-41s |", index, "HOME/LOGOUT"); // Changed text from EXIT to HOME/LOGOUT
        System.out.println(Color.YELLOW + exitOutput + Color.RESET);
        System.out.println(Color.CYAN + "+---------------------------------------------------+" + Color.RESET);
    }

    static void adminLogin() {
        int attempts = 3;

        while (attempts > 0) {
            System.out.print(Color.CYAN + "\nENTER ADMIN USERNAME: " + Color.RESET);
            String user = Main.sc.nextLine();

            System.out.print(Color.CYAN + "ENTER ADMIN PASSWORD: " + Color.RESET);
            String pass = Main.sc.nextLine();

            if (user.equals("admin") && pass.equals("1234")) {
                System.out.println(Color.GREEN + "\nLOGIN SUCCESSFUL!" + Color.RESET);
                adminMenu();
                return; // exit after successful login
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println(Color.RED + "\nINVALID CREDENTIALS! Attempts left: " + attempts + Color.RESET);
                }
            }
        }

        System.out.println(Color.RED + "\nTOO MANY FAILED ATTEMPTS! ACCESS BLOCKED.\n" + Color.RESET);
    }


    static void adminMenu() {
        while (true) {
            System.out.println("\n====== ADMIN PANEL ======");
            System.out.println(Color.CYAN+"1. VIEW MOVIES"+Color.RESET);
            System.out.println(Color.CYAN+"2. ADD MOVIE"+Color.RESET);
            System.out.println(Color.CYAN+"3. DELETE MOVIE"+Color.RESET);
            System.out.println(Color.CYAN+"4. LOGOUT"+Color.RESET);
            System.out.print(Color.YELLOW+"ENTER OPTION: "+Color.RESET);

            int opt;
            try {
                // Use nextLine() and parse to avoid leaving newline characters
                opt = Integer.parseInt(Main.sc.nextLine());
            } catch (Exception e) {
                System.out.println(Color.RED+"INVALID OPTION!"+Color.RESET);
                continue;
            }

            switch (opt) {
                case 1: displayMovies(); break;
                case 2: addMovie(); break;
                case 3: deleteMovie(); break;
                case 4: System.out.println(Color.GREEN+" LOGOUT SUCCESSFUL!"+Color.RESET); return;
                default: System.out.println(Color.RED+"INVALID OPTION!"+Color.RESET);
            }
        }
    }

    static void addMovie() {

        System.out.print(Color.YELLOW + "ENTER MOVIE NAME: " + Color.RESET);
        String name = Main.sc.nextLine();

        System.out.print(Color.YELLOW + "ENTER MOVIE RATING (e.g. 9/10): " + Color.RESET);
        String rating = Main.sc.nextLine();

        System.out.print(Color.YELLOW + "ENTER MOVIE DURATION (e.g. 2h 55m): " + Color.RESET);
        String duration = Main.sc.nextLine();

        System.out.print(Color.YELLOW + "ENTER GENRE (e.g. ACTION/THRILLER): " + Color.RESET);
        String genre = Main.sc.nextLine();

        System.out.print(Color.YELLOW + "ENTER MOVIE CERTIFICATION (e.g. UA, U/A, 18+): " + Color.RESET);
        String certification = Main.sc.nextLine();

        System.out.print(Color.YELLOW + "ENTER RELEASE DATE (e.g., 20-12-2025): " + Color.RESET);
        String releaseDate = Main.sc.nextLine();

        System.out.print(Color.YELLOW + "ENTER DIRECTOR NAME: " + Color.RESET);
        String director = Main.sc.nextLine();

        System.out.print(Color.YELLOW + "ENTER MUSIC DIRECTOR NAME: " + Color.RESET);
        String musicDirector = Main.sc.nextLine();

        // CAST LIST
        ArrayList<String> cast = new ArrayList<>();
        while (true) {
            System.out.print(Color.YELLOW + "ENTER CAST NAME (type 'done' to finish): " + Color.RESET);
            String actor = Main.sc.nextLine();

            if (actor.equalsIgnoreCase("done")) break;
            cast.add(actor);
        }

        movies.add(new AdminMovie(name, rating, duration, genre, certification,
                releaseDate, director, musicDirector, cast));

        System.out.println(Color.GREEN + "MOVIE ADDED SUCCESSFULLY!" + Color.RESET);
    }


    static void deleteMovie() {
        displayMovies();
        System.out.print(Color.YELLOW+"ENTER MOVIE NUMBER TO DELETE: "+Color.RESET);
        int num;
        try {
            // Input handling fixed
            num = Integer.parseInt(Main.sc.nextLine());
        } catch (Exception e) {
            System.out.println(Color.RED+"INVALID MOVIE NUMBER!"+Color.RESET);
            return;
        }

        if (num >= 1 && num <= movies.size()) {
            AdminMovie removed = movies.remove(num - 1);
            System.out.println(Color.RED+" MOVIE DELETED! (" + removed.name + ")"+Color.RESET);
        } else {
            System.out.println(Color.RED+"INVALID MOVIE NUMBER!"+Color.RESET);
        }
    }
}