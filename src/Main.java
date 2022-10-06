import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        System.out.println("Введите путь к стартовой директории: ");
        Scanner in = new Scanner(System.in);
        String startDirectory = in.nextLine();
        System.out.println("Введите путь к файлу: ");
        String startFileDirectory = in.nextLine();
        String testCheckSum = "";
        File file = new File(startDirectory);
        File startFile = new File (startFileDirectory);
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        testCheckSum = getFileChecksum(md5Digest, startFile);
        System.out.println("Результат вычисления контрольной суммы файла: " + testCheckSum);
        System.out.println("Результат поиска по контрольной сумме:");
        goThroughFilesAndDirectories(file, testCheckSum );
    }
    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        FileInputStream fis = new FileInputStream(file);

        //Буфер
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //return complete hash
        return sb.toString();
    }

    public static void goThroughFilesAndDirectories (File f, String checkSum) throws NoSuchAlgorithmException, IOException {

        File[] files = f.listFiles();
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        for(File p:files){
            if (!p.isDirectory ()) {
                String check = getFileChecksum(md5Digest, p);
                if (check.equals(checkSum) ) System.out.println(p.getAbsolutePath());
            }
            if (p.isDirectory ()) {
                try {
                    goThroughFilesAndDirectories(p, checkSum);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}