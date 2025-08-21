import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates IBANs according to ISO 13616 (mod-97), including country-specific length checks.
 */
public class IBANChecker {

    /** Map from country code (ISO alpha-2) to expected IBAN length. */
    private static final Map<String, Integer> chars = new HashMap<>();

    static {
        chars.put("AT", 20);
        chars.put("BE", 16);
        chars.put("CZ", 24);
        chars.put("DE", 22);
        chars.put("DK", 18);
        chars.put("FR", 27);
    }

    public static void main(String[] args) {
        String iban = "DE227902007600279131";
        System.out.println("Welcome to the IBAN Checker!");
        System.out.println("IBAN " + iban + " is " + validate(iban));
    }

    /**
     * Validates an IBAN.
     * @param iban IBAN in the form "<CC><check digits><BBAN>" (no spaces)
     * @return {@code true} if country/length match and the mod-97 result is 1, otherwise {@code false}
     */
    public static boolean validate(String iban) {

        if (!checkLength(iban)) {
            return false;
        }
        String rearrangedIban = rearrangeIban(iban);
        String convertedIban = convertToInteger(rearrangedIban);
        List<String> segments = createSegments(convertedIban);

        return calculate(segments) == 1;
    }

    /**
     * Reduces the digit blocks modulo 97.
     * @param segments substrings produced by createSegments
     * @return remainder of division by 97 (1 means valid)
     */
    private static int calculate(List<String> segments) {
        long n =0;

        for (String segment : segments) {
            if(segment.length() == 9) {
                n = Long.parseLong(segment)%97;
            }
            else {
                segment = n+ segment;
                n = Long.parseLong(segment)%97;
            }
        }
        return (int)n;
    }

    /** Checks whether the country code is known and the length matches exactly. */
    private static boolean checkLength(String iban) {
        String countryCode = iban.substring(0, 2);
        return chars.containsKey(countryCode) && chars.get(countryCode) == iban.length();
    }

    /** Replaces letters A–Z with 10–35 and normalizes case. */
    private static String convertToInteger(String iban) {
        StringBuilder convertedIban = new StringBuilder();
        String upperIban = iban.toUpperCase();

        for (char c : upperIban.toCharArray()) {
            if (Character.isDigit(c)) {
                convertedIban.append(c);
            }
            if (Character.isLetter(c)) {
                convertedIban.append(c - 55);
            }
        }

        return convertedIban.toString();
    }

    /** Splits the number into manageable segments for the modulo calculation. */
    private static List<String> createSegments(String iban) {
        List<String> segments = new ArrayList<>();
        String  remainingIban = iban;

        segments.add(remainingIban.substring(0, 9));
        remainingIban = remainingIban.substring(9);

        while (remainingIban.length() >= 9) {
            segments.add(remainingIban.substring(0, 7));
            remainingIban = remainingIban.substring(7);
        }

        segments.add(remainingIban);
        return segments;
    }

    private static String rearrangeIban(String iban) {
        return iban.substring(4) + iban.substring(0, 4);
    }



}