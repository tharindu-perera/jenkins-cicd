 import org.junit.Test;
 import static org.junit.Assert.*;

import java.io.IOException;

public class MangaServiceUnitTest {

    @Test
    public void testGetMangasByTitle() throws IOException {
        User user = new User("tharindu123");
//            user.setName("tharindu");
        assertEquals("tharindu" ,user.getName());
        assertEquals("tharindu" ,user.getName());

    }

}
