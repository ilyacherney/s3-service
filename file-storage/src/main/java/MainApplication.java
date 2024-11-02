import java.io.IOException;

public class MainApplication {
    public static void main(String[] args) throws IOException {
        FileStorage fileStorage = new FileStorage();

        fileStorage.saveFile("bucket2", "file2.txt", new byte[] {12, 42});

        byte[] bytes = fileStorage.getFile("bucket2", "file2.txt");
        for (Byte el : bytes) {
            System.out.println((char)(int) el);
        }

        fileStorage.deleteFile("bucket1", "file1.txt");
    }
}
