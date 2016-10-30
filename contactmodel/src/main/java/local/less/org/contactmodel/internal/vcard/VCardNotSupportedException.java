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

/**
 * The exception which tells that the input VCard is probably valid from the
 * view of specification but not supported in the current framework for now.
 * 
 * This is a kind of a good news from the view of development. It may be good to
 * ask users to send a report with the VCard example for the future development.
 */
public class VCardNotSupportedException extends VCardException {
    /**
     * 
     */
    private static final long serialVersionUID = -5709468413771450320L;

    public VCardNotSupportedException() {
        super();
    }

    public VCardNotSupportedException(String message) {
        super(message);
    }
}