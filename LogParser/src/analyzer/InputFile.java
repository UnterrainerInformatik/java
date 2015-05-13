/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package analyzer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class InputFile.
 * <p>
 * This class is used to deserialize the inputFile which is provided as an XML-file.
 * @author GEUNT
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.PROPERTY)
public class InputFile implements Serializable {

    private static final long serialVersionUID    = 6672213928489128159L;

    private List<String>      sourceFiles         = new ArrayList<String>();
    private List<String>      sourceDirsFlat      = new ArrayList<String>();
    private List<String>      sourceDirsRecursive = new ArrayList<String>();
    private String            xPath;

    /**
     * Gets the source files.
     * @return the source files {@link List<String>}
     */
    @XmlElement
    public List<String> getSourceFiles() {
        return sourceFiles;
    }

    /**
     * Sets the source files.
     * @param sourceFiles the new source files
     */
    public void setSourceFiles(final List<String> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    /**
     * Gets the source directories flat.
     * @return the source directories flat {@link List<String>}
     */
    @XmlElement
    public List<String> getSourceDirsFlat() {
        return sourceDirsFlat;
    }

    /**
     * Sets the source directories flat.
     * @param sourceDirsFlat the new source directories flat
     */
    public void setSourceDirsFlat(final List<String> sourceDirsFlat) {
        this.sourceDirsFlat = sourceDirsFlat;
    }

    /**
     * Gets the source directories recursive.
     * @return the source directories recursive {@link List<String>}
     */
    @XmlElement
    public List<String> getSourceDirsRecursive() {
        return sourceDirsRecursive;
    }

    /**
     * Sets the source directories recursive.
     * @param sourceDirsRecursive the new source directories recursive
     */
    public void setSourceDirsRecursive(final List<String> sourceDirsRecursive) {
        this.sourceDirsRecursive = sourceDirsRecursive;
    }

    /**
     * Gets the x path.
     * @return the x path {@link String}
     */
    @XmlElement
    public String getXPath() {
        return xPath;
    }

    /**
     * Sets the x path.
     * @param xPathIn the new x path
     */
    public void setXPath(final String xPathIn) {
        xPath = xPathIn;
    }
}
