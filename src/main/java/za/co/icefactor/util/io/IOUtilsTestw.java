package za.co.icefactor.util.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by justice on 2017/02/09.
 */
public class IOUtilsTestw {

//    @Test(expected = RuntimeException.class)
    @Test
    public void testInvaliddirectory(){
        String[] filesInDirectory = IOUtils.getFilesInDirectory("/home/justice/bup/az", (name) -> fileFilter(name));
        System.out.println(filesInDirectory);
        //        Assert.assertEquals(0, ds.size());
    }

    @Test
    public void testInvaliddirectorsy(){
        String[] filesInDirectory = IOUtils.getFilesInDirectory("/home/justice/bup/azw", (name) -> fileFilter(name));
        System.out.println(filesInDirectory);
        //        Assert.assertEquals(0, ds.size());
    }

    private boolean fileFilter(String name) {
        return name != null && ((name.toLowerCase().endsWith("xlsx")) || (name.toLowerCase().endsWith("xls")));
    }

}