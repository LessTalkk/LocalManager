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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public abstract class VCardParser {
    protected final int mParseType;
    protected boolean mCanceled;
    private boolean mError;

    public VCardParser() {
        this(VCardConfig.PARSE_TYPE_UNKNOWN);
    }

    public VCardParser(int parseType) {
        mParseType = parseType;
    }

    public abstract boolean parse(InputStream is, VCardInterpreter interepreter)
            throws IOException, VCardException;

    /**
     * <P>
     * The method variants which accept charset.
     * </P>
     * <P>
     * RFC 2426 "recommends" (not forces) to use UTF-8, so it may be OK to use
     * UTF-8 as an encoding when parsing vCard 3.0. But note that some Japanese
     * phone uses Shift_JIS as a charset (e.g. W61SH), and another uses
     * "CHARSET=SHIFT_JIS", which is explicitly prohibited in vCard 3.0
     * specification (e.g. W53K).
     * </P>
     * 
     * @param is
     *            The source to parse.
     * @param charset
     *            Charset to be used.
     * @param builder
     *            The VCardBuilderBase object.
     * @return Returns true when successful. Otherwise returns false.
     * @throws IOException
     *             , VCardException
     */
    public abstract boolean parse(InputStream is, String charset,
            VCardInterpreter builder) throws IOException, VCardException;

    /**
     * The method variants which tells this object the operation is already
     * canceled.
     */
    public abstract void parse(InputStream is, String charset,
            VCardInterpreter builder, boolean canceled) throws IOException,
            VCardException;

    /**
     * 读取字符串
     */
    public abstract boolean parse(Reader is, VCardInterpreter builder) throws IOException,
            VCardException;

    /**
     * Cancel parsing. Actual cancel is done after the end of the current one
     * vcard entry parsing.
     */
    public void cancel() {
        mCanceled = true;
    }

    /**
     * error. Actual cancel is done after the end of the current one vcard entry
     * parsing.
     */
    public void setError(boolean flag) {
        mError = flag;
    }

    public boolean isSuccessful() {
        return !mError && !mCanceled;
    }

    public boolean hasError() {
        return mError;
    }

}
