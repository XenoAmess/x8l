/*
 * MIT License
 *
 * Copyright (c) 2020 XenoAmess
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.xenoamess.x8l.dealers;

import com.xenoamess.x8l.AbstractTreeNode;
import com.xenoamess.x8l.X8lGrammarException;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.jetbrains.annotations.NotNull;

/**
 * <p>AbstractLanguageDealerHandler interface.</p>
 *
 * @author XenoAmess
 * @version 2.2.3-SNAPSHOT
 */
public interface AbstractLanguageDealerHandler<T extends AbstractTreeNode> extends Serializable {

    /**
     * read AbstractTreeNode
     *
     * @param reader reader
     * @param t      AbstractTreeNode to read
     * @return if read succeed
     * @throws java.io.IOException reader.read
     * @throws com.xenoamess.x8l.X8lGrammarException when grammar wrong
     */
    boolean read(@NotNull Reader reader, @NotNull T t) throws IOException, X8lGrammarException;

    /**
     * write AbstractTreeNode
     *
     * @param writer writer
     * @param t      AbstractTreeNode to write
     * @return if write succeed
     * @throws java.io.IOException writer.write
     * @throws com.xenoamess.x8l.X8lGrammarException when grammar wrong
     */
    boolean write(@NotNull Writer writer, @NotNull T t) throws IOException, X8lGrammarException;
}
