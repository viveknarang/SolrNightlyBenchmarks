package org.apache.solr.tests.nightlybenchmarks;
import java.io.IOException;

public class SolrNightlyBenchmarks {

	public static void main(String[] args) throws IOException, InterruptedException {

		Util.init(args);
		
		Tests.indexingTests(Util.COMMIT_ID, 10000);
		
		//Tests.setUpCloudForFeatureTests(Util.COMMIT_ID, 50000, 5, "2", "2");
		//BenchmarkReportData.numericQueryTNQMetric = Tests.numericQueryTests(commitID, NumericQueryType.TERM_NUMERIC_QUERY, 1, 180, 120);
		//BenchmarkReportData.numericQueryRNQMetric =	Tests.numericQueryTests(commitID, NumericQueryType.RANGE_NUMERIC_QUERY, 1, 180, 120);
		//BenchmarkReportData.numericQueryLNQMetric =	Tests.numericQueryTests(commitID, NumericQueryType.LESS_THAN_NUMERIC_QUERY, 1, 180, 120);
		//BenchmarkReportData.numericQueryGNQMetric =	Tests.numericQueryTests(commitID, NumericQueryType.GREATER_THAN_NUMERIC_QUERY, 1, 180, 120);
		//Tests.shutDown();
		//OutPort.publishDataForWebApp();

		Util.destroy();
	}
}