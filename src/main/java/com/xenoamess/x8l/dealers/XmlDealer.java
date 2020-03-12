/*
 * MIT License
 *
 * Copyright (c) 2019 XenoAmess
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

import com.xenoamess.x8l.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.tree.DefaultComment;
import org.dom4j.tree.DefaultText;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static com.xenoamess.x8l.dealers.JsonDealer.ARRAY_ID_ATTRIBUTE;

/**
 * Read and output xml.
 * Notice that I can not ensure the output is really an xml, because x8l has loosen rules on things,
 * And when convert, it might not suit xml's rule.
 * Thus if you want to convert your x8l file into an xml, please does not use some behavior that xml does not allow.
 * like nameless node.
 * <p>
 * We will try to auto fix some of the errors, but thus we cannot ensure output and then input can lead to same tree.
 * <p>
 * But if you convert your xml file into x8l then convert it back, we try to ensure the output is equal to original xml.
 * <p>
 * Also, this dealer can only read in some naive xml, but not complicated one with DTD or XLS or something.
 * <p>
 * I use dom4j for reading xml now. No interest in rewriting it.
 * <p>
 * If anybody want to refine it, pull request is always welcomed.
 *
 * @author XenoAmess
 */
public final class XmlDealer extends LanguageDealer implements Serializable {
    /*
     * no need to build more XmlDealer instances.
     * please just use XmlDealer.INSTANCE
     * if you want to extend it,
     * please just copy the codes and make your own AbstractLanguageDealer class.
     */
    private XmlDealer() {
        this.registerTreeNodeHandler(
                RootNode.class,
                new AbstractLanguageDealerHandler<RootNode>() {
                    @Override
                    public boolean read(Reader reader, RootNode rootNode) throws IOException, X8lGrammarException {
                        try {
                            Document document = DocumentHelper.parseText(IOUtils.toString(reader));
                            if (document.nodeCount() == 1 && document.node(0) instanceof Element && document.node(0).getName().equals(STRING_MAMELESS)) {
                                XmlDealer.this.readChildrenArea(rootNode, (Element) document.node(0));
                            } else {
                                XmlDealer.this.readChildrenArea(rootNode, document);
                            }
                        } catch (DocumentException e) {
                            throw new IOException(e);
                        }
                        return true;
                    }

                    @Override
                    public boolean write(Writer writer, RootNode rootNode) throws IOException, X8lGrammarException {
                        Document document = DocumentHelper.createDocument();
                        Element element = document.addElement(STRING_MAMELESS);
                        if (rootNode.getChildren().size() == 1 && rootNode.getChildren().get(0) instanceof ContentNode && "".equals(rootNode.getName())) {
                            XmlDealer.this.write((ContentNode) rootNode.getChildren().get(0), element);
                        } else {
                            XmlDealer.this.write(rootNode, element);
                        }
                        document.write(writer);
                        return true;
                    }
                }
        );


        this.registerTreeNodeHandler(
                ContentNode.class,
                new AbstractLanguageDealerHandler<ContentNode>() {
                    @Override
                    public boolean read(Reader reader, ContentNode contentNode) throws
                            IOException, X8lGrammarException {
                        try {
                            Document document = DocumentHelper.parseText(IOUtils.toString(reader));
                            XmlDealer.this.read(contentNode, document.getRootElement());
                        } catch (DocumentException e) {
                            throw new IOException(e);
                        }
                        return true;
                    }

                    @Override
                    public boolean write(Writer writer, ContentNode contentNode) throws
                            IOException, X8lGrammarException {
                        Document document = DocumentHelper.createDocument();
                        Element element = document.addElement(STRING_MAMELESS);
                        XmlDealer.this.write(contentNode, element);
                        document.write(writer);
                        return true;
                    }
                }
        );

        this.registerTreeNodeHandler(
                TextNode.class,
                new AbstractLanguageDealerHandler<TextNode>() {
                    @Override
                    public boolean read(Reader reader, TextNode textNode) throws IOException, X8lGrammarException {
                        return false;
                    }

                    @Override
                    public boolean write(Writer writer, TextNode textNode) throws IOException, X8lGrammarException {
                        Document document = DocumentHelper.createDocument();
                        Element element = document.addElement(STRING_MAMELESS);
                        element.addText(textNode.getTextContent());
                        document.write(writer);
                        return true;
                    }
                }
        );

        this.registerTreeNodeHandler(
                CommentNode.class,
                new AbstractLanguageDealerHandler<CommentNode>() {
                    @Override
                    public boolean read(Reader reader, CommentNode commentNode) throws
                            IOException, X8lGrammarException {
                        return false;
                    }

                    @Override
                    public boolean write(Writer writer, CommentNode commentNode) throws
                            IOException, X8lGrammarException {
                        Document document = DocumentHelper.createDocument();
                        Element element = document.addElement(STRING_MAMELESS);
                        element.addComment(commentNode.getTextContent());
                        document.write(writer);
                        return true;
                    }
                }
        );
    }


    public static final String STRING_MAMELESS = "_nameless";

    public static final XmlDealer INSTANCE = new XmlDealer();

    /**
     * delete attributes which contains illegal char in their key.
     * "illegal" here means illegal to xml, not for x8l.
     * for example, [ is illegal.(JsonDealer.ARRAY_ID_ATTRIBUTE)
     *
     * @param source original Strings to filer
     * @return filtered Strings
     */
    public static List<String> filterIllegalChars(List<String> source) {
        List<String> res = new ArrayList<>();
        for (String au : source) {
            boolean add = !au.contains(ARRAY_ID_ATTRIBUTE);
            if (add) {
                for (char c : au.toCharArray()) {
                    if ((c >= 0x00 && c <= 0x08)
                            || (c >= 0x0b && c <= 0x0c)
                            || (c >= 0x0e && c <= 0x1f)) {
                        add = false;
                        break;
                    }
                }
            }
            if (add) {
                res.add(au);
            }
        }
        return res;
    }

    private static void write(ContentNode contentNode, Element element) {
        boolean firstAttribute = true;
        String nodeName;
        boolean nodeNameless = false;

        if (ifNameless(contentNode)) {
            nodeName = STRING_MAMELESS;
            nodeNameless = true;
        } else {
            nodeName = contentNode.getAttributesKeyList().get(0);
        }

        if (nodeNameless) {
            //if nameless then give it a name.
            element.setName(nodeName);
            firstAttribute = false;
        }

        for (String key : contentNode.getAttributesKeyList()) {
            if (ARRAY_ID_ATTRIBUTE.equals(key)) {
                continue;
            }
            String value = contentNode.getAttributes().get(key);

            if (!StringUtils.isEmpty(value)) {
                element.addAttribute(key, value);
            } else {
                if (!firstAttribute) {
                    element.addAttribute(key, "");
                } else {
                    element.setName(key);
                }
            }
            firstAttribute = false;
        }
        writeChildrenArea(contentNode, element);
    }

    private static void writeChildrenArea(ContentNode contentNode, Element element) {
        for (AbstractTreeNode au : contentNode.getChildren()) {
            if (au instanceof ContentNode) {
                write((ContentNode) au, element.addElement(STRING_MAMELESS));
            } else if (au instanceof TextNode) {
                TextNode textNode = (TextNode) au;
                element.addText(textNode.getTextContent());
            } else if (au instanceof CommentNode) {
                CommentNode commentNode = (CommentNode) au;
                element.addComment(commentNode.getTextContent());
            } else {
                throw new NotImplementedException("not implemented for this class : " + au.getClass());
            }
        }
    }

    //    @Override
    private static void read(ContentNode contentNode, Element element) {
        if (!STRING_MAMELESS.equals(element.getName())) {
            contentNode.addAttribute(element.getName());
        }
        for (Attribute attribute : element.attributes()) {
            contentNode.addAttribute(attribute.getName(), attribute.getValue());
        }
        readChildrenArea(contentNode, element);
    }

    private static void readChildrenArea(ContentNode contentNode, Branch branch) {
        for (int i = 0, size = branch.nodeCount(); i < size; i++) {
            Node node = branch.node(i);
            if (node instanceof Element) {
                ContentNode childContentNode = new ContentNode(contentNode);
                read(childContentNode, (Element) node);
            } else if (node instanceof DefaultText) {
                new TextNode(contentNode, node.getText());
            } else if (node instanceof DefaultComment) {
                new CommentNode(contentNode, node.getText());
            } else {
                System.err.println("Cannot handle this node type: " + node.getClass().getCanonicalName() + " . Will treat it as TextNode.");
                new TextNode(contentNode, node.getText());
            }
        }
    }

    public static boolean ifNameless(ContentNode contentNode) {
        if (contentNode.getAttributesKeyList().isEmpty()) {
            return true;
        }
        String nodeName = contentNode.getAttributesKeyList().get(0);
        if (!StringUtils.isEmpty(contentNode.getAttributes().get(nodeName))) {
            //if "name" has value then it is nameless.
            return true;
        }
        return false;
    }

    /**
     * My naive implement to read xml file.
     * It is outdated and far limited,
     * and should not be used anymore now.
     *
     * @param writer   writer
     * @param treeNode treeNode
     * @throws IOException IOException
     */
    @Deprecated
    public void naiveWrite(Writer writer, AbstractTreeNode treeNode) throws IOException {
        if (treeNode instanceof ContentNode) {
            ContentNode contentNode = (ContentNode) treeNode;
            writer.append('<');
            boolean firstAttribute = true;

            String nodeName;
            boolean nodeNameless = false;

            if (ifNameless(contentNode)) {
                nodeName = STRING_MAMELESS;
                nodeNameless = true;
            } else {
                nodeName = contentNode.getAttributesKeyList().get(0);
            }

            if (nodeNameless) {
                //if nameless then give it a name.
                writer.append(nodeName);
                if (!contentNode.getAttributesKeyList().isEmpty()) {
                    writer.append(' ');
                }
                firstAttribute = false;
            }

            for (String key : contentNode.getAttributesKeyList()) {
                if (!firstAttribute) {
                    writer.append(' ');
                }
                writer.append(X8lTree.transcodeKey(key));
                String value = contentNode.getAttributes().get(key);

                if (!StringUtils.isEmpty(value)) {
                    writer.append('=');
                    writer.append('"');
                    writer.append(X8lTree.transcodeValue(value));
                    writer.append('"');
                } else {
                    if (!firstAttribute) {
                        writer.append("=\"\"");
                    }
                }

                firstAttribute = false;
            }
            if (!contentNode.getChildren().isEmpty()) {
                writer.append('>');
            }
            for (AbstractTreeNode abstractTreeNode : contentNode.getChildren()) {
                abstractTreeNode.write(writer, this);
            }
            if (contentNode.getChildren().isEmpty()) {
                writer.append("/>");
            } else {
                writer.append("</");
                writer.append(nodeName);
                writer.append(">");
            }
        } else if (treeNode instanceof TextNode) {
            TextNode textNode = (TextNode) treeNode;
            writer.append(X8lTree.transcodeText(textNode.getTextContent()));
        } else if (treeNode instanceof CommentNode) {
            CommentNode commentNode = (CommentNode) treeNode;
            writer.append("<!--");
            writer.append(X8lTree.transcodeComment(commentNode.getTextContent()));
            writer.append("-->");
        } else {
            throw new NotImplementedException("not implemented for this class : " + treeNode.getClass());
        }
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName();
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
