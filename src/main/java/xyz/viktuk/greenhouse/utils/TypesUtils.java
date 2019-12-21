package xyz.viktuk.greenhouse.utils;

import java.math.BigInteger;

public class TypesUtils {
    public static BigInteger getBigIntegerByString(String string) {
        try {
            return new BigInteger(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
