


import java.util.*;
        import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.stream.Collectors;

/**
 * Main client class to run the FlipFit console application.
 * This class provides a menu-driven interface for users to interact with the system.
 */
public class abcd {

    private static final Scanner scanner = new Scanner(System.in);
    private static final FlipFitService flipFitService = new FlipFitService();

    public static void main(String[] args) {
        // Initialize the system with some dummy data
        initializeData();
        System.out.println("Welcome to FlipFit!");

        System.out.println("------------------------------------");

        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        registerUser();
                        break;
                    case "2":
                        viewAllCenters();
                        break;
                    case "3":
                        viewCenterAvailability();
                        break;
                    case "4":
                        bookSlot();
                        break;
                    case "5":
                        cancelBooking();
                        break;
                    case "6":
                        viewMyBookings();
                        break;
                    case "7":
                        findNearestSlot();
                        break;
                    case "8":
                        System.out.println("Exiting FlipFit. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Register User");
        System.out.println("2. View All Centers");
        System.out.println("3. View Center Availability");
        System.out.println("4. Book a Slot");
        System.out.println("5. Cancel a Booking");
        System.out.println("6. View My Bookings (for a specific day)");
        System.out.println("7. Find Nearest Available Slot");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void initializeData() {
        // Pre-populate some centers and slots for demonstration
        flipFitService.addCenter("Bellandur", "Bangalore");
        flipFitService.addCenter("Koramangala", "Bangalore");

        // Add slots to Bellandur center
        flipFitService.addSlot("Bellandur", "Mon", LocalTime.of(6, 0), LocalTime.of(7, 0), 2);
        flipFitService.addSlot("Bellandur", "Mon", LocalTime.of(7, 0), LocalTime.of(8, 0), 2);
        flipFitService.addSlot("Bellandur", "Mon", LocalTime.of(8, 0), LocalTime.of(9, 0), 2);
        flipFitService.addSlot("Bellandur", "Mon", LocalTime.of(18, 0), LocalTime.of(19, 0), 3);
        flipFitService.addSlot("Bellandur", "Mon", LocalTime.of(19, 0), LocalTime.of(20, 0), 3);
        flipFitService.addSlot("Bellandur", "Mon", LocalTime.of(20, 0), LocalTime.of(21, 0), 3);

        // Add slots for another day
        flipFitService.addSlot("Bellandur", "Tue", LocalTime.of(7, 0), LocalTime.of(8, 0), 4);


        // Add slots to Koramangala center
        flipFitService.addSlot("Koramangala", "Mon", LocalTime.of(7, 0), LocalTime.of(8, 0), 5);
        flipFitService.addSlot("Koramangala", "Mon", LocalTime.of(8, 0), LocalTime.of(9, 0), 5);
    }

    private static void registerUser() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        Customer customer = flipFitService.registerUser(name, email);
        if (customer != null) {
            System.out.println("Registration successful! Your Customer ID is: " + customer.getId());
        }
    }

    private static void viewAllCenters() {
        System.out.print("Enter city to view centers (e.g., Bangalore): ");
        String city = scanner.nextLine();
        flipFitService.viewAllCenters(city);
    }

    private static void viewCenterAvailability() {
        System.out.print("Enter Center name: ");
        String centerName = scanner.nextLine();
        System.out.print("Enter day (e.g., Mon): ");
        String day = scanner.nextLine();
        flipFitService.viewCenterAvailability(centerName, day);
    }

    private static void bookSlot() {
        System.out.print("Enter your Customer ID: ");
        String customerId = scanner.nextLine();
        System.out.print("Enter Center name: ");
        String centerName = scanner.nextLine();
        System.out.print("Enter day (e.g., Mon): ");
        String day = scanner.nextLine();
        System.out.print("Enter Slot ID: ");
        String slotId = scanner.nextLine();
        flipFitService.bookSlot(customerId, centerName, day, slotId);
    }

    private static void cancelBooking() {
        System.out.print("Enter your Customer ID: ");
        String customerId = scanner.nextLine();
        System.out.print("Enter Booking ID to cancel: ");
        String bookingId = scanner.nextLine();
        flipFitService.cancelBooking(customerId, bookingId);
    }

    private static void viewMyBookings() {
        System.out.print("Enter your Customer ID: ");
        String customerId = scanner.nextLine();
        System.out.print("Enter day to view bookings (e.g., Mon): ");
        String day = scanner.nextLine();
        flipFitService.viewMyBookings(customerId, day);
    }

    private static void findNearestSlot() {
        System.out.print("Enter your Customer ID: ");
        String customerId = scanner.nextLine();
        System.out.print("Enter Center name: ");
        String centerName = scanner.nextLine();
        System.out.print("Enter day (e.g., Mon): ");
        String day = scanner.nextLine();
        flipFitService.findNearestAvailableSlot(customerId, centerName, day);
    }
}

/**
 * Service class that contains the core business logic for the FlipFit application.
 */
class FlipFitService {

    private final List<Customer> customers = new ArrayList<>(); // Fulfills the ArrayList requirement
    private final Map<String, Center> centers = new HashMap<>();
    private final Map<String, Booking> bookings = new HashMap<>();
    private int customerIdCounter = 0;
    private int bookingIdCounter = 0;

    /**
     * Registers a new user onto the platform.
     *
     * @param name  The name of the customer.
     * @param email The email of the customer.
     * @return The newly created Customer object.
     */
    public Customer registerUser(String name, String email) {
        // Simple check to prevent duplicate emails
        if (customers.stream().anyMatch(c -> c.getEmail().equals(email))) {
            System.out.println("User with this email already exists.");
            return null;
        }
        String id = "C" + (++customerIdCounter);
        Customer customer = new Customer(id, name, email);
        customers.add(customer);
        return customer;
    }

    /**
     * Adds a new fitness center to the system.
     *
     * @param name The name of the center.
     * @param city The city where the center is located.
     */
    public void addCenter(String name, String city) {
        if (centers.containsKey(name)) {
            System.out.println("Center with this name already exists.");
            return;
        }
        Center center = new Center(name, city);
        centers.put(name, center);
    }

    /**
     * Adds a new time slot for a center.
     *
     * @param centerName The name of the center.
     * @param day        The day of the week (e.g., "Mon").
     * @param startTime  The start time of the slot.
     * @param endTime    The end time of the slot.
     * @param capacity   The maximum number of seats available.
     */
    public void addSlot(String centerName, String day, LocalTime startTime, LocalTime endTime, int capacity) {
        Center center = centers.get(centerName);
        if (center == null) {
            System.out.println("Error: Center not found.");
            return;
        }
        String slotId = "S" + UUID.randomUUID().toString().substring(0, 4);
        Slot slot = new Slot(slotId, startTime, endTime, capacity);
        center.addSlot(day, slot);
    }

    /**
     * Displays all centers for a given city.
     *
     * @param city The city to search for centers.
     */
    public void viewAllCenters(String city) {
        System.out.println("Centers in " + city + ":");
        boolean found = false;
        for (Center center : centers.values()) {
            if (center.getCity().equalsIgnoreCase(city)) {
                System.out.println(" - " + center.getName());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No centers found in this city.");
        }
    }

    /**
     * Displays the availability of slots for a center on a specific day.
     *
     * @param centerName The name of the center.
     * @param day        The day of the week.
     */
    public void viewCenterAvailability(String centerName, String day) {
        Center center = centers.get(centerName);
        if (center == null) {
            System.out.println("Error: Center not found.");
            return;
        }

        List<Slot> slots = center.getSlots().get(day);
        if (slots == null || slots.isEmpty()) {
            System.out.println("No slots available for " + centerName + " on " + day);
            return;
        }

        System.out.println("Availability for " + centerName + " on " + day + ":");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Slot slot : slots) {
            System.out.printf(" - Slot ID: %s, Time: %s - %s, Available Seats: %d/%d, Waitlist: %d%n",
                    slot.getId(),
                    slot.getStartTime().format(formatter),
                    slot.getEndTime().format(formatter),
                    slot.getAvailableSeats(),
                    slot.getCapacity(),
                    slot.getWaitlist().size());
        }
    }

    /**
     * Books a workout slot for a user.
     * This method also handles the business logic for preventing overbooking,
     * removing old bookings for the same slot, and managing waiting lists.
     *
     * @param customerId The ID of the customer.
     * @param centerName The name of the center.
     * @param day        The day of the week.
     * @param slotId     The ID of the slot.
     */
    public void bookSlot(String customerId, String centerName, String day, String slotId) {
        Customer customer = findCustomerById(customerId);
        Center center = centers.get(centerName);

        if (customer == null) {
            System.out.println("Error: Customer not found.");
            return;
        }
        if (center == null) {
            System.out.println("Error: Center not found.");
            return;
        }

        Slot slot = center.getSlot(day, slotId);
        if (slot == null) {
            System.out.println("Error: Slot not found.");
            return;
        }

        // Check for existing bookings for the same day and time
        boolean hasExistingBooking = customer.getBookings().stream()
                .anyMatch(b -> {
                    Slot existingSlot = centers.get(b.getCenterName()).getSlot(b.getDay(), b.getSlotId());
                    return existingSlot != null && existingSlot.getStartTime().equals(slot.getStartTime()) && b.getStatus().equals("BOOKED");
                });

        if (hasExistingBooking) {
            System.out.println("You have a booking at the same time. The old booking will be cancelled.");
            // Find and cancel the old booking
            customer.getBookings().stream()
                    .filter(b -> {
                        Slot existingSlot = centers.get(b.getCenterName()).getSlot(b.getDay(), b.getSlotId());
                        return existingSlot != null && existingSlot.getStartTime().equals(slot.getStartTime()) && b.getStatus().equals("BOOKED");
                    })
                    .findFirst()
                    .ifPresent(oldBooking -> cancelBooking(customerId, oldBooking.getId()));
        }

        if (slot.getAvailableSeats() > 0) {
            // Book the slot
            String bookingId = "B" + (++bookingIdCounter);
            Booking booking = new Booking(bookingId, customerId, centerName, day, slotId);
            slot.bookSeat(customer, booking);
            customer.addBooking(booking);
            bookings.put(bookingId, booking);
            System.out.println("Booking successful! Your Booking ID is: " + bookingId);
        } else {
            // Add to waiting list
            System.out.println("The slot is fully booked. Adding you to the waiting list.");
            String bookingId = "B" + (++bookingIdCounter);
            Booking booking = new Booking(bookingId, customerId, centerName, day, slotId, "WAITLISTED");
            slot.addToWaitlist(customer);
            customer.addBooking(booking);
            bookings.put(booking.getId(), booking);
        }
    }

    /**
     * Cancels a user's booking.
     *
     * @param customerId The ID of the customer.
     * @param bookingId  The ID of the booking to cancel.
     */
    public void cancelBooking(String customerId, String bookingId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Error: Customer not found.");
            return;
        }

        Booking bookingToCancel = customer.getBookings().stream()
                .filter(b -> b.getId().equals(bookingId))
                .findFirst()
                .orElse(null);

        if (bookingToCancel == null) {
            System.out.println("Error: Booking not found or does not belong to this user.");
            return;
        }

        Center center = centers.get(bookingToCancel.getCenterName());
        Slot slot = center.getSlot(bookingToCancel.getDay(), bookingToCancel.getSlotId());

        // Remove booking from customer's list
        customer.removeBooking(bookingToCancel);
        bookings.remove(bookingToCancel.getId());

        if (bookingToCancel.getStatus().equals("BOOKED")) {
            slot.unbookSeat(customer);
            System.out.println("Booking " + bookingId + " has been successfully cancelled.");

            // Check waitlist and promote the next person
            if (!slot.getWaitlist().isEmpty()) {
                Customer promotedCustomer = slot.getWaitlist().remove(0);
                System.out.println("Notification: User " + promotedCustomer.getId() + " has been promoted from the waiting list.");
                // Find the booking for the promoted customer and update its status
                promotedCustomer.getBookings().stream()
                        .filter(b -> b.getStatus().equals("WAITLISTED") && b.getSlotId().equals(slot.getId()))
                        .findFirst()
                        .ifPresent(b -> {
                            b.setStatus("BOOKED");
                            slot.bookSeat(promotedCustomer, b); // Re-book the seat for the promoted customer
                            System.out.println("User " + promotedCustomer.getId() + "'s booking is now confirmed with ID " + b.getId());
                        });
            }
        } else if (bookingToCancel.getStatus().equals("WAITLISTED")) {
            slot.getWaitlist().remove(customer);
            System.out.println("You have been removed from the waiting list for booking " + bookingId);
        }
    }

    /**
     * Displays all bookings for a given user on a specific day.
     *
     * @param customerId The ID of the customer.
     * @param day        The day to view bookings for.
     */
    public void viewMyBookings(String customerId, String day) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Error: Customer not found.");
            return;
        }

        List<Booking> dailyBookings = customer.getBookings().stream()
                .filter(b -> b.getDay().equalsIgnoreCase(day))
                .collect(Collectors.toList());

        if (dailyBookings.isEmpty()) {
            System.out.println("You have no bookings for " + day + ".");
            return;
        }

        System.out.println("Your bookings for " + day + ":");
        for (Booking booking : dailyBookings) {
            System.out.printf(" - Booking ID: %s, Center: %s, Day: %s, Slot ID: %s, Status: %s%n",
                    booking.getId(),
                    booking.getCenterName(),
                    booking.getDay(),
                    booking.getSlotId(),
                    booking.getStatus());
        }
    }

    /**
     * Finds the nearest available slot for a user on a given day and center,
     * avoiding conflicts with their existing bookings.
     *
     * @param customerId The ID of the customer.
     * @param centerName The name of the center.
     * @param day        The day of the week.
     */
    public void findNearestAvailableSlot(String customerId, String centerName, String day) {
        Customer customer = findCustomerById(customerId);
        Center center = centers.get(centerName);

        if (customer == null) {
            System.out.println("Error: Customer not found.");
            return;
        }
        if (center == null) {
            System.out.println("Error: Center not found.");
            return;
        }

        List<Slot> allSlotsForDay = center.getSlots().get(day);
        if (allSlotsForDay == null || allSlotsForDay.isEmpty()) {
            System.out.println("No slots available for " + day + " at " + centerName);
            return;
        }

        // Get the start times of the customer's existing bookings
        Set<LocalTime> bookedTimes = customer.getBookings().stream()
                .filter(b -> b.getDay().equalsIgnoreCase(day) && b.getStatus().equals("BOOKED"))
                .map(b -> {
                    Center bookingCenter = centers.get(b.getCenterName());
                    return bookingCenter.getSlot(b.getDay(), b.getSlotId()).getStartTime();
                })
                .collect(Collectors.toSet());

        // Find the nearest available slot
        Slot nearestSlot = allSlotsForDay.stream()
                .filter(s -> s.getAvailableSeats() > 0) // Only consider slots with available seats
                .filter(s -> !bookedTimes.contains(s.getStartTime())) // Avoid time conflicts
                .min(Comparator.comparingLong(s -> Duration.between(LocalTime.now(), s.getStartTime()).abs().toMinutes()))
                .orElse(null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        if (nearestSlot != null) {
            System.out.println("Found the nearest available slot for you:");
            System.out.printf(" - Slot ID: %s, Time: %s - %s, Available Seats: %d/%d%n",
                    nearestSlot.getId(),
                    nearestSlot.getStartTime().format(formatter),
                    nearestSlot.getEndTime().format(formatter),
                    nearestSlot.getAvailableSeats(),
                    nearestSlot.getCapacity());
        } else {
            System.out.println("Sorry, no suitable available slots found for you on " + day + " at " + centerName + ".");
        }
    }

    /**
     * Helper method to find a customer by their ID.
     *
     * @param customerId The ID of the customer.
     * @return The Customer object or null if not found.
     */
    private Customer findCustomerById(String customerId) {
        // Since the requirement is to use ArrayList, we iterate to find the customer.
        return customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .orElse(null);
    }
}

/**
 * Represents a Customer.
 */
class Customer {
    private String id;
    private String name;
    private String email;
    private List<Booking> bookings;

    public Customer(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bookings = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Booking> getBookings() { return bookings; }
    public void addBooking(Booking booking) { this.bookings.add(booking); }
    public void removeBooking(Booking booking) { this.bookings.remove(booking); }
}

/**
 * Represents a fitness Center.
 */
class Center {
    private String name;
    private String city;
    private Map<String, List<Slot>> slots;

    public Center(String name, String city) {
        this.name = name;
        this.city = city;
        this.slots = new HashMap<>();
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public Map<String, List<Slot>> getSlots() { return slots; }

    public void addSlot(String day, Slot slot) {
        slots.computeIfAbsent(day, k -> new ArrayList<>()).add(slot);
        // Sort slots by start time to maintain order
        slots.get(day).sort(Comparator.comparing(Slot::getStartTime));
    }

    public Slot getSlot(String day, String slotId) {
        if (!slots.containsKey(day)) {
            return null;
        }
        return slots.get(day).stream()
                .filter(s -> s.getId().equals(slotId))
                .findFirst()
                .orElse(null);
    }
}

/**
 * Represents a time Slot within a center.
 */
class Slot {
    private String id;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;
    private int availableSeats;
    private Map<String, Booking> bookedSeats; // To track which customer has which seat (ID to booking)
    private List<Customer> waitlist;

    public Slot(String id, LocalTime startTime, LocalTime endTime, int capacity) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.availableSeats = capacity;
        this.bookedSeats = new HashMap<>();
        this.waitlist = new LinkedList<>();
    }

    public String getId() { return id; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getCapacity() { return capacity; }
    public int getAvailableSeats() { return availableSeats; }
    public List<Customer> getWaitlist() { return waitlist; }

    public void bookSeat(Customer customer, Booking booking) {
        if (availableSeats > 0) {
            availableSeats--;
            bookedSeats.put(customer.getId(), booking);
        } else {
            throw new IllegalStateException("Slot is fully booked.");
        }
    }

    public void unbookSeat(Customer customer) {
        if (bookedSeats.containsKey(customer.getId())) {
            availableSeats++;
            bookedSeats.remove(customer.getId());
        }
    }

    public void addToWaitlist(Customer customer) {
        waitlist.add(customer);
    }
}

/**
 * Represents a Booking made by a customer.
 */
class Booking {
    private String id;
    private String customerId;
    private String centerName;
    private String day;
    private String slotId;
    private String status; // BOOKED, CANCELED, WAITLISTED

    public Booking(String id, String customerId, String centerName, String day, String slotId) {
        this.id = id;
        this.customerId = customerId;
        this.centerName = centerName;
        this.day = day;
        this.slotId = slotId;
        this.status = "BOOKED";
    }

    public Booking(String id, String customerId, String centerName, String day, String slotId, String status) {
        this.id = id;
        this.customerId = customerId;
        this.centerName = centerName;
        this.day = day;
        this.slotId = slotId;
        this.status = status;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getCenterName() { return centerName; }
    public String getDay() { return day; }
    public String getSlotId() { return slotId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
