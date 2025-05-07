package com.library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
class LibraryManager {
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private ArrayList<BorrowRecord> borrowRecords;

    public LibraryManager() {
        // Load data from files
        loadAllData();
    }
    
    // Load all data from files
    private void loadAllData() {
        books = FileUtility.loadBooks();
        members = FileUtility.loadMembers();
        borrowRecords = FileUtility.loadBorrowRecords();
    }
    
    // Save all data to files
    private void saveAllData() {
        FileUtility.saveBooks(books);
        FileUtility.saveMembers(members);
        FileUtility.saveBorrowRecords(borrowRecords);
    }

    // Book management methods
    public boolean addBook(Book book) {
        if (getBook(book.getId()) != null) {
            return false;  // Book with this ID already exists
        }
        books.add(book);
        FileUtility.saveBooks(books); // Save changes to file
        return true;
    }

    public boolean updateBook(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(book.getId())) {
                books.set(i, book);
                FileUtility.saveBooks(books); // Save changes to file
                return true;
            }
        }
        return false;  // Book not found
    }

    public boolean removeBook(String bookId) {
        Book book = getBook(bookId);
        if (book == null) {
            return false;  // Book not found
        }

        // Check if the book is currently borrowed
        for (BorrowRecord record : borrowRecords) {
            if (record.getBookId().equals(bookId)) {
                return false;  // Book is currently borrowed
            }
        }

        books.remove(book);
        FileUtility.saveBooks(books); // Save changes to file
        return true;
    }

    public Book getBook(String bookId) {
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    public ArrayList<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getAvailableQuantity() > 0) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public ArrayList<Book> searchBooks(String searchTerm) {
        ArrayList<Book> results = new ArrayList<>();
        searchTerm = searchTerm.toLowerCase();

        for (Book book : books) {
            if (book.getId().toLowerCase().contains(searchTerm) ||
                    book.getTitle().toLowerCase().contains(searchTerm) ||
                    book.getAuthor().toLowerCase().contains(searchTerm) ||
                    book.getCategory().toLowerCase().contains(searchTerm)) {
                results.add(book);
            }
        }

        return results;
    }

    // Member management methods
    public boolean addMember(Member member) {
        if (getMember(member.getId()) != null) {
            return false;  // Member with this ID already exists
        }
        members.add(member);
        FileUtility.saveMembers(members); // Save changes to file
        return true;
    }

    public boolean updateMember(Member member) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId().equals(member.getId())) {
                // Keep the borrowed books list
                member.setBorrowedBooks(members.get(i).getBorrowedBooks());
                members.set(i, member);
                FileUtility.saveMembers(members); // Save changes to file
                return true;
            }
        }
        return false;  // Member not found
    }

    public boolean removeMember(String memberId) {
        Member member = getMember(memberId);
        if (member == null) {
            return false;  // Member not found
        }

        // Check if the member has borrowed books
        if (!member.getBorrowedBooks().isEmpty()) {
            return false;  // Member has borrowed books
        }

        members.remove(member);
        FileUtility.saveMembers(members); // Save changes to file
        return true;
    }

    public Member getMember(String memberId) {
        for (Member member : members) {
            if (member.getId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }

    public ArrayList<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    public ArrayList<Member> searchMembers(String searchTerm) {
        ArrayList<Member> results = new ArrayList<>();
        searchTerm = searchTerm.toLowerCase();

        for (Member member : members) {
            if (member.getId().toLowerCase().contains(searchTerm) ||
                    member.getName().toLowerCase().contains(searchTerm) ||
                    member.getPhone().toLowerCase().contains(searchTerm) ||
                    member.getEmail().toLowerCase().contains(searchTerm)) {
                results.add(member);
            }
        }

        return results;
    }

    // Borrowing management methods
    public boolean borrowBook(String memberId, String bookId, int days) {
        Member member = getMember(memberId);
        Book book = getBook(bookId);

        if (member == null || book == null) {
            return false;
        }

        if (book.getAvailableQuantity() <= 0) {
            return false;  // No available copies
        }

        // Create borrow record
        Calendar calendar = Calendar.getInstance();
        Date borrowDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date dueDate = calendar.getTime();

        BorrowRecord record = new BorrowRecord(memberId, bookId, borrowDate, dueDate);
        borrowRecords.add(record);

        // Update book quantity
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);

        // Update member's borrowed books
        member.getBorrowedBooks().add(bookId);

        // Save all changes to files
        saveAllData();
        
        return true;
    }

    public boolean returnBook(String memberId, String bookId) {
        Member member = getMember(memberId);
        Book book = getBook(bookId);

        if (member == null || book == null) {
            return false;
        }

        // Find and remove the borrow record
        BorrowRecord recordToRemove = null;
        for (BorrowRecord record : borrowRecords) {
            if (record.getMemberId().equals(memberId) && record.getBookId().equals(bookId)) {
                recordToRemove = record;
                break;
            }
        }

        if (recordToRemove == null) {
            return false;  // No borrow record found
        }

        borrowRecords.remove(recordToRemove);

        // Update book quantity
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);

        // Update member's borrowed books
        member.getBorrowedBooks().remove(bookId);

        // Save all changes to files
        saveAllData();
        
        return true;
    }

    public ArrayList<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }
    
    // Add a method to reload data from files (useful when restart)
    public void reloadData() {
        loadAllData();
    }
}