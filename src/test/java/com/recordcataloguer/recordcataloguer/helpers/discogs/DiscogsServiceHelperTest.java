package com.recordcataloguer.recordcataloguer.helpers.discogs;

import info.debatty.java.stringsimilarity.SorensenDice;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class DiscogsServiceHelperTest {

    @Test
    public void stringComparisonShouldBeAccurate() {
        SorensenDice sorensenDice = new SorensenDice();

        double test = sorensenDice.similarity("The Mothers - Uncle Meat".toLowerCase(Locale.ROOT), "THE MOTHERS OF INVENTION UNCLE MEAT / 2024".toLowerCase(Locale.ROOT));
        System.out.println(test);

    }
}
