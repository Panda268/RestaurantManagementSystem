import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RestaurantManagementSystem extends JFrame {
    private JPanel mainPanel;
    private JButton tableButton;
    private JButton menuItemButton;
    private JButton orderButton;
    private JButton staffButton;

    // Tạo một phiên bản duy nhất của MenuManager để quản lý menu items
    private MenuManager menuManager;

    public RestaurantManagementSystem() {
        setTitle("Restaurant Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 10, 10));

        tableButton = new JButton("Manage Tables");
        menuItemButton = new JButton("Manage Menu Items");
        orderButton = new JButton("Manage Orders");
        staffButton = new JButton("Manage Staff");

        mainPanel.add(tableButton);
        mainPanel.add(menuItemButton);
        mainPanel.add(orderButton);
        mainPanel.add(staffButton);

        add(mainPanel);

        // Khởi tạo MenuManager một lần và dùng lại
        menuManager = new MenuManager();

        // Thêm sự kiện cho các nút để mở cửa sổ quản lý
        tableButton.addActionListener(e -> new TableManager().setVisible(true));
        menuItemButton.addActionListener(e -> menuManager.setVisible(true));

        // Truyền danh sách menu items từ menuManager sang orderManager
        orderButton.addActionListener(e -> {
            ArrayList<MenuItem> menuItems = menuManager.getMenuItems();
            new OrderManager(menuItems).setVisible(true);
        });

        staffButton.addActionListener(e -> new StaffManager().setVisible(true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestaurantManagementSystem().setVisible(true));
    }
}

class Table {
    private int tableNumber;
    private int capacity;
    private boolean reserved;
    private LocalDateTime reservationTime; // Thêm thuộc tính thời gian

    public Table(int tableNumber, int capacity, LocalDateTime reservationTime) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.reserved = false;
        this.reservationTime = reservationTime;
    }

    public int getTableNumber() { return tableNumber; }
    public int getCapacity() { return capacity; }
    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }
    public LocalDateTime getReservationTime() { return reservationTime; }
    public void setReservationTime(LocalDateTime reservationTime) { this.reservationTime = reservationTime; }

    @Override
    public String toString() {
        return "Table " + tableNumber + " | Capacity: " + capacity + 
               " | Reserved: " + reserved + " | Time: " + reservationTime;
    }
}

class TableManager extends JFrame {
    private JTextField tableNumberField;
    private JTextField capacityField;
    private JTextField reservationTimeField;
    private JButton addButton;
    private JButton deleteButton;
    private JTextArea tableList;
    private ArrayList<Table> tables; // Danh sách các bàn

    public TableManager() {
        setTitle("Table Manager");
        setSize(500, 400);
        setLayout(new FlowLayout());

        tables = new ArrayList<>();

        tableNumberField = new JTextField(10);
        capacityField = new JTextField(10);
        reservationTimeField = new JTextField(15);
        addButton = new JButton("Add Table");
        deleteButton = new JButton("Delete Table");
        tableList = new JTextArea(15, 40);

        add(new JLabel("Table Number:"));
        add(tableNumberField);
        add(new JLabel("Capacity:"));
        add(capacityField);
        add(new JLabel("Reservation Time (yyyy-MM-dd HH:mm):"));
        add(reservationTimeField);
        add(addButton);
        add(deleteButton);
        add(new JScrollPane(tableList));

        addButton.addActionListener(e -> {
            try {
                int number = Integer.parseInt(tableNumberField.getText());
                int capacity = Integer.parseInt(capacityField.getText());
                LocalDateTime reservationTime = LocalDateTime.parse(reservationTimeField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                Table table = new Table(number, capacity, reservationTime);
                tables.add(table);
                updateTableList();
                tableNumberField.setText("");
                capacityField.setText("");
                reservationTimeField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please check your data.");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int number = Integer.parseInt(tableNumberField.getText());
                tables.removeIf(table -> table.getTableNumber() == number);
                updateTableList();
                tableNumberField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid table number to delete.");
            }
        });
    }

    private void updateTableList() {
        tableList.setText("");
        for (Table table : tables) {
            tableList.append(table.toString() + "\n");
        }
    }
}

class MenuItem {
    private String name;
    private double price;
    private String description;

    public MenuItem(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}

class OrderItem {
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return menuItem.getName() + " (x" + quantity + ")";
    }
}


class MenuManager extends JFrame {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField descriptionField;
    private JButton addButton;
    private JButton deleteButton;
    private JTextArea menuList;
    private ArrayList<MenuItem> menuItems; // Danh sách các món ăn

    public MenuManager() {
        setTitle("Menu Manager");
        setSize(500, 400);
        setLayout(new FlowLayout());

        menuItems = new ArrayList<>();

        nameField = new JTextField(10);
        priceField = new JTextField(10);
        descriptionField = new JTextField(20);
        addButton = new JButton("Add Item");
        deleteButton = new JButton("Delete Item");
        menuList = new JTextArea(15, 40);

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Price:"));
        add(priceField);
        add(new JLabel("Description:"));
        add(descriptionField);
        add(addButton);
        add(deleteButton);
        add(new JScrollPane(menuList));

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String description = descriptionField.getText();
                MenuItem menuItem = new MenuItem(name, price, description);
                menuItems.add(menuItem);
                updateMenuList();
                nameField.setText("");
                priceField.setText("");
                descriptionField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please check your data.");
            }
        });

        deleteButton.addActionListener(e -> {
            String name = nameField.getText();
            menuItems.removeIf(item -> item.getName().equalsIgnoreCase(name));
            updateMenuList();
            nameField.setText("");
        });
    }

    private void updateMenuList() {
        menuList.setText("");
        for (MenuItem item : menuItems) {
            menuList.append(item.toString() + "\n");
        }
    }

    // Phương thức để trả về danh sách các món ăn
    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }
}


class Order {
    private int orderId;
    private ArrayList<OrderItem> orderItems;
    private double totalAmount;

    public Order(int orderId) {
        this.orderId = orderId;
        this.orderItems = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        totalAmount += orderItem.getMenuItem().getPrice() * orderItem.getQuantity();
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        StringBuilder items = new StringBuilder();
        for (OrderItem item : orderItems) {
            items.append(item.toString()).append(", ");
        }
        return "Order ID: " + orderId + " | Items: " + items + " | Total: " + totalAmount;
    }
}

class OrderManager extends JFrame {
    private JComboBox<MenuItem> menuComboBox;
    private JTextField quantityField;
    private JButton addButton;
    private JTextField orderIdField;
    private JTextArea orderList;
    private ArrayList<Order> orders; // List of orders

    public OrderManager(ArrayList<MenuItem> menuItems) {
        setTitle("Order Manager");
        setSize(500, 400);
        setLayout(new FlowLayout());

        orders = new ArrayList<>();

        menuComboBox = new JComboBox<>(menuItems.toArray(new MenuItem[0]));
        quantityField = new JTextField(5);
        addButton = new JButton("Add to Order");
        orderIdField = new JTextField(5);
        orderList = new JTextArea(10, 40);
        orderList.setEditable(false);

        add(new JLabel("Select Item:"));
        add(menuComboBox);
        add(new JLabel("Quantity:"));
        add(quantityField);
        add(addButton);
        add(new JLabel("Order ID:"));
        add(orderIdField);
        add(new JScrollPane(orderList));

        addButton.addActionListener(e -> {
            try {
                MenuItem selectedMenuItem = (MenuItem) menuComboBox.getSelectedItem();
                int quantity = Integer.parseInt(quantityField.getText());
                int orderId = Integer.parseInt(orderIdField.getText());

                if (selectedMenuItem != null && quantity > 0) {
                    Order order = findOrCreateOrder(orderId);
                    order.addOrderItem(new OrderItem(selectedMenuItem, quantity));
                    updateOrderList();
                    quantityField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please check your data.");
            }
        });
    }

    private Order findOrCreateOrder(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        // If not found, create a new order
        Order newOrder = new Order(orderId);
        orders.add(newOrder);
        return newOrder;
    }

    private void updateOrderList() {
        orderList.setText("");
        for (Order order : orders) {
            orderList.append(order.toString() + "\n");
        }
    }
}


class Staff {
    private int staffId;
    private String name;
    private String role;

    public Staff(int staffId, String name, String role) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
    }

    public int getStaffId() {
        return staffId;
    }

    @Override
    public String toString() {
        return "Staff ID: " + staffId + " | Name: " + name + " | Role: " + role;
    }
}

class StaffManager extends JFrame {
    private JTextField staffIdField;
    private JTextField nameField;
    private JTextField roleField;
    private JButton addButton;
    private JButton deleteButton; // New button for deleting staff
    private JTextArea staffList;
    private ArrayList<Staff> staffMembers; // List to store staff members

    public StaffManager() {
        setTitle("Staff Manager");
        setSize(400, 300);
        setLayout(new FlowLayout());

        staffMembers = new ArrayList<>(); // Initialize the list

        staffIdField = new JTextField(10);
        nameField = new JTextField(10);
        roleField = new JTextField(10);
        addButton = new JButton("Add Staff");
        deleteButton = new JButton("Delete Staff"); // Initialize delete button
        staffList = new JTextArea(10, 30);
        staffList.setEditable(false); // Make the staff list read-only

        add(new JLabel("Staff ID:"));
        add(staffIdField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Role:"));
        add(roleField);
        add(addButton);
        add(deleteButton); // Add delete button to the frame
        add(new JScrollPane(staffList));

        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(staffIdField.getText());
                String name = nameField.getText();
                String role = roleField.getText();
                Staff staff = new Staff(id, name, role);
                staffMembers.add(staff); // Add staff to the list
                updateStaffList(); // Update display
                clearFields(); // Clear input fields
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Staff ID. Please enter a number.");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(staffIdField.getText());
                staffMembers.removeIf(staff -> staff.getStaffId() == id); // Remove staff by ID
                updateStaffList(); // Update display
                clearFields(); // Clear input fields
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Staff ID. Please enter a number.");
            }
        });
    }

    private void updateStaffList() {
        staffList.setText("");
        for (Staff staff : staffMembers) {
            staffList.append(staff.toString() + "\n");
        }
    }

    private void clearFields() {
        staffIdField.setText("");
        nameField.setText("");
        roleField.setText("");
    }
}
