import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class FileLoader {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите имя файла: ");
        String fileName = scanner.nextLine();

        // Путь на доступную директорию
        String directory = "C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\MyFiles";

        System.out.print("Введите текст для сохранения: ");
        String text = scanner.nextLine();

        // Валидация входных данных
        if (!isValidFileName(fileName)) {
            System.out.println("Недопустимое имя файла.");
            return;
        }
        if (text.isEmpty()) {
            System.out.println("Текст для сохранения не может быть пустым.");
            return;
        }

        Path filePath = Paths.get(directory, fileName);

        // Создание директории, если она не существует
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            System.out.println("Ошибка при создании директории: " + e.getMessage());
            return;
        }

        // Сохранение файла
        try {
            long fileSize = saveFile(filePath, text);
            System.out.println("Файл сохранен. Размер файла: " + fileSize + " байт.");
            System.out.println("Файл создан по пути: " + filePath.toString());
        } catch (InvalidPathException e) {
            System.out.println("Ошибка: недопустимый путь - " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Ошибка при записи файла: " + e.getMessage());
            return;
        }

        // Поиск файла и вывод содержимого
        String readResponse;
        do {
            System.out.print("Хотите прочитать файл? (yes/no): ");
            readResponse = scanner.nextLine();
            if (!readResponse.equalsIgnoreCase("yes") && !readResponse.equalsIgnoreCase("no")) {
                System.out.println("Пожалуйста, введите 'yes' или 'no'.");
            }
        } while (!readResponse.equalsIgnoreCase("yes") && !readResponse.equalsIgnoreCase("no"));

        if (readResponse.equalsIgnoreCase("yes")) {
            readFile(filePath);
        }

        scanner.close();
    }

    private static boolean isValidFileName(String fileName) {
        // Проверка на недопустимые символы в имени файла
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        return !fileName.matches(".*[<>:\"/\\|?*].*");
    }

    private static long saveFile(Path filePath, String text) throws IOException {
        long startTime = System.currentTimeMillis();
        try (FileWriter fileWriter = new FileWriter(filePath.toFile(), false)) {
            fileWriter.write(text);
            fileWriter.flush();
        }
        long endTime = System.currentTimeMillis();
        long fileSize = new File(filePath.toString()).length();
        System.out.println("Время записи: " + (endTime - startTime) + " мс");
        return fileSize;
    }

    private static void readFile(Path filePath) {
        if (Files.exists(filePath)) {
            try {
                List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                System.out.println("Содержимое файла:");
                for (String line : lines) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Ошибка при чтении файла: " + e.getMessage());
            }
        } else {
            System.out.println("Файл не найден.");
        }
    }
}
