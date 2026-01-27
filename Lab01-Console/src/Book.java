import java.util.*;

public class Book {
    private int id;
    private String title;
    private String author;
    private long price;

    public Book() {
    }

    public Book(int id, String title, String author, long price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }

    public void input(Scanner sc) {
        System.out.print("Nhập mã sách: ");
        this.id = Integer.parseInt(sc.nextLine());

        System.out.print("Nhập tên sách: ");
        this.title = sc.nextLine();

        System.out.print("Nhập tác giả: ");
        this.author = sc.nextLine();

        System.out.print("Nhập đơn giá: ");
        this.price = Long.parseLong(sc.nextLine());
    }

    public void output() {
        System.out.printf(
                "BOOK: id=%d, title=%s, author=%s, price=%d%n",
                id, title, author, price
        );
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Book> listBook = new ArrayList<>();

        int chon;
        do {
            System.out.println("""
                ========= QUẢN LÝ SÁCH =========
                1. Thêm sách
                2. Xóa sách
                3. Chỉnh sửa sách
                4. Xuất danh sách
                5. Tìm sách Lập trình
                6. Lấy K sách giá <= P
                7. Tìm theo tác giả
                0. Thoát
                ================================
                """);

            System.out.print("Chọn chức năng: ");
            chon = Integer.parseInt(sc.nextLine());

            switch (chon) {
                case 1 -> {
                    Book b = new Book();
                    b.input(sc);
                    listBook.add(b);
                }

                case 2 -> {
                    System.out.print("Nhập mã sách cần xóa: ");
                    int id = Integer.parseInt(sc.nextLine());
                    listBook.removeIf(b -> b.getId() == id);
                }

                case 3 -> {
                    System.out.print("Nhập mã sách cần sửa: ");
                    int id = Integer.parseInt(sc.nextLine());

                    Book find = listBook.stream()
                            .filter(b -> b.getId() == id)
                            .findFirst()
                            .orElse(null);

                    if (find == null) {
                        System.out.println("❌ Không tìm thấy sách");
                        break;
                    }

                    System.out.println("""
                        1. Sửa tên
                        2. Sửa tác giả
                        3. Sửa giá
                        0. Hủy
                        """);

                    System.out.print("Chọn: ");
                    int opt = Integer.parseInt(sc.nextLine());

                    switch (opt) {
                        case 1 -> {
                            System.out.print("Tên mới: ");
                            find.setTitle(sc.nextLine());
                        }
                        case 2 -> {
                            System.out.print("Tác giả mới: ");
                            find.setAuthor(sc.nextLine());
                        }
                        case 3 -> {
                            System.out.print("Giá mới: ");
                            find.setPrice(Long.parseLong(sc.nextLine()));
                        }
                    }
                }

                case 4 -> listBook.forEach(Book::output);

                case 5 -> listBook.stream()
                        .filter(b -> b.getTitle().toLowerCase().contains("lập trình"))
                        .forEach(Book::output);

                case 6 -> {
                    System.out.print("Nhập số lượng K: ");
                    int k = Integer.parseInt(sc.nextLine());

                    System.out.print("Nhập giá P: ");
                    long p = Long.parseLong(sc.nextLine());

                    listBook.stream()
                            .filter(b -> b.getPrice() <= p)
                            .limit(k)
                            .forEach(Book::output);
                }

                case 7 -> {
                    System.out.print("Nhập số lượng tác giả: ");
                    int n = Integer.parseInt(sc.nextLine());

                    Set<String> authors = new HashSet<>();
                    for (int i = 0; i < n; i++) {
                        System.out.print("Tác giả " + (i + 1) + ": ");
                        authors.add(sc.nextLine().toLowerCase());
                    }

                    listBook.stream()
                            .filter(b -> authors.contains(b.getAuthor().toLowerCase()))
                            .forEach(Book::output);
                }
            }

        } while (chon != 0);
    }
}