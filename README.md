# steps to reproduce the problem

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

Which should complete successfully
