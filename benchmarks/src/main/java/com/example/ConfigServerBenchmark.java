/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import java.io.File;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

@Measurement(iterations = 5)
@Warmup(iterations = 1)
@Fork(value = 2, warmups = 0)
@BenchmarkMode(Mode.AverageTime)
public class ConfigServerBenchmark {

	private static final String CLASSPATH = "BOOT-INF/classes" + File.pathSeparator
			+ "BOOT-INF/lib/*";

//	@Benchmark
//	public void fatJar14x(FatJar14xState state) throws Exception {
//		state.run();
//	}
//
//	@Benchmark
//	public void fatJar15x(FatJar15xState state) throws Exception {
//		state.run();
//	}
//
////	@Benchmark
//	public void fatJar13x(FatJar13xState state) throws Exception {
//		state.run();
//	}
//
//	@Benchmark
//	public void devtoolsRestart(JetDevtoolsState state) throws Exception {
//		state.run();
//	}
//
	@Benchmark
	public void explodedJarMain14x(MainState14x state) throws Exception {
		state.run();
	}

	@Benchmark
	public void explodedJarMain15x(MainState15x state) throws Exception {
		state.run();
	}

	@Benchmark
	public void jetPlain14x(JetPlainState14x state) throws Exception {
		state.run();
	}

//	@Benchmark
//	public void jetBoot14x(JetBootState14x state) throws Exception {
//		state.run();
//	}
//
	@Benchmark
	public void jetPlain15x(JetPlainState15x state) throws Exception {
		state.run();
	}

//	@Benchmark
//	public void jetBoot15x(JetBootState15x state) throws Exception {
//		state.run();
//	}
//
//	@Benchmark
//	public void jetDevtoolsRestart(JetDevtoolsState state) throws Exception {
//		state.run();
//	}
//
	public static void main(String[] args) throws Exception {
		ExplodedDevtoolsState state = new ExplodedDevtoolsState();
		state.setup();
		try {
			while (true) {
				state.run();
			}
		}
		finally {
			state.stop();
		}
	}

	@State(Scope.Benchmark)
	public static class FatJar15xState extends ProcessLauncherState {
		public FatJar15xState() {
			super("target", "-jar",
					jarFile("com.example:configserver:jar:15x:0.0.1-SNAPSHOT"),
					"--server.port=0");
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class FatJar14xState extends ProcessLauncherState {
		public FatJar14xState() {
			super("target", "-jar",
					jarFile("com.example:configserver:jar:14x:0.0.1-SNAPSHOT"),
					"--server.port=0");
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class FatJar13xState extends ProcessLauncherState {
		public FatJar13xState() {
			super("target", "-jar",
					jarFile("com.example:configserver:jar:13x:0.0.1-SNAPSHOT"),
					"--server.port=0");
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class ExplodedDevtoolsState extends DevToolsLauncherState {
		public ExplodedDevtoolsState() {
			super("target/demo", "/BOOT-INF/classes/.restart",
					jarFile("com.example:configserver:jar:14x:0.0.1-SNAPSHOT"), "-cp",
					CLASSPATH, "-Dspring.devtools.livereload.enabled=false",
					"-Dspring.devtools.restart.pollInterval=100",
					"-Dspring.devtools.restart.quietPeriod=10",
					"demo.ConfigServerApplication", "--server.port=0");
		}

		@Override
		@Setup(Level.Trial)
		public void setup() throws Exception {
			super.setup();
		}

		@TearDown(Level.Trial)
		public void stop() throws Exception {
			super.after();
		}
	}

    @State(Scope.Benchmark)
   	public static class JetDevtoolsState extends DevToolsLauncherState {
   		public JetDevtoolsState() {
   			super("target/demo/configserver", "target/demo", ".restart",
					jetZipFile("com.example:configserver:zip:plain-14x:0.0.1-SNAPSHOT"), true, "-Dspring.devtools.livereload.enabled=false",
   					"-Dspring.devtools.restart.pollInterval=100",
   					"-Dspring.devtools.restart.quietPeriod=10",
   					"-Djet.append.classpath.env=true",
   					"demo.ConfigServerApplication", "--server.port=0");
   		}

   		@Override
   		@Setup(Level.Trial)
   		public void setup() throws Exception {
   			super.setup();
   		}

   		@TearDown(Level.Trial)
   		public void stop() throws Exception {
   			super.after();
   		}
   	}

	@State(Scope.Benchmark)
	public static class MainState14x extends ProcessLauncherState {
		public MainState14x() {
			super("target/demo", "-cp", CLASSPATH, "demo.ConfigServerApplication",
					"--server.port=0");
			unpack("target/demo",
					jarFile("com.example:configserver:jar:14x:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class MainState15x extends ProcessLauncherState {
		public MainState15x() {
			super("target/demo", "-cp", CLASSPATH, "demo.ConfigServerApplication",
					"--server.port=0");
			unpack("target/demo",
					jarFile("com.example:configserver:jar:15x:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class JetPlainState14x extends ProcessLauncherState {
		public JetPlainState14x() {
			super("target/demo/configserver", true, "target/demo", "--server.port=0");
			unpack("target/demo", jetZipFile("com.example:configserver:zip:plain-14x:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class JetBootState14x extends ProcessLauncherState {
		public JetBootState14x() {
			super("target/demo/configserver", true, "target/demo", "--server.port=0");
			unpack("target/demo", jetZipFile("com.example:configserver:zip:spring-boot-14x:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class JetPlainState15x extends ProcessLauncherState {
		public JetPlainState15x() {
			super("target/demo/configserver", true, "target/demo", "--server.port=0");
			unpack("target/demo", jetZipFile("com.example:configserver:zip:plain-15x:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class JetBootState15x extends ProcessLauncherState {
		public JetBootState15x() {
			super("target/demo/configserver", true, "target/demo", "--server.port=0");
			unpack("target/demo", jetZipFile("com.example:configserver:zip:spring-boot-15x:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}
}
