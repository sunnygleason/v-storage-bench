package voldemort.xfaban;

import static com.sun.faban.driver.CycleType.THINKTIME;
import static com.sun.faban.driver.Timing.MANUAL;
import static java.util.concurrent.TimeUnit.MICROSECONDS;

import com.sun.faban.driver.BenchmarkDefinition;
import com.sun.faban.driver.BenchmarkDriver;
import com.sun.faban.driver.BenchmarkOperation;
import com.sun.faban.driver.FlatMix;
import com.sun.faban.driver.NegativeExponential;

@BenchmarkDefinition(name = "VoldemortStorageBenchScenario1", version = "1.0")
@BenchmarkDriver(name = "VoldemortStorageBenchScenario1", responseTimeUnit = MICROSECONDS)
@FlatMix(operations = { "Put" }, mix = { 1.0 }, deviation = 1.0)
public class VoldemortStorageBenchScenario1 extends
		VoldemortStorageEngineScenarioBase {
	@BenchmarkOperation(name = "Put", max90th = 1000000, timing = MANUAL)
	@NegativeExponential(cycleType = THINKTIME, cycleMean = 0, cycleDeviation = 0.0)
	public void doPut() throws Exception {
		super.putImpl();
	}
}
