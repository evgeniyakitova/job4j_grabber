package ru.job4j.cache;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Emulator {
    private final String actions = "Выберите действие:\n1 - Сохранить файл в кэш\n2 - Получить содержимое файла\n3 - завершить программу";

    public void start(DirFileCache cache, String dir) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(actions);
            int select = scanner.nextInt();
            if (select < 1 || select > 3) {
                System.out.println("Wrong input, you can select: 1 - 3");
                continue;
            }
            if (select == 3) {
                break;
            }
            System.out.println("Введите название файла:");
            String file = scanner.next();
            Path path = Path.of(dir, file);
            if (!Files.exists(Path.of(dir, file))) {
                throw new IllegalArgumentException("Неверное имя файла");
            }
            if (select == 1) {
                putToCache(cache, file);
            } else {
                getFromCache(cache, file);
            }
        }
    }

    private void putToCache(DirFileCache cache, String file) {
        cache.put(file, cache.load(file));
    }

    private void getFromCache(DirFileCache cache, String file) {
        System.out.println(cache.get(file));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите адрес кэшируемой директории:");
        String path = scanner.nextLine();
        if (!Files.isDirectory(Path.of(path))) {
            throw new IllegalArgumentException("Несуществующая директория");
        }
        DirFileCache cache = new DirFileCache(path);
        Emulator emulator = new Emulator();
        emulator.start(cache, path);
    }
}
