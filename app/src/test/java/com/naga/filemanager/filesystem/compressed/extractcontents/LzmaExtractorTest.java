package com.naga.filemanager.filesystem.compressed.extractcontents;

import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.LzmaExtractor;

public class LzmaExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "tar.lzma";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return LzmaExtractor.class;
    }
}
