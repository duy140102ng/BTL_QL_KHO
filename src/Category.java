import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Category implements ICategory, Serializable {
    private int id;
    private String name;
    private String description;
    private boolean status;

    public Category() {
    }

    public Category(int id, String name, String description, boolean status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public void inputData(Scanner scanner) {
        this.id = inputId(Store.listCategory, scanner);
        this.name = inputCategoryName(Store.listCategory, scanner);
        this.status = inputStatus(scanner);
        this.description = inputDescription(scanner);
    }

    public int inputId(List<Category> categoryList, Scanner scanner) {
        System.out.println("Mời bạn nhập mã danh mục:");
        do {
            int categoryId = validate(scanner, 0);
            boolean isDuplication = false;
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryId == categoryList.get(i).getId()) {
                    isDuplication = true;
                    break;
                }
            }
            if (!isDuplication) {
                return categoryId;
            } else {
                System.err.println("Mã danh mục bị trùng, vui lòng nhập lại!");
            }
        } while (true);
    }

    public String inputCategoryName(List<Category> categoryList, Scanner scanner) {
        System.out.println("Mời bạn nhập tên danh mục: ");
        do {
            String cateName = lenghthString(6, 30, scanner);
            boolean isDuplication = false;
            for (int i = 0; i < categoryList.size(); i++) {
                if (cateName.equals(categoryList.get(i).getName())) {
                    isDuplication = true;
                    break;
                }
            }
            if (!isDuplication) {
                return cateName;
            } else {
                System.err.println("Tên bị trùng, vui lòng nhập lại!");
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

    public String inputDescription(Scanner scanner) {
        System.out.println("Mời bạn nhập mô tả danh mục: ");
        do {
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.err.println("Mô tả danh mục không được bỏ trống, vui lòng nhập lại!");
            } else {
                return description;
            }
        } while (true);
    }

    public int validate(Scanner scanner, int i) {
        do {
            try {
                int number = Integer.parseInt(scanner.nextLine());
                if (number > i) {
                    return number;
                } else {
                    System.err.println("Số nguyên lớn hơn 0, vui lòng nhập lại");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("🔒Vui lòng nhập số nguyên");
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }

        } while (true);
    }

    public String lenghthString(int a, int b, Scanner scanner) {
        do {
            try {
                String str = scanner.nextLine();
                if (str.length() > a && str.length() <= b) {
                    return str;
                } else {
                    System.err.println("🔒Nhập đúng số lượng ký tự!");
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
        System.out.printf("%s|%s%-20d  %s|%s%-30s  %s|%s%-20s  %s|%s%-20s |%n",
                CYAN, RESET, this.id, CYAN, RESET, this.name,
                CYAN, RESET, (this.status ? "Hoạt động" : "Không hoạt động"),CYAN, RESET, this.description );
    }

    public static void writeDataToFile(List<Category> categoryList) {
        File file = new File("categories.txt");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(categoryList);
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

    public static List<Category> readDataFromFile() {
        List<Category> listCategoryRead = null;
        File file = new File("categories.txt");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            listCategoryRead = (List<Category>) ois.readObject();
            return listCategoryRead;
        } catch (FileNotFoundException e) {
            listCategoryRead = new ArrayList<>();
        } catch (IOException e) {
            listCategoryRead = new ArrayList<>();
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
        return listCategoryRead;
    }
}
