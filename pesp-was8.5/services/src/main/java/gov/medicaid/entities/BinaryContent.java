/*
 * Copyright 2012-2013 TopCoder, Inc.
 *
 * This code was developed under U.S. government contract NNH10CD71C. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.medicaid.entities;

import java.sql.Blob;

/**
 * Stores BLOB contents.
 *
 * @author TCSASSEMBLER
 * @version 1.0
 */
public class BinaryContent {

    /**
     * The binary content.
     */
    private Blob content;

    /**
     * The content id.
     */
    private String contentId;

    /**
     * Empty constructor.
     */
    public BinaryContent() {
    }

    /**
     * Gets the value of the field <code>content</code>.
     *
     * @return the content
     */
    public Blob getContent() {
        return content;
    }

    /**
     * Sets the value of the field <code>content</code>.
     *
     * @param content the content to set
     */
    public void setContent(Blob content) {
        this.content = content;
    }

    /**
     * Gets the value of the field <code>contentId</code>.
     *
     * @return the contentId
     */
    public String getContentId() {
        return contentId;
    }

    /**
     * Sets the value of the field <code>contentId</code>.
     *
     * @param contentId the contentId to set
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
