package com.kangyonggan.gitlab.constants;

import lombok.Getter;

/**
 * @author kyg
 */
public enum FileType {
    /**
     * JEPG.
     */
    JPEG("FFD8FF"),

    /**
     * PNG.
     */
    PNG("89504E47"),

    /**
     * GIF.
     */
    GIF("47494638"),

    /**
     * TIFF.
     */
    TIFF("49492A00"),

    /**
     * RTF.
     */
    RTF("7B5C727466"),

    /**
     * DOC
     */
    DOC("D0CF11E0"),

    /**
     * XLS
     */
    XLS("D0CF11E0"),

    /**
     * ACCESS
     */
    MDB("5374616E64617264204A"),

    /**
     * Windows Bitmap.
     */
    BMP("424D"),

    /**
     * CAD.
     */
    DWG("41433130"),

    /**
     * Adobe Photoshop.
     */
    PSD("38425053"),

    /**
     * XML.
     */
    XML("3C3F786D6C"),

    /**
     * HTML.
     */
    HTML("68746D6C3E"),

    /**
     * Adobe Acrobat.
     */
    PDF("255044462D312E"),

    /**
     * ZIP Archive.
     */
    ZIP("504B0304"),

    /**
     * RAR Archive.
     */
    RAR("52617221"),

    /**
     * Wave.
     */
    WAV("57415645"),

    /**
     * AVI.
     */
    AVI("41564920");

    @Getter
    private String value;

    FileType(String value) {
        this.value = value;
    }
}
