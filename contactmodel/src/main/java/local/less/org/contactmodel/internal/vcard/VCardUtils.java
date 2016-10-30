/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package local.less.org.contactmodel.internal.vcard;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import local.less.org.contactmodel.internal.ContactUtil;

/**
 * Utilities for VCard handling codes.
 */
public class VCardUtils {
    // Note that not all types are included in this map/set, since, for example,
    // TYPE_HOME_FAX is
    // converted to two parameter Strings. These only contain some minor fields
    // valid in both
    // vCard and current (as of 2009-08-07) Contacts structure.
    // private static final Set<String> sPhoneTypesUnknownToContactsSet;
    private static final Map<String, Integer> sKnownPhoneTypeMap_StoI;
    // private static final SparseArray<String> sKnownImPropNameMap_ItoS;

    static {
        sKnownPhoneTypeMap_StoI = new HashMap<String, Integer>();

        sKnownPhoneTypeMap_StoI.put(VCardConstants.PARAM_TYPE_CAR, 9);
        sKnownPhoneTypeMap_StoI.put(VCardConstants.PARAM_TYPE_PAGER, 6);
        sKnownPhoneTypeMap_StoI.put(VCardConstants.PARAM_TYPE_ISDN, 11);

        sKnownPhoneTypeMap_StoI.put(VCardConstants.PARAM_TYPE_HOME, 1);
        sKnownPhoneTypeMap_StoI.put(VCardConstants.PARAM_TYPE_WORK, 3);
        sKnownPhoneTypeMap_StoI.put(VCardConstants.PARAM_TYPE_CELL, 2);

        sKnownPhoneTypeMap_StoI.put(
                VCardConstants.PARAM_PHONE_EXTRA_TYPE_OTHER, 7);
        sKnownPhoneTypeMap_StoI.put(
                VCardConstants.PARAM_PHONE_EXTRA_TYPE_CALLBACK, 8);
        sKnownPhoneTypeMap_StoI.put(
                VCardConstants.PARAM_PHONE_EXTRA_TYPE_COMPANY_MAIN, 10);
        sKnownPhoneTypeMap_StoI.put(
                VCardConstants.PARAM_PHONE_EXTRA_TYPE_RADIO, 14);
        sKnownPhoneTypeMap_StoI.put(
                VCardConstants.PARAM_PHONE_EXTRA_TYPE_TTY_TDD, 16);
        sKnownPhoneTypeMap_StoI.put(
                VCardConstants.PARAM_PHONE_EXTRA_TYPE_ASSISTANT, 19);

        /*
        sPhoneTypesUnknownToContactsSet = new HashSet<String>();
        sPhoneTypesUnknownToContactsSet.add(VCardConstants.PARAM_TYPE_MODEM);
        sPhoneTypesUnknownToContactsSet.add(VCardConstants.PARAM_TYPE_MSG);
        sPhoneTypesUnknownToContactsSet.add(VCardConstants.PARAM_TYPE_BBS);
        sPhoneTypesUnknownToContactsSet.add(VCardConstants.PARAM_TYPE_VIDEO);

        sKnownImPropNameMap_ItoS = new SparseArray<String>();
        sKnownImPropNameMap_ItoS.put(0, VCardConstants.PROPERTY_X_AIM);
        sKnownImPropNameMap_ItoS.put(1, VCardConstants.PROPERTY_X_MSN);
        sKnownImPropNameMap_ItoS.put(2, VCardConstants.PROPERTY_X_YAHOO);
        sKnownImPropNameMap_ItoS.put(3, VCardConstants.PROPERTY_X_SKYPE_USERNAME);
        sKnownImPropNameMap_ItoS.put(5, VCardConstants.PROPERTY_X_GOOGLE_TALK);
        sKnownImPropNameMap_ItoS.put(6, VCardConstants.PROPERTY_X_ICQ);
        sKnownImPropNameMap_ItoS.put(7, VCardConstants.PROPERTY_X_JABBER);
        sKnownImPropNameMap_ItoS.put(4, VCardConstants.PROPERTY_X_QQ);
        sKnownImPropNameMap_ItoS.put(8, VCardConstants.PROPERTY_X_NETMEETING);
        */
    }

    /**
     * Returns Interger when the given types can be parsed as known type.
     * Returns String object when not, which should be set to label.
     */
    public static Object getPhoneTypeFromStrings(Collection<String> types, String number) {
        if (number == null) {
            number = "";
        }
        int type = -1;
        String label = null;
        boolean isFax = false;
        boolean hasPref = false;
        boolean isCell = false;
        boolean isPager = false;

        if (types != null) {
            for (String typeString : types) {
                if (typeString == null) {
                    continue;
                }
                typeString = typeString.toUpperCase(Locale.getDefault());
                if (typeString.equals(VCardConstants.PARAM_TYPE_PREF)) {
                    hasPref = true;
                } else if (typeString.equals(VCardConstants.PARAM_TYPE_FAX)) {
                    isFax = true;
                } else {
                    if (typeString.equals(VCardConstants.PARAM_TYPE_CELL)) {
                        isCell = true;
                    } else if (typeString
                            .equals(VCardConstants.PARAM_TYPE_PAGER)) {
                        isPager = true;
                    }

                    if (typeString.startsWith("X-") && type < 0) {
                        typeString = typeString.substring(2);
                    }
                    if (typeString.length() == 0) {
                        continue;
                    }
                    final Integer tmp = sKnownPhoneTypeMap_StoI.get(typeString);
                    if (tmp != null) {
                        final int typeCandidate = tmp;
                        // TYPE_PAGER is prefered when the number contains @
                        // surronded by
                        // a pager number and a domain name.
                        // e.g.
                        // o 1111@domain.com
                        // x @domain.com
                        // x 1111@
                        final int indexOfAt = number.indexOf("@");
                        if ((typeCandidate == 6 && 0 < indexOfAt && indexOfAt < number
                                .length() - 1) || type < 0 || type == 0) {
                            type = tmp;
                        }
                    } else if (type < 0) {
                        type = 0;
                        label = typeString;
                    }
                }
            }
        }
        if (type < 0) {
            if (hasPref) {
                type = 12;
            } else {
                // default to TYPE_HOME
                type = 1;
            }
        }

        if (isFax) {
            if (type == 1) {
                type = 5;
            } else if (type == 3) {
                type = 4;
            } else if (type == 7) {
                type = 13;
            }
        } else if (isCell && type == 3) {
            // TYPE_WORK_MOBILE
            type = 17;
        } else if (isPager
                && sKnownPhoneTypeMap_StoI.get(VCardConstants.PARAM_TYPE_WORK) != null) {
            // TYPE_WORK_PAGER
            type = 18;
        }

        // Fix unsupported types depending on SDK version.
        if (type == 0) {
            return label;
        } else {
            return type;
        }
    }

    public static Object getRelationTypeFromStrings(Collection<String> types, String name) {
        int type = -1;
        String lable = "";
        if (types != null) {
            for (String typeString : types) {
                if (typeString.equals("ASSISTANT")) {
                    type = 1;
                } else if (typeString.equals("BROTHER")) {
                    type = 2;
                } else if (typeString.equals("CHILD")) {
                    type = 3;
                } else if (typeString.equals("DOMESTIC_PARTNER")) {
                    type = 4;
                } else if (typeString.equals("FATHER")) {
                    type = 5;
                } else if (typeString.equals("FRIEND")) {
                    type = 6;
                } else if (typeString.equals("MANAGER")) {
                    type = 7;
                } else if (typeString.equals("MOTHER")) {
                    type = 8;
                } else if (typeString.equals("PARENT")) {
                    type = 9;
                } else if (typeString.equals("PARTNER")) {
                    type = 10;
                } else if (typeString.equals("REFERRED_BY")) {
                    type = 11;
                } else if (typeString.equals("RELATIVE")) {
                    type = 12;
                } else if (typeString.equals("SISTER")) {
                    type = 13;
                } else if (typeString.equals("SPOUSE")) {
                    type = 14;
                } else {
                    if (typeString.startsWith("X-") && type < 0) {
                        typeString = typeString.substring(2);
                    }
                }
            }
        }
        if (type == -1) {
            return lable;
        } else {
            return type;
        }
    }

    public static String[] sortNameElements(final int vcardType,
            final String familyName, final String middleName,
            final String givenName) {
        final String[] list = new String[3];
        final int nameOrderType = VCardConfig.getNameOrderType(vcardType);
        switch (nameOrderType) {
            case VCardConfig.NAME_ORDER_JAPANESE: {
                if (containsOnlyPrintableAscii(familyName)
                        && containsOnlyPrintableAscii(givenName)) {
                    list[0] = givenName;
                    list[1] = middleName;
                    list[2] = familyName;
                } else {
                    list[0] = familyName;
                    list[1] = middleName;
                    list[2] = givenName;
                }
                break;
            }
            case VCardConfig.NAME_ORDER_EUROPE: {
                list[0] = middleName;
                list[1] = givenName;
                list[2] = familyName;
                break;
            }
            default: {
                list[0] = givenName;
                list[1] = middleName;
                list[2] = familyName;
                break;
            }
        }
        return list;
    }

    public static String constructNameFromElements(final int vcardType,
            final String familyName, final String middleName,
            final String givenName) {
        return constructNameFromElements(vcardType, familyName, middleName,
                givenName, null, null);
    }

    public static String constructNameFromElements(final int vcardType,
            final String familyName, final String middleName,
            final String givenName, final String prefix, final String suffix) {
        final StringBuilder builder = new StringBuilder();
        final String[] nameList = sortNameElements(vcardType, familyName,
                middleName, givenName);
        boolean first = true;
        if (!ContactUtil.isEmpty(prefix)) {
            first = false;
            builder.append(prefix);
        }
        final int nameOrderType = VCardConfig.getNameOrderType(vcardType);
        for (final String namePart : nameList) {
            if (!ContactUtil.isEmpty(namePart)) {
                if (first) {
                    first = false;
                } else if (nameOrderType != VCardConfig.NAME_ORDER_JAPANESE) {
                    builder.append(' ');
                }
                builder.append(namePart);
            }
        }
        if (!ContactUtil.isEmpty(suffix)) {
            if (!first && nameOrderType != VCardConfig.NAME_ORDER_JAPANESE) {
                builder.append(' ');
            }
            builder.append(suffix);
        }
        return builder.toString();
    }

    public static List<String> constructListFromValue(final String value,
            final boolean isV30) {
        final List<String> list = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        int length = value.length();
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            if (ch == '\\' && i < length - 1) {
                char nextCh = value.charAt(i + 1);
                final String unescapedString = (isV30 ? VCardParser_V30
                        .unescapeCharacter(nextCh) : VCardParser_V21
                        .unescapeCharacter(nextCh));
                if (unescapedString != null) {
                    builder.append(unescapedString);
                    i++;
                } else {
                    builder.append(ch);
                }
            } else if (ch == ';') {
                list.add(builder.toString());
                builder = new StringBuilder();
            } else {
                builder.append(ch);
            }
        }
        list.add(builder.toString());
        return list;
    }

    public static boolean containsOnlyPrintableAscii(final String... values) {
        if (values == null) {
            return true;
        }
        return containsOnlyPrintableAscii(Arrays.asList(values));
    }

    public static boolean containsOnlyPrintableAscii(
            final Collection<String> values) {
        if (values == null) {
            return true;
        }
        for (final String value : values) {
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            if (!isPrintableAsciiOnly(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @hide
     */
    public static boolean isPrintableAscii(final char c) {
        final int asciiFirst = 0x20;
        final int asciiLast = 0x7E; // included
        return (asciiFirst <= c && c <= asciiLast) || c == '\r' || c == '\n';
    }

    /**
     * @hide
     */
    public static boolean isPrintableAsciiOnly(final CharSequence str) {
        final int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!isPrintableAscii(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private VCardUtils() {
    }
}
