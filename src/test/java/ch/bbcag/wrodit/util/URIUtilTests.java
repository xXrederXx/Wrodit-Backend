package ch.bbcag.wrodit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class URIUtilTests {
    @Test
    void checkURI_whenJoinParts_thenValidURI()
    {
        Assertions.assertEquals("hallo/welt", URIHelper.join("hallo", "welt"));
    }
}
