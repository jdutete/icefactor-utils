package za.co.icefactor.util.io;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Useful IO utils
 *
 * @since 1.0
 */

/**
 * Created by justice on 2014/01/09.
 */
public class IOUtils {

    //test
    private static final Logger LOGGER = Logger.getLogger(IOUtils.class.getName());

    //All public methods are static.. no initialization
    private IOUtils(){}

    /**
     *
     * @param location
     * @param filterFn
     * @return
     */
    public static String[] getFilesInDirectory(String location, Function<String, Boolean> filterFn){
        final Path path = Paths.get(location);
        String[] filesInPath = path.toFile()
                .list((d,f) -> filterFn.apply(f));
        return filesInPath;
    }

    /**
     * Returns the list of files in the supplied directory, filtered by the supplied FileNameFilter supplier
     *
     * @param   location
     *          the file location
     * @param   filterSupplier
     *          {@link FilenameFilter}  supplier
     * @return
     */
    public static String[] getFilesInDirectory(String location, Supplier<FilenameFilter> filterSupplier) {
        final Path path = Paths.get(location);
        return path.toFile()
                .list((a, b) -> filterSupplier.get().accept(a, b));
    }
    /**
     * Retrieves the files in the supplied directory matching the supplied filter
     * @param   location
     *          directory to look for the files
     *
     * @param   filterFn
     *          function used to filter the files
     *
     * @return  the resulting list of {@code File}s
     *
     * @throws InvalidPathException
     *          if the path string cannot be converted to a {@code Path}
     */
    public static List<File> getFilesInDDirectory(String location, Function<String, Boolean> filterFn){

        List<File> files = Arrays.stream(getFilesInDirectory(location, filterFn))
                .map(a -> Paths.get(location, a).toFile())
                .collect(Collectors.toList());
        return files;
    }

    /**
     * Read a file and return the bytes
     * @param   filePath
     *          the file {@code Path}
     *
     * @return  the byte array
     */
    public static byte[] readFileAsBytes(Path filePath){

        byte[] data;
        try {
            data = Files.readAllBytes(filePath);
            LOGGER.info("File size (bytes): " + data.length);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to read file: " + filePath.getFileName(), e));
        }
        return data;
    }

    /**
     * Reads the file and returns an ArrayList containing the blocks
     *
     * @param   filePath
     *          the file {@code Path}
     *
     * @param   blockSize
     *          the maximum size to be read in each block (in bytes)
     *
     * @return  list containing all the read blocks
     */
    public static List<byte[]> readFileInBlocks(Path filePath, int blockSize){
        final byte[] bytes = readFileAsBytes(filePath);
        return getBytesInBlocks(blockSize, bytes);
    }

    /**
     * reads the supplied {@code bytes} in chunks of size {@code blockSize} into the returned ArrayList
     * @param   blockSize
     *          the maximum size to be read in each block (in bytes)
     * @param   bytes
     *          the data to be read into the byte blocks/chunks
     * @return
     */
    public static List<byte[]> getBytesInBlocks(int blockSize, byte[] bytes) {
        int fileSize = bytes.length;
        boolean loop = true;
        int i = 1;
        List<byte[]> dataList = new ArrayList<>();
        while(loop){
            int copyFrom = (i * blockSize) - blockSize;
            int copyTo = copyFrom + blockSize;
            if(copyTo > fileSize){
                copyTo = fileSize;
                loop = false;
            }
            final byte[] b = Arrays.copyOfRange(bytes, copyFrom, copyTo);
            dataList.add(b);
            i++;
        }
        return dataList;
    }

    /**
     * Reads all the lines in the file and returns them in a list
     *
     * @param   path
     *          the {@code Path} to the file
     *
     * @return  List of Strings
     */
    public static List<String> readFile(Path path){
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to read file: %s", path.getFileName(), e));
        }
    }
}
