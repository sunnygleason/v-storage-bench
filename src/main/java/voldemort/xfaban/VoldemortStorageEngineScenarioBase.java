package voldemort.xfaban;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import voldemort.store.StorageEngine;
import voldemort.versioning.VectorClock;

import com.g414.dgen.EntityGenerator;
import com.google.inject.Inject;

public class VoldemortStorageEngineScenarioBase {
	@Inject
	protected StorageEngine store;

	@Inject
	protected Iterator<Long> keyRange;

	@Inject
	protected EntityGenerator entityGen;

	protected AtomicLong serial = new AtomicLong(1);

	protected Random random = new Random();

	protected volatile long maxId = 0;

	public void putImpl() throws Exception {
		Long nextLong = keyRange.next();
		String nextId = nextLong.toString();
		final Map<String, Object> entity = entityGen.getEntity(nextId);

		VoldemortStorageEngineOperations.insert(store, entity, new VectorClock(
				serial.getAndIncrement()));

		this.maxId = nextLong;
	}

	public void getImpl() throws Exception {
		if (this.maxId < 1000) {
			putImpl();
			return;
		}

		String nextId = Long.toString(Math.abs(random.nextLong())
				% (this.maxId - 500));
		final Map<String, Object> entity = entityGen.getEntity(nextId);

		VoldemortStorageEngineOperations.get(store, entity);
	}

	public void updateImpl() throws Exception {
		if (this.maxId < 1000) {
			putImpl();
			return;
		}

		String nextId = Long.toString(Math.abs(random.nextLong())
				% (this.maxId - 500));
		final Map<String, Object> entity = entityGen.getEntity(nextId);

		VoldemortStorageEngineOperations.insert(store, entity, new VectorClock(
				serial.getAndIncrement()));
	}

	public void deleteImpl() throws Exception {
		if (this.maxId < 1000) {
			putImpl();
			return;
		}

		String nextId = Long.toString(Math.abs(random.nextLong())
				% (this.maxId - 500));
		final Map<String, Object> entity = entityGen.getEntity(nextId);

		VoldemortStorageEngineOperations.delete(store, entity, new VectorClock(
				serial.getAndIncrement()));
	}
}
