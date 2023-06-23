package DomainLayer.Config;

import DAL.DALService;
import DomainLayer.Facade;
import DomainLayer.Logging.UniversalHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConfigParser {
    private  static final Logger logger = Logger.getLogger("Facade Logger");

    public static boolean parse(Facade facade) {
        UniversalHandler.GetInstance().HandleError(logger);
        UniversalHandler.GetInstance().HandleInfo(logger);
        ObjectMapper objectMapper = new ObjectMapper();
        String projectRootPath = System.getProperty("user.dir");
        if(projectRootPath.endsWith("WSEP")){
             projectRootPath = projectRootPath+ File.separator + "dev"+ File.separator + "onlineStore" + File.separator + "config";
        }
        else {
            projectRootPath = projectRootPath + File.separator + "config";
        }
        List<File> jsonFiles = findJsonFiles(projectRootPath);
        boolean loadedAdmin=false;
        for (File file : jsonFiles) {
            try {
                List<User> userList = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
                for (User user : userList) {
                    if (DALService.getInstance().getUser(user.getUsername()) == null) {
                        int id = facade.enterNewSiteVisitor();
                        if (user.isAdmin()) {
                            facade.registerInitialAdmin(user.getUsername(), user.getPassword());
                            loadedAdmin = true;
                        } else
                            facade.register(id, user.getUsername(), user.getPassword());
                        if (user.getStore() != null) {
                            facade.login(id, user.getUsername(), user.getPassword());
                            for (Store store : user.getStore()) {
                                int storeId = facade.OpenNewStore(id, store.getName());
                                if (store.getProducts() != null) {
                                    for (Product product : store.getProducts()) {
                                        facade.AddProduct(id, storeId, product.getName(), product.getPrice(), product.getCategory(), product.getQuantity(), product.getDescription());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (IOException e){
                logger.warning("Failed to parse file "+file.getName()+", Cause: I/O error");
            }
            catch (Exception e){
                logger.warning("Error while loading data from config file "+file.getName()+": invalid data values or structure, or data already exists");
            }
        }
        return loadedAdmin;
    }

    public static List<File> findJsonFiles(String directoryPath) {
        List<File> jsonFiles = new ArrayList<>();

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    jsonFiles.add(file);
                }
            }
        }

        return jsonFiles;
    }
    static class User {
        private String username;
        private String password;
        private boolean admin;
        private List<Store> store;

        @JsonProperty("admin")
        public boolean isAdmin() {
            return admin;
        }
        @JsonProperty("admin")
        public boolean setAdmin(boolean admin) {
            return this.admin=admin;
        }
        @JsonProperty("username")
        public String getUsername() {
            return username;
        }

        @JsonProperty("username")
        public void setUsername(String username) {
            this.username = username;
        }

        @JsonProperty("password")
        public String getPassword() {
            return password;
        }

        @JsonProperty("password")
        public void setPassword(String password) {
            this.password = password;
        }

        @JsonProperty("store")
        public List<Store> getStore() {
            return store;
        }

        @JsonProperty("store")
        public void setStore(List<Store> store) {
            this.store = store;
        }
    }

    static class Store {
        @JsonProperty("name")
        private String name;
        @JsonProperty("products")

        private List<Product> products;

        // Getters and setters

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("products")
        public List<Product> getProducts() {
            return products;
        }

        @JsonProperty("products")
        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

    static class Product {

        private String name;

        private double price;

        private String category;

        private String description;

        private int quantity;

        // Getters and setters

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }
        @JsonProperty("category")
        public String getCategory() {
            return category;
        }

        @JsonProperty("category")
        public void setCategory(String category) {
            this.category = category;
        }
        @JsonProperty("description")
        public String getDescription() {
            return description;
        }

        @JsonProperty("description")
        public void setDescription(String description) {
            this.description = description;
        }

        @JsonProperty("price")
        public double getPrice() {
            return price;
        }

        @JsonProperty("price")
        public void setPrice(double price) {
            this.price = price;
        }

        @JsonProperty("quantity")
        public int getQuantity() {
            return quantity;
        }

        @JsonProperty("quantity")
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}

