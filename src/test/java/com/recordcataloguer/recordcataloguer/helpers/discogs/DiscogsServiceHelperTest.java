package com.recordcataloguer.recordcataloguer.helpers.discogs;

import info.debatty.java.stringsimilarity.SorensenDice;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Locale;

public class DiscogsServiceHelperTest {

    @Test
    public void stringComparisonShouldBeAccurate() {
        SorensenDice sorensenDice = new SorensenDice();

        double test = sorensenDice.similarity("The Mothers - Uncle Meat".toLowerCase(Locale.ROOT), "THE MOTHERS OF INVENTION UNCLE MEAT / 2024".toLowerCase(Locale.ROOT));
        System.out.println(test);

    }

    @Test
    public void test() {
       boolean t = false;
        System.out.println(t);
       if(!t) System.out.println(t);
       else {
           System.out.println("fail");
       }

    }

    @Test
    public void splitPathVariable() {
        String path =
"/Users/david/.nvm/versions/node/v19.6.1/bin:/opt/homebrew/bin:/opt/homebrew/sbin:/usr/local/bin:/System/Cryptexes/App/usr/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Library/Apple/usr/bin:/Users/david/.nvm/versions/node/v19.6.1/bin:/opt/homebrew/bin:/opt/homebrew/sbin:/Users/david:/Library/Android/sdk/platform-tools:/Library/Android/sdk/tools:/Library/Android/sdk/tools/bin:/Library/Android/sdk/emulator:/Users/david/.rvm/bin:/Users/david:/Library/Android/sdk/platform-tools:/Users/david:/Library/Android/sdk/tools:/Users/david:/Library/Android/sdk/tools/bin:/Users/david:/Library/Android/sdk/emulator:/Users/david/.rvm/bin";
        String[] spl = StringUtils.split(path, ":");
        Arrays.stream(spl).forEach(s -> System.out.println(s));

    }
}
