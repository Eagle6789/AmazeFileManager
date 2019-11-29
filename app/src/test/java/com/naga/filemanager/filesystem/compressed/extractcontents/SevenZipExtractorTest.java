package com.naga.filemanager.filesystem.compressed.extractcontents;

import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.SevenZipExtractor;

public class SevenZipExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "7z";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return SevenZipExtractor.class;
    }
}
