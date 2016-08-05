package com.robohorse.gpversionchecker;

import com.robohorse.gpversionchecker.domain.Version;
import com.robohorse.gpversionchecker.helper.DataParser;
import com.robohorse.gpversionchecker.utils.AssetsReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


/**
 * Created by vadim on 17.07.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DataParserTest {
    private static final String URL = "https://play.google.com/store/apps/details?id=com.robohorse.gpversionchecker";
    private AssetsReader assetsReader = new AssetsReader();
    private DataParser dataParser = new DataParser();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDataParsing_isCorrectOnLowerUserVersion() throws Exception {
        final String currentVersion = "1";
        String htmlBody = assetsReader.read("sample_app.html");
        Document document = Jsoup.parse(htmlBody);
        final Version version = dataParser.parse(document, currentVersion, URL);

        assertNotNull("VersionVO is empty", version);
        assertTrue("App update is not required, but should be", version.isNeedToUpdate());
    }

    @Test
    public void testDataParsing_isCorrectOnHigherUserVersion() throws Exception {
        final String currentVersion = "1.9.49";
        String htmlBody = assetsReader.read("sample_app.html");
        Document document = Jsoup.parse(htmlBody);
        final Version version = dataParser.parse(document, currentVersion, URL);

        assertNotNull("VersionVO is empty", version);
        assertFalse("App update is required, but should be not", version.isNeedToUpdate());
    }

    @Test
    public void testDataParsing_isCorrectOnSameUserVersion() throws Exception {
        final String currentVersion = "1.9.48";
        String htmlBody = assetsReader.read("sample_app.html");
        Document document = Jsoup.parse(htmlBody);
        final Version version = dataParser.parse(document, currentVersion, URL);

        assertNotNull("VersionVO is empty", version);
        assertFalse("App update is required, but should be not", version.isNeedToUpdate());
    }
}

