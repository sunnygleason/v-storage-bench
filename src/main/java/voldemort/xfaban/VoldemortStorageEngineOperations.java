package voldemort.xfaban;

import java.util.Map;
import java.util.logging.Logger;

import voldemort.store.StorageEngine;
import voldemort.utils.ByteArray;
import voldemort.versioning.VectorClock;
import voldemort.versioning.Version;
import voldemort.versioning.Versioned;

import com.sun.faban.driver.DriverContext;

public class VoldemortStorageEngineOperations {
	public static void insert(StorageEngine<ByteArray, byte[], ?> store,
			Map<String, Object> entity, VectorClock clock) throws Exception {
		DriverContext.getContext().recordTime();

		try {
			store.put(new ByteArray((byte[]) entity.get("key")),
					new Versioned<byte[]>((byte[]) entity.get("value"), clock),
					null);
		} catch (Exception e) {
			Logger.getAnonymousLogger().severe(e.getMessage());
			e.printStackTrace();

			throw e;
		} finally {
			DriverContext.getContext().recordTime();
		}
	}

	public static void get(StorageEngine<ByteArray, byte[], ?> store,
			Map<String, Object> entity) throws Exception {
		DriverContext.getContext().recordTime();

		try {
			store.get(new ByteArray((byte[]) entity.get("key")), null);
		} catch (Exception e) {
			Logger.getAnonymousLogger().severe(e.getMessage());
			e.printStackTrace();

			throw e;
		} finally {
			DriverContext.getContext().recordTime();
		}
	}

	public static void delete(StorageEngine<ByteArray, byte[], ?> store,
			Map<String, Object> entity, Version version) throws Exception {
		DriverContext.getContext().recordTime();

		try {
			store.delete(new ByteArray((byte[]) entity.get("key")), version);
		} catch (Exception e) {
			Logger.getAnonymousLogger().severe(e.getMessage());
			e.printStackTrace();

			throw e;
		} finally {
			DriverContext.getContext().recordTime();
		}
	}
}
