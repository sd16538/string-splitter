package com.sdm.stringsplitter;

import org.apache.commons.lang3.text.WordUtils;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class StringSplitterTest {

    private StringSplitter splitter = new StringSplitter(10);

    @Test
    public void shouldReturnEmptyListWhenGivenNullString() {
        List<String> splitStrings = splitter.split(null);
        assertThat(splitStrings, is(empty()));
    }

    @Test
    public void shouldReturnEmptyListWhenGivenBlankString() {
        List<String> splitStrings = splitter.split("");
        assertThat(splitStrings, is(empty()));
    }

    @Test
    public void shouldReturnListWithSameStringWhenInputLengthLessThanLimit() {
        List<String> splitStrings = splitter.split("Hello you");
        assertThat(splitStrings.size(), is(1));
        assertThat(splitStrings, contains("Hello you"));
    }

    @Test
    public void shouldReturnListWithSplitsBasedOnSpecifiedLimit() {
        StringSplitter splitter = new StringSplitter(5);
        List<String> splitStrings = splitter.split("Hello you");
        assertThat(splitStrings.size(), is(2));
        assertThat(splitStrings, contains("Hello", "you"));
    }

    @Test
    public void shouldReturnListWithTwoStringsWhenInputLengthExceedsLimitByOneCharacter() {
        List<String> splitStrings = splitter.split("abcdefghik l");
        assertThat(splitStrings.size(), is(2));
        assertThat(splitStrings, contains("abcdefghik", "l"));
    }

    @Test
    public void shouldReturnListWithFirstStringCutAtSpace() {
        List<String> splitStrings = splitter.split("abcdef ghijklmn");
        assertThat(splitStrings.size(), is(2));
        assertThat(splitStrings, contains("abcdef", "ghijklmn"));
    }

    @Test
    public void shouldReturnListWithThreeStringsWhenInputLengthIsThreeTimesLimit() {
        List<String> splitStrings = splitter.split("abcdefghik lmnopqrstu vwxyz");
        assertThat(splitStrings.size(), is(3));
        assertThat(splitStrings, contains("abcdefghik", "lmnopqrstu", "vwxyz"));
    }

    @Test
    public void shouldSplitStringWithHyphenWhenNoValidBreakpoints() {
        List<String> splitStrings = new StringSplitter(6).split("breakfast");
        assertThat(splitStrings.size(), is(2));
        assertThat(splitStrings, contains("break-", "fast"));
    }

    @Test
    public void shouldSplitStringAfterTrimmingExtraSpaces() {
        String reallyLongString = "aaaaaaaaaaa asojdsj sdfsdfijdjfo sdfisifj " +
                "ssssssssssssssssssssssssssssssssssssssssssssssssssssssss sdfsdfsdfsdfsdfsd ijjirej sdjf 1 1 1 1      " +
                "1         1111111   3333 z                                             ";
        List<String> splits = new StringSplitter(49).split(reallyLongString);
        assertThat(splits, contains(
                "aaaaaaaaaaa asojdsj sdfsdfijdjfo sdfisifj",
                "ssssssssssssssssssssssssssssssssssssssssssssssss-",
                "ssssssss sdfsdfsdfsdfsdfsd ijjirej sdjf 1 1 1 1",
                "1         1111111   3333 z"
        ));
    }
}
