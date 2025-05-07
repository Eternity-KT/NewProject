package com.library;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileUtility {
    private static final String BOOK_FILE_PATH = "src/main/java/DataPack/BookInfo.txt";
    private static final String MEMBER_FILE_PATH = "src/main/java/DataPack/MemberInfo.txt";
    private static final String TRANSACTION_FILE_PATH = "src/main/java/DataPack/Transaction.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Ensure directories exist
    static {
        File dataPackDir = new File("src/main/java/DataPack");
        if (!dataPackDir.exists()) {
            dataPackDir.mkdirs();
        }
    }

    // Book file operations
    public static ArrayList<Book> loadBooks() {
        ArrayList<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOK_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5) {
                        String id = parts[0].trim();
                        String title = parts[1].trim();
                        String author = parts[2].trim();
                        String category = parts[3].trim();
                        int quantity = Integer.parseInt(parts[4].trim());
                        
                        books.add(new Book(id, title, author, category, quantity));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, that's okay
            System.out.println("Book file not found, will be created when needed.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    public static void saveBooks(ArrayList<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOK_FILE_PATH))) {
            for (Book book : books) {
                writer.println(
                    book.getId() + "|" +
                    book.getTitle() + "|" +
                    book.getAuthor() + "|" +
                    book.getCategory() + "|" +
                    book.getAvailableQuantity()
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    // Member file operations
    public static ArrayList<Member> loadMembers() {
        ArrayList<Member> members = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MEMBER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String phone = parts[2].trim();
                        String email = parts[3].trim();
                        
                        Member member = new Member(id, name, phone, email);
                        
                        // Load borrowed books if available
                        if (parts.length > 4 && parts[4] != null && !parts[4].isEmpty()) {
                            String[] borrowedBooks = parts[4].split(",");
                            for (String bookId : borrowedBooks) {
                                if (!bookId.trim().isEmpty()) {
                                    member.getBorrowedBooks().add(bookId.trim());
                                }
                            }
                        }
                        
                        members.add(member);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, that's okay
            System.out.println("Member file not found, will be created when needed.");
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
        return members;
    }

    public static void saveMembers(ArrayList<Member> members) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MEMBER_FILE_PATH))) {
            for (Member member : members) {
                StringBuilder sb = new StringBuilder();
                sb.append(member.getId()).append("|")
                  .append(member.getName()).append("|")
                  .append(member.getPhone()).append("|")
                  .append(member.getEmail()).append("|");
                
                // Add borrowed books
                ArrayList<String> borrowedBooks = member.getBorrowedBooks();
                if (!borrowedBooks.isEmpty()) {
                    for (int i = 0; i < borrowedBooks.size(); i++) {
                        sb.append(borrowedBooks.get(i));
                        if (i < borrowedBooks.size() - 1) {
                            sb.append(",");
                        }
                    }
                }
                
                writer.println(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }

    // Transaction file operations
    public static ArrayList<BorrowRecord> loadBorrowRecords() {
        ArrayList<BorrowRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        String memberId = parts[0].trim();
                        String bookId = parts[1].trim();
                        Date borrowDate = DATE_FORMAT.parse(parts[2].trim());
                        Date dueDate = DATE_FORMAT.parse(parts[3].trim());
                        
                        records.add(new BorrowRecord(memberId, bookId, borrowDate, dueDate));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, that's okay
            System.out.println("Transaction file not found, will be created when needed.");
        } catch (IOException | ParseException e) {
            System.err.println("Error loading borrow records: " + e.getMessage());
        }
        return records;
    }

    public static void saveBorrowRecords(ArrayList<BorrowRecord> records) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTION_FILE_PATH))) {
            for (BorrowRecord record : records) {
                writer.println(
                    record.getMemberId() + "|" +
                    record.getBookId() + "|" +
                    DATE_FORMAT.format(record.getBorrowDate()) + "|" +
                    DATE_FORMAT.format(record.getDueDate())
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving borrow records: " + e.getMessage());
        }
    }
}
