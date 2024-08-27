import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OnlineShoppingSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductManager productManager = new ProductManager();
        ShoppingCart shoppingCart = new ShoppingCart();
        UserManager userManager = new UserManager();
        OrderManager orderManager = new OrderManager();
        ReviewManager reviewManager = new ReviewManager();
        FileHandler fileHandler = new FileHandler();

        while (true) {
            System.out.println("Welcome to the Online Shopping System!");
            System.out.println("1. Product Management");
            System.out.println("2. Shopping Cart");
            System.out.println("3. User Management");
            System.out.println("4. Order Management");
            System.out.println("5. Product Reviews");
            System.out.println("6. File I/O");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    productManager.manageProducts(scanner);
                    break;
                case 2:
                    shoppingCart.manageShoppingCart(scanner, productManager);
                    break;
                case 3:
                    userManager.manageUsers(scanner);
                    break;
                case 4:
                    orderManager.manageOrders(scanner, userManager, shoppingCart);
                    break;
                case 5:
                    reviewManager.manageReviews(scanner, productManager);
                    break;
                case 6:
                    fileHandler.manageFiles(productManager, userManager, orderManager);
                    break;
                case 7:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

class ProductManager {

    Map<Integer, Product> products = new HashMap<>();
    int nextProductId = 1;

    public void manageProducts(Scanner scanner) {
        while (true) {
            System.out.println("\nProduct Management Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Search Products");
            System.out.println("4. Back");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    viewProducts();
                    break;
                case 3:
                    searchProducts(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addProduct(Scanner scanner) {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter product category: ");
        String category = scanner.nextLine();

        Product product = new Product(nextProductId++, name, price, category);
        products.put(product.getId(), product);
        System.out.println("Product added successfully.");
    }

    private void viewProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("\nAvailable Products:");
        for (Product product : products.values()) {
            System.out.println(product);
        }
    }

    private void searchProducts(Scanner scanner) {
        System.out.print("Enter search term (name or category): ");
        String searchTerm = scanner.nextLine();

        boolean found = false;
        for (Product product : products.values()) {
            if (product.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    product.getCategory().toLowerCase().contains(searchTerm.toLowerCase())) {
                System.out.println(product);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No products found matching the search criteria.");
        }
    }

    public Product getProductById(int id) {
        return products.get(id);
    }
}

class Product {

    private int id;
    private String name;
    private double price;
    private String category;

    public Product(int id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }
}

class ShoppingCart {

    private List<Product> items = new ArrayList<>();

    public void manageShoppingCart(Scanner scanner, ProductManager productManager) {
        while (true) {
            System.out.println("\nShopping Cart Menu:");
            System.out.println("1. Add to Cart");
            System.out.println("2. View Cart");
            System.out.println("3. Remove from Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Back");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addProductToCart(scanner, productManager);
                    break;
                case 2:
                    viewCart();
                    break;
                case 3:
                    removeFromCart(scanner);
                    break;
                case 4:
                    checkout(scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addProductToCart(Scanner scanner, ProductManager productManager) {
        System.out.print("Enter product ID to add: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        Product product = productManager.getProductById(productId);
        if (product != null) {
            items.add(product);
            System.out.println("Product added to cart.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        System.out.println("\nYour Cart:");
        for (Product product : items) {
            System.out.println(product);
        }
    }

    private void removeFromCart(Scanner scanner) {
        System.out.print("Enter product ID to remove: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        boolean removed = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == productId) {
                items.remove(i);
                removed = true;
                break;
            }
        }
        if (removed) {
            System.out.println("Product removed from cart.");
        } else {
            System.out.println("Product not found in cart.");
        }
    }

    private void checkout(Scanner scanner) {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty. Nothing to checkout.");
            return;
        }
        System.out.println("\nCheckout:");
        double totalPrice = 0;
        for (Product product : items) {
            totalPrice += product.getPrice();
            System.out.println(product);
        }
        System.out.println("Total Price: " + totalPrice);
        System.out.print("Confirm checkout? (y/n): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            System.out.println("Thank you for your purchase! Your order is confirmed.");
            items.clear(); // Clear the cart
        } else {
            System.out.println("Checkout cancelled.");
        }
    }

    public List<Product> getItems() {
        return items;
    }
}

class UserManager {

    Map<Integer, User> users = new HashMap<>();
    int nextUserId = 1;

    public void manageUsers(Scanner scanner) {
        while (true) {
            System.out.println("\nUser Management Menu:");
            System.out.println("1. Register User");
            System.out.println("2. View Users");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    viewUsers();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = new User(nextUserId++, username, password);
        users.put(user.getId(), user);
        System.out.println("User registered successfully.");
    }

    private void viewUsers() {
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }
        System.out.println("\nRegistered Users:");
        for (User user : users.values()) {
            System.out.println(user);
        }
    }

    public User getUserById(int id) {
        return users.get(id);
    }
}

class User {

    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}

class OrderManager {

    public ProductManager productManager;
    public UserManager userManager;
    Map<Integer, Order> orders = new HashMap<>();
    int nextOrderId = 1;

    public void manageOrders(Scanner scanner, UserManager userManager, ShoppingCart shoppingCart) {
        while (true) {
            System.out.println("\nOrder Management Menu:");
            System.out.println("1. Place Order");
            System.out.println("2. View Order History");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    placeOrder(scanner, userManager, shoppingCart);
                    break;
                case 2:
                    viewOrderHistory();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void placeOrder(Scanner scanner, UserManager userManager, ShoppingCart shoppingCart) {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        User user = userManager.getUserById(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        if (shoppingCart.getItems().isEmpty()) {
            System.out.println("Your cart is empty. Nothing to order.");
            return;
        }

        Order order = new Order(nextOrderId++, user, shoppingCart.getItems());
        orders.put(order.getId(), order);
        System.out.println("Order placed successfully.");
        shoppingCart.getItems().clear(); // Clear the cart
    }

    private void viewOrderHistory() {
        if (orders.isEmpty()) {
            System.out.println("No order history available.");
            return;
        }
        System.out.println("\nOrder History:");
        for (Order order : orders.values()) {
            System.out.println(order);
        }
    }
}

class Order {

    private int id;
    private User user;
    private List<Product> items;

    public Order(int id, User user, List<Product> items) {
        this.id = id;
        this.user = user;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<Product> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", items=" + items +
                '}';
    }
}

class ReviewManager {

    private Map<Integer, Review> reviews = new HashMap<>();
    private int nextReviewId = 1;

    public void manageReviews(Scanner scanner, ProductManager productManager) {
        while (true) {
            System.out.println("\nProduct Review Menu:");
            System.out.println("1. Add Review");
            System.out.println("2. View Reviews");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addReview(scanner, productManager);
                    break;
                case 2:
                    viewReviews();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addReview(Scanner scanner, ProductManager productManager) {
        System.out.print("Enter product ID to review: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        Product product = productManager.getProductById(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter your review: ");
        String reviewText = scanner.nextLine();

        Review review = new Review(nextReviewId++, product, reviewText);
        reviews.put(review.getId(), review);
        System.out.println("Review added successfully.");
    }

    private void viewReviews() {
        if (reviews.isEmpty()) {
            System.out.println("No reviews available.");
            return;
        }
        System.out.println("\nAvailable Reviews:");
        for (Review review : reviews.values()) {
            System.out.println(review);
        }
    }
}

class Review {

    private int id;
    private Product product;
    private String reviewText;

    public Review(int id, Product product, String reviewText) {
        this.id = id;
        this.product = product;
        this.reviewText = reviewText;
    }

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getReviewText() {
        return reviewText;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", product=" + product.getName() +
                ", reviewText='" + reviewText + '\'' +
                '}';
    }
}

class FileHandler {
    private static final String PRODUCTS_FILE = "products.txt";
    private static final String USERS_FILE = "users.txt";
    private static final String ORDERS_FILE = "orders.txt";

    public void manageFiles(ProductManager productManager, UserManager userManager, OrderManager orderManager) {
        while (true) {
            System.out.println("\nFile I/O Menu:");
            System.out.println("1. Save Data");
            System.out.println("2. Load Data");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    saveData(productManager, userManager, orderManager);
                    break;
                case 2:
                    loadData(productManager, userManager, orderManager);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void saveData(ProductManager productManager, UserManager userManager, OrderManager orderManager) {
        try {
            saveProducts(productManager);
            saveUsers(userManager);
            saveOrders(orderManager);
            System.out.println("Data saved to files.");
        }catch (IOException e){
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void saveProducts(ProductManager productManager) {
    }

    private void loadData(ProductManager productManager, UserManager userManager, OrderManager orderManager) {
        try {
            loadProducts(productManager);
            loadUsers(userManager);
            loadOrders(orderManager);
            System.out.println("Data loaded from files.");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }


    }
    
    private void loadProducts(ProductManager productManager) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                String category = parts[3];
                Product product = new Product(id, name, price, category);
                productManager.products.put(id, product);
                productManager.nextProductId = Math.max(productManager.nextProductId, id + 1);
            }
        }
    }
    private void saveUsers(UserManager userManager) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : userManager.users.values()) {
                writer.write(user.getId() + "," + user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        }
    }
    private void loadUsers(UserManager userManager) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String username = parts[1];
                String password = parts[2];
                User user = new User(id, username, password);
                userManager.users.put(id, user);
                userManager.nextUserId = Math.max(userManager.nextUserId, id + 1);
            }
        }
    }
    private void saveOrders(OrderManager orderManager) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (Order order : orderManager.orders.values()) {
                writer.write(order.getId() + "," + order.getUser().getId() + "," + order.getItems().size());
                writer.newLine();
                for (Product product : order.getItems()) {
                    writer.write(product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getCategory());
                    writer.newLine();
                }
            }
        }
    }
    private void loadOrders(OrderManager orderManager) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                int userId = Integer.parseInt(parts[1]);
                int itemCount = Integer.parseInt(parts[2]);

                User user = orderManager.userManager.getUserById(userId);
                if (user == null) continue; // Skip if user is not found

                List<Product> items = new ArrayList<>();
                for (int i = 0; i < itemCount; i++) {
                    line = reader.readLine();
                    String[] productParts = line.split(",");
                    int productId = Integer.parseInt(productParts[0]);
                    Product product = orderManager.productManager.getProductById(productId);
                    if (product != null) items.add(product);
                }
                Order order = new Order(id, user, items);
                orderManager.orders.put(id, order);
                orderManager.nextOrderId = Math.max(orderManager.nextOrderId, id + 1);
            }
        }
    }
}





