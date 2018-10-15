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

import org.openjdk.jmh.annotations.*;

@Measurement(iterations = 5)
@Warmup(iterations = 1)
@Fork(value = 2, warmups = 0)
@BenchmarkMode(Mode.AverageTime)
public class MinimalBenchmark {

	private static final String CLASSPATH = "BOOT-INF/classes" + File.pathSeparator
			+ "BOOT-INF/lib/*";

	@Benchmark
	public void fatJar(FatJarState state) throws Exception {
		state.run();
	}

	@Benchmark
	public void explodedJarMain(MainState state) throws Exception {
		state.run();
	}

	@Benchmark
	public void jetPlain(JetGlobalState state) throws Exception {
		state.run();
	}

	@Benchmark
	public void jetGlobal(JetGlobalState state) throws Exception {
		state.run();
	}

	@Benchmark
	public void jetBoot(JetBootState state) throws Exception {
		state.run();
	}

	@State(Scope.Benchmark)
	public static class FatJarState extends ProcessLauncherState {
		public FatJarState() {
			super("target", "-jar", jarFile("com.example:minimal:jar:boot:0.0.1-SNAPSHOT"),
					"--server.port=0");
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class MainState extends ProcessLauncherState {
		public MainState() {
			super("target/demo", "-cp", CLASSPATH, "com.example.DemoApplication",
					"--server.port=0");
			unpack("target/demo", jarFile("com.example:minimal:jar:boot:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class JetPlainState extends ProcessLauncherState {

		public JetPlainState() {
			super("target/demo/minimal", true, "target/demo", "--server.port=0");
			unpack("target/demo", jetZipFile("com.example:minimal:zip:plain-boot:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class JetGlobalState extends ProcessLauncherState {

		public JetGlobalState() {
			super("target/demo/minimal", true, "target/demo", "--server.port=0");
			unpack("target/demo", jetZipFile("com.example:minimal:zip:plain-boot-global:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class JetBootState extends ProcessLauncherState {
		public JetBootState() {
			super("target/demo/minimal", true, "target/demo", "--server.port=0");
			unpack("target/demo", jetZipFile("com.example:minimal:zip:spring-boot-boot:0.0.1-SNAPSHOT"));
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}
}
