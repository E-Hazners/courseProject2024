import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class DataHandler {
    private static final String FILE_PATH = "resources/customers.xml";
    private static final Map<String, Customer> customers = new HashMap<>();

    // Load customers from XML file
    public static void loadCustomers() {
        customers.clear(); // Clear the existing list to avoid duplicates
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                System.out.println("No customers.xml file found, creating a new one.");
                saveCustomers();
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            NodeList customerNodes = document.getElementsByTagName("customer");

            for (int i = 0; i < customerNodes.getLength(); i++) {
                Node node = customerNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String id = element.getElementsByTagName("id").item(0).getTextContent();
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    double balance = Double.parseDouble(element.getElementsByTagName("balance").item(0).getTextContent());

                    Customer customer = new Customer(id, name, balance);
                    customers.put(id, customer);
                }
            }

            System.out.println("Customers loaded successfully. Total: " + customers.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load customers.");
        }
    }

    // Save customers to XML file
    public static void saveCustomers() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("customers");
            document.appendChild(root);

            for (Customer customer : customers.values()) {
                Element customerElement = document.createElement("customer");

                Element idElement = document.createElement("id");
                idElement.appendChild(document.createTextNode(customer.getId()));
                customerElement.appendChild(idElement);

                Element nameElement = document.createElement("name");
                nameElement.appendChild(document.createTextNode(customer.getName()));
                customerElement.appendChild(nameElement);

                Element balanceElement = document.createElement("balance");
                balanceElement.appendChild(document.createTextNode(String.valueOf(customer.getBalance())));
                customerElement.appendChild(balanceElement);

                root.appendChild(customerElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(FILE_PATH));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            System.out.println("Customers saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to save customers.");
        }
    }

    public static Customer getCustomer(String id) {
        return customers.get(id);
    }

    public static Customer getCustomerByName(String name) {
        for (Customer customer : customers.values()) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null; // Return null if no match is found
    }

    public static String addCustomer(String name, double balance) {
        String id = "C" + (customers.size() + 1); // Generate new ID
        if (!customers.containsKey(id)) {
            Customer customer = new Customer(id, name, balance);
            customers.put(id, customer);
            saveCustomers();
            return id;
        }
        return null;
    }

    public static boolean deleteCustomer(String id) {
        if (customers.remove(id) != null) {
            saveCustomers();
            return true;
        }
        return false;
    }

    public static List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public static int countCustomersByBalance(double threshold, boolean above) {
        return (int) customers.values().stream()
                .filter(c -> above ? c.getBalance() > threshold : c.getBalance() < threshold)
                .count();
    }

    public static List<Customer> searchCustomersByBalance(double threshold, boolean above) {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (above && customer.getBalance() > threshold || !above && customer.getBalance() < threshold) {
                result.add(customer);
            }
        }
        return result;
    }
}
