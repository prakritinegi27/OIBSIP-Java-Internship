import java.util.*;

class Book {
    int id; String title; boolean issued = false;
    Book(int id, String title) { this.id = id; this.title = title; }
    public String toString() { return id + ": " + title + (issued ? " (Issued)" : ""); }
}

class Member {
    int id; String name;
    Member(int id, String name) { this.id = id; this.name = name; }
}

class Library {
    Map<Integer, Book> books = new HashMap<>();
    Map<Integer, Member> members = new HashMap<>();
    Map<Integer, List<Integer>> issued = new HashMap<>();

    void addBook(int id, String title) { books.put(id, new Book(id, title)); }
    void addMember(int id, String name) { members.put(id, new Member(id, name)); }
    
    void listBooks() {
        if(books.isEmpty()) System.out.println("No books.");
        else books.values().forEach(b -> System.out.println(b));
    }

    void issueBook(int bId, int mId) {
        Book b = books.get(bId);
        Member m = members.get(mId);
        if(b == null || m == null) System.out.println("Invalid IDs.");
        else if(b.issued) System.out.println("Already issued.");
        else {
            b.issued = true;
            issued.computeIfAbsent(mId, k -> new ArrayList<>()).add(bId);
            System.out.println("Issued book to " + m.name);
        }
    }

    void returnBook(int bId, int mId) {
        Book b = books.get(bId);
        List<Integer> userBooks = issued.get(mId);
        if(b == null || userBooks == null || !userBooks.contains(bId)) {
            System.out.println("Invalid return.");
            return;
        }
        b.issued = false;
        userBooks.remove(Integer.valueOf(bId));
        System.out.println("Book returned.");
    }
}

public class LibrarySystem {
    static Scanner sc = new Scanner(System.in);
    static Library lib = new Library();

    public static void main(String[] args) {
        lib.addBook(1, "Java Basics");
        lib.addBook(2, "Python 101");
        lib.addMember(1, "Alice");
        lib.addMember(2, "Bob");

        while(true) {
            System.out.println("\n1.List Books 2.Issue Book 3.Return Book 4.Exit");
            int ch = Integer.parseInt(sc.nextLine());
            if(ch == 1) lib.listBooks();
            else if(ch == 2) {
                System.out.print("Member ID: "); int m = Integer.parseInt(sc.nextLine());
                System.out.print("Book ID: "); int b = Integer.parseInt(sc.nextLine());
                lib.issueBook(b, m);
            }
            else if(ch == 3) {
                System.out.print("Member ID: "); int m = Integer.parseInt(sc.nextLine());
                System.out.print("Book ID: "); int b = Integer.parseInt(sc.nextLine());
                lib.returnBook(b, m);
            }
            else break;
        }
        System.out.println("Goodbye!");
    }
}
