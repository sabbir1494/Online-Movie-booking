import java.io.*;
import java.util.*;

class Movie {
    String name;
    String time;
    int seats;
    double price;

    Movie(String name, String time, int seats, double price) {
        this.name = name;
        this.time = time;
        this.seats = seats;
        this.price = price;
    }

    public String toString() {
        return name + "," + time + "," + seats + "," + price;
    }

    public static Movie fromString(String line) {
        String[] parts = line.split(",");
        return new Movie(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
    }
}

class User {
    String username;
    String password;
    List<String> bookings = new ArrayList<>();

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toString() {
        return username + "," + password + "," + String.join(";", bookings);
    }

    public static User fromString(String line) {
        String[] parts = line.split(",", 3); // split only into 3 parts
        User user = new User(parts[0], parts[1]);
        if (parts.length == 3 && !parts[2].isEmpty()) {
            user.bookings = new ArrayList<>(Arrays.asList(parts[2].split(";")));
        }
        return user;
    }
}

public class MovieBookingSystem {
    static Scanner sc = new Scanner(System.in);
    static List<Movie> movies = new ArrayList<>();
    static List<User> users = new ArrayList<>();
    static String adminUser = "admin";
    static String adminPass = "admin123";
    static User currentUser = null;

    static final String MOVIE_FILE = "movies.txt";
    static final String USER_FILE = "users.txt";

    public static void main(String[] args) {
        loadMoviesFromFile();
        loadUsersFromFile();

        while (true) {
            System.out.println("\n=== MOVIE BOOKING SYSTEM ===");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Register");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: adminLogin(); break;
                case 2: userLogin(); break;
                case 3: registerUser(); break;
                case 4:
                    saveMoviesToFile();
                    saveUsersToFile();
                    System.exit(0);
                default: System.out.println("Invalid choice!");
            }
        }
    }

    static void adminLogin() {
        System.out.print("Enter admin username: ");
        String user = sc.nextLine();
        System.out.print("Enter admin password: ");
        String pass = sc.nextLine();
        if (user.equals(adminUser) && pass.equals(adminPass)) {
            adminMenu();
        } else {
            System.out.println("Wrong credentials!");
        }
    }

    static void adminMenu() {
        while (true) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. Add Movie");
            System.out.println("2. View Movies");
            System.out.println("3. Update Movie");
            System.out.println("4. Delete Movie");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: addMovie(); break;
                case 2: viewMovies(); break;
                case 3: updateMovie(); break;
                case 4: deleteMovie(); break;
                case 5: return;
                default: System.out.println("Invalid option!");
            }
        }
    }

    static void addMovie() {
        System.out.print("Enter movie name: ");
        String name = sc.nextLine();
        System.out.print("Enter show time: ");
        String time = sc.nextLine();
        System.out.print("Enter total seats: ");
        int seats = sc.nextInt();
        System.out.print("Enter ticket price: ");
        double price = sc.nextDouble();
        movies.add(new Movie(name, time, seats, price));
        saveMoviesToFile();
        System.out.println("Movie added successfully!");
    }

    static void viewMovies() {
        System.out.println("\n--- MOVIE LIST ---");
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }
        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);
            System.out.printf("%d. %s | Time: %s | Seats: %d  | Price %.2f\n", i + 1, m.name, m.time, m.seats, m.price);
        }
    }

    static void updateMovie() {
        viewMovies();
        System.out.print("Enter movie number to update: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();
        if (index >= 0 && index < movies.size()) {
            System.out.print("Enter new name: ");
            String name = sc.nextLine();
            System.out.print("Enter new time: ");
            String time = sc.nextLine();
            System.out.print("Enter new seats: ");
            int seats = sc.nextInt();
            System.out.print("Enter new price: ");
            Double price = sc.nextDouble();
            movies.set(index, new Movie(name, time, seats, price));
            saveMoviesToFile();
            System.out.println("Movie updated!");
        } else {
            System.out.println("Invalid movie index!");
        }
    }

    static void deleteMovie() {
        viewMovies();
        System.out.print("Enter movie number to delete: ");
        int index = sc.nextInt() - 1;
        if (index >= 0 && index < movies.size()) {
            movies.remove(index);
            saveMoviesToFile();
            System.out.println("Movie deleted!");
        } else {
            System.out.println("Invalid movie index!");
        }
    }

    static void registerUser() {
        System.out.print("Choose username: ");
        String uname = sc.nextLine();
        for (User u : users) {
            if (u.username.equals(uname)) {
                System.out.println("Username already exists!");
                return;
            }
        }
        System.out.print("Choose password: ");
        String pass = sc.nextLine();
        users.add(new User(uname, pass));
        saveUsersToFile();
        System.out.println("Registration successful!");
    }

    static void userLogin() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();
        for (User u : users) {
            if (u.username.equals(uname) && u.password.equals(pass)) {
                currentUser = u;
                userMenu();
                return;
            }
        }
        System.out.println("Login failed!");
    }

    static void userMenu() {
        while (true) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. View Movies");
            System.out.println("2. Book Ticket");
            System.out.println("3. My Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: viewMovies(); break;
                case 2: bookTicket(); break;
                case 3: viewBookings(); break;
                case 4: cancelBooking(); break;
                case 5: currentUser = null; return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    static void bookTicket() {
        viewMovies();
        System.out.print("Enter movie number to book: ");
        int index = sc.nextInt() - 1;
        if (index >= 0 && index < movies.size()) {
            Movie m = movies.get(index);
            
            System.out.println("Selected Movie: " + m.name);
            System.out.println("Showtime: " + m.time);
            System.out.println("Available Seats: " + m.seats);
            System.out.printf("Ticket Price: %.2f\n", m.price);
    
            System.out.print("Enter number of tickets: ");
            int ticketCount = sc.nextInt();
    
            if (ticketCount <= 0) {
                System.out.println("You must book at least 1 ticket.");
            } else if (ticketCount <= m.seats) {
                m.seats -= ticketCount;
                double totalCost = ticketCount * m.price;
                String bookingInfo = String.format("%s | Time: %s | Tickets: %d | Total: %.2f", 
                                                    m.name, m.time, ticketCount, totalCost);
                currentUser.bookings.add(bookingInfo);
                saveMoviesToFile();
                saveUsersToFile(); // 🧠 Don't forget to save booking too
                System.out.println("✅ Booking successful!");
                System.out.println("🎟️ " + bookingInfo);
            } else {
                System.out.println("❌ Not enough seats available!");
            }
        } else {
            System.out.println("Invalid movie number!");
        }
    }
    

    static void viewBookings() {
        System.out.println("\n--- MY BOOKINGS ---");
        if (currentUser.bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (int i = 0; i < currentUser.bookings.size(); i++) {
                System.out.println((i + 1) + ". " + currentUser.bookings.get(i));
            }
        }
    }

    static void cancelBooking() {
        viewBookings();
        if (currentUser.bookings.isEmpty()) return;
        System.out.print("Enter booking number to cancel: ");
        int index = sc.nextInt() - 1;
        if (index >= 0 && index < currentUser.bookings.size()) {
            String booking = currentUser.bookings.remove(index);
            String[] parts = booking.split("\\|"); // Changed delimiter
            for (Movie m : movies) {
                if (m.name.equals(parts[0]) && m.time.equals(parts[1])) {
                    m.seats++;
                    break;
                }
            }
            saveMoviesToFile();
            saveUsersToFile();  // Save the bookings
            System.out.println("Booking cancelled.");
        } else {
            System.out.println("Invalid booking number!");
        }
    }

    // ===================== FILE OPERATIONS =====================

    static void loadMoviesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                movies.add(Movie.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Movie file not found. Starting with an empty movie list.");
        }
    }

    static void saveMoviesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MOVIE_FILE))) {
            for (Movie m : movies) {
                writer.println(m.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving movies to file.");
        }
    }

    static void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(User.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("User file not found. Starting with an empty user list.");
        }
    }

    static void saveUsersToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (User u : users) {
                writer.println(u.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving users to file.");
        }
    }
}
