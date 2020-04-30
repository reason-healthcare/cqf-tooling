package org.opencds.cqf.processor.argument;

import static java.util.Arrays.asList;

import org.opencds.cqf.parameter.RefreshLibraryParameters;
import org.opencds.cqf.processor.IGProcessor;
import org.opencds.cqf.processor.IGProcessor.IGVersion;
import org.opencds.cqf.utilities.ArgUtils;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.OptionSpecBuilder;
import org.opencds.cqf.utilities.IOUtils.Encoding;


public class RefreshLibraryArgumentProcessor {
    public static final String[] OPERATION_OPTIONS = {"RefreshLibrary"};

    public static final String[] IG_CANONICAL_BASE = {"igcb", "igCanonicalBase"};
    public static final String[] CQL_PATH_OPTIONS = {"cql", "content", "cqlPath", "cqlContentPath", "contentPath", "cp"};
    public static final String[] Library_PATH_OPTIONS = {"library", "libraryPath", "resourcePath", "lp", "cp"};
    public static final String[] FHIR_VERSION_OPTIONS = {"fv", "fhir-version"};
    public static final String[] OUTPUT_ENCODING = {"e", "encoding"};
    public static final String[] VERSIONED_OPTIONS = {"v", "versioned"};

    public OptionParser build() {
        OptionParser parser = new OptionParser();

        OptionSpecBuilder igCanonicalBaseBuilder = parser.acceptsAll(asList(IG_CANONICAL_BASE),"resource canonical base");
        OptionSpecBuilder cqlPathBuilder = parser.acceptsAll(asList(CQL_PATH_OPTIONS),"Library will be created in the same folder as the cql");
        OptionSpecBuilder libraryPathBuilder = parser.acceptsAll(asList(Library_PATH_OPTIONS),"If omitted, the library will be created in the same folder as the cql");
        OptionSpecBuilder fhirVersionBuilder = parser.acceptsAll(asList(FHIR_VERSION_OPTIONS),"Limited to a single version of FHIR.");
        OptionSpecBuilder outputEncodingBuilder = parser.acceptsAll(asList(OUTPUT_ENCODING), "If omitted, output will be generated using JSON encoding.");

        OptionSpec<String> igCanonicalBasePath = igCanonicalBaseBuilder.withOptionalArg().describedAs("resource canonical base");
        OptionSpec<String> cqlPath = cqlPathBuilder.withRequiredArg().describedAs("path to the cql content");
        OptionSpec<String> libraryPath = libraryPathBuilder.withOptionalArg().describedAs("path to the library");
        OptionSpec<String> fhirVersion = fhirVersionBuilder.withRequiredArg().describedAs("fhir version");
        OptionSpec<String> outputEncoding = outputEncodingBuilder.withOptionalArg().describedAs("desired output encoding for resources");

        parser.acceptsAll(asList(OPERATION_OPTIONS),"The operation to run.");
        parser.acceptsAll(asList(VERSIONED_OPTIONS),"If omitted resources must be uniquely named.");

        OptionSpec<Void> help = parser.acceptsAll(asList(ArgUtils.HELP_OPTIONS), "Show this help page").forHelp();

        return parser;
    }

    public RefreshLibraryParameters parseAndConvert(String[] args) {
        OptionParser parser = build();
        OptionSet options = ArgUtils.parse(args, parser);

        ArgUtils.ensure(OPERATION_OPTIONS[0], options);

        String igCanonicalBase = (String)options.valueOf(IG_CANONICAL_BASE[0]);
        String cqlPath = (String)options.valueOf(CQL_PATH_OPTIONS[0]);
        String fhirVersion = (String)options.valueOf(FHIR_VERSION_OPTIONS[0]);
        String encoding = (String)options.valueOf(OUTPUT_ENCODING[0]);
        String libraryPath = (String)options.valueOf(Library_PATH_OPTIONS[0]);
        if (libraryPath == null) {
            libraryPath = "";
        }
        Encoding outputEncodingEnum = Encoding.JSON;
        if (encoding != null) {
            outputEncodingEnum = Encoding.parse(encoding.toLowerCase());
        }
        Boolean versioned = options.has(VERSIONED_OPTIONS[0]);
    
        RefreshLibraryParameters lp = new RefreshLibraryParameters();
        lp.igCanonicalBase = igCanonicalBase;
        lp.cqlContentPath = cqlPath;
        lp.fhirContext = IGProcessor.getIgFhirContext(IGVersion.parse(fhirVersion));
        lp.encoding = outputEncodingEnum;
        lp.versioned = versioned;
        lp.libraryPath = libraryPath;
       
        return lp;
    }
}