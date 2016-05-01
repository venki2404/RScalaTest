FROM phusion/baseimage

MAINTAINER possibly <georg.heiler@tuwien.ac.at>

RUN echo "deb http://repos.mesosphere.io/ubuntu/ trusty main" > /etc/apt/sources.list.d/mesosphere.list
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv E56151BF

# Install basic packages
RUN \
    apt-get update; apt-get upgrade -y -qq; \
    apt-get install -y -qq wget; \
    apt-get install -y -qq curl; \
    apt-get install -y -qq r-base; \
    apt-get install -y -qq r-cran-rjava; \
    apt-get install -y -qq git; \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Install Oracle Java.
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer && \
  rm -rf /tmp/* && \
  rm -rf /var/tmp/*

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# spark
RUN git clone https://github.com/apache/spark.git
WORKDIR /spark
RUN git checkout v1.6.1
RUN ./dev/change-scala-version.sh 2.11
RUN build/mvn -Pyarn -Phadoop-2.6 -Dhadoop.version=2.6.4 -Dscala-2.11 -Pnetlib-lgpl -Phive -Phive-thriftserver -DskipTests clean package
WORKDIR /

# R
COPY InstallRPackages.R /InstallRPackages.R

RUN R CMD javareconf
RUN Rscript InstallRPackages.R

# spark jobserver
ENV TERM=xterm
ARG MESOS_VERSION=0.28.1-2.0.20.ubuntu1404
ARG SJS_VERSION=v0.6.2

RUN echo "deb http://repos.mesosphere.io/ubuntu/ trusty main" > /etc/apt/sources.list.d/mesosphere.list
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv E56151BF
RUN apt-get update
RUN apt-get install mesos=${MESOS_VERSION} -y -o Dpkg::Options::="--force-confold"
RUN apt-get upgrade -y -o Dpkg::Options::="--force-confold"
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN curl -s https://raw.githubusercontent.com/paulp/sbt-extras/master/sbt > /usr/local/bin/sbt
RUN chmod 0755 /usr/local/bin/sbt

RUN mkdir /app

RUN git clone https://github.com/spark-jobserver/spark-jobserver.git
RUN cd spark-jobserver && git checkout ${SJS_VERSION}

RUN cd spark-jobserver && sbt -q clean update package assembly

RUN cp /spark-jobserver/bin/server_start.sh /app/server_start.sh
RUN cp /spark-jobserver/bin/server_stop.sh /app/server_stop.sh
RUN cp /spark-jobserver/bin/setenv.sh /app/setenv.sh

RUN cp /spark-jobserver/config/log4j-stdout.properties /app/log4j-server.properties
RUN cp /spark-jobserver/config/docker.conf /app/docker.conf
RUN cp /spark-jobserver/config/docker.sh /app/settings.sh
RUN cp /spark-jobserver/job-server-extras/target/scala-2.10/spark-job-server.jar /app/spark-job-server.jar

ENV JOBSERVER_MEMORY=1G
ENV SPARK_HOME=/spark
VOLUME /database

CMD ["/sbin/my_init"]

ENTRYPOINT /app/server_start.sh
EXPOSE 8090
EXPOSE 9999
