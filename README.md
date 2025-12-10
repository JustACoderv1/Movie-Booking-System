# Movie Ticket Booking System

A Java-based console project implementing user login, admin operations, movie listings, seat booking, and UPI payment.

---

## Overview

This project is a console-based movie booking portal built in Java. It allows:

- Users to login or register.
- Admins to add and delete movies.
- Users to view movies, select theatres, choose seats, and pay via UPI.
- The system to retain booking history & seat memory during runtime.

> **Note:** This was a group project. My contribution: complete development of `Login.java`.

---

## Project Structure

```
MovieBookingSystem/
│
├── Main.java
├── src/
│ ├── Login.java
│ ├── Movie.java
│ ├── MovieSystem.java
│ └── PaymentAppUPI.java
│
└── bin/ (generated after compilation)
```

---

## File Descriptions

### Login.java

Handles user authentication:

- Register new users
- Login via username/password or phone OTP
- "Forgot password" feature
- Maximum 3 login attempts

> My contribution to the project.

### MovieSystem.java

Manages movies & admin operations:

- Displays the list of movies in the booking portal
- Admin can add new movies
- Admin can delete movies

### Movie.java

Handles:

- Displaying movies
- Showing available theatres and showtimes
- Seat selection
- Booking process for tickets

### PaymentAppUPI.java

Handles payment after seat selection:

- Simulates UPI payment
- Validates UPI PIN
- Completes the transaction

### Main.java

Central portal of the project:

- Runs and connects all other classes
- Stores seat memory and booking history during runtime
- Provides main menu for users/admins

---

## Running the Project

**Step 1 — Compile**

Create the `bin` folder (if not already present):

```bash
mkdir bin
```

Compile all `.java` files into `bin`:

```bash
javac -d bin src/*.java Main.java
```

**Step 2 — Run**

```bash
java -cp bin Main
```

---

## Contributors

### Group project

My contribution: complete development of `Login.java` (registration, authentication, OTP, password reset, login limit).

---
## Notes

Seat availability & booking history persist only during runtime.

No external databases are used—everything runs in memory.

---
