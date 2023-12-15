import java.util.*;
import java.util.stream.Collectors;

public class Store {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
    public static List<Category> listCategory;
    public static List<Product> listProduct;
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        listCategory = Category.readDataFromFile();
        listProduct = Product.readDataFromFile();
        int choice = 0;
        do {
            System.out.println("===== QUẢN LÝ KHO =====\n" +
                    "1. Quản lý danh mục\n" +
                    "2. Quản lý sản phẩm\n" +
                    "3. Thoát");
            System.out.println("Lựa chọn của bạn: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception ex) {
                System.out.println("Có lỗi: " + ex);
            }
            switch (choice) {
                case 1:
                    displayCategoryMenu();
                    break;
                case 2:
                    displayProductMenu();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.err.println("Vui lòng chọn từ 1-3!");
            }
        } while (true);
    }

    public static void displayCategoryMenu() {
        boolean isExit = true;
        int choice = 0;
        do {
            System.out.println("===== QUẢN LÝ DANH MỤC =====\n" +
                    "1. Thêm mới danh mục\n" +
                    "2. Cập nhật danh mục\n" +
                    "3. Xóa danh mục\n" +
                    "4. Tìm kiếm danh mục theo tên danh mục\n" +
                    "5. Thống kê số lượng sp đang có trong danh mục\n" +
                    "6. Quay lại");
            System.out.println("Lựa chọn của bạn:");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception ex) {
                System.out.println("Có lỗi: " + ex);
            }
            switch (choice) {
                case 1:
                    addCategory();
                    Category.writeDataToFile(Store.listCategory);
                    break;
                case 2:
                    updateCategory();
                    Category.writeDataToFile(Store.listCategory);
                    break;
                case 3:
                    deleteCategory();
                    Category.writeDataToFile(Store.listCategory);
                    break;
                case 4:
                    searchCategory();
                    break;
                case 5:
                    printStatistics();
                    break;
                case 6:
                    isExit = false;
                    break;
                default:
                    System.err.println("Vui lòng chọn 1-6!");
            }
        } while (isExit);
    }

    public static void addCategory() {
        System.out.println("Mời bạn nhập số lượng danh mục cần thêm: ");
        try {
            int n = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < n; i++) {
                Category category = new Category();
                category.inputData(scanner);
                listCategory.add(category);
            }
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }

    public static void updateCategory() {
        System.out.println("Mời bạn nhập mã danh mục cần cập nhật: ");
        try {
            int updateId = Integer.parseInt(scanner.nextLine());
            Optional<Category> updateCategory = listCategory.stream().filter(category -> category.getId() == updateId).findFirst();
            updateCategory.ifPresent(category -> {
                System.out.println("Mời bạn nhập thông tin mới cho danh mục");
                category.inputData(scanner);
                System.out.println("Danh mục đã được cập nhật thành công");
            });
            if (!updateCategory.isPresent()) {
                System.err.println("Không tìm thấy danh mục cần cập nhật");
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Mã danh mục phải là số nguyên");
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }

    public static void deleteCategory() {
        System.out.println("Mời bạn nhập mã danh mục cần xóa");
        try {
            int deleteId = Integer.parseInt(scanner.nextLine());
            boolean isReferenced = listProduct.stream().anyMatch(product -> product.getId().equals(deleteId));
            if (isReferenced) {
                System.err.println("Danh mục đang có sản phẩm, không thể xóa danh mục");
            } else {
                listCategory.removeIf(category -> category.getId() == deleteId);
                System.out.println("Danh mục đã xóa thành công");
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Mã danh mục phải là số nguyên");
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }

    public static List<Category> searchCategory(String keyCategory) {
        String keyLowerCase = keyCategory.toLowerCase();
        List<Category> foundCategories = listCategory.stream()
                .filter(category -> containsIgnoreCase(category.getName(), keyLowerCase))
                .collect(Collectors.toList());
        return foundCategories;
    }

    private static boolean containsIgnoreCase(String source, String key) {
        return source.toLowerCase().contains(key);
    }

    public static void displayCategory(List<Category> categories) {
        if (categories == null) {
            System.out.println("Không tìm thấy danh mục phù hợp");
        } else {
            System.out.println("DANH SÁCH DANH MỤC:");
            System.out.println("---------------------------------------------------------");
            System.out.printf("|%s%-15s |%-20s |%-15s |%s%n",
                    YELLOW, "Mã danh mục", "Tên danh mục", "Mô tả", RESET);
            System.out.println("---------------------------------------------------------");
            categories.forEach(category -> {
                System.out.printf("|%-15s |%-20s |%-15s |%n",
                        category.getId(), category.getName(), category.getDescription());
                System.out.println("---------------------------------------------------------");
            });
        }
    }

    public static void searchCategory() {
        System.out.println("Mời bạn nhập tên danh mục để tìm kiếm: ");
        try {
            String keyCategory = scanner.nextLine().trim();

            if (!keyCategory.isEmpty()) {
                List<Category> searchResults = searchCategory(keyCategory);
                if (!searchResults.isEmpty()) {
                    displayCategory(searchResults);
                } else {
                    System.out.println("Không tìm thấy danh mục phù hợp với tên \"" + keyCategory + "\"");
                }
            } else {
                System.err.println("Vui lòng nhập từ tên danh mục tìm kiếm");
            }
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }

    public static void printStatistics() {
        System.out.printf("%s THỐNG KÊ SỐ LƯỢNG SẢN PHẨM ĐANG CÓ TRONG DANH MỤC:\n", RED);
        System.out.println("୨♡୧┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈୨♡୧");
        System.out.printf("%s|%-16s |%-21s |%-12s|%s%n", YELLOW, "Mã danh mục", "Tên tên danh mục", "Số sản phẩm", CYAN);
        System.out.println("୨♡୧┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈୨♡୧");

        try {
            for (Category category : listCategory) {
                int categoryId = category.getId();
                String categoryName = category.getName();
                long bookCount = listProduct.stream().filter(product -> product.getCategoryId() == categoryId).count();

                System.out.printf("| %s%-15d%s | %-20s | %s%-10d%s |%n", CYAN, categoryId, CYAN, categoryName, CYAN, bookCount, CYAN);
                System.out.println("୨♡୧┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈୨♡୧");
            }
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }

    public static void displayProductMenu() {
        boolean isExit = true;
        int choice = 0;
        do {
            System.out.println("===== QUẢN LÝ SẢN PHẨM =====\n" +
                    "1. Thêm mới sản phẩm\n" +
                    "2. Cập nhật sản phẩm\n" +
                    "3. Xóa sản phẩm\n" +
                    "4. Hiển thị sản phẩm theo tên A-Z\n" +
                    "5. Hiển thị sản phẩm theo lợi nhuận từ cao-thấp\n" +
                    "6. Tìm kiếm sản phẩm\n" +
                    "7. Quay lại");
            System.out.println("Lựa chọn của bạn:");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception ex) {
                System.out.println("Có lỗi: " + ex);
            }
            switch (choice) {
                case 1:
                    inputProduct();
                    Product.writeDataToFile(Store.listProduct);
                    break;
                case 2:
                    updateProduct();
                    Product.writeDataToFile(Store.listProduct);
                    break;
                case 3:
                    deleteProduct();
                    Product.writeDataToFile(Store.listProduct);
                    break;
                case 4:
                    displayProduct();
                    break;
                case 5:
                    displayProductsByProfitDescending();
                    break;
                case 6:
                    searchProduct();
                    break;
                case 7:
                    isExit = false;
                    break;
                default:
                    System.err.println("Vui lòng chọn 1-7!");
            }
        } while (isExit);
    }

    public static void inputProduct() {
        System.out.println("Mời bạn nhập số sản phẩm: ");
        try {
            int n = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < n; i++) {
                Product product = new Product();
                product.inputData(scanner);
                listProduct.add(product);
            }
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }

    public static void updateProduct() {
        System.out.println("Mời bạn nhập mã sản phẩm cần cập nhật: ");
        try {
            String updateId = scanner.nextLine().trim();
            Optional<Product> updateProductOptional = listProduct.stream()
                    .filter(product -> {
                        String productId = product.getId();
                        return productId != null && productId.equals(updateId);
                    })
                    .findFirst();
            if (updateProductOptional.isPresent()) {
                Product updateProduct = updateProductOptional.get();
                System.out.println("Mời bạn nhập thông tin mới cho sản phẩm");
                updateProduct.inputData(scanner);
                System.out.println("Thông tin đã được cập nhật thành công");
            } else {
                System.err.println("Không tìm thấy sản phẩm cần cập nhật");
            }
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }

    public static void deleteProduct() {
        System.out.println("Mời bạn nhập mã sản phẩm cần xóa");
        try {
            String deleteId = scanner.nextLine().trim();
            boolean isProductFound = listProduct.stream()
                    .anyMatch(product -> {
                        String bookId = product.getId();
                        return bookId != null && bookId.equals(deleteId);
                    });
            if (isProductFound) {
                listProduct.removeIf(product -> {
                    String productId = product.getId();
                    return productId != null && productId.equals(deleteId);
                });
                System.out.println("Thông tin sản phẩm đã xóa thành công");
            } else {
                System.out.println("Không tìm thấy sản phẩm với mã đã nhập");
            }
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }
    public static void displayProduct() {
        System.out.printf("%s                                                               THÔNG TIN SẢN PHẨM\n", RED);
        System.out.println("╔═━────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────━═╗");
        System.out.printf("%s|%-15s |%-25s |%-15s|%-15s |%-20s |%-20s|%-20s|%s%n", YELLOW, "Mã sản phẩm", "Tên sản phẩm", "Gía nhập","Gía xuất", "Mô tả", "Trạng thái","Lợi nhuận", PURPLE);
        System.out.println("╔═━────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────━═╗");
        try {
            List<Product> sortedProduct = listProduct.stream()
                    .sorted(Comparator.comparing(Product::getName))
                    .collect(Collectors.toList());

            for (Product product : sortedProduct) {
                product.displayData();
                System.out.printf("%s╔═━────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────━═╗\n", PURPLE);
            }
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }
    public static void searchProduct() {
        System.out.println("Mời bạn nhập thông tin cần tìm kiếm: ");
        try {
            String keyword = scanner.nextLine().trim().toLowerCase();
            List<Product> foundProducts = listProduct.stream()
                    .filter(product -> productContainsKeyword(product, keyword))
                    .collect(Collectors.toList());
            if (!foundProducts.isEmpty()) {
                System.out.println("                                                         CÁC SẢN PHẨM ĐƯỢC TÌM THẤY");
                System.out.println("╔═━────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────━═╗");
                System.out.printf("%s|%-15s |%-25s |%-15s|%-15s |%-20s |%-20s|%-20s|%s%n", YELLOW, "Mã sản phẩm", "Tên sản phẩm", "Gía nhập","Gía xuất", "Mô tả", "Trạng thái","Lợi nhuận", PURPLE);
                System.out.println("╔═━────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────━═╗");
                for (Product product : foundProducts) {
                    product.displayData();
                    System.out.println("╔═━────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────━═╗");
                }
            } else {
                System.out.println("Không tìm thấy sản phẩm nào phù hợp.");
            }
        } catch (Exception ex) {
            System.err.println("Có lỗi: " + ex);
        }
    }

    private static boolean productContainsKeyword(Product product, String keyword) {
        return product.getId().toLowerCase().contains(keyword)
                || product.getName().toLowerCase().contains(keyword)
                || Double.toString(product.getImportPrice()).contains(keyword)
                || Double.toString(product.getExportPrice()).contains(keyword)
                || Double.toString(product.getProfit()).contains(keyword)
                || product.getDescription().toLowerCase().contains(keyword)
                || Boolean.toString(product.isStatus()).contains(keyword)
                || Integer.toString(product.getCategoryId()).contains(keyword);
    }
    public static void displayProductsByProfitDescending() {
        Collections.sort(listProduct, Comparator.comparing(Product::getProfit).reversed());
        System.out.println("                                            DANH SÁCH CÁC SẢN PHẨM SẮP XẾP THEO LỢI NHUẬN GIẢM DẦN");
        System.out.println("╔═━────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────━═╗");
        System.out.printf("%s|%-15s |%-25s |%-15s|%-15s |%-20s |%-20s|%-20s|%s%n", YELLOW, "Mã sản phẩm", "Tên sản phẩm", "Gía nhập","Gía xuất", "Mô tả", "Trạng thái","Lợi nhuận", PURPLE);
        System.out.println("╔═━────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────━═╗");
        for (Product product : listProduct) {
            product.displayData();
            System.out.println("╔═━────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────━═╗");
        }
    }
}