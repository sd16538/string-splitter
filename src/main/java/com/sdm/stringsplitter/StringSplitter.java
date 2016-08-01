package com.sdm.stringsplitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

class StringSplitter {
    private final int rowLength;
    private static final int ROW_START = 0;
    private int breakpoint;

    StringSplitter(int rowLength) {
        this.rowLength = rowLength;
        this.breakpoint = this.rowLength - 1;
    }

    List<String> split(final String input) {
        if (isEmpty(input) || isBlank(input)) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        String remainder = input.trim();
        String substring;
        ValidBreakpoint validBreakpoint;
        do {
            validBreakpoint = getValidBreakpointForNewline(remainder);
            substring = remainder.substring(ROW_START, validBreakpoint.getBreakpoint());
            remainder = remainder.substring(substring.length()).trim();

            if (validBreakpoint.isHyphenated()) {
                result.add(substring.trim() + "-");
            } else {
                result.add(substring.trim());
            }
        } while (remainder.length() > rowLength);

        if (!remainder.isEmpty()) {
            result.add(remainder);
        }

        return result;
    }

    private ValidBreakpoint getValidBreakpointForNewline(String text) {
        ValidBreakpoint validBreakpoint;
        int textLength = text.length();

        if (textLength <= rowLength) {
            validBreakpoint = new ValidBreakpoint(textLength);
        } else if (isCharacterAtRowLengthValidStartOfNewline(text)) {
            validBreakpoint = new ValidBreakpoint(rowLength);
        } else if (isCharacterImmediatelyAfterRowLengthValidStartOfNewline(text)) {
            validBreakpoint = new ValidBreakpoint(rowLength + 1);
        } else {
            int indexOfLastSpace = text.substring(ROW_START, rowLength).lastIndexOf(' ');
            if (indexOfLastSpace > 0) {
                validBreakpoint = new ValidBreakpoint(indexOfLastSpace);
            } else if (textLength >= rowLength) {
                validBreakpoint = new ValidBreakpoint(breakpoint, true);
            } else {
                validBreakpoint = new ValidBreakpoint(textLength);
            }
        }
        return validBreakpoint;
    }

    private boolean isCharacterAtRowLengthValidStartOfNewline(String remainder) {
        return remainder.charAt(breakpoint) == ' ';
    }

    private boolean isCharacterImmediatelyAfterRowLengthValidStartOfNewline(String remainder) {
        return remainder.charAt(rowLength) == ' ';
    }

    private static class ValidBreakpoint {
        private final int breakpoint;
        private final boolean hyphenated;

        ValidBreakpoint(int breakpoint) {
            this.breakpoint = breakpoint;
            this.hyphenated = false;
        }

        ValidBreakpoint(int breakpoint, boolean hyphenated) {
            this.breakpoint = breakpoint;
            this.hyphenated = hyphenated;
        }

        int getBreakpoint() {
            return breakpoint;
        }

        boolean isHyphenated() {
            return hyphenated;
        }
    }
}
