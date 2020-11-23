package org.opencds.cqf.tooling.processor;

import org.opencds.cqf.individual_tooling.cql_generation.CqlGenerator;
import org.opencds.cqf.individual_tooling.cql_generation.drool.DroolIshCqlGenerator;
import org.opencds.cqf.tooling.parameter.GenerateCQLDroolParameters;
import org.opencds.cqf.tooling.utilities.IOUtils;

public class GenerateCQLDroolProcessor {

	public static void generate(GenerateCQLDroolParameters params) {
		CqlGenerator generator = new DroolIshCqlGenerator(params.outputPath);
		if (params.encoding.equals(IOUtils.Encoding.JSON)) {
			generator.generate(params.encodingFilePath.toString());
		} else {
			throw new IllegalArgumentException("encoding " + params.encoding.toString() + " not supported yet.");
		}
	}

}
