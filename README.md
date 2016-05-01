# steps to reproduce the problem

0. `docker-compose up` to start the jobserver in version 0.6.2
1. create a hive context `curl -d "" "jobserverUrl:8090/contexts/hive-context?context-factory=spark.jobserver.context.HiveContextFactory"`
2. upload the jar
	0. for the 2.10 problems (locally) make sure to not set the provided scope in `build.sbt` to keep the size of the assembled jar small use the provided scope when testing on the job server
	1. `sbt assembly`
	2. upload `curl --data-binary @target/scala-2.10/RScalaTest-assembly-0.0.1.SNAPSHOT.jar jobseverUrl:8090/jars/jobs`
	
3. execute the job

This is the error message:

```
[error] C:\ase\RScalaTest\RScalaTest\src\main\scala\myPackage\RScalaTest.scala:3
6: type mismatch;
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

**But locally it works**

execute `sbt run`

# steps to reproduce another problem with scala 2.11

1. start jobserver of version 0.6.2
2. create a hive context `curl -d "" "jobserverUrl:8090/contexts/hive-context?context-factory=spark.jobserver.context.HiveContextFactory"`
3. upload the jar
	1. `sbt assembly`
	2. upload `curl --data-binary @target/scala-2.10/RScalaTest-assembly-0.0.1.SNAPSHOT.jar jobseverUrl:8090/jars/jobs`

Which should complete successfully
