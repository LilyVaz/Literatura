package com.alura.Literatura;

import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MainController implements CommandLineRunner {
    private final BookService bookService;

    public MainController(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nMenú:");
                System.out.println("1. Buscar libro por título");
                System.out.println("2. Listar todos los libros");
                System.out.println("3. Salir");

                int option = scanner.nextInt();
                scanner.nextLine(); // Consumir salto de línea

                switch (option) {
                    case 1:
                        System.out.println("Ingrese el título del libro:");
                        String title = scanner.nextLine();
                        Book book = bookService.fetchBookFromApi(title);
                        if (book != null) {
                            bookService.saveBook(book);
                            System.out.println("Libro encontrado: " + book.getTitle());
                        }
                        break;
                    case 2:
                        List<Book> books = bookService.listAllBooks();
                        books.forEach(b -> System.out.println(b.getTitle() + " - " + b.getAuthor()));
                        break;
                    case 3:
                        System.out.println("Saliendo...");
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            }
        }
    }
}



