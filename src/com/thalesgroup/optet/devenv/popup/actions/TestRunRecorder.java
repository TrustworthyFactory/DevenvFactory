package com.thalesgroup.optet.devenv.popup.actions;
import java.util.HashMap;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;

/**
 * A TestRunListener that stores all events for later check.
 *
 * <p>
 * All the events are stored chronologically in distinct vectors
 * and are made available as public instances
 * </p>
 *
 */
public class TestRunRecorder    extends TestRunListener  {


	private String testName = null;
	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Result getGlobalTestResult() {
		return globalTestResult;
	}

	public void setGlobalTestResult(Result globalTestResult) {
		this.globalTestResult = globalTestResult;
	}

	public HashMap<String, Result> getResult() {
		return result;
	}

	public void setResult(HashMap<String, Result> result) {
		this.result = result;
	}

	private Result globalTestResult = null;
	private HashMap<String, Result> result = new HashMap<String, Result>();

	@Override
	public void sessionStarted(ITestRunSession session) {
		System.out.println("JUNIT sessionStarted");
	}

	@Override
	public void testCaseFinished(ITestCaseElement testCaseElement) {
		result.put(testCaseElement.getTestMethodName(), testCaseElement.getTestResult(true));
		super.testCaseFinished(testCaseElement);
	}

	@Override
	public void testCaseStarted(ITestCaseElement testCaseElement) {
		// TODO Auto-generated method stub
		super.testCaseStarted(testCaseElement);
	}

	@Override
	public void sessionFinished(ITestRunSession session) {
		System.out.println("JUNIT sessionStarted");
		testName = session.getTestRunName();
		globalTestResult = session.getTestResult(true);
	}
}


