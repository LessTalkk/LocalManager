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

import android.os.Build;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import local.less.org.base.util.Base64;
import local.less.org.base.util.QuotedPrintableCodec;
import local.less.org.contactmodel.internal.bean.ContactInfo;

/**
 * Store the parse result to custom datastruct: VNode, PropertyNode Maybe
 * several vcard instance, so use vNodeList to store. VNode: standy by a vcard
 * instance. PropertyNode: standy by a property line of a card.
 * 
 * Previously used in main vCard handling code but now exists only for testing.
 */
public class VNodeBuilder implements VCardInterpreter {
    private static String LOG_TAG = "VNodeBuilder";

    /**
     * If there's no other information available, this class uses this charset
     * for encoding byte arrays to String.
     */
    /* package */static final String DEFAULT_CHARSET_FOR_DECODED_BYTES = "UTF-8";

    private ContactInfo.Property mCurrentProperty = new ContactInfo.Property();
    private ContactInfo mCurrentContact;
    private String mParamType;

    /**
     * The charset using which {@link VCardInterpreter} parses the text.
     */
    private String mInputCharset;

    /**
     * The charset with which byte array is encoded to String.
     */
    final private String mCharsetForDecodedBytes;
    final private boolean mStrictLineBreakParsing;
    final private int mVCardType;
    final private String mAccountName;
    final private String mAccountType;

    /** For measuring performance. */
    //    private long mTimePushIntoContentResolver;

    final private List<VCardEntryHandler> mEntryHandlers = new ArrayList<VCardEntryHandler>();

    //    public VNodeBuilder() {
    //        this(null, null, false, VCardConfig.VCARD_TYPE_V21_GENERIC_UTF8, null, null);
    //    }

    public VNodeBuilder(final int vcardType) {
        this(null, null, false, vcardType, null, null);
    }

    //    public VNodeBuilder(final String charset,
    //            final boolean strictLineBreakParsing, final int vcardType,
    //            final String accountName, final String accountType) {
    //        this(null, charset, strictLineBreakParsing, vcardType, accountName,
    //                accountType);
    //    }

    public VNodeBuilder(final String inputCharset,
            final String charsetForDetodedBytes,
            final boolean strictLineBreakParsing, final int vcardType,
            final String accountName, final String accountType) {
        if (inputCharset != null) {
            mInputCharset = inputCharset;
        } else {
            mInputCharset = VCardConfig.DEFAULT_CHARSET;
        }
        if (charsetForDetodedBytes != null) {
            mCharsetForDecodedBytes = charsetForDetodedBytes;
        } else {
            mCharsetForDecodedBytes = DEFAULT_CHARSET_FOR_DECODED_BYTES;
        }
        mStrictLineBreakParsing = strictLineBreakParsing;
        mVCardType = vcardType;
        mAccountName = accountName;
        mAccountType = accountType;
    }

    public void addEntryHandler(VCardEntryHandler entryHandler) {
        mEntryHandlers.add(entryHandler);
    }

    public void clearEntryHandler() {
        mEntryHandlers.clear();
    }

    @Override
    public void start() {
        for (VCardEntryHandler entryHandler : mEntryHandlers) {
            entryHandler.onStart();
        }
    }

    @Override
    public void end() {
        for (VCardEntryHandler entryHandler : mEntryHandlers) {
            entryHandler.onEnd();
        }
    }

    /**
     * Assume that VCard is not nested. In other words, this code does not
     * accept
     */
    @Override
    public void startEntry() {
        if (mCurrentContact != null) {
            if (DevEnv.bBackupDebug)
                Log.e(LOG_TAG, "Nested VCard code is not supported now.");
        }
        mCurrentContact = new ContactInfo(mVCardType, mAccountName, mAccountType);
    }

    @Override
    public void endEntry() {
        mCurrentContact.consolidateFields();
        for (VCardEntryHandler entryHandler : mEntryHandlers) {
            entryHandler.onContactCreated(mCurrentContact);
        }
        mCurrentContact = null;
    }

    @Override
    public void startProperty() {
        mCurrentProperty.clear();
    }

    @Override
    public void endProperty() {
        mCurrentContact.addProperty(mCurrentProperty);
    }

    @Override
    public void propertyName(String name) {
        mCurrentProperty.setPropertyName(name);
    }

    @Override
    public void propertyGroup(String group) {
    }

    @Override
    public void propertyParamType(String type) {
        if (mParamType != null) {
            if (DevEnv.bBackupDebug)
                Log.e(LOG_TAG, "propertyParamType() is called more than once " + "before propertyParamValue() is called");
        }
        mParamType = type;
    }

    @Override
    public void propertyParamValue(String value) {
        if (mParamType == null) {
            // From vCard 2.1 specification. vCard 3.0 formally does not allow
            // this case.
            mParamType = "TYPE";
        }
        mCurrentProperty.addParameter(mParamType, value);
        mParamType = null;
    }

    private String encodeString(String originalString,
            String charsetForDecodedBytes) {
        if (mInputCharset.equalsIgnoreCase(charsetForDecodedBytes)) {
            return originalString;
        }
        Charset charset = Charset.forName(mInputCharset);
        ByteBuffer byteBuffer = charset.encode(originalString);
        // byteBuffer.array() "may" return byte array which is larger than
        // byteBuffer.remaining(). Here, we keep on the safe side.
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        try {
            return new String(bytes, charsetForDecodedBytes);
        } catch (UnsupportedEncodingException e) {
            if (DevEnv.bBackupDebug)
                Log.e(LOG_TAG, "Failed to encode: charset=" + charsetForDecodedBytes);
            return null;
        }
    }

    private String handleOneValue(String value, String charsetForDecodedBytes,
            String encoding) {
        if (encoding != null) {
            if (encoding.equals("BASE64") || encoding.equals("B")) {
                mCurrentProperty.setPropertyBytes(Base64.decodeBase64(value.getBytes()));
                return new String(Base64.decodeBase64(value.getBytes()));
            } else if (encoding.equals("QUOTED-PRINTABLE")) {
                // "= " -> " ", "=\t" -> "\t".
                // Previous code had done this replacement. Keep on the safe
                // side.
                StringBuilder builder = new StringBuilder();
                int length = value.length();
                for (int i = 0; i < length; i++) {
                    char ch = value.charAt(i);
                    if (ch == '=' && i < length - 1) {
                        char nextCh = value.charAt(i + 1);
                        if (nextCh == ' ' || nextCh == '\t') {
                            builder.append(nextCh);
                            i++;
                            continue;
                        }
                    }
                    builder.append(ch);
                }
                String quotedPrintable = builder.toString();

                String[] lines;
                if (mStrictLineBreakParsing) {
                    lines = quotedPrintable.split("\r\n");
                } else {
                    builder = new StringBuilder();
                    length = quotedPrintable.length();
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < length; i++) {
                        char ch = quotedPrintable.charAt(i);
                        if (ch == '\n') {
                            list.add(builder.toString());
                            builder = new StringBuilder();
                        } else if (ch == '\r') {
                            list.add(builder.toString());
                            builder = new StringBuilder();
                            if (i < length - 1) {
                                char nextCh = quotedPrintable.charAt(i + 1);
                                if (nextCh == '\n') {
                                    i++;
                                }
                            }
                        } else {
                            builder.append(ch);
                        }
                    }
                    String finalLine = builder.toString();
                    if (finalLine.length() > 0) {
                        list.add(finalLine);
                    }
                    lines = list.toArray(new String[0]);
                }

                builder = new StringBuilder();
                for (String line : lines) {
                    if (line.endsWith("=")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    builder.append(line);
                }
                byte[] bytes;
                try {
                    bytes = builder.toString().getBytes(mInputCharset);
                } catch (UnsupportedEncodingException e1) {
                    if (DevEnv.bBackupDebug)
                        Log.e(LOG_TAG, "Failed to encode: charset=" + mInputCharset);
                    bytes = builder.toString().getBytes();
                }

                try {
                    bytes = QuotedPrintableCodec.decodeQuotedPrintable(bytes);
                } catch (Exception e) {
                    if (DevEnv.bBackupDebug)
                        Log.e(LOG_TAG, "Failed to decode quoted-printable: " + e);
                    return "";
                }

                try {
                    return new String(bytes, charsetForDecodedBytes);
                } catch (UnsupportedEncodingException e) {
                    if (DevEnv.bBackupDebug)
                        Log.e(LOG_TAG, "Failed to encode: charset=" + charsetForDecodedBytes);
                    return new String(bytes);
                }
            }
            // Unknown encoding. Fall back to default.
        }
        return encodeString(value, charsetForDecodedBytes);
    }

    @Override
    public void propertyValues(List<String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }

        final Collection<String> charsetCollection = mCurrentProperty.getParameters("CHARSET");
        final String charset = ((charsetCollection != null) ? charsetCollection.iterator().next() : null);
        final Collection<String> encodingCollection = mCurrentProperty.getParameters("ENCODING");
        final String encoding = ((encodingCollection != null) ? encodingCollection.iterator().next() : null);

        String charsetForDecodedBytes = nameForDefaultVendor(charset);
        if (charsetForDecodedBytes == null || charsetForDecodedBytes.length() == 0) {
            charsetForDecodedBytes = mCharsetForDecodedBytes;
        }

        for (final String value : values) {
            mCurrentProperty.addToPropertyValueList(handleOneValue(value, charsetForDecodedBytes, encoding));
        }
    }

    /**
     * Returns the name of the vendor-specific character set corresponding to
     * the given original character set name and vendor. If there is no
     * vendor-specific character set for the given name/vendor pair, this
     * returns the original character set name. The vendor name is matched
     * case-insensitively.
     * 
     * @param charsetName
     *            the base character set name
     * @param vendor
     *            the vendor to specialize for
     * @return the specialized character set name, or {@code charsetName} if
     *         there is no specialized name
     */
    public static String nameForVendor(String charsetName, String vendor) {
        // TODO: Eventually, this may want to be table-driven.

        if (vendor.equalsIgnoreCase(VENDOR_DOCOMO) && isShiftJis(charsetName)) {
            return "docomo-shift_jis-2007";
        }

        return charsetName;
    }

    /**
     * Returns the name of the vendor-specific character set corresponding to
     * the given original character set name and the default vendor (that is,
     * the targeted vendor of the device this code is running on). This method
     * merely calls through to {@link #nameForVendor(String,String)}, passing
     * the default vendor as the second argument.
     * 
     * @param charsetName
     *            the base character set name
     * @return the specialized character set name, or {@code charsetName} if
     *         there is no specialized name
     */
    public static String nameForDefaultVendor(String charsetName) {
        return nameForVendor(charsetName, getDefaultVendor());
    }

    /**
     * Returns whether the given character set name indicates the Shift-JIS
     * encoding. Returns false if the name is null.
     * 
     * @param charsetName
     *            the character set name
     * @return {@code true} if the name corresponds to Shift-JIS or
     *         {@code false} if not
     */
    private static boolean isShiftJis(String charsetName) {
        // Bail quickly if the length doesn't match.
        if (charsetName == null) {
            return false;
        }
        int length = charsetName.length();
        if (length != 4 && length != 9) {
            return false;
        }

        return charsetName.equalsIgnoreCase("shift_jis")
                || charsetName.equalsIgnoreCase("shift-jis")
                || charsetName.equalsIgnoreCase("sjis");
    }

    /**
     * Gets the default vendor for this build.
     * 
     * @return the default vendor name
     */
    private static String getDefaultVendor() {
        return Build.BRAND;
    }

    /**
     * name of the vendor "Docomo". <b>Note:</b> This isn't a public constant,
     * in order to keep this class from becoming a de facto reference list of
     * vendor names.
     */
    private static final String VENDOR_DOCOMO = "docomo";
}
