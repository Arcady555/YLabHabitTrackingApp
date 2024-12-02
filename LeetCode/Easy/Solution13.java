package Easy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.
 *
 * Symbol       Value
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 * For example, 2 is written as II in Roman numeral, just two ones added together. 12 is written as XII, which is simply X + II. The number 27 is written as XXVII, which is XX + V + II.
 *
 * Roman numerals are usually written largest to smallest from left to right. However, the numeral for four is not IIII. Instead, the number four is written as IV. Because the one is before the five we subtract it making four. The same principle applies to the number nine, which is written as IX. There are six instances where subtraction is used:
 *
 * I can be placed before V (5) and X (10) to make 4 and 9.
 * X can be placed before L (50) and C (100) to make 40 and 90.
 * C can be placed before D (500) and M (1000) to make 400 and 900.
 * Given a roman numeral, convert it to an integer.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "III"
 * Output: 3
 * Explanation: III = 3.
 * Example 2:
 *
 * Input: s = "LVIII"
 * Output: 58
 * Explanation: L = 50, V= 5, III = 3.
 * Example 3:
 *
 * Input: s = "MCMXCIV"
 * Output: 1994
 * Explanation: M = 1000, CM = 900, XC = 90 and IV = 4.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 15
 * s contains only the characters ('I', 'V', 'X', 'L', 'C', 'D', 'M').
 * It is guaranteed that s is a valid roman numeral in the range [1, 3999].
 */
class Solution13 {
    public int romanToInt(String s) {
        Map<String, Integer> romanMap = Map.ofEntries(
                Map.entry("M", 1000),
                Map.entry("CM", 900),
                Map.entry("D", 500),
                Map.entry("CD", 400),
                Map.entry("C", 100),
                Map.entry("XC", 90),
                Map.entry("L", 50),
                Map.entry("XL", 40),
                Map.entry("X", 10),
                Map.entry("IX", 9),
                Map.entry("V", 5),
                Map.entry("IV", 4),
                Map.entry("I", 1)
        );
        int rsl = 0;
        String[] array = s.split("");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
            if (i > 0 && romanMap.get(array[i]) > romanMap.get(array[i - 1])) {
                list.remove(array[i]);
                list.remove(array[i - 1]);
                list.add(array[i - 1] + array[i]);
            }
        }
        for (String element : list) {
            for (String mapElement : romanMap.keySet()) {
                if (element.equals(mapElement)) {
                    rsl = rsl + romanMap.get(mapElement);
                }
            }
        }
        for (String element : list) {
            System.out.println(element);
        }
        System.out.println(rsl);
        return rsl;
    }
}
