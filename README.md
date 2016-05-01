# steps to reproduce the problem

0. `docker-compose up` to start the jobserver in version 0.6.2
1. create a hive context `curl -d "" "jobserverUrl:8090/contexts/hive-context?context-factory=spark.jobserver.context.HiveContextFactory"`
2. upload the jar
	1. `sbt assembly`
	2. upload `curl --data-binary @target/scala-2.10/RScalaTest-assembly-0.0.1.SNAPSHOT.jar jobseverUrl:8090/jars/jobs`
	
3. execute the job

This is the error message:

```

```

**But locally it works**

execute `sbt run`

Which should complete successfully. However, for a scala version of 2.10
This is clashing locally with
```
 R.type (with underlying type org.ddahl.rscala.callback.RClient)
[error]  required: ?{def x: ?}
[error] Note that implicit conversions are not applicable because they are ambiguous:
[error]  both method any2Ensuring in object Predef of type [A](x: A)Ensuring[A]
[error]  and method any2ArrowAssoc in object Predef of type [A](x: A)ArrowAssoc[A]
[error]  are possible conversion functions from R.type to ?{def x: ?}
[error]     R.x = x.toArray // send x to R
```
which I could not solve yet.
When using 2.11 
