package voldemort.xfaban;

import java.util.Iterator;
import java.util.Random;

import voldemort.server.VoldemortConfig;
import voldemort.store.StorageConfiguration;
import voldemort.store.StorageEngine;

import com.g414.dgen.EntityGenerator;
import com.g414.dgen.range.LongRangeBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.sun.faban.driver.BenchmarkDriver;

public class BenchmarkDriverModule extends AbstractModule {

	protected final long seed;

	public BenchmarkDriverModule() {
		this.seed = 0L;
	}

	@Override
	protected void configure() {
		final int klen = Integer.parseInt(System.getProperty("klen", "64"));
		final int vlen = Integer.parseInt(System.getProperty("vlen", "1024"));
		final long min = Long.parseLong(System.getProperty("min", "1"));
		final long max = Long.parseLong(System.getProperty("max", "100000000"));
		final boolean isRandom = Boolean.parseBoolean(System.getProperty(
				"random", "false"));

		Random theRandom = isRandom ? EntityGeneratorProvider.getRandom(seed)
				: null;

		bind(new TypeLiteral<Iterator<Long>>() {
		}).toInstance(
				(new LongRangeBuilder(min, max)).withRandom(theRandom)
						.setRepeating(false).build().iterator());

		bind(EntityGenerator.class).toProvider(
				new EntityGeneratorProvider(klen, vlen, seed));

		try {
			bind(Key.get(Object.class, BenchmarkDriver.class)).to(
					Class.forName(System.getProperty("driver")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			VoldemortConfig config = new VoldemortConfig(System.getProperties());
			Class<StorageConfiguration> clazz = (Class<StorageConfiguration>) Class
					.forName(System.getProperty("storageClass"));

			bind(StorageEngine.class).toInstance(
					clazz.getConstructor(VoldemortConfig.class)
							.newInstance(config)
							.getStore(System.getProperty("storageName")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
