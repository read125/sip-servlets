rm ./logs/*
java -Djava.util.logging.config.file=./lb-logging.properties -jar ${M2_REPO}/org/mobicents/tools/sip-balancer/1.0.BETA8-SNAPSHOT/sip-balancer-1.0.BETA8-SNAPSHOT-jar-with-dependencies.jar -mobicents-balancer-config=lb-configuration.properties -Xms1024m -Xmx1024m -XX:PermSize=128M -XX:MaxPermSize=256M -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode
