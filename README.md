# steps to reproduce the problem

0. `docker-compose up` to start the jobserver in version 0.6.2
1. create a hive context `curl -d "" "jobserverUrl:8090/contexts/hive-context?context-factory=spark.jobserver.context.HiveContextFactory"`
2. upload the jar
	1. `sbt assembly`
	2. upload `curl --data-binary @target/scala-2.10/RScalaTest-assembly-0.0.1.SNAPSHOT.jar jobseverUrl:8090/jars/jobs`
	
3. execute the job

This is the error message from the server when using 2.11 scala version:

```
java.lang.NoSuchMethodError: scala.reflect.api.JavaUniverse.runtimeMirror(Ljava/lang/ClassLoader;)Lscala/reflect/api/JavaUniverse$JavaMirror;
    at myPackage.RScalaTest$.executePayload(RScalaTest.scala:24)
    at myPackage.RScalaTest$.runJob(RScalaTest.scala:18)
    at myPackage.RScalaTest$.runJob(RScalaTest.scala:13)
    at spark.jobserver.JobManagerActor$$anonfun$spark$jobserver$JobManagerActor$$getJobFuture$4.apply(JobManagerActor.scala:301)
    at scala.concurrent.impl.Future$PromiseCompletingRunnable.liftedTree1$1(Future.scala:24)
    at scala.concurrent.impl.Future$PromiseCompletingRunnable.run(Future.scala:24)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
    at java.lang.Thread.run(Thread.java:745)
```

For 2.10 scala we get the following error which we could not resolve and thus triggered us to use scala 2.11

```
[error]  found   : R.type (with underlying type org.ddahl.rscala.callback.RClien
t)
[error]  required: ?{def x: ?}
[error] Note that implicit conversions are not applicable because they are ambig
uous:
[error]  both method any2Ensuring in object Predef of type [A](x: A)Ensuring[A]
[error]  and method any2ArrowAssoc in object Predef of type [A](x: A)ArrowAssoc[
A]
[error]  are possible conversion functions from R.type to ?{def x: ?}
[error]     R.x = x.toArray // send x to R
[error]     ^
[error] one error found
[error] (compile:compileIncremental) Compilation failed
[error] Total time: 25 s, completed 01.05.2016 19:44:09
```

**Locally it works for a 2.11 scala**

execute `sbt run`, make sure that the provided scope is NOT set
This should complete successfully
