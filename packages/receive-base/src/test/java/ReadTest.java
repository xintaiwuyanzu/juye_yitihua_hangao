import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class ReadTest {


    public static void main(String[] args) throws IOException {
        int start = 1024;
        int end = 20000;

        int size = 1024;
        int totalRead = (int) Math.ceil((end - start) / size);


        String filePath = "";
        RandomAccessFile raf = new RandomAccessFile(filePath, "r");

        OutputStream ops = new ByteArrayOutputStream();

        raf.seek(start);
        byte[] bytes = new byte[size];
        for (int i = 0; i < totalRead; i++) {
            int currentIndex = i * size;
            int readLength = size;
            if (currentIndex > totalRead) {
                readLength = totalRead - currentIndex + size;
            }
            raf.read(bytes, 0, readLength);
            ops.write(bytes, 0, readLength);
        }

    }

}
