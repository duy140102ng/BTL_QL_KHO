import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Product implements IProduct, Serializable {
    private String id;
    private String name;
    private double importPrice;
    private double exportPrice;
    private double profit;
    private String description;
    private boolean status;
    private int categoryId;

    public Product() {
    }

    public Product(String id, String name, double importPrice, double exportPrice, double profit, String description, boolean status, int categoryId) {
        this.id = id;
        this.name = name;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
        this.profit = profit;
        this.description = description;
        this.status = status;
        this.categoryId = categoryId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }

    public double getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(double exportPrice) {
        this.exportPrice = exportPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void inputData(Scanner scanner) {
        this.id = inputId(Store.listProduct, scanner);
        this.name = inputNameProduct(Store.listProduct, scanner);
        this.importPrice = inputImportPrice(scanner);
        this.exportPrice = inpuExportPrice(scanner);
        this.description = inputDescription(scanner);
        this.status = inputStatus(scanner);
        this.categoryId = inputCategoryId(scanner, Store.listCategory);
    }

    public String inputId(List<Product> productList, Scanner scanner) {
        System.out.println("Mời bạn nhập mã sản phẩm: ");
        do {
            String bookId = lenghthString(3, 4, scanner);
            if (bookId.charAt(0) == 'P') {
                boolean isDupcription = false;
                for (int i = 0; i < productList.size(); i++) {
                    if (bookId.equals(productList.get(i).getId())) {
                        isDupcription = true;
                        break;
                    }
                }
                if (!isDupcription) {
                    return bookId;
                } else {
                    System.err.println("Mã sản phẩm bị trùng");
                }
            } else {
                System.err.println("Ký tự đầu tiên của sản phẩm là P, vui lòng nhập lại");
            }
        } while (true);
    }

    public String inputNameProduct(List<Product> productList, Scanner scanner) {
        System.out.println("Mời bạn nhập tên sản phẩm: ");
        do {
            String bookTitle = lenghthString(6, 30, scanner);
            boolean isDupcription = false;
            for (int i = 0; i < productList.size(); i++) {
                if (bookTitle.equals(productList.get(i).getName())) {
                    isDupcription = true;
                    break;
                }
            }
            if (!isDupcription) {
                return bookTitle;
            } else {
                System.err.println("Tên sản phẩm đã có, vui lòng nhập lại!");
            }
        } while (true);
    }

    public double inputImportPrice(Scanner scanner) {
        System.out.println("Mời bạn nhập giá nhập sản phẩm: ");
        do {
            double cateImportPrice = 0;
            try {
                cateImportPrice = Double.parseDouble(scanner.nextLine());
            }catch (Exception ex){
                System.err.println("Có lỗi: " +ex);
            }
            if (cateImportPrice < 0) {
                System.err.println("Gía nhập phải là số thực lớn hơn 0, vui lòng nhập lại");
            } else {
                return cateImportPrice;
            }
        } while (true);
    }

    public double inpuExportPrice(Scanner scanner) {
        System.out.println("Mời bạn nhập giá xuất sản phẩm: ");
        do {
            double cateExportPrice = 0;
            try {
                cateExportPrice = Double.parseDouble(scanner.nextLine());
            }catch (Exception ex){
                System.err.println("Có lỗi: " +ex);
            }
            if (cateExportPrice < MIN_INTEREST_RATE) {
                System.err.println("Gía xuất phải là số thực lớn hơn lãi suất, vui lòng nhập lại");
            } else {
                return cateExportPrice;
            }
        } while (true);
    }
    public String inputDescription(Scanner scanner) {
        System.out.println("Mời bạn nhập mô tả sản phẩm: ");
        do {
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.err.println("Mô tả sản phẩm không được bỏ trống, vui lòng nhập lại!");
            } else {
                return description;
            }
        } while (true);
    }
    public boolean inputStatus(Scanner scanner) {
        System.out.println("Mời bạn nhập trạng thái: ");
        do {
            String statusCategory = scanner.nextLine();
            if (statusCategory.equals("true") || statusCategory.equals("false")) {
                return Boolean.parseBoolean(statusCategory);
            }
        } while (true);
    }
    public int inputCategoryId(Scanner scanner, List<Category> categoryList) {
        System.out.println("Chọn danh mục của sản phẩm: ");
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.printf("%d.%s\n", i + 1, categoryList.get(i).getName());
        }
        System.out.println("Lựa chọn của bạn: ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 1 || choice > categoryList.size()) {
            System.err.println("Lựa chọn không hợp lệ, vui lòng chọn lại");
        }
        return categoryList.get(choice - 1).getId();
    }

    public String lenghthString(int a, int b, Scanner scanner) {
        do {
            try {
                String str = scanner.nextLine();
                if (str.length() > a && str.length() <= b) {
                    return str;
                } else {
                    System.err.println("Nhập đúng số lượng ký tự!");
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } while (true);
    }

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
    @Override
    public void displayData() {
        System.out.printf("%s|%-15s |%-25s |%-15s|%-15s |%-20s |%-20s|%-20s|%s%n", CYAN, this.id, this.name, this.importPrice,this.exportPrice, this.description, (this.status == true ? "Còn hàng" : "Ngừng bán"),(this.profit = this.exportPrice - this.importPrice), PURPLE);
    }

    @Override
    public void calProfit() {

    }

    public static void writeDataToFile(List<Product> productList) {
        File file = new File("books.txt");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(productList);
            oos.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static List<Product> readDataFromFile() {
        List<Product> listProductRead = null;
        File file = new File("books.txt");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            listProductRead = (List<Product>) ois.readObject();
            return listProductRead;
        } catch (FileNotFoundException e) {
            listProductRead = new ArrayList<>();
        } catch (IOException e) {
            listProductRead = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listProductRead;
    }
}
